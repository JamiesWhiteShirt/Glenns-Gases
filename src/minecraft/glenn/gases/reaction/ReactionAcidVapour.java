package glenn.gases.reaction;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import glenn.gases.BlockGas;
import glenn.gases.Gases;

public class ReactionAcidVapour extends ReactionBase
{
	public ReactionAcidVapour(int priority, Block block1, Block block2)
	{
		super(priority, block1, block2);
	}
	
	@Override
	protected boolean reactBoth(World world, Random random, int block1x, int block1y, int block1z, int block2x, int block2y, int block2z)
	{
		int block1ID = world.getBlockId(block1x, block1y, block1z);
		int block2ID = world.getBlockId(block2x, block2y, block2z);
		int block1Metadata = world.getBlockMetadata(block1x, block1y, block1z);
		int block2Metadata = world.getBlockMetadata(block2x, block2y, block2z);
		
		if((block1ID == block1.blockID | block1ID == block2.blockID) && Block.blocksList[block1ID] instanceof BlockGas)
		{
			world.setBlock(block1x, block1y, block1z, Gases.gasAcidVapour.blockID, block1Metadata, 3);
		}
		
		if((block2ID == block1.blockID | block2ID == block2.blockID) && Block.blocksList[block2ID] instanceof BlockGas)
		{
			world.setBlock(block2x, block2y, block2z, Gases.gasAcidVapour.blockID, block2Metadata, 3);
		}
		
		return true;
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