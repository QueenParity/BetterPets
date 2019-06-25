package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.gui.container.WaterCollectorContainer;
import com.kingparity.betterpets.init.BetterPetTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class WaterCollectorTileEntity extends BetterPetTileEntityBase
{
    public static final int MAX_CONTENTS = 10000; //1 bucket = 1000 contents; this is 10 buckets
    
    public WaterCollectorTileEntity()
    {
        super(BetterPetTileEntities.WATER_COLLECTOR_TILE_ENTITY, "water_collector", slotNum);
    }
    
    private FluidTank tank = new FluidTank(MAX_CONTENTS)
    {
        @Override
        protected void onContentsChanged()
        {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    };
    
    public static int slotNum = 3;
    
    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTag = super.getUpdateTag();
        CompoundNBT tankNBT = new CompoundNBT();
        tank.writeToNBT(tankNBT);
        nbtTag.put("tank", tankNBT);
        return nbtTag;
    }
    
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        tank.readFromNBT(pkt.getNbtCompound().getCompound("tank"));
    }
    
    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        tank.readFromNBT(compound.getCompound("tank"));
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        CompoundNBT tankNBT = new CompoundNBT();
        tank.writeToNBT(tankNBT);
        compound.put("tank", tankNBT);
        return super.write(compound);
    }
    
    public FluidTank getTank()
    {
        return tank;
    }
    
    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return LazyOptional.of(() -> (T) tank);
        }
        return super.getCapability(capability, facing);
    }
    
    @Override
    protected Container createMenu(int id, PlayerInventory inventory)
    {
        return new WaterCollectorContainer(id, inventory, this, this.pos);
    }
}