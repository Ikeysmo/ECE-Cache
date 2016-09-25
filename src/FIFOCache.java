
public class FIFOCache extends GenericCache {
	private int[] counter;
	public FIFOCache(int BLOCKSIZE, int SIZE, int ASSOC, int REPL_POLICY, int INCLUSION) throws InterruptedException {
		super(BLOCKSIZE, SIZE, ASSOC, REPL_POLICY, INCLUSION);
		counter = new int[numSets];
		for (int i = 0; i < counter.length; i++)
			counter[i] = 0;
	}

	@Override
	public void handleTransaction(String address, char operation) {
		long mem_address = Long.decode("0x" + address.trim());
		long current_tag = mem_address >>> (index_width + offset_width);
		int index =(int)((mem_address >>> offset_width) & ((~0)>>> (64-index_width)));
		System.out.println("Before:\n+---------------+---------------+--------------+--------------+---------------+---------------+--------------+--------------+");
		System.out.println("| " +  Long.toHexString(cache[index][0].tag) +" |  " +Long.toHexString(cache[index][1].tag) +" | "+Long.toHexString(cache[index][2].tag)+"  |  " + Long.toHexString(cache[index][3].tag)+" |" + "| " +  Long.toHexString(cache[index][4].tag) +" |  " +Long.toHexString(cache[index][5].tag) +" | "+Long.toHexString(cache[index][6].tag)+"  |  " + Long.toHexString(cache[index][7].tag)+" |");
		System.out.println("+---------------+---------------+--------------+--------------+---------------+---------------+--------------+--------------+");
		System.out.println("Index: " + Long.toString(index));
		System.out.println("Current tag: " + Long.toHexString(current_tag));
		if(lookForMatch(index, current_tag, operation)){
			System.out.println("After:\n+---------------+---------------+--------------+--------------+---------------+---------------+--------------+--------------+");
			System.out.println("| " +  Long.toHexString(cache[index][0].tag) +" |  " +Long.toHexString(cache[index][1].tag) +" | "+Long.toHexString(cache[index][2].tag)+"  |  " + Long.toHexString(cache[index][3].tag)+" |" + "| " +  Long.toHexString(cache[index][4].tag) +" |  " +Long.toHexString(cache[index][5].tag) +" | "+Long.toHexString(cache[index][6].tag)+"  |  " + Long.toHexString(cache[index][7].tag)+" |");
			System.out.println("+---------------+---------------+--------------+--------------+---------------+---------------+--------------+--------------+");
			return;}
		if(lookForEmpty(index, current_tag, operation)){
			System.out.println("After:\n+---------------+---------------+--------------+--------------+---------------+---------------+--------------+--------------+");
			System.out.println("| " +  Long.toHexString(cache[index][0].tag) +" |  " +Long.toHexString(cache[index][1].tag) +" | "+Long.toHexString(cache[index][2].tag)+"  |  " + Long.toHexString(cache[index][3].tag)+" |" + "| " +  Long.toHexString(cache[index][4].tag) +" |  " +Long.toHexString(cache[index][5].tag) +" | "+Long.toHexString(cache[index][6].tag)+"  |  " + Long.toHexString(cache[index][7].tag)+" |");
			System.out.println("+---------------+---------------+--------------+--------------+---------------+---------------+--------------+--------------+");
			return;
		}
		else if(getFIFO(index, current_tag, operation)){
			System.out.println("After:\n+---------------+---------------+--------------+--------------+---------------+---------------+--------------+--------------+");
			System.out.println("| " +  Long.toHexString(cache[index][0].tag) +" |  " +Long.toHexString(cache[index][1].tag) +" | "+Long.toHexString(cache[index][2].tag)+"  |  " + Long.toHexString(cache[index][3].tag)+" |" + "| " +  Long.toHexString(cache[index][4].tag) +" |  " +Long.toHexString(cache[index][5].tag) +" | "+Long.toHexString(cache[index][6].tag)+"  |  " + Long.toHexString(cache[index][7].tag)+" |");
			System.out.println("+---------------+---------------+--------------+--------------+---------------+---------------+--------------+--------------+");
			return;}
		else throw new IllegalArgumentException("NOT HANDLER");
	}

	private boolean getFIFO(int index, long current_tag, char operation) {
		//we already know what to replace, counter
		int firstPointer = counter[index]; // for each set in cache, has a counter/pointer to FIFO
		if(cache[index][firstPointer].status_bit == element.DIRTY){
			writebacks++;
			System.out.println("Writeback!");
		}
		cache[index][firstPointer].tag = current_tag;
		if(operation == 'w')
			cache[index][firstPointer].status_bit = element.DIRTY;
		else if (operation == 'r')
			cache[index][firstPointer].status_bit = element.VALID;
		else
			throw new IllegalArgumentException();
		inc_counter(index);
		System.out.println("Replaced " + counter[index] + " of block");
		return true;
	}

	private boolean lookForEmpty(int index, long current_tag, char operation) {
		for(int j = 0; j < cache[index].length; j++){ //check row for invalid bits
			if(cache[index][j].status_bit == element.INVALID){ //if invalid, put tag in and stop!
				System.out.println("Found empty slot, placing in it");
				cache[index][j].tag = current_tag; //place current tag in set
				cache[index][j].status_bit = element.VALID; //valid after putting a tag in unless tag is dirty...
				//System.out.println("Empty/Invalid, placing in now!");
				if(operation == 'w'){
					cache[index][j].status_bit = element.DIRTY; //the element should now be "dirty"
					//System.out.println("Dirty bit is set!");
				}
				else if (operation == 'r'); //just here to allow the if not r|w then error
				else
					throw new IllegalArgumentException("Invalid operation!!!");
				//System.out.println("");
				return true;
			}
		}
		return false;
	}

	private boolean lookForMatch(int index, long current_tag, char operation) {
		for(int j = 0; j < cache[index].length; j++){ //
			if(cache[index][j].tag == current_tag){ //found a hit
				if(operation == 'w'){
					cache[index][j].status_bit = element.DIRTY; //becomes dirty if overwritten
				}
				else if (operation == 'r'); //if read, doesn't affect dirty
				else throw new IllegalArgumentException("Invalid operation!!!");
				System.out.println("Cache hit!");
				return true;
			}
		}
		return false;
	}
	
	private void inc_counter(int index){
		if(counter[index] >= numWays-1)
			counter[index] = 0;
		else counter[index]++;
	}

}
