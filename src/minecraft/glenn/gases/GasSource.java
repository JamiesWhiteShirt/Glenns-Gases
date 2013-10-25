package glenn.gases;

import net.minecraft.world.World;

public interface GasSource
{
	GasType getGasTypeFromSide(World world, int x, int y, int z, int side);
	GasType takeGasTypeFromSide(World world, int x, int y, int z, int side);
}