package com.william.betterpets.init;

import com.william.betterpets.entity.BetterWolfEntity;
import com.william.betterpets.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterPetEntities
{
    public static EntityType<BetterWolfEntity> BETTER_WOLF;
    
    private static final List<EntityType<?>> ENTITY_TYPES = new LinkedList<>();
    
    public static <T extends Entity> void add(EntityType<T> type)
    {
        ENTITY_TYPES.add(type);
    }
    
    public static List<EntityType<?>> getEntityTypes()
    {
        return Collections.unmodifiableList(ENTITY_TYPES);
    }
    
    @SubscribeEvent
    public static void addEntities(final RegistryEvent.Register<EntityType<?>> event)
    {
        register(BETTER_WOLF = createEntity(BetterWolfEntity::new, EntityClassification.CREATURE, "better_wolf", 0.6F, 0.85F));
        ENTITY_TYPES.stream().forEach(entityType -> event.getRegistry().register(entityType));
    }
    
    private static <T extends Entity> EntityType<T> createEntity(EntityType.IFactory<T> factory, EntityClassification classification, String name, float width, float height)
    {
        return createEntity(factory, classification, name, 256, 1, true, width, height);
    }
    
    private static <T extends Entity> EntityType<T> createEntity(EntityType.IFactory<T> factory, EntityClassification classification, String name, int range, int updateFrequency, boolean sendVelocityUpdates, float width, float height)
    {
        EntityType<T> type = EntityType.Builder.create(factory, classification).size(width, height).setTrackingRange(range).setUpdateInterval(updateFrequency).setShouldReceiveVelocityUpdates(sendVelocityUpdates).build(Reference.ID + ":" + name);
        type.setRegistryName(new ResourceLocation(Reference.ID, name));
        return type;
    }
    
    private static void register(EntityType<? extends Entity> entity)
    {
        ENTITY_TYPES.add(entity);
    }
}
