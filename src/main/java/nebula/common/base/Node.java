package nebula.common.base;

public class Node<T> implements INode<T>
{
	/**
	 * Build the first node of nodes.
	 * @param target
	 * @return
	 */
	public static <T> Node<T> first(T target)
	{
		return new Node<>(target);
	}
	
	public static <T> Node<T> chain(T...ts)
	{
		switch (ts.length)
		{
		case 0 : return null;
		case 1 : return new Node<>(ts[0]);
		default:
			Node<T> result = new Node<>(ts[0]);
			Node<T> n1 = result;
			for (int i = 1; i < ts.length; ++i)
			{
				n1 = (n1.next = new Node<>(ts[i]));
			}
			return result;
		}
	}
	
	private T target;
	private Node<T> next;
	private Node<T> last;
	
	Node(T target)
	{
		this.target = target;
	}
	
	@Override
	public T value()
	{
		return this.target;
	}
	
	@Override
	public Node<T> next()
	{
		return this.next;
	}
	
	@Override
	public Node<T> last()
	{
		return this.last;
	}
	
	@Override
	public void addLast(T target)
	{
		if(this.last != null)
		{
			this.last.addLast(target);
		}
		else
		{
			this.last = new Node<>(target);
			this.last.next = this;
		}
	}
	
	@Override
	public void addNext(T target)
	{
		if(this.next != null)
		{
			this.next.addNext(target);
		}
		else
		{
			this.next = new Node<>(target);
			this.next.last = this;
		}
	}
	
	@Override
	public void insertAfter(T target)
	{
		Node<T> node = this.next;
		this.next = new Node<>(target);
		this.next.last = this;
		if(node != null)
		{
			node.last = this.next;
			this.next.next = node;
		}
	}
	
	@Override
	public void insertBefore(T target)
	{
		Node<T> node = this.last;
		this.last = new Node<>(target);
		this.last.next = this;
		if(node != null)
		{
			node.next = this.last;
			this.last.last = node;
		};
	}
	
	@Override
	public T remove()
	{
		if(this.last != null)
		{
			this.last.next = this.next;
		}
		if(this.next != null)
		{
			this.next.last = this.last;
		}
		this.next = this.last = null;
		return this.target;
	}
}