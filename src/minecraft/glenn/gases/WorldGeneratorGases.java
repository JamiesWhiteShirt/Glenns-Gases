package glenn.gases;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGeneratorGases implements IWorldGenerator
{
	private Random randomGenerator;
	private int chunk_X;
	private int chunk_Z;
	private World currentWorld;

    private WorldGenerator risingFlammableGasGen;
    private WorldGenerator fallingExplosiveGasGen;
    private WorldGenerator nitrousGasGen;
    private WorldGenerator electricGasGen;
    private WorldGenerator corrosiveGasGen;

    public WorldGeneratorGases()
    {
    	this.risingFlammableGasGen = new WorldGenGas(Gases.gasRisingFlammable.blockID, 0, 16, 32);
    	this.fallingExplosiveGasGen = new WorldGenGas(Gases.gasFallingExplosive.blockID, 0, 16, 32);
    	this.nitrousGasGen = new WorldGenGas(Gases.gasNitrous.blockID, 0, 12, 24);
    	this.electricGasGen = new WorldGenFloatingGas(Gases.gasElectric.blockID, 4, 16, 32);
    	this.corrosiveGasGen = new WorldGenFloatingGas(Gases.gasCorrosive.blockID, 4, 16, 32);
    }

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		this.randomGenerator = random;
		this.chunk_X = chunkX * 16;
		this.chunk_Z = chunkZ * 16;
		this.currentWorld = world;
		
		if(chunkGenerator instanceof ChunkProviderHell)
		{
			if(Gases.generateElectricGas) this.genOreWithChance(4, this.electricGasGen, 40, 128, 4);
			if(Gases.generateCorrosiveGas) this.genOreWithChance(4, this.corrosiveGasGen, 40, 128, 4);
		}
		else if(chunkGenerator instanceof ChunkProviderGenerate)
		{
			if(Gases.generateGreenGas) this.genStandardOre1(6, this.risingFlammableGasGen, 16, 48);
			if(Gases.generateRedGas) this.genStandardOre1(3, this.fallingExplosiveGasGen, 0, 16);
			if(Gases.generateNitrousGas) this.genStandardOre1(3, this.nitrousGasGen, 0, 16);
		}
	}

    /**
     * Standard ore generation helper. Generates most ores.
     */
    protected void genStandardOre1(int par1, WorldGenerator par2WorldGenerator, int par3, int par4)
    {
        for (int var5 = 0; var5 < par1; ++var5)
        {
            int var6 = this.chunk_X + this.randomGenerator.nextInt(16);
            int var7 = this.randomGenerator.nextInt(par4 - par3) + par3;
            int var8 = this.chunk_Z + this.randomGenerator.nextInt(16);
            par2WorldGenerator.generate(this.currentWorld, this.randomGenerator, var6, var7, var8);
        }
    }
    
    protected void genOreWithChance(int par1, WorldGenerator par2WorldGenerator, int par3, int par4, int par5)
    {
    	if(this.randomGenerator.nextInt(par1) == 0)
    	{
    		int var6 = this.chunk_X + this.randomGenerator.nextInt(16);
            int var7 = this.randomGenerator.nextInt(par4 - par3) + par3;
            int var8 = this.chunk_Z + this.randomGenerator.nextInt(16);
            for(int i = 0; i < par5; i++)
        	{
            	par2WorldGenerator.generate(this.currentWorld, this.randomGenerator, var6 + this.randomGenerator.nextInt(10) - 5, var7 + this.randomGenerator.nextInt(10) - 5, var8 + this.randomGenerator.nextInt(10) - 5);
        	}
    	}
    }
}