package com.kingparity.betterpets.init;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.util.Reference;
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

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
    
    static
    {
        BETTER_WOLF = register(BetterWolfEntity::new, EntityClassification.CREATURE, "better_wolf", 0.6F, 0.85F);
    }
    
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
        EntityType<T> type = EntityType.Builder.create(factory, classification).size(width, height).setTrackingRange(range).setUpdateInterval(updateFrequency).setShouldReceiveVelocityUpdates(sendVelocityUpdates).build(Reference.ID + ":" + name);
        type.setRegistryName(new ResourceLocation(Reference.ID, name));
        ENTITY_TYPES.add(type);
        return type;
    }
}
