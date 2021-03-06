package nebula.base;

import java.util.Map.Entry;

import nebula.common.util.L;

/**
 * Nebula entry instance.
 * @author ueyudiud
 *
 * @param <K> the <tt>key</tt> type.
 * @param <V> the <tt>value</tt> type.
 */
public
class Ety<K, V> implements Entry<K, V>
{
	/** The key object. */
	public final K key;
	/** The value object. */
	public V value;
	
	public Ety(K key, V value)
	{
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey()
	{
		return this.key;
	}
	
	@Override
	public V getValue()
	{
		return this.value;
	}
	
	@Override
	public V setValue(V value)
	{
		V ret = this.value;
		this.value = value;
		return ret;
	}
	
	@Override
	public int hashCode()
	{
		return this.key.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		else if(!(obj instanceof Entry)) return false;
		else return L.equal(this.key, ((Entry<?, ?>) obj).getKey());
	}
	
	@Override
	public String toString()
	{
		return new StringBuilder().append(this.key).append('=').append(this.value).toString();
	}
}