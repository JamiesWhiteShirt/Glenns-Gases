package glenn.gases;

import glenn.gasesframework.BlockGas;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;

public class BlockGasElectric extends BlockGas
{
	public BlockGasElectric(int id)
	{
		super(id);
	}
	
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
		if(!par1World.isRemote && par5Random.nextInt(8) == 0)
		{
			par1World.spawnEntityInWorld(new EntitySmallLightning(par1World, par2 + 0.5D, par3 + 0.5D, par4 + 0.5D));
		}
		
		super.updateTick(par1World, par2, par3, par4, par5Random);
    }
}
