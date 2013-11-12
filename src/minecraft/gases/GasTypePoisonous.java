package glenn.gases;

import glenn.gasesframework.BlockGas;
import glenn.gasesframework.BlockGasPipe;
import glenn.gasesframework.Combustibility;
import glenn.gasesframework.GasType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class GasTypePoisonous extends GasType
{
	public GasTypePoisonous(BlockGas gasBlock, BlockGasPipe gasPipe, int gasIndex, String name, int color, int opacity, int density, Combustibility combustibility)
	{
		super(gasBlock, gasPipe, gasIndex, name, color, opacity, density, combustibility);
	}
	
	@Override
	public void onBreathed(EntityLivingBase entity)
	{
		entity.addPotionEffect(new PotionEffect(Potion.poison.id, 300, 0));
	}
}