package com.kingparity.betterpets.core;

import com.kingparity.betterpets.BetterPetMod;
import com.kingparity.betterpets.block.WaterCollectorBlock;
import com.kingparity.betterpets.block.WaterFilterBlock;
import com.kingparity.betterpets.names.BlockNames;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Function;

public class ModBlocks
{
    public static final Material WOOD_MATERIAL = new Material(MaterialColor.WOOD, false, false, false, false, true, true, false, PushReaction.NORMAL);
    public static final Material STONE_MATERIAL = new Material(MaterialColor.STONE, false, false, false, false, false, true, false, PushReaction.NORMAL);

    public static final Block.Properties WOOD = Block.Properties.create(WOOD_MATERIAL).hardnessAndResistance(0.5F).sound(SoundType.WOOD);
    public static final Block.Properties STONE = Block.Properties.create(STONE_MATERIAL).hardnessAndResistance(1.0F).sound(SoundType.STONE);
    public static final Block.Properties FLUID = Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops();

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Reference.ID);

    //public static final RegistryObject<Block> PET_RESOURCES_CRAFTER = null;

    public static final RegistryObject<Block> WATER_COLLECTOR_OAK = register(BlockNames.WATER_COLLECTOR_OAK, new WaterCollectorBlock(WOOD));
    public static final RegistryObject<Block> WATER_COLLECTOR_SPRUCE = register(BlockNames.WATER_COLLECTOR_SPRUCE, new WaterCollectorBlock(WOOD));
    public static final RegistryObject<Block> WATER_COLLECTOR_BIRCH = register(BlockNames.WATER_COLLECTOR_BIRCH, new WaterCollectorBlock(WOOD));
    public static final RegistryObject<Block> WATER_COLLECTOR_JUNGLE = register(BlockNames.WATER_COLLECTOR_JUNGLE, new WaterCollectorBlock(WOOD));
    public static final RegistryObject<Block> WATER_COLLECTOR_ACACIA = register(BlockNames.WATER_COLLECTOR_ACACIA, new WaterCollectorBlock(WOOD));
    public static final RegistryObject<Block> WATER_COLLECTOR_DARK_OAK = register(BlockNames.WATER_COLLECTOR_DARK_OAK, new WaterCollectorBlock(WOOD));
    public static final RegistryObject<Block> WATER_COLLECTOR_STONE = register(BlockNames.WATER_COLLECTOR_STONE, new WaterCollectorBlock(STONE));
    public static final RegistryObject<Block> WATER_COLLECTOR_GRANITE = register(BlockNames.WATER_COLLECTOR_GRANITE, new WaterCollectorBlock(STONE));
    public static final RegistryObject<Block> WATER_COLLECTOR_DIORITE = register(BlockNames.WATER_COLLECTOR_DIORITE, new WaterCollectorBlock(STONE));
    public static final RegistryObject<Block> WATER_COLLECTOR_ANDESITE = register(BlockNames.WATER_COLLECTOR_ANDESITE, new WaterCollectorBlock(STONE));
    public static final RegistryObject<Block> WATER_COLLECTOR_STRIPPED_OAK = register(BlockNames.WATER_COLLECTOR_STRIPPED_OAK, new WaterCollectorBlock(WOOD));
    public static final RegistryObject<Block> WATER_COLLECTOR_STRIPPED_SPRUCE = register(BlockNames.WATER_COLLECTOR_STRIPPED_SPRUCE, new WaterCollectorBlock(WOOD));
    public static final RegistryObject<Block> WATER_COLLECTOR_STRIPPED_BIRCH = register(BlockNames.WATER_COLLECTOR_STRIPPED_BIRCH, new WaterCollectorBlock(WOOD));
    public static final RegistryObject<Block> WATER_COLLECTOR_STRIPPED_JUNGLE = register(BlockNames.WATER_COLLECTOR_STRIPPED_JUNGLE, new WaterCollectorBlock(WOOD));
    public static final RegistryObject<Block> WATER_COLLECTOR_STRIPPED_ACACIA = register(BlockNames.WATER_COLLECTOR_STRIPPED_ACACIA, new WaterCollectorBlock(WOOD));
    public static final RegistryObject<Block> WATER_COLLECTOR_STRIPPED_DARK_OAK = register(BlockNames.WATER_COLLECTOR_STRIPPED_DARK_OAK, new WaterCollectorBlock(WOOD));

    public static final RegistryObject<Block> WATER_FILTER_OAK = register(BlockNames.WATER_FILTER_OAK, new WaterFilterBlock(WOOD));

    /*@ObjectHolder(BlockNames.WATER_FILTER_SPRUCE)
    public static final Block WATER_FILTER_SPRUCE = null;

    @ObjectHolder(BlockNames.WATER_FILTER_BIRCH)
    public static final Block WATER_FILTER_BIRCH = null;

    @ObjectHolder(BlockNames.WATER_FILTER_JUNGLE)
    public static final Block WATER_FILTER_JUNGLE = null;

    @ObjectHolder(BlockNames.WATER_FILTER_ACACIA)
    public static final Block WATER_FILTER_ACACIA = null;

    @ObjectHolder(BlockNames.WATER_FILTER_DARK_OAK)
    public static final Block WATER_FILTER_DARK_OAK = null;

    @ObjectHolder(BlockNames.WATER_FILTER_STONE)
    public static final Block WATER_FILTER_STONE = null;

    @ObjectHolder(BlockNames.WATER_FILTER_GRANITE)
    public static final Block WATER_FILTER_GRANITE = null;

    @ObjectHolder(BlockNames.WATER_FILTER_DIORITE)
    public static final Block WATER_FILTER_DIORITE = null;

    @ObjectHolder(BlockNames.WATER_FILTER_ANDESITE)
    public static final Block WATER_FILTER_ANDESITE = null;

    @ObjectHolder(BlockNames.WATER_FILTER_STRIPPED_OAK)
    public static final Block WATER_FILTER_STRIPPED_OAK = null;

    @ObjectHolder(BlockNames.WATER_FILTER_STRIPPED_SPRUCE)
    public static final Block WATER_FILTER_STRIPPED_SPRUCE = null;

    @ObjectHolder(BlockNames.WATER_FILTER_STRIPPED_BIRCH)
    public static final Block WATER_FILTER_STRIPPED_BIRCH = null;

    @ObjectHolder(BlockNames.WATER_FILTER_STRIPPED_JUNGLE)
    public static final Block WATER_FILTER_STRIPPED_JUNGLE = null;

    @ObjectHolder(BlockNames.WATER_FILTER_STRIPPED_ACACIA)
    public static final Block WATER_FILTER_STRIPPED_ACACIA = null;

    @ObjectHolder(BlockNames.WATER_FILTER_STRIPPED_DARK_OAK)
    public static final Block WATER_FILTER_STRIPPED_DARK_OAK = null;*/

    /*@ObjectHolder(BlockNames.FLUID_PIPE)
    public static final Block FLUID_PIPE = null;

    @ObjectHolder(BlockNames.FLUID_PUMP)
    public static final Block FLUID_PUMP = null;*/

    public static final RegistryObject<FlowingFluidBlock> FILTERED_WATER = register(BlockNames.FILTERED_WATER, new FlowingFluidBlock(ModFluids.FLOWING_FILTERED_WATER, FLUID), null);
    //public static final FlowingFluidBlock FILTERED_WATER = (FlowingFluidBlock) register(new FlowingFluidBlock(() -> ModFluids.FLOWING_FILTERED_WATER, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()).setRegistryName(Reference.ID, "filtered_water"), null);

    /*@ObjectHolder(BlockNames.WATER_BOTTLE)
    public static final Block WATER_BOTTLE = null;*/

    /*@SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event)
    {
        //Water Collector

        //Water Filter

        register(BlockNames.WATER_FILTER_SPRUCE, new WaterFilterBlock(WOOD));
        register(BlockNames.WATER_FILTER_BIRCH, new WaterFilterBlock(WOOD));
        register(BlockNames.WATER_FILTER_JUNGLE, new WaterFilterBlock(WOOD));
        register(BlockNames.WATER_FILTER_ACACIA, new WaterFilterBlock(WOOD));
        register(BlockNames.WATER_FILTER_DARK_OAK, new WaterFilterBlock(WOOD));
        register(BlockNames.WATER_FILTER_STONE, new WaterFilterBlock(STONE));
        register(BlockNames.WATER_FILTER_GRANITE, new WaterFilterBlock(STONE));
        register(BlockNames.WATER_FILTER_DIORITE, new WaterFilterBlock(STONE));
        register(BlockNames.WATER_FILTER_ANDESITE, new WaterFilterBlock(STONE));
        register(BlockNames.WATER_FILTER_STRIPPED_OAK, new WaterFilterBlock(WOOD));
        register(BlockNames.WATER_FILTER_STRIPPED_SPRUCE, new WaterFilterBlock(WOOD));
        register(BlockNames.WATER_FILTER_STRIPPED_BIRCH, new WaterFilterBlock(WOOD));
        register(BlockNames.WATER_FILTER_STRIPPED_JUNGLE, new WaterFilterBlock(WOOD));
        register(BlockNames.WATER_FILTER_STRIPPED_ACACIA, new WaterFilterBlock(WOOD));
        register(BlockNames.WATER_FILTER_STRIPPED_DARK_OAK, new WaterFilterBlock(WOOD));

        //register(BlockNames.PET_RESOURCES_CRAFTER, new PetResourcesCrafterBlock(STONE));

        //register(BlockNames.FLUID_PIPE, new FluidPipeBlock(STONE));
        //register(BlockNames.FLUID_PUMP, new FluidPumpBlock(STONE));
    }*/

    private static <T extends Block> RegistryObject<T> register(String name, T block)
    {
        return register(name, block, item -> new BlockItem(item, new Item.Properties().group(BetterPetMod.GROUP)));
    }

    private static <T extends Block> RegistryObject<T> register(String name, T block, @Nullable Function<T, BlockItem> supplier)
    {
        if(supplier != null)
        {
            ModItems.ITEMS.register(name, () -> supplier.apply(block));
        }
        return ModBlocks.BLOCKS.register(name, () -> block);
    }

    /*private static void register(String name, Block block)
    {
        register(name, block, new Item.Properties());
    }

    private static void register(String name, Block block, Item.Properties properties)
    {
        register(name, block, new BlockItem(block, properties.group(BetterPetMod.GROUP)));
    }

    private static void register(String name, Block block, BlockItem item)
    {
        block.setRegistryName(name);
        BLOCKS.add(block);
        if(block.getRegistryName() != null)
        {
            item.setRegistryName(name);
            ModItems.add(item);
        }
    }

    private static void registerTall(String name, Block block)
    {
        block.setRegistryName(name);
        BLOCKS.add(block);
        if(block.getRegistryName() != null)
        {

        }
    }

    private static void register(String name, FlowingFluidBlock block)
    {
        block.setRegistryName(name);
        BLOCKS.add(block);
    }*/
}