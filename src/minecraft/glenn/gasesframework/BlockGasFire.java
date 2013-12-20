package glenn.gasesframework;

import java.util.Random;

import glenn.gasesframework.reaction.Reaction;
import glenn.gasesframework.reaction.ReactionEmpty;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class BlockGasFire extends BlockGas
{
	public BlockGasFire(int blockID)
	{
		super(blockID);
	}
	
	protected int getDelayForUpdate(World world, int x, int y, int z)
    {
    	Reaction reaction = new ReactionEmpty();
    	int delay = -1;
		
		for(int i = 0; i < 6; i++)
		{
			int xDirection = i < 2 ? i * 2 - 1: 0;
			int yDirection = i < 4 & i >= 2 ? i * 2 - 5: 0;
			int zDirection = i >= 4 ? i * 2 - 9: 0;
			
			int directionBlockID = world.getBlockId(x + xDirection, y + yDirection, z + zDirection);
			Reaction reaction2 = GasesFramework.getReactionForBlocks(blockID, directionBlockID);
			
			if(reaction.isErroneous() || reaction2.priority < reaction.priority)
			{
				reaction = reaction2;
				delay = reaction.getDelay(world, x, y, z, x + xDirection, y + yDirection, z + zDirection);
			}
		}
		
		if(delay == -1)
		{
			delay = 4;
		}
    	
    	return delay;
    }
	
	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (random.nextInt(12) == 0)
		{
			world.playSound((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "fire.fire", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
		}
	}
	
	/**
	 * Called when the block disappears because of evaporation.
	 * @param world
	 * @param i
	 * @param j
	 * @param k
	 */
	protected void onEvaporated(World world, int i, int j, int k)
	{
		if(Block.fire.canPlaceBlockAt(world, i, j, k))
		{
			world.setBlock(i, j, k, Block.fire.blockID);
		}
		else if(world.rand.nextInt(4) == 0)
		{
			world.setBlock(i, j, k, GasesFramework.gasSmoke.blockID, 8, 3);
		}
	}
}