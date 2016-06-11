package farcore.interfaces.energy.thermal;

import farcore.enums.Direction;
import net.minecraft.item.ItemStack;

public interface IThermalItem
{
	default ItemStack updateItemInThermalTile(IThermalTile tile, ItemStack target)
	{	
		float temp1 = tile.getTemperature(Direction.Q);
		float temp = getTemperature(target);
		if(temp1 == temp)
		{
			return target;
		}
		else if(temp1 > temp || canOutputThermalEnergy(target))
		{
			float value = (temp1 - temp) * (tile.getThermalConductivity(Direction.Q) + getThermalConductivity(target)) * .5F;
			if(value > 0)
			{
				tile.emitThermalEnergy(Direction.Q, value);
			}
			else if(value < 0)
			{
				tile.receiveThermalEnergy(Direction.Q, value);
			}
			return onHeatChanged(target, value);
		}
		else
		{
			return target;
		}
	}
	
	float getTemperature(ItemStack target);
	
	boolean canOutputThermalEnergy(ItemStack target);
	
	float getThermalConductivity(ItemStack target);
	
	ItemStack onHeatChanged(ItemStack target, float deltaHeat);
}