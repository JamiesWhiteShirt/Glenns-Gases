package glenn.gases;

import java.util.ArrayList;

import glenn.gases.TileEntityGasFurnace.SpecialFurnaceRecipe;
import glenn.gases.client.RenderBlockGas;
import glenn.gases.client.RenderBlockGasPipe;
import glenn.gases.client.RenderBlockLantern;
import glenn.gases.client.RenderBlockPump;
import glenn.gases.client.RenderBlockTank;
import glenn.gases.client.RenderSmallLightning;
import glenn.gases.client.SoundBus;
import glenn.gases.reaction.Reaction;
import glenn.gases.reaction.ReactionAcidVapour;
import glenn.gases.reaction.ReactionBlockReplacement;
import glenn.gases.reaction.ReactionCorrosion;
import glenn.gases.reaction.ReactionEmpty;
import glenn.gases.reaction.ReactionGlowstoneShard;
import glenn.gases.reaction.ReactionIgnition;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "Gases", name = "Gases", version = Gases.version)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class Gases
{
	// The instance of your mod that Forge uses.
	@Instance("Gases")
	public static Gases instance;
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "glenn.gases.client.ClientProxy", serverSide = "glenn.gases.CommonProxy")
	public static CommonProxy proxy;
	public static final GuiHandler guiHandler = new GuiHandler();
	
	public static final String version = "1.4.0";
	
	private static Configuration config;
	public static boolean generateGreenGas;
	public static boolean generateRedGas;
	public static boolean generateNitrousGas;
	public static boolean generateElectricGas;
	public static boolean generateCorrosiveGas;
	public static float maxHardnessForCorrosion;
	public static float gasExplosionFactor;
	public static int gasFurnaceHeatingSpeed;
	public static boolean enableUpdateCheck;
	
	public static int renderBlockGasID;
	public static int renderBlockLanternID;
	public static int renderBlockGasPipeID;
	public static int renderBlockPumpID;
	public static int renderBlockTankID;
	
	public static final Material gasMaterial = (new Material(MapColor.airColor)).setReplaceable();
	public static final DamageSource lightningDamageSource = new DamageSourceLightning("lightning");
	
	
	
	private static boolean gasReactives[] = new boolean[Block.blocksList.length];
	private static ArrayList<Reaction> reactions = new ArrayList<Reaction>();
	
	
	
	public static Item gasBottle;
	public static Item glowstoneShard;
	public static Item gasSamplerIncluder;
	public static Item gasSamplerExcluder;
	
	public static BlockGas gasSmoke;
	public static BlockGas gasSteam;
	public static BlockGas gasRisingFlammable;
	public static BlockGas gasFallingExplosive;
	public static BlockGas gasVoid;
	public static BlockGas gasElectric;
	public static BlockGas gasCorrosive;
	public static BlockGas gasNitrous;
	public static BlockGas gasAcidVapour;
	
	
	/**
	 * The empty gas pipe block.
	 */
	public static BlockGasPipe gasPipeAir;
	public static BlockGasPipe gasPipeSmoke;
	public static BlockGasPipe gasPipeSteam;
	public static BlockGasPipe gasPipeRisingFlammable;
	public static BlockGasPipe gasPipeFallingExplosive;
	public static BlockGasPipe gasPipeVoid;
	public static BlockGasPipe gasPipeElectric;
	public static BlockGasPipe gasPipeCorrosive;
	public static BlockGasPipe gasPipeNitrous;
	public static BlockGasPipe gasPipeAcidVapour;
	
	public static Block gasPump;
	public static Block gasTank;
	public static Block gasCollector;
	public static Block gasFurnaceIdle;
	public static Block gasFurnaceActive;
	
	public static int lanternEmptyID;
	public static BlockLantern lanternEmpty;
	public static BlockLantern lanternTorch;
	public static BlockLantern lanternGasEmpty;
	public static BlockLantern lanternGas1;
	public static BlockLantern lanternGas2;
	public static BlockLantern lanternGas3;
	public static BlockLantern lanternGas4;
	public static BlockLantern lanternGas5;
	public static BlockLantern lanternGlowstone;
	
	public static GasType gasTypeAir;
	public static GasType gasTypeSmoke;
	public static GasType gasTypeSteam;
	public static GasType gasTypeRisingFlammable;
	public static GasType gasTypeFallingExplosive;
	public static GasType gasTypeVoid;
	public static GasType gasTypeElectric;
	public static GasType gasTypeCorrosive;
	public static GasType gasTypeNitrous;
	public static GasType gasTypeAcidVapour;
	
	public static CreativeTabs creativeTab;
	
	public static UtilMethods util = new UtilMethods();
	
	private void initBlocksAndItems()
	{
		GameRegistry.registerItem(gasBottle = (new ItemGasBottle(b("gasBottleID", 400))).setUnlocalizedName("gasBottle").setCreativeTab(creativeTab).setTextureName("gases:gas_bottle"), "gasBottle");
		GameRegistry.registerItem(glowstoneShard = (new ItemGlowstoneShard(b("glowstoneShardID", 401))).setUnlocalizedName("glowstoneShard").setCreativeTab(creativeTab).setTextureName("gases:glowstone_shard"), "glowstoneShard");
		GameRegistry.registerItem(gasSamplerIncluder = (new ItemGasSampler(b("gasSamplerIncluderID", 402), false)).setUnlocalizedName("gasSamplerIncluder").setCreativeTab(creativeTab).setTextureName("gases:sampler"), "gasSamplerIncluder");
		GameRegistry.registerItem(gasSamplerExcluder = (new ItemGasSampler(b("gasSamplerExcluderID", 403), true)).setUnlocalizedName("gasSamplerExcluder").setCreativeTab(creativeTab).setTextureName("gases:sampler"), "gasSamplerExcluder");
		
		GameRegistry.registerBlock(gasSmoke = (BlockGas)(new BlockGas(a("gasSmokeID", 500))).setHardness(0.0F).setUnlocalizedName("gasSmoke").setCreativeTab(creativeTab).setTextureName("gases:gas"), "gasSmoke");
		GameRegistry.registerBlock(gasSteam = (BlockGas)(new BlockGas(a("gasSteamID", 501))).setHardness(0.0F).setUnlocalizedName("gasSteam").setCreativeTab(creativeTab).setTextureName("gases:gas"), "gasSteam");
		GameRegistry.registerBlock(gasRisingFlammable = (BlockGas)(new BlockGas(a("gasRisingFlammableID", 502))).setHardness(0.0F).setUnlocalizedName("gasRisingFlammable").setCreativeTab(creativeTab).setTextureName("gases:gas"), "gasRisingFlammable");
		GameRegistry.registerBlock(gasFallingExplosive = (BlockGas)(new BlockGas(a("gasFallingExplosiveID", 503))).setHardness(0.0F).setUnlocalizedName("gasFallingExplosive").setCreativeTab(creativeTab).setTextureName("gases:gas"), "gasFallingExplosive");
		GameRegistry.registerBlock(gasVoid = (BlockGas)(new BlockGasVoid(a("gasVoidID", 504))).setHardness(0.0F).setUnlocalizedName("gasVoid").setCreativeTab(creativeTab).setTextureName("gases:gas"), "gasVoid");
		GameRegistry.registerBlock(gasElectric = (BlockGas)(new BlockGasElectric(a("gasElectricID", 505))).setHardness(0.0F).setUnlocalizedName("gasElectric").setCreativeTab(creativeTab).setTextureName("gases:gas_special"), "gasElectric");
		GameRegistry.registerBlock(gasCorrosive = (BlockGas)(new BlockGas(a("gasCorrosiveID", 506))).setHardness(0.0F).setUnlocalizedName("gasCorrosive").setCreativeTab(creativeTab).setTextureName("gases:gas_special"), "gasCorrosive");
		GameRegistry.registerBlock(gasNitrous = (BlockGas)(new BlockGas(a("gasNitrousID", 506))).setHardness(0.0F).setUnlocalizedName("gasNitrous").setCreativeTab(creativeTab).setTextureName("gases:gas"), "gasNitrous");
		GameRegistry.registerBlock(gasAcidVapour = (BlockGas)(new BlockGas(a("gasAcidVapourID", 507))).setHardness(0.0F).setUnlocalizedName("gasAcidVapour").setCreativeTab(creativeTab).setTextureName("gases:gas_special"), "gasAcidVapour");
		
		GameRegistry.registerBlock(gasPipeAir = (BlockGasPipe)new BlockGasPipe(a("gasPipeAirID", 525)).setCreativeTab(creativeTab).setUnlocalizedName("gasPipeEmpty"), "gasPipeAir");
		GameRegistry.registerBlock(gasPipeSmoke = (BlockGasPipe)new BlockGasPipe(a("gasPipeSmokeID", 526)).setUnlocalizedName("gasPipeSmoke"), "gasPipeSmoke");
		GameRegistry.registerBlock(gasPipeSteam = (BlockGasPipe)new BlockGasPipe(a("gasPipeSteamID", 527)).setUnlocalizedName("gasPipeSteam"), "gasPipeSteam");
		GameRegistry.registerBlock(gasPipeRisingFlammable = (BlockGasPipe)new BlockGasPipe(a("gasPipeRisingFlammableID", 528)).setUnlocalizedName("gasPipeRisingFlammable"), "gasPipeRisingFlammable");
		GameRegistry.registerBlock(gasPipeFallingExplosive = (BlockGasPipe)new BlockGasPipe(a("gasPipeFallingExplosiveID", 529)).setUnlocalizedName("gasPipeFallingExplosive"), "gasPipeFallingExplosive");
		GameRegistry.registerBlock(gasPipeVoid = (BlockGasPipe)new BlockGasPipe(a("gasPipeVoidID", 530)).setUnlocalizedName("gasPipeVoid"), "gasPipeVoid");
		GameRegistry.registerBlock(gasPipeElectric = (BlockGasPipe)new BlockGasPipe(a("gasPipeElectricID", 531)).setUnlocalizedName("gasPipeElectric"), "gasPipeElectric");
		GameRegistry.registerBlock(gasPipeCorrosive = (BlockGasPipe)new BlockGasPipe(a("gasPipeCorrosiveID", 532)).setUnlocalizedName("gasPipeCorrosive"), "gasPipeCorrosive");
		GameRegistry.registerBlock(gasPipeNitrous = (BlockGasPipe)new BlockGasPipe(a("gasPipeNitrousID", 532)).setUnlocalizedName("gasPipeNitrous"), "gasPipeNitrous");
		GameRegistry.registerBlock(gasPipeNitrous = (BlockGasPipe)new BlockGasPipe(a("gasPipeAcidVapourID", 532)).setUnlocalizedName("gasPipeAcidVapour"), "gasPipeAcidVapour");
	
		GameRegistry.registerBlock(gasPump = new BlockPump(a("gasPumpID", 540)).setCreativeTab(creativeTab).setUnlocalizedName("gasPump").setTextureName("gases:pump"), "gasPump");
		GameRegistry.registerBlock(gasTank = new BlockGasTank(a("gasTankID", 541)).setCreativeTab(creativeTab).setUnlocalizedName("gasTank").setTextureName("gases:tank"), "gasTank");
		GameRegistry.registerBlock(gasCollector = new BlockGasCollector(a("gasCollectorID", 542)).setCreativeTab(creativeTab).setUnlocalizedName("gasCollector").setTextureName("gases:collector"), "gasCollector");
		GameRegistry.registerBlock(gasFurnaceIdle = (new BlockGasFurnace(a("gasFurnaceIdleID", 543), false)).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("gasFurnace").setCreativeTab(creativeTab), "gasFurnaceIdle");
		GameRegistry.registerBlock(gasFurnaceActive = (new BlockGasFurnace(a("gasFurnaceActiveID", 544), true)).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setLightValue(0.25F).setUnlocalizedName("gasFurnace"), "gasFurnaceActive");
		
		lanternEmptyID = a("lanternEmptyID", 550);
		GameRegistry.registerBlock(lanternEmpty = (BlockLantern)(new BlockLanternEmpty(lanternEmptyID)).setHardness(0.0F).setUnlocalizedName("lanternEmpty").setCreativeTab(creativeTab).setTextureName("gases:lantern_empty"), "lanternEmpty");
		GameRegistry.registerBlock(lanternTorch = (BlockLantern)(new BlockLanternSpecial(a("lanternTorchID", 551), 0, new ItemStack(Block.torchWood), new ItemStack(Block.torchWood), null)).setLightValue(10.0F / 16.0F).setUnlocalizedName("lanternTorch").setTextureName("gases:lantern_torch"), "lanternTorch");
		GameRegistry.registerBlock(lanternGasEmpty = (BlockLantern)(new BlockLanternSpecial(a("lanternGasEmptyID", 552), 0, new ItemStack(Item.glassBottle), new ItemStack(Item.glassBottle), null)).setUnlocalizedName("lanternGasEmpty").setTextureName("gases:lantern_gas0_off"), "lanternGasEmpty");
		GameRegistry.registerBlock(lanternGas1 = (BlockLantern)(new BlockLantern(a("lanternGas1ID", 553), Combustibility.CONTROLLABLE)).setLightValue(1.0F).setUnlocalizedName("lanternGas1").setTextureName("gases:lantern_gas0_on"), "lanternGas1");
		GameRegistry.registerBlock(lanternGas2 = (BlockLantern)(new BlockLantern(a("lanternGas2ID", 555), Combustibility.FLAMMABLE)).setLightValue(1.0F).setUnlocalizedName("lanternGas2").setTextureName("gases:lantern_gas0_on"), "lanternGas2");
		GameRegistry.registerBlock(lanternGas3 = (BlockLantern)(new BlockLantern(a("lanternGas3ID", 557), Combustibility.HIGHLY_FLAMMABLE)).setLightValue(1.0F).setUnlocalizedName("lanternGas3").setTextureName("gases:lantern_gas0_on"), "lanternGas3");
		GameRegistry.registerBlock(lanternGas4 = (BlockLantern)(new BlockLantern(a("lanternGas4ID", 558), Combustibility.EXPLOSIVE)).setLightValue(1.0F).setUnlocalizedName("lanternGas4").setTextureName("gases:lantern_gas0_on"), "lanternGas4");
		GameRegistry.registerBlock(lanternGas5 = (BlockLantern)(new BlockLantern(a("lanternGas5ID", 559), Combustibility.HIGHLY_EXPLOSIVE)).setLightValue(1.0F).setUnlocalizedName("lanternGas5").setTextureName("gases:lantern_gas0_on"), "lanternGas5");
		GameRegistry.registerBlock(lanternGlowstone = (BlockLantern)(new BlockLanternSpecial(a("lanternGlowstoneID", 554), 0, new ItemStack(Item.glowstone), new ItemStack(Item.glowstone), null)).setLightValue(1.0F).setUnlocalizedName("lanternGlowstone").setTextureName("gases:lantern_glowstone"), "lanternGlowstone");
		
		gasTypeAir = new GasTypeAir();
		gasTypeSmoke = new GasType(gasSmoke, gasPipeSmoke, 1, "Smoke", 0x3F3F3F, 2, -16, Combustibility.NONE).setEffectRates(4, 4, 0);
		gasTypeSteam = new GasType(gasSteam, gasPipeSteam, 2, "Steam", 0xFFFFFF, 0, -8, Combustibility.NONE).setDamage(2.0F).setEvaporationRate(1);
		gasTypeRisingFlammable = new GasType(gasRisingFlammable, gasPipeRisingFlammable, 3, "Green gas", 0x6F7F6F, 1, -12, Combustibility.FLAMMABLE).setEffectRates(2, 2, 0);
		gasTypeFallingExplosive = new GasType(gasFallingExplosive, gasPipeFallingExplosive, 4, "Red gas", 0x7F4F4F, 3, 4, Combustibility.EXPLOSIVE).setEffectRates(1, 2, 0);
		gasTypeVoid = new GasType(gasVoid, gasPipeVoid, 5, "Void gas", 0x1F1F1F, 4, 8, Combustibility.NONE).setEffectRates(20, 4, 0).setDamage(1.0F);
		gasTypeElectric = new GasType(gasElectric, gasPipeElectric, 6, "Electric gas", 0x1F7F7F, 0, 0, Combustibility.NONE).setEffectRates(4, 2, 0);
		gasTypeCorrosive = new GasType(gasCorrosive, gasPipeCorrosive, 7, "Corrosive gas", 0x1F1FDF, 0, 0, Combustibility.NONE).setEffectRates(4, 2, 0).setDamage(0.5F);
		gasTypeNitrous = new GasTypePoisonous(gasNitrous, gasPipeNitrous, 8, "Nitrous gas", 0x6F3F2F, 3, 4, Combustibility.NONE).setEffectRates(1, 4, 0);
		gasTypeAcidVapour = new GasType(gasAcidVapour, gasPipeAcidVapour, 9, "Acidic vapour", 0x4F7FBF, 0, -20, Combustibility.NONE).setEffectRates(20, 1, 0).setDamage(2.0F).setEvaporationRate(2);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		creativeTab = new CreativeTabs("tabGases")
		{
			public ItemStack getIconItemStack()
			{
				return new ItemStack(lanternEmpty, 1, 0);
			}
		};
		
		initBlocksAndItems();
		
		generateGreenGas = config.get("world", "generateGreenGas", true).getBoolean(true);
		generateRedGas = config.get("world", "generateRedGas", true).getBoolean(true);
		generateNitrousGas = config.get("world", "generateNitrousGas", true).getBoolean(true);
		generateElectricGas = config.get("world", "generateElectricGas", true).getBoolean(true);
		generateCorrosiveGas = config.get("world", "generateCorrosiveGas", true).getBoolean(true);
		maxHardnessForCorrosion = Float.parseFloat(config.get("gases", "maxHardnessForCorrosion", 2.0F).getString());
		gasExplosionFactor = Float.parseFloat(config.get("gases", "gasExplosionFactor", 2.5F).getString());
		gasFurnaceHeatingSpeed = config.get("gases", "gasFurnaceHeatingSpeed", 2).getInt();
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
		renderBlockGasID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderBlockGasID, new RenderBlockGas());
		renderBlockLanternID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderBlockLanternID, new RenderBlockLantern());
		renderBlockGasPipeID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderBlockGasPipeID, new RenderBlockGasPipe());
		renderBlockPumpID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderBlockPumpID, new RenderBlockPump());
		renderBlockTankID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderBlockTankID, new RenderBlockTank());
	
		GameRegistry.registerWorldGenerator(new WorldGeneratorGases());
	
		GameRegistry.addRecipe(new ItemStack(lanternEmpty, 4), "I", "G", 'I', Item.ingotIron, 'G', Block.glass);
		GameRegistry.addRecipe(new ItemStack(gasPipeAir, 24), "III", 'I', Item.ingotIron);
		GameRegistry.addRecipe(new ItemStack(gasPump), " I ", "PRP", " I ", 'I', Item.ingotIron, 'P', gasPipeAir, 'R', Item.redstone);
		GameRegistry.addRecipe(new ItemStack(gasCollector), " P ", "PUP", " P ", 'U', gasPump, 'P', gasPipeAir);
		GameRegistry.addRecipe(new ItemStack(gasTank), "IPI", "P P", "IPI", 'I', Item.ingotIron, 'P', gasPipeAir);
		GameRegistry.addRecipe(new ItemStack(gasFurnaceIdle), " I ", "IFI", " I ", 'I', Item.ingotIron, 'F', Block.furnaceIdle);
		GameRegistry.addShapelessRecipe(new ItemStack(gasSamplerExcluder), new ItemStack(Item.glassBottle), new ItemStack(Item.dyePowder, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(gasSamplerIncluder), new ItemStack(Item.glassBottle), new ItemStack(Item.dyePowder, 1, 15));
		addSpecialFurnaceRecipe(new ItemStack(Item.coal, 64), new ItemStack(Item.diamond), 25600);
		
		registerIgnitionBlock(Block.torchWood.blockID);
		registerIgnitionBlock(Block.fire.blockID);
		
		registerReaction(new ReactionIgnition());
		registerReaction(new ReactionGlowstoneShard(gasElectric, gasCorrosive));
		registerReaction(new ReactionCorrosion((BlockGas)gasCorrosive));
		registerReaction(new ReactionAcidVapour(11, gasNitrous, Block.waterStill));
		registerReaction(new ReactionAcidVapour(12, gasNitrous, Block.waterMoving));
		
		LanguageRegistry.addName(gasSmoke, "Smoke");
		LanguageRegistry.addName(gasSteam, "Steam");
		LanguageRegistry.addName(gasRisingFlammable, "Green Gas");
		LanguageRegistry.addName(gasFallingExplosive, "Red Gas");
		LanguageRegistry.addName(gasVoid, "Void Gas");
		LanguageRegistry.addName(gasElectric, "Electric Gas");
		LanguageRegistry.addName(gasCorrosive, "Corrosive Gas");
		LanguageRegistry.addName(gasNitrous, "Nitrous Gas");
		LanguageRegistry.addName(gasAcidVapour, "Acidic Vapour");
		LanguageRegistry.addName(lanternEmpty, "Lantern");
		LanguageRegistry.addName(lanternTorch, "Lantern with Torch");
		LanguageRegistry.addName(lanternGasEmpty, "Empty Gas Lantern");
		LanguageRegistry.addName(lanternGas1, "Gas Lantern");
		LanguageRegistry.addName(lanternGas2, "Gas Lantern");
		LanguageRegistry.addName(lanternGas3, "Gas Lantern");
		LanguageRegistry.addName(lanternGas4, "Gas Lantern");
		LanguageRegistry.addName(lanternGas5, "Gas Lantern");
		LanguageRegistry.addName(lanternGlowstone, "Lantern with Glowstone");
		LanguageRegistry.addName(gasPipeAir, "Gas Pipe");
		LanguageRegistry.addName(gasFurnaceIdle, "Gas Furnace");
		LanguageRegistry.addName(gasPump, "Gas Pump");
		LanguageRegistry.addName(gasTank, "Gas Tank");
		LanguageRegistry.addName(gasCollector, "Gas Collector");
		
		LanguageRegistry.addName(glowstoneShard, "Glowstone Shard");
		LanguageRegistry.addName(gasBottle, "Bottle of Gas");
		
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabGases", "en_US", "Glenn's Gases");
		
		EntityRegistry.registerGlobalEntityID(EntitySmallLightning.class, "smallLightning", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntitySmallLightning.class, "smallLightning", 127, this, 20, 1, false);
	
		EntityRegistry.registerGlobalEntityID(EntityGlowstoneShard.class, "glowstoneShard", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityGlowstoneShard.class, "glowstoneShard", 126, this, 20, 1, false);
		
		GameRegistry.registerTileEntity(TileEntityPump.class, "gasPump");
		GameRegistry.registerTileEntity(TileEntityGasCollector.class, "gasCollector");
		GameRegistry.registerTileEntity(TileEntityTank.class, "gasTank");
		GameRegistry.registerTileEntity(TileEntityGasFurnace.class, "gasFurnace");
		
		proxy.registerRenderers();
		
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
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
	
	/**
	 * Register a block as a gas igniting block, e.g. a block which can cause a gas to combust or explode
	 * @param blockID
	 */
	public static void registerIgnitionBlock(int blockID)
	{
		gasReactives[blockID] = true;
	}
	
	/**
	 * Unregister a block as a gas igniting block, e.g. a block which can cause a gas to combust or explode
	 * @param blockID
	 */
	public static void unregisterIgnitionBlock(int blockID)
	{
		gasReactives[blockID] = false;
	}
	
	/**
	 * Returns true if the block is a gas igniting block, e.g. a block which can cause a gas to combust or explode
	 * @param blockID
	 * @return isGasReactive
	 */
	public static boolean isIgnitionBlock(int blockID)
	{
		return gasReactives[blockID];
	}
	
	public static void registerReaction(Reaction reaction)
	{
		if(!reaction.isErroneous())
		{
			reactions.add(reaction);
		}
	}
	
	public static Reaction getReactionForBlocks(int block1ID, int block2ID)
	{
		for(Reaction reaction : reactions)
		{
			if(reaction.is(block1ID, block2ID)) return reaction;
		}
		
		return new ReactionEmpty();
	}
	
	public static int reverseDirection(int direction)
	{
		return (direction / 2) * 2 + 1 - direction % 2;
	}
	
	public static void addSpecialFurnaceRecipe(ItemStack ingredient, ItemStack result, int cookTime)
	{
		TileEntityGasFurnace.specialFurnaceRecipes.add(new SpecialFurnaceRecipe(ingredient, result, cookTime));
	}
}