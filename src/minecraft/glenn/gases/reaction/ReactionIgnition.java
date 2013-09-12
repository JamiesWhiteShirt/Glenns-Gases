package glenn.gases.reaction;

import glenn.gases.Gases;
import glenn.gases.BlockGas;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ReactionIgnition extends Reaction
{
	public ReactionIgnition()
	{
		super(10);
	}
	
	public static int getDelay()
	{
		return 8;
	}
	
	public boolean isErroneous()
	{
		return false;
	}
	
	public boolean is(int block1ID, int block2ID)
	{
		if(Gases.isIgnitionBlock(block2ID))
		{
			Block block = Block.blocksList[block1ID];
			return block instanceof BlockGas && ((BlockGas)block).canCombustNormally();
		}
		else if(Gases.isIgnitionBlock(block1ID))
		{
			Block block = Block.blocksList[block2ID];
			return block instanceof BlockGas && ((BlockGas)block).canCombustNormally();
		}
		
		return false;
	}
	
	public boolean reactIfPossible(World world, Random random, int block1X, int block1Y, int block1Z, int block2X, int block2Y, int block2Z)
	{
		int block1ID = world.getBlockId(block1X, block1Y, block1Z);
		int block2ID = world.getBlockId(block2X, block2Y, block2Z);
		
		if(Gases.isIgnitionBlock(block2ID))
		{
			if(Block.blocksList[block1ID] instanceof BlockGas)
			{
				((BlockGas)Block.blocksList[block1ID]).onFire(world, block1X, block1Y, block1Z, random);
				return true;
			}
		}
		else if(Gases.isIgnitionBlock(block1ID))
		{
			if(Block.blocksList[block2ID] instanceof BlockGas)
			{
				((BlockGas)Block.blocksList[block2ID]).onFire(world, block2X, block2Y, block2Z, random);
				return true;
			}
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
		return false;
	}

	@Override
	protected boolean reactBlock2(World world, Random random, int blockX, int blockY, int blockZ)
	{
		return false;
	}
}