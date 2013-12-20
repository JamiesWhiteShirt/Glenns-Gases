package glenn.gasesframework;

import glenn.gasesframework.util.DVec;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPump extends TileEntity
{
	protected static final int[] xDirection = new int[]{
		0, 0, 1, -1, 0, 0
	};
	protected static final int[] yDirection = new int[]{
		1, -1, 0, 0, 0, 0
	};
	protected static final int[] zDirection = new int[]{
		0, 0, 0, 0, 1, -1
	};
	
	private int pumpTime;
	private int failedPumpings;
	public boolean excludes;
	public GasType containedType;
	public GasType filterType;
	
	public TileEntityPump()
	{
		pumpTime = 25;
		excludes = false;
	}
	
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		pumpTime = par1NBTTagCompound.getInteger("pumpTime");
		failedPumpings = par1NBTTagCompound.getInteger("failedPumpings");
		excludes = par1NBTTagCompound.getBoolean("excludes");
		int i = par1NBTTagCompound.getInteger("containedType");
		if(i == -1)
		{
			containedType = null;
		}
		else
		{
			containedType = GasType.gasTypes[i];
		}
		
		i = par1NBTTagCompound.getInteger("filterType");
		if(i == -1)
		{
			filterType = null;
		}
		else
		{
			filterType = GasType.gasTypes[i];
		}
	}
	
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("pumpTime", pumpTime);
		par1NBTTagCompound.setInteger("failedPumpings", failedPumpings);
		par1NBTTagCompound.setBoolean("excludes", excludes);
		par1NBTTagCompound.setInteger("containedType", containedType != null ? containedType.gasIndex : -1);
		par1NBTTagCompound.setInteger("filterType", filterType != null ? filterType.gasIndex : -1);
	}
	
	/**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
    	NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }
	
	/**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
    {
    	readFromNBT(packet.data);
    }
    
    public boolean acceptsType(GasType gasType)
    {
    	if(filterType == null)
    	{
    		return true;
    	}
    	else
    	{
    		return filterType == gasType ^ excludes;
    	}
    }
	
    protected boolean extractFromSides()
    {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    	int[] indices = randomIndexArray(this.worldObj.rand);
		for(int i = 0; i < 6 & containedType == null; i++)
		{
			int index = indices[i];
			if(index == metadata)
			{
				continue;
			}
			
			int x1 = xCoord + xDirection[index];
			int y1 = yCoord + yDirection[index];
			int z1 = zCoord + zDirection[index];
			
			Block directionBlock = Block.blocksList[worldObj.getBlockId(x1, y1, z1)];
			
			if(directionBlock != null && IGasSource.class.isAssignableFrom(directionBlock.getClass()))
			{
				IGasSource gasSource = (IGasSource)directionBlock;
				if(acceptsType(gasSource.getGasTypeFromSide(worldObj, x1, y1, z1, index)))
				{
					containedType = gasSource.takeGasTypeFromSide(worldObj, x1, y1, z1, index);
				}
				return false;
			}
		}
		
		return true;
    }
    
    protected void handleFailedPumpings()
    {
    	if(failedPumpings > 20 && worldObj.rand.nextInt(1000 / ((failedPumpings - 20) * (failedPumpings - 20)) + 1) == 0)
		{
			DVec velocity = DVec.randomNormalizedVec(worldObj.rand).scale(0.25D);
			worldObj.spawnParticle("largesmoke", xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, velocity.x, velocity.y, velocity.z);
		}
		
		if(failedPumpings > 60)
		{
			worldObj.createExplosion(null, xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, 3.0F, true);
		}
    }
    
	public void updateEntity()
    {
		if(pumpTime-- <= 0)
		{
			if(!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
			{
				BlockGasPump block = (BlockGasPump)Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)];
				int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
				boolean canPumpAir = extractFromSides();
				
				if(containedType == null & canPumpAir)
				{
					containedType = GasesFramework.gasTypeAir;
				}
				
				if(containedType != null)
				{
					int x1 = xCoord + xDirection[metadata];
					int y1 = yCoord + yDirection[metadata];
					int z1 = zCoord + zDirection[metadata];
					
					int directionBlockID = worldObj.getBlockId(x1, y1, z1);
					Block directionBlock = Block.blocksList[directionBlockID];
					boolean success = false;
					
					if(directionBlockID == 0)
					{
						if(containedType.gasBlock != null)
						{
							worldObj.setBlock(x1, y1, z1, containedType.gasBlock.blockID);
						}
						success = true;
					}
					else if(directionBlock != null && IGasReceptor.class.isAssignableFrom(directionBlock.getClass()))
					{
						success = ((IGasReceptor)directionBlock).receiveGas(worldObj, x1, y1, z1, metadata < 2 ? (1 - metadata) : metadata, containedType);
					}
					
					if(success)
					{
						containedType = null;
						failedPumpings = 0;
					}
					else
					{
						failedPumpings++;
					}
				}
			}
			else
			{
				failedPumpings = 0;
			}
			
			
			pumpTime = 25;
		}
		
		handleFailedPumpings();
    }
	
	protected int[] randomIndexArray(Random random)
	{
		int[] array = new int[6];
		
		for(int i = 0; i < 6; i++)
    	{
    		while(true)
    		{
    			int index = random.nextInt(6);
    			if(array[index] == 0)
    			{
    				array[index] = i;
    				break;
    			}
    		}
    	}
		
		return array;
	}
}