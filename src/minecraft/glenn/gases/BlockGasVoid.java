package glenn.gases;

import java.util.Random;

import net.minecraft.world.World;

public class BlockGasVoid extends BlockGasHazard
{
	public BlockGasVoid(int id, int color, int opacity, int density, Combustibility combustibility, float damage)
    {
        super(id, color, opacity, density, combustibility, damage);
    }

    protected int getGasDecay(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	return par1World.getBlockLightValue(par2, par3 + 1, par4) > 10 ? (par1World.getBlockLightValue(par2, par3 + 1, par4) - 10) : 0;
    }
}
