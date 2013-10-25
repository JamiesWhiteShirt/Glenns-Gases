package glenn.gases;

import java.util.ArrayList;

import glenn.gases.client.RenderBlockGas;
import glenn.gases.client.RenderBlockGasPipe;
import glenn.gases.client.RenderBlockLantern;
import glenn.gases.client.RenderBlockPump;
import glenn.gases.client.RenderBlockTank;
import glenn.gases.client.RenderSmallLightning;
import glenn.gases.client.SoundBus;
import glenn.gases.reaction.Reaction;
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
	public static boolean generateElectricGas;
	public static boolean generateCorrosiveGas;
	public static float maxHardnessForCorrosion;
	public static float gasExplosionFactor;
	public static boolean enableUpdateCheck;
	
	public static int renderBlockGasID;
	public static int renderBlockLanternID;
	public static int renderBlockGasPipeID;
	public static int renderBlockPumpID;
	public static int renderBlockTankID;
	
	public static final Material gasMaterial = (new Material(MapColor.airColor)).setReplaceable();
	public static final DamageSource lightningDamageSource = new DamageSourceLightning("lightning");
	
	
	
	private static boolean gasReactives[] = new boolean[Block.blocksList.length];
	private static int lanternRecipes[] = new int[Item.itemsList.length];
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
	
	public static Block gasPump;
	public static Block gasTank;
	public static Block gasTank2;
	public static Block gasFurnaceIdle;
	public static Block gasFurnaceActive;
	
	public static int lanternEmptyID;
	public static Block lanternEmpty;
	public static Block lanternTorch;
	public static Block lanternGasEmpty;
	public static Block lanternGas;
	public static Block lanternGlowstone;
	
	public static GasType gasTypeAir;
	public static GasType gasTypeSmoke;
	public static GasType gasTypeSteam;
	public static GasType gasTypeRisingFlammable;
	public static GasType gasTypeFallingExplosive;
	public static GasType gasTypeVoid;
	public static GasType gasTypeElectric;
	public static GasType gasTypeCorrosive;
	
	public static UtilMethods util = new UtilMethods();
	
	private void initBlocksAndItems()
	{
		GameRegistry.registerItem(gasBottle = (new Item(b("gasBottleID", 400))).setUnlocalizedName("gasBottle").setCreativeTab(CreativeTabs.tabMisc).setTextureName("gases:bottle_gas"), "gasBottle");
		GameRegistry.registerItem(glowstoneShard = (new ItemGlowstoneShard(b("glowstoneShardID", 401))).setUnlocalizedName("glowstoneShard").setCreativeTab(CreativeTabs.tabMisc).setTextureName("gases:glowstone_shard"), "glowstoneShard");
		GameRegistry.registerItem(gasSamplerIncluder = (new ItemGasSampler(b("gasSamplerIncluderID", 402), false)).setUnlocalizedName("gasSamplerIncluder").setCreativeTab(CreativeTabs.tabMisc).setTextureName("gases:sampler"), "gasSamplerIncluder");
		GameRegistry.registerItem(gasSamplerExcluder = (new ItemGasSampler(b("gasSamplerExcluderID", 403), true)).setUnlocalizedName("gasSamplerExcluder").setCreativeTab(CreativeTabs.tabMisc).setTextureName("gases:sampler"), "gasSamplerExcluder");
		
		GameRegistry.registerBlock(gasSmoke = (BlockGas)(new BlockGas(a("gasSmokeID", 500))).setHardness(0.0F).setUnlocalizedName("gasSmoke").setCreativeTab(CreativeTabs.tabMisc).setTextureName("gases:gas"), "gasSmoke");
		GameRegistry.registerBlock(gasSteam = (BlockGas)(new BlockGas(a("gasSteamID", 501))).setHardness(0.0F).setUnlocalizedName("gasSteam").setCreativeTab(CreativeTabs.tabMisc).setTextureName("gases:gas"), "gasSteam");
		GameRegistry.registerBlock(gasRisingFlammable = (BlockGas)(new BlockGas(a("gasRisingFlammableID", 502))).setHardness(0.0F).setUnlocalizedName("gasRisingFlammable").setCreativeTab(CreativeTabs.tabMisc).setTextureName("gases:gas"), "gasRisingFlammable");
		GameRegistry.registerBlock(gasFallingExplosive = (BlockGas)(new BlockGas(a("gasFallingExplosiveID", 503))).setHardness(0.0F).setUnlocalizedName("gasFallingExplosive").setCreativeTab(CreativeTabs.tabMisc).setTextureName("gases:gas"), "gasFallingExplosive");
		GameRegistry.registerBlock(gasVoid = (BlockGas)(new BlockGasVoid(a("gasVoidID", 504))).setHardness(0.0F).setUnlocalizedName("gasVoid").setCreativeTab(CreativeTabs.tabMisc).setTextureName("gases:gas"), "gasVoid");
		GameRegistry.registerBlock(gasElectric = (BlockGas)(new BlockGasElectric(a("gasElectricID", 505))).setHardness(0.0F).setUnlocalizedName("gasElectric").setCreativeTab(CreativeTabs.tabMisc).setTextureName("gases:gas_special"), "gasElectric");
		GameRegistry.registerBlock(gasCorrosive = (BlockGas)(new BlockGas(a("gasCorrosiveID", 506))).setHardness(0.0F).setUnlocalizedName("gasCorrosive").setCreativeTab(CreativeTabs.tabMisc).setTextureName("gases:gas_special"), "gasCorrosive");
		
		GameRegistry.registerBlock(gasPipeAir = (BlockGasPipe)new BlockGasPipe(a("gasPipeAirID", 525)).setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("gasPipeEmpty"), "gasPipeAir");
		GameRegistry.registerBlock(gasPipeSmoke = (BlockGasPipe)new BlockGasPipe(a("gasPipeSmokeID", 526)).setUnlocalizedName("gasPipeSmoke"), "gasPipeSmoke");
		GameRegistry.registerBlock(gasPipeSteam = (BlockGasPipe)new BlockGasPipe(a("gasPipeSteamID", 527)).setUnlocalizedName("gasPipeSteam"), "gasPipeSteam");
		GameRegistry.registerBlock(gasPipeRisingFlammable = (BlockGasPipe)new BlockGasPipe(a("gasPipeRisingFlammableID", 528)).setUnlocalizedName("gasPipeRisingFlammable"), "gasPipeRisingFlammable");
		GameRegistry.registerBlock(gasPipeFallingExplosive = (BlockGasPipe)new BlockGasPipe(a("gasPipeFallingExplosiveID", 529)).setUnlocalizedName("gasPipeFallingExplosive"), "gasPipeFallingExplosive");
		GameRegistry.registerBlock(gasPipeVoid = (BlockGasPipe)new BlockGasPipe(a("gasPipeVoidID", 530)).setUnlocalizedName("gasPipeVoid"), "gasPipeVoid");
		GameRegistry.registerBlock(gasPipeElectric = (BlockGasPipe)new BlockGasPipe(a("gasPipeElectricID", 531)).setUnlocalizedName("gasPipeElectric"), "gasPipeElectric");
		GameRegistry.registerBlock(gasPipeCorrosive = (BlockGasPipe)new BlockGasPipe(a("gasPipeCorrosiveID", 532)).setUnlocalizedName("gasPipeCorrosive"), "gasPipeCorrosive");
	
		GameRegistry.registerBlock(gasPump = new BlockPump(a("gasPump", 540)).setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("gas_pump").setTextureName("gases:pump"), "gasPump");
		GameRegistry.registerBlock(gasTank = new BlockGasTank(a("gasTank", 541)).setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("gas_tank").setTextureName("gases:tank"), "gasTank");
		GameRegistry.registerBlock(gasTank = new BlockGasTank2(a("gasTank2", 542)).setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("gas_tank2").setTextureName("gases:tank"), "gasTank2");
		GameRegistry.registerBlock(gasFurnaceIdle = (new BlockGasFurnace(a("gasFurnaceIdle", 543), false)).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("gas_furnace").setCreativeTab(CreativeTabs.tabDecorations), "gasFurnaceIdle");
		GameRegistry.registerBlock(gasFurnaceActive = (new BlockGasFurnace(a("gasFurnaceActive", 544), true)).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setLightValue(0.875F).setUnlocalizedName("gas_furnace"), "gasFurnaceActive");
		
		lanternEmptyID = a("lanternEmptyID", 550);
		GameRegistry.registerBlock(lanternEmpty = (new BlockLantern(lanternEmptyID, 0, 0, 0, -1)).setHardness(0.0F).setUnlocalizedName("lanternEmpty").setCreativeTab(CreativeTabs.tabDecorations).setTextureName("gases:lantern_empty"), "lanternEmpty");
		GameRegistry.registerBlock(lanternTorch = (new BlockLantern(a("lanternTorchID", 551), Block.torchWood.blockID, Block.torchWood.blockID, 0, -1)).setLightValue(10.0F / 16.0F).setUnlocalizedName("lanternTorch").setTextureName("gases:lantern_torch"), "lanternTorch");
		GameRegistry.registerBlock(lanternGasEmpty = (new BlockLantern(a("lanternGasEmpty", 552), Item.glassBottle.itemID, Item.glassBottle.itemID, 0, -1)).setUnlocalizedName("lanternGasEmpty").setTextureName("gases:lantern_gas0_off"), "lanternGasEmpty");
		GameRegistry.registerBlock(lanternGas = (new BlockLantern(a("lanternGas", 553), gasBottle.itemID, Item.glassBottle.itemID, 1, lanternGasEmpty.blockID)).setLightValue(1.0F).setUnlocalizedName("lanternGas").setTextureName("gases:lantern_gas0_on"), "lanternGas");
		GameRegistry.registerBlock(lanternGlowstone = (new BlockLantern(a("lanternGlowstone", 554), Item.glowstone.itemID, Item.glowstone.itemID, 0, -1)).setLightValue(1.0F).setUnlocalizedName("lanternGlowstone").setTextureName("gases:lantern_glowstone"), "lanternGlowstone");
		
		gasTypeAir = new GasType(null, gasPipeAir, null, 0, "Air", 0, 0, 0);
		gasTypeSmoke = new GasType(gasSmoke, gasPipeSmoke, null, 1, "Smoke", 0x3F3F3F, 2, -16).setEffectRates(4, 4, 0);
		gasTypeSteam = new GasType(gasSteam, gasPipeSteam, null, 2, "Steam", 0xFFFFFF, 0, -8).setDamage(2.0F).setEvaporationRate(1);
		gasTypeRisingFlammable = new GasType(gasRisingFlammable, gasPipeRisingFlammable, new ItemStack(gasBottle), 3, "Green gas", 0x6F7F6F, 1, -12).setCombustibility(Combustibility.FLAMMABLE).setEffectRates(2, 2, 0);
		gasTypeFallingExplosive = new GasType(gasFallingExplosive, gasPipeFallingExplosive, new ItemStack(gasBottle), 4, "Red gas", 0x7F4F4F, 3, 4).setCombustibility(Combustibility.EXPLOSIVE).setEffectRates(1, 2, 0);
		gasTypeVoid = new GasType(gasVoid, gasPipeVoid, null, 5, "Void gas", 0x1F1F1F, 4, 8).setEffectRates(20, 4, 0).setDamage(1.0F);
		gasTypeElectric = new GasType(gasElectric, gasPipeElectric, null, 6, "Electric gas", 0x1F7F7F, 0, 0).setEffectRates(4, 2, 0);
		gasTypeCorrosive = new GasType(gasCorrosive, gasPipeCorrosive, null, 7, "Corrosive gas", 0x1F1FDF, 0, 0).setEffectRates(4, 2, 0).setDamage(0.5F);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		initBlocksAndItems();
		
		generateGreenGas = config.get("world", "generateGreenGas", true).getBoolean(true);
		generateRedGas = config.get("world", "generateRedGas", true).getBoolean(true);
		generateElectricGas = config.get("world", "generateElectricGas", true).getBoolean(true);
		generateCorrosiveGas = config.get("world", "generateCorrosiveGas", true).getBoolean(true);
		maxHardnessForCorrosion = Float.parseFloat(config.get("gases", "maxHardnessForCorrosion", 2.0F).getString());
		gasExplosionFactor = Float.parseFloat(config.get("gases", "gasExplosionFactor", 2.5F).getString());
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
	
		GameRegistry.addRecipe(new ItemStack(lanternEmpty, 4), new Object[] {"I", "G", 'I', Item.ingotIron, 'G', Block.glass});
		
		registerIgnitionBlock(Block.torchWood.blockID);
		registerIgnitionBlock(Block.fire.blockID);
		
		registerReaction(new ReactionIgnition());
		registerReaction(new ReactionGlowstoneShard(gasElectric, gasCorrosive));
		registerReaction(new ReactionCorrosion((BlockGas)gasCorrosive));
		
		registerLanternRecipe(new ItemStack(Block.torchWood), lanternTorch.blockID);
		registerLanternRecipe(new ItemStack(gasBottle), lanternGas.blockID);
		registerLanternRecipe(new ItemStack(Item.glassBottle), lanternGasEmpty.blockID);
		registerLanternRecipe(new ItemStack(Item.glowstone), lanternGlowstone.blockID);
		
		LanguageRegistry.addName(gasBottle, "Bottle of Gas");
		LanguageRegistry.addName(glowstoneShard, "Glowstone Shard");
		LanguageRegistry.addName(gasSmoke, "Smoke");
		LanguageRegistry.addName(gasSteam, "Steam");
		LanguageRegistry.addName(gasRisingFlammable, "Green Gas");
		LanguageRegistry.addName(gasFallingExplosive, "Red Gas");
		LanguageRegistry.addName(gasVoid, "Void Gas");
		LanguageRegistry.addName(gasElectric, "Electric Gas");
		LanguageRegistry.addName(gasCorrosive, "Corrosive Gas");
		LanguageRegistry.addName(lanternEmpty, "Lantern");
		LanguageRegistry.addName(lanternTorch, "Lantern with Torch");
		LanguageRegistry.addName(lanternGasEmpty, "Empty Gas Lantern");
		LanguageRegistry.addName(lanternGas, "Gas Lantern");
		LanguageRegistry.addName(lanternGlowstone, "Lantern with Glowstone");
		
		EntityRegistry.registerGlobalEntityID(EntitySmallLightning.class, "smallLightning", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntitySmallLightning.class, "smallLightning", 127, this, 20, 1, false);
	
		EntityRegistry.registerGlobalEntityID(EntityGlowstoneShard.class, "glowstoneShard", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityGlowstoneShard.class, "glowstoneShard", 126, this, 20, 1, false);
		
		GameRegistry.registerTileEntity(TileEntityPump.class, "gasPump");
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
	
	/**
	 * Registers a recipe for a lantern, allowing the lantern to be created with a shapeless crafting recipe or by right clicking a lantern with specified item
	 * @param placedItem - The the item/block used in the recipe along with the lantern
	 * @param lanternBlockID - The ID of the lantern block which can be made with the placed item
	 */
	public static void registerLanternRecipe(ItemStack placedItem, int lanternBlockID)
	{
		lanternRecipes[placedItem.itemID] = lanternBlockID;
		GameRegistry.addShapelessRecipe(new ItemStack(Block.blocksList[lanternBlockID]), new Object[] {lanternEmpty, placedItem});
	}
	
	/**
	 * Gets the lantern block ID for the specified item/block ID
	 * @param id - The ID of the item/block
	 * @return lanternBlockID
	 */
	public static int getLanternForID(int id)
	{
		if(id > lanternRecipes.length)
		{
			return 0;
		}
		return lanternRecipes[id];
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
}