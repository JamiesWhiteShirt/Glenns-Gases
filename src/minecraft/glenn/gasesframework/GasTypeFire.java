package glenn.gasesframework;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class GasTypeFire extends GasType
{
	public GasTypeFire(BlockGas gasBlock, int gasIndex)
	{
		super(gasBlock, null, gasIndex, "Ignited Gas", 0xFFFFFF, 0, 0, Combustibility.NONE);
	}
	
	public ResourceLocation getOverlayImage()
	{
		return GasesFramework.fireOverlayImage;
	}
	
	/**
	 * Called when an entity touches the gas in block form.
	 * @param entity
	 */
	public void onTouched(Entity entity)
	{
		entity.setFire(5);
	}
}