package glenn.gases.reaction;

import glenn.gases.BlockGas;
import glenn.gases.Gases;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ReactionCorrosion extends Reaction
{
	protected BlockGas gasBlock;
	
	public ReactionCorrosion(BlockGas gasBlock)
	{
		super(30);
		this.gasBlock = gasBlock;
	}

	@Override
	public boolean isErroneous()
	{
		return false;
	}

	@Override
	public boolean is(int block1ID, int block2ID)
	{
		if(block1ID == gasBlock.blockID & block2ID != 0)
		{
			Block block = Block.blocksList[block2ID];
			return !(block instanceof BlockGas) & block.blockHardness != -1.0F & block.blockHardness < Gases.maxHardnessForCorrosion;
		}
		else if(block1ID != 0)
		{
			Block block = Block.blocksList[block1ID];
			return !(block instanceof BlockGas) & block.blockHardness != -1.0F & block.blockHardness < Gases.maxHardnessForCorrosion;
		}
		
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
		int blockID = world.getBlockId(blockX, blockY, blockZ);
		if(blockID != gasBlock.blockID & random.nextInt(10) == 0)
		{
			if(random.nextInt(10) == 0)
			{
				Block.blocksList[blockID].dropBlockAsItem(world, blockX, blockY, blockZ, world.getBlockMetadata(blockX, blockY, blockZ), 0);
			}
			world.setBlockToAir(blockX, blockY, blockZ);
			return true;
		} else
		{
			return false;
		}
	}

	@Override
	protected boolean reactBlock2(World world, Random random, int blockX, int blockY, int blockZ)
	{
		int blockID = world.getBlockId(blockX, blockY, blockZ);
		if(blockID != gasBlock.blockID & random.nextInt(10) == 0)
		{
			if(random.nextInt(10) == 0)
			{
				Block.blocksList[blockID].dropBlockAsItem(world, blockX, blockY, blockZ, world.getBlockMetadata(blockX, blockY, blockZ), 0);
			}
			world.setBlockToAir(blockX, blockY, blockZ);
			return true;
		} else
		{
			return false;
		}
	}
}