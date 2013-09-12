package glenn.gases.core;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"glenn.gases.core"})
public class EDFMLLoadingPlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]{EDClassTransformer.class.getName()};
	}

	@Override
	public String[] getLibraryRequestClass()
	{
		return null;
	}

	@Override
	public String getModContainerClass()
	{
		return DummyContainerGases.class.getName();
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
	}
}
