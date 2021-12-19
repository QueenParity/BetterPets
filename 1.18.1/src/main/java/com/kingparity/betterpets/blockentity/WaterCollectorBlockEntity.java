package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class WaterCollectorBlockEntity extends FluidHandlerSyncedBlockEntity
{
    public WaterCollectorBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.WATER_COLLECTOR.get(), pos, state, FluidAttributes.BUCKET_VOLUME * 14, stack -> stack.getFluid() == Fluids.WATER);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, WaterCollectorBlockEntity blockEntity)
    {
        if(!level.isClientSide)
        {
            if(blockEntity.tank.getFluidAmount() < blockEntity.tank.getCapacity())
            {
                if(level.getBiome(pos).getPrecipitation() == Biome.Precipitation.RAIN)
                {
                    if(level.getRandom().nextFloat() < 0.05F)
                    {
                        blockEntity.tank.fill(new FluidStack(Fluids.WATER, Math.round(FluidAttributes.BUCKET_VOLUME / 2.0F)), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
    }
}
