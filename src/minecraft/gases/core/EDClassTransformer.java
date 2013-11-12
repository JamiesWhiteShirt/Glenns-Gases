package glenn.gases.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

import static org.objectweb.asm.Opcodes.*;

public class EDClassTransformer implements IClassTransformer
{
	private FMLDeobfuscatingRemapper remapper;
	private Map<String, String> c = new HashMap<String, String>();
	
	{
		c.put("Block", "aqz");
		c.put("BlockFire", "aoi");
		c.put("ItemRenderer", "bfj");
		c.put("Entity", "nn");
		c.put("EntityLivingBase", "of");
		c.put("ItemGlassBottle", "wo");
		c.put("BlockFluid", "apc");
		c.put("BlockFlowing", "apd");
		c.put("EntityRenderer", "bfe");
		c.put("WorldProvider", "aei");
		c.put("World", "abw");
		c.put("Material", "akc");
		c.put("EntityPlayer", "uf");
		c.put("MovingObjectPosition", "ata");
		c.put("ItemStack", "ye");
		c.put("Item", "yc");
		c.put("InventoryPlayer", "ud");
		c.put("ItemPotion", "yp");
		c.put("EntityItem", "ss");
		c.put("EnumMovingObjectType", "atb");
		c.put("MathHelper", "ls");
		c.put("DamageSource", "nb");
		c.put("Minecraft", "atv");
		c.put("ResourceLocation", "bjo");
		c.put("EntityClientPlayerMP", "bdi");
		c.put("TextureManager", "bim");
		c.put("Tessellator", "bfq");
		c.put("WorldClient", "bdd");
		c.put("GuiIngame", "avj");
	}
	
	@Override
	public byte[] transform(String className, String arg1, byte[] data)
	{
		remapper = FMLDeobfuscatingRemapper.INSTANCE;
		
		byte[] newData = data;

		if(className.equals(c.get("Block")))
		{
			newData = patchClassBlock(data, true);
		}
		else if(className.equals("net.minecraft.block.Block"))
		{
			newData = patchClassBlock(data, false);
		}
		else if(className.equals(c.get("BlockFire")))
		{
			newData = patchClassBlockFire(data, true);
		}
		else if(className.equals("net.minecraft.block.BlockFire"))
		{
			newData = patchClassBlockFire(data, false);
		}
		else if(className.equals(c.get("BlockFluid")))
		{
			newData = patchClassBlockFluid(data, true);
		}
		else if(className.equals("net.minecraft.block.BlockFluid"))
		{
			newData = patchClassBlockFluid(data, false);
		}
		else if(className.equals(c.get("BlockFlowing")))
		{
			newData = patchClassBlockFlowing(data, true);
		}
		else if(className.equals("net.minecraft.block.BlockFlowing"))
		{
			newData = patchClassBlockFlowing(data, false);
		}

		if(newData != data)
		{
			System.out.println("[GasesCore]Patched class: " + className);
		}
		
		return newData;
	}
	
