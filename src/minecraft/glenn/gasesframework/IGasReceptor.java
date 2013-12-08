package glenn.gasesframework;

import java.util.Random;

import net.minecraft.world.World;

/**
 * An interface for interactivity with gas pipe systems. A gas receptor is able to receive gas from a pipe.
 * @author Glenn
 *
 */
public interface IGasReceptor
{
	/**
	 * This method is called when gas is attempted to be pumped inside this block. Returns true if this gas type is accepted and consumed.
	 * @param world - The World object
	 * @param x
	 * @param y
	 * @param z
	 * @param side - The side the received gas is coming from
	 * @param gasType - The type of gas being received
	 * @return
	 */
	boolean receiveGas(World world, int x, int y, int z, int side, GasType gasType);
}