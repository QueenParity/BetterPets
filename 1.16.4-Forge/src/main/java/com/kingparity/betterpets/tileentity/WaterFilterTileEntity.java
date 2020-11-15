package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.block.RotatedBlockObject;
import com.kingparity.betterpets.init.ModFluids;
import com.kingparity.betterpets.init.ModTileEntities;
import com.kingparity.betterpets.inventory.container.WaterFilterContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class WaterFilterTileEntity extends TileEntitySynced implements IInventory, ITickableTileEntity, INamedContainerProvider, IFluidTankWriter
{
    private NonNullList<ItemStack> inventory = NonNullList.withSize(7, ItemStack.EMPTY);
    
    private FluidTank tankWater = new FluidTank(7000, stack -> stack.getFluid() == Fluids.WATER);
    private FluidTank tankFilteredWater = new FluidTank(7000, stack -> stack.getFluid() == ModFluids.FILTERED_WATER.get());
    
    private static final int SLOT_FUEL = 0;
    
    private int remainingFuel;
    private int fuelMaxProgress;
    private int filterProgress;
    
    private String customName;
    
    protected final IIntArray waterFilterData = new IIntArray()
    {
        @Override
        public int get(int index)
        {
            switch(index)
            {
                case 0:
                    return filterProgress;
                case 1:
                    return remainingFuel;
                case 2:
                    return fuelMaxProgress;
                case 3:
                    return tankWater.getFluidAmount();
                case 4:
                    return tankFilteredWater.getFluidAmount();
                case 5:
                    return tankWater.getFluid().getFluid().getRegistryName().hashCode();
                case 6:
                    return tankFilteredWater.getFluid().getFluid().getRegistryName().hashCode();
            }
            return 0;
        }
    
        @Override
        public void set(int index, int value)
        {
            switch(index)
            {
                case 0:
                    filterProgress = value;
                    break;
                case 1:
                    remainingFuel = value;
                    break;
                case 2:
                    fuelMaxProgress = value;
                    break;
                case 3:
                    if(!tankWater.isEmpty() || tankWater.getFluid().getRawFluid() != Fluids.EMPTY)
                    {
                        tankWater.getFluid().setAmount(value);
                    }
                    break;
                case 4:
                    if(!tankFilteredWater.isEmpty() || tankFilteredWater.getFluid().getRawFluid() != Fluids.EMPTY)
                    {
                        tankFilteredWater.getFluid().setAmount(value);
                    }
                    break;
                case 5:
                    updateFluid(tankWater, value);
                    break;
                case 6:
                    updateFluid(tankFilteredWater, value);
                    break;
            }
        }
    
        @Override
        public int size()
        {
            return 7;
        }
    };
    
    public WaterFilterTileEntity()
    {
        super(ModTileEntities.WATER_FILTER.get());
    }
    
    @Override
    public int getSizeInventory()
    {
        return 7;
    }
    
    @Override
    public boolean isEmpty()
    {
        for(ItemStack stack : this.inventory)
        {
            if(!stack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.inventory.get(index);
    }
    
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack stack = ItemStackHelper.getAndSplit(this.inventory, index, count);
        if(!stack.isEmpty())
        {
            this.markDirty();
        }
        return stack;
    }
    
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }
    
    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.inventory.set(index, stack);
        if(stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }
    
    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if(index == 0)
        {
            return ForgeHooks.getBurnTime(stack) > 0;
        }
        return false;
    }
    
    @Override
    public void clear()
    {
        this.inventory.clear();
    }
    
    @Override
    public void tick()
    {
        if(!this.world.isRemote)
        {
            ItemStack fuel = this.getStackInSlot(SLOT_FUEL);
            
            if(!this.canFilter())
            {
                this.filterProgress = 0;
            }
        
            if(this.canFilter())
            {
                this.updateFuel(fuel);
            
                if(this.remainingFuel > 0)
                {
                    if(this.filterProgress++ == 58)
                    {
                        this.tankFilteredWater.fill(new FluidStack(ModFluids.FILTERED_WATER.get(), 100), IFluidHandler.FluidAction.EXECUTE);
                        this.tankWater.drain(100, IFluidHandler.FluidAction.EXECUTE);
                        this.filterProgress = 0;
                        this.syncToClient();
                    }
                }
                else
                {
                    this.filterProgress = 0;
                }
            }
            else
            {
                this.filterProgress = 0;
            }
        
            if(this.remainingFuel > 0)
            {
                this.remainingFuel--;
                this.updateFuel(fuel);
            }
        }
    }
    
    private void updateFuel(ItemStack fuel)
    {
        if(!fuel.isEmpty() && ForgeHooks.getBurnTime(fuel) > 0 && this.remainingFuel == 0 && this.canFilter())
        {
            this.fuelMaxProgress = ForgeHooks.getBurnTime(fuel);
            this.remainingFuel = this.fuelMaxProgress;
            this.shrinkItem(SLOT_FUEL);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean canFilterClient()
    {
        return this.canFilter() && this.remainingFuel >= 0;
    }
    
    private void shrinkItem(int index)
    {
        ItemStack stack = this.getStackInSlot(index);
        stack.shrink(1);
        if(stack.isEmpty())
        {
            this.setInventorySlotContents(index, ItemStack.EMPTY);
        }
    }
    
    private boolean canFilter()
    {
        if(this.tankWater.getFluid().isEmpty())
        {
            return false;
        }
        
        if(this.tankWater.getFluidAmount() < 100)
        {
            return false;
        }
        
        if(this.tankFilteredWater.getFluidAmount() >= this.tankFilteredWater.getCapacity())
        {
            return false;
        }
        return this.tankFilteredWater.getFluidAmount() + 100 <= this.tankFilteredWater.getCapacity();
    }
    
    @Override
    public void read(BlockState state, CompoundNBT compound)
    {
        super.read(state, compound);
        if(compound.contains("Items", Constants.NBT.TAG_LIST))
        {
            this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(compound, this.inventory);
        }
        if(compound.contains("CustomName", Constants.NBT.TAG_STRING))
        {
            this.customName = compound.getString("CustomName");
        }
        if(compound.contains("TankWater", Constants.NBT.TAG_COMPOUND))
        {
            CompoundNBT tagCompound = compound.getCompound("TankWater");
            this.tankWater.readFromNBT(tagCompound);
        }
        if(compound.contains("TankFilteredWater", Constants.NBT.TAG_COMPOUND))
        {
            CompoundNBT tagCompound = compound.getCompound("TankFilteredWater");
            this.tankFilteredWater.readFromNBT(tagCompound);
        }
        if(compound.contains("RemainingFuel", Constants.NBT.TAG_INT))
        {
            this.remainingFuel = compound.getInt("RemainingFuel");
        }
        if(compound.contains("FuelMaxProgress", Constants.NBT.TAG_INT))
        {
            this.fuelMaxProgress = compound.getInt("FuelMaxProgress");
        }
        if(compound.contains("FilterProgress", Constants.NBT.TAG_INT))
        {
            this.filterProgress = compound.getInt("FilterProgress");
        }
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        
        ItemStackHelper.saveAllItems(compound, this.inventory);
        
        if(this.hasCustomName())
        {
            compound.putString("CustomName", this.customName);
        }
        
        this.writeTanks(compound);
        
        compound.putInt("RemainingFuel", this.remainingFuel);
        compound.putInt("FuelMaxProgress", this.fuelMaxProgress);
        compound.putInt("FilterProgress", this.filterProgress);
        return compound;
    }
    
    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT tag = super.write(new CompoundNBT());
        this.writeTanks(tag);
        return tag;
    }
    
    @Override
    public void writeTanks(CompoundNBT compound)
    {
        CompoundNBT tagTankWater = new CompoundNBT();
        this.tankWater.writeToNBT(tagTankWater);
        compound.put("TankWater", tagTankWater);
        
        CompoundNBT tagTankFilteredWater = new CompoundNBT();
        this.tankFilteredWater.writeToNBT(tagTankFilteredWater);
        compound.put("TankFilteredWater", tagTankFilteredWater);
    }
    
    @Override
    public boolean areTanksEmpty()
    {
        return this.tankWater.isEmpty() && this.tankFilteredWater.isEmpty();
    }
    
    private String getName()
    {
        return this.hasCustomName() ? this.customName : "container.water_filter";
    }
    
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }
    
    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new StringTextComponent(this.getName()) : new TranslationTextComponent(this.getName());
    }
    
    @Nullable
    public FluidStack getWaterFluidStack()
    {
        return this.tankWater.getFluid();
    }
    
    @Nullable
    public FluidStack getFilteredWaterFluidStack()
    {
        return this.tankFilteredWater.getFluid();
    }
    
    public int getFilterProgress()
    {
        return this.waterFilterData.get(0);
    }
    
    public int getRemainingFuel()
    {
        return this.waterFilterData.get(1);
    }
    
    public int getFuelMaxProgress()
    {
        return this.waterFilterData.get(2);
    }
    
    public int getWaterLevel()
    {
        return this.waterFilterData.get(3);
    }
    
    public int getFilteredWaterLevel()
    {
        return this.waterFilterData.get(4);
    }
    
    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return new WaterFilterContainer(windowId, playerInventory, this);
    }
    
    public IIntArray getWaterFilterData()
    {
        return waterFilterData;
    }
    
    public void updateFluid(FluidTank tank, int fluidHash)
    {
        Optional<Fluid> optional = ForgeRegistries.FLUIDS.getValues().stream().filter(fluid -> fluid.getRegistryName().hashCode() == fluidHash).findFirst();
        optional.ifPresent(fluid -> tank.setFluid(new FluidStack(fluid, tank.getFluidAmount())));
    }
    
    public FluidTank getWaterTank()
    {
        return tankWater;
    }
    
    public FluidTank getFilteredWaterTank()
    {
        return tankFilteredWater;
    }
    
    private final LazyOptional<?> itemHandler = LazyOptional.of(this::createUnSidedHandler);
    
    @Nonnull
    protected IItemHandler createUnSidedHandler()
    {
        return new InvWrapper(this);
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction facing)
    {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            BlockState state = this.world.getBlockState(this.pos);
            if(state.getProperties().contains(RotatedBlockObject.DIRECTION))
            {
                Direction direction = state.get(RotatedBlockObject.DIRECTION);
                if(facing == direction.rotateYCCW())
                {
                    return LazyOptional.of(() -> this.tankWater).cast();
                }
                if(facing == direction.rotateY())
                {
                    return LazyOptional.of(() -> this.tankFilteredWater).cast();
                }
            }
            return LazyOptional.empty();
        }
        else if(!this.removed && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return this.itemHandler.cast();
        }
        return super.getCapability(cap, facing);
    }
}