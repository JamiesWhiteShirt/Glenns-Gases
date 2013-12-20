package glenn.gasesframework;

import glenn.gasesframework.TileEntityGasFurnace.SpecialFurnaceRecipe;
import glenn.gasesframework.client.RenderBlockGas;
import glenn.gasesframework.client.RenderBlockGasPipe;
import glenn.gasesframework.client.RenderBlockLantern;
import glenn.gasesframework.client.RenderBlockPump;
import glenn.gasesframework.client.RenderBlockTank;
import glenn.gasesframework.reaction.Reaction;
import glenn.gasesframework.reaction.ReactionEmpty;
import glenn.gasesframework.reaction.ReactionIgnition;
import glenn.gasesframework.util.QueuedLanternRecipe;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
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

/**
 * <b>Gases Framework</b>
 * <br>
 * Gases Framework provides support for simplified implementation of gases in Minecraft.
 * <br>
 * <br>
 * This piece of software can be distributed freely without permission from the author. The software must be unaltered.
 * This software will sometimes be updated, so it is recommended to keep an eye on the official download/source.
 * Backwards compatibility will maintained in most cases, but it is recommended to check for changes at least on a monthly basis.
 * <br>
 * Copyright 2013 Erlend Amdal
 * @author Glenn
 *
 */
