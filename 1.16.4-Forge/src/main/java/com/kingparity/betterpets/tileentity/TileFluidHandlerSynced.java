package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.util.TileEntityUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class TileFluidHandlerSynced extends TileFluidHandler
{
    public TileFluidHandlerSynced(@Nonnull TileEntityType<?> tileEntityType, int capacity)
    {
        super(tileEntityType);
        this.tank = new FluidTank(capacity)
        {
            @Override
            protected void onContentsChanged()
            {
                TileFluidHandlerSynced.this.syncFluidToClient();
            }
        };
    }
    
    public TileFluidHandlerSynced(@Nonnull TileEntityType<?> tileEntityType, int capacity, Predicate<FluidStack> validator)
    {
        super(tileEntityType);
        this.tank = new FluidTank(capacity, validator)
        {
            @Override
            protected void onContentsChanged()
            {
                TileFluidHandlerSynced.this.syncFluidToClient();
            }
        };
    }
    
    public FluidStack getFluidStackTank()
    {
        return this.tank.getFluid();
    }
    
    public int getCapacity()
    {
        return 14000;
    }
    
    public int getFluidLevel()
    {
        return this.tank.getFluidAmount();
    }
    
    public void syncFluidToClient()
    {
        if(this.world != null && !this.world.isRemote)
        {
            CompoundNBT compound = new CompoundNBT();
            super.write(compound);
            TileEntityUtil.sendUpdatePacket(this, compound);
        }
    }
    
    public void syncFluidToPlayer(ServerPlayerEntity player)
    {
        if(this.world != null && !this.world.isRemote)
        {
            CompoundNBT compound = new CompoundNBT();
            super.write(compound);
            TileEntityUtil.sendUpdatePacket(this, compound);
        }
    }
    
    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }
    
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        this.read(null, pkt.getNbtCompound());
    }
    
    public FluidTank getFluidTank()
    {
        return this.tank;
    }
}