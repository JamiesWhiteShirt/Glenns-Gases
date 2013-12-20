package glenn.gasesframework;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class GasType
{
	/**
	 * All gas indexed by their gas index
	 */
	public static final GasType[] gasTypes = new GasType[256];
	
	/**
	 * The gas block associated with this gas type.
	 */
	public final BlockGas gasBlock;
	/**
	 * The gas pipe associated with this gas type. Can be null.
	 */
	public final BlockGasPipe gasPipe;
	/**
	 * The gas index of this gas type.
	 */
	public final int gasIndex;
	/**
	 * A display-friendly name for this gas type.
	 */
	public final String name;
	/**
	 * The color of this gas type, represented as an RGB hex.
	 */
	public final int color;
	/**
	 * The opacity of this gas type. A highly opaque gas will block more light.
	 */
	public final int opacity;
	/**
	 * The density of this gas type. A negative density will make a rising gas. A positive density will make a falling gas. If The density is 0, the gas will spread evenly in all directions.
	 */
	public final int density;
	
	/**
	 * The combustibility of this gas. See {@link glenn.gasesframework.Combustibility Combustibility}.
	 */
	public final Combustibility combustibility;
	/**
	 * How quickly this gas type will evaporate in the world. Higher values will decrease evaporation speed.
	 */
	public int evaporationRate;
	/**
	 * The damage this gas deals upon touch.
	 */
	public float damage;
	
	/**
	 * The rate at which gas of this type will cause blindness.
	 */
	public int blindnessRate;
	/**
	 * The rate at which gas of this type will cause suffocation. For higher values, the player will suffocate earlier and will take damage more frequently.
	 */
	public int suffocationRate;
	/**
	 * How much this gas will gradually slow down the player. NOTE: NOT IMPLEMENTED.
	 */
	public int slownessRate;
	/**
	 * The overlay image used when the player is inside the gas.
	 */
	//public static ResourceLocation overlayImage = new ResourceLocation("gases:textures/misc/gas_overlay.png");
	
	/**
	 * Creates a new industrial gas type. Gas types are automatically registered.
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
	 * Creates a new non-industrial gas type. Gas types are automatically registered.
	 * @param gasBlock - The gas block associated with this GasType.
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
	public GasType(BlockGas gasBlock, int gasIndex, String name, int color, int opacity, int density, Combustibility combustibility)
	{
		this(gasBlock, null, gasIndex, name, color, opacity, density, combustibility);
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
	 * Set how quickly the gas can evaporate. Higher values will decrease evaporation speed.
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
	
	/**
	 * Called when an entity touches the gas in block form.
	 * @param entity
	 */
	public void onTouched(Entity entity)
	{
		if(damage > 0.0F)
    	{
    		entity.attackEntityFrom(DamageSource.generic, damage);
    	}
	}
	
	/**
	 * Can this gas be used in pipes and can it be bottled?
	 * @return
	 */
	public boolean isIndustrial()
	{
		return gasPipe != null;
	}
	
	public ResourceLocation getOverlayImage()
	{
		return GasesFramework.gasOverlayImage;
	}
}