package glenn.gases.reaction;

import java.util.Random;

import glenn.gases.BlockGas;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ReactionGasMix extends ReactionBase
{
	protected BlockGas newBlock;
	
	public ReactionGasMix(int priority, BlockGas block1, BlockGas block2, BlockGas newBlock)
	{
		super(priority, block1, block2);
		this.newBlock = newBlock;
	}
	
	@Override
	public boolean reactBoth(World world, Random random, int block1X, int block1Y, int block1Z, int block2X, int block2Y, int block2Z)
	{
		return false;
	}

	protected boolean reactBlock1(World world, Random random, int blockX, int blockY, int blockZ)
	{
		int metadata = world.getBlockMetadata(blockX, blockY, blockZ);
		world.setBlock(blockX, blockY, blockZ, newBlock.blockID, metadata, 3);
		return true;
	}

	protected boolean reactBlock2(World world, Random random, int blockX, int blockY, int blockZ)
	{
		int metadata = world.getBlockMetadata(blockX, blockY, blockZ);
		world.setBlock(blockX, blockY, blockZ, newBlock.blockID, metadata, 3);
		return true;
	}
}