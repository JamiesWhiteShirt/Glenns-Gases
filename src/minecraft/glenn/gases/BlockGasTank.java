package glenn.gases;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGasTank extends Block implements GasSource, GasReceptor, ITileEntityProvider
{
	public Icon side;
	public Icon top;
	public Icon inside;
	
	public BlockGasTank(int blockID)
	{
		super(blockID, Material.circuits);
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
        inside = par1IconRegister.registerIcon(this.getTextureName() + "_inside");
    }
	
	@Override
	public GasType getGasTypeFromSide(World world, int x, int y, int z, int side)
	{
		TileEntityTank tileEntity = (TileEntityTank)world.getBlockTileEntity(x, y, z);
		
		return tileEntity.containedType;
	}

	@Override
	public GasType takeGasTypeFromSide(World world, int x, int y, int z, int side)
	{
		TileEntityTank tileEntity = (TileEntityTank)world.getBlockTileEntity(x, y, z);
		GasType gasType = tileEntity.containedType;
		tileEntity.decrement();
		
		return gasType;
	}
	
	@Override
	public boolean receiveGas(World world, int x, int y, int z, int side, GasType gasType)
	{
		TileEntityTank tileEntity = (TileEntityTank)world.getBlockTileEntity(x, y, z);
		return tileEntity.increment(gasType);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
	   return new TileEntityTank();
	}
	
	public boolean isOpaqueCube()
	{
		return false;
	}

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return Gases.renderBlockTankID;
    }
}