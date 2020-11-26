package com.kingparity.betterpets.init;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, BetterPets.ID);
    
    public static final RegistryObject<EntityType<BetterWolfEntity>> BETTER_WOLF = registerEntity("better_wolf", BetterWolfEntity::new, 0.6F, 0.85F);
    
    public static void registerEntityTypeAttributes()
    {
        GlobalEntityTypeAttributes.put(BETTER_WOLF.get(), BetterWolfEntity.prepareAttributes().create());
    }
    
    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String id, BiFunction<EntityType<T>, World, T> function, float width, float height)
    {
        EntityType<T> type = EntityType.Builder.create(function::apply, EntityClassification.CREATURE).size(width, height).setTrackingRange(10).build(id);
        return ModEntities.ENTITY_TYPES.register(id, () -> type);
    }
}