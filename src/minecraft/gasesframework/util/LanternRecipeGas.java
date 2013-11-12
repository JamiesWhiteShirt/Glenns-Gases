package glenn.gasesframework.util;

import glenn.gasesframework.BlockLantern;
import glenn.gasesframework.Combustibility;
import glenn.gasesframework.GasType;
import glenn.gasesframework.ItemGasBottle;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LanternRecipeGas extends LanternRecipe
{
	public boolean equals(ItemStack itemStack)
	{
		Item item = itemStack.getItem();
		
		return item != null && item instanceof ItemGasBottle;
	}
	
	public BlockLantern getLantern(ItemStack itemStack)
	{
		Combustibility combustibility = GasType.gasTypes[itemStack.getItemDamage()].combustibility;
		return GasType.gasTypes[itemStack.getItemDamage()].combustibility.lanternBlock;
	}
}