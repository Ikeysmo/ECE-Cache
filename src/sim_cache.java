import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class sim_cache {
	public final static int LRU = 0;
	public final static int FIFO = 1;
	public final static int pseudoLRU = 2;
	public final static int OPTIMAL = 3;
	public final static int NON_INCLUSIVE = 0;
	public final static int INCLUSIVE = 1;
	public final static int EXCLUSIVE = 2;
	//end of macros
	
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		if (args.length != 8){
			System.out.println("Sorry, incorrect arguments");
			System.exit(-1);
		}
		String currdir = System.getProperty("user.dir");
		int BLOCKSIZE = Integer.parseInt(args[0]);
		int L1_SIZE = Integer.parseInt(args[1]);
		int L1_ASSOC = Integer.parseInt(args[2]);
		int L2_SIZE = Integer.parseInt(args[3]);
		int L2_ASSOC = Integer.parseInt(args[4]);
		int REPL_POLICY = Integer.parseInt(args[5]);
		int INCLUSION = Integer.parseInt(args[6]);
		String Tracefile = args[7];
		//end of this
		FileReader fr = new FileReader("/home/isaiah/workspace/Cache Project/src/" +Tracefile);
		BufferedReader br = new BufferedReader(fr);
		String temp = "";
		int reads = 0;
		int writes = 0;
		GenericCache L1 = null;
		//cache parameters
		if(REPL_POLICY == LRU)
			L1 = new LRUCache(BLOCKSIZE, L1_SIZE, L1_ASSOC, REPL_POLICY, INCLUSION);
		else if (REPL_POLICY == FIFO)
			L1 = new FIFOCache(BLOCKSIZE,L1_SIZE, L1_ASSOC, REPL_POLICY, INCLUSION);
		else if (REPL_POLICY == pseudoLRU)
			L1 = new psuedoLRUCache(BLOCKSIZE,L1_SIZE, L1_ASSOC, REPL_POLICY, INCLUSION);
		else if (REPL_POLICY == OPTIMAL)
			L1 = new optimalCache(BLOCKSIZE,L1_SIZE, L1_ASSOC, REPL_POLICY, INCLUSION);
		else
			throw new IllegalArgumentException("Invalid replacement policy given!");
		//end initiate cache
		while(true){
			temp = br.readLine();
			if(temp == null) break;
			L1.feed(temp.substring(1), temp.charAt(0));
			
			if (temp.charAt(0) == 'r'){
				reads++;
			}
			else if(temp.charAt(0) == 'w'){
				writes++;
			}
			else throw new IllegalArgumentException("ERROR! Got unexpected output!");
			//System.out.println("\n============================================================================================================\n");
			//Thread.sleep(4000);
		}
		// done reading file.. but gotta do the stuff inside
		int totalWriteBacks = L1.getWritebacks();
		int totalReadMisses = L1.getReadMisses();
		int totalWriteMisses = L1.getWriteMisses();
		double missRate = (double)(totalReadMisses + totalWriteMisses)/(double)(reads + writes);
		int totalMemTraffic = (totalReadMisses + totalWriteBacks + totalWriteMisses);
		System.out.println("===== Simulator configuration =====\n");
		System.out.println("BLOCKSIZE:             " + BLOCKSIZE);
		System.out.println("L1_SIZE:               " + L1_SIZE);
		System.out.println("L1_ASSOC:              " + L1_ASSOC);
		System.out.println("L2_SIZE:               " + L2_SIZE);
		System.out.println("L2_ASSOC:              " + L2_ASSOC);
		switch(REPL_POLICY){
		case LRU:
			System.out.println("REPLACEMENT POLICY:    LRU");
			break;
		case FIFO:
			System.out.println("REPLACEMENT POLICY:    FIFO");
			break;
		case pseudoLRU:
			System.out.println("REPLACEMENT POLICY:    Pseudo");
			break;
		case OPTIMAL:
			System.out.println("REPLACEMENT POLICY:    Optimal");
			break;
		}
		
		switch(INCLUSION){
		case INCLUSIVE:
			System.out.println("INCLUSION PROPERTY:    inclusion");
			break;
		case NON_INCLUSIVE:
			System.out.println("INCLUSION PROPERTY:    non-inclusion");
			break;
		case EXCLUSIVE:
			System.out.println("INCLUSION PROPERTY:    exclusive");
			break;
		}
		
		System.out.println("trace_file:            " + Tracefile);
		System.out.println("===== Simulation results (raw) =====");
		System.out.println("a. number of L1 reads:        " + reads);
		System.out.println("b. number of L1 read misses:  " + totalReadMisses);
		System.out.println("c. number of L1 writes:       " + writes);
		System.out.println("d. number of L1 write misses: " + totalWriteMisses);
		System.out.println("e. L1 miss rate:              " + missRate);
		System.out.println("f. number of L1 writebacks:   " + totalWriteBacks);
		System.out.println("g. number of L2 reads:        0");
		System.out.println("h. number of L2 read misses:  0");
		System.out.println("i. number of L2 writes:       0");
		System.out.println("j. number of L2 write misses: 0");
		System.out.println("k. L2 miss rate:              0");
		System.out.println("l. number of L2 writebacks:   0");
		System.out.println("m. total memory traffic:      " + totalMemTraffic);
		
	}
		
}