@Mod(modid = "gasesFramework", name = "Gases Framework", version = GasesFramework.version, dependencies="required-after:gasesFrameworkCore")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GasesFramework
{
	// The instance of your mod that Forge uses.
	@Instance("gasesFramework")
	public static GasesFramework instance;
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "glenn.gasesframework.client.ClientProxy", serverSide = "glenn.gasesframework.CommonProxy")
	public static CommonProxy proxy;
	public static final GuiHandler guiHandler = new GuiHandler();
	
	public static final String version = "1.0.1";
	public static float gasExplosionFactor;
	public static int gasFurnaceHeatingSpeed;
	
	private static boolean preInited = false;
	private static ArrayList<QueuedLanternRecipe> queuedLanternRecipes = new ArrayList<QueuedLanternRecipe>();
	
	private static Configuration config;
	
	public static int renderBlockGasID;
	public static int renderBlockLanternID;
	public static int renderBlockGasPipeID;
	public static int renderBlockPumpID;
	public static int renderBlockTankID;
	
	public static ResourceLocation gasOverlayImage = new ResourceLocation("gasesframework:textures/misc/gas_overlay.png");
	public static ResourceLocation fireOverlayImage = new ResourceLocation("gasesframework:textures/misc/fire_overlay.png");
	
	private static boolean ENCOURAGE_MECHANICAL_ITEMS_VAR = false;
	private static boolean ENCOURAGE_LANTERNS_VAR = false;
	
	/**
	 * Standard gas material used by gas blocks.
	 */
	public static final Material gasMaterial = (new Material(MapColor.airColor)
	{
		public boolean isSolid()
	    {
	        return false;
	    }
		
		/**
	     * Returns if this material is considered solid or not
	     */
	    public boolean blocksMovement()
	    {
	        return false;
	    }
	}).setReplaceable();
	
	private static boolean gasReactives[] = new boolean[Block.blocksList.length];
	private static ArrayList<Reaction> reactions = new ArrayList<Reaction>();
	
	/**
	 * Included by default with Gases Framework.
	 */
	public static BlockGas gasSmoke;
	/**
	 * Included by default with Gases Framework.
	 */
	public static BlockGas gasFire;
	/**
	 * Included by default with Gases Framework.
	 */
	public static BlockGasPipe gasPipeSmoke;
	/**
	 * Included by default with Gases Framework.
	 */
	public static GasType gasTypeSmoke;
	/**
	 * Included by default with Gases Framework.
	 */
	public static GasType gasTypeFire;
	
	/**
	 * The item used for glass bottles containing gas. These bottles are registered automatically for each gas type created, unless it is specified as non-industrial.
	 */
	public static Item gasBottle;
	/**
	 * The item used for including gas sampling. Sub-types of the samplers are registered automatically for each gas type created, unless it is specified as non-industrial.
	 */
	public static Item gasSamplerIncluder;
	/**
	 * The item used for excluding gas sampling. Sub-types of the samplers are registered automatically for each gas type created, unless it is specified as non-industrial.
	 */
	public static Item gasSamplerExcluder;
	
	
	
	public static BlockGasPipe gasPipeAir;
	public static Block gasPump;
	public static Block gasTank;
	public static Block gasCollector;
	public static Block gasFurnaceIdle;
	public static Block gasFurnaceActive;
	
	public static int lanternEmptyID;
	public static BlockLantern lanternEmpty;
	public static BlockLantern lanternGasEmpty;
	public static BlockLantern lanternGas1;
	public static BlockLantern lanternGas2;
	public static BlockLantern lanternGas3;
	public static BlockLantern lanternGas4;
	public static BlockLantern lanternGas5;
	
	public static GasType gasTypeAir;
	
	/**
	 * The creative tab nameded Glenn's Gases. Has a lantern icon.
	 */
	public static CreativeTabs creativeTab = new CreativeTabs("tabGases")
	{
		public ItemStack getIconItemStack()
		{
			return new ItemStack(lanternEmpty, 1, 0);
		}
	};
	
	private void initBlocksAndItems()
	{
		GameRegistry.registerItem(gasBottle = (new ItemGasBottle(b("gasBottleID", 400))).setUnlocalizedName("gasBottle").setCreativeTab(creativeTab).setTextureName("gasesframework:gas_bottle"), "gasBottle");

		GameRegistry.registerBlock(gasSmoke = (BlockGas)(new BlockGas(a("gasSmokeID", 500))).setUnlocalizedName("gasSmoke").setCreativeTab(creativeTab), "gasSmoke");
		GameRegistry.registerBlock(gasFire = (BlockGas)(new BlockGasFire(a("gasFireID", 500))).setLightValue(0.5F).setUnlocalizedName("gasFire").setCreativeTab(creativeTab).setTextureName("gasesframework:gas_fire"), "gasFire");
		
		if(MECHANICAL_ITEMS_ENCOURAGED())
		{
			GameRegistry.registerItem(gasSamplerIncluder = (new ItemGasSampler(b("gasSamplerIncluderID", 401), false)).setUnlocalizedName("gasSamplerIncluder").setCreativeTab(creativeTab).setTextureName("gasesframework:sampler"), "gasSamplerIncluder");
			GameRegistry.registerItem(gasSamplerExcluder = (new ItemGasSampler(b("gasSamplerExcluderID", 402), true)).setUnlocalizedName("gasSamplerExcluder").setCreativeTab(creativeTab).setTextureName("gasesframework:sampler"), "gasSamplerExcluder");
			
			GameRegistry.registerBlock(gasPipeAir = (BlockGasPipe)new BlockGasPipe(a("gasPipeAirID", 525)).setCreativeTab(creativeTab).setUnlocalizedName("gasPipeEmpty"), "gasPipeAir");
			GameRegistry.registerBlock(gasPipeSmoke = (BlockGasPipe)new BlockGasPipe(a("gasPipeSmokeID", 526)).setUnlocalizedName("gasPipeSmoke"), "gasPipeSmoke");
			GameRegistry.registerBlock(gasPump = new BlockGasPump(a("gasPumpID", 540)).setHardness(2.5F).setCreativeTab(creativeTab).setUnlocalizedName("gasPump").setTextureName("gasesframework:pump"), "gasPump");
			GameRegistry.registerBlock(gasTank = new BlockGasTank(a("gasTankID", 541)).setHardness(3.5F).setCreativeTab(creativeTab).setUnlocalizedName("gasTank").setTextureName("gasesframework:tank"), "gasTank");
			GameRegistry.registerBlock(gasCollector = new BlockGasCollector(a("gasCollectorID", 542)).setHardness(2.5F).setCreativeTab(creativeTab).setUnlocalizedName("gasCollector").setTextureName("gasesframework:collector"), "gasCollector");
			GameRegistry.registerBlock(gasFurnaceIdle = (new BlockGasFurnace(a("gasFurnaceIdleID", 543), false)).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("gasFurnace").setCreativeTab(creativeTab), "gasFurnaceIdle");
			GameRegistry.registerBlock(gasFurnaceActive = (new BlockGasFurnace(a("gasFurnaceActiveID", 544), true)).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setLightValue(0.25F).setUnlocalizedName("gasFurnace"), "gasFurnaceActive");
		}

		gasTypeAir = new GasTypeAir();
		gasTypeSmoke = new GasType(gasSmoke, gasPipeSmoke, 1, "Smoke", 0x3F3F3F, 2, -16, Combustibility.NONE).setEffectRates(4, 4, 0);
		gasTypeFire = new GasTypeFire(gasFire, 11).setEvaporationRate(2);
		
		lanternEmptyID = a("lanternEmptyID", 550);
		GameRegistry.registerBlock(lanternEmpty = (BlockLantern)(new BlockLanternEmpty(lanternEmptyID)).setHardness(0.0F).setUnlocalizedName("lanternEmpty").setCreativeTab(creativeTab).setTextureName("gasesframework:lantern_empty"), "lanternEmpty");
		
		if(LANTERNS_ENCOURAGED())
		{
			GameRegistry.registerBlock(lanternGasEmpty = (BlockLantern)(new BlockLanternSpecial(a("lanternGasEmptyID", 552), 0, new ItemStack(Item.glassBottle), new ItemStack(Item.glassBottle), null)).setUnlocalizedName("lanternGasEmpty").setTextureName("gasesframework:lantern_gas0_off"), "lanternGasEmpty");
			GameRegistry.registerBlock(lanternGas1 = (BlockLantern)(new BlockLantern(a("lanternGas1ID", 553), Combustibility.CONTROLLABLE)).setLightValue(1.0F).setUnlocalizedName("lanternGas1").setTextureName("gasesframework:lantern_gas0_on"), "lanternGas1");
			GameRegistry.registerBlock(lanternGas2 = (BlockLantern)(new BlockLantern(a("lanternGas2ID", 555), Combustibility.FLAMMABLE)).setLightValue(1.0F).setUnlocalizedName("lanternGas2").setTextureName("gasesframework:lantern_gas0_on"), "lanternGas2");
			GameRegistry.registerBlock(lanternGas3 = (BlockLantern)(new BlockLantern(a("lanternGas3ID", 557), Combustibility.HIGHLY_FLAMMABLE)).setLightValue(1.0F).setUnlocalizedName("lanternGas3").setTextureName("gasesframework:lantern_gas0_on"), "lanternGas3");
			GameRegistry.registerBlock(lanternGas4 = (BlockLantern)(new BlockLantern(a("lanternGas4ID", 558), Combustibility.EXPLOSIVE)).setLightValue(1.0F).setUnlocalizedName("lanternGas4").setTextureName("gasesframework:lantern_gas0_on"), "lanternGas4");
			GameRegistry.registerBlock(lanternGas5 = (BlockLantern)(new BlockLantern(a("lanternGas5ID", 559), Combustibility.HIGHLY_EXPLOSIVE)).setLightValue(1.0F).setUnlocalizedName("lanternGas5").setTextureName("gasesframework:lantern_gas0_on"), "lanternGas5");
		}
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		initBlocksAndItems();
		
		gasExplosionFactor = Float.parseFloat(config.get("gases", "gasExplosionFactor", 2.5F).getString());
		gasFurnaceHeatingSpeed = config.get("other", "gasFurnaceHeatingSpeed", 2).getInt();
		String[] additionalIgnitionBlocks = config.get("other", "additionalIgnitionBlocks", new String[0]).getStringList();
		for(String s : additionalIgnitionBlocks)
		{
			registerIgnitionBlock(Integer.parseInt(s));
		}
		
		config.save();
		
		preInited = true;
		for(QueuedLanternRecipe recipe : queuedLanternRecipes)
		{
			recipe.register();
		}
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
		
		GameRegistry.addRecipe(new ItemStack(lanternEmpty, 4), "I", "G", 'I', Item.ingotIron, 'G', Block.glass);
		LanguageRegistry.addName(lanternEmpty, "Lantern");
		
		if(LANTERNS_ENCOURAGED())
		{
			LanguageRegistry.addName(lanternGasEmpty, "Empty Gas Lantern");
			LanguageRegistry.addName(lanternGas1, "Gas Lantern");
			LanguageRegistry.addName(lanternGas2, "Gas Lantern");
			LanguageRegistry.addName(lanternGas3, "Gas Lantern");
			LanguageRegistry.addName(lanternGas4, "Gas Lantern");
			LanguageRegistry.addName(lanternGas5, "Gas Lantern");
		}
		
		if(MECHANICAL_ITEMS_ENCOURAGED())
		{
			GameRegistry.addRecipe(new ItemStack(gasPipeAir, 24), "III", 'I', Item.ingotIron);
			GameRegistry.addRecipe(new ItemStack(gasPump), " I ", "PRP", " I ", 'I', Item.ingotIron, 'P', gasPipeAir, 'R', Item.redstone);
			GameRegistry.addRecipe(new ItemStack(gasCollector), " P ", "PUP", " P ", 'U', gasPump, 'P', gasPipeAir);
			GameRegistry.addRecipe(new ItemStack(gasTank), "IPI", "P P", "IPI", 'I', Item.ingotIron, 'P', gasPipeAir);
			GameRegistry.addRecipe(new ItemStack(gasFurnaceIdle), " I ", "IFI", " I ", 'I', Item.ingotIron, 'F', Block.furnaceIdle);
			GameRegistry.addShapelessRecipe(new ItemStack(gasSamplerExcluder), new ItemStack(Item.glassBottle), new ItemStack(Item.dyePowder, 1, 0));
			GameRegistry.addShapelessRecipe(new ItemStack(gasSamplerIncluder), new ItemStack(Item.glassBottle), new ItemStack(Item.dyePowder, 1, 15));
			
			LanguageRegistry.addName(gasPipeAir, "Gas Pipe");
			LanguageRegistry.addName(gasFurnaceIdle, "Gas Furnace");
			LanguageRegistry.addName(gasPump, "Gas Pump");
			LanguageRegistry.addName(gasTank, "Gas Tank");
			LanguageRegistry.addName(gasCollector, "Gas Collector");
			
			GameRegistry.registerTileEntity(TileEntityPump.class, "gasPump");
			GameRegistry.registerTileEntity(TileEntityGasCollector.class, "gasCollector");
			GameRegistry.registerTileEntity(TileEntityTank.class, "gasTank");
			GameRegistry.registerTileEntity(TileEntityGasFurnace.class, "gasPoweredFurnace");
		}
		
		registerIgnitionBlock(Block.torchWood.blockID);
		registerIgnitionBlock(Block.fire.blockID);
		registerIgnitionBlock(gasFire.blockID);
		
		registerReaction(new ReactionIgnition());
		
		LanguageRegistry.addName(gasSmoke, "Smoke");
		LanguageRegistry.addName(gasFire, "Ignited Gas");
		LanguageRegistry.addName(gasBottle, "Bottle of Gas");
		
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabGases", "en_US", "Glenn's Gases");
		
		proxy.registerRenderers();
		
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	/**
	 * Tidier code is better, am I right?
	 * @param name
	 * @param def
	 * @return
	 */
	private static int a(String name, int def)
	{
		return config.getBlock(name, def).getInt();
	}
	
	/**
	 * Tidier code is better, am I right?
	 * @param name
	 * @param def
	 * @return
	 */
	private static int b(String name, int def)
	{
		return config.getItem(name, def).getInt();
	}
	
	/**
	 * Register a block as a gas igniting block, e.g. a block which can cause a gas to combust or explode.
	 * @param blockID
	 */
	public static void registerIgnitionBlock(int blockID)
	{
		gasReactives[blockID] = true;
	}
	
	/**
	 * Unregister a block as a gas igniting block, e.g. a block which can cause a gas to combust or explode.
	 * @param blockID
	 */
	public static void unregisterIgnitionBlock(int blockID)
	{
		gasReactives[blockID] = false;
	}
	
	/**
	 * Returns true if the block is a gas igniting block, e.g. a block which can cause a gas to combust or explode.
	 * @param blockID
	 * @return isGasReactive
	 */
	public static boolean isIgnitionBlock(int blockID)
	{
		return gasReactives[blockID];
	}
	
	/**
	 * Registers a custom gas reaction.
	 * @param reaction
	 */
	public static void registerReaction(Reaction reaction)
	{
		if(!reaction.isErroneous())
		{
			reactions.add(reaction);
		}
	}
	
	/**
	 * Gets the reaction between 2 blocks from their IDs. Returns an empty reaction if it doesn't exist (not null)
	 * @param block1ID
	 * @param block2ID
	 * @return
	 */
	public static Reaction getReactionForBlocks(int block1ID, int block2ID)
	{
		for(Reaction reaction : reactions)
		{
			if(reaction.is(block1ID, block2ID)) return reaction;
		}
		
		return new ReactionEmpty();
	}
	
	/**
	 * Returns the reverse direction index of a direction index. Used by most blocks in Gases Framework.
	 */
	public static int reverseDirection(int direction)
	{
		return (direction / 2) * 2 + 1 - direction % 2;
	}
	
	/**
	 * Adds a special furnace recipe which can be used in a gas furnace. Special furnace recipes are notably different.
	 * @param ingredient - The item to be smelted. Can have a stack size larger than 1.
	 * @param result - The result of the smelting action.
	 * @param cookTime - The time it takes to complete the smelting action. Default is 200.
	 */
	public static void addSpecialFurnaceRecipe(ItemStack ingredient, ItemStack result, int cookTime)
	{
		if(!MECHANICAL_ITEMS_ENCOURAGED())
		{
			throw new RuntimeException("A furnace recipe was registered while mechanical items were not encouraged. Please put GasesFramework.ENCOURAGE_MECHANICAL_ITEMS() in the mod's static initalizer.");
		}
		TileEntityGasFurnace.specialFurnaceRecipes.add(new SpecialFurnaceRecipe(ingredient, result, cookTime));
	}
	
	/**
	 * Ignore, used by the Gases Framework only.
	 * @param result
	 * @param ingredient
	 */
	public static void queueLanternRecipe(ItemStack result, ItemStack ingredient)
	{
		if(!LANTERNS_ENCOURAGED())
		{
			throw new RuntimeException("A lantern recipe was registered while lanterns were not encouraged. Please put GasesFramework.ENCOURAGE_LANTERNS() in the mod's static initalizer.");
		}
		if(preInited)
		{
			GameRegistry.addShapelessRecipe(result, new ItemStack(lanternEmpty), ingredient);
		}
		else
		{
			queuedLanternRecipes.add(new QueuedLanternRecipe(result, ingredient));
		}
	}
	
	/**
	 * If your mod uses the mechanical items of Gases Framework, you must call this in your mod's static constructor.
	 */
	public static void ENCOURAGE_MECHANICAL_ITEMS()
	{
		ENCOURAGE_MECHANICAL_ITEMS_VAR = true;
	}
	
	/**
	 * If your mod uses lanterns in Gases Framework, you must call this in your mod's static constructor.
	 */
	public static void ENCOURAGE_LANTERNS()
	{
		ENCOURAGE_LANTERNS_VAR = true;
	}
	
	/**
	 * Returns whether mechanical items are encouraged or not.
	 * @return
	 */
	public static boolean MECHANICAL_ITEMS_ENCOURAGED()
	{
		return ENCOURAGE_MECHANICAL_ITEMS_VAR;
	}
	
	/**
	 * Returns whether lanterns are encouraged or not.
	 * @return
	 */
	public static boolean LANTERNS_ENCOURAGED()
	{
		return ENCOURAGE_LANTERNS_VAR;
	}
}