package glenn.gases;

import java.util.ArrayList;

import glenn.gases.client.RenderBlockGas;
import glenn.gases.client.RenderBlockLantern;
import glenn.gases.client.RenderSmallLightning;
import glenn.gases.client.SoundBus;
import glenn.gases.reaction.Reaction;
import glenn.gases.reaction.ReactionBlockReplacement;
import glenn.gases.reaction.ReactionCorrosion;
import glenn.gases.reaction.ReactionEmpty;
import glenn.gases.reaction.ReactionGlowstoneShard;
import glenn.gases.reaction.ReactionIgnition;
import net.minecraft.block.Block;
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
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "Gases", name = "Gases", version = "1.3.2")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class Gases
{
	// The instance of your mod that Forge uses.
	@Instance("GlennsGases")
	public static Gases instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "glenn.gases.client.ClientProxy", serverSide = "glenn.gases.CommonProxy")
	public static CommonProxy proxy;
	
	private static Configuration config;
	public static boolean generateGreenGas;
	public static boolean generateRedGas;
	public static boolean generateElectricGas;
	public static boolean generateCorrosiveGas;
	public static float maxHardnessForCorrosion;
	public static float gasExplosionFactor;

	public static int renderBlockGasID;
	public static int renderBlockLanternID;

    public static final Material gasMaterial = (new Material(MapColor.airColor)).setReplaceable();
    public static final DamageSource lightningDamageSource = new DamageSourceLightning("lightning");
    
    
    
    private static boolean gasReactives[] = new boolean[Block.blocksList.length];
    private static int lanternRecipes[] = new int[Item.itemsList.length];
    private static ArrayList<Reaction> reactions = new ArrayList<Reaction>();
    
    

    public static Item gasBottle;
    public static Item glowstoneShard;
    
    public static Block gasSmoke;
    public static Block gasSteam;
    public static Block gasRisingFlammable;
    public static Block gasFallingExplosive;
    public static Block gasVoid;
    public static Block gasElectric;
    public static Block gasCorrosive;

	
	/**
	 * The empty gas pipe block.
	 */
	public static Block gasPipeEmpty;
    public static Block gasPipeSmoke;
    
    public static int lanternEmptyID;
    public static Block lanternEmpty;
    public static Block lanternTorch;
    public static Block lanternGasEmpty;
    public static Block lanternGas;
    public static Block lanternGlowstone;

    private void initBlocksAndItems()
    {
    	GameRegistry.registerItem(gasBottle = (new Item(b("gasBottleID", 400))).setUnlocalizedName("gasBottle").setCreativeTab(CreativeTabs.tabMisc).func_111206_d("gases:bottle_gas"), "gasBottle");
    	GameRegistry.registerItem(glowstoneShard = (new ItemGlowstoneShard(b("glowstoneShardID", 401))).setUnlocalizedName("glowstoneShard").setCreativeTab(CreativeTabs.tabMisc).func_111206_d("gases:glowstone_shard"), "glowstoneShard");

    	GameRegistry.registerBlock(gasSmoke = (new BlockGas(a("gasSmokeID", 500), 0x3F3F3F, 2, -16, Combustibility.NONE)).setEffectRates(4, 4, 0).setHardness(0.0F).setUnlocalizedName("gasSmoke").setCreativeTab(CreativeTabs.tabMisc).func_111022_d("gases:gas"), "gasSmoke");
    	GameRegistry.registerBlock(gasSteam = (new BlockGasHazard(a("gasSteamID", 501), 0xFFFFFF, 0, -8, Combustibility.NONE, 2.0F)).setEvaporation(4).setHardness(0.0F).setUnlocalizedName("gasSteam").setCreativeTab(CreativeTabs.tabMisc).func_111022_d("gases:gas"), "gasSteam");
    	GameRegistry.registerBlock(gasRisingFlammable = (new BlockGas(a("gasRisingFlammableID", 502), 0x6F7F6F, 1, -12, Combustibility.FLAMMABLE)).setEffectRates(2, 2, 0).setHardness(0.0F).setUnlocalizedName("gasRisingFlammable").setCreativeTab(CreativeTabs.tabMisc).func_111022_d("gases:gas"), "gasRisingFlammable");
    	GameRegistry.registerBlock(gasFallingExplosive = (new BlockGas(a("gasFallingExplosiveID", 503), 0x7F4F4F, 3, 4, Combustibility.EXPLOSIVE)).setEffectRates(1, 2, 0).setHardness(0.0F).setUnlocalizedName("gasFallingExplosive").setCreativeTab(CreativeTabs.tabMisc).func_111022_d("gases:gas"), "gasFallingExplosive");
    	GameRegistry.registerBlock(gasVoid = (new BlockGasVoid(a("gasVoidID", 504), 0x1F1F1F, 4, 8, Combustibility.NONE, 1.0F)).setEffectRates(20, 4, 0).setHardness(0.0F).setUnlocalizedName("gasVoid").setCreativeTab(CreativeTabs.tabMisc).func_111022_d("gases:gas"), "gasVoid");
    	GameRegistry.registerBlock(gasElectric = (new BlockGasElectric(a("gasElectricID", 505), 0x1F7F7F, 0, 0, Combustibility.NONE)).setEffectRates(4, 2, 0).setHardness(0.0F).setUnlocalizedName("gasElectric").setCreativeTab(CreativeTabs.tabMisc).func_111022_d("gases:gas_special"), "gasElectric");
    	GameRegistry.registerBlock(gasCorrosive = (new BlockGasHazard(a("gasCorrosiveID", 506), 0x1F1FFF, 0, 0, Combustibility.NONE, 0.5F)).setEffectRates(4, 2, 0).setHardness(0.0F).setUnlocalizedName("gasCorrosive").setCreativeTab(CreativeTabs.tabMisc).func_111022_d("gases:gas_special"), "gasCorrosive");
    	
    	GameRegistry.registerBlock(gasPipeEmpty = (BlockGasPipe)(new BlockGasPipe(a("gasPipeEmpty", 525), (BlockGas)gasSmoke)).setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("gasPipeEmpty"), "gasPipeEmpty");
    	GameRegistry.registerBlock(gasPipeSmoke = (new BlockGasPipe(a("gasPipeSmokeID", 526), (BlockGas)gasSmoke)).setUnlocalizedName("gasPipeSmoke"), "gasPipeSmoke");
    	
    	lanternEmptyID = a("lanternEmptyID", 550);
    	GameRegistry.registerBlock(lanternEmpty = (new BlockLantern(lanternEmptyID, 0, 0, 0, -1)).setHardness(0.0F).setUnlocalizedName("lanternEmpty").setCreativeTab(CreativeTabs.tabDecorations).func_111022_d("gases:lantern_empty"), "lanternEmpty");
    	GameRegistry.registerBlock(lanternTorch = (new BlockLantern(a("lanternTorchID", 551), Block.torchWood.blockID, Block.torchWood.blockID, 0, -1)).setLightValue(10.0F / 16.0F).setUnlocalizedName("lanternTorch").func_111022_d("gases:lantern_torch"), "lanternTorch");
    	GameRegistry.registerBlock(lanternGasEmpty = (new BlockLantern(a("lanternGasEmpty", 552), Item.glassBottle.itemID, Item.glassBottle.itemID, 0, -1)).setUnlocalizedName("lanternGasEmpty").func_111022_d("gases:lantern_gas0_off"), "lanternGasEmpty");
    	GameRegistry.registerBlock(lanternGas = (new BlockLantern(a("lanternGas", 553), gasBottle.itemID, Item.glassBottle.itemID, 1, lanternGasEmpty.blockID)).setLightValue(1.0F).setUnlocalizedName("lanternGas").func_111022_d("gases:lantern_gas0_on"), "lanternGas");
    	GameRegistry.registerBlock(lanternGlowstone = (new BlockLantern(a("lanternGlowstone", 554), Item.glowstone.itemID, Item.glowstone.itemID, 0, -1)).setLightValue(1.0F).setUnlocalizedName("lanternGlowstone").func_111022_d("gases:lantern_glowstone"), "lanternGlowstone");
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
		
		config.save();
		
		if(FMLCommonHandler.instance().getSide().isClient())
		{
			MinecraftForge.EVENT_BUS.register(new SoundBus());
		}
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		renderBlockGasID = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(renderBlockGasID, new RenderBlockGas());
    	renderBlockLanternID = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(renderBlockLanternID, new RenderBlockLantern());

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
}