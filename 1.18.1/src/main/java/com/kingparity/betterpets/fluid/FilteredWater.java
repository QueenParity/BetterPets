package com.kingparity.betterpets.fluid;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.init.ModBlocks;
import com.kingparity.betterpets.init.ModFluids;
import com.kingparity.betterpets.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class FilteredWater extends ForgeFlowingFluid
{
    public FilteredWater()
    {
        super(new Properties(() ->ModFluids.FILTERED_WATER.get(), () -> ModFluids.FLOWING_FILTERED_WATER.get(), FluidAttributes.builder(new ResourceLocation(BetterPets.ID, "block/filtered_water_still"), new ResourceLocation(BetterPets.ID, "block/filtered_water_flowing")).overlay(new ResourceLocation(BetterPets.ID, "block/filtered_water_overlay")).color(/*oxFF3F76E4*/0xFF00C8FF).sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)).block(() -> ModBlocks.FILTERED_WATER.get()));
    }
    
    @Override
    public Item getBucket()
    {
        return ModItems.FILTERED_WATER_BUCKET.get();
    }
    
    public static class Source extends FilteredWater
    {
        @Override
        public int getAmount(FluidState state)
        {
            return 8;
        }
        
        @Override
        public boolean isSource(FluidState state)
        {
            return true;
        }
    }
    
    public static class Flowing extends FilteredWater
    {
        public Flowing()
        {
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }
    
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder)
        {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
    
        @Override
        public int getAmount(FluidState state)
        {
            return state.getValue(LEVEL);
        }
    
        @Override
        public boolean isSource(FluidState state)
        {
            return false;
        }
    }
}
