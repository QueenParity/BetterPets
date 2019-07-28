package com.kingparity.betterpets.core;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.names.EntityNames;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities
{
    private static final List<EntityType<?>> ENTITY_TYPES = new ArrayList<>();
    
    //Can't use @ObjectHolder due to what is probably a bug where items are registered before entities, causing the spawn eggs to not work
    public static final EntityType<BetterWolfEntity> BETTER_WOLF = register(BetterWolfEntity::new, EntityClassification.CREATURE, EntityNames.BETTER_WOLF, 0.6F, 0.85F);;
    
    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event)
    {
        ENTITY_TYPES.forEach(entityType -> event.getRegistry().register(entityType));
    }
    
    private static <T extends Entity> EntityType<T> register(EntityType.IFactory<T> factory, EntityClassification classification, String name, float width, float height)
    {
        return register(factory, classification, name, 256, 1, true, width, height);
    }
    
    private static <T extends Entity> EntityType<T> register(EntityType.IFactory<T> factory, EntityClassification classification, String name, int range, int updateFrequency, boolean sendVelocityUpdates, float width, float height)
    {
        EntityType<T> type = EntityType.Builder.create(factory, classification).size(width, height).setTrackingRange(range).setUpdateInterval(updateFrequency).setShouldReceiveVelocityUpdates(sendVelocityUpdates).build(name);
        type.setRegistryName(name);
        ENTITY_TYPES.add(type);
        return type;
    }
}