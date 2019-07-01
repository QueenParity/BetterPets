package com.kingparity.betterpets.init;

import com.kingparity.betterpets.tileentity.PetResourcesCrafterTileEntity;
import com.kingparity.betterpets.tileentity.WaterCollectorTileEntity;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterPetTileEntities
{
    public static TileEntityType<PetResourcesCrafterTileEntity> PET_RESOURCES_CRAFTER_TILE_ENTITY;
    public static TileEntityType<WaterCollectorTileEntity> WATER_COLLECTOR_TILE_ENTITY;
    public static TileEntityType<WaterFilterTileEntity> WATER_FILTER_TILE_ENTITY;
    
    private static final List<TileEntityType<?>> TILE_ENTITY_TYPES = new LinkedList<>();
    
    public static List<TileEntityType<?>> getTileEntityTypes()
    {
        return Collections.unmodifiableList(TILE_ENTITY_TYPES);
    }
    
    static
    {
        PET_RESOURCES_CRAFTER_TILE_ENTITY = register("pet_resources_crafter_tile_entity", PetResourcesCrafterTileEntity::new, BetterPetBlocks.PET_RESOURCES_CRAFTER);
        WATER_COLLECTOR_TILE_ENTITY = register("water_collector_tile_entity", WaterCollectorTileEntity::new, BetterPetBlocks.WATER_COLLECTOR);
        WATER_FILTER_TILE_ENTITY = register("water_filter_tile_entity", WaterFilterTileEntity::new, BetterPetBlocks.WATER_FILTER);
    }
    
    @SubscribeEvent
    public static void registerTileEntityTypes(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        TILE_ENTITY_TYPES.forEach(tile_entity_type -> event.getRegistry().register(tile_entity_type));
    }
    
    private static <T extends TileEntity> TileEntityType<T> register(String name, Supplier<T> factory, Block... validBlocks)
    {
        TileEntityType<T> type = TileEntityType.Builder.create(factory, validBlocks).build(null);
        type.setRegistryName(new ResourceLocation(Reference.ID, name));
        TILE_ENTITY_TYPES.add(type);
        return type;
    }
}
