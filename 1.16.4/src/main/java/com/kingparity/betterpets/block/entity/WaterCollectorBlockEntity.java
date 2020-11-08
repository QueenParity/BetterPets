package com.kingparity.betterpets.block.entity;

import com.kingparity.betterpets.core.ModBlockEntityTypes;
import com.kingparity.betterpets.fluidtank.FluidStack;
import com.kingparity.betterpets.fluidtank.FluidTanks;
import com.kingparity.betterpets.fluidtank.ImplementedFluidTank;
import com.kingparity.betterpets.util.BlockEntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public class WaterCollectorBlockEntity extends BlockEntity implements ImplementedFluidTank, Tickable
{
    private final DefaultedList<FluidStack> fluids = DefaultedList.ofSize(2, FluidStack.EMPTY);
    
    public WaterCollectorBlockEntity()
    {
        super(ModBlockEntityTypes.WATER_COLLECTOR_BLOCK_ENTITY);
    }
    
    @Override
    public FluidStack getStack(int slot)
    {
        return getFluids().get(slot);
    }
    
    @Override
    public DefaultedList<FluidStack> getFluids()
    {
        return fluids;
    }
    
    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        FluidTanks.fromTag(tag,fluids);
    }
    
    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        FluidTanks.toTag(tag,fluids);
        return super.toTag(tag);
    }
    
    @Override
    public void tick()
    {
        System.out.println("BE (" + this.getWorld().isClient() + "): " + this.getStack(0).getFluid());
    }
    
    public void syncFluidToClient()
    {
        if(this.world != null && !this.world.isClient)
        {
            CompoundTag tag = new CompoundTag();
            this.toTag(tag);
            BlockEntityUtil.sendUpdatePacket(this, tag);
        }
        this.markDirty();
    }
    
    @Override
    public CompoundTag toInitialChunkDataTag()
    {
        return this.toTag(new CompoundTag());
    }
    
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket()
    {
        return new BlockEntityUpdateS2CPacket(this.pos, 0, this.toInitialChunkDataTag());
    }
}