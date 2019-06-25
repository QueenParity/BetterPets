package com.kingparity.betterpets.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PetFoodItem extends Item
{
    private int food_points;
    private float saturation_restored;
    
    public PetFoodItem(Item.Properties properties)
    {
        this(properties, 0, 0);
    }
    
    public PetFoodItem(Item.Properties properties, int food_points, float saturation_restored)
    {
        super(properties);
        this.food_points = food_points;
        this.saturation_restored = saturation_restored;
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        tooltip.add(new TranslationTextComponent(TextFormatting.GREEN + I18n.format("betterpets.pet_food_info.food_points") + ": " + TextFormatting.RESET + this.food_points));
        tooltip.add(new TranslationTextComponent(TextFormatting.GREEN + I18n.format("betterpets.pet_food_info.saturation_restored") + ": " + TextFormatting.RESET + this.saturation_restored));
    }
    
    public void setFoodPoints(int food_points)
    {
        this.food_points = food_points;
    }
    
    public void setSaturationRestored(float saturation_restored)
    {
        this.saturation_restored = saturation_restored;
    }
    
    public int getFoodPoints()
    {
        return food_points;
    }
    
    public float getSaturationRestored()
    {
        return saturation_restored;
    }
}