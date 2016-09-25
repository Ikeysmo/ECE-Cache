import java.math.BigInteger;

public class LRUCache extends GenericCache {
	public LRUCache(int BLOCKSIZE, int SIZE, int ASSOC, int REPL_POLICY, int INCLUSION) throws InterruptedException {
		super(BLOCKSIZE, SIZE, ASSOC, REPL_POLICY, INCLUSION);
		}

	@Override
	public void handleTransaction(String address, char operation) {
		//tag width is already known
		
		long mem_address = Long.decode("0x" + address.trim());
		long current_tag = mem_address >>> (index_width + offset_width);
		int index =(int)((mem_address >>> offset_width) & ((~0)>>> (64-index_width)));
		//System.out.println("Index: " + Long.toString(index));
		//System.out.println("Current tag: " + Long.toHexString(current_tag));
		
		//steps for LRU
		//1) try to look for empty/invalid
		//2) try to find a hit
		//3) if all else fails, no hit
		//System.out.println("Before:\n+---------------+---------------+--------------+--------------+");
		//System.out.println("| " +  Long.toHexString(cache[index][0].tag) +" |  " +Long.toHexString(cache[index][1].tag) +" | "+Long.toHexString(cache[index][2].tag)+"  |  " + Long.toHexString(cache[index][3].tag)+" |");
		//System.out.println("+---------------+---------------+--------------+--------------+");

		if(lookForMatch(index, current_tag, operation)){
			//System.out.println("After:\n+---------------+---------------+--------------+--------------+");
			//System.out.println("| " +  Long.toHexString(cache[index][0].tag) +" |  " +Long.toHexString(cache[index][1].tag) +" | "+Long.toHexString(cache[index][2].tag)+"  |  " + Long.toHexString(cache[index][3].tag)+" |");
			//System.out.println("+---------------+---------------+--------------+--------------+");
			return;
		}
		if(lookForEmpty(index, current_tag, operation)){ //find if any in set x is empty
		//	System.out.println("After:\n+---------------+---------------+--------------+--------------+");
			//System.out.println("| " +  Long.toHexString(cache[index][0].tag) +" |  " +Long.toHexString(cache[index][1].tag) +" | "+Long.toHexString(cache[index][2].tag)+"  |  " + Long.toHexString(cache[index][3].tag)+" |");
			//System.out.println("+---------------+---------------+--------------+--------------+");
			return;
		}
		else if(getLRU(index, current_tag, operation)){ //if full, find min/LRU element and replace
			//System.out.println("After:\n+---------------+---------------+--------------+--------------+");
			//System.out.println("| " +  Long.toHexString(cache[index][0].tag) +" |  " +Long.toHexString(cache[index][1].tag) +" | "+Long.toHexString(cache[index][2].tag)+"  |  " + Long.toHexString(cache[index][3].tag)+" |");
			//System.out.println("+---------------+---------------+--------------+--------------+");
			return;
		}
		else
			throw new IllegalArgumentException("NOT HANDLER");
		
		
	}
	
	private boolean lookForMatch(int index, long current_tag, char operation) {
		//find a hit if possible

		for(int j = 0; j < cache[index].length; j++){ //
			if(cache[index][j].tag == current_tag){ //found a hit
				//System.out.println("Cache hit: Valid/Dirty");
				cache[index][j].counter = getMax(cache[index]) + 1; 
				if(operation == 'w'){
					cache[index][j].status_bit = element.DIRTY; //becomes dirty if overwritten
					//System.out.println("Dirty bit is set!");
				}
				else if (operation == 'r'); //if read, doesn't affect dirty
				else throw new IllegalArgumentException("Invalid operation!!!");
				
				return true;
			}
		}
		return false;
	}

	public boolean lookForEmpty(int index, long current_tag, char operation){
		for(int j = 0; j < cache[index].length; j++){ //check row for invalid bits
			if(cache[index][j].status_bit == element.INVALID){ //if invalid, put tag in and stop!
				cache[index][j].tag = current_tag; //place current tag in set
				cache[index][j].status_bit = element.VALID; //valid after putting a tag in unless tag is dirty...
				cache[index][j].counter = getMax(cache[index])+1; //is equal to max
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
		//System.out.println("There are no empty locations");
		return false; //fails if there are no empty slots
	}
	
	public boolean getLRU(int index, long current_tag, char operation){
		int min; 
	    min = getMin(cache[index]);
		//if not a hit and not empty spot, then consider replacement policy && writeback if needed!
		for(int j = 0; j < cache[index].length; j++){
			if(cache[index][j].counter == min){ //LRU
				//System.out.println("Replacement");
				cache[index][j].tag = current_tag;
				if(cache[index][j].status_bit == element.DIRTY){
					//System.out.println("Writeback!");
					writebacks++;		
				}
				//then consider other stuff
				if(operation == 'w')
					cache[index][j].status_bit = element.DIRTY;
				else if (operation == 'r')
					cache[index][j].status_bit = element.VALID;
				else
					throw new IllegalArgumentException();
				cache[index][j].counter = getMax(cache[index])+1; //after replacing, set it's counter higher
				//System.out.println("");
				return true;
			}
		}
		
		return false;
		
	}
	

	private int getMax(element[] list){
		int max = 0; 
		for(int j = 0; j < list.length; j++){ //find the maximum counter
			if(list[j].counter > max)
				max = list[j].counter;
		}
		return max;
	}
	private int getMin(element[] list){
		int min = list[0].counter;
		for(int j = 0; j < list.length; j++){
			if(list[j].counter < min)
				min = list[j].counter;
		}
		return min;
	}
}
