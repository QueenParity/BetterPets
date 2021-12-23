package com.kingparity.betterpets.blockentity;

import com.kingparity.betterpets.block.RotatedBlockObjectEntity;
import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.init.ModFluids;
import com.kingparity.betterpets.inventory.menu.WaterFilterMenu;
import com.kingparity.betterpets.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
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

public class WaterFilterBlockEntity extends BaseContainerBlockEntity implements IFluidTankWriter
{
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    
    private FluidTank tankWater = new FluidTank(FluidAttributes.BUCKET_VOLUME * 7, stack -> stack.getFluid() == Fluids.WATER)
    {
        @Override
        protected void onContentsChanged()
        {
            WaterFilterBlockEntity.this.syncToClient();
        }
    };
    
    private FluidTank tankFilteredWater = new FluidTank(FluidAttributes.BUCKET_VOLUME * 7, stack -> stack.getFluid() == ModFluids.FILTERED_WATER.get())
    {
        @Override
        protected void onContentsChanged()
        {
            WaterFilterBlockEntity.this.syncToClient();
        }
    };
    
    private static final int SLOT_FUEL = 0;
    
    private int remainingFuel;
    private int fuelMaxProgress;
    private int filterProgress;
    
    protected final ContainerData dataAccess = new ContainerData()
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
                default:
                    return 0;
            }
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
        public int getCount()
        {
            return 2;
        }
    };
    
    public WaterFilterBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.WATER_FILTER.get(), pos, state);
    }
    
    @Override
    public int getContainerSize()
    {
        return 2;
    }
    
    @Override
    public boolean isEmpty()
    {
        for(ItemStack stack : this.items)
        {
            if(!stack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getItem(int index)
    {
        return this.items.get(index);
    }
    
    @Override
    public ItemStack removeItem(int index, int count)
    {
        return ContainerHelper.removeItem(this.items, index, count);
    }
    
    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        return ContainerHelper.takeItem(this.items, index);
    }
    
    @Override
    public void setItem(int index, ItemStack stack)
    {
        this.items.set(index, stack);
        if(stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }
    
    @Override
    public boolean stillValid(Player player)
    {
        if(this.level.getBlockEntity(this.worldPosition) != this)
        {
            return false;
        }
        else
        {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }
    
    @Override
    public void clearContent()
    {
        this.items.clear();
    }
    
    public void syncToClient()
    {
        this.setChanged();
        BlockEntityUtil.sendUpdatePacket(this, this.saveWithFullMetadata());
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, WaterFilterBlockEntity blockEntity)
    {
        if(!level.isClientSide)
        {
            ItemStack fuel = blockEntity.getItem(SLOT_FUEL);
            
            if(!blockEntity.canFilter())
            {
                blockEntity.filterProgress = 0;
            }
        
            if(blockEntity.canFilter())
            {
                blockEntity.updateFuel(fuel);
            
                if(blockEntity.remainingFuel > 0)
                {
                    if(blockEntity.filterProgress++ == 58)
                    {
                        blockEntity.tankFilteredWater.fill(new FluidStack(ModFluids.FILTERED_WATER.get(), 100), IFluidHandler.FluidAction.EXECUTE);
                        blockEntity.tankWater.drain(100, IFluidHandler.FluidAction.EXECUTE);
                        blockEntity.filterProgress = 0;
                        blockEntity.syncToClient();
                    }
                }
                else
                {
                    blockEntity.filterProgress = 0;
                }
            }
            else
            {
                blockEntity.filterProgress = 0;
            }
            
            if(blockEntity.remainingFuel > 0)
            {
                blockEntity.remainingFuel--;
                blockEntity.updateFuel(fuel);
            }
        }
    }
    
    private void updateFuel(ItemStack fuel)
    {
        if(!fuel.isEmpty() && ForgeHooks.getBurnTime(fuel, null) > 0 && this.remainingFuel == 0 && this.canFilter())
        {
            this.fuelMaxProgress = ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);
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
        ItemStack stack = this.getItem(index);
        stack.shrink(1);
        if(stack.isEmpty())
        {
            this.setItem(index, ItemStack.EMPTY);
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
    public void load(CompoundTag compound)
    {
        super.load(compound);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.items);
        this.tankWater.readFromNBT(compound.getCompound("TankWater"));
        this.tankFilteredWater.readFromNBT(compound.getCompound("TankFilteredWater"));
        this.remainingFuel = compound.getInt("RemainingFuel");
        this.fuelMaxProgress = compound.getInt("FuelMaxProgress");
        this.filterProgress = compound.getInt("FilterProgress");
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound)
    {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, this.items);
        
        this.writeTanks(compound);
    
        compound.putInt("RemainingFuel", this.remainingFuel);
        compound.putInt("FuelMaxProgress", this.fuelMaxProgress);
        compound.putInt("FilterProgress", this.filterProgress);
    }
    
    @Override
    public void writeTanks(CompoundTag compound)
    {
        CompoundTag tagTankWater = new CompoundTag();
        this.tankWater.writeToNBT(tagTankWater);
        compound.put("TankWater", tagTankWater);
    
        CompoundTag tagTankFilteredWater = new CompoundTag();
        this.tankFilteredWater.writeToNBT(tagTankFilteredWater);
        compound.put("TankFilteredWater", tagTankFilteredWater);
    }
    
    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }
    
    @org.jetbrains.annotations.Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        this.load(pkt.getTag());
    }
    
    @Override
    public boolean areTanksEmpty()
    {
        return this.tankWater.isEmpty() && this.tankFilteredWater.isEmpty();
    }
    
    @Override
    protected Component getDefaultName()
    {
        return new TranslatableComponent("container.water_filter");
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
        return this.dataAccess.get(0);
    }
    
    public int getRemainingFuel()
    {
        return this.dataAccess.get(1);
    }
    
    public int getFuelMaxProgress()
    {
        return this.dataAccess.get(2);
    }
    
    public int getWaterLevel()
    {
        return this.dataAccess.get(3);
    }
    
    public int getFilteredWaterLevel()
    {
        return this.dataAccess.get(4);
    }
    
    public ContainerData getWaterFilterData()
    {
        return dataAccess;
    }
    
    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory inventory)
    {
        return new WaterFilterMenu(windowId, inventory, this);
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
            BlockState state = this.level.getBlockState(this.worldPosition);
            if(state.getProperties().contains(RotatedBlockObjectEntity.DIRECTION))
            {
                Direction direction = state.getValue(RotatedBlockObjectEntity.DIRECTION);
                if(facing == direction.getCounterClockWise())
                {
                    return LazyOptional.of(() -> this.tankWater).cast();
                }
                if(facing == direction.getClockWise())
                {
                    return LazyOptional.of(() -> this.tankFilteredWater).cast();
                }
            }
            return LazyOptional.empty();
        }
        else if(!this.remove && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return this.itemHandler.cast();
        }
        return super.getCapability(cap, facing);
    }
}