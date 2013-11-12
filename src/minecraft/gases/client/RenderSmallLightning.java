package glenn.gases.client;

import java.util.Random;

import glenn.gases.EntitySmallLightning;
import glenn.gasesframework.util.DVec;
import glenn.gasesframework.util.DVec2;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderSmallLightning extends Render
{
	public RenderSmallLightning()
	{
		
	}
	
	private void renderSmallLightning(EntitySmallLightning entity, double x, double y, double z)
	{
		Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        //GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        
        tessellator.setColorOpaque(255, 255, 255);
        //tessellator.setBrightness(0);
        
        DVec[] renderVertices = new DVec[(entity.vertices.length - 1) * 4];
        
        for(int i = 0; i < entity.vertices.length - 1; i++)
        {
        	DVec prevVertex = i >= 1 ? entity.vertices[i - 1] : entity.vertices[i + 1].inverted();
        	DVec vertex = entity.vertices[i];
        	DVec nextVertex = entity.vertices[i + 1];
        	
        	DVec relVec1 = vertex.subtracted(prevVertex);
        	DVec2 relVec1XZ = relVec1.xz();
        	DVec2 relVec1XY = new DVec2(relVec1XZ.length(), relVec1.y);
        	DVec relVec2 = nextVertex.subtracted(vertex);
        	DVec2 relVec2XZ = relVec2.xz();
        	DVec2 relVec2XY = new DVec2(relVec2XZ.length(), relVec2.y);
        	
        	double scale = ((double)(entity.vertices.length - i) / (double)(entity.vertices.length - 1)) * (relVec1.length() + relVec2.length()) / 50D;
        	double angle1 = (relVec1XY.angle() + relVec2XY.angle()) * 0.5D;
        	double angle2 = (relVec1XZ.angle() + relVec2XZ.angle()) * 0.5D;
        	
        	renderVertices[i * 4] = new DVec(0.0, -1.0, -1.0).scale(scale).zRotate(-angle1).yRotate(-angle2);
        	renderVertices[i * 4 + 1] = new DVec(0.0, 1.0, -1.0).scale(scale).zRotate(-angle1).yRotate(-angle2);
        	renderVertices[i * 4 + 2] = new DVec(0.0, 1.0, 1.0).scale(scale).zRotate(-angle1).yRotate(-angle2);
        	renderVertices[i * 4 + 3] = new DVec(0.0, -1.0, 1.0).scale(scale).zRotate(-angle1).yRotate(-angle2);
        }
        
        {
        	tessellator.startDrawing(GL11.GL_QUADS);
            tessellator.setColorOpaque(255, 255, 255);
        	
        	tessellator.addVertex(x + renderVertices[0].x, y + renderVertices[0].y, z + renderVertices[0].z);
        	tessellator.addVertex(x + renderVertices[3].x, y + renderVertices[3].y, z + renderVertices[3].z);
        	tessellator.addVertex(x + renderVertices[2].x, y + renderVertices[2].y, z + renderVertices[2].z);
        	tessellator.addVertex(x + renderVertices[1].x, y + renderVertices[1].y, z + renderVertices[1].z);
        	
        	tessellator.draw();
        }
        
        for(int i = 0; i < entity.vertices.length - 2; i++)
        {
        	tessellator.startDrawing(GL11.GL_QUAD_STRIP);
            tessellator.setColorOpaque(255, 255, 255);
        	DVec vertex = entity.vertices[i];
        	DVec nextVertex = entity.vertices[i + 1];
        	DVec tempVertex;
        	
        	tempVertex = vertex.added(renderVertices[i * 4]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	tempVertex = nextVertex.added(renderVertices[(i + 1) * 4]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	
        	tempVertex = vertex.added(renderVertices[i * 4 + 3]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	tempVertex = nextVertex.added(renderVertices[(i + 1) * 4 + 3]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	
        	tempVertex = vertex.added(renderVertices[i * 4 + 2]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	tempVertex = nextVertex.added(renderVertices[(i + 1) * 4 + 2]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	
        	tempVertex = vertex.added(renderVertices[i * 4 + 1]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	tempVertex = nextVertex.added(renderVertices[(i + 1) * 4 + 1]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	
        	tempVertex = vertex.added(renderVertices[i * 4]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	tempVertex = nextVertex.added(renderVertices[(i + 1) * 4]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	
        	tessellator.draw();
        }
        
        
        {
        	tessellator.startDrawing(GL11.GL_TRIANGLE_FAN);
            tessellator.setColorOpaque(255, 255, 255);
        	DVec vertex = entity.vertices[entity.vertices.length - 2];
        	DVec nextVertex = entity.vertices[entity.vertices.length - 1];
        	DVec tempVertex;
        	
        	tessellator.addVertex(x + nextVertex.x, y + nextVertex.y, z + nextVertex.z);
        	
        	tempVertex = vertex.added(renderVertices[entity.vertices.length * 4 - 8]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	
        	tempVertex = vertex.added(renderVertices[entity.vertices.length * 4 - 7]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	
        	tempVertex = vertex.added(renderVertices[entity.vertices.length * 4 - 6]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	
        	tempVertex = vertex.added(renderVertices[entity.vertices.length * 4 - 5]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	
        	tempVertex = vertex.added(renderVertices[entity.vertices.length * 4 - 8]);
        	tessellator.addVertex(x + tempVertex.x, y + tempVertex.y, z + tempVertex.z);
        	
        	tessellator.draw();
        }
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        //GL11.glDisable(GL11.GL_BLEND);
	}
	
	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
	{
		renderSmallLightning((EntitySmallLightning)entity, d0, d1, d2);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return null;
	}
}