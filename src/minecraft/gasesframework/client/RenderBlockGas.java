package glenn.gasesframework.client;

import glenn.gasesframework.BlockGas;
import glenn.gasesframework.GasesFramework;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBlockGas implements ISimpleBlockRenderingHandler
{
	private Icon icon;
	private Tessellator tessellator;
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		Tessellator tessellator = Tessellator.instance;

		float red = 1.0F;
		float green = 1.0F;
		float blue = 1.0F;

		if (renderer.useInventoryTint)
        {
            int var6 = block.getRenderColor(metadata);

            red = (float)(var6 >> 16 & 255) / 255.0F;
            green = (float)(var6 >> 8 & 255) / 255.0F;
            blue = (float)(var6 & 255) / 255.0F;
        }

        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glColor4f(red, green, blue, 1.0F);

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int i, int j, int k, Block bblock, int modelId, RenderBlocks renderer)
	{
		/*BlockGas block = (BlockGas)bblock;

		int metadata = 16 - blockAccess.getBlockMetadata(i, j, k);
    	double xBotIndent = block.sideIndent(blockAccess, i - 1, j, k);
    	double xTopIndent = block.sideIndent(blockAccess, i + 1, j, k);
    	double yBotIndent = block.sideIndent(blockAccess, i, j - 1, k);
    	double yTopIndent = block.sideIndent(blockAccess, i, j + 1, k);
    	double zBotIndent = block.sideIndent(blockAccess, i, j, k - 1);
    	double zTopIndent = block.sideIndent(blockAccess, i, j, k + 1);
    	double xBot = 0.0D;
    	double xTop = 1.0D;
    	double yBot = 0.0D;
    	double yTop = 1.0D;
    	double zBot = 0.0D;
    	double zTop = 1.0D;

		Icon icon = renderer.hasOverrideBlockTexture() ? renderer.overrideBlockTexture : renderer.getBlockIcon(block);
        int brightness = block.getMixedBrightnessForBlock(blockAccess, i, j, k);
        int color = block.colorMultiplier(blockAccess, i, j, k);
    	Tessellator tessellator = Tessellator.instance;
        //tessellator.setColorOpaque_F((float)((color >> 16) & 0xFF) / 255.0F, (float)((color >> 8) & 0xFF) / 255.0F, (float)(color & 0xFF) / 255.0F);
    	tessellator.setColorRGBA((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, 128 + block.opacity * 32);

    	if(block.density > 0)
    	{
    		yTop = (double)(metadata) / 16.0D;
    	} else if(block.density < 0)
    	{
    		yBot = 1.0D - (double)(metadata) / 16.0D;
    	} else
    	{
    		yTop = (double)(metadata) / 32.0D;
    		yBot = 1.0D - (double)(metadata) / 32.0D;
    	}

    	renderer.setRenderBounds(xBot + xBotIndent, yBot + yBotIndent, zBot + zBotIndent, xTop - xTopIndent, yTop - yTopIndent, zTop - zTopIndent);
    	//this.renderStandardBlock(block, i, j, k);

    	if(block.shouldSideBeRendered(blockAccess, i, j - 1, k, 0))
    	{
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? brightness : block.getMixedBrightnessForBlock(renderer.blockAccess, i, j - 1, k));
    		renderer.renderFaceYNeg(block, i, j, k, icon);
    	}

    	if(block.shouldSideBeRendered(blockAccess, i, j + 1, k, 1))
    	{
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k));
            renderer.renderFaceYPos(block, i, j, k, icon);
    	}

    	if(block.shouldSideBeRendered(blockAccess, i, j, k - 1, 2))
    	{
	    	if(block.density > 0)
	    	{
	    		yBot = (double)(16 - (blockAccess.getBlockId(i, j, k - 1) == block.blockID ? (double)blockAccess.getBlockMetadata(i, j, k - 1) : 16)) / 16.0D;
	    	} else if(block.density < 0)
	    	{
	    		yTop = 1.0D - (double)(16 - (blockAccess.getBlockId(i, j, k - 1) == block.blockID ? (double)blockAccess.getBlockMetadata(i, j, k - 1) : 16)) / 16.0D;
	    	}

	    	renderer.setRenderBounds(xBot + xBotIndent, yBot + yBotIndent, zBot + zBotIndent, xTop - xTopIndent, yTop - yTopIndent, zTop - zTopIndent);
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k));
            renderer.renderFaceZNeg(block, i, j, k, icon);
    	}

    	if(block.shouldSideBeRendered(blockAccess, i, j, k + 1, 3))
    	{
    		if(block.density > 0)
	    	{
	    		yBot = (double)(16 - (blockAccess.getBlockId(i, j, k + 1) == block.blockID ? (double)blockAccess.getBlockMetadata(i, j, k + 1) : 16)) / 16.0D;
	    	} else if(block.density < 0)
	    	{
	    		yTop = 1.0D - (double)(16 - (blockAccess.getBlockId(i, j, k + 1) == block.blockID ? (double)blockAccess.getBlockMetadata(i, j, k + 1) : 16)) / 16.0D;
	    	}

    		renderer.setRenderBounds(xBot + xBotIndent, yBot + yBotIndent, zBot + zBotIndent, xTop - xTopIndent, yTop - yTopIndent, zTop - zTopIndent);
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k));
            renderer.renderFaceZPos(block, i, j, k, icon);
    	}

    	if(block.shouldSideBeRendered(blockAccess, i - 1, j, k, 4))
    	{
    		if(block.density > 0)
	    	{
	    		yBot = (double)(16 - (blockAccess.getBlockId(i - 1, j, k) == block.blockID ? (double)blockAccess.getBlockMetadata(i - 1, j, k) : 16)) / 16.0D;
	    	} else if(block.density < 0)
	    	{
	    		yTop = 1.0D - (double)(16 - (blockAccess.getBlockId(i - 1, j, k) == block.blockID ? (double)blockAccess.getBlockMetadata(i - 1, j, k) : 16)) / 16.0D;
	    	}

    		renderer.setRenderBounds(xBot + xBotIndent, yBot + yBotIndent, zBot + zBotIndent, xTop - xTopIndent, yTop - yTopIndent, zTop - zTopIndent);
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k));
            renderer.renderFaceXNeg(block, i, j, k, icon);
    	}

    	if(block.shouldSideBeRendered(blockAccess, i + 1, j, k, 5))
    	{
    		if(block.density > 0)
	    	{
	    		yBot = (double)(16 - (blockAccess.getBlockId(i + 1, j, k) == block.blockID ? (double)blockAccess.getBlockMetadata(i + 1, j, k) : 16)) / 16.0D;
	    	} else if(block.density < 0)
	    	{
	    		yTop = 1.0D - (double)(16 - (blockAccess.getBlockId(i + 1, j, k) == block.blockID ? (double)blockAccess.getBlockMetadata(i + 1, j, k) : 16)) / 16.0D;
	    	}

    		renderer.setRenderBounds(xBot + xBotIndent, yBot + yBotIndent, zBot + zBotIndent, xTop - xTopIndent, yTop - yTopIndent, zTop - zTopIndent);
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k));
            renderer.renderFaceXPos(block, i, j, k, icon);
    	}*/
		
		BlockGas block = (BlockGas)bblock;
		
		int metadata = blockAccess.getBlockMetadata(i, j, k);
		int sideBlockID;
		int sideBlockMetadata;

		icon = renderer.hasOverrideBlockTexture() ? renderer.overrideBlockTexture : renderer.getBlockIcon(block);
        int brightness = block.getMixedBrightnessForBlock(blockAccess, i, j, k);
    	tessellator = Tessellator.instance;
    	int color = block.colorMultiplier(blockAccess, i, j, k);
    	
    	float red = (float)((color >> 16) & 0xFF) / 255.0F;
    	float green = (float)((color >> 8) & 0xFF) / 255.0F;
    	float blue = (float)(color & 0xFF) / 255.0F;

    	double minX = block.sideIndent(blockAccess, i - 1, j, k);
    	double maxX = 1.0D - block.sideIndent(blockAccess, i + 1, j, k);
    	
    	double minY = block.getMinY(metadata) + block.sideIndent(blockAccess, i, j - 1, k);
    	double maxY = block.getMaxY(metadata) - block.sideIndent(blockAccess, i, j + 1, k);
    	
    	double minZ = block.sideIndent(blockAccess, i, j, k - 1);
    	double maxZ = 1.0D - block.sideIndent(blockAccess, i, j, k + 1);
    	
    	double sideMinY;
    	double sideMaxY;

    	tessellator.setBrightness(brightness);
    	//tessellator.setColorRGBA((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, 128 + block.opacity * 32);
    	//tessellator.setColorRGBA((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, 255);
    	tessellator.addTranslation((float)i, (float)j, (float)k);
    	
		tessellator.setColorOpaque_F(red * 0.9F, green * 0.9F, blue * 0.9F);
    	if(block.shouldSideBeRendered(blockAccess, i, j, k, 3))
    	{
    		sideBlockID = blockAccess.getBlockId(i, j, k - 1);
    		if(sideBlockID == block.blockID)
    		{
        		sideBlockMetadata = blockAccess.getBlockMetadata(i, j, k - 1);
    			sideMinY = ((BlockGas)Block.blocksList[sideBlockID]).getMinY(sideBlockMetadata);
    			sideMaxY = ((BlockGas)Block.blocksList[sideBlockID]).getMaxY(sideBlockMetadata);
    			
    			if(minY < sideMinY)
    			{
    				vertexAutoMap(minX, minY, minZ, maxX, minY);
    		    	vertexAutoMap(minX, sideMinY, minZ, maxX, sideMinY);
    		    	vertexAutoMap(maxX, sideMinY, minZ, minX, sideMinY);
    		    	vertexAutoMap(maxX, minY, minZ, minX, minY);
    			}
    			
    			if(maxY > sideMaxY)
    			{
    				vertexAutoMap(minX, sideMaxY, minZ, maxX, sideMaxY);
    		    	vertexAutoMap(minX, maxY, minZ, maxX, maxY);
    		    	vertexAutoMap(maxX, maxY, minZ, minX, maxY);
    		    	vertexAutoMap(maxX, sideMaxY, minZ, minX, sideMaxY);
    			}
    		} else
    		{
    			vertexAutoMap(minX, minY, minZ, maxX, minY);
		    	vertexAutoMap(minX, maxY, minZ, maxX, maxY);
		    	vertexAutoMap(maxX, maxY, minZ, minX, maxY);
		    	vertexAutoMap(maxX, minY, minZ, minX, minY);
    		}
    		
    	}
    	
    	if(block.shouldSideBeRendered(blockAccess, i, j, k, 2))
    	{
    		sideBlockID = blockAccess.getBlockId(i, j, k + 1);
    		if(sideBlockID == block.blockID)
    		{
        		sideBlockMetadata = blockAccess.getBlockMetadata(i, j, k + 1);
    			sideMinY = ((BlockGas)Block.blocksList[sideBlockID]).getMinY(sideBlockMetadata);
    			sideMaxY = ((BlockGas)Block.blocksList[sideBlockID]).getMaxY(sideBlockMetadata);
    			
    			if(minY < sideMinY)
    			{
    				vertexAutoMap(maxX, minY, maxZ, maxX, minY);
    	    		vertexAutoMap(maxX, sideMinY, maxZ, maxX, sideMinY);
    	    		vertexAutoMap(minX, sideMinY, maxZ, minX, sideMinY);
    	    		vertexAutoMap(minX, minY, maxZ, minX, minY);
    			}
    			
    			if(maxY > sideMaxY)
    			{
    				vertexAutoMap(maxX, sideMaxY, maxZ, maxX, sideMaxY);
    	    		vertexAutoMap(maxX, maxY, maxZ, maxX, maxY);
    	    		vertexAutoMap(minX, maxY, maxZ, minX, maxY);
    	    		vertexAutoMap(minX, sideMaxY, maxZ, minX, sideMaxY);
    			}
    		} else
    		{
	    		vertexAutoMap(maxX, minY, maxZ, maxX, minY);
	    		vertexAutoMap(maxX, maxY, maxZ, maxX, maxY);
	    		vertexAutoMap(minX, maxY, maxZ, minX, maxY);
	    		vertexAutoMap(minX, minY, maxZ, minX, minY);
    		}
    	}
    	
    	if(block.shouldSideBeRendered(blockAccess, i, j, k, 5))
    	{
    		sideBlockID = blockAccess.getBlockId(i - 1, j, k);
    		if(sideBlockID == block.blockID)
    		{
        		sideBlockMetadata = blockAccess.getBlockMetadata(i - 1, j, k);
    			sideMinY = ((BlockGas)Block.blocksList[sideBlockID]).getMinY(sideBlockMetadata);
    			sideMaxY = ((BlockGas)Block.blocksList[sideBlockID]).getMaxY(sideBlockMetadata);
    			
    			if(minY < sideMinY)
    			{
    				vertexAutoMap(minX, minY, minZ, minZ, sideMinY);
    	    		vertexAutoMap(minX, sideMinY, minZ, minZ, minY);
    	    		vertexAutoMap(minX, sideMinY, maxZ, maxZ, minY);
    	    		vertexAutoMap(minX, minY, maxZ, maxZ, sideMinY);
    			}
    			
    			if(maxY > sideMaxY)
    			{
    				vertexAutoMap(minX, sideMaxY, minZ, minZ, maxY);
    	    		vertexAutoMap(minX, maxY, minZ, minZ, sideMaxY);
    	    		vertexAutoMap(minX, maxY, maxZ, maxZ, sideMaxY);
    	    		vertexAutoMap(minX, sideMaxY, maxZ, maxZ, maxY);
    			}
    		} else
    		{
	    		vertexAutoMap(minX, minY, minZ, minZ, maxY);
	    		vertexAutoMap(minX, maxY, minZ, minZ, minY);
	    		vertexAutoMap(minX, maxY, maxZ, maxZ, minY);
	    		vertexAutoMap(minX, minY, maxZ, maxZ, maxY);
    		}
    	}
    	
    	if(block.shouldSideBeRendered(blockAccess, i, j, k, 4))
    	{
    		sideBlockID = blockAccess.getBlockId(i + 1, j, k);
    		if(sideBlockID == block.blockID)
    		{
        		sideBlockMetadata = blockAccess.getBlockMetadata(i + 1, j, k);
    			sideMinY = ((BlockGas)Block.blocksList[sideBlockID]).getMinY(sideBlockMetadata);
    			sideMaxY = ((BlockGas)Block.blocksList[sideBlockID]).getMaxY(sideBlockMetadata);
    			
    			if(minY < sideMinY)
    			{
    				vertexAutoMap(maxX, minY, maxZ, minZ, sideMinY);
    	    		vertexAutoMap(maxX, sideMinY, maxZ, minZ, minY);
    	    		vertexAutoMap(maxX, sideMinY, minZ, maxZ, minY);
    	    		vertexAutoMap(maxX, minY, minZ, maxZ, sideMinY);
    			}
    			
    			if(maxY > sideMaxY)
    			{
    				vertexAutoMap(maxX, sideMaxY, maxZ, minZ, maxY);
    	    		vertexAutoMap(maxX, maxY, maxZ, minZ, sideMaxY);
    	    		vertexAutoMap(maxX, maxY, minZ, maxZ, sideMaxY);
    	    		vertexAutoMap(maxX, sideMaxY, minZ, maxZ, maxY);
    			}
    		} else
    		{
	    		vertexAutoMap(maxX, minY, maxZ, minZ, maxY);
	    		vertexAutoMap(maxX, maxY, maxZ, minZ, minY);
	    		vertexAutoMap(maxX, maxY, minZ, maxZ, minY);
	    		vertexAutoMap(maxX, minY, minZ, maxZ, maxY);
    		}
    	}

		tessellator.setColorOpaque_F(red * 0.8F, green * 0.8F, blue * 0.8F);
    	if(block.shouldSideBeRendered(blockAccess, i, j, k, 1))
    	{
    		vertexAutoMap(maxX, minY, minZ, maxX, maxZ);
    		vertexAutoMap(maxX, minY, maxZ, maxX, minZ);
    		vertexAutoMap(minX, minY, maxZ, minX, minZ);
    		vertexAutoMap(minX, minY, minZ, minX, maxZ);
    	}

		tessellator.setColorOpaque_F(red, green, blue);
    	if(block.shouldSideBeRendered(blockAccess, i, j, k, 0))
    	{
    		vertexAutoMap(maxX, maxY, maxZ, maxX, maxZ);
    		vertexAutoMap(maxX, maxY, minZ, maxX, minZ);
    		vertexAutoMap(minX, maxY, minZ, minX, minZ);
    		vertexAutoMap(minX, maxY, maxZ, minX, maxZ);
    	}

    	tessellator.addTranslation((float)-i, (float)-j, (float)-k);

    	return true;
	}
	
	private void vertexAutoMap(double x, double y, double z, double u, double v)
	{
		tessellator.addVertexWithUV(x, y, z, icon.getInterpolatedU(u * 16.0D), icon.getInterpolatedV(v * 16.0D));
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return GasesFramework.renderBlockGasID;
	}
}