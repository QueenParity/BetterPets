package com.kingparity.betterpets.core;

import com.kingparity.betterpets.fluid.FilteredWater;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ModFluids
{
    public static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, Reference.ID);
    
    public static final RegistryObject<Fluid> FILTERED_WATER = FLUIDS.register("filtered_water", FilteredWater.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_FILTERED_WATER = FLUIDS.register("flowing_filtered_water", FilteredWater.Flowing::new);
}