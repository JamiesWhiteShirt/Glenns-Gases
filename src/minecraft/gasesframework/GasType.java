package glenn.gasesframework;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class GasType
{
	public static final GasType[] gasTypes = new GasType[256];
	
	public final BlockGas gasBlock;
	public final BlockGasPipe gasPipe;
	public final int gasIndex;
	public final String name;
	public final int color;
	public final int opacity;
	public final int density;
	
	public final Combustibility combustibility;
	public int evaporationRate;
	public float damage;
	
	public int blindnessRate;
	public int suffocationRate;
	public int slownessRate;
	
	/**
	 * 
	 * @param gasBlock - The gas block associated with this GasType.
	 * @param gasPipe - The gas pipe associated with this GasType. Can be null if set to non-industrial.
	 * @param gasIndex - An unique index for the GasType. Consult the Gases Framework documentation for unoccupied gas IDs.
	 * @param name - A display-friendly name for the gas type.
	 * @param color - An RGB representation of the color to be used by this gas.
	 * @param opacity - Higher values will increase the opacity of this gas. This will also affect how well light passes through it.
	 * @param density - A value determining how dense the gas will be relative to air.
	 * <ul><li><b>density > 0</b> Will produce a falling gas. Greater values means the gas will move faster.</li>
	 * <li><b>density < 0</b> Will produce a rising gas. Lower values means the gas will move faster.</li>
	 * <li><b>density = 0</b> Will produce a floating gas which will spread in all directions.</li></ul>
	 * @param combustibility - The grade of combustibility of this gas type.
	 */
	public GasType(BlockGas gasBlock, BlockGasPipe gasPipe, int gasIndex, String name, int color, int opacity, int density, Combustibility combustibility)
	{
		this.gasBlock = gasBlock;
		this.gasPipe = gasPipe;
		
		this.gasIndex = gasIndex;
		this.name = name;
        this.color = color;
        this.opacity = opacity;
        this.density = density;
        this.combustibility = combustibility;
        
        this.evaporationRate = 0;
        this.damage = 0.0F;
        this.blindnessRate = 0;
        this.suffocationRate = 0;
        this.slownessRate = 0;
        
        if(gasBlock != null)
        {
        	gasBlock.type = this;
        	gasBlock.setLightOpacity(opacity);
        }
		if(gasPipe != null)
		{
			gasPipe.type = this;
		}
		
		if(gasBlock != null & (combustibility.fireSpreadRate >= 0 | combustibility.explosionPower > 0.0F))
        {
        	gasBlock.setBurnProperties(gasBlock.blockID, 1000, 1000);
        }
		
		if(combustibility.lanternBlock != null)
		{
			GameRegistry.addShapelessRecipe(new ItemStack(combustibility.lanternBlock), new Object[]{new ItemStack(GasesFramework.lanternEmpty), new ItemStack(GasesFramework.gasBottle, 1, gasIndex)});
		}
		
		if(gasTypes[gasIndex] != null)
		{
			System.out.println("A gas named " + name + " has overridden a gas at gas index " + gasIndex);
		}
		
		gasTypes[gasIndex] = this;
	}
	
	/**
	 * Sets the rates of gas effects on this gas type.
	 * @param blindness - How quickly the player will experience gradual blindness.
	 * @param suffocation - How quickly the player will suffocate in the gas, and how often {@link GasType#onBreathed(EntityLivingBase)} will be called
	 * @param slowness - How quickly the player will lose their movement speed inside the gas. NOTE: CURRENTLY UNIMPLEMENTED.
	 * @return
	 */
	public GasType setEffectRates(int blindness, int suffocation, int slowness)
    {
    	this.blindnessRate = blindness;
    	this.suffocationRate = suffocation;
    	this.slownessRate = slowness;
    	return this;
    }
	
	/**
	 * Set how much damage the gas will deal upon contact with the player.
	 * @param damage
	 * @return
	 */
	public GasType setDamage(float damage)
	{
		this.damage = damage;
		return this;
	}
	
	/**
	 * Set how quickly the gas can evaporate in parts per tick.
	 * @param evaporation
	 * @return
	 */
	public GasType setEvaporationRate(int evaporation)
    {
    	this.evaporationRate = evaporation;
    	return this;
    }
	
	/**
	 * Get the bottled item. Is {@link GasesFramework#gasBottle} by default.
	 * @return
	 */
	public ItemStack getBottledItem()
	{
		return new ItemStack(GasesFramework.gasBottle, 1, gasIndex);
	}
	
	/**
	 * Apply effects onto an entity when breathed. A gas is breathed when the player runs out of air in their hidden air meter.
	 * How quickly this happens, and how frequently this method is called depends on this gas type's {@link GasType#suffocationRate}.
	 * @param entity
	 */
	public void onBreathed(EntityLivingBase entity)
	{
		entity.attackEntityFrom(DamageSource.generic, 0.5F);
	}
}