package glenn.gases.client;

import org.lwjgl.opengl.GL11;

import glenn.gases.BlockGasPipe;
import glenn.gases.IGasReceptor;
import glenn.gases.Gases;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBlockGasPipe implements ISimpleBlockRenderingHandler
{
	private Icon icon;
	private double uIconTranslate = 0.0F;
	private double vIconTranslate = 0.0F;
	
	private static final int[] xDirection = new int[]{
		0, 0, 1, -1, 0, 0
	};
	private static final int[] yDirection = new int[]{
		-1, 1, 0, 0, 0, 0
	};
	private static final int[] zDirection = new int[]{
		0, 0, 0, 0, 1, -1
	};
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,RenderBlocks renderer)
	{
		Tessellator tessellator = Tessellator.instance;
		icon = renderer.hasOverrideBlockTexture() ? renderer.overrideBlockTexture : renderer.getBlockIcon(block);

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(1.4F, 1.4F, 1.4F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        
		double d1 = 6.0F / 16.0F;
		double d2 = 10.0F / 16.0F;

        tessellator.startDrawingQuads();
    	
    	uIconTranslate = 0.0D;
    	vIconTranslate = 0.0D;
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		tessellator.addVertexWithUV(0.0D, d2, 1.0D, u(0.0D), v(1.0D));
		tessellator.addVertexWithUV(1.0D, d2, 1.0D, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(1.0D, d2, 0.0D, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(0.0D, d2, 0.0D, u(0.0D), v(0.0D));

        tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
		tessellator.addVertexWithUV(0.0D, d1, 0.0D, u(0.0D), v(0.0D));
		tessellator.addVertexWithUV(1.0D, d1, 0.0D, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(1.0D, d1, 1.0D, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(0.0D, d1, 1.0D, u(0.0D), v(1.0D));
		
		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
		tessellator.addVertexWithUV(d1, 0.0D, 1.0D, u(0.0D), v(1.0D));
		tessellator.addVertexWithUV(d1, 1.0D, 1.0D, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(d1, 1.0D, 0.0D, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(d1, 0.0D, 0.0D, u(0.0D), v(0.0D));
		
		tessellator.addVertexWithUV(d2, 0.0D, 0.0D, u(0.0D), v(0.0D));
		tessellator.addVertexWithUV(d2, 1.0D, 0.0D, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(d2, 1.0D, 1.0D, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(d2, 0.0D, 1.0D, u(0.0D), v(1.0D));
		
		tessellator.addVertexWithUV(0.0D, 1.0D, d1, u(0.0D), v(1.0D));
		tessellator.addVertexWithUV(1.0D, 1.0D, d1, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(1.0D, 0.0D, d1, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(0.0D, 0.0D, d1, u(0.0D), v(0.0D));
		
		tessellator.addVertexWithUV(0.0D, 0.0D, d2, u(0.0D), v(0.0D));
		tessellator.addVertexWithUV(1.0D, 0.0D, d2, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(1.0D, 1.0D, d2, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(0.0D, 1.0D, d2, u(0.0D), v(1.0D));
		
		vIconTranslate = 8.0D;
    	
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		tessellator.addVertexWithUV(0.0D, 1.0D, 1.0D, u(0.0D), v(1.0D));
		tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, u(0.0D), v(0.0D));
		
		tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, u(0.0D), v(0.0D));
		tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(1.0D, 0.0D, 1.0D, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(0.0D, 0.0D, 1.0D, u(0.0D), v(1.0D));
	
		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
		tessellator.addVertexWithUV(0.0D, 0.0D, 1.0D, u(0.0D), v(1.0D));
		tessellator.addVertexWithUV(0.0D, 1.0D, 1.0D, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, u(0.0D), v(0.0D));
		
		tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, u(0.0D), v(0.0D));
		tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(1.0D, 0.0D, 1.0D, u(0.0D), v(1.0D));
		
		tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, u(0.0D), v(1.0D));
		tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, u(0.0D), v(0.0D));
	
		tessellator.addVertexWithUV(0.0D, 0.0D, 1.0D, u(0.0D), v(0.0D));
		tessellator.addVertexWithUV(1.0D, 0.0D, 1.0D, u(1.0D), v(0.0D));
		tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, u(1.0D), v(1.0D));
		tessellator.addVertexWithUV(0.0D, 1.0D, 1.0D, u(0.0D), v(1.0D));

        tessellator.draw();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block bblock, int modelId, RenderBlocks renderer)
	{
		BlockGasPipe block = (BlockGasPipe)bblock;
		
        int brightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
		icon = renderer.hasOverrideBlockTexture() ? renderer.overrideBlockTexture : renderer.getBlockIcon(block);
		Tessellator tessellator = Tessellator.instance;
		
		final boolean[] sidePipe = new boolean[6];
		final boolean[] sideOpaque = new boolean[6];
		
		for(int i = 0; i < 6; i++)
		{
			int x1 = x + xDirection[i];
			int y1 = y + yDirection[i];
			int z1 = z + zDirection[i];
			
			int directionBlockID = blockAccess.getBlockId(x1, y1, z1);
			if(directionBlockID != 0)
			{
				Block directionBlock = Block.blocksList[directionBlockID];
				sidePipe[i] = directionBlock instanceof BlockGasPipe || IGasReceptor.class.isAssignableFrom(directionBlock.getClass());
				sideOpaque[i] = directionBlock.isOpaqueCube() & !sidePipe[i];
			}
		}
		
		double d1 = 6.0F / 16.0F;
		double d2 = 10.0F / 16.0F;
		double d3 = 4.5F / 16.0F;
		double d4 = 11.5F / 16.0F;
		
		boolean collectionAll = sidePipe[0] || sidePipe[1] || sidePipe[2] || sidePipe[3] || sidePipe[4] || sidePipe[5];
		boolean collectionY = sidePipe[2] || sidePipe[3] || sidePipe[4] || sidePipe[5];
		boolean collectionX = sidePipe[0] || sidePipe[1] || sidePipe[4] || sidePipe[5];
		boolean collectionZ = sidePipe[0] || sidePipe[1] || sidePipe[2] || sidePipe[3];
		
		double minX = (sidePipe[3] | !collectionX) & collectionAll ? 0.0F : d1;
		double maxX = (sidePipe[2] | !collectionX) & collectionAll ? 1.0F : d2;
		double minY = (sidePipe[0] | !collectionY) & collectionAll ? 0.0F : d1;
		double maxY = (sidePipe[1] | !collectionY) & collectionAll ? 1.0F : d2;
		double minZ = (sidePipe[5] | !collectionZ) & collectionAll ? 0.0F : d1;
		double maxZ = (sidePipe[4] | !collectionZ) & collectionAll ? 1.0F : d2;
		/*double minX = sidePipe[2] ? 0.0F : 0.0F;		
		double maxX = sidePipe[2] ? 1.0F : 1.0F;
		double minY = sidePipe[0] ? 0.0F : 0.0F;		
		double maxY = sidePipe[1] ? 1.0F : 1.0F;
		double minZ = sidePipe[4] ? 0.0F : 0.0F;		
		double maxZ = sidePipe[5] ? 1.0F : 1.0F;*/

    	tessellator.setBrightness(brightness);
    	tessellator.addTranslation((float)x, (float)y, (float)z);
    	
    	uIconTranslate = 0.0D;
    	vIconTranslate = 0.0D;
    	
    	if(collectionY | !collectionAll)
    	{
    		tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
    		tessellator.addVertexWithUV(minX, d1, minZ, u(minX), v(minZ));
    		tessellator.addVertexWithUV(maxX, d1, minZ, u(maxX), v(minZ));
    		tessellator.addVertexWithUV(maxX, d1, maxZ, u(maxX), v(maxZ));
    		tessellator.addVertexWithUV(minX, d1, maxZ, u(minX), v(maxZ));

    		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
    		tessellator.addVertexWithUV(minX, d2, maxZ, u(minX), v(maxZ));
    		tessellator.addVertexWithUV(maxX, d2, maxZ, u(maxX), v(maxZ));
    		tessellator.addVertexWithUV(maxX, d2, minZ, u(maxX), v(minZ));
    		tessellator.addVertexWithUV(minX, d2, minZ, u(minX), v(minZ));
    	}

    	if(collectionX | !collectionAll)
    	{
    		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
    		tessellator.addVertexWithUV(d1, minY, maxZ, u(minY), v(maxZ));
    		tessellator.addVertexWithUV(d1, maxY, maxZ, u(maxY), v(maxZ));
    		tessellator.addVertexWithUV(d1, maxY, minZ, u(maxY), v(minZ));
    		tessellator.addVertexWithUV(d1, minY, minZ, u(minY), v(minZ));
    		
    		tessellator.addVertexWithUV(d2, minY, minZ, u(minY), v(minZ));
    		tessellator.addVertexWithUV(d2, maxY, minZ, u(maxY), v(minZ));
    		tessellator.addVertexWithUV(d2, maxY, maxZ, u(maxY), v(maxZ));
    		tessellator.addVertexWithUV(d2, minY, maxZ, u(minY), v(maxZ));
    	}
    	
    	if(collectionZ | !collectionAll)
    	{
    		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
    		tessellator.addVertexWithUV(minX, maxY, d1, u(minX), v(maxY));
    		tessellator.addVertexWithUV(maxX, maxY, d1, u(maxX), v(maxY));
    		tessellator.addVertexWithUV(maxX, minY, d1, u(maxX), v(minY));
    		tessellator.addVertexWithUV(minX, minY, d1, u(minX), v(minY));
    		
    		tessellator.addVertexWithUV(minX, minY, d2, u(minX), v(minY));
    		tessellator.addVertexWithUV(maxX, minY, d2, u(maxX), v(minY));
    		tessellator.addVertexWithUV(maxX, maxY, d2, u(maxX), v(maxY));
    		tessellator.addVertexWithUV(minX, maxY, d2, u(minX), v(maxY));
    	}
    	
    	if(collectionAll)
    	{
    		vIconTranslate = 8.0D;
        	
    		if(maxY == 1.0D)
    		{
        		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
    			tessellator.addVertexWithUV(minX, maxY, maxZ, u(minX), v(maxZ));
        		tessellator.addVertexWithUV(maxX, maxY, maxZ, u(maxX), v(maxZ));
        		tessellator.addVertexWithUV(maxX, maxY, minZ, u(maxX), v(minZ));
        		tessellator.addVertexWithUV(minX, maxY, minZ, u(minX), v(minZ));
    		}
    		
    		if(minY == 0.0D)
    		{
        		tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
    			tessellator.addVertexWithUV(minX, minY, minZ, u(minX), v(minZ));
        		tessellator.addVertexWithUV(maxX, minY, minZ, u(maxX), v(minZ));
        		tessellator.addVertexWithUV(maxX, minY, maxZ, u(maxX), v(maxZ));
        		tessellator.addVertexWithUV(minX, minY, maxZ, u(minX), v(maxZ));
    		}
    	
    		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
    		if(minX == 0.0D)
    		{
    			tessellator.addVertexWithUV(minX, minY, maxZ, u(minY), v(maxZ));
        		tessellator.addVertexWithUV(minX, maxY, maxZ, u(maxY), v(maxZ));
        		tessellator.addVertexWithUV(minX, maxY, minZ, u(maxY), v(minZ));
        		tessellator.addVertexWithUV(minX, minY, minZ, u(minY), v(minZ));
    		}
    		
    		if(maxX == 1.0D)
    		{
    			tessellator.addVertexWithUV(maxX, minY, minZ, u(minY), v(minZ));
        		tessellator.addVertexWithUV(maxX, maxY, minZ, u(maxY), v(minZ));
        		tessellator.addVertexWithUV(maxX, maxY, maxZ, u(maxY), v(maxZ));
        		tessellator.addVertexWithUV(maxX, minY, maxZ, u(minY), v(maxZ));
    		}
    	
    		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
    		if(minZ == 0.0D)
    		{
    			tessellator.addVertexWithUV(minX, maxY, minZ, u(minX), v(maxY));
        		tessellator.addVertexWithUV(maxX, maxY, minZ, u(maxX), v(maxY));
        		tessellator.addVertexWithUV(maxX, minY, minZ, u(maxX), v(minY));
        		tessellator.addVertexWithUV(minX, minY, minZ, u(minX), v(minY));
    		}
    		
    		if(maxZ == 1.0D)
    		{
    			tessellator.addVertexWithUV(minX, minY, maxZ, u(minX), v(minY));
        		tessellator.addVertexWithUV(maxX, minY, maxZ, u(maxX), v(minY));
        		tessellator.addVertexWithUV(maxX, maxY, maxZ, u(maxX), v(maxY));
        		tessellator.addVertexWithUV(minX, maxY, maxZ, u(minX), v(maxY));
    		}
    		
    		if(((x ^ y ^ z) & 1) > 0)
    		{
    			uIconTranslate = 8.0D;
            	vIconTranslate = 0.0D;
            	
            	minX = sideOpaque[3] ? 0.0F : d1;
        		maxX = sideOpaque[2] ? 1.0F : d2;
        		minY = sideOpaque[0] ? 0.0F : d1;
        		maxY = sideOpaque[1] ? 1.0F : d2;
        		minZ = sideOpaque[5] ? 0.0F : d1;
        		maxZ = sideOpaque[4] ? 1.0F : d2;
        		
        		if(sidePipe[0] | !collectionY)
            	{
            		tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
            		tessellator.addVertexWithUV(minX, d3, minZ, u(minX), v(minZ));
            		tessellator.addVertexWithUV(maxX, d3, minZ, u(maxX), v(minZ));
            		tessellator.addVertexWithUV(maxX, d3, maxZ, u(maxX), v(maxZ));
            		tessellator.addVertexWithUV(minX, d3, maxZ, u(minX), v(maxZ));

            		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            		tessellator.addVertexWithUV(minX, d3, maxZ, u(minX), v(maxZ));
            		tessellator.addVertexWithUV(maxX, d3, maxZ, u(maxX), v(maxZ));
            		tessellator.addVertexWithUV(maxX, d3, minZ, u(maxX), v(minZ));
            		tessellator.addVertexWithUV(minX, d3, minZ, u(minX), v(minZ));
            	}
        		
        		if(sidePipe[1] | !collectionY)
            	{
            		tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
            		tessellator.addVertexWithUV(minX, d4, minZ, u(minX), v(minZ));
            		tessellator.addVertexWithUV(maxX, d4, minZ, u(maxX), v(minZ));
            		tessellator.addVertexWithUV(maxX, d4, maxZ, u(maxX), v(maxZ));
            		tessellator.addVertexWithUV(minX, d4, maxZ, u(minX), v(maxZ));

            		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            		tessellator.addVertexWithUV(minX, d4, maxZ, u(minX), v(maxZ));
            		tessellator.addVertexWithUV(maxX, d4, maxZ, u(maxX), v(maxZ));
            		tessellator.addVertexWithUV(maxX, d4, minZ, u(maxX), v(minZ));
            		tessellator.addVertexWithUV(minX, d4, minZ, u(minX), v(minZ));
            	}
        		
        		if(sidePipe[2] | !collectionX)
            	{
            		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
            		tessellator.addVertexWithUV(d4, minY, maxZ, u(minY), v(maxZ));
            		tessellator.addVertexWithUV(d4, maxY, maxZ, u(maxY), v(maxZ));
            		tessellator.addVertexWithUV(d4, maxY, minZ, u(maxY), v(minZ));
            		tessellator.addVertexWithUV(d4, minY, minZ, u(minY), v(minZ));
            		
            		tessellator.addVertexWithUV(d4, minY, minZ, u(minY), v(minZ));
            		tessellator.addVertexWithUV(d4, maxY, minZ, u(maxY), v(minZ));
            		tessellator.addVertexWithUV(d4, maxY, maxZ, u(maxY), v(maxZ));
            		tessellator.addVertexWithUV(d4, minY, maxZ, u(minY), v(maxZ));
            	}

            	if(sidePipe[3] | !collectionX)
            	{
            		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
            		tessellator.addVertexWithUV(d3, minY, maxZ, u(minY), v(maxZ));
            		tessellator.addVertexWithUV(d3, maxY, maxZ, u(maxY), v(maxZ));
            		tessellator.addVertexWithUV(d3, maxY, minZ, u(maxY), v(minZ));
            		tessellator.addVertexWithUV(d3, minY, minZ, u(minY), v(minZ));
            		
            		tessellator.addVertexWithUV(d3, minY, minZ, u(minY), v(minZ));
            		tessellator.addVertexWithUV(d3, maxY, minZ, u(maxY), v(minZ));
            		tessellator.addVertexWithUV(d3, maxY, maxZ, u(maxY), v(maxZ));
            		tessellator.addVertexWithUV(d3, minY, maxZ, u(minY), v(maxZ));
            	}
            	
            	if(sidePipe[4] | !collectionZ)
            	{
            		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
            		tessellator.addVertexWithUV(minX, maxY, d4, u(minX), v(maxY));
            		tessellator.addVertexWithUV(maxX, maxY, d4, u(maxX), v(maxY));
            		tessellator.addVertexWithUV(maxX, minY, d4, u(maxX), v(minY));
            		tessellator.addVertexWithUV(minX, minY, d4, u(minX), v(minY));
            		
            		tessellator.addVertexWithUV(minX, minY, d4, u(minX), v(minY));
            		tessellator.addVertexWithUV(maxX, minY, d4, u(maxX), v(minY));
            		tessellator.addVertexWithUV(maxX, maxY, d4, u(maxX), v(maxY));
            		tessellator.addVertexWithUV(minX, maxY, d4, u(minX), v(maxY));
            	}
            	
            	if(sidePipe[5] | !collectionZ)
            	{
            		tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
            		tessellator.addVertexWithUV(minX, maxY, d3, u(minX), v(maxY));
            		tessellator.addVertexWithUV(maxX, maxY, d3, u(maxX), v(maxY));
            		tessellator.addVertexWithUV(maxX, minY, d3, u(maxX), v(minY));
            		tessellator.addVertexWithUV(minX, minY, d3, u(minX), v(minY));
            		
            		tessellator.addVertexWithUV(minX, minY, d3, u(minX), v(minY));
            		tessellator.addVertexWithUV(maxX, minY, d3, u(maxX), v(minY));
            		tessellator.addVertexWithUV(maxX, maxY, d3, u(maxX), v(maxY));
            		tessellator.addVertexWithUV(minX, maxY, d3, u(minX), v(maxY));
            	}
    		}
    	}
    	
    	tessellator.addTranslation((float)-x, (float)-y, (float)-z);
		
		return true;
	}
	
	private double u(double u)
	{
		return icon.getInterpolatedU(u * 8.0D + uIconTranslate);
	}
	
	private double v(double v)
	{
		return icon.getInterpolatedV(v * 8.0D + vIconTranslate);
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return Gases.renderBlockGasPipeID;
	}
}