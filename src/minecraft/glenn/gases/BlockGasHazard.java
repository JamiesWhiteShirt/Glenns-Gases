package glenn.gases;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockGasHazard extends BlockGas
{
	public float damage;

	public BlockGasHazard(int id, int color, int opacity, int density, Combustibility combustibility, float damage)
    {
        super(id, color, opacity, density, combustibility);
        this.damage = damage;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    /*public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getAABBPool().getAABB((double)par2, (double)par3, (double)par4, (double)par2 + 1.0D, (double)par3 + 1.0D, (double)par4 + 1.0D);
    }*/

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
    	int metadata = par1World.getBlockMetadata(par2, par3, par4);
    	double minY = 0.0D;
    	double maxY = 1.0D;

    	if(density > 0)
    	{
    		maxY = 1.0D - (16.0D - (double)metadata) / 16.0D;
    	} else if(density < 0)
    	{
    		minY = (16.0D - (double)metadata) / 16.0D;
    	}

    	return AxisAlignedBB.getAABBPool().getAABB((double)par2, (double)par3 + minY, (double)par4, (double)par2 + 1.0D, (double)par3 + maxY, (double)par4 + 1.0D);
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        par5Entity.attackEntityFrom(DamageSource.cactus, damage);
    }
}