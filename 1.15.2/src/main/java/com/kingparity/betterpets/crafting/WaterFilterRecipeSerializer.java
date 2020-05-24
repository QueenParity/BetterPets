package com.kingparity.betterpets.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;

public class WaterFilterRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WaterFilterRecipe>
{
    @Override
    public WaterFilterRecipe read(ResourceLocation recipeId, JsonObject json)
    {
        if(!json.has("input"))
        {
            throw new com.google.gson.JsonSyntaxException("Missing input, expected to find a fluid entry");
        }
        FluidEntry fluidInput = FluidEntry.deserialize(json.getAsJsonObject("input"));

        if(!json.has("ingredient"))
        {
            throw new com.google.gson.JsonSyntaxException("Missing ingredient, expected to find an item");
        }
        ItemStack ingredient = CraftingHelper.getItemStack(json.getAsJsonObject("ingredient"), false);

        if(!json.has("result"))
        {
            throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a fluid entry");
        }
        FluidEntry result = FluidEntry.deserialize(json.getAsJsonObject("result"));

        return new WaterFilterRecipe(recipeId, fluidInput, ingredient, result);
    }

    @Nullable
    @Override
    public WaterFilterRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
    {
        FluidEntry fluidInput = FluidEntry.read(buffer);
        ItemStack ingredient = buffer.readItemStack();
        FluidEntry result = FluidEntry.read(buffer);
        return new WaterFilterRecipe(recipeId, fluidInput, ingredient, result);
    }

    @Override
    public void write(PacketBuffer buffer, WaterFilterRecipe recipe)
    {
        recipe.getInput().write(buffer);
        buffer.writeItemStack(recipe.getIngredient());
        recipe.getResult().write(buffer);
    }
}
