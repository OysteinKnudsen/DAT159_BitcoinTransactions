package no.hvl.dat159;

import javax.swing.text.html.HTMLDocument;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UTXO {
    
    //Why is this a Map and not a Set?
    //  The values in this map are the UTXOs (unspent Outputs)
    //  When removing UTXOs, we need to identify which to remove.
    //  Since the Inputs are references to UTXOs, we can use those
    //  as keys.


	private Map<Input, Output> map = new HashMap<>();
	
	public void printUTXO() {
		System.out.println("UTXO");

		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()){
			Map.Entry pair = (Map.Entry)iterator.next();
			System.out.println(pair.getKey().toString() + "\n -->" + pair.getValue().toString());
		}

	}
	
	public void addOutputFrom(CoinbaseTx ctx) {
	    map.put(new Input(null, -1), ctx.getOutput());
	}

    public void addAndRemoveOutputsFrom(Transaction tx) {
        for(Output output : tx.getOutputs()) {
            this.map.put(new Input(tx.getTxHash(), tx.getOutputs().indexOf(output)), output);
        }

	    for(Input input : tx.getInputs()) {
            this.map.remove(input);
        }
    }

    public Map<Input, Output> getUTXOMap(){
		return map;
	}
}
