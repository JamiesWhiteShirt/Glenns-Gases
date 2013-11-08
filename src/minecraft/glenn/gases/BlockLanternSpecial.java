package glenn.gases;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockLanternSpecial extends BlockLantern
{
	public ItemStack containedItemIn;
	public ItemStack containedItemOut;
	public BlockLantern expirationBlock;
	
	public BlockLanternSpecial(int blockID, int tickrate, ItemStack containedItemIn, ItemStack containedItemOut, BlockLantern expirationBlock)
	{
		super(blockID, null, tickrate);
		
		this.containedItemIn = containedItemIn;
		this.containedItemOut = containedItemOut;
		this.expirationBlock = expirationBlock;
		
		lanternRecipes.add(new LanternRecipe(containedItemIn, this));
		//TODO: Add item recipe!
	}
	
	public int getExpirationBlockID()
	{
		if(expirationBlock == null) return -1;
		return expirationBlock.blockID;
	}
	
	public ItemStack getContainedItemOut()
	{
		return containedItemOut.copy();
	}
	
	public ItemStack getContainedItemIn()
	{
		return containedItemIn.copy();
	}
}