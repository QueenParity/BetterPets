package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.block.WaterFilterBlock;
import com.kingparity.betterpets.core.ModItems;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.gui.container.WaterFilterContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class WaterFilterTileEntity extends BasicLootTileEntity implements ITickableTileEntity
{
    private int transferCooldown = 0;
    
    public static final int inventorySize = 2;
    
    private int capacity;
    private int maxReceive;
    private int maxExtract;
    private final int[] fluidAmount;
    
    public WaterFilterTileEntity()
    {
        super(ModTileEntities.WATER_FILTER, "water_filter");
        this.capacity = 12000;
        this.maxReceive = 500;
        this.maxExtract = 500;
        this.fluidAmount = new int[2];
        for(int i : fluidAmount)
        {
            this.fluidAmount[i] = 0;
        }
    }
    
    @Override
    public int getSizeInventory()
    {
        return inventorySize;
    }
    
    @Override
    protected Container createMenu(int id, PlayerInventory inventory)
    {
        return new WaterFilterContainer(id, inventory, this, this.pos);
    }
    
    public int receiveFluid(int maxReceive, boolean doDrain, int tankId)
    {
        if(!canReceive())
        {
            return 0;
        }
        int fluidReceived = Math.min((capacity / 2) - fluidAmount[tankId], Math.min(this.maxReceive, maxReceive));
        if(doDrain)
        {
            fluidAmount[tankId] += fluidReceived;
            syncToClient();
        }
        return fluidReceived;
    }
    
    public int extractFluid(int maxExtract, boolean doDrain, int tankId)
    {
        if(!canExtract())
        {
            return 0;
        }
        
        int fluidExtracted = Math.min(fluidAmount[tankId], Math.min(this.maxExtract, maxExtract));
        if (doDrain)
        {
            fluidAmount[tankId] -= fluidExtracted;
            syncToClient();
        }
        return fluidExtracted;
    }
    
    public int getCapacity()
    {
        return capacity;
    }
    
    public int getMaxReceive()
    {
        return maxReceive;
    }
    
    public int getMaxExtract()
    {
        return maxExtract;
    }
    
    public int getFluidAmount()
    {
        int r = 0;
        for(int i : fluidAmount)
        {
            r += i;
        }
        return r;
    }
    
    public int getFluidAmount(int tankId)
    {
        return fluidAmount[tankId];
    }
    
    public boolean canExtract()
    {
        return this.maxExtract > 0;
    }
    
    public boolean canReceive()
    {
        return this.maxReceive > 0;
    }
    
    public boolean hasFluid()
    {
        for(int i : fluidAmount)
        {
            if(i != 0)
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasFluid(int tankId)
    {
        return fluidAmount[tankId] != 0;
    }
    
    public ResourceLocation getStill()
    {
        return new ResourceLocation("block/water_still");
    }
    
    public ResourceLocation getFlowing()
    {
        return new ResourceLocation("block/water_flow");
    }
    
    @Override
    public void tick()
    {
        boolean hasFabric = this.getStackInSlot(0).getItem() == ModItems.WATER_FILTER_FABRIC;
        if(transferCooldown == 0)
        {
            if(hasFabric)
            {
                this.extractFluid(this.receiveFluid(Math.min(this.getMaxExtract(), this.getFluidAmount(1)), true, 0), true, 1);
            }
            transferCooldown = 9;
        }
        else
        {
            transferCooldown--;
        }
        syncToClient();
        
        this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(WaterFilterBlock.ACTIVE, hasFabric), 3);
        this.markDirty();
    }
    
    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        if(compound.contains("FluidAmount", Constants.NBT.TAG_INT_ARRAY))
        {
            int[] aint = compound.getIntArray("FluidAmount");
            System.arraycopy(aint, 0, this.fluidAmount, 0, Math.min(this.fluidAmount.length, aint.length));
        }
        if(compound.contains("Capacity", Constants.NBT.TAG_INT))
        {
            this.capacity = compound.getInt("Capacity");
        }
        if(compound.contains("TransferCooldown", Constants.NBT.TAG_INT))
        {
            this.transferCooldown = compound.getInt("TransferCooldown");
        }
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putIntArray("FluidAmount", fluidAmount);
        compound.putInt("Capacity", capacity);
        compound.putInt("TransferCooldown", this.transferCooldown);
        return compound;
    }
}