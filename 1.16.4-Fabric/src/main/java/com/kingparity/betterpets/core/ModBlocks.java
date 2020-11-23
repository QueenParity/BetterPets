package com.kingparity.betterpets.core;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.block.WaterCollectorBlock;
import com.kingparity.betterpets.block.WaterFilterBlock;
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
    
    public static Block WATER_COLLECTOR;
    
    public static Block WATER_FILTER;
    
    private static <T extends Block> T register(String name, T block)
    {
        Registry.register(Registry.ITEM, new Identifier(Reference.ID, name), new BlockItem(block, new FabricItemSettings().group(BetterPets.GROUP)));
        return Registry.register(Registry.BLOCK, new Identifier(Reference.ID, name), block);
    }
    
    @Override
    public void onInitialize()
    {
        WATER_COLLECTOR = register("water_collector", new WaterCollectorBlock(WOOD));
        
        WATER_FILTER = register("water_filter", new WaterFilterBlock(WOOD));
    }
}
