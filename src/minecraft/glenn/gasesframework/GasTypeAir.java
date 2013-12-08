package glenn.gasesframework;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class GasTypeAir extends GasType
{
	public GasTypeAir()
	{
		super(null, GasesFramework.gasPipeAir, 0, "Air", 0, 0, 0, Combustibility.NONE);
	}
	
	public ItemStack getBottledItem()
	{
		return new ItemStack(Item.glassBottle);
	}
	
	public void onBreathed(EntityLivingBase entity)
	{
		
	}
}