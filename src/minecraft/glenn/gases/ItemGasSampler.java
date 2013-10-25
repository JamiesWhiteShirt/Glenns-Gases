package glenn.gases;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemGasSampler extends Item
{
	public boolean excludes;
	public Icon icon;
	public Icon overlayIcon;
	public Icon emptyOverlayIcon;
	
	public ItemGasSampler(int par1, boolean excludes)
	{
		super(par1);
		this.excludes = excludes;
	}
	
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		String s = "";
		if(par1ItemStack.getItemDamage() > 0)
		{
			s = " of " + GasType.gasTypes[par1ItemStack.getItemDamage()].name;
		}
		return StatCollector.translateToLocal("item.sampler" + (excludes ? "Exluder" : "Includer") + ".name").trim() + s;
    }
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

        if(movingobjectposition == null)
        {
            return par1ItemStack;
        }
        else
        {
            if(movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;
                Block block = Block.blocksList[par2World.getBlockId(i, j, k)];
                
                if(block instanceof BlockGas)
                {
                	par1ItemStack.setItemDamage(((BlockGas)block).type.gasIndex);
                }
                else if(block instanceof BlockPump & par1ItemStack.getItemDamage() > 0)
                {
                	TileEntityPump tileEntity = (TileEntityPump)par2World.getBlockTileEntity(i, j, k);
                	
                	tileEntity.excludes = this.excludes;
                	tileEntity.filterType = GasType.gasTypes[par1ItemStack.getItemDamage()];
                }
            }
            
            return par1ItemStack;
        }
    }
	
	@SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
	
	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return par2 > 0 ? 16777215 : this.getColorFromDamage(par1ItemStack.getItemDamage());
    }
	
	@SideOnly(Side.CLIENT)
    public int getColorFromDamage(int par1)
    {
		if(par1 > 0)
		{
			return GasType.gasTypes[par1].color;
		}
		else
		{
			return 16777215;
		}
    }
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
		icon = par1IconRegister.registerIcon(this.getIconString() + (excludes ? "_black" : "_white"));
		overlayIcon = par1IconRegister.registerIcon(this.getIconString() + "_overlay");
		emptyOverlayIcon = par1IconRegister.registerIcon(this.getIconString() + "_overlay_empty");
    }
	
	@SideOnly(Side.CLIENT)
    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public Icon getIconFromDamageForRenderPass(int par1, int par2)
    {
        return par2 == 0 ? (par1 > 0 ? overlayIcon : emptyOverlayIcon) : icon;
    }
	
	@SideOnly(Side.CLIENT)
    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int par1)
    {
        return this.icon;
    }
}