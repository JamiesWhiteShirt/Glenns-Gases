package glenn.gases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class UpdateChecker
{
	private boolean alreadyChecked = !Gases.enableUpdateCheck;
	
	@ForgeSubscribe
	public void onEntityJoinedWorld(EntityJoinWorldEvent event)
	{
		if(event.entity instanceof EntityClientPlayerMP & !alreadyChecked)
		{
			try
			{
				URL url = new URL("http://www.jamieswhiteshirt.com/trackable/gases.txt");
				InputStreamReader isr = new InputStreamReader(url.openStream());
				BufferedReader bf = new BufferedReader(isr);
				
				String version = bf.readLine();
				String link = bf.readLine();
				
				if(versionStringToInt(Gases.version) < versionStringToInt(version))
				{
					((EntityClientPlayerMP)event.entity).addChatMessage("A newer version of Glenn's Gases(" + version + ") is avaliable for download.");
					((EntityClientPlayerMP)event.entity).addChatMessage(link);
				}
				
				bf.close();
			}
			catch(IOException e)
			{
				
			}
			
			alreadyChecked = true;
		}
	}
	
	private int versionStringToInt(String version)
	{
		int versionScore = 0;
		int multiplier = 1;
		String[] splitVersion = version.split("\\.");
		
		for(int i = splitVersion.length - 1; i >= 0; i--, multiplier *= 1000)
		{
			versionScore += Integer.parseInt(splitVersion[i]) * multiplier;
		}
		
		return versionScore;
	}
}