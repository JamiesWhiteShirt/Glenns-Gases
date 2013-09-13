package glenn.gases;

import glenn.gases.util.IVec;
import glenn.gases.util.PipeBranch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockGasPipe extends Block
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
	
	private static final boolean[][][] branchedBlocks = new boolean[31][31][31];
	private static final ArrayList<PipeBranch> branches = new ArrayList<PipeBranch>();
	private static final ArrayList<IVec> ends = new ArrayList<IVec>();
	
	/**
	 * The gas block contained by this gas pipe.
	 */
	public final BlockGas containedGas;
	
	/**
	 * Constructs a new gas pipe block. Because of technical reasons, each type of gas needs its own gas pipe block for pipe usage.
	 * @param par1 - Block ID
	 * @param containedGas - The gas this pipe will carry
	 */
	public BlockGasPipe(int blockID, BlockGas containedGas)
	{
		super(blockID, Material.circuits);
		this.containedGas = containedGas;
		
		this.setHardness(1.0F);
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
	
	public void updateTick(World world, int x, int y, int z, Random random)
    {
		branches.clear();
		branches.add(new PipeBranch(0, 0, 0, 0, 0));
		ends.clear();
		
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
					if(j == branch.reverseDirection())
					{
						continue;
					}
					
					int x2 = x1 + xDirection[j];
					int y2 = y1 + yDirection[j];
					int z2 = z1 + zDirection[j];
					boolean notBranched = !branchedBlocks[15 + branch.x + xDirection[j]][15 + branch.y + yDirection[j]][15 + branch.z + zDirection[j]];
					
					int directionBlockID = world.getBlockId(x2, y2, z2);
					int directionBlockMetadata = world.getBlockMetadata(x2, y2, z2);
					
					if(branch.length < 14)
					{
						if(/*directionBlockID != Gases.gasPipeEmpty.blockID && */Block.blocksList[directionBlockID] instanceof BlockGasPipe)
						{
							connectedSurroundingBlocks++;
							if(notBranched)
							{
								newBranches[subBranches++] = new PipeBranch(branch.x + xDirection[j], branch.y + yDirection[j], branch.z + zDirection[j], j, branch.length + 1);
								branchedBlocks[15 + branch.x + xDirection[j]][15 + branch.y + yDirection[j]][15 + branch.z + zDirection[j]] = true;
							}
						}
					}
				}
				
				if(connectedSurroundingBlocks <= 0)
				{
					int x2 = x1 + xDirection[branch.direction];
					int y2 = y1 + yDirection[branch.direction];
					int z2 = z1 + zDirection[branch.direction];
					
					if(world.isAirBlock(x2, y2, z2))
					{
						ends.add(new IVec(x2, y2, z2));
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
		
		if(ends.size() > 0)
		{
			IVec end = ends.get(random.nextInt(ends.size()));
			
			world.setBlock(end.x, end.y, end.z, Gases.gasSmoke.blockID);
		}
		
		for(int x1 = 0; x1 < 31; x1++)
		{
			for(int y1 = 0; y1 < 31; y1++)
			{
				for(int z1 = 0; z1 < 31; z1++)
				{
					branchedBlocks[x1][y1][z1] = false;
				}
			}
		}
		
		branchedBlocks[15][15][15] = true;
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
    
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
    	world.scheduleBlockUpdate(x, y, z, this.blockID, 0);
    	return true;
    }
}