package glenn.gases;

import glenn.gasesframework.util.DVec;
import glenn.gasesframework.util.IVec;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntitySmallLightning extends EntityWeatherEffect
{
	public int timeUntilChange;
	public int totalTimeUntilChange;
	public DVec vertices[];
	public DVec momentum[];
	public DVec acceleration[];
	
	public EntitySmallLightning(World world)
	{
		super(world);
	}

    public EntitySmallLightning(World par1World, double par2, double par4, double par6)
    {
    	this(par1World);
        this.setLocationAndAngles(par2, par4, par6, 0.0F, 0.0F);
    }
    
    private void findNewTarget()
    {
    	DVec target = new DVec();
    	
    	timeUntilChange = this.rand.nextInt(15) + 5;
        totalTimeUntilChange = timeUntilChange;
        
		if(this.rand.nextInt(5) == 0)
    	{
    		List nearbyEntities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(posX - 6.0D, posY - 6.0D, posZ - 6.0D, posX + 6.0D, posY + 6.0D, posZ + 6.0D));
    		
    		if(nearbyEntities.size() > 0)
    		{
    			EntityLivingBase e = (EntityLivingBase)nearbyEntities.get(this.rand.nextInt(nearbyEntities.size()));
    			e.attackEntityFrom(Gases.lightningDamageSource, 2.0F);
    			
    			target.set(e.posX - posX, e.posY - posY, e.posZ - posZ);
    		}
    	}
    	
    	if(target.isNull())
    	{
    		IVec iPos = new IVec(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
            
            IVec iTarget = new IVec();
            for(int i = 0; i < 100; i++)
            {
            	iTarget.set(iPos).add(this.rand.nextInt(12) - 6, this.rand.nextInt(12) - 6, this.rand.nextInt(12) - 6);
            	
            	if(this.worldObj.isBlockOpaqueCube(iTarget.x, iTarget.y, iTarget.z)) break;
            }
            
            iTarget.subtract(iPos);
            target.set(iTarget.x, iTarget.y, iTarget.z);
    	}
        
    	double lengthPower = target.length() / 32.0D;
        vertices = new DVec[10];
        momentum = new DVec[10];
        acceleration = new DVec[10];
        
        for(int i = 0; i < vertices.length; i++)
        {
        	double percentage = (double)i / (vertices.length - 1);
        	double momentumPower = lengthPower * (percentage - percentage * percentage);
        	
        	vertices[i] = target.scaled(percentage).add(DVec.randomNormalizedVec(this.rand).scale(momentumPower * 2.0D));
        	momentum[i] = DVec.randomNormalizedVec(this.rand).scale(momentumPower);
        	acceleration[i] = DVec.randomNormalizedVec(this.rand).scale(momentumPower * 0.2D);
        }
    }
    
    private void moveBeam()
    {
    	for(int i = 0; i < vertices.length; i++)
    	{
    		momentum[i].add(acceleration[i]);
    		vertices[i].add(momentum[i]);
    	}
    }

    public void onUpdate()
    {
        super.onUpdate();
        
        if(--timeUntilChange < 0)
        {
        	if(this.rand.nextInt(5) != 0)
        	{
        		findNewTarget();
        		this.worldObj.playSoundAtEntity(this, "gases:effect.spark", 1.0F, 0.9F + this.rand.nextFloat() * 0.2F);
        	}
        	else
        	{
        		this.worldObj.removeEntity(this);
        	}
        } else
        {
        	moveBeam();
        }
    }

    protected void entityInit()
    {
    	findNewTarget();
    }

    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	
    }

    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderVec3D(Vec3 par1Vec3)
    {
        return true;
    }
}
