package glenn.gases;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

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
	
	public Combustibility combustibility;
	public int evaporationRate;
	public float damage;
	
	public int blindnessRate;
	public int suffocationRate;
	public int slownessRate;
	
	private ItemStack bottledItem;
	
	public GasType(BlockGas gasBlock, BlockGasPipe gasPipe, ItemStack bottledItem, int gasIndex, String name, int color, int opacity, int density)
	{
		this.gasBlock = gasBlock;
		this.gasPipe = gasPipe;
		this.bottledItem = bottledItem;
		
		this.gasIndex = gasIndex;
		this.name = name;
        this.color = color;
        this.opacity = opacity;
        this.density = density;
        this.combustibility = Combustibility.NONE;
        
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
	
	public GasType setCombustibility(Combustibility combustibility)
	{
		this.combustibility = combustibility;
		
    	if(gasBlock != null & (combustibility.fireSpreadRate >= 0 | combustibility.explosionPower > 0.0F))
        {
        	gasBlock.setBurnProperties(gasBlock.blockID, 1000, 1000);
        }
		
		return this;
	}
	
	public GasType setEvaporationRate(int evaporation)
    {
    	this.evaporationRate = evaporation;
    	return this;
    }
	
	public ItemStack getBottledItem()
	{
		if(bottledItem != null)
		{
			return bottledItem.copy();
		}
		else
		{
			return null;
		}
	}
}