/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongPredicate;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ObjectArrays;
import com.google.common.reflect.TypeToken;

/**
 * @author ueyudiud
 */
public final class A
{
	private A() {}
	
	/**
	 * Copy array elements to a new array with selected length.
	 * @param array The source array.
	 * @param len The length of new array, use old array length if the select length is smaller than old array length.
	 * @return The copied array.
	 */
	public static int[] copyToLength(int[] array, int len)
	{
		int[] result = new int[len];
		System.arraycopy(array, 0, result, 0, Math.min(len, array.length));
		return result;
	}
	
	/**
	 * Copy array elements to a new array with selected length.
	 * The result array type is same to old array type.
	 * @param array the old array, if it is <tt>null</tt> it will only use to mark a type.
	 * @param len the new array length.
	 * @param <T> the type of array.
	 * @return if array is <tt>null</tt>, return a new array or
	 *         return a copy otherwise.
	 */
	public static <T> T[] copyToLength(@Nullable T[] array, int len)
	{
		if (array != null)
		{
			T[] result = ObjectArrays.newArray(array, len);
			System.arraycopy(array, 0, result, 0, Math.min(len, array.length));
			return result;
		}
		else
		{
			@SuppressWarnings("unchecked")
			T[] result = (T[]) ObjectArrays.newArray(new TypeToken<T>() {
				private static final long serialVersionUID = 8964692193893392799L;
			}.getRawType(), len);
			return result;
		}
	}
	
	/**
	 * Given action to every element in array.
	 * @see java.util.Collection#forEach(Consumer)
	 * @param iterable
	 * @param consumer
	 */
	public static <E> void executeAll(@Nonnull E[] iterable, @Nonnull Consumer<E> consumer)
	{
		Objects.requireNonNull(consumer);
		for(E element : iterable) consumer.accept(element);
	}
	
	/**
	 * Given action to every element in array.
	 * @see java.util.Collection#forEach(Consumer)
	 * @param iterable
	 * @param consumer
	 */
	public static <E> void executeAll(@Nonnull E[] iterable, @Nonnull ObjIntConsumer<E> consumer)
	{
		Objects.requireNonNull(consumer);
		for(int i = 0; i < iterable.length; ++i) consumer.accept(iterable[i], i);
	}
	
	/**
	 * Match same element in array, use {@code L.equal(element, arg)} to
	 * match objects.
	 * @param list The given array.
	 * @param arg The matched argument.
	 * @return
	 */
	public static <E> boolean contain(@Nonnull E[] list, @Nullable E arg)
	{
		for(E element : list) if(L.equal(element, arg)) return true;
		return false;
	}
	
	public static boolean contain(int[] list, int arg)
	{
		for(int element : list) if(element == arg) return true;
		return false;
	}
	
	public static boolean contain(char[] list, char arg)
	{
		for(char element : list) if(element == arg) return true;
		return false;
	}
	
	/**
	 * Check all elements can be matched.
	 * @param list
	 * @param checker
	 * @param <E> The type of element.
	 * @return
	 */
	public static <E> boolean and(E[] list, Predicate<? super E> checker)
	{
		for(E element : list) if (!checker.test(element)) return false;
		return true;
	}
	
	public static <E> boolean and(int[] list, IntPredicate predicate)
	{
		for(int element : list) if (!predicate.test(element)) return false;
		return true;
	}
	
	public static <E> boolean and(long[] list, LongPredicate predicate)
	{
		for(long element : list) if (!predicate.test(element)) return false;
		return true;
	}
	
	public static <E> boolean or(E[] list, Predicate<? super E> checker)
	{
		for(E element : list) if (checker.test(element)) return true;
		return false;
	}
	
	public static <E> boolean or(int[] list, IntPredicate predicate)
	{
		for(int element : list) if (predicate.test(element)) return true;
		return false;
	}
	
	public static <E> boolean or(long[] list, LongPredicate predicate)
	{
		for(long element : list) if (predicate.test(element)) return true;
		return false;
	}
	
	/**
	 * Create new integer array with same elements.
	 * @param length
	 * @param value
	 * @return
	 */
	public static int[] fillIntArray(int length, int value)
	{
		switch(length)
		{
		case 0 : return new int[0];
		case 1 : return new int[]{value};
		default:
			int[] ret = new int[length];
			Arrays.fill(ret, value);
			return ret;
		}
	}
	
	public static <E> E[] fill(E[] array, IntFunction<E> function)
	{
		for (int i = 0; i < array.length; array[i] = function.apply(i), ++i);
		return array;
	}
	
	/**
	 * Get first equally {@code int} value position.
	 * @param list
	 * @param arg
	 * @return
	 */
	public static int indexOf(int[] list, int arg)
	{
		for(int i = 0; i < list.length; ++i) if(list[i] == arg) return i;
		return -1;
	}
	
