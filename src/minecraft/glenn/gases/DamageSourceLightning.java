package glenn.gases;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;

public class DamageSourceLightning extends DamageSource
{
	public DamageSourceLightning(String par1Str)
	{
		super(par1Str);
		this.setDamageBypassesArmor();
	}
	
	/**
     * Returns the message to be displayed on player death.
     */
    public ChatMessageComponent getDeathMessage(EntityLivingBase par1EntityLivingBase)
    {
        return ChatMessageComponent.func_111082_b(par1EntityLivingBase.getTranslatedEntityName() + " was electrocuted.", new Object[] {par1EntityLivingBase.getTranslatedEntityName()});
    }
}