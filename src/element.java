
public class element {
	public final static int DIRTY = 2;
	public final static int VALID = 1;
	public final static int INVALID = 0;
	public long tag; //contain the tag. if not equal, replacement policy
	public int status_bit = INVALID; //used for dirty,valid or invalid
	public int counter = 0; //used for LRU
}
