package glenn.gases;

import glenn.gases.reaction.Reaction;
import glenn.gases.reaction.ReactionEmpty;
import glenn.gases.reaction.ReactionIgnition;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * To allow a gas to flow in pipes, create a {@link glenn.gases.BlockGasPipe BlockGasPipe} for the gas.
 * @author Glenn
 *
 */
public class BlockGas extends Block
{
	public GasType type;

	//                                   Ring 0       Ring 1        Ring 2
	private static final int[] ringsX = {1, 0, -1, 0, 1, -1, -1, 1, 2, 0, -2, 0};
	private static final int[] ringsZ = {0, 1, 0, -1, 1, 1, -1, -1, 0, 2, 0, -2};
	
	private static final int[] ringX = {0, 0, -1, 1, 0, 0};
	private static final int[] ringY = {-1, 1, 0, 0, 0, 0};
	private static final int[] ringZ = {0, 0, 0, 0, -1, 1};

	//                                   Ring 0       Ring 1        Ring 2
	private static final int[] movesX = {1, 0, -1, 0, 1, -1, -1, 1, 1, 0, -1, 0};
	private static final int[] movesZ = {0, 1, 0, -1, 1, 1, -1, -1, 0, 1, 0, -1};
	
	/**
	 * Constructs a new gas block.
	 * @param id - The block ID to be used by this gas block.
	 * @param color - An RGB representation of the color to be used by this gas.
	 * @param opacity - Higher values will increase the opacity of this gas. This will also affect how well light passes through it.
	 * @param density - A value determining how dense the gas will be relative to air.
	 * <ul><li><b>density > 0</b> Will produce a falling gas. Greater values means the gas will move faster.</li>
	 * <li><b>density < 0</b> Will produce a rising gas. Lower values means the gas will move faster.</li>
	 * <li><b>density = 0</b> Will produce a floating gas which will spread in all directions.</li></ul>
	 * @param combustibility - How the block will react with any blocks registered with {@link Gases#registerIgnitionBlock(int)}.
	 */
    public BlockGas(int id)
    {
		super(id, Gases.gasMaterial);
		
		this.setTickRandomly(true);
		this.disableStats();
    }

    /*public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return this.blockMaterial != Material.lava;
    }*/

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
    	int metadata = par1World.getBlockMetadata(par2, par3, par4);
    	double minY = getMinY(metadata);
    	double maxY = getMaxY(metadata);

