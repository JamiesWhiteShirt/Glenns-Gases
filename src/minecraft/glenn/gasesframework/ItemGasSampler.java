package glenn.gasesframework;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
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
        this.setHasSubtypes(true);
	}
	
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		String s = "";
		if(par1ItemStack.getItemDamage() > 0)
		{
			s = " of " + GasType.gasTypes[par1ItemStack.getItemDamage()].name;
		}
		return (excludes ? "Excluding" : "Including") + " Sampler" + s;
    }
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, entityPlayer, true);

        if(movingobjectposition == null)
        {
            return itemStack;
        }
        else
        {
            if(movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;
                Block block = Block.blocksList[world.getBlockId(i, j, k)];
                
                if(ISample.class.isAssignableFrom(block.getClass()))
                {
                	ISample sample = (ISample)block;
                	GasType newType = sample.sampleInteraction(world, i, j, k, GasType.gasTypes[itemStack.getItemDamage()], excludes);
                	
                	if(!(newType == null || !newType.isIndustrial()))
                	{
                		itemStack.setItemDamage(newType.gasIndex);
                	}
                	else
                	{
                		itemStack.setItemDamage(0);
                	}
                }
            }
            
            return itemStack;
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
	
	@SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < GasType.gasTypes.length; i++)
        {
        	if(GasType.gasTypes[i] != null && GasType.gasTypes[i].isIndustrial())
        	{
        		par3List.add(new ItemStack(par1, 1, i));
        	}
        }
    }
}