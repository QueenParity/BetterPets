package com.kingparity.betterpets.core;

import com.kingparity.betterpets.block.entity.WaterCollectorTileEntity;
import com.kingparity.betterpets.block.entity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModTileEntities implements ModInitializer
{
    public static BlockEntityType<WaterCollectorTileEntity> WATER_COLLECTOR;
    public static BlockEntityType<WaterFilterTileEntity> WATER_FILTER;
    
    @Override
    public void onInitialize()
    {
        WATER_COLLECTOR = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Reference.ID, "water_collector"),
            BlockEntityType.Builder.create(
                WaterCollectorTileEntity::new,
                ModBlocks.WATER_COLLECTOR
            )
            .build(null)
        );
    
        WATER_FILTER = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Reference.ID, "water_filter"),
            BlockEntityType.Builder.create(
                WaterFilterTileEntity::new,
                ModBlocks.WATER_FILTER
            )
            .build(null)
        );
    }
}
