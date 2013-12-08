package glenn.gasesframework;

import net.minecraft.world.World;

/**
 * An interface for interactivity with gas samplers.
 * @author Glenn
 *
 */
public interface ISample
{
	/**
	 * Called when right clicked with sampler. Returns the new gas type to be used for the sampler. Return "in" if nothing is sampled.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param in - The current gas type of the sampler
	 * @param excludes - True if the sampler is an excluding one
	 * @return
	 */
	public GasType sampleInteraction(World world, int x, int y, int z, GasType in, boolean excludes);
}