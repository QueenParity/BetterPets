package com.kingparity.betterpets.core;

import com.kingparity.betterpets.names.TileEntityNames;
import com.kingparity.betterpets.tileentity.*;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTileEntities
{
    @ObjectHolder(TileEntityNames.PET_RESOURCES_CRAFTER)
    public static final TileEntityType<?> PET_RESOURCES_CRAFTER = null;
    
    @ObjectHolder(TileEntityNames.WATER_COLLECTOR)
    public static final TileEntityType<?> WATER_COLLECTOR = null;
    
    @ObjectHolder(TileEntityNames.WATER_FILTER)
    public static final TileEntityType<?> WATER_FILTER = null;
    
    @ObjectHolder(TileEntityNames.FLUID_PIPE)
    public static final TileEntityType<?> FLUID_PIPE = null;
    
    @ObjectHolder(TileEntityNames.FLUID_PUMP)
    public static final TileEntityType<?> FLUID_PUMP = null;
    
    private static final List<TileEntityType<?>> TILE_ENTITY_TYPES = new LinkedList<>();
    
    @SubscribeEvent
    public static void registerTileEntityTypes(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        register(TileEntityNames.WATER_COLLECTOR, WaterCollectorTileEntity::new,
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
                ModBlocks.WATER_COLLECTOR_STRIPPED_DARK_OAK);
        
        register(TileEntityNames.WATER_FILTER, WaterFilterTileEntity::new,
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
                ModBlocks.WATER_FILTER_STRIPPED_DARK_OAK*/);
        
        register(TileEntityNames.PET_RESOURCES_CRAFTER, PetResourcesCrafterTileEntity::new, ModBlocks.PET_RESOURCES_CRAFTER);
        
        register(TileEntityNames.FLUID_PIPE, FluidPipeTileEntity::new, ModBlocks.FLUID_PIPE);
        register(TileEntityNames.FLUID_PUMP, FluidPumpTileEntity::new, ModBlocks.FLUID_PUMP);
        
        TILE_ENTITY_TYPES.forEach(tile_entity_type -> event.getRegistry().register(tile_entity_type));
    }
    
    private static <T extends TileEntity> void register(String name, Supplier<T> factory, Block... validBlocks)
    {
        TileEntityType<T> type = TileEntityType.Builder.create(factory, validBlocks).build(null);
        type.setRegistryName(name);
        TILE_ENTITY_TYPES.add(type);
    }
}