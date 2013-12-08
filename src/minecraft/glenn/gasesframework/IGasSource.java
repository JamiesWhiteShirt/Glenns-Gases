package glenn.gasesframework;

import net.minecraft.world.World;

/**
 * An interface for interactivity with gas pipe systems. A gas source can have gas extracted from one or more of its sides.
 * @author Glenn
 *
 */
public interface IGasSource
{
	/**
	 * Returns the gas type this gas source will give from a certain side. Return null if no gas will be extracted.
	 * @param world - The world object
	 * @param x
	 * @param y
	 * @param z
	 * @param side - The side gas is attempted to be extracted from.
	 * @return
	 */
	GasType getGasTypeFromSide(World world, int x, int y, int z, int side);
	/**
	 * Extracts the gas then returns the gas type this gas source will give from a certain side. Return null if no gas was extracted.
	 * @param world - The world object
	 * @param x
	 * @param y
	 * @param z
	 * @param side - The side gas was extracted from.
	 * @return
	 */
	GasType takeGasTypeFromSide(World world, int x, int y, int z, int side);
}