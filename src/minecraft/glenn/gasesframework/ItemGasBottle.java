package glenn.gasesframework;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemGasBottle extends Item
{
	public Icon icon;
	public Icon overlayIcon;
	
	public ItemGasBottle(int itemID)
	{
		super(itemID);
        this.setHasSubtypes(true);
	}
	
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		String s = "";
		if(par1ItemStack.getItemDamage() > 0)
		{
			s = " of " + GasType.gasTypes[par1ItemStack.getItemDamage()].name;
		}
		return "Bottle" + s;
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
		icon = par1IconRegister.registerIcon(this.getIconString() + "_empty");
		overlayIcon = par1IconRegister.registerIcon(this.getIconString() + "_overlay");
    }
	
	@SideOnly(Side.CLIENT)
    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public Icon getIconFromDamageForRenderPass(int par1, int par2)
    {
        return par2 == 0 ? overlayIcon : icon;
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
        for (int i = 1; i < GasType.gasTypes.length; i++)
        {
        	if(GasType.gasTypes[i] != null && GasType.gasTypes[i].isIndustrial())
        	{
        		par3List.add(new ItemStack(par1, 1, i));
        	}
        }
    }
	
	public GasType getGasType(ItemStack itemStack)
	{
		return GasType.gasTypes[itemStack.getItemDamage()];
	}
}