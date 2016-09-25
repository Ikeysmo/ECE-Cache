
public class psuedoLRUCache extends GenericCache {
	private node[] cache; 
	private int numLevels;
	
	public psuedoLRUCache(int BLOCKSIZE, int SIZE, int ASSOC, int REPL_POLICY, int INCLUSION) throws InterruptedException {
		super(BLOCKSIZE, SIZE, ASSOC, REPL_POLICY, INCLUSION);
		cache = new node[numSets]; //yeah boy
		numLevels = (int) (Math.log10(numWays)/Math.log10(2));
		System.out.println("Num of levels: " + numLevels);
		for(int i = 0; i < cache.length; i++){
			cache[i] = new node(numLevels); //each node makes nodes makes i-1 nodes until 0
		}
		System.out.println("DIDn'tBREAK!");
		Thread.sleep(5000);
	}

	@Override
	public void handleTransaction(String address, char operation) {
		long mem_address = Long.decode("0x" + address.trim());
		long current_tag = mem_address >>> (index_width + offset_width);
		int index =(int)((mem_address >>> offset_width) & ((~0)>>> (64-index_width)));
		// now find if tag hit
		long[] cache_set = cache[index].getAllElements();
		System.out.println("Current tag: " + Long.toHexString(current_tag));
		System.out.println("Index: " + index);
		System.out.print("[ ");
		for(int i = 0; i < cache_set.length; i++){
			System.out.print(Long.toHexString(cache_set[i]) + "  | ");
		}
		System.out.println("]\n");
		if(operation == 'w'){
			if(cache[index].findEqualTag(current_tag, element.DIRTY, operation))
				return;
			writeMisses++;
			if(cache[index].findEmptySlot(current_tag, element.DIRTY))
				return;
			else {
				if(cache[index].accessChild(current_tag, element.DIRTY)) //automatically find and replace
					writebacks++;
			}
		}

		else{ //if read
			if(cache[index].findEqualTag(current_tag, element.VALID, operation))
				return;
			readMisses++;
			if(cache[index].findEmptySlot(current_tag, element.VALID))
				return;
			else{
				if(cache[index].accessChild(current_tag, element.VALID)) //automatically find and replace
					writebacks++;
			}
		}

	}

}
