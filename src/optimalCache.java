
public class optimalCache extends GenericCache {

	public optimalCache(int BLOCKSIZE, int SIZE, int ASSOC, int REPL_POLICY, int INCLUSION)
			throws InterruptedException {
		super(BLOCKSIZE, SIZE, ASSOC, REPL_POLICY, INCLUSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleTransaction(String address, char operation) {
		long mem_address = Long.decode("0x" + address.trim());
		long current_tag = mem_address >>> (index_width + offset_width);
		int index =(int)((mem_address >>> offset_width) & ((~0)>>> (64-index_width)));

	}

}
