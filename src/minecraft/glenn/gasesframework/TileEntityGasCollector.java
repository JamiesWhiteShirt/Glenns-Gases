package glenn.gasesframework;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityGasCollector extends TileEntityPump
{
	private GasType pendingGasType;
	private int pendingGasAmount;
	private int collectionTime;
	
	protected boolean extractFromSides()
    {
		return true;
    }
	
	protected void handleFailedPumpings()
    {
		
    }
	
	public void updateEntity()
    {
		if(collectionTime-- <= 0)
		{
			if(enabled & !worldObj.isRemote & (containedType == null | containedType == GasesFramework.gasTypeAir))
			{
				if(pendingGasAmount < 16)
				{
					int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
					int[] indices = randomIndexArray(this.worldObj.rand);
					for(int i = 0; i < 6; i++)
					{
						int index = indices[i];
						if(index == metadata)
						{
							continue;
						}
						
						int x1 = xCoord + xDirection[index];
						int y1 = yCoord + yDirection[index];
						int z1 = zCoord + zDirection[index];
						
						int directionBlockID = worldObj.getBlockId(x1, y1, z1);
						int directionBlockMetadata = worldObj.getBlockMetadata(x1, y1, z1);
						Block directionBlock = Block.blocksList[directionBlockID];
						
						if(directionBlockID != 0 && directionBlock instanceof BlockGas)
						{
							BlockGas gasBlock = (BlockGas)directionBlock;
							if(gasBlock.type.isIndustrial() && acceptsType(gasBlock.type))
							{
								worldObj.setBlockToAir(x1, y1, z1);
								
								if(gasBlock.type != pendingGasType)
								{
									pendingGasAmount = 0;
								}
								
								pendingGasAmount += 16 - directionBlockMetadata;
								
								pendingGasType = gasBlock.type;
								break;
							}
						}
					}
				}
				
				if(pendingGasAmount >= 16)
				{
					pendingGasAmount -= 16;
					containedType = pendingGasType;
				}
			}
			
			collectionTime = 5;
		}
		
		
		super.updateEntity();
    }
	
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("pendingGasType", pendingGasType != null ? pendingGasType.gasIndex : -1);
		par1NBTTagCompound.setInteger("pendingGasAmount", pendingGasAmount);
		par1NBTTagCompound.setInteger("collectionTime", collectionTime);
	}
	
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		pendingGasAmount = par1NBTTagCompound.getInteger("pendingGasType");
		collectionTime = par1NBTTagCompound.getInteger("collectionTime");
		int i = par1NBTTagCompound.getInteger("pendingGasType");
		if(i == -1)
		{
			pendingGasType = null;
		}
		else
		{
			pendingGasType = GasType.gasTypes[i];
		}
	}
}