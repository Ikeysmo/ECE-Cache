
public abstract class GenericCache {
	protected int inclusion;
	protected int numSets;
	protected int numWays;
	protected int index_width;
	protected int offset_width;
	protected int tag_width = 0; 
	protected int address_width;
	protected long address;
	protected int writebacks = 0;
	protected int readMisses = 0;
	protected int writeMisses = 0;
	protected element[][] cache;
	
	public GenericCache(int BLOCKSIZE, int SIZE, int ASSOC, int REPL_POLICY, int INCLUSION) throws InterruptedException{
		inclusion = INCLUSION;
		numSets = SIZE/(ASSOC* BLOCKSIZE);
		numWays = ASSOC;
		index_width = (int) (Math.log10(numSets)/Math.log10(2)); //same as log2(a)
		offset_width = (int) (Math.log10(BLOCKSIZE)/Math.log10(2));
		cache = new element[numSets][numWays];
		//initialize cache
		if(REPL_POLICY != sim_cache.pseudoLRU){ //don't initialize pointer if unneeded!
			for(int j = 0; j < cache.length; j++){
				for(int x = 0; x < cache[j].length; x++){
					cache[j][x] = new element();
				}
			}
		}
		//
		System.out.println("Number sets: " + numSets);
		System.out.println("Number ways: " + numWays);
		System.out.println("Index width: " + index_width);
		System.out.println("Offset width: " + offset_width);
		System.out.println("");
		Thread.sleep(1000);
	}
	public void feed(String address, char operation) throws InterruptedException{
		//System.out.println("Operation:" + operation);
		if (tag_width == 0){
			address_width = address.trim().length()*4;
			tag_width = address_width -offset_width - index_width;	
		}
		//System.out.println("This is tag width: " + tag_width );
		handleTransaction(address,operation);
	}
	
	public abstract void handleTransaction(String address, char operation);
	
	public String toString(){
		return "This is contents\nWriteback: " + writebacks;
	}
	public int getWritebacks(){
		return writebacks;
	}
	public int getReadMisses(){
		return readMisses;
	}
	public int getWriteMisses(){
		return writeMisses;
	}
}
