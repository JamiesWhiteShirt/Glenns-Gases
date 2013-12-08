package glenn.gasesframework;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockLanternSpecial extends BlockLantern
{
	public ItemStack containedItemIn;
	public ItemStack containedItemOut;
	public BlockLantern expirationBlock;
	
	/**
	 * Creates a new lantern which contains certain items and can eject another item in return.
	 * @param blockID - The ID of the block.
	 * @param tickrate - The rate at which the lantern will burn out. Set to 0 for non-expiring lanterns.
	 * @param containedItemIn - The item/block which can be used with a lantern to create this lantern.
	 * @param containedItemOut - The item/block which will be ejected by this lantern. For instance, gas lanterns accept bottles of gas but eject empty bottles.
	 * @param expirationBlock - The block this lantern will become when expired or destroyed.
	 */
	public BlockLanternSpecial(int blockID, int tickrate, ItemStack containedItemIn, ItemStack containedItemOut, BlockLantern expirationBlock)
	{
		super(blockID, null, tickrate, containedItemIn);
		
		this.containedItemIn = containedItemIn;
		this.containedItemOut = containedItemOut;
		this.expirationBlock = expirationBlock;
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