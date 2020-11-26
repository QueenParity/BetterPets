package com.kingparity.betterpets.init;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.fluid.FilteredWater;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFluids
{
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, BetterPets.ID);
    
    public static final RegistryObject<Fluid> FILTERED_WATER = FLUIDS.register("filtered_water", FilteredWater.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_FILTERED_WATER = FLUIDS.register("flowing_filtered_water", FilteredWater.Flowing::new);
}