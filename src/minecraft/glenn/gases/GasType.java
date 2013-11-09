package glenn.gases;

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
			GameRegistry.addShapelessRecipe(new ItemStack(combustibility.lanternBlock), new Object[]{new ItemStack(Gases.lanternEmpty), new ItemStack(Gases.gasBottle, 1, gasIndex)});
		}
		
		if(gasTypes[gasIndex] != null)
		{
			System.out.println("A gas named " + name + " has overridden a gas at gas index " + gasIndex);
		}
		
		gasTypes[gasIndex] = this;
	}
	
	public GasType setEffectRates(int blindness, int suffocation, int slowness)
    {
    	this.blindnessRate = blindness;
    	this.suffocationRate = suffocation;
    	this.slownessRate = slowness;
    	return this;
    }
	
	public GasType setDamage(float damage)
	{
		this.damage = damage;
		return this;
	}
	
	public GasType setEvaporationRate(int evaporation)
    {
    	this.evaporationRate = evaporation;
    	return this;
    }
	
	public ItemStack getBottledItem()
	{
		return new ItemStack(Gases.gasBottle, 1, gasIndex);
	}
	
	public void onBreathed(EntityLivingBase entity)
	{
		entity.attackEntityFrom(DamageSource.generic, 0.5F);
	}
}