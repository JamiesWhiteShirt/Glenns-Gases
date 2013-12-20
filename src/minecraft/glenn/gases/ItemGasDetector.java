package glenn.gases;

import glenn.gasesframework.BlockGas;
import glenn.gasesframework.GasType;
import glenn.gasesframework.ISample;
import glenn.gasesframework.util.DVec;
import glenn.gasesframework.util.DVec2;
import glenn.gasesframework.util.IVec;

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

public class ItemGasDetector extends Item
{
	public Icon icon;
	public Icon overlayIcon;
	public Icon emptyOverlayIcon;
	
	public ItemGasDetector(int par1)
	{
		super(par1);
        this.setHasSubtypes(true);
	}
	
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		String s = "Gas";
		if(par1ItemStack.getItemDamage() > 0)
		{
			s = GasType.gasTypes[par1ItemStack.getItemDamage()].name;
		}
		return s + " Detector";
    }
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, entityPlayer, true);
		
		if(movingobjectposition != null && movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
		{
			int i = movingobjectposition.blockX;
			int j = movingobjectposition.blockY;
			int k = movingobjectposition.blockZ;
			Block block = Block.blocksList[world.getBlockId(i, j, k)];
			
			if(ISample.class.isAssignableFrom(block.getClass()))
			{
				ISample sample = (ISample)block;
				GasType newType = sample.sampleInteraction(world, i, j, k, GasType.gasTypes[itemStack.getItemDamage()], false);
				
				if(!(newType == null || !newType.isIndustrial()))
				{
					itemStack.setItemDamage(newType.gasIndex);
				}
				else
				{
					itemStack.setItemDamage(0);
				}
				
				return itemStack;
			}
		}
		
		if(itemStack.getItemDamage() != 0)
		{
			for(int i = 0; i < 100; i++)
			{
				double x = Math.sin(entityPlayer.rotationYaw * Math.PI / -180.0D);
				double y = Math.sin(entityPlayer.rotationPitch * Math.PI / -180.0D);
				double y2 = Math.cos(entityPlayer.rotationPitch * Math.PI / -180.0D);
				double z = Math.cos(entityPlayer.rotationYaw * Math.PI / -180.0D);
				DVec vec = new DVec(x * y2, y, z * y2).scale(Math.sqrt(i));
				
				DVec2 vec2 = DVec2.randomNormalizedVec(world.rand).scale(Math.sqrt(world.rand.nextDouble() * i) * 0.5D);
				DVec vec3 = new DVec(vec2.x, vec2.y, 0.0D);
				vec3.xRotate(entityPlayer.rotationPitch * Math.PI / -180.0D);
				vec3.yRotate(entityPlayer.rotationYaw * Math.PI / -180.0D);
				
				vec.add(entityPlayer.posX, entityPlayer.posY + entityPlayer.getEyeHeight(), entityPlayer.posZ).add(vec3);
				
				IVec ivec = new IVec((int)Math.round(vec.x), (int)Math.round(vec.y), (int)Math.round(vec.z));
				
				Block block = Block.blocksList[world.getBlockId(ivec.x, ivec.y, ivec.z)];
				if(block != null && block instanceof BlockGas)
				{
					BlockGas gasBlock = (BlockGas)block;
					if(GasType.gasTypes[itemStack.getItemDamage()] == gasBlock.type)
					{
		        		world.playSoundAtEntity(entityPlayer, "liquid.lavapop", 2.0F, 0.5F + (100.0F - i) * 0.015F);
		    			break;
					}
				}
			}
		}
		
		return itemStack;
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
		icon = par1IconRegister.registerIcon(this.getIconString());
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