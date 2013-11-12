package glenn.gasesframework.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.client.renderer.entity.RenderLightningBolt;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraftforge.client.MinecraftForgeClient;
import glenn.gasesframework.CommonProxy;
import glenn.gasesframework.TileEntityTank;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, new TileEntityTankRenderer());
	}
	
	@Override
	public void registerSounds()
	{
		
	}
}
