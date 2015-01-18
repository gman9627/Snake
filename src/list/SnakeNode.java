package list;

public class SnakeNode<T> {

	private SnakeNode<T> next, previous;
	private final T contents; 
	
	public SnakeNode(SnakeNode next, SnakeNode previous, T contents) {
		this.setNext(next);
		this.setPrevious(previous);
		this.contents = contents;
	}
	
	public SnakeNode(T contents) {
		this(null, null, contents);
	}

	public SnakeNode<T> getNext() {
		return next;
	}

	public void setNext(SnakeNode<T> next) {
		this.next = next;
	}

	public SnakeNode<T> getPrevious() {
		return previous;
	}

	public void setPrevious(SnakeNode<T> previous) {
		this.previous = previous;
	}

	public T getContents() {
		return contents;
	}
	
	
	
}
