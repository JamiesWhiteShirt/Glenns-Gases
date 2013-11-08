package glenn.gases;

import java.util.Random;

import net.minecraft.world.World;

public interface IGasReceptor
{
	boolean receiveGas(World world, int x, int y, int z, int side, GasType gasType);
}