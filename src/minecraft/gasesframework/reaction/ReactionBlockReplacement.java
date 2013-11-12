package glenn.gasesframework.reaction;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ReactionBlockReplacement extends ReactionBase
{
	protected int block1NewID;
	protected int block1NewMetadata;
	protected int block2NewID;
	protected int block2NewMetadata;
	
	public ReactionBlockReplacement(int priority, Block block1, Block block2, int block1NewID, int block1NewMetadata, int block2NewID, int block2NewMetadata)
	{
		super(priority, block1, block2);
		this.block1NewID = block1NewID;
		this.block1NewMetadata = block1NewMetadata;
		this.block2NewID = block2NewID;
		this.block2NewMetadata = block2NewMetadata;
	}
	
	public ReactionBlockReplacement(int priority, Block block1, Block block2, int block1NewID, int block2NewID)
	{
		super(priority, block1, block2);
		this.block1NewID = block1NewID;
		this.block1NewMetadata = 0;
		this.block2NewID = block2NewID;
		this.block2NewMetadata = 0;
	}

	@Override
	protected boolean reactBoth(World world, Random random, int block1x, int block1y, int block1z, int block2x, int block2y, int block2z)
	{
		return false;
	}

	@Override
	protected boolean reactBlock1(World world, Random random, int blockX, int blockY, int blockZ)
	{
		if(block1NewID >= 0)
		{
			world.setBlock(blockX, blockY, blockZ, block1NewID, block1NewMetadata, 3);
			return true;
		}
		return false;
	}

	@Override
	protected boolean reactBlock2(World world, Random random, int blockX, int blockY, int blockZ)
	{
		if(block2NewID >= 0)
		{
			world.setBlock(blockX, blockY, blockZ, block2NewID, block1NewMetadata, 3);
			return true;
		}
		return false;
	}
}