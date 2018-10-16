package no.hvl.dat159;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import no.hvl.dat159.DSAUtil;

public class Transaction {

    //Simplified compared to Bitcoin
	private List<Input> inputs = new ArrayList<>();
	private List<Output> outputs = new ArrayList<>();
	
	//If we make the assumption that all the inputs belong to the
	//same key, we can have one signature for the entire transaction, 
	//and not one for each input. This simplifies things a lot 
	//(more than you think)!

	private PublicKey senderPublicKey;
	private byte[] signature;
	private String txHash; //Unique ID
	
	public Transaction(PublicKey senderPublicKey) {
		this.senderPublicKey = senderPublicKey;


	}
	
	public void addInput(Input input) {
		inputs.add(input);
	}
	
	public void addOutput(Output output) {
		outputs.add(output);
	}
	
	@Override
	public String toString() {
		return "Transaction( " + txHash + " ) \n Inputs = " + getInputs() + " Outputs = " + getOutputs();
	}

	public void signTxUsing(PrivateKey privateKey) {
	    // TODO
	}

	public void calculateTxHash() {

	}
	
	public boolean isValid() {
	    //TODO Complete validation of the transaction. Called by the Application.
	    return true;
	}

	private String getInputs () {
		String inputString = "";
		for (Input input : inputs){
			inputString += input.toString() + "\n";
		}
		return inputString;
	}

	private String getOutputs() {
		String outputString = "";
		for (Output output : outputs){
			outputString += output.toString() + "\n";
		}
		return outputString;
	}
	
   //TODO Getters?

}
