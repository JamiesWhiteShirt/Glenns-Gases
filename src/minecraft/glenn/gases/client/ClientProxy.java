package glenn.gases.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.client.renderer.entity.RenderLightningBolt;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraftforge.client.MinecraftForgeClient;
import glenn.gases.CommonProxy;
import glenn.gases.EntityGlowstoneShard;
import glenn.gases.EntitySmallLightning;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySmallLightning.class, new RenderSmallLightning());
		RenderingRegistry.registerEntityRenderingHandler(EntityGlowstoneShard.class, new RenderGlowstoneShard());
	}
	
	@Override
	public void registerSounds()
	{
		
	}
}
