package glenn.gasesframework;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenGas extends WorldGenerator
{
    /** The block ID of the ore to be placed using this generator. */
    private int minableBlockId;
    /** The metadata of the ore to be placed using this generator. */
    private int minableBlockMetadata;

    /** The number of blocks to minimally generate. */
    private int numberOfBlocksMin;
    /** The number of blocks to maxmimally generate. */
    private int numberOfBlocksMax;
    private int field_94523_c;
    
    /**
     * Creates a new gas world generator. Gas generators are much like ore generators with the exception of the size being variable, and no generated blocks are visible on cave surfaces.
     * @param blockID - ID of the block to be generated
     * @param blockMetadata - Metadata of the block to be generated
     * @param min - The minimal amount of blocks to generate
     * @param max - The maximal amount of blocks to generate
     */
    public WorldGenGas(int blockID, int blockMetadata, int min, int max)
    {
        this.minableBlockId = blockID;
        this.minableBlockMetadata = blockMetadata;
        numberOfBlocksMin = min;
        numberOfBlocksMax = max;
        this.field_94523_c = Block.stone.blockID;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
    	int numberOfBlocks = numberOfBlocksMin + par2Random.nextInt(numberOfBlocksMax - numberOfBlocksMin);
        float var6 = par2Random.nextFloat() * (float)Math.PI;
        double var7 = (double)((float)(par3 + 8) + MathHelper.sin(var6) * (float)numberOfBlocks / 8.0F);
        double var9 = (double)((float)(par3 + 8) - MathHelper.sin(var6) * (float)numberOfBlocks / 8.0F);
        double var11 = (double)((float)(par5 + 8) + MathHelper.cos(var6) * (float)numberOfBlocks / 8.0F);
        double var13 = (double)((float)(par5 + 8) - MathHelper.cos(var6) * (float)numberOfBlocks / 8.0F);
        double var15 = (double)(par4 + par2Random.nextInt(3) - 2);
        double var17 = (double)(par4 + par2Random.nextInt(3) - 2);

        for (int var19 = 0; var19 <= numberOfBlocks; ++var19)
        {
            double var20 = var7 + (var9 - var7) * (double)var19 / (double)numberOfBlocks;
            double var22 = var15 + (var17 - var15) * (double)var19 / (double)numberOfBlocks;
            double var24 = var11 + (var13 - var11) * (double)var19 / (double)numberOfBlocks;
            double var26 = par2Random.nextDouble() * (double)numberOfBlocks / 16.0D;
            double var28 = (double)(MathHelper.sin((float)var19 * (float)Math.PI / (float)numberOfBlocks) + 1.0F) * var26 + 1.0D;
            double var30 = (double)(MathHelper.sin((float)var19 * (float)Math.PI / (float)numberOfBlocks) + 1.0F) * var26 + 1.0D;
            int var32 = MathHelper.floor_double(var20 - var28 / 2.0D);
            int var33 = MathHelper.floor_double(var22 - var30 / 2.0D);
            int var34 = MathHelper.floor_double(var24 - var28 / 2.0D);
            int var35 = MathHelper.floor_double(var20 + var28 / 2.0D);
            int var36 = MathHelper.floor_double(var22 + var30 / 2.0D);
            int var37 = MathHelper.floor_double(var24 + var28 / 2.0D);

            for (int var38 = var32; var38 <= var35; ++var38)
            {
                double var39 = ((double)var38 + 0.5D - var20) / (var28 / 2.0D);

                if (var39 * var39 < 1.0D)
                {
                    for (int var41 = var33; var41 <= var36; ++var41)
                    {
                        double var42 = ((double)var41 + 0.5D - var22) / (var30 / 2.0D);

                        if (var39 * var39 + var42 * var42 < 1.0D)
                        {
                            for (int var44 = var34; var44 <= var37; ++var44)
                            {
                                double var45 = ((double)var44 + 0.5D - var24) / (var28 / 2.0D);

                                if (var39 * var39 + var42 * var42 + var45 * var45 < 1.0D)
                                {
                                	if(isPlacementValid(par1World, var38, var41, var44))
                                	{
                                		par1World.setBlock(var38, var41, var44, this.minableBlockId, this.minableBlockMetadata, 2);
                                	}
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
    
    protected boolean isPlacementValid(World world, int x, int y, int z)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
    	if(block != null && !block.isGenMineableReplaceable(world, x, y, z, this.field_94523_c))
    	{
    		return false;
    	}
    	
    	for(int i = 0; i < 6; i++)
    	{
    		int xDirection = i == 4 ? -1 : (i == 5 ? 1 : 0);
    		int yDirection = i == 0 ? -1 : (i == 1 ? 1 : 0);
    		int zDirection = i == 2 ? -1 : (i == 3 ? 1 : 0);

			int blockID = world.getBlockId(x + xDirection, y + yDirection, z + zDirection);
			if(blockID != minableBlockId && (blockID == Block.gravel.blockID || blockID == Block.sand.blockID || !world.isBlockOpaqueCube(x + xDirection, y + yDirection, z + zDirection)))
			{
				return false;
			}
    	}
    	
    	return true;
    }
}
