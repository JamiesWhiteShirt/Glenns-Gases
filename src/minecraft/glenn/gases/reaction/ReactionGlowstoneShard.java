package glenn.gases.reaction;

import glenn.gases.EntityGlowstoneShard;
import glenn.gases.EntitySmallLightning;
import glenn.gases.util.DVec;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;

public class ReactionGlowstoneShard extends ReactionSpecialEvent
{
	
	public ReactionGlowstoneShard(Block block1, Block block2)
	{
		super(20, block1, block2, true, true);
	}
	
	public static int getDelay()
	{
		return 0;
	}

	protected boolean reactBoth(World world, Random random, int block1x, int block1y, int block1z, int block2x, int block2y, int block2z)
	{
    	//world.createExplosion(null, (double)(block1x + block2x) * 2.0D + 0.5D, (double)(block1y + block2y) * 2.0D + 0.5D, (double)(block1z + block2z) * 2.0D + 0.5D, 2.5F, true);
		//world.spawnEntityInWorld(new EntitySmallLightning(world, (double)(block1x + block2x) * 2.0D + 0.5D, (double)(block1y + block2y) * 2.0D + 0.5D, (double)(block1z + block2z) * 2.0D + 0.5D));
		//world.addWeatherEffect(new EntityLightningBolt(world, (double)(block1x + block2x) * 2.0D + 0.5D, (double)(block1y + block2y) * 2.0D + 0.5D, (double)(block1z + block2z) * 2.0D + 0.5D));
		
		if(!world.isRemote)
		{
			double x = (block1x + block2x + 1) * 0.5D;
			double y = (block1y + block2y + 1) * 0.5D;
			double z = (block1z + block2z + 1) * 0.5D;
			
			for(int i = 0; i < 5; i++)
	    	{
	    		EntityGlowstoneShard shard = new EntityGlowstoneShard(world, x, y, z);
		    	DVec vec = DVec.randomNormalizedVec(random).scale(3.0D);
		    	shard.setVelocity(vec.x, vec.y, vec.z);
		    	world.spawnEntityInWorld(shard);
	    	}
		}
		
		return true;
	}
}
