package glenn.gases;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockPump extends Block implements IGasReceptor, ITileEntityProvider, ISample
{
	private static final int[] reindex = new int[]{
		1, 0, 5, 4, 3, 2
	};
	
	protected Icon side;
	protected Icon top;
	
	public BlockPump(int blockID)
	{
		super(blockID, Material.iron);
	}
	
	/**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        return reindex[par5];
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return Gases.renderBlockPumpID;
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
    }
	
	@SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
	public Icon getIcon(int par1, int par2)
    {
        boolean flag = true;
        
        switch(par1)
        {
        case 0:
        case 1:
        	flag = par2 == 0 | par2 == 1;
        	break;
        case 2:
        case 3:
        	flag = par2 == 5 | par2 == 4;
        	break;
        case 4:
        case 5:
        	flag = par2 == 3 | par2 == 2;
        	break;
        }
        
        return flag ? top : side;
    }

	@Override
	public boolean receiveGas(World world, int x, int y, int z, int side, GasType gasType)
	{
		if(world.getBlockMetadata(x, y, z) != Gases.reverseDirection(side))
		{
			TileEntityPump tileEntity = (TileEntityPump)world.getBlockTileEntity(x, y, z);
			
			if(tileEntity.acceptsType(gasType) & tileEntity.containedType == null)
			{
				tileEntity.containedType = gasType;
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
	   return new TileEntityPump();
	}

	@Override
	public GasType sampleInteraction(World world, int x, int y, int z, GasType in, boolean excludes)
	{
		TileEntityPump tileEntity = (TileEntityPump)world.getBlockTileEntity(x, y, z);
    	tileEntity.excludes = excludes;
    	tileEntity.filterType = in;
    	
		return in;
	}
}