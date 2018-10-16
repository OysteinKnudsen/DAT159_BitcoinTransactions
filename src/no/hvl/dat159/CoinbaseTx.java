package no.hvl.dat159;

public class CoinbaseTx {
	
    //Simplified compared to Bitcoin (nothing significant missing)
	private String message; // "The Times 03/Jan/2009 Chancellor
	                         //  on brink of second bailout for banks"
	private Output output;
	private String txHash;

	/*
	* 	CoinBase transaction
	*
	*  - Can only be created by a miner.
	*  - Has no inputs.
	*  - One message transaction is created with each new block that is mined on the network.
	*  - THE TRANSACTION THAT REWARDS A MINER WITH THE BLOCK REWARD FOR THEIR WORK.
	*  - In most cases the first transaction in a new block.
	*
	* */

	public CoinbaseTx(String message, int value, String address) {
		this.message = message;
		this.txHash = HashUtil.base64Encode(HashUtil.sha256Hash(message));
		output = new Output(value,address);
	}
	
	@Override
	public String toString() {
		return "CoinbaseTx( " + txHash + " ) \n Message = " + message + " Output = " + output.toString();
	}
	
	//TODO Getters?
	
}
