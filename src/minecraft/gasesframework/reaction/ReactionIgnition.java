package glenn.gasesframework.reaction;

import glenn.gasesframework.GasesFramework;
import glenn.gasesframework.BlockGas;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ReactionIgnition extends Reaction
{
	public ReactionIgnition()
	{
		super(10);
	}
	
	public int getDelay(World world, int block1X, int block1Y, int block1Z, int block2X, int block2Y, int block2Z)
	{
		int block1ID = world.getBlockId(block1X, block1Y, block1Z);
		int block2ID = world.getBlockId(block2X, block2Y, block2Z);
		
		if(GasesFramework.isIgnitionBlock(block2ID))
		{
			if(Block.blocksList[block1ID] instanceof BlockGas)
			{
				return ((BlockGas)Block.blocksList[block1ID]).type.combustibility.fireSpreadRate;
			}
		}
		else if(GasesFramework.isIgnitionBlock(block1ID))
		{
			if(Block.blocksList[block2ID] instanceof BlockGas)
			{
				return ((BlockGas)Block.blocksList[block2ID]).type.combustibility.fireSpreadRate;
			}
		}
		
		return -1;
	}
	
	public boolean isErroneous()
	{
		return false;
	}
	
	public boolean is(int block1ID, int block2ID)
	{
		if(GasesFramework.isIgnitionBlock(block2ID))
		{
			Block block = Block.blocksList[block1ID];
			return block instanceof BlockGas && ((BlockGas)block).canCombustNormally();
		}
		else if(GasesFramework.isIgnitionBlock(block1ID))
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
		
		if(GasesFramework.isIgnitionBlock(block2ID))
		{
			if(Block.blocksList[block1ID] instanceof BlockGas)
			{
				((BlockGas)Block.blocksList[block1ID]).onFire(world, block1X, block1Y, block1Z, random);
				return true;
			}
		}
		else if(GasesFramework.isIgnitionBlock(block1ID))
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