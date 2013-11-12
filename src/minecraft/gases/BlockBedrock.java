package glenn.gases;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockBedrock extends Block
{
	public BlockBedrock(int id, Material material)
    {
        super(id, material);

        this.setTickRandomly(true);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return 1;
    }

    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	int xDirection;
    	int yDirection;
    	int zDirection;
    	do
    	{
    		xDirection = par5Random.nextInt(3) - 1;
    		yDirection = par5Random.nextInt(3) - 1;
    		zDirection = par5Random.nextInt(3) - 1;
    	} while(xDirection == 0 & yDirection == 0 & zDirection == 0);

    	if(par1World.getBlockLightValue(par2 + xDirection, par3 + yDirection, par4 + zDirection) > 10 | par3 + yDirection >= Gases.voidGasMaxHeight)
    	{
    		return;
    	}

    	int directionBlockID = par1World.getBlockId(par2 + xDirection, par3 + yDirection, par4 + zDirection);

    	if(directionBlockID == 0)
    	{
    		par1World.setBlock(par2 + xDirection, par3 + yDirection, par4 + zDirection, Gases.gasVoid.blockID, 8, 3);
    	} else if(directionBlockID == Gases.gasVoid.blockID)
    	{
    		int directionBlockMetadata = par1World.getBlockMetadata(par2 + xDirection, par3 + yDirection, par4 + zDirection) - 8;

    		if(directionBlockMetadata < 0)
    		{
    			directionBlockMetadata = 0;
    		}

    		par1World.setBlockMetadataWithNotify(par2 + xDirection, par3 + yDirection, par4 + zDirection, directionBlockMetadata, 3);
    	}
    }
}
