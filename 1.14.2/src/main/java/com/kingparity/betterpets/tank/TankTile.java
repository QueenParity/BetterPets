package com.kingparity.betterpets.tank;

import com.kingparity.betterpets.init.BetterPetTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class TankTile extends TileEntity implements IRestorableTileEntity
{
    public static final int MAX_CONTENTS = 10000;       // 10 buckets
    
    public TankTile() {
        super(BetterPetTileEntities.TANK_TILE_ENTITY);
    }
    
    private FluidTank tank = new FluidTank(MAX_CONTENTS) {
        @Override
        protected void onContentsChanged() {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    };
    
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbtTag = super.getUpdateTag();
        CompoundNBT tankNBT = new CompoundNBT();
        tank.writeToNBT(tankNBT);
        nbtTag.put("tank", tankNBT);
        return nbtTag;
    }
    
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        tank.readFromNBT(packet.getNbtCompound().getCompound("tank"));
    }
    
    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        readRestorableFromNBT(tagCompound);
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        writeRestorableToNBT(compound);
        return super.write(compound);
    }
    
    @Override
    public void readRestorableFromNBT(CompoundNBT compound) {
        tank.readFromNBT(compound.getCompound("tank"));
    }
    
    @Override
    public void writeRestorableToNBT(CompoundNBT compound) {
        CompoundNBT tankNBT = new CompoundNBT();
        tank.writeToNBT(tankNBT);
        compound.put("tank", tankNBT);
    }
    
    public FluidTank getTank() {
        return tank;
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) tank);
        }
        return super.getCapability(capability, facing);
    }
}
