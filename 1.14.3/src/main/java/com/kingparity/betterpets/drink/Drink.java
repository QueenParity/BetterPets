package com.kingparity.betterpets.drink;

import com.kingparity.betterpets.core.ModRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class Drink extends ForgeRegistryEntry<Drink>
{
    private int thirstReplenish;
    private float saturationReplenish;
    private int drinkColor;
    
    private boolean alwaysDrinkable;
    private boolean shiny;
    private String recipeItem;
    private int manufactureTime;
    private float poisonChance;
    
    private String name;
    
    public static Drink getDrinkTypeForName(String name)
    {
        return ModRegistries.DRINKS.getValue(ResourceLocation.tryCreate(name));
    }
    
    public Drink(int thirstReplenish, float saturationReplenish, int drinkColor)
    {
        this.thirstReplenish = thirstReplenish;
        this.saturationReplenish = saturationReplenish;
        this.drinkColor = drinkColor;
    }
    
    public String getNamePrefixed(String prefix)
    {
        return prefix + ModRegistries.DRINKS.getKey(this).getPath();
    }
    
    public String getName()
    {
        return this.getOrCreateDescriptionId();
    }
    
    protected String getOrCreateDescriptionId()
    {
        if (this.name == null)
        {
            this.name = Util.makeTranslationKey("drink", ModRegistries.DRINKS.getKey(this));
        }
        
        return this.name;
    }
    
    public int getThirstReplenish()
    {
        return thirstReplenish;
    }
    
    public float getSaturationReplenish()
    {
        return saturationReplenish;
    }
    
    public int getDrinkColor()
    {
        return drinkColor;
    }
    
    public boolean isAlwaysDrinkable()
    {
        return alwaysDrinkable;
    }
    
    public boolean isShiny()
    {
        return shiny;
    }
    
    public String getRecipeItem()
    {
        return recipeItem;
    }
    
    public int getManufactureTime()
    {
        return manufactureTime;
    }
    
    public float getPoisonChance()
    {
        return poisonChance;
    }
}