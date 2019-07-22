package com.kingparity.betterpets.drink;

import com.kingparity.betterpets.core.ModDrinks;
import com.kingparity.betterpets.core.ModRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class DrinkUtils
{
    public static ItemStack setDrinkInItemStack(ItemStack stack, Drink drink, int capacity)
    {
        ResourceLocation location = ModRegistries.DRINKS.getKey(drink);
        if(drink == ModDrinks.EMPTY)
        {
            stack.removeChildTag("Drink");
        }
        else
        {
            stack.getOrCreateTag().putString("Drink", location.toString());
            stack.getOrCreateTag().putInt("Amount", capacity);
            stack.getOrCreateTag().putInt("Capacity", capacity);
        }
        
        return stack;
    }
    
    public static Drink getDrinkFromItem(ItemStack stack)
    {
        return getDrinkFromNBT(stack.getTag());
    }
    
    private static Drink getDrinkFromNBT(@Nullable CompoundNBT tag)
    {
        return tag == null ? ModDrinks.EMPTY : Drink.getDrinkTypeForName(tag.getString("Drink"));
    }
}