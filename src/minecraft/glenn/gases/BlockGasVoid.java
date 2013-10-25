package glenn.gases;

import java.util.Random;

import net.minecraft.world.World;

public class BlockGasVoid extends BlockGas
{
	public BlockGasVoid(int id)
    {
        super(id);
    }

    protected int getGasDecay(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	return par1World.getBlockLightValue(par2, par3 + 1, par4) > 10 ? (par1World.getBlockLightValue(par2, par3 + 1, par4) - 10) : 0;
    }
}
