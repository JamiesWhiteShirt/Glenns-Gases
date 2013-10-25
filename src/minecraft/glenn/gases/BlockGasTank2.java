package glenn.gases;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockGasTank2 extends Block implements GasSource
{
	public BlockGasTank2(int blockID)
	{
		super(blockID, Material.circuits);
	}
	
	@Override
	public GasType getGasTypeFromSide(World world, int x, int y, int z, int side)
	{
		return Gases.gasTypeRisingFlammable;
	}
	
	@Override
	public GasType takeGasTypeFromSide(World world, int x, int y, int z, int side)
	{
		return Gases.gasTypeRisingFlammable;
	}
}