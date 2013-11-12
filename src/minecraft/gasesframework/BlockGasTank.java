package glenn.gasesframework;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGasTank extends Block implements IGasSource, IGasReceptor, ITileEntityProvider, ISample
{
	public Icon side;
	public Icon top;
	public Icon inside;
	
	public BlockGasTank(int blockID)
	{
		super(blockID, Material.iron);
	}
	
	@SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        side = par1IconRegister.registerIcon(this.getTextureName() + "_side");
        top = par1IconRegister.registerIcon(this.getTextureName() + "_top");
        inside = par1IconRegister.registerIcon(this.getTextureName() + "_inside");
    }
	
	@Override
	public GasType getGasTypeFromSide(World world, int x, int y, int z, int side)
	{
		TileEntityTank tileEntity = (TileEntityTank)world.getBlockTileEntity(x, y, z);
		
		return tileEntity.containedType;
	}

	@Override
	public GasType takeGasTypeFromSide(World world, int x, int y, int z, int side)
	{
		TileEntityTank tileEntity = (TileEntityTank)world.getBlockTileEntity(x, y, z);
		GasType gasType = tileEntity.containedType;
		tileEntity.decrement();
		
		return gasType;
	}
	
	@Override
	public boolean receiveGas(World world, int x, int y, int z, int side, GasType gasType)
	{
		TileEntityTank tileEntity = (TileEntityTank)world.getBlockTileEntity(x, y, z);
		return tileEntity.increment(gasType);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
	   return new TileEntityTank();
	}
	
	public boolean isOpaqueCube()
	{
		return false;
	}

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return GasesFramework.renderBlockTankID;
    }

	@Override
	public GasType sampleInteraction(World world, int x, int y, int z, GasType in, boolean excludes)
	{
		TileEntityTank tileEntity = (TileEntityTank)world.getBlockTileEntity(x, y, z);
		return tileEntity.containedType;
	}
	
	/**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
		TileEntityTank tileEntity = (TileEntityTank)world.getBlockTileEntity(x, y, z);
		GasType containedType = tileEntity.containedType;
    	ItemStack inUse = entityPlayer.getCurrentEquippedItem();
    	boolean consumed = false;
    	ItemStack newItem = null;
    	
    	if(inUse != null)
    	{
    		if(inUse.itemID == Item.glassBottle.itemID)
        	{
        		if(tileEntity.decrement())
        		{
        			consumed = true;
        			newItem = containedType.getBottledItem();
        		}
        	}
        	else if(inUse.itemID == GasesFramework.gasBottle.itemID)
        	{
        		GasType heldType = ((ItemGasBottle)GasesFramework.gasBottle).getGasType(inUse);
        		if(tileEntity.increment(heldType))
        		{
        			consumed = true;
        			newItem = new ItemStack(Item.glassBottle);
        		}
        	}
    	}
    	
    	boolean addNewItem = newItem != null;
    	
    	if(consumed)
    	{
    		inUse.stackSize--;
			if(inUse.stackSize <= 0 & addNewItem)
			{
				inUse.itemID = newItem.itemID;
				inUse.setItemDamage(newItem.getItemDamage());
				inUse.stackSize = newItem.stackSize;
				addNewItem = false;
			}
    	}
    	
    	if(addNewItem)
    	{
			if(!entityPlayer.inventory.addItemStackToInventory(newItem))
			{
				entityPlayer.dropPlayerItem(newItem);
			}
    	}
    	
    	if(newItem == null | inUse == null)
    	{
    		return false;
    	}
    	
    	return consumed & newItem.itemID != inUse.itemID & newItem.getItemDamage() != inUse.getItemDamage();
    }
}