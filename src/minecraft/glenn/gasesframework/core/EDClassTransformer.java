package glenn.gasesframework.core;

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
import org.objectweb.asm.Opcodes;
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
		c.put("IBlockAccess", "acf");
	}
	
	@Override
	public byte[] transform(String className, String arg1, byte[] data)
	{
		byte[] newData = data;

		if(className.equals("net.minecraftforge.client.GuiIngameForge"))
		{
			//newData = patchClassGuiIngame(className, data);
		}
		else if(className.equals(c.get("ItemRenderer")))
		{
			System.out.println("[GasesFrameworkCore]Patching class: " + className + "(ItemRenderer)...");
			newData = patchClassItemRenderer(data, true);
		}
		else if(className.equals("net.minecraft.client.renderer.ItemRenderer"))
		{
			System.out.println("[GasesFrameworkCore]Patching class: " + className + "(ItemRenderer)...");
			newData = patchClassItemRenderer(data, false);
		}
		else if(className.equals(c.get("Entity")))
		{
			System.out.println("[GasesFrameworkCore]Patching class: " + className + "(Entity)...");
			newData = patchClassEntity(data, true);
		}
		else if(className.equals("net.minecraft.entity.Entity"))
		{
			System.out.println("[GasesFrameworkCore]Patching class: " + className + "(Entity)...");
			newData = patchClassEntity(data, false);
		}
		else if(className.equals(c.get("EntityLivingBase")))
		{
			System.out.println("[GasesFrameworkCore]Patching class: " + className + "(EntityLivingBase)...");
			newData = patchClassEntityLivingBase(data, true);
		}
		else if(className.equals("net.minecraft.entity.EntityLivingBase"))
		{
			System.out.println("[GasesFrameworkCore]Patching class: " + className + "(EntityLivingBase)...");
			newData = patchClassEntityLivingBase(data, false);
		}
		else if(className.equals(c.get("EntityRenderer")))
		{
			System.out.println("[GasesFrameworkCore]Patching class: " + className + "(EntityRenderer)...");
			newData = patchClassEntityRenderer(data, true);
		}
		else if(className.equals("net.minecraft.client.renderer.EntityRenderer"))
		{
			System.out.println("[GasesFrameworkCore]Patching class: " + className + "(EntityRenderer)...");
			newData = patchClassEntityRenderer(data, false);
		}

		if(newData != data)
		{
			System.out.println("[GasesFrameworkCore]Patch OK!");
		}
		
		return newData;
	}
	
	public byte[] patchClassEntityRenderer(byte[] data, boolean obfuscated)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);
		
		String classEntityRenderer = obfuscated ? c.get("EntityRenderer") : "net/minecraft/client/renderer/EntityRenderer";
		String classEntityLivingBase = obfuscated ? c.get("EntityLivingBase") : "net/minecraft/entity/EntityLivingBase";
		String classWorldProvider = obfuscated ? c.get("WorldProvider") : "net/minecraft/world/WorldProvider";
		
		String methodSetupFog = obfuscated ? "a" : "setupFog";
		String methodUpdateFogColor = obfuscated ? "i" : "updateFogColor";
		String methodGetVoidFogYFactor = obfuscated ? "k" : "getVoidFogYFactor";
		
		String fieldFarPlaneDistance = obfuscated ? "r" : "farPlaneDistance";
		
		for(int i = 0; i < classNode.methods.size(); i++)
		{
			MethodNode method = classNode.methods.get(i);
			
			if(method.name.equals(methodSetupFog) && method.desc.equals("(IF)V"))
			{
				InsnList newInstructions = new InsnList();
				for(int j = 0; j < method.instructions.size(); j++)
				{
					AbstractInsnNode instruction = method.instructions.get(j);
					newInstructions.add(instruction);
					
					if(instruction.getOpcode() == GETFIELD)
					{
						FieldInsnNode fieldInstruction = (FieldInsnNode)instruction;
						if(fieldInstruction.name.equals(fieldFarPlaneDistance) && fieldInstruction.owner.equals(classEntityRenderer) && fieldInstruction.desc.equals("F"))
						{
							newInstructions.add(new InsnNode(FCONST_1));
							newInstructions.add(new VarInsnNode(ALOAD, 3));
							newInstructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "blindnessTimer", "I"));
							newInstructions.add(new InsnNode(I2F));
							newInstructions.add(new LdcInsnNode(5.0F));
							newInstructions.add(new InsnNode(FDIV));
							newInstructions.add(new InsnNode(FADD));
							newInstructions.add(new InsnNode(FDIV));
						}
					}
				}
				method.instructions = newInstructions;
			}
			else if(method.name.equals(methodUpdateFogColor) && method.desc.equals("(F)V"))
			{
				InsnList newInstructions = new InsnList();
				for(int j = 0; j < method.instructions.size(); j++)
				{
					AbstractInsnNode instruction = method.instructions.get(j);
					newInstructions.add(instruction);
					
					if(instruction.getOpcode() == INVOKEVIRTUAL)
					{
						MethodInsnNode methodInstruction = (MethodInsnNode)instruction;
						if(methodInstruction.name.equals(methodGetVoidFogYFactor) && methodInstruction.owner.equals(classWorldProvider) && methodInstruction.desc.equals("()D"))
						{
							newInstructions.add(new InsnNode(FCONST_1));
							newInstructions.add(new VarInsnNode(ALOAD, 3));
							newInstructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "blindnessTimer", "I"));
							newInstructions.add(new InsnNode(I2F));
							newInstructions.add(new LdcInsnNode(30.0F));
							newInstructions.add(new InsnNode(FDIV));
							newInstructions.add(new InsnNode(FADD));
							newInstructions.add(new InsnNode(F2D));
							newInstructions.add(new InsnNode(DDIV));
						}
					}
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
	public byte[] patchClassEntityLivingBase(byte[] data, boolean obfuscated)
	{
		String classEntityLivingBase = obfuscated ? c.get("EntityLivingBase") : "net/minecraft/entity/EntityLivingBase";
		String classMaterial = obfuscated ? c.get("Material") : "net/minecraft/block/material/Material";
		String classMathHelper = obfuscated ? c.get("MathHelper") : "net/minecraft/util/MathHelper";
		String classBlock = obfuscated ? c.get("Block") : "net/minecraft/block/Block";
		String classWorld = obfuscated ? c.get("World") : "net/minecraft/world/World";
		String classDamageSource = obfuscated ? c.get("DamageSource") : "net/minecraft/util/DamageSource";

		String methodOnEntityUpdate = obfuscated ? "y" : "onEntityUpdate";
		String methodIsInsideOfMaterial = obfuscated ? "a" : "isInsideOfMaterial";
		String methodGetEyeHeight = obfuscated ? "f" : "getEyeHeight";
		String methodFloor_double = obfuscated ? "c" : "floor_double";
		String methodFloor_float = obfuscated ? "d" : "floor_float";
		String methodGetBlockId = obfuscated ? "a" : "getBlockId";
		String methodAttackEntityFrom = obfuscated ? "a" : "attackEntityFrom";
		String methodUpdatePotionEffects = obfuscated ? "aJ" : "updatePotionEffects";
		
		String fieldPosX = obfuscated ? "u" : "posX";
		String fieldPosY = obfuscated ? "v" : "posY";
		String fieldPosZ = obfuscated ? "w" : "posZ";
		String fieldBlocksList = obfuscated ? "s" : "blocksList";
		String fieldWorldObj = obfuscated ? "q" : "worldObj";
		String fieldGeneric = obfuscated ? "j" : "generic";
		
		String descriptor = "(L" + classMaterial + ";)Z";

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);
		
		classNode.fields.add(new FieldNode(ACC_PUBLIC, "blindnessTimer", "I", null, null));
		classNode.fields.add(new FieldNode(ACC_PUBLIC, "suffocationTimer", "I", null, null));
		classNode.fields.add(new FieldNode(ACC_PUBLIC, "slownessTimer", "I", null, null));

		for(int i = 0; i < classNode.methods.size(); i++)
		{
			MethodNode method = (MethodNode)classNode.methods.get(i);
			if(method.name.equals(methodOnEntityUpdate) & method.desc.equals("()V"))
			{
				/*InsnList newInstructions = new InsnList();
				for(int j = 0; j < method.instructions.size(); j++)
				{
					AbstractInsnNode instruction = method.instructions.get(j);
					newInstructions.add(instruction);
					if(instruction.getOpcode() == INVOKEVIRTUAL)
					{
						MethodInsnNode invokeInstruction = (MethodInsnNode)instruction;
						if(invokeInstruction.name.equals(methodIsInsideOfMaterial) & invokeInstruction.desc.equals(descriptor))
						{
							newInstructions.add(new VarInsnNode(ALOAD, 0));
							newInstructions.add(new FieldInsnNode(GETSTATIC, "glenn/gasesframework/GasesFramework", "gasMaterial", "L" + classMaterial + ";"));
							newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, classEntityLivingBase, methodIsInsideOfMaterial, "(L" + classMaterial + ";)Z"));
							newInstructions.add(new InsnNode(IOR));
						}
					}
				}
				method.instructions = newInstructions;*/
				
				InsnList newInstructions = new InsnList();
				for(int j = 0; j < method.instructions.size(); j++)
				{
					AbstractInsnNode instruction = method.instructions.get(j);
					newInstructions.add(instruction);
					
					if(instruction.getOpcode() == INVOKEVIRTUAL)
					{
						MethodInsnNode methodInstruction = (MethodInsnNode)instruction;
						if(methodInstruction.name.equals(methodUpdatePotionEffects) && methodInstruction.desc.equals("()V"))
						{
							newInstructions.add(new VarInsnNode(ALOAD, 0));
							newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, classEntityLivingBase, "handleGasEffects", "()V"));
						}
					}
				}
				method.instructions = newInstructions;
			}
			else if(method.name.equals("<init>"))
			{
				method.instructions.remove(method.instructions.getLast());
				method.visitVarInsn(ALOAD, 0);
				method.visitInsn(ICONST_0);
				method.visitFieldInsn(PUTFIELD, classEntityLivingBase, "blindnessTimer", "I");
				method.visitVarInsn(ALOAD, 0);
				method.visitInsn(ICONST_0);
				method.visitFieldInsn(PUTFIELD, classEntityLivingBase, "suffocationTimer", "I");
				method.visitVarInsn(ALOAD, 0);
				method.visitInsn(ICONST_0);
				method.visitFieldInsn(PUTFIELD, classEntityLivingBase, "slownessTimer", "I");
				method.visitInsn(RETURN);
			}
		}
		
		{
			classNode.visitMethod(ACC_PROTECTED, "handleGasEffects", "()V", null, null);
			MethodNode method = classNode.methods.get(classNode.methods.size() - 1);
			
			LabelNode l0 = new LabelNode();
			method.instructions.add(l0);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETSTATIC, "glenn/gasesframework/GasesFramework", "gasMaterial", "L" + classMaterial + ";"));
			method.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, classEntityLivingBase, methodIsInsideOfMaterial, "(L" + classMaterial + ";)Z"));
			method.instructions.add(new VarInsnNode(ISTORE, 1));
			LabelNode l1 = new LabelNode();
			method.instructions.add(l1);
			method.instructions.add(new InsnNode(ACONST_NULL));
			method.instructions.add(new VarInsnNode(ASTORE, 2));
			LabelNode l2 = new LabelNode();
			method.instructions.add(l2);
			method.instructions.add(new VarInsnNode(ILOAD, 1));
			LabelNode l3 = new LabelNode();
			method.instructions.add(new JumpInsnNode(IFEQ, l3));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, fieldPosY, "D"));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, classEntityLivingBase, methodGetEyeHeight, "()F"));
			method.instructions.add(new InsnNode(F2D));
			method.instructions.add(new InsnNode(DADD));
			method.instructions.add(new VarInsnNode(DSTORE, 3));
			LabelNode l5 = new LabelNode();
			method.instructions.add(l5);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, fieldPosX, "D"));
			method.instructions.add(new MethodInsnNode(INVOKESTATIC, classMathHelper, methodFloor_double, "(D)I"));
			method.instructions.add(new VarInsnNode(ISTORE, 5));
			LabelNode l6 = new LabelNode();
			method.instructions.add(l6);
			method.instructions.add(new VarInsnNode(DLOAD, 3));
			method.instructions.add(new MethodInsnNode(INVOKESTATIC, classMathHelper, methodFloor_double, "(D)I"));
			method.instructions.add(new InsnNode(I2F));
			method.instructions.add(new MethodInsnNode(INVOKESTATIC, classMathHelper, methodFloor_float, "(F)I"));
			method.instructions.add(new VarInsnNode(ISTORE, 6));
			LabelNode l7 = new LabelNode();
			method.instructions.add(l7);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, fieldPosZ, "D"));
			method.instructions.add(new MethodInsnNode(INVOKESTATIC, classMathHelper, methodFloor_double, "(D)I"));
			method.instructions.add(new VarInsnNode(ISTORE, 7));
			LabelNode l8 = new LabelNode();
			method.instructions.add(l8);
			method.instructions.add(new FieldInsnNode(GETSTATIC, classBlock, fieldBlocksList, "[L" + classBlock + ";"));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, fieldWorldObj, "L" + classWorld + ";"));
			method.instructions.add(new VarInsnNode(ILOAD, 5));
			method.instructions.add(new VarInsnNode(ILOAD, 6));
			method.instructions.add(new VarInsnNode(ILOAD, 7));
			method.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, classWorld, methodGetBlockId, "(III)I"));
			method.instructions.add(new InsnNode(AALOAD));
			method.instructions.add(new TypeInsnNode(CHECKCAST, "glenn/gasesframework/BlockGas"));
			method.instructions.add(new VarInsnNode(ASTORE, 2));
			method.instructions.add(l3);
			method.instructions.add(new VarInsnNode(ILOAD, 1));
			LabelNode l9 = new LabelNode();
			method.instructions.add(new JumpInsnNode(IFEQ, l9));
			method.instructions.add(new VarInsnNode(ALOAD, 2));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/BlockGas", "type", "Lglenn/gasesframework/GasType;"));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/GasType", "blindnessRate", "I"));
			method.instructions.add(new JumpInsnNode(IFLT, l9));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new InsnNode(DUP));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "blindnessTimer", "I"));
			method.instructions.add(new VarInsnNode(ALOAD, 2));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/BlockGas", "type", "Lglenn/gasesframework/GasType;"));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/GasType", "blindnessRate", "I"));
			method.instructions.add(new InsnNode(IADD));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "blindnessTimer", "I"));
			LabelNode l12 = new LabelNode();
			method.instructions.add(new JumpInsnNode(GOTO, l12));
			method.instructions.add(l9);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new InsnNode(DUP));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "blindnessTimer", "I"));
			method.instructions.add(new InsnNode(ICONST_2));
			method.instructions.add(new InsnNode(ISUB));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "blindnessTimer", "I"));
			method.instructions.add(l12);
			method.instructions.add(new VarInsnNode(ILOAD, 1));
			LabelNode l13 = new LabelNode();
			method.instructions.add(new JumpInsnNode(IFEQ, l13));
			method.instructions.add(new VarInsnNode(ALOAD, 2));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/BlockGas", "type", "Lglenn/gasesframework/GasType;"));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/GasType", "suffocationRate", "I"));
			method.instructions.add(new JumpInsnNode(IFLT, l13));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new InsnNode(DUP));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "suffocationTimer", "I"));
			method.instructions.add(new VarInsnNode(ALOAD, 2));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/BlockGas", "type", "Lglenn/gasesframework/GasType;"));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/GasType", "suffocationRate", "I"));
			method.instructions.add(new InsnNode(IADD));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "suffocationTimer", "I"));
			LabelNode l16 = new LabelNode();
			method.instructions.add(new JumpInsnNode(GOTO, l16));
			method.instructions.add(l13);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new InsnNode(DUP));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "suffocationTimer", "I"));
			method.instructions.add(new IntInsnNode(BIPUSH, 16));
			method.instructions.add(new InsnNode(ISUB));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "suffocationTimer", "I"));
			method.instructions.add(l16);
			method.instructions.add(new VarInsnNode(ILOAD, 1));
			LabelNode l17 = new LabelNode();
			method.instructions.add(new JumpInsnNode(IFEQ, l17));
			method.instructions.add(new VarInsnNode(ALOAD, 2));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/BlockGas", "type", "Lglenn/gasesframework/GasType;"));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/GasType", "slownessRate", "I"));
			method.instructions.add(new JumpInsnNode(IFLT, l17));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new InsnNode(DUP));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "slownessTimer", "I"));
			method.instructions.add(new VarInsnNode(ALOAD, 2));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/BlockGas", "type", "Lglenn/gasesframework/GasType;"));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/GasType", "slownessRate", "I"));
			method.instructions.add(new InsnNode(IADD));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "slownessTimer", "I"));
			LabelNode l20 = new LabelNode();
			method.instructions.add(new JumpInsnNode(GOTO, l20));
			method.instructions.add(l17);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new InsnNode(DUP));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "slownessTimer", "I"));
			method.instructions.add(new InsnNode(ICONST_2));
			method.instructions.add(new InsnNode(ISUB));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "slownessTimer", "I"));
			method.instructions.add(l20);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "blindnessTimer", "I"));
			LabelNode l21 = new LabelNode();
			method.instructions.add(new JumpInsnNode(IFGE, l21));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new InsnNode(ICONST_0));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "blindnessTimer", "I"));
			LabelNode l24 = new LabelNode();
			method.instructions.add(new JumpInsnNode(GOTO, l24));
			method.instructions.add(l21);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "blindnessTimer", "I"));
			method.instructions.add(new IntInsnNode(SIPUSH, 500));
			method.instructions.add(new JumpInsnNode(IF_ICMPLE, l24));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new IntInsnNode(SIPUSH, 500));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "blindnessTimer", "I"));
			method.instructions.add(l24);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "suffocationTimer", "I"));
			LabelNode l26 = new LabelNode();
			method.instructions.add(new JumpInsnNode(IFGE, l26));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new InsnNode(ICONST_0));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "suffocationTimer", "I"));
			LabelNode l29 = new LabelNode();
			method.instructions.add(new JumpInsnNode(GOTO, l29));
			method.instructions.add(l26);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "suffocationTimer", "I"));
			method.instructions.add(new IntInsnNode(SIPUSH, 400));
			method.instructions.add(new JumpInsnNode(IF_ICMPLE, l29));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new InsnNode(DUP));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "suffocationTimer", "I"));
			method.instructions.add(new IntInsnNode(BIPUSH, 50));
			method.instructions.add(new InsnNode(ISUB));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "suffocationTimer", "I"));
			
			/*method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETSTATIC, classDamageSource, fieldGeneric, "L" + classDamageSource + ";"));
			method.instructions.add(new LdcInsnNode(0.5F));
			method.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, classEntityLivingBase, methodAttackEntityFrom, "(L" + classDamageSource + ";F)Z"));
			method.instructions.add(new InsnNode(POP));*/
			method.instructions.add(new VarInsnNode(ALOAD, 2));
			method.instructions.add(new FieldInsnNode(GETFIELD, "glenn/gasesframework/BlockGas", "type", "Lglenn/gasesframework/GasType;"));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "glenn/gasesframework/GasType", "onBreathed", "(L" + classEntityLivingBase + ";)V"));
			
			method.instructions.add(l29);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "slownessTimer", "I"));
			LabelNode l32 = new LabelNode();
			method.instructions.add(new JumpInsnNode(IFGE, l32));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new InsnNode(ICONST_0));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "slownessTimer", "I"));
			LabelNode l35 = new LabelNode();
			method.instructions.add(new JumpInsnNode(GOTO, l35));
			method.instructions.add(l32);
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new FieldInsnNode(GETFIELD, classEntityLivingBase, "slownessTimer", "I"));
			method.instructions.add(new IntInsnNode(SIPUSH, 1000));
			method.instructions.add(new JumpInsnNode(IF_ICMPLE, l35));
			method.instructions.add(new VarInsnNode(ALOAD, 0));
			method.instructions.add(new IntInsnNode(SIPUSH, 1000));
			method.instructions.add(new FieldInsnNode(PUTFIELD, classEntityLivingBase, "slownessTimer", "I"));
			method.instructions.add(l35);
			method.instructions.add(new InsnNode(RETURN));
			LabelNode l37 = new LabelNode();
			method.instructions.add(l37);
			
			method.localVariables.add(new LocalVariableNode("this", "L" + classEntityLivingBase + ";", null, l0, l37, 0));
			method.localVariables.add(new LocalVariableNode("isInsideOfGas", "Z", null, l1, l37, 1));
			method.localVariables.add(new LocalVariableNode("block", "Lglenn/gasesframework/BlockGas;", null, l2, l37, 2));
			method.localVariables.add(new LocalVariableNode("d0", "D", null, l5, l3, 3));
			method.localVariables.add(new LocalVariableNode("i", "I", null, l6, l3, 5));
			method.localVariables.add(new LocalVariableNode("j", "I", null, l7, l3, 6));
			method.localVariables.add(new LocalVariableNode("k", "I", null, l8, l3, 7));
			
			method.maxStack = 5;
			method.maxLocals = 8;
		}
		
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	public byte[] patchClassEntity(byte[] data, boolean obfuscated)
	{
		String classMinecraft = obfuscated ? c.get("Minecraft") : "net/minecraft/client/Minecraft";
		String classBlock = obfuscated ? c.get("Block") : "net/minecraft/block/Block";
		String classEntity = obfuscated ? c.get("Entity") : "net/minecraft/entity/Entity";
		String classMaterial = obfuscated ? c.get("Material") : "net/minecraft/block/material/Material";
		String classWorld = obfuscated ? c.get("World") : "net/minecraft/world/World";
		
		String interfaceBlockAccess = obfuscated ? c.get("IBlockAccess") : "net/minecraft/world/IBlockAccess";

		String methodIsInsideOfMaterial = obfuscated ? "a" : "isInsideOfMaterial";
		String methodGetBlockMetadata = obfuscated ? "h" : "getBlockMetadata";

		String fieldBlocksList = obfuscated ? "s" : "blocksList";
		String fieldBlockMaterial = obfuscated ? "cU" : "blockMaterial";
		String fieldWorldObj = obfuscated ? "q" : "worldObj";
		
		String descriptor = "(L" + classMaterial + ";)Z";

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);

		for(int i = 0; i < classNode.methods.size(); i++)
		{
			MethodNode method = (MethodNode)classNode.methods.get(i);
			if(method.name.equals(methodIsInsideOfMaterial) && method.desc.equals(descriptor))
			{
				InsnList newInstructions = new InsnList();
				for(int j = 0; j < method.instructions.size(); j++)
				{
					AbstractInsnNode instruction = (AbstractInsnNode)method.instructions.get(j);
					newInstructions.add(instruction);

					if(instruction.getOpcode() == ISTORE && ((VarInsnNode)instruction).var == 7)
					{
						newInstructions.add(new VarInsnNode(ILOAD, 7));
						LabelNode l6 = new LabelNode();
						newInstructions.add(new JumpInsnNode(IFEQ, l6));
						newInstructions.add(new VarInsnNode(ALOAD, 1));
						newInstructions.add(new FieldInsnNode(GETSTATIC, "glenn/gasesframework/GasesFramework", "gasMaterial", "L" + classMaterial + ";"));
						newInstructions.add(new JumpInsnNode(IF_ACMPNE, l6));
						newInstructions.add(new FieldInsnNode(GETSTATIC, classBlock, fieldBlocksList, "[L" + classBlock + ";"));
						newInstructions.add(new VarInsnNode(ILOAD, 7));
						newInstructions.add(new InsnNode(AALOAD));
						newInstructions.add(new FieldInsnNode(GETFIELD, classBlock, fieldBlockMaterial, "L" + classMaterial + ";"));
						newInstructions.add(new FieldInsnNode(GETSTATIC, "glenn/gasesframework/GasesFramework", "gasMaterial", "L" + classMaterial + ";"));
						newInstructions.add(new JumpInsnNode(IF_ACMPNE, l6));
						newInstructions.add(new VarInsnNode(DLOAD, 2));
						newInstructions.add(new VarInsnNode(ILOAD, 5));
						newInstructions.add(new InsnNode(I2D));
						newInstructions.add(new InsnNode(DSUB));
						newInstructions.add(new FieldInsnNode(GETSTATIC, classBlock, fieldBlocksList, "[L" + classBlock + ";"));
						newInstructions.add(new VarInsnNode(ILOAD, 7));
						newInstructions.add(new InsnNode(AALOAD));
						newInstructions.add(new TypeInsnNode(CHECKCAST, "glenn/gasesframework/BlockGas"));
						newInstructions.add(new VarInsnNode(ALOAD, 0));
						newInstructions.add(new FieldInsnNode(GETFIELD, classEntity, fieldWorldObj, "L" + classWorld + ";"));
						newInstructions.add(new TypeInsnNode(CHECKCAST, interfaceBlockAccess));
						newInstructions.add(new VarInsnNode(ILOAD, 4));
						newInstructions.add(new VarInsnNode(ILOAD, 5));
						newInstructions.add(new VarInsnNode(ILOAD, 6));
						newInstructions.add(new VarInsnNode(ALOAD, 0));
						newInstructions.add(new FieldInsnNode(GETFIELD, classEntity, fieldWorldObj, "L" + classWorld + ";"));
						newInstructions.add(new VarInsnNode(ILOAD, 4));
						newInstructions.add(new VarInsnNode(ILOAD, 5));
						newInstructions.add(new VarInsnNode(ILOAD, 6));
						newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, classWorld, methodGetBlockMetadata, "(III)I"));
						newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, "glenn/gasesframework/BlockGas", "getMinY", "(L" + interfaceBlockAccess + ";IIII)D"));
						newInstructions.add(new InsnNode(DCMPL));
						LabelNode l8 = new LabelNode();
						newInstructions.add(new JumpInsnNode(IFLE, l8));
						newInstructions.add(new VarInsnNode(DLOAD, 2));
						newInstructions.add(new VarInsnNode(ILOAD, 5));
						newInstructions.add(new InsnNode(I2D));
						newInstructions.add(new InsnNode(DSUB));
						newInstructions.add(new FieldInsnNode(GETSTATIC, classBlock, fieldBlocksList, "[L" + classBlock + ";"));
						newInstructions.add(new VarInsnNode(ILOAD, 7));
						newInstructions.add(new InsnNode(AALOAD));
						newInstructions.add(new TypeInsnNode(CHECKCAST, "glenn/gasesframework/BlockGas"));
						newInstructions.add(new VarInsnNode(ALOAD, 0));
						newInstructions.add(new FieldInsnNode(GETFIELD, classEntity, fieldWorldObj, "L" + classWorld + ";"));
						newInstructions.add(new TypeInsnNode(CHECKCAST, interfaceBlockAccess));
						newInstructions.add(new VarInsnNode(ILOAD, 4));
						newInstructions.add(new VarInsnNode(ILOAD, 5));
						newInstructions.add(new VarInsnNode(ILOAD, 6));
						newInstructions.add(new VarInsnNode(ALOAD, 0));
						newInstructions.add(new FieldInsnNode(GETFIELD, classEntity, fieldWorldObj, "L" + classWorld + ";"));
						newInstructions.add(new VarInsnNode(ILOAD, 4));
						newInstructions.add(new VarInsnNode(ILOAD, 5));
						newInstructions.add(new VarInsnNode(ILOAD, 6));
						newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, classWorld, methodGetBlockMetadata, "(III)I"));
						newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, "glenn/gasesframework/BlockGas", "getMaxY", "(L" + interfaceBlockAccess + ";IIII)D"));
						newInstructions.add(new InsnNode(DCMPG));
						newInstructions.add(new JumpInsnNode(IFGE, l8));
						newInstructions.add(new InsnNode(ICONST_1));
						newInstructions.add(new InsnNode(IRETURN));
						newInstructions.add(l8);
						newInstructions.add(new InsnNode(ICONST_0));
						newInstructions.add(new InsnNode(IRETURN));
						newInstructions.add(l6);
					}
				}
				method.instructions = newInstructions;
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	public byte[] patchClassItemRenderer(byte[] data, boolean obfuscated)
	{
		String classResourceLocation = obfuscated ? c.get("ResourceLocation") : "net/minecraft/util/ResourceLocation";
		String classItemRenderer = obfuscated ? c.get("ItemRenderer") : "net/minecraft/client/renderer/ItemRenderer";
		String classMinecraft = obfuscated ? c.get("Minecraft") : "net/minecraft/client/Minecraft";
		String classEntityClientPlayerMP = obfuscated ? c.get("EntityClientPlayerMP") : "net/minecraft/client/entity/EntityClientPlayerMP";
		String classMaterial = obfuscated ? c.get("Material") : "net/minecraft/block/material/Material";
		String classTextureManager = obfuscated ? c.get("TextureManager") : "net/minecraft/client/renderer/texture/TextureManager";
		String classTessellator = obfuscated ? c.get("Tessellator") : "net/minecraft/client/renderer/Tessellator";
		String classMathHelper = obfuscated ? c.get("MathHelper") : "net/minecraft/util/MathHelper";
		String classWorldClient = obfuscated ? c.get("WorldClient") : "net/minecraft/client/multiplayer/WorldClient";
		String classBlock = obfuscated ? c.get("Block") : "net/minecraft/block/Block";

		String methodRenderOverlays = obfuscated ? "b" : "renderOverlays";
		String methodRenderWarpedTextureOverlay = obfuscated ? "c" : "renderWarpedTextureOverlay";
		String methodIsInsideOfMaterial = obfuscated ? "a" : "isInsideOfMaterial";
		String methodFloor_double = obfuscated ? "c" : "floor_double";
		String methodGetBrightness = obfuscated ? "d" : "getBrightness";
		String methodGetBlockId = obfuscated ? "a" : "getBlockId";
		String methodGetEyeHeight = obfuscated ? "f" : "getEyeHeight";
		String methodStartDrawingQuads = obfuscated ? "b" : "startDrawingQuads";
		String methodAddVertexWithUV = obfuscated ? "a" : "addVertexWithUV";
		String methodDraw = obfuscated ? "a" : "draw";
		String methodGetTextureManager = obfuscated ? "J" : "getTextureManager";
		String methodBindTexture = obfuscated ? "a" : "bindTexture";

		String fieldMc = obfuscated ? "e" : "mc";
		String fieldThePlayer = obfuscated ? "h" : "thePlayer";
		String fieldInstance = obfuscated ? "a" : "instance";
		String fieldBlocksList = obfuscated ? "s" : "blocksList";
		String fieldPosX = obfuscated ? "u" : "posX";
		String fieldPosY = obfuscated ? "v" : "posY";
		String fieldPosZ = obfuscated ? "w" : "posZ";
		String fieldTheWorld = obfuscated ? "f" : "theWorld";
		String fieldRotationYaw = obfuscated ? "A" : "rotationYaw";
		String fieldRotationPitch = obfuscated ? "B" : "rotationPitch";
		
		String descriptor = "(L" + classMaterial + ";)Z";

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);

		for(int i = 0; i < classNode.methods.size(); i++)
		{
			MethodNode method = (MethodNode)classNode.methods.get(i);
			if(method.name.equals(methodRenderOverlays) & method.desc.equals("(F)V"))
			{
				InsnList newInstructions = new InsnList();
				LabelNode theLabelNode = null;

				Label label1 = new Label();
				LabelNode labelNode1 = new LabelNode(label1);

				for(int j = 0; j < method.instructions.size(); j++)
				{
					AbstractInsnNode instruction = (AbstractInsnNode)method.instructions.get(j);
					newInstructions.add(instruction);
					if(instruction.getOpcode() == INVOKEVIRTUAL)
					{
						MethodInsnNode invokeInstruction = (MethodInsnNode)instruction;
						
						if(invokeInstruction.name.equals(methodIsInsideOfMaterial) & invokeInstruction.desc.equals(descriptor))
						{
							if(instruction.getNext().getOpcode() == IFEQ)
							{
								theLabelNode = ((JumpInsnNode)instruction.getNext()).label;
							}
						}
						else if(invokeInstruction.name.equals(methodRenderWarpedTextureOverlay) & invokeInstruction.desc.equals("(F)V"))
						{
							newInstructions.add(new JumpInsnNode(GOTO, labelNode1));
						}
					}
					else if(instruction instanceof LabelNode)
					{
						if(theLabelNode != null && theLabelNode == (LabelNode)instruction)
						{
							newInstructions.add(new VarInsnNode(ALOAD, 0));
							newInstructions.add(new FieldInsnNode(GETFIELD, classItemRenderer, fieldMc, "L" + classMinecraft + ";"));
							newInstructions.add(new FieldInsnNode(GETFIELD, classMinecraft, fieldThePlayer, "L" + classEntityClientPlayerMP + ";"));
							newInstructions.add(new FieldInsnNode(GETSTATIC, "glenn/gasesframework/GasesFramework", "gasMaterial", "L" + classMaterial + ";"));
							newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, classEntityClientPlayerMP, methodIsInsideOfMaterial, "(L" + classMaterial + ";)Z"));
							newInstructions.add(new JumpInsnNode(IFEQ, labelNode1));
							newInstructions.add(new VarInsnNode(ALOAD, 0));
							newInstructions.add(new VarInsnNode(FLOAD, 1));
							newInstructions.add(new MethodInsnNode(INVOKESPECIAL, classItemRenderer, "renderGasOverlay", "(F)V"));
							newInstructions.add(labelNode1);

							theLabelNode = null;
						}
					}
				}
				method.instructions = newInstructions;
			}
		}

		MethodNode renderGasOverlay = new MethodNode(2, "renderGasOverlay", "(F)V", null, new String[0]);
		{
			renderGasOverlay.visitCode();
			Label l0 = new Label();
			renderGasOverlay.visitLabel(l0);
			/*renderGasOverlay.visitVarInsn(ALOAD, 0);
			renderGasOverlay.visitFieldInsn(GETFIELD, classItemRenderer, fieldMc, "L" + classMinecraft + ";");
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classMinecraft, methodGetTextureManager, "()L" + classTextureManager+ ";");
			renderGasOverlay.visitFieldInsn(GETSTATIC, classItemRenderer, "gasOverlay", "L" + classResourceLocation + ";");
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classTextureManager, methodBindTexture, "(L" + classResourceLocation + ";)V");*/
			renderGasOverlay.visitFieldInsn(GETSTATIC, classTessellator, fieldInstance, "L" + classTessellator + ";");
			renderGasOverlay.visitVarInsn(ASTORE, 2);
			Label l2 = new Label();
			renderGasOverlay.visitLabel(l2);
			renderGasOverlay.visitVarInsn(ALOAD, 0);
			renderGasOverlay.visitFieldInsn(GETFIELD, classItemRenderer, fieldMc, "L" + classMinecraft + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classMinecraft, fieldThePlayer, "L" + classEntityClientPlayerMP + ";");
			renderGasOverlay.visitVarInsn(FLOAD, 1);
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classEntityClientPlayerMP, methodGetBrightness, "(F)F");
			renderGasOverlay.visitVarInsn(FSTORE, 3);
			Label l3 = new Label();
			renderGasOverlay.visitLabel(l3);
			renderGasOverlay.visitVarInsn(ALOAD, 0);
			renderGasOverlay.visitFieldInsn(GETFIELD, classItemRenderer, fieldMc, "L" + classMinecraft + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classMinecraft, fieldThePlayer, "L" + classEntityClientPlayerMP + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classEntityClientPlayerMP, fieldPosX, "D");
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, classMathHelper, methodFloor_double, "(D)I");
			renderGasOverlay.visitVarInsn(ISTORE, 4);
			Label l4 = new Label();
			renderGasOverlay.visitLabel(l4);
			renderGasOverlay.visitVarInsn(ALOAD, 0);
			renderGasOverlay.visitFieldInsn(GETFIELD, classItemRenderer, fieldMc, "L" + classMinecraft + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classMinecraft, fieldThePlayer, "L" + classEntityClientPlayerMP + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classEntityClientPlayerMP, fieldPosY, "D");
			renderGasOverlay.visitVarInsn(ALOAD, 0);
			renderGasOverlay.visitFieldInsn(GETFIELD, classItemRenderer, fieldMc, "L" + classMinecraft + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classMinecraft, fieldThePlayer, "L" + classEntityClientPlayerMP + ";");
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classEntityClientPlayerMP, methodGetEyeHeight, "()F");
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitInsn(DADD);
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, classMathHelper, methodFloor_double, "(D)I");
			renderGasOverlay.visitVarInsn(ISTORE, 5);
			Label l5 = new Label();
			renderGasOverlay.visitLabel(l5);
			renderGasOverlay.visitVarInsn(ALOAD, 0);
			renderGasOverlay.visitFieldInsn(GETFIELD, classItemRenderer, fieldMc, "L" + classMinecraft + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classMinecraft, fieldThePlayer, "L" + classEntityClientPlayerMP + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classEntityClientPlayerMP, fieldPosZ, "D");
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, classMathHelper, methodFloor_double, "(D)I");
			renderGasOverlay.visitVarInsn(ISTORE, 6);
			Label l6 = new Label();
			renderGasOverlay.visitLabel(l6);
			renderGasOverlay.visitVarInsn(ALOAD, 0);
			renderGasOverlay.visitFieldInsn(GETFIELD, classItemRenderer, fieldMc, "L" + classMinecraft + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classMinecraft, fieldTheWorld, "L" + classWorldClient + ";");
			renderGasOverlay.visitVarInsn(ILOAD, 4);
			renderGasOverlay.visitVarInsn(ILOAD, 5);
			renderGasOverlay.visitVarInsn(ILOAD, 6);
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classWorldClient, methodGetBlockId, "(III)I");
			renderGasOverlay.visitVarInsn(ISTORE, 7);
			Label l7 = new Label();
			renderGasOverlay.visitLabel(l7);
			renderGasOverlay.visitFieldInsn(GETSTATIC, classBlock, fieldBlocksList, "[L" + classBlock + ";");
			renderGasOverlay.visitVarInsn(ILOAD, 7);
			renderGasOverlay.visitInsn(AALOAD);
			renderGasOverlay.visitTypeInsn(INSTANCEOF, "glenn/gasesframework/BlockGas");
			Label l8 = new Label();
			renderGasOverlay.visitJumpInsn(IFNE, l8);
			renderGasOverlay.visitInsn(RETURN);
			renderGasOverlay.visitLabel(l8);
			renderGasOverlay.visitLineNumber(667, l8);
			renderGasOverlay.visitFieldInsn(GETSTATIC, classBlock, fieldBlocksList, "[L" + classBlock + ";");
			renderGasOverlay.visitVarInsn(ILOAD, 7);
			renderGasOverlay.visitInsn(AALOAD);
			renderGasOverlay.visitTypeInsn(CHECKCAST, "glenn/gasesframework/BlockGas");
			renderGasOverlay.visitFieldInsn(GETFIELD, "glenn/gasesframework/BlockGas", "type", "Lglenn/gasesframework/GasType;");
			renderGasOverlay.visitInsn(DUP);
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, "glenn/gasesframework/GasType", "getOverlayImage", "()L" + classResourceLocation + ";");
			renderGasOverlay.visitVarInsn(ALOAD, 0);
			renderGasOverlay.visitFieldInsn(GETFIELD, classItemRenderer, fieldMc, "L" + classMinecraft + ";");
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classMinecraft, methodGetTextureManager, "()L" + classTextureManager+ ";");
			renderGasOverlay.visitInsn(SWAP);
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classTextureManager, methodBindTexture, "(L" + classResourceLocation + ";)V");
			renderGasOverlay.visitFieldInsn(GETFIELD, "glenn/gasesframework/GasType", "color", "I");
			renderGasOverlay.visitVarInsn(ISTORE, 8);
			Label l10 = new Label();
			renderGasOverlay.visitLabel(l10);
			renderGasOverlay.visitVarInsn(FLOAD, 3);
			renderGasOverlay.visitVarInsn(ILOAD, 8);
			renderGasOverlay.visitIntInsn(BIPUSH, 16);
			renderGasOverlay.visitInsn(ISHR);
			renderGasOverlay.visitIntInsn(SIPUSH, 255);
			renderGasOverlay.visitInsn(IAND);
			renderGasOverlay.visitInsn(I2F);
			renderGasOverlay.visitInsn(FMUL);
			renderGasOverlay.visitLdcInsn(255.0F);
			renderGasOverlay.visitInsn(FDIV);
			renderGasOverlay.visitVarInsn(FSTORE, 9);
			Label l11 = new Label();
			renderGasOverlay.visitLabel(l11);
			renderGasOverlay.visitVarInsn(FLOAD, 3);
			renderGasOverlay.visitVarInsn(ILOAD, 8);
			renderGasOverlay.visitIntInsn(BIPUSH, 8);
			renderGasOverlay.visitInsn(ISHR);
			renderGasOverlay.visitIntInsn(SIPUSH, 255);
			renderGasOverlay.visitInsn(IAND);
			renderGasOverlay.visitInsn(I2F);
			renderGasOverlay.visitInsn(FMUL);
			renderGasOverlay.visitLdcInsn(255.0F);
			renderGasOverlay.visitInsn(FDIV);
			renderGasOverlay.visitVarInsn(FSTORE, 10);
			Label l12 = new Label();
			renderGasOverlay.visitLabel(l12);
			renderGasOverlay.visitVarInsn(FLOAD, 3);
			renderGasOverlay.visitVarInsn(ILOAD, 8);
			renderGasOverlay.visitIntInsn(SIPUSH, 255);
			renderGasOverlay.visitInsn(IAND);
			renderGasOverlay.visitInsn(I2F);
			renderGasOverlay.visitInsn(FMUL);
			renderGasOverlay.visitLdcInsn(255.0F);
			renderGasOverlay.visitInsn(FDIV);
			renderGasOverlay.visitVarInsn(FSTORE, 11);
			Label l13 = new Label();
			renderGasOverlay.visitLabel(l13);
			renderGasOverlay.visitVarInsn(FLOAD, 9);
			renderGasOverlay.visitVarInsn(FLOAD, 10);
			renderGasOverlay.visitVarInsn(FLOAD, 11);
			renderGasOverlay.visitInsn(FCONST_1);
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glColor4f", "(FFFF)V");
			renderGasOverlay.visitIntInsn(SIPUSH, 3042);
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glEnable", "(I)V");
			renderGasOverlay.visitIntInsn(SIPUSH, 2929);
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glDisable", "(I)V");
			renderGasOverlay.visitIntInsn(SIPUSH, 770);
			renderGasOverlay.visitIntInsn(SIPUSH, 771);
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glBlendFunc", "(II)V");
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glPushMatrix", "()V");
			renderGasOverlay.visitLdcInsn(4.0F);
			renderGasOverlay.visitVarInsn(FSTORE, 12);
			Label l19 = new Label();
			renderGasOverlay.visitLabel(l19);
			renderGasOverlay.visitLdcInsn(-1.0F);
			renderGasOverlay.visitVarInsn(FSTORE, 13);
			Label l20 = new Label();
			renderGasOverlay.visitLabel(l20);
			renderGasOverlay.visitInsn(FCONST_1);
			renderGasOverlay.visitVarInsn(FSTORE, 14);
			Label l21 = new Label();
			renderGasOverlay.visitLabel(l21);
			renderGasOverlay.visitLdcInsn(-1.0F);
			renderGasOverlay.visitVarInsn(FSTORE, 15);
			Label l22 = new Label();
			renderGasOverlay.visitLabel(l22);
			renderGasOverlay.visitInsn(FCONST_1);
			renderGasOverlay.visitVarInsn(FSTORE, 16);
			Label l23 = new Label();
			renderGasOverlay.visitLabel(l23);
			renderGasOverlay.visitLdcInsn(-0.5F);
			renderGasOverlay.visitVarInsn(FSTORE, 17);
			Label l24 = new Label();
			renderGasOverlay.visitLabel(l24);
			renderGasOverlay.visitVarInsn(ALOAD, 0);
			renderGasOverlay.visitFieldInsn(GETFIELD, classItemRenderer, fieldMc, "L" + classMinecraft + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classMinecraft, fieldThePlayer, "L" + classEntityClientPlayerMP + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classEntityClientPlayerMP, fieldRotationYaw, "F");
			renderGasOverlay.visitInsn(FNEG);
			renderGasOverlay.visitLdcInsn(64.0F);
			renderGasOverlay.visitInsn(FDIV);
			renderGasOverlay.visitVarInsn(FSTORE, 18);
			Label l25 = new Label();
			renderGasOverlay.visitLabel(l25);
			renderGasOverlay.visitVarInsn(ALOAD, 0);
			renderGasOverlay.visitFieldInsn(GETFIELD, classItemRenderer, fieldMc, "L" + classMinecraft + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classMinecraft, fieldThePlayer, "L" + classEntityClientPlayerMP + ";");
			renderGasOverlay.visitFieldInsn(GETFIELD, classEntityClientPlayerMP, fieldRotationPitch, "F");
			renderGasOverlay.visitLdcInsn(64.0F);
			renderGasOverlay.visitInsn(FDIV);
			renderGasOverlay.visitVarInsn(FSTORE, 19);
			Label l26 = new Label();
			renderGasOverlay.visitLabel(l26);
			renderGasOverlay.visitVarInsn(ALOAD, 2);
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classTessellator, methodStartDrawingQuads, "()V");
			renderGasOverlay.visitVarInsn(ALOAD, 2);
			renderGasOverlay.visitVarInsn(FLOAD, 13);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 15);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 17);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 12);
			renderGasOverlay.visitVarInsn(FLOAD, 18);
			renderGasOverlay.visitInsn(FADD);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 12);
			renderGasOverlay.visitVarInsn(FLOAD, 19);
			renderGasOverlay.visitInsn(FADD);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classTessellator, methodAddVertexWithUV, "(DDDDD)V");
			renderGasOverlay.visitVarInsn(ALOAD, 2);
			renderGasOverlay.visitVarInsn(FLOAD, 14);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 15);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 17);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitInsn(FCONST_0);
			renderGasOverlay.visitVarInsn(FLOAD, 18);
			renderGasOverlay.visitInsn(FADD);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 12);
			renderGasOverlay.visitVarInsn(FLOAD, 19);
			renderGasOverlay.visitInsn(FADD);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classTessellator, methodAddVertexWithUV, "(DDDDD)V");
			renderGasOverlay.visitVarInsn(ALOAD, 2);
			renderGasOverlay.visitVarInsn(FLOAD, 14);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 16);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 17);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitInsn(FCONST_0);
			renderGasOverlay.visitVarInsn(FLOAD, 18);
			renderGasOverlay.visitInsn(FADD);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitInsn(FCONST_0);
			renderGasOverlay.visitVarInsn(FLOAD, 19);
			renderGasOverlay.visitInsn(FADD);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classTessellator, methodAddVertexWithUV, "(DDDDD)V");
			renderGasOverlay.visitVarInsn(ALOAD, 2);
			renderGasOverlay.visitVarInsn(FLOAD, 13);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 16);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 17);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitVarInsn(FLOAD, 12);
			renderGasOverlay.visitVarInsn(FLOAD, 18);
			renderGasOverlay.visitInsn(FADD);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitInsn(FCONST_0);
			renderGasOverlay.visitVarInsn(FLOAD, 19);
			renderGasOverlay.visitInsn(FADD);
			renderGasOverlay.visitInsn(F2D);
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classTessellator, methodAddVertexWithUV, "(DDDDD)V");
			renderGasOverlay.visitVarInsn(ALOAD, 2);
			renderGasOverlay.visitMethodInsn(INVOKEVIRTUAL, classTessellator, methodDraw, "()I");
			renderGasOverlay.visitInsn(POP);
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glPopMatrix", "()V");
			renderGasOverlay.visitInsn(FCONST_1);
			renderGasOverlay.visitInsn(FCONST_1);
			renderGasOverlay.visitInsn(FCONST_1);
			renderGasOverlay.visitInsn(FCONST_1);
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glColor4f", "(FFFF)V");
			renderGasOverlay.visitIntInsn(SIPUSH, 3042);
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glDisable", "(I)V");
			renderGasOverlay.visitIntInsn(SIPUSH, 2929);
			renderGasOverlay.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glEnable", "(I)V");
			renderGasOverlay.visitInsn(RETURN);
			Label l37 = new Label();
			renderGasOverlay.visitLabel(l37);
			renderGasOverlay.visitLocalVariable("this", "L" + classItemRenderer + ";", null, l0, l37, 0);
			renderGasOverlay.visitLocalVariable("par1", "F", null, l0, l37, 1);
			renderGasOverlay.visitLocalVariable("var2", "L" + classTessellator + ";", null, l2, l37, 2);
			renderGasOverlay.visitLocalVariable("var3", "F", null, l3, l37, 3);
			renderGasOverlay.visitLocalVariable("playerPosX", "I", null, l4, l37, 4);
			renderGasOverlay.visitLocalVariable("playerPosY", "I", null, l5, l37, 5);
			renderGasOverlay.visitLocalVariable("playerPosZ", "I", null, l6, l37, 6);
			renderGasOverlay.visitLocalVariable("blockID", "I", null, l7, l37, 7);
			renderGasOverlay.visitLocalVariable("color", "I", null, l10, l37, 8);
			renderGasOverlay.visitLocalVariable("red", "F", null, l11, l37, 9);
			renderGasOverlay.visitLocalVariable("green", "F", null, l12, l37, 10);
			renderGasOverlay.visitLocalVariable("blue", "F", null, l13, l37, 11);
			renderGasOverlay.visitLocalVariable("var4", "F", null, l19, l37, 12);
			renderGasOverlay.visitLocalVariable("var5", "F", null, l20, l37, 13);
			renderGasOverlay.visitLocalVariable("var6", "F", null, l21, l37, 14);
			renderGasOverlay.visitLocalVariable("var7", "F", null, l22, l37, 15);
			renderGasOverlay.visitLocalVariable("var8", "F", null, l23, l37, 16);
			renderGasOverlay.visitLocalVariable("var9", "F", null, l24, l37, 17);
			renderGasOverlay.visitLocalVariable("var10", "F", null, l25, l37, 18);
			renderGasOverlay.visitLocalVariable("var11", "F", null, l26, l37, 19);
			renderGasOverlay.visitMaxs(11, 20);
			renderGasOverlay.visitEnd();
		}

		classNode.methods.add(renderGasOverlay);

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	public byte[] patchClassGuiIngame(byte[] data)
	{
		String methodRenderAir = "renderAir";

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);

		for(int i = 0; i < classNode.methods.size(); i++)
		{
			MethodNode method = (MethodNode)classNode.methods.get(i);
			if(method.name.equals(methodRenderAir))
			{
				boolean obfuscated = true;
				
				for(int j = 0; j < method.instructions.size(); j++)
				{
					AbstractInsnNode dependentInsnNode = method.instructions.get(11);
					if(dependentInsnNode.getOpcode() == GETFIELD)
					{
						FieldInsnNode fieldInsnNode = (FieldInsnNode)dependentInsnNode;
						if(fieldInsnNode.name.equals("mc"))
						{
							obfuscated = false;
						}
						break;
					}
				}
				
				
				String classGuiIngameForge = "net/minecraftforge/client/GuiIngameForge";
				String classGuiIngame = obfuscated ? c.get("GuiIngame") : "net/minecraft/client/gui/GuiIngame";
				String classMinecraft = obfuscated ? c.get("Minecraft") : "net/minecraft/client/Minecraft";
				String classEntityClientPlayerMP = obfuscated ? c.get("EntityClientPlayerMP") : "net/minecraft/client/entity/EntityClientPlayerMP";
				String classMaterial = obfuscated ? c.get("Material") : "net/minecraft/block/material/Material";

				String methodIsInsideOfMaterial = obfuscated ? "a" : "isInsideOfMaterial";

				String fieldMc = obfuscated  ? "g" : "mc";
				String fieldThePlayer = obfuscated ? "h" : "thePlayer";
				
				String descriptor = "(L" + classMaterial + ";)Z";
				
				InsnList newInstructions = new InsnList();
				for(int j = 0; j < method.instructions.size(); j++)
				{
					AbstractInsnNode instruction = method.instructions.get(j);
					newInstructions.add(instruction);
					if(instruction.getOpcode() == INVOKEVIRTUAL)
					{
						MethodInsnNode invokeInstruction = (MethodInsnNode)instruction;
						if(invokeInstruction.name.equals(methodIsInsideOfMaterial) & invokeInstruction.desc.equals(descriptor))
						{
							newInstructions.add(new VarInsnNode(ALOAD, 0));
							newInstructions.add(new FieldInsnNode(GETFIELD, classGuiIngame, fieldMc, "L" + classMinecraft + ";"));
							newInstructions.add(new FieldInsnNode(GETFIELD, classMinecraft, fieldThePlayer, "L" + classEntityClientPlayerMP + ";"));
							newInstructions.add(new FieldInsnNode(GETSTATIC, "glenn/gasesframework/GasesFramework", "gasMaterial", "L" + classMaterial + ";"));
							newInstructions.add(new MethodInsnNode(INVOKEVIRTUAL, classEntityClientPlayerMP, methodIsInsideOfMaterial, "(L" + classMaterial + ";)Z"));
							newInstructions.add(new InsnNode(IOR));
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
}
