package com.kingparity.betterpets.crafting;

import com.kingparity.betterpets.util.Reference;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class RecipeType
{
    public static final IRecipeType<WaterFilterRecipe> WATER_FILTER = register(Reference.ID + ":water_filter");

    static <T extends IRecipe<?>> IRecipeType<T> register(final String key)
    {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(key), new IRecipeType<T>()
        {
            @Override
            public String toString()
            {
                return key;
            }
        });
    }
}
