package com.kingparity.betterpets.core;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.block.WaterCollectorBlock;
import com.kingparity.betterpets.block.WaterFilterBlock;
import com.kingparity.betterpets.names.BlockNames;
import com.kingparity.betterpets.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks implements ModInitializer
{
    public static final FabricBlockSettings WOOD = FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD);
    public static final FabricBlockSettings STONE = FabricBlockSettings.of(Material.STONE, MaterialColor.STONE);
    
    public static Block WATER_COLLECTOR_OAK;
    public static Block WATER_COLLECTOR_SPRUCE;
    public static Block WATER_COLLECTOR_BIRCH;
    public static Block WATER_COLLECTOR_JUNGLE;
    public static Block WATER_COLLECTOR_ACACIA;
    public static Block WATER_COLLECTOR_DARK_OAK;
    public static Block WATER_COLLECTOR_STONE;
    public static Block WATER_COLLECTOR_GRANITE;
    public static Block WATER_COLLECTOR_DIORITE;
    public static Block WATER_COLLECTOR_ANDESITE;
    public static Block WATER_COLLECTOR_STRIPPED_OAK;
    public static Block WATER_COLLECTOR_STRIPPED_SPRUCE;
    public static Block WATER_COLLECTOR_STRIPPED_BIRCH;
    public static Block WATER_COLLECTOR_STRIPPED_JUNGLE;
    public static Block WATER_COLLECTOR_STRIPPED_ACACIA;
    public static Block WATER_COLLECTOR_STRIPPED_DARK_OAK;
    
    public static Block WATER_FILTER_OAK;
    public static Block WATER_FILTER_SPRUCE;
    public static Block WATER_FILTER_BIRCH;
    public static Block WATER_FILTER_JUNGLE;
    public static Block WATER_FILTER_ACACIA;
    public static Block WATER_FILTER_DARK_OAK;
    public static Block WATER_FILTER_STONE;
    public static Block WATER_FILTER_GRANITE;
    public static Block WATER_FILTER_DIORITE;
    public static Block WATER_FILTER_ANDESITE;
    public static Block WATER_FILTER_STRIPPED_OAK;
    public static Block WATER_FILTER_STRIPPED_SPRUCE;
    public static Block WATER_FILTER_STRIPPED_BIRCH;
    public static Block WATER_FILTER_STRIPPED_JUNGLE;
    public static Block WATER_FILTER_STRIPPED_ACACIA;
    public static Block WATER_FILTER_STRIPPED_DARK_OAK;
    
    private static <T extends Block> T register(String name, T block)
    {
        Registry.register(Registry.ITEM, new Identifier(Reference.ID, name), new BlockItem(block, new FabricItemSettings().group(BetterPets.GROUP)));
        return Registry.register(Registry.BLOCK, new Identifier(Reference.ID, name), block);
    }
    
    @Override
    public void onInitialize()
    {
        WATER_COLLECTOR_OAK = register(BlockNames.WATER_COLLECTOR_OAK, new WaterCollectorBlock(WOOD));
        WATER_COLLECTOR_SPRUCE = register(BlockNames.WATER_COLLECTOR_SPRUCE, new WaterCollectorBlock(WOOD));
        WATER_COLLECTOR_BIRCH = register(BlockNames.WATER_COLLECTOR_BIRCH, new WaterCollectorBlock(WOOD));
        WATER_COLLECTOR_JUNGLE = register(BlockNames.WATER_COLLECTOR_JUNGLE, new WaterCollectorBlock(WOOD));
        WATER_COLLECTOR_ACACIA = register(BlockNames.WATER_COLLECTOR_ACACIA, new WaterCollectorBlock(WOOD));
        WATER_COLLECTOR_DARK_OAK = register(BlockNames.WATER_COLLECTOR_DARK_OAK, new WaterCollectorBlock(WOOD));
        WATER_COLLECTOR_STONE = register(BlockNames.WATER_COLLECTOR_STONE, new WaterCollectorBlock(STONE));
        WATER_COLLECTOR_GRANITE = register(BlockNames.WATER_COLLECTOR_GRANITE, new WaterCollectorBlock(STONE));
        WATER_COLLECTOR_DIORITE = register(BlockNames.WATER_COLLECTOR_DIORITE, new WaterCollectorBlock(STONE));
        WATER_COLLECTOR_ANDESITE = register(BlockNames.WATER_COLLECTOR_ANDESITE, new WaterCollectorBlock(STONE));
        WATER_COLLECTOR_STRIPPED_OAK = register(BlockNames.WATER_COLLECTOR_STRIPPED_OAK, new WaterCollectorBlock(WOOD));
        WATER_COLLECTOR_STRIPPED_SPRUCE = register(BlockNames.WATER_COLLECTOR_STRIPPED_SPRUCE, new WaterCollectorBlock(WOOD));
        WATER_COLLECTOR_STRIPPED_BIRCH = register(BlockNames.WATER_COLLECTOR_STRIPPED_BIRCH, new WaterCollectorBlock(WOOD));
        WATER_COLLECTOR_STRIPPED_JUNGLE = register(BlockNames.WATER_COLLECTOR_STRIPPED_JUNGLE, new WaterCollectorBlock(WOOD));
        WATER_COLLECTOR_STRIPPED_ACACIA = register(BlockNames.WATER_COLLECTOR_STRIPPED_ACACIA, new WaterCollectorBlock(WOOD));
        WATER_COLLECTOR_STRIPPED_DARK_OAK = register(BlockNames.WATER_COLLECTOR_STRIPPED_DARK_OAK, new WaterCollectorBlock(WOOD));
    
        WATER_FILTER_OAK = register(BlockNames.WATER_FILTER_OAK, new WaterFilterBlock(WOOD));
        WATER_FILTER_SPRUCE = register(BlockNames.WATER_FILTER_SPRUCE, new WaterFilterBlock(WOOD));
        WATER_FILTER_BIRCH = register(BlockNames.WATER_FILTER_BIRCH, new WaterFilterBlock(WOOD));
        WATER_FILTER_JUNGLE = register(BlockNames.WATER_FILTER_JUNGLE, new WaterFilterBlock(WOOD));
        WATER_FILTER_ACACIA = register(BlockNames.WATER_FILTER_ACACIA, new WaterFilterBlock(WOOD));
        WATER_FILTER_DARK_OAK = register(BlockNames.WATER_FILTER_DARK_OAK, new WaterFilterBlock(WOOD));
        WATER_FILTER_STONE = register(BlockNames.WATER_FILTER_STONE, new WaterFilterBlock(STONE));
        WATER_FILTER_GRANITE = register(BlockNames.WATER_FILTER_GRANITE, new WaterFilterBlock(STONE));
        WATER_FILTER_DIORITE = register(BlockNames.WATER_FILTER_DIORITE, new WaterFilterBlock(STONE));
        WATER_FILTER_ANDESITE = register(BlockNames.WATER_FILTER_ANDESITE, new WaterFilterBlock(STONE));
        WATER_FILTER_STRIPPED_OAK = register(BlockNames.WATER_FILTER_STRIPPED_OAK, new WaterFilterBlock(WOOD));
        WATER_FILTER_STRIPPED_SPRUCE = register(BlockNames.WATER_FILTER_STRIPPED_SPRUCE, new WaterFilterBlock(WOOD));
        WATER_FILTER_STRIPPED_BIRCH = register(BlockNames.WATER_FILTER_STRIPPED_BIRCH, new WaterFilterBlock(WOOD));
        WATER_FILTER_STRIPPED_JUNGLE = register(BlockNames.WATER_FILTER_STRIPPED_JUNGLE, new WaterFilterBlock(WOOD));
        WATER_FILTER_STRIPPED_ACACIA = register(BlockNames.WATER_FILTER_STRIPPED_ACACIA, new WaterFilterBlock(WOOD));
        WATER_FILTER_STRIPPED_DARK_OAK = register(BlockNames.WATER_FILTER_STRIPPED_DARK_OAK, new WaterFilterBlock(WOOD));
    }
}
