package glenn.gases;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockLantern extends Block
{
	protected class LanternRecipe
	{
		public int itemID;
		public int itemDamage;
		protected BlockLantern blockLantern;
		
		public LanternRecipe(ItemStack itemStack, BlockLantern blockLantern)
		{
			this.itemID = itemStack.itemID;
			this.itemDamage = itemStack.getItemDamage();
			this.blockLantern = blockLantern;
		}
		
		public LanternRecipe()
		{
			
		}
		
		public boolean equals(ItemStack itemStack)
		{
			return itemStack != null && this.itemID == itemStack.itemID & itemStack.getItemDamage() == this.itemDamage;
		}
		
		public BlockLantern getLantern(ItemStack itemStack)
		{
			return blockLantern;
		}
	}
	
	private class LanternRecipeGas extends LanternRecipe
	{
		public boolean equals(ItemStack itemStack)
		{
			Item item = itemStack.getItem();
			
			return item != null && item instanceof ItemGasBottle;
		}
		
		public BlockLantern getLantern(ItemStack itemStack)
		{
			Combustibility combustibility = GasType.gasTypes[itemStack.getItemDamage()].combustibility;
			return GasType.gasTypes[itemStack.getItemDamage()].combustibility.lanternBlock;
		}
	}
	
	public int tickrate;
	public Combustibility combustibility;
	
	protected static final ArrayList<LanternRecipe> lanternRecipes = new ArrayList<LanternRecipe>();
	
	{
		lanternRecipes.add(new LanternRecipeGas());
	}
	
	/**
	 * Creates a new lantern block.
	 * @param blockID - The ID of the block.
	 * @param containedItemIn - The item/block which can be used with a lantern to create this lantern.
	 * @param containedItemOut - The item/block which will be ejected by this lantern. For instance, gas lanterns accept bottles of gas but eject empty bottles.
	 * @param tickrate - The rate at which the lantern will burn out. Set to 0 for non-expiring lanterns.
	 * @param expirationBlockID - The ID of the block this lantern will become when expired or destroyed. If -1, will become the block ID.
	 */
	public BlockLantern(int blockID, Combustibility combustibility)
	{
		super(blockID, Material.iron);
		this.tickrate = combustibility != null ? combustibility.burnRate : 0;
		this.combustibility = combustibility;

        this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 10.0F / 16.0F, 0.75F);
        
        if(tickrate > 0)
        {
        	setTickRandomly(true);
        }
        
        if(combustibility != null)
        {
        	combustibility.lanternBlock = this;
        }
	}
	
	protected BlockLantern(int blockID, Combustibility combustibility, int tickrate)
	{
		super(blockID, Material.iron);
		this.tickrate = tickrate;

        this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 10.0F / 16.0F, 0.75F);
        
        if(tickrate > 0)
        {
        	setTickRandomly(true);
        }
        
        if(combustibility != null)
        {
        	combustibility.lanternBlock = this;
        }
	}
	
	public int getExpirationBlockID()
	{
		return Gases.lanternGasEmpty.blockID;
	}
	
	public ItemStack getContainedItemOut()
	{
		return new ItemStack(Item.glassBottle);
	}
	
	public ItemStack getContainedItemIn()
	{
		return null;
	}
	
	public boolean isValidInItem(ItemStack itemStack)
	{
		for(LanternRecipe lanternRecipe : lanternRecipes)
		{
			if(lanternRecipe.equals(itemStack))
			{
				return true;
			}
		}
		
		return false;
	}

	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
		if(tickrate > 0)
		{
			int metadata = par1World.getBlockMetadata(par2, par3, par4);
			if(metadata <= 0)
			{
				par1World.setBlock(par2, par3, par4, getExpirationBlockID());
			} else
			{
				par1World.setBlockMetadataWithNotify(par2, par3, par4, metadata - 1, 3);
			}
		}
    }

    public int tickRate(World par1World)
    {
        return tickrate;
    }

	/**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
    	ItemStack inUse = par5EntityPlayer.getCurrentEquippedItem();
    	ItemStack containedItem = getContainedItemIn();
    	ItemStack ejectedItem = getContainedItemOut();
    	boolean consumeItem = false;
    	boolean blockAlreadyPlaced = false;
    	
    	boolean isNotEqual = !(!(ejectedItem == null | inUse == null) && (ejectedItem.itemID == inUse.itemID & ejectedItem.getItemDamage() == inUse.getItemDamage()));

    	if(inUse != null)
    	{
    		/*if(inUse.itemID == ejectedItem.itemID & inUse.getItemDamage() == ejectedItem.getItemDamage())
	    	{
	    		return false;
	    	}
    		else if(containedItem != null && inUse.itemID == containedItem.itemID & inUse.getItemDamage() == containedItem.getItemDamage() & !(containedItem.itemID == ejectedItem.itemID & containedItem.getItemDamage() == ejectedItem.getItemDamage()))
	    	{
				consumeItem = true;
				onBlockAdded(par1World, par2, par3, par4);
				blockAlreadyPlaced = true;
	    	}
    		else
	    	*/{
	    		BlockLantern lanternBlock = null;
	    		
	    		for(LanternRecipe lanternRecipe : lanternRecipes)
	    		{
	    			if(lanternRecipe.equals(inUse))
	    			{
	    				lanternBlock = lanternRecipe.getLantern(inUse);
	    				break;
	    			}
	    		}
    			
	    		/*if(lanternBlock == this)
	    		{
	    			ejectedItem = null;
    				blockAlreadyPlaced = true;
	    		}
	    		else */if(lanternBlock != null)
    			{
    				consumeItem = true;
    				par1World.setBlock(par2, par3, par4, lanternBlock.blockID);
    				blockAlreadyPlaced = true;
    			}
	    	}

    		if((consumeItem & isNotEqual) && --inUse.stackSize <= 0)
			{
				par5EntityPlayer.destroyCurrentEquippedItem();
			}
    	}

    	if(ejectedItem != null & isNotEqual)
    	{
    		if(!par5EntityPlayer.inventory.addItemStackToInventory(ejectedItem))
            {
                this.dropBlockAsItem_do(par1World, par2, par3, par4, ejectedItem);
            }
    	}

		if(!blockAlreadyPlaced & getExpirationBlockID() != blockID)
		{
			par1World.setBlock(par2, par3, par4, Gases.lanternEmpty.blockID);
			return false;
		}

    	return true;
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
    	if(tickrate != 0)
    	{
    		par1World.setBlockMetadataWithNotify(par2, par3, par4, 15, 3);
    	}
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return Gases.renderBlockLanternID;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
    	if(!this.canBlockStay(par1World, par2, par3, par4))
        {
        	this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        return par1World.isBlockOpaqueCube(par2, par3 - 1, par4) || par1World.isBlockOpaqueCube(par2, par3 + 1, par4) || par1World.isBlockOpaqueCube(par2 - 1, par3, par4) || par1World.isBlockOpaqueCube(par2, par3, par4 - 1) || par1World.isBlockOpaqueCube(par2 + 1, par3, par4) || par1World.isBlockOpaqueCube(par2, par3, par4 + 1);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return canBlockStay(par1World, par2, par3, par4);
    }
    
    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return getExpirationBlockID();
    }
}