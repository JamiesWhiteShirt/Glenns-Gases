package glenn.gases;

import glenn.gases.client.SoundBus;
import glenn.gases.reaction.ReactionAcidVapour;
import glenn.gases.reaction.ReactionCorrosion;
import glenn.gases.reaction.ReactionGlowstoneShard;
import glenn.gasesframework.BlockGas;
import glenn.gasesframework.BlockGasPipe;
import glenn.gasesframework.BlockLantern;
import glenn.gasesframework.BlockLanternSpecial;
import glenn.gasesframework.Combustibility;
import glenn.gasesframework.GasType;
import glenn.gasesframework.GasesFramework;
import glenn.gasesframework.reaction.ReactionIgnition;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * <b>Glenn's Gases</b>
 * <br>
 * Takes use of the Gases Framework to add a variety of gases to Minecraft, and to greatly affect the player's priorities and experience.
 * <br>
 * <br>
 * This piece of software can be only distributed with written permission from the author. The software must be unaltered.
 * <br>
 * Copyright 2013 Erlend Amdal
 * @author Glenn
 *
 */
@Mod(modid = "gases", name = "Glenn's Gases", version = Gases.version, dependencies="required-after:gasesFramework; required-after:gasesCore")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class Gases
{
	// The instance of your mod that Forge uses.
	@Instance("gases")
	public static Gases instance;
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "glenn.gases.client.ClientProxy", serverSide = "glenn.gases.CommonProxy")
	public static CommonProxy proxy;
	
	public static final String version = "1.4.3";
	
	static
	{
		GasesFramework.ENCOURAGE_MECHANICAL_ITEMS();
		GasesFramework.ENCOURAGE_LANTERNS();
	}
	
	private static Configuration config;
	public static int generateGreenGas;
	public static int generateRedGas;
	public static int generateNitrousGas;
	public static int generateElectricGas;
	public static int generateCorrosiveGas;
	public static int voidGasMaxHeight;
	public static float maxHardnessForCorrosion;
	public static boolean enableUpdateCheck;
	
	public static final DamageSource lightningDamageSource = new DamageSourceLightning("lightning");
	
	public static Item glowstoneShard;
	public static Item gasDetector;
	public static Item gasSensitiveChip;
	
	public static BlockGas gasSteam;
	public static BlockGas gasRisingFlammable;
	public static BlockGas gasFallingExplosive;
	public static BlockGas gasVoid;
	public static BlockGas gasElectric;
	public static BlockGas gasCorrosive;
	public static BlockGas gasNitrous;
	public static BlockGas gasAcidVapour;
	public static BlockGas gasCoalDust;
	
	public static BlockGasPipe gasPipeSteam;
	public static BlockGasPipe gasPipeRisingFlammable;
	public static BlockGasPipe gasPipeFallingExplosive;
	public static BlockGasPipe gasPipeElectric;
	public static BlockGasPipe gasPipeCorrosive;
	public static BlockGasPipe gasPipeNitrous;
	
	public static BlockLantern lanternTorch;
	public static BlockLantern lanternGlowstone;
	
	public static GasType gasTypeSteam;
	public static GasType gasTypeRisingFlammable;
	public static GasType gasTypeFallingExplosive;
	public static GasType gasTypeVoid;
	public static GasType gasTypeElectric;
	public static GasType gasTypeCorrosive;
	public static GasType gasTypeNitrous;
	public static GasType gasTypeAcidVapour;
	public static GasType gasTypeCoalDust;
	
	private void initBlocksAndItems()
	{
		GameRegistry.registerItem(glowstoneShard = new ItemGlowstoneShard(b("glowstoneShardID", 401)).setUnlocalizedName("glowstoneShard").setCreativeTab(GasesFramework.creativeTab).setTextureName("gases:glowstone_shard"), "glowstoneShard");
		GameRegistry.registerItem(gasDetector = new ItemGasDetector(b("gasDetectorID", 403)).setUnlocalizedName("gasDetector").setCreativeTab(GasesFramework.creativeTab).setTextureName("gases:detector"), "gasDetector");
		GameRegistry.registerItem(gasSensitiveChip = new Item(b("gasSensitiveChipID", 404)).setUnlocalizedName("gasSensitiveChip").setCreativeTab(GasesFramework.creativeTab).setTextureName("gases:chip"), "gasSensitiveChip");
		
		GameRegistry.registerBlock(gasSteam = (BlockGas)(new BlockGas(a("gasSteamID", 501))).setUnlocalizedName("gasSteam").setCreativeTab(GasesFramework.creativeTab), "gasSteam");
		GameRegistry.registerBlock(gasRisingFlammable = (BlockGas)(new BlockGas(a("gasRisingFlammableID", 502))).setUnlocalizedName("gasRisingFlammable").setCreativeTab(GasesFramework.creativeTab), "gasRisingFlammable");
		GameRegistry.registerBlock(gasFallingExplosive = (BlockGas)(new BlockGas(a("gasFallingExplosiveID", 503))).setUnlocalizedName("gasFallingExplosive").setCreativeTab(GasesFramework.creativeTab), "gasFallingExplosive");
		GameRegistry.registerBlock(gasVoid = (BlockGas)(new BlockGasVoid(a("gasVoidID", 504))).setUnlocalizedName("gasVoid").setCreativeTab(GasesFramework.creativeTab), "gasVoid");
		GameRegistry.registerBlock(gasElectric = (BlockGas)(new BlockGasElectric(a("gasElectricID", 505))).setUnlocalizedName("gasElectric").setCreativeTab(GasesFramework.creativeTab).setTextureName("gases:gas_special"), "gasElectric");
		GameRegistry.registerBlock(gasCorrosive = (BlockGas)(new BlockGas(a("gasCorrosiveID", 506))).setUnlocalizedName("gasCorrosive").setCreativeTab(GasesFramework.creativeTab).setTextureName("gases:gas_special"), "gasCorrosive");
		GameRegistry.registerBlock(gasNitrous = (BlockGas)(new BlockGas(a("gasNitrousID", 506))).setUnlocalizedName("gasNitrous").setCreativeTab(GasesFramework.creativeTab), "gasNitrous");
		GameRegistry.registerBlock(gasAcidVapour = (BlockGas)(new BlockGas(a("gasAcidVapourID", 507))).setUnlocalizedName("gasAcidVapour").setTextureName("gases:gas_special"), "gasAcidVapour");
		GameRegistry.registerBlock(gasCoalDust = (BlockGas)(new BlockGas(a("gasCoalDustID", 508)).setUnlocalizedName("gasCoalDust").setCreativeTab(GasesFramework.creativeTab)), "gasCoalDust");
		
		GameRegistry.registerBlock(gasPipeSteam = (BlockGasPipe)new BlockGasPipe(a("gasPipeSteamID", 527)).setUnlocalizedName("gasPipeSteam"), "gasPipeSteam");
		GameRegistry.registerBlock(gasPipeRisingFlammable = (BlockGasPipe)new BlockGasPipe(a("gasPipeRisingFlammableID", 528)).setUnlocalizedName("gasPipeRisingFlammable"), "gasPipeRisingFlammable");
		GameRegistry.registerBlock(gasPipeFallingExplosive = (BlockGasPipe)new BlockGasPipe(a("gasPipeFallingExplosiveID", 529)).setUnlocalizedName("gasPipeFallingExplosive"), "gasPipeFallingExplosive");
		GameRegistry.registerBlock(gasPipeElectric = (BlockGasPipe)new BlockGasPipe(a("gasPipeElectricID", 531)).setUnlocalizedName("gasPipeElectric"), "gasPipeElectric");
		GameRegistry.registerBlock(gasPipeCorrosive = (BlockGasPipe)new BlockGasPipe(a("gasPipeCorrosiveID", 532)).setUnlocalizedName("gasPipeCorrosive"), "gasPipeCorrosive");
		GameRegistry.registerBlock(gasPipeNitrous = (BlockGasPipe)new BlockGasPipe(a("gasPipeNitrousID", 532)).setUnlocalizedName("gasPipeNitrous"), "gasPipeNitrous");
		
		GameRegistry.registerBlock(lanternTorch = (BlockLantern)(new BlockLanternSpecial(a("lanternTorchID", 551), 0, new ItemStack(Block.torchWood), new ItemStack(Block.torchWood), null)).setLightValue(10.0F / 16.0F).setUnlocalizedName("lanternTorch").setTextureName("gases:lantern_torch"), "lanternTorch");
		GameRegistry.registerBlock(lanternGlowstone = (BlockLantern)(new BlockLanternSpecial(a("lanternGlowstoneID", 554), 0, new ItemStack(Item.glowstone), new ItemStack(Item.glowstone), null)).setLightValue(1.0F).setUnlocalizedName("lanternGlowstone").setTextureName("gases:lantern_glowstone"), "lanternGlowstone");
		
		gasTypeSteam = new GasType(gasSteam, gasPipeSteam, 2, "Steam", 0xFFFFFF, 0, -8, Combustibility.NONE).setDamage(2.0F).setEvaporationRate(2);
		gasTypeRisingFlammable = new GasType(gasRisingFlammable, gasPipeRisingFlammable, 3, "Green gas", 0x6F7F6F, 1, -12, Combustibility.FLAMMABLE).setEffectRates(2, 2, 0);
		gasTypeFallingExplosive = new GasType(gasFallingExplosive, gasPipeFallingExplosive, 4, "Red gas", 0x7F4F4F, 3, 4, Combustibility.EXPLOSIVE).setEffectRates(1, 2, 0);
		gasTypeVoid = new GasType(gasVoid, 5, "Void gas", 0x1F1F1F, 4, 8, Combustibility.NONE).setEffectRates(20, 4, 0).setDamage(1.0F);
		gasTypeElectric = new GasType(gasElectric, gasPipeElectric, 6, "Electric gas", 0x1F7F7F, 0, 0, Combustibility.NONE).setEffectRates(4, 2, 0);
		gasTypeCorrosive = new GasType(gasCorrosive, gasPipeCorrosive, 7, "Corrosive gas", 0x1F1FDF, 0, 0, Combustibility.NONE).setEffectRates(4, 2, 0).setDamage(0.5F);
		gasTypeNitrous = new GasTypePoisonous(gasNitrous, gasPipeNitrous, 8, "Nitrous gas", 0x6F3F2F, 3, 4, Combustibility.NONE).setEffectRates(1, 4, 0);
		gasTypeAcidVapour = new GasType(gasAcidVapour, 9, "Acidic vapour", 0x4F7FBF, 0, -20, Combustibility.NONE).setEffectRates(20, 1, 0).setDamage(2.0F).setEvaporationRate(2);
		gasTypeCoalDust = new GasType(gasCoalDust, 10, "Coal dust", 0x2F2F2F, 0, 0, Combustibility.HIGHLY_EXPLOSIVE).setEffectRates(1, 6, 0).setEvaporationRate(5);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		initBlocksAndItems();
		
		generateGreenGas = config.get("world", "generateGreenGasAmount", true).getInt(8);
		generateRedGas = config.get("world", "generateRedGasAmount", true).getInt(4);
		generateNitrousGas = config.get("world", "generateNitrousGasAmount", true).getInt(4);
		generateElectricGas = config.get("world", "generateElectricGasAmount", true).getInt(4);
		generateCorrosiveGas = config.get("world", "generateCorrosiveGasAmount", true).getInt(4);
		voidGasMaxHeight = config.get("gases", "voidGasMaxHeight", true).getInt(64);
		maxHardnessForCorrosion = Float.parseFloat(config.get("gases", "maxHardnessForCorrosion", 2.0F).getString());
		enableUpdateCheck = config.get("other", "enableUpdateCheck", true).getBoolean(true);
		
		config.save();
		
		if(FMLCommonHandler.instance().getSide().isClient())
		{
			MinecraftForge.EVENT_BUS.register(new SoundBus());
		}
		
		MinecraftForge.EVENT_BUS.register(new UpdateChecker());
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		GameRegistry.registerWorldGenerator(new WorldGeneratorGases());
		
		GameRegistry.addRecipe(new ItemStack(gasSensitiveChip), "GRG", "RDR", "GRG", 'G', Item.ingotGold, 'R', Item.redstone, 'D', Item.diamond);
		for(int i = 0; i < GasType.gasTypes.length; i++)
		{
			GasType type = GasType.gasTypes[i];
			if(i == 0 | (type != null && type.isIndustrial()))
			{
				GameRegistry.addRecipe(new ItemStack(gasDetector, 1, i), " I ", "ICS", " I ", 'S', new ItemStack(GasesFramework.gasSamplerIncluder, 1, i), 'I', new ItemStack(Item.ingotIron), 'C', new ItemStack(gasSensitiveChip));
			}
		}
		
		GasesFramework.addSpecialFurnaceRecipe(new ItemStack(Item.coal, 64), new ItemStack(Item.diamond), 25600);
		
		GasesFramework.registerIgnitionBlock(Block.torchWood.blockID);
		GasesFramework.registerIgnitionBlock(Block.fire.blockID);
		
		GasesFramework.registerReaction(new ReactionIgnition());
		GasesFramework.registerReaction(new ReactionGlowstoneShard(gasElectric, gasCorrosive));
		GasesFramework.registerReaction(new ReactionCorrosion((BlockGas)gasCorrosive));
		GasesFramework.registerReaction(new ReactionAcidVapour(11, gasNitrous, Block.waterStill));
		GasesFramework.registerReaction(new ReactionAcidVapour(12, gasNitrous, Block.waterMoving));
		
		LanguageRegistry.addName(gasSteam, "Steam");
		LanguageRegistry.addName(gasRisingFlammable, "Green Gas");
		LanguageRegistry.addName(gasFallingExplosive, "Red Gas");
		LanguageRegistry.addName(gasVoid, "Void Gas");
		LanguageRegistry.addName(gasElectric, "Electric Gas");
		LanguageRegistry.addName(gasCorrosive, "Corrosive Gas");
		LanguageRegistry.addName(gasNitrous, "Nitrous Gas");
		LanguageRegistry.addName(gasAcidVapour, "Acidic Vapour");
		LanguageRegistry.addName(gasCoalDust, "Coal Dust");
		LanguageRegistry.addName(lanternTorch, "Lantern with Torch");
		LanguageRegistry.addName(lanternGlowstone, "Lantern with Glowstone");
		
		LanguageRegistry.addName(glowstoneShard, "Glowstone Shard");
		LanguageRegistry.addName(gasSensitiveChip, "Gas Sensitive Chip");
		
		EntityRegistry.registerGlobalEntityID(EntitySmallLightning.class, "smallLightning", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntitySmallLightning.class, "smallLightning", 127, this, 20, 1, false);
	
		EntityRegistry.registerGlobalEntityID(EntityGlowstoneShard.class, "glowstoneShard", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityGlowstoneShard.class, "glowstoneShard", 126, this, 20, 1, false);
		
		proxy.registerRenderers();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	private static int a(String name, int def)
	{
		return config.getBlock(name, def).getInt();
	}
	
	private static int b(String name, int def)
	{
		return config.getItem(name, def).getInt();
	}
}