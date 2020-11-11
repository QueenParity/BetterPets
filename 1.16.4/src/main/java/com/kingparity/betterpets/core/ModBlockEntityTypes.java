package com.kingparity.betterpets.core;

import com.kingparity.betterpets.block.entity.WaterCollectorBlockEntity;
import com.kingparity.betterpets.block.entity.WaterFilterBlockEntity;
import com.kingparity.betterpets.names.BlockEntityNames;
import com.kingparity.betterpets.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntityTypes implements ModInitializer
{
    public static BlockEntityType<WaterCollectorBlockEntity> WATER_COLLECTOR_BLOCK_ENTITY;
    public static BlockEntityType<WaterFilterBlockEntity> WATER_FILTER_BLOCK_ENTITY;
    
    @Override
    public void onInitialize()
    {
        WATER_COLLECTOR_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Reference.ID, BlockEntityNames.WATER_COLLECTOR),
            BlockEntityType.Builder.create(
                WaterCollectorBlockEntity::new,
                ModBlocks.WATER_COLLECTOR_OAK,
                ModBlocks.WATER_COLLECTOR_SPRUCE,
                ModBlocks.WATER_COLLECTOR_BIRCH,
                ModBlocks.WATER_COLLECTOR_JUNGLE,
                ModBlocks.WATER_COLLECTOR_ACACIA,
                ModBlocks.WATER_COLLECTOR_DARK_OAK,
                ModBlocks.WATER_COLLECTOR_STONE,
                ModBlocks.WATER_COLLECTOR_GRANITE,
                ModBlocks.WATER_COLLECTOR_DIORITE,
                ModBlocks.WATER_COLLECTOR_ANDESITE,
                ModBlocks.WATER_COLLECTOR_STRIPPED_OAK,
                ModBlocks.WATER_COLLECTOR_STRIPPED_SPRUCE,
                ModBlocks.WATER_COLLECTOR_STRIPPED_BIRCH,
                ModBlocks.WATER_COLLECTOR_STRIPPED_JUNGLE,
                ModBlocks.WATER_COLLECTOR_STRIPPED_ACACIA,
                ModBlocks.WATER_COLLECTOR_STRIPPED_DARK_OAK
            )
            .build(null)
        );
    
        WATER_FILTER_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Reference.ID, BlockEntityNames.WATER_FILTER),
            BlockEntityType.Builder.create(
                WaterFilterBlockEntity::new,
                ModBlocks.WATER_FILTER_OAK/*,
                ModBlocks.WATER_FILTER_SPRUCE,
                ModBlocks.WATER_FILTER_BIRCH,
                ModBlocks.WATER_FILTER_JUNGLE,
                ModBlocks.WATER_FILTER_ACACIA,
                ModBlocks.WATER_FILTER_DARK_OAK,
                ModBlocks.WATER_FILTER_STONE,
                ModBlocks.WATER_FILTER_GRANITE,
                ModBlocks.WATER_FILTER_DIORITE,
                ModBlocks.WATER_FILTER_ANDESITE,
                ModBlocks.WATER_FILTER_STRIPPED_OAK,
                ModBlocks.WATER_FILTER_STRIPPED_SPRUCE,
                ModBlocks.WATER_FILTER_STRIPPED_BIRCH,
                ModBlocks.WATER_FILTER_STRIPPED_JUNGLE,
                ModBlocks.WATER_FILTER_STRIPPED_ACACIA,
                ModBlocks.WATER_FILTER_STRIPPED_DARK_OAK*/
            )
            .build(null)
        );
    }
}
