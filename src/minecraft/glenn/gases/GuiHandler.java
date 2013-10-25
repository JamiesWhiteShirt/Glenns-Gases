package glenn.gases;

import glenn.gases.gui.GuiGasFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		switch(id)
		{
		case 0:
			return new ContainerGasFurnace(player.inventory, (TileEntityGasFurnace)tileEntity);
		}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		switch(id)
		{
		case 0:
			return new GuiGasFurnace(player.inventory, (TileEntityGasFurnace)tileEntity);
		}
		
		return null;
	}
}