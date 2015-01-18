package list;

public class SnakeList<T> {
	
	private SnakeNode<T> first, last;
	private int size;
	
	public SnakeList() {
		first = last = null;
		size = 0;
	}
	
	public int size() {return size;}
	public boolean isEmpty() {return size == 0;}
	public T getFirst() {return first.getContents();}
	public T getLast() {return last.getContents();}
	
	public void addFirst(T add) {
		SnakeNode node = new SnakeNode(add);
		if(isEmpty()) first = last = node;
		else {
			node.setNext(first);
			first.setPrevious(node);
			first = node;
		}
		size++;
	}
	
	public void addLast(T add) {
		SnakeNode node = new SnakeNode(add);
		if(isEmpty()) first = last = node;
		else {
			last.setNext(node);
			node.setPrevious(last);
			last = node;
		}
		size++;
	}
	
	public void removeLast() {
		if(isEmpty()) throw new NullPointerException();
		last = last.getPrevious();
		last.setNext(null);
		size--;
	}
	
	public boolean contains(T content) {
		SnakeNode node = first;
		while(node != null) {
			if(node.getContents().equals(content)) return true;
			node = node.getNext();
		}
		return false;
	}
	
	
	public static void main(String args[]) {
		SnakeList<Integer> list = new SnakeList(); 
		list.addFirst(3);
		list.addFirst(2);
		list.addFirst(0);
		list.addLast(5);
		list.addFirst(1);
		list.addLast(4);
		System.out.println(list.getFirst());
		System.out.println(list.getLast());
	}

}
