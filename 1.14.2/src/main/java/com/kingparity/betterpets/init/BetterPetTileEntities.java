package com.kingparity.betterpets.init;

import com.kingparity.betterpets.tileentity.PetFoodMakerTileEntity;
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
    private static final List<TileEntityType<?>> TILE_ENTITY_TYPES = new LinkedList<>();
    
    public static TileEntityType<PetFoodMakerTileEntity> PET_FOOD_MAKER_TILE_ENTITY;
    
    public static List<TileEntityType<?>> getTileEntityTypes()
    {
        return Collections.unmodifiableList(TILE_ENTITY_TYPES);
    }
    
    @SubscribeEvent
    public static void addTileEntityTypes(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        PET_FOOD_MAKER_TILE_ENTITY = register("pet_food_maker_tile_entity", PetFoodMakerTileEntity::new, BetterPetBlocks.PET_FOOD_MAKER);
        
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
