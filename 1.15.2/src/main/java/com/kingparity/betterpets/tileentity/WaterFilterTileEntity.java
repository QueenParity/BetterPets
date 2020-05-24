package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.block.PetHorizontalBlock;
import com.kingparity.betterpets.core.ModFluids;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.crafting.RecipeType;
import com.kingparity.betterpets.crafting.WaterFilterRecipe;
import com.kingparity.betterpets.inventory.container.WaterFilterContainer;
import com.kingparity.betterpets.util.InventoryUtil;
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
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WaterFilterTileEntity extends TileEntitySynced implements IInventory, ITickableTileEntity, INamedContainerProvider, IFluidTankWriter
{
    private NonNullList<ItemStack> inventory = NonNullList.withSize(7, ItemStack.EMPTY);

    private FluidTank tankWater = new FluidTank(5000, this::isValidFluid);
    private FluidTank tankFilteredWater = new FluidTank(10000, stack -> stack.getFluid() == ModFluids.FILTERED_WATER);

    private static final int SLOT_FUEL = 0;
    public static final int SLOT_FABRIC = 1;

    private WaterFilterRecipe currentRecipe = null;
    private int remainingFilteredWater;
    private int filterMaxProgress;
    private int filterProgress;

    private String customName;

    protected final IIntArray waterFilterData = new IIntArray()
    {
        public int get(int index)
        {
            switch(index)
            {
                case 0:
                    return filterProgress;
                case 1:
                    return remainingFilteredWater;
                case 2:
                    return filterMaxProgress;
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

        public void set(int index, int value)
        {
            switch(index)
            {
                case 0:
                    filterProgress = value;
                    break;
                case 1:
                    remainingFilteredWater = value;
                    break;
                case 2:
                    filterMaxProgress = value;
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

        public int size()
        {
            return 7;
        }
    };

    public WaterFilterTileEntity()
    {
        super(ModTileEntities.WATER_FILTER);
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
        else if(index == 1)
        {
            return this.isValidIngredient(stack);
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
            ItemStack fabric = this.getStackInSlot(SLOT_FABRIC);
            ItemStack fuel = this.getStackInSlot(SLOT_FUEL);

            if(this.currentRecipe == null && !fabric.isEmpty())
            {
                this.currentRecipe = this.getRecipe().orElse(null);
            }
            else if(!this.canFilter(this.currentRecipe))
            {
                this.currentRecipe = null;
                this.filterProgress = 0;
            }

            if(this.canFilter(this.currentRecipe))
            {
                this.updateFuel(fuel);

                if(this.remainingFilteredWater > 0)
                {
                    if(this.filterProgress++ == 100)
                    {
                        WaterFilterRecipe recipe = this.currentRecipe;
                        this.tankFilteredWater.fill(recipe.getResult().createStack(), IFluidHandler.FluidAction.EXECUTE);
                        this.tankWater.drain(recipe.getFluidAmount(this.tankWater.getFluid().getFluid()), IFluidHandler.FluidAction.EXECUTE);
                        //this.tankEnderSap.drain(recipe.getFluidAmount(this.tankEnderSap.getFluid().getFluid()), IFluidHandler.FluidAction.EXECUTE);
                        this.shrinkItem(SLOT_FABRIC);
                        this.filterProgress = 0;
                        this.currentRecipe = null;
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

            if(this.remainingFilteredWater > 0)
            {
                this.remainingFilteredWater--;
                this.updateFuel(fuel);
            }
        }
    }

    private void updateFuel(ItemStack fuel)
    {
        if(!fuel.isEmpty() && ForgeHooks.getBurnTime(fuel) > 0 && this.remainingFilteredWater == 0 && this.canFilter(this.currentRecipe))
        {
            this.filterMaxProgress = ForgeHooks.getBurnTime(fuel);
            this.remainingFilteredWater = this.filterMaxProgress;
            this.shrinkItem(SLOT_FUEL);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean canFilter()
    {
        ItemStack ingredient = this.getStackInSlot(SLOT_FABRIC);
        if(!ingredient.isEmpty() && !this.tankWater.getFluid().isEmpty())
        {
            if(this.currentRecipe == null)
            {
                this.currentRecipe = this.getRecipe().orElse(null);
            }
        }
        else
        {
            this.currentRecipe = null;
        }
        return this.currentRecipe != null && this.canFilter(this.currentRecipe) && this.remainingFilteredWater >= 0;
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

    private boolean canFilter(@Nullable WaterFilterRecipe recipe)
    {
        if(recipe == null)
            return false;
        ItemStack ingredient = this.getStackInSlot(SLOT_FABRIC);
        if(ingredient.getItem() != recipe.getIngredient().getItem())
            return false;
        if(this.tankWater.getFluid().isEmpty())
            return false;
        if(this.tankWater.getFluidAmount() < recipe.getFluidAmount(this.tankWater.getFluid().getFluid()))
            return false;
        if(this.tankFilteredWater.getFluidAmount() >= this.tankFilteredWater.getCapacity())
            return false;
        return this.tankFilteredWater.getFluidAmount() + recipe.getResult().getAmount() <= this.tankFilteredWater.getCapacity();
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
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
            //FluidUtils.fixEmptyTag(tagCompound); //TODO might not need
            this.tankWater.readFromNBT(tagCompound);
        }
        if(compound.contains("TankFilteredWater", Constants.NBT.TAG_COMPOUND))
        {
            CompoundNBT tagCompound = compound.getCompound("TankFilteredWater");
            //FluidUtils.fixEmptyTag(tagCompound);
            this.tankFilteredWater.readFromNBT(tagCompound);
        }
        if(compound.contains("RemainingFilteredWater", Constants.NBT.TAG_INT))
        {
            this.remainingFilteredWater = compound.getInt("RemainingFilteredWater");
        }
        if(compound.contains("FilterMaxProgress", Constants.NBT.TAG_INT))
        {
            this.filterMaxProgress = compound.getInt("FilterMaxProgress");
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

        compound.putInt("RemainingFilteredWater", this.remainingFilteredWater);
        compound.putInt("FilterMaxProgress", this.filterMaxProgress);
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

    public int getRemainingFilteredWater()
    {
        return this.waterFilterData.get(1);
    }

    public int getFilterMaxProgress()
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

    public Optional<WaterFilterRecipe> getRecipe()
    {
        return this.world.getRecipeManager().getRecipe(RecipeType.WATER_FILTER, this, this.world);
    }

    private boolean isValidIngredient(ItemStack ingredient)
    {
        List<WaterFilterRecipe> recipes = this.world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == RecipeType.WATER_FILTER).map(recipe -> (WaterFilterRecipe) recipe).collect(Collectors.toList());
        return recipes.stream().anyMatch(recipe -> InventoryUtil.areItemStacksEqualIgnoreCount(ingredient, recipe.getIngredient()));
    }

    private boolean isValidFluid(FluidStack stack)
    {
        List<WaterFilterRecipe> recipes = this.world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == RecipeType.WATER_FILTER).map(recipe -> (WaterFilterRecipe) recipe).collect(Collectors.toList());
        return recipes.stream().anyMatch(recipe ->
        {
            if(recipe.getInput().getFluid() == stack.getFluid())
            {
                return true;
            }
            return false;
        });
    }

    public FluidTank getWaterTank()
    {
        return tankWater;
    }

    public FluidTank getFilteredWaterTank()
    {
        return tankFilteredWater;
    }

    private final net.minecraftforge.common.util.LazyOptional<?> itemHandler = net.minecraftforge.common.util.LazyOptional.of(this::createUnSidedHandler);

    @Nonnull
    protected net.minecraftforge.items.IItemHandler createUnSidedHandler()
    {
        return new net.minecraftforge.items.wrapper.InvWrapper(this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction facing)
    {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            BlockState state = this.world.getBlockState(this.pos);
            if(state.getProperties().contains(PetHorizontalBlock.DIRECTION))
            {
                Direction direction = state.get(PetHorizontalBlock.DIRECTION);
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