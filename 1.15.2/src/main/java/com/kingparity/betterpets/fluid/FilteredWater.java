package com.kingparity.betterpets.fluid;

import com.kingparity.betterpets.core.ModBlocks;
import com.kingparity.betterpets.core.ModFluids;
import com.kingparity.betterpets.core.ModItems;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
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
        super(new Properties(() -> ModFluids.FILTERED_WATER, () -> ModFluids.FLOWING_FILTERED_WATER, FluidAttributes.builder(new ResourceLocation("block/water_still"), new ResourceLocation("block/water_flow")).overlay(new ResourceLocation("block/water_overlay")).density(900).viscosity(900).color(0x3F1080FF).sound(SoundEvents.ITEM_BUCKET_FILL, SoundEvents.ITEM_BUCKET_EMPTY)).bucket(() -> ModItems.FILTERED_WATER_BUCKET).block(() -> ModBlocks.FILTERED_WATER));
    }

    @Override
    public Item getFilledBucket()
    {
        return ModItems.FILTERED_WATER_BUCKET;
    }

    public static class Source extends FilteredWater
    {
        public Source()
        {
            this.setRegistryName(new ResourceLocation(Reference.ID, "filtered_water"));
        }

        @Override
        public boolean isSource(IFluidState state)
        {
            return true;
        }

        @Override
        public int getLevel(IFluidState state)
        {
            return 8;
        }
    }

    public static class Flowing extends FilteredWater
    {
        public Flowing()
        {
            this.setRegistryName(new ResourceLocation(Reference.ID, "flowing_filtered_water"));
        }

        @Override
        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder)
        {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        @Override
        public int getLevel(IFluidState state)
        {
            return state.get(LEVEL_1_8);
        }

        @Override
        public boolean isSource(IFluidState state)
        {
            return false;
        }
    }
}