	public byte[] dummyTransformFunc(byte[] data, boolean obfuscated)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);
		
		for(int i = 0; i < classNode.methods.size(); i++)
		{
		    MethodNode method = classNode.methods.get(i);
		    String methodName = method.name;
		    if(method.name.equals(obfuscated ? "h" : "updateLightmap") && method.desc.equals("(F)V"))
		    {
		    	InsnList newInstructions = new InsnList();
		    	for(int j = 0; j < method.instructions.size(); j++)
		    	{
		    		AbstractInsnNode instruction = method.instructions.get(j);
		    		newInstructions.add(instruction);
		    		if(instruction.getOpcode() == GETFIELD)
					{
		    			FieldInsnNode fieldInstruction = (FieldInsnNode)instruction;
		    			if(fieldInstruction.name.equals(obfuscated ? "ak" : "gammaSetting") & fieldInstruction.owner.equals(obfuscated ? "aui" : "net/minecraft/client/settings/GameSettings"))
		    			{
		    				newInstructions.add(new InsnNode(POP));
		    				newInstructions.add(new LdcInsnNode(-1.0F));
		    			}
					}
		    	}
		    	method.instructions = newInstructions;
		    }
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
	public byte[] patchClassBlockFlowing(byte[] data, boolean obfuscated)
	{
		String classWorld = obfuscated ? c.get("World") : "net/minecraft/world/World";
		String classBlock = obfuscated ? c.get("Block") : "net/minecraft/block/Block";
		
		String methodUpdateTick = obfuscated ? "a" : "updateTick";
		String methodSetBlock = obfuscated ? "c" : "setBlock";
		
		String fieldStone = obfuscated ? "y" : "stone";
		String fieldBlockID = obfuscated ? "cF" : "blockID";

		String descriptor = "(L" + classWorld + ";IIILjava/util/Random;)V";
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);
		
		for(int i = 0; i < classNode.methods.size(); i++)
		{
			MethodNode method = classNode.methods.get(i);
			if(method.name.equals(methodUpdateTick) & method.desc.equals(descriptor))
			{
				InsnList newInstructions = new InsnList();
				for(int j = 0; j < method.instructions.size(); j++)
				{
					AbstractInsnNode instruction = method.instructions.get(j);
					newInstructions.add(instruction);
					
					if(instruction.getOpcode() == INVOKEVIRTUAL)
					{
						MethodInsnNode methodInsnNode = (MethodInsnNode)instruction;
						if(methodInsnNode.name.equals(methodSetBlock))
						{
							AbstractInsnNode dependentInstruction = method.instructions.get(j - 2);
							if(dependentInstruction.getOpcode() == GETSTATIC)
							{
								FieldInsnNode fieldInsnNode = (FieldInsnNode)dependentInstruction;
								if(fieldInsnNode.name.equals(fieldStone))
								{
									newInstructions.add(new InsnNode(POP));
									newInstructions.add(new VarInsnNode(ALOAD, 1));
									newInstructions.add(new VarInsnNode(ILOAD, 2));
									newInstructions.add(new VarInsnNode(ILOAD, 3));
									newInstructions.add(new VarInsnNode(ILOAD, 4));
									newInstructions.add(new FieldInsnNode(GETSTATIC, "glenn/gases/Gases", "gasSteam", "Lglenn/gasesframework/BlockGas;"));
									newInstructions.add(new FieldInsnNode(GETFIELD, classBlock, fieldBlockID, "I"));
									newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, classWorld, methodSetBlock, "(IIII)Z"));
								}
							}
						}
					}
				}
				
				method.instructions = newInstructions;
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
	public byte[] patchClassBlockFluid(byte[] data, boolean obfuscated)
	{
		String classWorld = obfuscated ? c.get("World") : "net/minecraft/world/World";
		String classBlock = obfuscated ? c.get("Block") : "net/minecraft/block/Block";
		
		String methodCheckForHarden = obfuscated ? "k" : "checkForHarden";
		String methodSetBlock = obfuscated ? "c" : "setBlock";
		
		String fieldCobblestone = obfuscated ? "B" : "cobblestone";
		String fieldBlockID = obfuscated ? "cF" : "blockID";
		
		String descriptor = "(L" + classWorld + ";III)V";
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);
		
		for(int i = 0; i < classNode.methods.size(); i++)
		{
			MethodNode method = classNode.methods.get(i);
			if(method.name.equals(methodCheckForHarden) & method.desc.equals(descriptor))
			{
				InsnList newInstructions = new InsnList();
				for(int j = 0; j < method.instructions.size(); j++)
				{
					AbstractInsnNode instruction = method.instructions.get(j);
					newInstructions.add(instruction);
					
					if(instruction.getOpcode() == POP)
					{
						AbstractInsnNode dependentInstruction = method.instructions.get(j - 1);
						if(dependentInstruction.getOpcode() == INVOKEVIRTUAL)
						{
							MethodInsnNode methodInsnNode = (MethodInsnNode)dependentInstruction;
							if(methodInsnNode.name.equals(methodSetBlock) & methodInsnNode.desc.equals("(IIII)Z"))
							{
								newInstructions.add(new VarInsnNode(ALOAD, 1));
								newInstructions.add(new VarInsnNode(ILOAD, 2));
								newInstructions.add(new VarInsnNode(ILOAD, 3));
								newInstructions.add(new InsnNode(ICONST_1));
								newInstructions.add(new InsnNode(IADD));
								newInstructions.add(new VarInsnNode(ILOAD, 4));
								newInstructions.add(new FieldInsnNode(GETSTATIC, "glenn/gases/Gases", "gasSteam", "Lglenn/gasesframework/BlockGas;"));
								newInstructions.add(new FieldInsnNode(GETFIELD, classBlock, fieldBlockID, "I"));
								newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, classWorld, methodSetBlock, "(IIII)Z"));
								newInstructions.add(new InsnNode(POP));
							}
						}
					}
				}
				
				method.instructions = newInstructions;
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	public byte[] patchClassBlockFire(byte[] data, boolean obfuscated)
	{
		String classBlock = obfuscated ? c.get("Block") : "net/minecraft/block/Block";
		String classWorld = obfuscated ? c.get("World") : "net/minecraft/world/World";

		String methodTryToCatchBlockOnFire = "tryToCatchBlockOnFire";
		String methodSetBlockToAir = obfuscated ? "i" : "setBlockToAir";
		String methodSetBlock = obfuscated ? "f" : "setBlock";

		String fieldBlockID = obfuscated ? "cF" : "blockID";
		
		String descriptor = "(L" + classWorld + ";IIIILjava/util/Random;I)V";
		String descriptor2 = "(L" + classWorld + ";IIIILjava/util/Random;ILnet/minecraftforge/common/ForgeDirection;)V";
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);

		for(int i = 0; i < classNode.methods.size(); i++)
		{
			MethodNode method = (MethodNode)classNode.methods.get(i);
			if(method.name.equals(methodTryToCatchBlockOnFire) & method.desc.equals(descriptor2))
			{
				InsnList newInstructions = new InsnList();
				for(int j = 0; j < method.instructions.size(); j++)
				{
					AbstractInsnNode instruction = method.instructions.get(j);
					if(instruction.getOpcode() == INVOKEVIRTUAL)
					{
						MethodInsnNode invokeInstruction = (MethodInsnNode)instruction;
						if(invokeInstruction.name.equals(methodSetBlockToAir))
						{
							invokeInstruction.name = methodSetBlock;
							invokeInstruction.desc = "(IIIIII)Z";

							newInstructions.add(new FieldInsnNode(GETSTATIC, "glenn/gasesframework/GasesFramework", "gasSmoke", "Lglenn/gasesframework/BlockGas;"));
							newInstructions.add(new FieldInsnNode(GETFIELD, classBlock, fieldBlockID, "I"));
							newInstructions.add(new IntInsnNode(BIPUSH, 7));
							newInstructions.add(new InsnNode(ICONST_3));
						}
					}
					newInstructions.add(instruction);
				}
				method.instructions = newInstructions;
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	public byte[] patchClassBlock(byte[] data, boolean obfuscated)
	{
		String fieldBedrock = obfuscated ? "E" : "bedrock";

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);

		Iterator<MethodNode> methods = classNode.methods.iterator();

		while(methods.hasNext())
		{
			MethodNode method = methods.next();
			if(method.name.equals("<clinit>"))
			{
				Iterator<AbstractInsnNode> instructions = method.instructions.iterator();
				int instructionIndex = 0;
				while(instructions.hasNext())
				{
					instructionIndex++;
					AbstractInsnNode instruction = instructions.next();
					if(instruction.getOpcode() == PUTSTATIC)
					{
						FieldInsnNode fieldInstruction = (FieldInsnNode)instruction;
						if(fieldInstruction.name.equals(fieldBedrock))
						{
							for(int i = instructionIndex - 1; i > 0; i--)
							{
								AbstractInsnNode newInstruction = method.instructions.get(i);
								if(newInstruction.getOpcode() == NEW)
								{
									TypeInsnNode newTypeInstruction = (TypeInsnNode)newInstruction;
									newTypeInstruction.desc = "glenn/gases/BlockBedrock";
									break;
								}
								else if(newInstruction.getOpcode() == INVOKESPECIAL)
								{
									MethodInsnNode methodInstruction = (MethodInsnNode)newInstruction;
									methodInstruction.owner = "glenn/gases/BlockBedrock";
								}
							}
						}
					}
				}
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
}
