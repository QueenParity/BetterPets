package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidAttributes;

public class TankBlockEntity extends FluidHandlerSyncedBlockEntity
{
    public TankBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.TANK.get(), pos, state, FluidAttributes.BUCKET_VOLUME * 16);
    }
}
