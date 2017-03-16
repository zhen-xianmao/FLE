/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;

import nebula.common.stack.AbstractStack;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class SingleInputMatch
{
	public static final SingleInputMatch EMPTY = new SingleInputMatch(null);
	
	/**
	 * The input stack.
	 */
	protected AbstractStack input;
	/**
	 * The effect on output.
	 */
	protected @Nullable BiConsumer<ItemStack, ItemStack> consumer;
	/**
	 * The result of input.
	 */
	protected @Nullable Function<ItemStack, ItemStack> result;
	
	public SingleInputMatch(AbstractStack input)
	{
		this(input, null);
	}
	public SingleInputMatch(AbstractStack input, UnaryOperator<ItemStack> result)
	{
		this(input, null, result);
	}
	public SingleInputMatch(AbstractStack input, BiConsumer<ItemStack, ItemStack> consumer, UnaryOperator<ItemStack> result)
	{
		this.input = input;
		this.consumer = consumer;
		this.result = result;
	}
	
	public boolean match(ItemStack stack)
	{
		return this.input == null ? stack == null : (stack != null && this.input.contain(stack));
	}
	
	public void applyOutput(ItemStack input, ItemStack output)
	{
		if (this.consumer != null)
		{
			this.consumer.accept(input, output);
		}
	}
	
	public ItemStack getRemain(ItemStack input)
	{
		return this.result == null ? null : this.result.apply(input);
	}
}