package glenn.gases.core;

import glenn.gases.BlockGas;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Ignore, only used to simplify core modding.
 * @author Glenn
 *
 */
public class UtilMethods
{
	public static ItemStack getBottledItem(World world, int x, int y, int z)
	{
		int blockID = world.getBlockId(x, y, z);
		Block block = Block.blocksList[blockID];
		if(block != null && block instanceof BlockGas)
		{
			return ((BlockGas)block).getBottledItem();
		}
		else
		{
			return new ItemStack(Item.glassBottle);
		}
	}
}