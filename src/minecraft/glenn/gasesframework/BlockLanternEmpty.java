package glenn.gasesframework;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockLanternEmpty extends BlockLantern
{
	public BlockLanternEmpty(int blockID)
	{
		super(blockID, null);
	}
	
	public ItemStack getContainedItemOut()
	{
		return null;
	}
	
	public ItemStack getContainedItemIn()
	{
		return null;
	}
}