
public class node {
	public node[] childs = new node[2];
	public int pointer = 0; //points to one of the nodes
	public int level; //level of this node incase it matters
	public node(){}
	
	public node(int remaining){
		level = remaining;
		remaining--;
		if(remaining < 1 ){
			childs[0] = new element();
			childs[1] = new element();
		}
		else{
			childs[0] = new node(remaining);
			childs[1] = new node(remaining);
		}
		
	}
	
	
	public boolean findEmptySlot(long tag, int status_bit){
		boolean temp = childs[0].findEmptySlot(tag, status_bit);
		if(temp){
			if(pointer == 0)
				pointer = 1;
			return temp;
		}
		temp = childs[1].findEmptySlot(tag, status_bit);
		if(temp){
			if(pointer == 1)
				pointer = 0;
			return temp;
		}
		return temp;
	}
	
	public boolean findEqualTag(long tag, int status_bit, char operation){
		boolean temp = childs[0].findEqualTag(tag, status_bit, operation);
		if(temp){
			//System.out.println("Cache hit!");
			if(pointer == 0)
				pointer = 1;
			return temp;
		}
		temp = childs[1].findEqualTag(tag, status_bit, operation);
		if(temp){
			//System.out.println("Cache hit!");
			if(pointer == 1)
				pointer = 0;
			return temp;
		}
		return temp;
	}
	public boolean accessChild(long current_tag, int status_bit){
		boolean temp = childs[pointer].accessChild(current_tag, status_bit); //may NOT be v
		if(pointer == 0)
			pointer = 1;
		else if(pointer == 1)
			pointer = 0;
		return temp;
	
	}
	public long[] getAllElements(){
		long[] temp = childs[0].getAllElements();
		long[] temp2 = childs[1].getAllElements();
//		System.out.println("Child 1 elements:");
//		System.out.print("[ ");
//		for(int i = 0; i < temp.length; i++)
//			System.out.print(Long.toHexString(temp[i]) + "  ");
//		System.out.println(" ]");
//		System.out.println("Child 2 elements:");
//		System.out.print("[ ");
//		for(int i = 0; i < temp2.length; i++)
//			System.out.print(Long.toHexString(temp2[i]) + "  ");
//		System.out.println(" ]");
		long[] total = new long[temp.length+temp2.length];
		
		for(int i = 0; i < temp.length; i++){
			total[i] = temp[i];
		}
		for(int i = 0; i < temp2.length; i++){
			total[i+temp.length] = temp2[i];
		}
		
		return total;
	}

}
