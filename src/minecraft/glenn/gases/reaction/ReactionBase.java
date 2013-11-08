package glenn.gases.reaction;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class ReactionBase extends Reaction
{
	protected final Block block1;
	protected final Block block2;
	
	/**
	 * Constructs a new reaction between the specified blocks. One of the blocks should be a {@link glenn.gases.BlockGas}, otherwise the reaction will never occur
	 * @param priority - The priority of this reaction. If a gas touches several blocks it may react with, it will priorify the reaction of the greatest priority. For example, an ignition reaction uses priority level 10
	 * @param delay - The delay of the reaction. If set below 0, it will happen at the same rate as the flow of the block
	 * @param block1
	 * @param block2
	 */
	public ReactionBase(int priority, Block block1, Block block2)
	{
		super(priority);
		this.block1 = block1;
		this.block2 = block2;
	}
	
	/**
	 * Returns true if this reaction is erroneous and cannot be used
	 * @return
	 */
	public boolean isErroneous()
	{
		return this.block1.blockID == this.block2.blockID;
	}
	
	public boolean is(int block1ID, int block2ID)
	{
		return (block1.blockID == block1ID & block2.blockID == block2ID) | (block1.blockID == block2ID & block2.blockID == block1ID);
	}
}
