package glenn.gasesframework.core;

import glenn.gasesframework.GasesFramework;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class DummyContainerGasesFramework extends DummyModContainer
{
	public DummyContainerGasesFramework()
	{
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "gasesFrameworkCore";
		meta.name = "Gases Framework Core";
		meta.version = GasesFramework.version;
		meta.description = "Adds core functionality for mods based on the Gases Framework.";
		meta.authorList = Arrays.asList("Glenn");
		meta.url = "http://www.minecraftforum.net/topic/1890587-/";
		meta.screenshots = new String[0];
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}

	@Subscribe
	public void modConstruction(FMLConstructionEvent evt)
	{
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent evt)
	{
	}

	@Subscribe
	public void init(FMLInitializationEvent evt)
	{
	}

	@Subscribe
	public void postInit(FMLPostInitializationEvent evt)
	{
	}
}