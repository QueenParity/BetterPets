package com.kingparity.betterpets.init;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.entity.BetterWolf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = BetterPets.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, BetterPets.ID);
    
    public static final RegistryObject<EntityType<BetterWolf>> BETTER_WOLF = registerEntity("better_wolf", BetterWolf::new, 0.6F, 0.85F);
    
    @SubscribeEvent
    public static void onRegisterAttributes(EntityAttributeCreationEvent event)
    {
        event.put(BETTER_WOLF.get(), BetterWolf.createAttributes().build());
    }
    
    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String id, Function<Level, T> function, float width, float height)
    {
        EntityType<T> type = EntityType.Builder.<T>of((entityType, world) -> function.apply(world), MobCategory.CREATURE).sized(width, height).setCustomClientFactory((spawnEntity, level2) -> function.apply(level2)).setTrackingRange(10).build(id);
        return ModEntities.ENTITY_TYPES.register(id, () -> type);
    }
}
