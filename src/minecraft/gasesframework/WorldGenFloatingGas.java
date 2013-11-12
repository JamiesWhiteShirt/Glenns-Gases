package glenn.gasesframework;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class WorldGenFloatingGas extends WorldGenGas
{
	public WorldGenFloatingGas(int par1, int par2, int par3, int par4)
	{
		super(par1, par2, par3, par4);
	}
	
	protected boolean isPlacementValid(World world, int x, int y, int z)
    {
    	return world.getBlockId(x, y, z) == 0;
    }
}
