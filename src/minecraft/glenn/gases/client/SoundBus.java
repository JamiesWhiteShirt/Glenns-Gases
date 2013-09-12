package glenn.gases.client;

import glenn.gases.Gases;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class SoundBus
{
	@ForgeSubscribe
	public void onSound(SoundLoadEvent event)
	{
		try
		{
			event.manager.soundPoolSounds.addSound("gases:effect/spark1.ogg");
			event.manager.soundPoolSounds.addSound("gases:effect/spark2.ogg");
			event.manager.soundPoolSounds.addSound("gases:effect/spark3.ogg");
		}
		catch(Exception e)
		{
			System.out.print("[Gases] Failed to load sound file!");
		}
	}
}
