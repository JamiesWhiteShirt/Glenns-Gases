package glenn.gasesframework;

import net.minecraft.world.World;

public interface ISample
{
	/**
	 * Called upon right clicked with sampler. Returns the new gas type to be used for the sampler.
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