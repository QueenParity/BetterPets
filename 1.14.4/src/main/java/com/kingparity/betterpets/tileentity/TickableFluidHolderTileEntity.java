package com.kingparity.betterpets.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

public abstract class TickableFluidHolderTileEntity extends FluidHolderTileEntity implements ITickableTileEntity
{
    protected int transferCooldown = 0;
    
    public TickableFluidHolderTileEntity(TileEntityType<?> tileEntityType)
    {
        super(tileEntityType, 12000);
    }
    
    public TickableFluidHolderTileEntity(TileEntityType<?> tileEntityType, int capacity)
    {
        super(tileEntityType, capacity, capacity);
    }
    
    public TickableFluidHolderTileEntity(TileEntityType<?> tileEntityType, int capacity, int maxTransfer)
    {
        super(tileEntityType, capacity, maxTransfer, maxTransfer);
    }
    
    public TickableFluidHolderTileEntity(TileEntityType<?> tileEntityType, int capacity, int maxReceive, int maxExtract)
    {
        super(tileEntityType, capacity, maxReceive, maxExtract);
    }
    
    public int getTransferCooldown()
    {
        return transferCooldown;
    }
    
    @Override
    public void tick()
    {
        if(transferCooldown == 0)
        {
            fluidTick();
            transferCooldown = 2;
        }
        else
        {
            transferCooldown--;
        }
        syncToClient();
    }
    
    public abstract void fluidTick();
    
    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        if(compound.contains("TransferCooldown", Constants.NBT.TAG_INT))
        {
            this.transferCooldown = compound.getInt("TransferCooldown");
        }
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("TransferCooldown", this.transferCooldown);
        return compound;
    }
}