    	return AxisAlignedBB.getAABBPool().getAABB((double)par2, (double)par3 + minY, (double)par4, (double)par2 + 1.0D, (double)par3 + maxY, (double)par4 + 1.0D);
    }
    
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
    	if(type.damage > 0.0F)
    	{
    		par5Entity.attackEntityFrom(DamageSource.generic, type.damage);
    	}
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return type.color;
    }

    public int getRenderColor(int metadata)
    {
    	return type.color;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
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
     * Returns whether this block is collideable based on the arguments passed in \n@param par1 block metaData \n@param
     * par2 whether the player right-clicked while holding a boat
     */
    public boolean canCollideCheck(int par1, boolean par2)
    {
        return par2 && par1 == 0;
    }
    
    public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return false;
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
    	int xDirection = par5 == 4 ? 1 : (par5 == 5 ? -1 : 0);
    	int yDirection = par5 == 0 ? 1 : (par5 == 1 ? -1 : 0);
    	int zDirection = par5 == 2 ? 1 : (par5 == 3 ? -1 : 0);
    	
		int metadata = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
		int directionBlockID = par1IBlockAccess.getBlockId(par2 + xDirection, par3 + yDirection, par4 + zDirection);
		int directionBlockMetadata = par1IBlockAccess.getBlockMetadata(par2 + xDirection, par3 + yDirection, par4 + zDirection);
    	
    	if(par5 == 1)
    	{
    		Block block = Block.blocksList[directionBlockID];
    		
    		if(block instanceof BlockGas)
    		{
    			double maxY = ((BlockGas)block).getMaxY(directionBlockMetadata);
    			
    			return maxY - 1.0D != getMinY(metadata);
    		} else
    		{
    			return true;
    		}
    	}
    	else if(par5 == 0)
    	{
    		Block block = Block.blocksList[directionBlockID];
    		
    		if(block instanceof BlockGas)
    		{
    			double minY = ((BlockGas)block).getMinY(directionBlockMetadata);
    			
    			return minY != getMaxY(metadata)- 1.0D ;
    		} else
    		{
    			return true;
    		}
    	}
    	else
    	{
    		return (directionBlockID == blockID & metadata < directionBlockMetadata) | directionBlockID != blockID;
    	}
    }
    
    /**
     * Returns the indent to a block at the given coordinate. Should only be used by the renderer to avoid side clipping.
     * @param par1IBlockAccess
     * @param par2
     * @param par3
     * @param par4
     * @return
     */
    public double sideIndent(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
    	return par1IBlockAccess.isBlockOpaqueCube(par2, par3, par4) ? 0.001D : 0.0D;
    }
    
    /**
     * Gets the height of the bottom side of the gas
     * @param metadata
     * @return
     */
    public double getMinY(int metadata)
    {
    	if(type.density > 0)
    	{
    		return 0.0D;
    	}
    	else if(type.density < 0)
    	{
    		return (double)metadata / 16.0D;
    	} else
    	{
    		double d = 0.5D - (double)(16 - metadata) / 8.0D;
    		return d < 0.0D ? 0.0D : d;
    	}
    }
    
    /**
     * Gets the height of the top side of the gas
     * @param metadata
     * @return
     */
    public double getMaxY(int metadata)
    {
    	if(type.density > 0)
    	{
    		return 1.0D - (double)metadata / 16.0D;
    	}
    	else if(type.density < 0)
    	{
    		return 1.0D;
    	} else
    	{
    		double d = 0.5D + (double)(16 - metadata) / 8.0D;
    		return d > 1.0D ? 1.0D : d;
    	}
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return Gases.renderBlockGasID;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return 0;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return 5;
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }

    /*public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        
    }*/
    
    /**
     * Called whenever a gas {@link glenn.gases.reaction.Reaction reacts} with a block registered with {@link Gases#registerIgnitionBlock(int)}.
     * @param par1World
     * @param par2
     * @param par3
     * @param par4
     * @param par5Random
     * @return
     */
    public boolean onFire(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	if(type.combustibility.explosionPower > 0.0F)
		{
	        if (!par1World.isRemote)
	        {
	        	par1World.setBlock(par2, par3, par4, 0);
	        	par1World.createExplosion(null, (double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, type.combustibility.explosionPower * Gases.gasExplosionFactor, true);
	        }
	        return true;
		}
    	else if(type.combustibility.fireSpreadRate >= 0)
		{
			par1World.setBlock(par2, par3, par4, Block.fire.blockID);
			
			int xDirection = par5Random.nextInt(3) - 1;
    		int yDirection = par5Random.nextInt(3) - 1;
    		int zDirection = par5Random.nextInt(3) - 1;
    		
    		if(par1World.isAirBlock(par2 + xDirection, par3 + yDirection, par4 + zDirection))
    		{
    			par1World.setBlock(par2 + xDirection, par3 + yDirection, par4 + zDirection, Gases.gasSmoke.blockID);
    		}
    		
			return true;
		}

    	return false;
    }
    
    /**
     * Used internally only. Mixes the indices of items of the same value in a sorted array
     * @param rand
     * @param valueList
     * @param indexList
     */
    private void mixEqualSortedValues(Random rand, int[] valueList, int[] indexList)
    {
    	for(int i = 0; i < valueList.length;)
    	{
    		int value = valueList[i];
    		for(int j = 1; j + i <= valueList.length; j++)
    		{
    			if(j + i >= valueList.length || valueList[i + j] != value)
    			{
    				int amountOfSwaps = (j - 1) * (j - 1) * 2;
    				for(int k = 0; k < amountOfSwaps; k++)
    				{
    					int pos1 = rand.nextInt(j);
    					int pos2 = rand.nextInt(j);
    					if(pos1 != pos2)
    					{
    						int temp = indexList[i + pos1];
    						indexList[i + pos1] = indexList[i + pos2];
    						indexList[i + pos2] = temp;
    					}
    				}
    				
    				i += j;
    				break;
    			}
    		}
    	}
    }
    
    /**
     * Used internally only. Fills an array with randomly placed unique indices with boundaries
     * @param rand
     * @param indices
     * @param length
     * @param start
     * @param arrayTranslate
     */
    private void fillArrayWithIndices(Random rand, int[] indices, int length, int start, int arrayTranslate)
    {
    	for(int i = 0; i < length; i++)
    	{
    		while(true)
    		{
    			int index = arrayTranslate + rand.nextInt(length);
    			if(indices[index] == 0)
    			{
    				indices[index] = start + i;
    				break;
    			}
    		}
    	}
    }
    
    /**
     * Fills an array of {@link glenn.gases.reaction.Reaction Reaction} with reactions for surrounding blocks. Used internally only.
     * @param world
     * @param x
     * @param y
     * @param z
     * @param reactions
     * @param reactionIndices
     * @param random
     */
    private void reactionsWithSurroundingBlocks(World world, int x, int y, int z, Reaction[] reactions, int[] reactionIndices, Random random)
    {
    	int[] indices = new int[6];
    	fillArrayWithIndices(random, indices, 6, 0, 0);
    	
    	for(int index = 0; index < reactions.length; index++)
    	{
    		int i = indices[index];
    		int xDirection = i < 2 ? i * 2 - 1: 0;
			int yDirection = i < 4 & i >= 2 ? i * 2 - 5: 0;
			int zDirection = i >= 4 ? i * 2 - 9: 0;
			
			int directionBlockID = world.getBlockId(x + xDirection, y + yDirection, z + zDirection);
			Reaction reaction = Gases.getReactionForBlocks(blockID, directionBlockID);
			for(int j = 0; j < reactions.length; j++)
			{
				Reaction reaction2 = reactions[j];
				if(reaction2 == null)
				{
					reactions[j] = reaction;
					reactionIndices[j] = i;
					break;
				}
				else if(reaction2.priority < reaction.priority)
				{
					for(int k = index - 1; k >= j; k--)
					{
						reactions[k + 1] = reactions[k];
						reactionIndices[k + 1] = reactionIndices[k];
					}
					
					reactions[j] = reaction;
					reactionIndices[j] = i;
					break;
				}
			}
    	}
    }
    
    /**
     * Gets the delay in ticks needed before the block should update again. Used internally only.
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    private int getDelayForUpdate(World world, int x, int y, int z)
    {
    	Reaction reaction = new ReactionEmpty();
    	int delay = -1;
		
		for(int i = 0; i < 6; i++)
		{
			int xDirection = i < 2 ? i * 2 - 1: 0;
			int yDirection = i < 4 & i >= 2 ? i * 2 - 5: 0;
			int zDirection = i >= 4 ? i * 2 - 9: 0;
			
			int directionBlockID = world.getBlockId(x + xDirection, y + yDirection, z + zDirection);
			Reaction reaction2 = Gases.getReactionForBlocks(blockID, directionBlockID);
			
			if(reaction.isErroneous() || reaction2.priority < reaction.priority)
			{
				reaction = reaction2;
				delay = reaction.getDelay(world, x, y, z, x + xDirection, y + yDirection, z + zDirection);
			}
		}
		
		if(delay == -1)
		{
			if(type.density > 0)
	    	{
	        	delay = (int)(128.0F / (float)type.density);
	    	}
	    	else if(type.density < 0)
	    	{
	    		delay = (int)(-128.0F / (float)type.density);
	    	}
	    	else
	    	{
	    		delay = 8;
	    	}
		}
    	
    	return delay;
    }

    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	//Fetch a list of reactions. If any of these reactions succeed, the block will not update normally.
    	{
    		Reaction[] reactions = new Reaction[6];
    		int[] reactionIndices = new int[6];
    		reactionsWithSurroundingBlocks(par1World, par2, par3, par4, reactions, reactionIndices, par5Random);
    		
    		for(int i = 0; i < reactions.length; i++)
        	{
        		int index = reactionIndices[i];
        		int xDirection = index < 2 ? index * 2 - 1: 0;
    			int yDirection = index < 4 & index >= 2 ? index * 2 - 5: 0;
    			int zDirection = index >= 4 ? index * 2 - 9: 0;
    			if(reactions[i].reactIfPossible(par1World, par5Random, par2, par3, par4, par2 + xDirection, par3 + yDirection, par4 + zDirection))
    			{
    				return;
    			}
        	}
    	}
    	
    	//For technical reasons, metadata is a reverse representation of how much gas there is inside a block
    	int metadata = 16 - par1World.getBlockMetadata(par2, par3, par4) - getGasDecay(par1World, par2, par3, par4, par5Random);
		boolean requiresTick = type.evaporationRate > 0;
    	
		//If density is 0, the block will behave very differently.
    	if(type.density == 0)
    	{
    		//The gas will flow out from its position, but will priorify blocks around with the least amount of gas, and especially air. It will not flow into other blocks.
        	int[] metadataList = new int[6];
    		int[] priorityList = new int[6];
    		int surroundingAirBlocks = 0;

    		for(int i = 0; i < 6; i++)
    		{
    			int xDirection = ringX[i];
    			int yDirection = ringY[i];
    			int zDirection = ringZ[i];
    			
    	    	int direction2BlockID = par1World.getBlockId(par2 + xDirection, par3 + yDirection, par4 + zDirection);
    			int direction2BlockMetadata = 16 - par1World.getBlockMetadata(par2 + xDirection, par3 + yDirection, par4 + zDirection);
    			
    			if(direction2BlockID == 0)
    			{
    				direction2BlockMetadata = -1;
    				surroundingAirBlocks++;
    			} else if(direction2BlockID != this.blockID)
    			{
    				direction2BlockMetadata = 17;
    			}

    			for(int j = 0; j < 6; j++)
    			{
    				if(metadataList[j] <= direction2BlockMetadata & j != i)
    				{
    					continue;
    				}

    				for(int k = 4; k >= j; k--)
    				{
    					metadataList[k + 1] = metadataList[k];
    					priorityList[k + 1] = priorityList[k];
    				}

    				metadataList[j] = direction2BlockMetadata;
    				priorityList[j] = i;

    				break;
    			}
    		}
    		
    		mixEqualSortedValues(par5Random, metadataList, priorityList);

    		for(int i = 0; i < 6 & metadata > 1; i++)
    		{
    			int j = priorityList[i];
    			int direction2BlockMetadata = metadataList[i];
    			int xDirection = ringX[j];
    			int yDirection = ringY[j];
    			int zDirection = ringZ[j];

    			if(direction2BlockMetadata != 17)
    			{
    				if(direction2BlockMetadata == -1)
    				{
    					par1World.setBlock(par2 + xDirection, par3 + yDirection, par4 + zDirection, this.blockID, 15, 3);
    					requiresTick = false;
        				metadata--;
    				} else if(direction2BlockMetadata < 16 & direction2BlockMetadata + 1 < metadata)
    				{
    					par1World.setBlockMetadataWithNotify(par2 + xDirection, par3 + yDirection, par4 + zDirection, 15 - direction2BlockMetadata, 3);
    					requiresTick = false;
        				metadata--;
    				}
    			}
    		}
    		
    		//Remember to set the new metadata for the gas block.
    		if(metadata > 0)
			{
				par1World.setBlockMetadataWithNotify(par2, par3, par4, 16 - metadata, 3);
			} else
			{
	    		par1World.setBlock(par2, par3, par4, 0);
			}
    		
    		if(requiresTick)
        	{
        		par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 10);
        	}
    		
    		return;
    	}

    	int yDirection = type.density > 0 ? -1 : 1;
    	int directionBlockID = par1World.getBlockId(par2, par3 + yDirection, par4);
		int directionBlockMetadata = 16 - par1World.getBlockMetadata(par2, par3 + yDirection, par4);
    	int reverseDirectionBlockID = par1World.getBlockId(par2, par3 - yDirection, par4);
		int reverseDirectionBlockMetadata = 16 - par1World.getBlockMetadata(par2, par3 - yDirection, par4);
		
    	if(directionBlockID == 0)
    	{
    		//If the block in the direction is air, it will only move in this direction.
    		if(metadata > 0)
    		{
    			par1World.setBlock(par2, par3 + yDirection, par4, this.blockID, 16 - metadata, 3);
    		}
    		par1World.setBlock(par2, par3, par4, 0);
    	}
    	else
    	{
    		if(directionBlockID != this.blockID && Block.blocksList[directionBlockID] instanceof BlockGas)
        	{
        		//If the block in the direction is another gas, it will swap the position of the gases according to their densities.
        		int directionBlockDensity = ((BlockGas)Block.blocksList[directionBlockID]).type.density;

        		if((type.density > 0 & type.density > directionBlockDensity) | (type.density < 0 & type.density < directionBlockDensity))
        		{
        			par1World.setBlock(par2, par3, par4, directionBlockID, 16 - directionBlockMetadata, 3);
        			par1World.setBlock(par2, par3 + yDirection, par4, this.blockID, 16 - metadata, 3);
        			return;
        		}
        	}
    		
    		//If the block in the direction is the same gas, it will attempt to fill the other gas block.
    		if(directionBlockID == this.blockID)
    		{
    			if(directionBlockMetadata < 16)
    			{
    				if(directionBlockMetadata + metadata < 16)
    				{
    					par1World.setBlock(par2, par3, par4, 0);
    					par1World.setBlockMetadataWithNotify(par2, par3 + yDirection, par4, 16 - directionBlockMetadata - metadata, 3);

    					return;
    				} else
    				{
    					par1World.setBlockMetadataWithNotify(par2, par3, par4, 32 - directionBlockMetadata - metadata, 3);
    					par1World.setBlockMetadataWithNotify(par2, par3 + yDirection, par4, 0, 3);
    				}

    				metadata -= 16 - directionBlockMetadata;
    			}
    		}
    		
    		//If the block in the opposite direction is this gas, it will take the contents of the other block to fill itself
    		if(reverseDirectionBlockID == this.blockID)
    		{
    			metadata += reverseDirectionBlockMetadata;
    		}
    		
    		//The gas will flow out from its position, but will priorify blocks around with the least amount of gas, and especially air. It will not flow into other blocks.
    		int[] metadataList = new int[4];
    		int[] priorityList = new int[4];
    		int surroundingAirBlocks = 0;
    		
    		for(int i = 0; i < 4; i++)
    		{
    			int xDirection = ringsX[i];
    			int zDirection = ringsZ[i];

    	    	int direction2BlockID = par1World.getBlockId(par2 + xDirection, par3, par4 + zDirection);
    			int direction2BlockMetadata = 16 - par1World.getBlockMetadata(par2 + xDirection, par3, par4 + zDirection);

    			if(direction2BlockID == 0)
    			{
    				direction2BlockMetadata = -1;
    				surroundingAirBlocks++;
    			} else if(direction2BlockID != this.blockID)
    			{
    				direction2BlockMetadata = 17;
    			}

    			for(int j = 0; j < 4; j++)
    			{
    				if(metadataList[j] <= direction2BlockMetadata & j != i)
    				{
    					continue;
    				}

    				for(int k = 2; k >= j; k--)
    				{
    					metadataList[k + 1] = metadataList[k];
    					priorityList[k + 1] = priorityList[k];
    				}

    				metadataList[j] = direction2BlockMetadata;
    				priorityList[j] = i;

    				break;
    			}
    		}
    		
    		mixEqualSortedValues(par5Random, metadataList, priorityList);
    		
    		//If this block is too small to spread properly, it will attempt to flow along the surface to a gap to be able to move further.
    		//Closer gaps are prioritized.
    		if(metadata < surroundingAirBlocks + 2)
    		{
    			int[] indices = new int[ringsX.length];
    			this.fillArrayWithIndices(par5Random, indices, 4, 0, 0);
    			this.fillArrayWithIndices(par5Random, indices, 4, 4, 4);
    			this.fillArrayWithIndices(par5Random, indices, 4, 8, 8);
    			for(int index = 0; index < indices.length; index++)
    			{
    				int i = indices[index];
        			int x = par2 + ringsX[i];
        			int z = par4 + ringsZ[i];

        			int direction2BlockID = par1World.getBlockId(x, par3, z);

        			if(direction2BlockID == 0)
        			{
        				int direction3BlockID = par1World.getBlockId(x, par3 + yDirection, z);

        				if(direction3BlockID == 0 || (direction3BlockID == this.blockID && metadata - par1World.getBlockMetadata(x, par3 + yDirection, z) <= 0))
        				{
        					if(i >= 8)
        					{
        						if(par1World.getBlockId(par2 + movesX[i], par3, par4 + movesZ[i]) != 0)
        						{
        							continue;
        						}
        					} else if(i >= 4)
        					{
        						if(par1World.getBlockId(par2 + movesX[i], par3, par4) != 0 && par1World.getBlockId(par2, par3, par4 + movesZ[i]) != 0)
        						{
        							continue;
        						}
        					}

    						par1World.setBlock(par2, par3, par4, 0);
    						if(metadata > 0)
    						{
    							par1World.setBlock(par2 + movesX[i], par3, par4 + movesZ[i], this.blockID, 16 - metadata, 3);
    						}
    						return;
        				}
        			}
    			}
    		}
    		
    		//If the previous did not happen, the block will then disperse.
    		for(int i = 0; i < 4 & metadata > 1; i++)
    		{
    			int j = priorityList[i];
    			int direction2BlockMetadata = metadataList[i];
    			int xDirection = ringsX[j];
    			int zDirection = ringsZ[j];

    			if(direction2BlockMetadata != 17)
    			{
    				if(direction2BlockMetadata == -1)
    				{
    					par1World.setBlock(par2 + xDirection, par3, par4 + zDirection, this.blockID, 15, 3);
    					requiresTick = false;
        				metadata--;
    				} else if(direction2BlockMetadata < 16 & direction2BlockMetadata + 1 < metadata)
    				{
    					par1World.setBlockMetadataWithNotify(par2 + xDirection, par3, par4 + zDirection, 15 - direction2BlockMetadata, 3);
    					requiresTick = false;
        				metadata--;
    				}
    			}
    		}
    		
    		//Finalizing. Setting the metadata for both this block and the block in the opposite direction to make sure the gases are finite.
    		if(metadata > 16)
    		{
    			par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 3);
    			if(reverseDirectionBlockID == this.blockID)
    			{
    				par1World.setBlockMetadataWithNotify(par2, par3 - yDirection, par4, 32 - metadata, 3);
    			}
    		} else
    		{
    			if(reverseDirectionBlockID == this.blockID)
    			{
    				par1World.setBlock(par2, par3 - yDirection, par4, 0);
    			}

    			if(metadata > 0)
    			{
    				par1World.setBlockMetadataWithNotify(par2, par3, par4, 16 - metadata, 3);
    			} else
    			{
    	    		par1World.setBlock(par2, par3, par4, 0);
    			}
    		}
    	}
    	
    	//If this gas requires a new tick, it will schedule one.
    	if(requiresTick)
    	{
    		par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, getDelayForUpdate(par1World, par2, par3, par4));
    	}
    }
    
    /**
     * Gets the decay of the gas in a tick. Only used internally.
     * @param par1World
     * @param par2
     * @param par3
     * @param par4
     * @param par5Random
     * @return
     */
    protected int getGasDecay(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	return type.evaporationRate > 0 && par5Random.nextInt(type.evaporationRate) == 0 ? 1 : 0;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
    	par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, getDelayForUpdate(par1World, par2, par3, par4));
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
    	onBlockAdded(par1World, par2, par3, par4);
    }

    public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion)
    {
    	onFire(par1World, par2, par3, par4, par1World.rand);
    }

    public boolean func_82506_l()
    {
        return false;
    }
    
    /**
     * Returns the item received when the gas is captured in a bottle.
     * @return itemStack
     */
    public ItemStack getBottledItem()
    {
    	return new ItemStack(Gases.gasBottle);
    }
    
    /**
     * Returns whether this block will combust normally.
     * @return
     */
    public boolean canCombustNormally()
    {
    	return this.type.combustibility != Combustibility.NONE;
    }
}
