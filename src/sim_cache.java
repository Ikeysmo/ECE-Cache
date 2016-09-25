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
		int REPL_POLICY = Integer.parseInt(args[5]);
		int INCLUSION = Integer.parseInt(args[6]);
		String Tracefile = args[7];
		//end of this
		FileReader fr = new FileReader(Tracefile);
		BufferedReader br = new BufferedReader(fr);
		String temp = "";
		int reads = 0;
		int writes = 0;
		//cache parameters
		LRUCache L1 = new LRUCache(BLOCKSIZE, L1_SIZE, L1_ASSOC, REPL_POLICY, INCLUSION);
		
		//end initiate cache
		while(true){
			System.out.println(temp);
			temp = br.readLine();
			if(temp == null)
				break;
			//feed into cache
			L1.feed(temp.substring(1), temp.charAt(0));
			//iterate and count
			if (temp.charAt(0) == 'r'){
				reads++;
			}
			else if(temp.charAt(0) == 'w'){
				writes++;
			}
			else throw new IllegalArgumentException("ERROR! Got unexpected output!");
		}
		// done reading file.. but gotta do the stuff inside
		System.out.println("DONE!");
		System.out.println("Reads: " + reads + "\nWrites: " + writes);
		System.out.println(L1);
	}
		
}
