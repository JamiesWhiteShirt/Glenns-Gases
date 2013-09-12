package glenn.gases.reaction;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ReactionEmpty extends Reaction
{
	public ReactionEmpty()
	{
		super(0);
	}
	
	public boolean isErroneous()
	{
		return true;
	}
	
	public boolean is(int block1ID, int block2ID)
	{
		return false;
	}

	@Override
	protected boolean reactBoth(World world, Random random, int block1x, int block1y, int block1z, int block2x, int block2y, int block2z)
	{
		return false;
	}

	@Override
	protected boolean reactBlock1(World world, Random random, int blockX, int blockY, int blockZ)
	{
		return false;
	}

	@Override
	protected boolean reactBlock2(World world, Random random, int blockX, int blockY, int blockZ)
	{
		return false;
	}

	@Override
	public boolean reactIfPossible(World world, Random random, int block1x, int block1y, int block1z, int block2x, int block2y, int block2z)
	{
		return false;
	}
}