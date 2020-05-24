package com.kingparity.betterpets.crafting;

import com.kingparity.betterpets.core.ModRecipeSerializers;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.InventoryUtil;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Objects;

public class WaterFilterRecipe implements IRecipe<WaterFilterTileEntity>
{
    private ResourceLocation id;
    private FluidEntry input;
    private ItemStack ingredient;
    private FluidEntry result;
    private int hashCode;

    public WaterFilterRecipe(ResourceLocation id, FluidEntry input, ItemStack ingredient, FluidEntry result)
    {
        this.id = id;
        this.input = input;
        this.ingredient = ingredient;
        this.result = result;
    }

    public FluidEntry getInput()
    {
        return input;
    }

    public ItemStack getIngredient()
    {
        return this.ingredient;
    }

    public FluidEntry getResult()
    {
        return result;
    }

    public int getFluidAmount(Fluid fluid)
    {
        FluidEntry entry = this.input;
        if(entry.getFluid().equals(fluid))
        {
            return entry.getAmount();
        }
        return -1;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof WaterFilterRecipe)) return false;
        WaterFilterRecipe other = (WaterFilterRecipe) obj;
        if(!other.input.getFluid().equals(this.input.getFluid())) return false;
        return InventoryUtil.areItemStacksEqualIgnoreCount(other.ingredient, this.ingredient);
    }

    @Override
    public int hashCode()
    {
        if(this.hashCode == 0)
        {
            this.hashCode = Objects.hash(this.input.getFluid().getRegistryName(), this.ingredient.getItem().getRegistryName());
        }
        return this.hashCode;
    }

    @Override
    public boolean matches(WaterFilterTileEntity waterFilter, World world)
    {
        if(waterFilter.getWaterTank().isEmpty())
            return false;
        Fluid input = waterFilter.getWaterTank().getFluid().getFluid();
        if(!input.equals(this.input.getFluid())) return false;
        return InventoryUtil.areItemStacksEqualIgnoreCount(waterFilter.getStackInSlot(WaterFilterTileEntity.SLOT_FABRIC), this.ingredient);
    }

    @Override
    public ItemStack getCraftingResult(WaterFilterTileEntity inv)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId()
    {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return ModRecipeSerializers.WATER_FILTER;
    }

    @Override
    public IRecipeType<?> getType()
    {
        return RecipeType.WATER_FILTER;
    }
}