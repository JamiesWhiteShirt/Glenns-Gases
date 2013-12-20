package glenn.gasesframework;

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

public class BlockGasCollector extends BlockGasPump
{
	private static final int[] reindex = new int[]{
		1, 0, 5, 4, 3, 2
	};
	
	public BlockGasCollector(int blockID)
	{
		super(blockID);
	}
	
	@SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
	public Icon getIcon(int par1, int par2)
    {
        return par1 == reindex[par2] ? top : side;
    }
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
	   return new TileEntityGasCollector();
	}

	@Override
	public boolean receiveGas(World world, int x, int y, int z, int side, GasType gasType)
	{
		return false;
	}
}