package com.kingparity.betterpets.core;

import com.kingparity.betterpets.names.TileEntityNames;
import com.kingparity.betterpets.tileentity.WaterCollectorTileEntity;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModTileEntities
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Reference.ID);

    public static final RegistryObject<TileEntityType<WaterCollectorTileEntity>> WATER_COLLECTOR = register
    (
        TileEntityNames.WATER_COLLECTOR,
        WaterCollectorTileEntity::new,
        () -> new Block[]
        {
            ModBlocks.WATER_COLLECTOR_OAK.get(),
            ModBlocks.WATER_COLLECTOR_SPRUCE.get(),
            ModBlocks.WATER_COLLECTOR_BIRCH.get(),
            ModBlocks.WATER_COLLECTOR_JUNGLE.get(),
            ModBlocks.WATER_COLLECTOR_ACACIA.get(),
            ModBlocks.WATER_COLLECTOR_DARK_OAK.get(),
            ModBlocks.WATER_COLLECTOR_STONE.get(),
            ModBlocks.WATER_COLLECTOR_GRANITE.get(),
            ModBlocks.WATER_COLLECTOR_DIORITE.get(),
            ModBlocks.WATER_COLLECTOR_ANDESITE.get(),
            ModBlocks.WATER_COLLECTOR_STRIPPED_OAK.get(),
            ModBlocks.WATER_COLLECTOR_STRIPPED_SPRUCE.get(),
            ModBlocks.WATER_COLLECTOR_STRIPPED_BIRCH.get(),
            ModBlocks.WATER_COLLECTOR_STRIPPED_JUNGLE.get(),
            ModBlocks.WATER_COLLECTOR_STRIPPED_ACACIA.get(),
            ModBlocks.WATER_COLLECTOR_STRIPPED_DARK_OAK.get()
        }
    );
    
    public static final RegistryObject<TileEntityType<WaterFilterTileEntity>> WATER_FILTER = register
    (
        TileEntityNames.WATER_FILTER,
        WaterFilterTileEntity::new,
        () -> new Block[]
        {
            ModBlocks.WATER_FILTER_OAK.get()
        }
    );

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String id, Supplier<T> factoryIn, Supplier<Block[]> validBlocksSupplier)
    {
        return TILE_ENTITY_TYPES.register(id, () -> TileEntityType.Builder.create(factoryIn, validBlocksSupplier.get()).build(null));
    }
}