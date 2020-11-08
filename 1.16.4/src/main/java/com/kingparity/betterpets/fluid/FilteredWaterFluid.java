package com.kingparity.betterpets.fluid;

import com.kingparity.betterpets.core.ModFluids;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;

public abstract class FilteredWaterFluid extends BetterPetsFluid
{
    @Override
    public Fluid getStill()
    {
        return ModFluids.STILL_FILTERED_WATER;
    }
    
    @Override
    public Fluid getFlowing()
    {
        return ModFluids.FLOWING_FILTERED_WATER;
    }
    
    @Override
    public Item getBucketItem()
    {
        return ModFluids.FILTERED_WATER_BUCKET;
    }
    
    @Override
    protected BlockState toBlockState(FluidState state)
    {
        return ModFluids.FILTERED_WATER.getDefaultState().with(Properties.LEVEL_15, method_15741(state));
    }
    
    public static class Flowing extends FilteredWaterFluid
    {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder)
        {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }
    
        @Override
        public int getLevel(FluidState fluidState)
        {
            return fluidState.get(LEVEL);
        }
    
        @Override
        public boolean isStill(FluidState fluidState)
        {
            return false;
        }
    }
    
    public static class Still extends FilteredWaterFluid
    {
        @Override
        public int getLevel(FluidState fluidState)
        {
            return 8;
        }
        
        @Override
        public boolean isStill(FluidState fluidState)
        {
            return true;
        }
    }
}
