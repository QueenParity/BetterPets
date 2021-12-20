package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TankBlockEntity extends FluidHandlerSyncedBlockEntity
{
    public TankBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.TANK.get(), pos, state, FluidAttributes.BUCKET_VOLUME * 16);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, TankBlockEntity blockEntity)
    {
        if(!level.isClientSide)
        {
            blockEntity.syncFluidToClient();
            BlockEntity blockEntityBelow = level.getBlockEntity(pos.below());
            if(blockEntityBelow instanceof TankBlockEntity)
            {
                IFluidHandler fluidHandler = blockEntityBelow.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
                if(fluidHandler != null)
                {
                    FluidUtils.transferFluid(blockEntity.getTank(), fluidHandler, blockEntity.getTank().getFluidAmount());
                }
                else
                {
                    throw new IllegalStateException("well this was unexpected :/ and is impossible");
                }
            }
        }
    }
}