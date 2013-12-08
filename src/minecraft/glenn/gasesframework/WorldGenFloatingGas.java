package glenn.gasesframework;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class WorldGenFloatingGas extends WorldGenGas
{
	/**
	 * Constructs a new floating gas generator. Floating gas generators are much like gas generators, but will only replace air.
	 * @param par1
	 * @param par2
	 * @param par3
	 * @param par4
	 */
	public WorldGenFloatingGas(int blockID, int blockMetadata, int min, int max)
	{
		super(blockID, blockMetadata, min, max);
	}
	
	protected boolean isPlacementValid(World world, int x, int y, int z)
    {
    	return world.getBlockId(x, y, z) == 0;
    }
}
