
public class element extends node {
	public element(){
		super();}  //for whatever purposes 
	

	public final static int DIRTY = 2;
	public final static int VALID = 1;
	public final static int INVALID = 0;
	public long tag; //contain the tag. if not equal, replacement policy
	public int status_bit = INVALID; //used for dirty,valid or invalid
	public int counter = 0; //used for LRU
	
	public boolean accessChild(long current_tag, int status_bit){ //for psuedoLRU
		//System.out.println("Replacing a element!");
		boolean temp = false;
		if(this.status_bit == DIRTY)
			temp = true; //true for writeback/false for w/e
		this.status_bit = status_bit;
		this.tag = current_tag;
		
		return temp; //writeback if true!
		
	}
	
	public boolean findEmptySlot(long current_tag, int status_bit){
		//System.out.println("SHould be empty?");
		if(this.status_bit == INVALID){
			this.tag = current_tag;
			this.status_bit = status_bit;
			//System.out.println("FOUND EMPTY SLOT and adding!");
			return true;
		}
		//System.out.println("Did not find empty!");
		return false; //didn't find it!
		
	}
	
	public boolean findEqualTag(long current_tag, int status_bit, char operation){
		if(this.tag == current_tag){
			System.out.println("Cache hit!");
			if(operation == 'w')
				this.status_bit = status_bit;
			return true;
		}
		else
			return false;
	}
	public long[] getAllElements(){
		long[] temp = new long[]{tag};
		return temp;
	}
}
