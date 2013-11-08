package glenn.gases;

import glenn.gases.util.IVec;
import glenn.gases.util.PipeBranch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGasPipe extends Block implements IGasReceptor
{
	private static final int[] xDirection = new int[]{
		0, 0, 1, -1, 0, 0
	};
	private static final int[] yDirection = new int[]{
		-1, 1, 0, 0, 0, 0
	};
	private static final int[] zDirection = new int[]{
		0, 0, 0, 0, 1, -1
	};
	
	/*private static final boolean[][][] branchedBlocks = new boolean[31][31][31];
	private static final ArrayList<PipeBranch> branches = new ArrayList<PipeBranch>();
	private static final ArrayList<PipeBranch> looseEnds = new ArrayList<PipeBranch>();
	private static final ArrayList<PipeBranch> ends = new ArrayList<PipeBranch>();*/
	
	/**
	 * The gas block contained by this gas pipe.
	 */
	public GasType type;
	
	/**
	 * Constructs a new gas pipe block. Because of technical reasons, each type of gas needs its own gas pipe block for pipe usage.
	 * @param par1 - Block ID
	 * @param containedGas - The gas this pipe will carry
	 */
	public BlockGasPipe(int blockID)
	{
		super(blockID, Material.iron);
		
		this.setHardness(1.0F);
		this.setTextureName("gases:pipe");
	}
	
	public boolean[] getRenderConnectionArray(IBlockAccess blockAccess, int x, int y, int z)
	{
		final boolean[] sidePipe = new boolean[6];
		final boolean[] renderConnections = new boolean[6];
        for(int i = 0; i < 6; i++)
		{
			int x1 = x + xDirection[i];
			int y1 = y + yDirection[i];
			int z1 = z + zDirection[i];
			
			int directionBlockID = blockAccess.getBlockId(x1, y1, z1);
			if(directionBlockID != 0)
			{
				Block directionBlock = Block.blocksList[directionBlockID];
				sidePipe[i] = directionBlock instanceof BlockGasPipe || IGasReceptor.class.isAssignableFrom(directionBlock.getClass());
			}
		}
        
        boolean collectionAll = sidePipe[0] || sidePipe[1] || sidePipe[2] || sidePipe[3] || sidePipe[4] || sidePipe[5];
		boolean collectionY = sidePipe[2] || sidePipe[3] || sidePipe[4] || sidePipe[5];
		boolean collectionX = sidePipe[0] || sidePipe[1] || sidePipe[4] || sidePipe[5];
		boolean collectionZ = sidePipe[0] || sidePipe[1] || sidePipe[2] || sidePipe[3];
        
    	renderConnections[0] = (sidePipe[3] | !collectionX) & collectionAll;
    	renderConnections[1] = (sidePipe[2] | !collectionX) & collectionAll;
    	renderConnections[2] = (sidePipe[0] | !collectionY) & collectionAll;
    	renderConnections[3] = (sidePipe[1] | !collectionY) & collectionAll;
    	renderConnections[4] = (sidePipe[5] | !collectionZ) & collectionAll;
    	renderConnections[5] = (sidePipe[4] | !collectionZ) & collectionAll;
    	
    	return renderConnections;
	}
	
	/**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
    {
        final boolean[] renderConnections = this.getRenderConnectionArray(world, x, y, z);
    	
        float f1 = 6.0F / 16.0F;
    	float f2 = 10.0F / 16.0F;
        
    	float minX = renderConnections[0] ? 0.0F : f1;
    	float maxX = renderConnections[1] ? 1.0F : f2;
    	float minY = renderConnections[2] ? 0.0F : f1;
    	float maxY = renderConnections[3] ? 1.0F : f2;
    	float minZ = renderConnections[4] ? 0.0F : f1;
    	float maxZ = renderConnections[5] ? 1.0F : f2;
    	
    	this.setBlockBounds(f1, f1, minZ, f2, f2, maxZ);
        super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);

    	this.setBlockBounds(f1, minY, f1, f2, maxY, f2);
        super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);

    	this.setBlockBounds(minX, f1, f1, maxX, f2, f2);
        super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
        
        this.setBlockBoundsBasedOnState(world, x, y, z);
    }
	
	/**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
    	final boolean[] renderConnections = this.getRenderConnectionArray(blockAccess, x, y, z);
    	
        float f1 = 6.0F / 16.0F;
    	float f2 = 10.0F / 16.0F;
        
    	float minX = renderConnections[0] ? 0.0F : f1;
    	float maxX = renderConnections[1] ? 1.0F : f2;
    	float minY = renderConnections[2] ? 0.0F : f1;
    	float maxY = renderConnections[3] ? 1.0F : f2;
    	float minZ = renderConnections[4] ? 0.0F : f1;
    	float maxZ = renderConnections[5] ? 1.0F : f2;
    	
    	this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
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
	
	private void shuffleArray(Random random, PipeBranch[] array, int limit)
	{
		for(int i = limit; i > 0; i--)
		{
			int otherIndex = random.nextInt(i + 1);
			PipeBranch pipeBranch = array[otherIndex];
			array[otherIndex] = array[i];
			array[i] = pipeBranch;
		}
	}
	
	public boolean receiveGas(World world, int x, int y, int z, int side, GasType gasType)
	{
		final boolean[][][] branchedBlocks = new boolean[31][31][31];
		branchedBlocks[15][15][15] = true;
		final ArrayList<PipeBranch> branches = new ArrayList<PipeBranch>();
		final ArrayList<PipeBranch> looseEnds = new ArrayList<PipeBranch>();
		final ArrayList<PipeBranch> ends = new ArrayList<PipeBranch>();
		
		//branches.clear();
		branches.add(new PipeBranch(0, 0, 0, side));
		//ends.clear();
		//looseEnds.clear();
		
		boolean stop = false;
		while(branches.size() > 0 & !stop)
		{
			int branchAmount = branches.size();
			for(int i = 0; i < branchAmount & !stop; i++)
			{
				PipeBranch branch = branches.get(i);
				PipeBranch[] newBranches = new PipeBranch[6];
				
				int subBranches = 0;
				int connectedSurroundingBlocks = 0;
				int x1 = x + branch.x;
				int y1 = y + branch.y;
				int z1 = z + branch.z;
				
				for(int j = 0; j < 6; j++)
				{
					if(j == branch.getReverseDirection())
					{
						continue;
					}
					
					int x2 = x1 + xDirection[j];
					int y2 = y1 + yDirection[j];
					int z2 = z1 + zDirection[j];
					boolean notBranched = !branchedBlocks[15 + branch.x + xDirection[j]][15 + branch.y + yDirection[j]][15 + branch.z + zDirection[j]];
					
					int directionBlockID = world.getBlockId(x2, y2, z2);
					Block directionBlock = Block.blocksList[directionBlockID];
					int directionBlockMetadata = world.getBlockMetadata(x2, y2, z2);
					
					if(directionBlock != null)
					{
						if(directionBlock instanceof BlockGasPipe)
						{
							connectedSurroundingBlocks++;
							if(branch.length < 15 & notBranched)
							{
								branchedBlocks[15 + branch.x + xDirection[j]][15 + branch.y + yDirection[j]][15 + branch.z + zDirection[j]] = true;
								newBranches[subBranches++] = branch.branch(j);
							}
						}
						else if(IGasReceptor.class.isAssignableFrom(directionBlock.getClass()))
						{
							connectedSurroundingBlocks++;
							ends.add(branch.branch(j));
						}
					}
				}
				
				if(connectedSurroundingBlocks <= 0)
				{
					PipeBranch branch2 = branch.branch(branch.getDirection());
					int x2 = x + branch2.x;
					int y2 = y + branch2.y;
					int z2 = z + branch2.z;
					
					if(world.isAirBlock(x2, y2, z2))
					{
						looseEnds.add(branch2);
					}
				}
				
				if(subBranches > 0)
				{
					//shuffleArray(random, newBranches, subBranches - 1);
					
					for(int j = 0; j < subBranches; j++)
					{
						PipeBranch newBranch = newBranches[j];
						
						if(j < 1)
						{
							branches.set(i, newBranch);
						}
						else
						{
							branches.add(newBranch);
						}
					}
				}
				else
				{
					branches.remove(i--);
					branchAmount--;
				}
			}
		}
		
		PipeBranch pushedEnd = null;
		
		if(looseEnds.size() > 0)
		{
			pushedEnd = looseEnds.get(world.rand.nextInt(looseEnds.size()));
			PipeBranch originPipe = pushedEnd.unBranch();
			
			BlockGasPipe pipeBlock = (BlockGasPipe)Block.blocksList[world.getBlockId(x + originPipe.x, y + originPipe.y, z + originPipe.z)];
			if(pipeBlock.type.gasBlock != null)
			{
				world.setBlock(x + pushedEnd.x, y + pushedEnd.y, z + pushedEnd.z, pipeBlock.type.gasBlock.blockID);
			}
			pushedEnd = originPipe;
		}
		else if(ends.size() > 0)
		{
			Collections.shuffle(ends);
			
			for(int i = 0; i < ends.size() & pushedEnd == null; i++)
			{
				PipeBranch end = ends.get(i);
				int x1 = x + end.x;
				int y1 = y + end.y;
				int z1 = z + end.z;
				
				PipeBranch outPipe = end.unBranch();
				int x2 = x + outPipe.x;
				int y2 = y + outPipe.y;
				int z2 = z + outPipe.z;
				
				IGasReceptor endBlock = (IGasReceptor)Block.blocksList[world.getBlockId(x1, y1, z1)];
				GasType outGasType = null;
				
				Block sourceBlock = Block.blocksList[world.getBlockId(x2, y2, z2)];
				if(sourceBlock instanceof BlockGasPipe)
				{
					outGasType = ((BlockGasPipe)sourceBlock).type;
				}
				else if(IGasSource.class.isAssignableFrom(sourceBlock.getClass()))
				{
					outGasType = ((IGasSource)sourceBlock).takeGasTypeFromSide(world, x2, y2, z2, outPipe.getReverseDirection());
				}
				
				if(endBlock.receiveGas(world, x1, y1, z1, (int)end.getDirection(), outGasType))
				{
					pushedEnd = end.unBranch();
				}
			}
		}
		
		if(pushedEnd != null)
		{
			//pushedEnd = pushedEnd.unBranch();
			while(pushedEnd.length > 1)
			{
				int x1 = x + pushedEnd.x;
				int y1 = y + pushedEnd.y;
				int z1 = z + pushedEnd.z;
				int block1 = world.getBlockId(x1, y1, z1);
				pushedEnd = pushedEnd.unBranch();
				int block2 = world.getBlockId(x + pushedEnd.x, y + pushedEnd.y, z + pushedEnd.z);
				
				if(block1 != block2)
				{
					world.setBlock(x1, y1, z1, block2);
				}
			}
			
			world.setBlock(x, y, z, gasType.gasPipe.blockID);
		}
		
		/*for(int x1 = 0; x1 < 31; x1++)
		{
			for(int y1 = 0; y1 < 31; y1++)
			{
				for(int z1 = 0; z1 < 31; z1++)
				{
					branchedBlocks[x1][y1][z1] = false;
				}
			}
		}
		
		branchedBlocks[15][15][15] = true;*/
		
		return pushedEnd != null;
	}
	
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighboutBlockID)
	{
		//if(this.blockID != Gases.gasPipeEmpty.blockID)
		{
	    	//world.scheduleBlockUpdate(x, y, z, this.blockID, 8);
		}
	}
	
	/*public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
	{
		world.scheduleBlockUpdate(x, y, z, this.blockID, 0);
		return true;
	}*/
	
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
	    return Gases.renderBlockGasPipeID;
	}
	
	public int idPicked(World par1World, int par2, int par3, int par4)
	{
	    return Gases.gasPipeAir.blockID;
	}
	
	/**
	 * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a
	 * different metadata value, but before the new metadata value is set. Args: World, x, y, z, old block ID, old
	 * metadata
	 */
	public void breakBlock(World world, int x, int y, int z, int oldBlockID, int oldBlockMetadata)
	{
		if(type.gasBlock != null && world.isAirBlock(x, y, z))
		{
			world.setBlock(x, y, z, type.gasBlock.blockID);
		}
	}
}