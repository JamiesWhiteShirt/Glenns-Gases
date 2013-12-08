package glenn.gasesframework.client;

import org.lwjgl.opengl.GL11;

import glenn.gasesframework.TileEntityTank;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

public class TileEntityTankRenderer extends TileEntitySpecialRenderer
{
	private ResourceLocation texture = new ResourceLocation("gases:textures/misc/gas_tanked.png");
	private Tessellator tessellator;
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d0, (float)d1, (float)d2);
		
		GL11.glDisable(GL11.GL_BLEND);
		renderTankAt((TileEntityTank)tileEntity, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
		
		GL11.glPopMatrix();
	}
	
	public void renderTankAt(TileEntityTank tileEntity, int i, int j, int k)
	{
		double gasHeight = (double)tileEntity.amount / (double)tileEntity.getGasCap();
		
		if(gasHeight > 0.0D)
		{
			bindTexture(texture);
			
			int color = tileEntity.containedType.color;
			
			float red = (float)((color >> 16) & 0xFF) / 255.0F;
	    	float green = (float)((color >> 8) & 0xFF) / 255.0F;
	    	float blue = (float)(color & 0xFF) / 255.0F;
	    	
	    	tessellator = Tessellator.instance;
	    	tessellator.startDrawingQuads();
	    	
	    	double minX = 2.0D / 16.0D;
	    	double maxX = 14.0D / 16.0D;
	    	
	    	double minY = 2.0D / 16.0D;
	    	double maxY = (2.0D + gasHeight * 12.0D) / 16.0D;
	    	
	    	double minZ = 2.0D / 16.0D;
	    	double maxZ = 14.0D / 16.0D;
	    	
	    	tessellator.setColorOpaque_F(red * 0.9F, green * 0.9F, blue * 0.9F);
	    	tessellator.addVertexWithUV(minX, minY, 0.0D, maxX, minY);
	    	tessellator.addVertexWithUV(minX, maxY, 0.0D, maxX, maxY);
	    	tessellator.addVertexWithUV(maxX, maxY, 0.0D, minX, maxY);
	    	tessellator.addVertexWithUV(maxX, minY, 0.0D, minX, minY);
	    	
	    	tessellator.addVertexWithUV(maxX, minY, 1.0D, maxX, minY);
    		tessellator.addVertexWithUV(maxX, maxY, 1.0D, maxX, maxY);
    		tessellator.addVertexWithUV(minX, maxY, 1.0D, minX, maxY);
    		tessellator.addVertexWithUV(minX, minY, 1.0D, minX, minY);
    		
    		tessellator.addVertexWithUV(0.0D, minY, maxZ, minZ, minY);
    		tessellator.addVertexWithUV(0.0D, maxY, maxZ, minZ, maxY);
    		tessellator.addVertexWithUV(0.0D, maxY, minZ, maxZ, maxY);
    		tessellator.addVertexWithUV(0.0D, minY, minZ, maxZ, minY);
    		
    		tessellator.addVertexWithUV(1.0D, minY, minZ, minZ, minY);
    		tessellator.addVertexWithUV(1.0D, maxY, minZ, minZ, maxY);
    		tessellator.addVertexWithUV(1.0D, maxY, maxZ, maxZ, maxY);
    		tessellator.addVertexWithUV(1.0D, minY, maxZ, maxZ, minY);

	    	tessellator.setColorOpaque_F(red, green, blue);
    		tessellator.addVertexWithUV(1.0D, maxY, 1.0D, 1.0D, 1.0D);
    		tessellator.addVertexWithUV(1.0D, maxY, 0.0D, 1.0D, 0.0D);
    		tessellator.addVertexWithUV(0.0D, maxY, 0.0D, 0.0D, 0.0D);
    		tessellator.addVertexWithUV(0.0D, maxY, 1.0D, 0.0D, 1.0D);
    		
    		tessellator.draw();
		}
	}
}