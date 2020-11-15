package com.kingparity.betterpets.fluid;

import com.kingparity.betterpets.init.ModBlocks;
import com.kingparity.betterpets.init.ModFluids;
import com.kingparity.betterpets.init.ModItems;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class FilteredWater extends ForgeFlowingFluid
{
    public FilteredWater()
    {
        super(new Properties(() -> ModFluids.FILTERED_WATER.get(), () -> ModFluids.FLOWING_FILTERED_WATER.get(), FluidAttributes.builder(new ResourceLocation(Reference.ID, "block/filtered_water_still"), new ResourceLocation(Reference.ID, "block/filtered_water_flowing")).overlay(new ResourceLocation(Reference.ID, "block/filtered_water_overlay")).color(/*0xFF3F76E4*/0xFF00C8FF).sound(SoundEvents.ITEM_BUCKET_FILL, SoundEvents.ITEM_BUCKET_EMPTY)).block(() -> ModBlocks.FILTERED_WATER.get()));
    }
    
    @Override
    public Item getFilledBucket()
    {
        return ModItems.FILTERED_WATER_BUCKET.get();
    }
    
    public static class Source extends FilteredWater
    {
        @Override
        public boolean isSource(FluidState state)
        {
            return true;
        }
    
        @Override
        public int getLevel(FluidState state)
        {
            return 8;
        }
    }
    
    public static class Flowing extends FilteredWater
    {
        @Override
        protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder)
        {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }
    
        @Override
        public int getLevel(FluidState state)
        {
            return state.get(LEVEL_1_8);
        }
    
        @Override
        public boolean isSource(FluidState state)
        {
            return false;
        }
    }
}
