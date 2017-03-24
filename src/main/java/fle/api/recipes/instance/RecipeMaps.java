/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance;

import static nebula.common.data.NBTLSs.RW_FLOAT;
import static nebula.common.data.NBTLSs.RW_INT;
import static nebula.common.data.NBTLSs.RW_ITEMSTACK;
import static nebula.common.data.NBTLSs.RW_UNORDERED_ITEMSTACKS;

import fle.api.recipes.TemplateRecipeMap;
import fle.api.recipes.instance.interfaces.ICraftingRecipeHandler;
import fle.api.recipes.instance.interfaces.IPolishRecipeHandler;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeMaps
{
	//NAME        : minecraft.crafting
	public static final
	VanillaCraftingHandler CRAFTING_VANILLA =
	new VanillaCraftingHandler();
	//NAME        : minecraft.smelting
	//NBT         : (ItemStack)
	public static final
	VanillaSmeltingHandler SMELTING_VANILLA =
	new VanillaSmeltingHandler();
	//NAME        : fle.crafting
	//NBT ENTRY   : time(int), output(ItemStack)
	//DISPLAYMENTS: materialRequired(@Nullable Material)
	//              matrix(AbstractStack[][]), toolEntries(Entry<AbstractStack, AbstractStack>[]),
	//              outputDefault(ItemStack)
	public static final
	TemplateRecipeMap<ICraftingRecipeHandler> CRAFTING =
	TemplateRecipeMap.<ICraftingRecipeHandler>builder("fle.crafting")
	.addCacheEntry("time", RW_INT)
	.addCacheEntry("output", RW_ITEMSTACK).build();
	//NAME        : fle.washing.bar.grizzly
	//NBT ENTRY   : time(int), outputs(ItemStack[])
	//DISPLAYMENTS: input(AbstractStack), duration(int), output(ItemStack[]), outputChances(int[][])
	public static final
	TemplateRecipeMap<ItemStack> WASHING_BARGRIZZLY =
	TemplateRecipeMap.<ItemStack>builder("fle.washing.bar.grizzly")
	.addCacheEntry("time", RW_INT)
	.addCacheEntry("outputs", RW_UNORDERED_ITEMSTACKS).build();
	//NAME        : fle.portable.woodwork
	public static final
	PortableWoodworkRecipeMap PORTABLE_WOODWORK =
	new PortableWoodworkRecipeMap();
	//NAME        : fle.polishing
	//NBT ENTRY   : output(ItemStack)
	//DISPLAYMENTS: input(AbstractStack), map(char[]), output(ItemStack)
	public static final
	TemplateRecipeMap<IPolishRecipeHandler> POLISHING =
	TemplateRecipeMap.<IPolishRecipeHandler>builder("fle.polishing").addCacheEntry("output", RW_ITEMSTACK).build();
	//NAME        : fle.dring
	//NBT ENTRY   : duration(int), multiplier(float), output(ItemStack)
	//DISPLAYMENTS: input(AbstractStack), duration(int), multiplier(float), output(ItemStack)
	public static final
	TemplateRecipeMap<ItemStack> DRYING =
	TemplateRecipeMap.<ItemStack>builder("fle.dring").addCacheEntry("duration", RW_INT).addCacheEntry("multiplier", RW_FLOAT).addCacheEntry("output", RW_ITEMSTACK).build();
}