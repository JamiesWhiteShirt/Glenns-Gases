package glenn.gases;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTank extends TileEntity
{
	public GasType containedType;
	public int amount;
	
	public TileEntityTank()
	{
		amount = 0;
	}
	
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		amount = par1NBTTagCompound.getInteger("amount");
		int i = par1NBTTagCompound.getInteger("containedType");
		if(i == -1)
		{
			containedType = null;
		}
		else
		{
			containedType = GasType.gasTypes[i];
		}
	}
	
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("amount", amount);
		par1NBTTagCompound.setInteger("containedType", containedType != null ? containedType.gasIndex : -1);
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
	
	public void updateEntity()
    {
		
    }
	
	public int getGasCap()
	{
		if(containedType == null)
		{
			return 0;
		}
		else
		{
			return (64 - containedType.density) * 2 + 16;
		}
	}
	
	public boolean increment(GasType gasType)
	{
		if((containedType == null | containedType == gasType) & gasType != Gases.gasTypeAir)
		{
			containedType = gasType;
			if(++amount <= getGasCap())
			{
				return true;
			}
			else
			{
				amount = getGasCap();
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	public boolean decrement()
	{
		if(amount-- > 0)
		{
			if(amount == 0)
			{
				containedType = null;
			}
			return true;
		}
		else
		{
			amount = 0;
			return false;
		}
	}
}