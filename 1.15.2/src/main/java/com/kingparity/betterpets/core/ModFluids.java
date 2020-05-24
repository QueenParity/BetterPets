package com.kingparity.betterpets.core;

import com.kingparity.betterpets.fluid.FilteredWater;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFluids
{
    public static final Fluid FILTERED_WATER = new FilteredWater.Source();
    public static final FlowingFluid FLOWING_FILTERED_WATER = new FilteredWater.Flowing();

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void register(final RegistryEvent.Register<Fluid> event)
    {
        IForgeRegistry<Fluid> registry = event.getRegistry();
        registry.register(FILTERED_WATER);
        registry.register(FLOWING_FILTERED_WATER);
    }
}