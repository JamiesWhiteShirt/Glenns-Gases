package glenn.gasesframework;

import net.minecraft.world.World;

public interface IGasSource
{
	GasType getGasTypeFromSide(World world, int x, int y, int z, int side);
	GasType takeGasTypeFromSide(World world, int x, int y, int z, int side);
}