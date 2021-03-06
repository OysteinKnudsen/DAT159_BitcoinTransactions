package no.hvl.dat159;

import java.util.ArrayList;
import java.util.List;

public class Application {
    
    private static UTXO utxo = new UTXO();
    private static int blockNumber = 0;
	
	public static void main(String[] args) throws Exception {
	    
        /*
         * In this assignment, we are going to look at how to represent and record
         * monetary transactions. We will use Bitcoin as the basis for the assignment,
         * but there will be some simplifications.
         * 
         * We are skipping the whole blockchain this time, and instead focus on the
         * transaction details, the UTXOs and how money movements are represented.
         * 
         * (If you want to, you can of course extend the assignment by collecting the
         * individual transactions into blocks, create a Merkle tree for the block
         * header, validate, mine and add the block to a blockchain.)
         * 
         */

        // 0. To get started, we need a few (single address) Wallets. Create 2 wallets.
        //    Think of one of them as the "miner" (the one collecting "block rewards").
//        UTXO utxo = new UTXO();
        Wallet minerWallet = new Wallet("SomeAddress", utxo);
        Wallet myWallet = new Wallet("someAdress2", utxo);

        // 1. The first "block" (= round of transactions) contains only a coinbase
        //    transaction. Create a coinbase transaction that adds a certain
        //    amount to the "miner"'s address. Update the UTXO-set (add only).
        CoinbaseTx firstBlockCoinbase = new CoinbaseTx("genesis", 10, minerWallet.getAddress());
        utxo.addOutputFrom(firstBlockCoinbase);

        // 2. The second "block" contains two transactions, the mandatory coinbase
        //    transaction and a regular transaction. The regular transaction shall
        //    send ~20% of the money from the "miner"'s address to the other address.
        createRegularBlock(minerWallet, myWallet, "Hallo på do!");


        // 3. Do the same once more. Now, the "miner"'s address should have two or more
        //    unspent outputs (depending on the strategy for choosing inputs) with a
        //    total of 2.6 * block reward, and the other address should have 0.4 ...
        //    Validate the regular transaction ..
        //    Update the UTXO-set ...
        createRegularBlock(minerWallet, myWallet, "Hallo fra do!");

        // 4. Make a nice print-out of all that has happened, as well as the end status.
        //
        //      for each of the "block"s (rounds), print
        //          "block" number
        //          the coinbase transaction
        //              hash, message
        //              output
        //          the regular transaction(s), if any
        //              hash
        //              inputs
        //              outputs
        //      End status: the set of unspent outputs
        //      End status: for each of the wallets, print
        //          wallet id, address, balance
        utxo.printUTXO();
        System.out.println(myWallet.toString());
        System.out.println(minerWallet.toString());
	}

    private static void createRegularBlock(Wallet minerWallet, Wallet myWallet, String message) throws Exception {
        CoinbaseTx coinbaseTx = new CoinbaseTx(message, 10, minerWallet.getAddress());
        utxo.addOutputFrom(coinbaseTx);
        Transaction regularTx = minerWallet.
                createTransaction(Math.round(minerWallet.getBalance() * 0.2D), myWallet.getAddress());

        //    Validate the regular transaction created by the "miner"'s wallet:
        if (!regularTx.isValid())
            throw new Exception("Not valid block!");

        //      - All the inputs are unspent and belongs to the sender
        for (Input input : regularTx.getInputs()) {
            if (!utxo.getUTXOMap().containsKey(input))
                throw new Exception("Not valid block!");
            if (!utxo.getUTXOMap().get(input).getAddress().equals(minerWallet.getAddress()))
                throw new Exception("Not valid block!");
        }

        //      - There are no repeating inputs!!!
        List<String> prevTxHashes = new ArrayList<>();
        for (Input input : regularTx.getInputs()) {
            if (prevTxHashes.contains(input.getPrevTxHash()))
                throw new Exception("Not valid block!");
            prevTxHashes.add(input.getPrevTxHash());
        }

        //      - All the outputs must have a value > 0
        for (Output output : regularTx.getOutputs()) {
            if (output.getValue() <= 0)
                throw new Exception("Not valid block!");
        }

        //      - The sum of inputs equals the sum of outputs
        long inputSum = 0;
        for (Input input : regularTx.getInputs())
            inputSum += utxo.getUTXOMap().get(input).getValue();

        long outputSum = 0;
        for (Output output : regularTx.getOutputs())
            outputSum += output.getValue();


        if (inputSum != outputSum)
            throw new Exception("Not valid block!");

        //      - The transaction is correctly signed by the sender
        if(!DSAUtil.verifyWithDSA(minerWallet.getPublicKey(), regularTx.getMessage(), regularTx.getSignature()))
            throw new Exception("Not valid block!");


        //      - The transaction hash is correct
        String newTxHash = HashUtil.base64Encode(HashUtil.sha256Hash(regularTx.getMessage()));
        if (!regularTx.getTxHash().equals(newTxHash))
            throw new Exception("Not valid block!");

        //    Update the UTXO-set (both add and remove).
        utxo.addAndRemoveOutputsFrom(regularTx);

        System.out.println("Block number " + blockNumber++ + ":");
        System.out.println(coinbaseTx.toString());
        System.out.println(regularTx.toString());
        System.out.println();

    }
}
