package glenn.gasesframework.util;

import glenn.gasesframework.BlockLantern;
import net.minecraft.item.ItemStack;

public class LanternRecipe
{
	public int itemID;
	public int itemDamage;
	protected BlockLantern blockLantern;
	
	public LanternRecipe(ItemStack itemStack, BlockLantern blockLantern)
	{
		this.itemID = itemStack.itemID;
		this.itemDamage = itemStack.getItemDamage();
		this.blockLantern = blockLantern;
	}
	
	public LanternRecipe()
	{
		
	}
	
	public boolean equals(ItemStack itemStack)
	{
		return itemStack != null && this.itemID == itemStack.itemID & itemStack.getItemDamage() == this.itemDamage;
	}
	
	public BlockLantern getLantern(ItemStack itemStack)
	{
		return blockLantern;
	}
}