	/**
	 * Get first matched element index in list.
	 * @param list
	 * @param arg The matching target.
	 * @return The index of element, -1 means no element matched.
	 */
	public static int indexOfFirst(Object[] list, Object arg)
	{
		for(int i = 0; i < list.length; ++i) if(L.equal(list[i], arg)) return i;
		return -1;
	}
	
	/**
	 * Transform key array to target array.
	 * @param array
	 * @param elementClass
	 * @param function Transform function.
	 * @return
	 */
	public static <K, T> T[] transform(K[] array, Class<T> elementClass, Function<? super K, ? extends T> function)
	{
		T[] result = ObjectArrays.newArray(elementClass, array.length);
		for(int i = 0; i < array.length; result[i] = function.apply(array[i]), ++i);
		return result;
	}
	
	/**
	 * Transform key array to object array.
	 * @param array
	 * @param function Transform function.
	 * @return
	 */
	public static <K> Object[] transform(K[] array, Function<? super K, ?> function)
	{
		Object[] result = new Object[array.length];
		for(int i = 0; i < array.length; result[i] = function.apply(array[i]), ++i);
		return result;
	}
	
	/**
	 * Transform key array to target array.
	 * @param array
	 * @param elementClass
	 * @param function Transform function.
	 * @return
	 */
	public static <T> T[] transform(int[] array, Class<T> elementClass, IntFunction<? extends T> function)
	{
		T[] result = ObjectArrays.newArray(elementClass, array.length);
		for(int i = 0; i < array.length; result[i] = function.apply(array[i]), ++i);
		return result;
	}
	
	public static int[] rangeIntArray(int to)
	{
		return rangeIntArray(0, to);
	}
	
	/**
	 * Create a array with ranged number.<p>
	 * For examples : the result of <tt>rangeIntArray(1, 3)</tt>
	 * is <tt>[1, 2]</tt>
	 * @param from start value (include itself)
	 * @param to end value (exclude itself)
	 * @return the int array.
	 */
	public static int[] rangeIntArray(int from, int to)
	{
		int[] array = new int[to - from];
		for(int i = 0; i < array.length; array[i] = from + i, i++);
		return array;
	}
	
	/**
	 * Create a array with selected generator.<p>
	 * Examples :
	 * <code>createIntArray(3, i->i*i)</code>
	 * and the result is <tt>[0, 1, 4]</tt>
	 * @param length the length of array.
	 * @param operator the function to provide <tt>int</tt> value.
	 * @return the array.
	 */
	public static int[] createIntArray(int length, IntUnaryOperator operator)
	{
		int[] result = new int[length];
		for (int i = 0; i < length; result[i] = operator.applyAsInt(i), ++i);
		return result;
	}
	
	/**
	 * Create a array with selected generator.
	 * @param length the length of array.
	 * @param function the function to provider <tt>long</tt> value
	 * @return the array.
	 * @see #createIntArray(int, IntUnaryOperator)
	 */
	public static long[] createLongArray(int length, IntToLongFunction function)
	{
		long[] result = new long[length];
		for (int i = 0; i < length; result[i] = function.applyAsLong(i), ++i);
		return result;
	}
	
	/**
	 * Create a new array fill with single element.<p>
	 * The argument should not be null for this method use value to predicated
	 * type of array.
	 * @param length the length of array.
	 * @param value the value filling the array.
	 * @return the filled array.
	 */
	public static <E> E[] createArray(int length, @Nonnull E value)
	{
		E[] array = (E[]) ObjectArrays.newArray(value.getClass(), length);
		Arrays.fill(array, value);
		return array;
	}
	
	public static char[] sublist(char[] array, int start, int end)
	{
		char[] a1 = new char[end - start];
		System.arraycopy(array, start, a1, 0, end - start);
		return a1;
	}
	
	/**
	 * Get sub list of array.
	 * @param array the source array.
	 * @param off the sublist start pos(include itself).
	 * @return the sub array with element start at <tt>off</tt> position in source array and
	 * include all element after.
	 * @see #sublist(Object[], int, int)
	 */
	public static <E> E[] sublist(@Nonnull E[] array, int off)
	{
		return sublist(array, off, array.length - off);
	}
	
	/**
	 * Create a sub list from argument list.
	 * @param array the source of array.
	 * @param off the sublist start pos(include itself).
	 * @param len the length of array.
	 * @return the sub array with element start at <tt>off</tt> position in source array and
	 * end at <tt>off + len</tt> postion in source array.
	 * @throws java.lang.IndexOutOfBoundsException when copy length is out of array bound.
	 */
	public static <E> E[] sublist(@Nonnull E[] array, int off, int len)
	{
		E[] a1 = ObjectArrays.newArray(array, len);
		System.arraycopy(array, off, a1, 0, len);
		return a1;
	}
	
	/**
	 * Return if all elements are non-null.
	 * @param <T> type of elements.
	 * @param array the elements.
	 * @return the elements.
	 * @throws java.lang.NullPointerException if any element in array is null.
	 */
	public static <T> T[] allNonNull(@Nonnull T[] array)
	{
		for (Object arg : array) if (arg == null) throw new NullPointerException();
		return array;
	}
}