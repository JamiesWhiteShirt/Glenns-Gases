package glenn.gasesframework.reaction;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class ReactionSpecialEvent extends ReactionBase
{
	protected boolean removeBlock1;
	protected boolean removeBlock2;
	
	public ReactionSpecialEvent(int priority, Block block1, Block block2, boolean removeBlock1, boolean removeBlock2)
	{
		super(priority, block1, block2);
		this.removeBlock1 = removeBlock1;
		this.removeBlock2 = removeBlock2;
	}

	@Override
	protected boolean reactBlock1(World world, Random random, int blockX, int blockY, int blockZ)
	{
		if(removeBlock1) world.setBlockToAir(blockX, blockY, blockZ);
		return removeBlock1;
	}

	@Override
	protected boolean reactBlock2(World world, Random random, int blockX, int blockY, int blockZ)
	{
		if(removeBlock2) world.setBlockToAir(blockX, blockY, blockZ);
		return removeBlock2;
	}
}