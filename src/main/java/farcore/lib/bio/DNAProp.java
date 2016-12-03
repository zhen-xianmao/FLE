package farcore.lib.bio;

import farcore.lib.collection.Selector;
import farcore.lib.collection.Stack;
import farcore.lib.collection.WeightedRandomSelector;

public abstract class DNAProp<T extends DNACharacter>
{
	public char startType;
	public float borderChanceBase;
	public char fixedType;
	public T[] allowedChars;
	public Selector<T> weight;
	
	public DNAProp(Stack<T>...characters)
	{
		this.startType = characters[0].element.chr;
		this.allowedChars = createCharacters(characters.length);
		this.weight = new WeightedRandomSelector(characters);
		for(int i = 0; i < characters.length; ++i)
		{
			this.allowedChars[i] = characters[i].element;
		}
	}
	
	protected abstract T[] createCharacters(int length);
	
	public DNAProp setBorderChance(float chance)
	{
		this.borderChanceBase = chance;
		return this;
	}
	
	public DNAProp setFixedType(char fixedType)
	{
		this.fixedType = fixedType;
		return this;
	}
	
	public char borderDNA(char dna0, float chance)
	{
		if(dna0 == this.fixedType)
		{
			return dna0;
		}
		if(Math.random() < chance * this.borderChanceBase)
		{
			return this.weight.next().chr;
		}
		return dna0;
	}
}