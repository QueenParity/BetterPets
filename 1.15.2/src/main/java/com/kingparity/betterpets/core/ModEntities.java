package com.kingparity.betterpets.core;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.names.EntityNames;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Reference.ID);
    
    public static final RegistryObject<EntityType<BetterWolfEntity>> BETTER_WOLF = register(EntityNames.BETTER_WOLF, BetterWolfEntity::new, 0.6F, 0.85F);
    
    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, BiFunction<EntityType<T>, World, T> function, float width, float height)
    {
        EntityType<T> type = EntityType.Builder.create(function::apply, EntityClassification.CREATURE).size(width, height).setTrackingRange(256).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build(name);
        return ModEntities.ENTITY_TYPES.register(name, () -> type);
    }
}