package glenn.gasesframework.util;

import glenn.gasesframework.GasesFramework;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class QueuedLanternRecipe
{
	public ItemStack result;
	public ItemStack ingredient;
	
	public QueuedLanternRecipe(ItemStack result, ItemStack ingredient)
	{
		this.result = result;
		this.ingredient = ingredient;
	}
	
	public void register()
	{
		GameRegistry.addShapelessRecipe(result, new ItemStack(GasesFramework.lanternEmpty), ingredient);
	}
}