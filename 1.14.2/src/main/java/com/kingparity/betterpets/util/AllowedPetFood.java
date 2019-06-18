package com.kingparity.betterpets.util;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;

import javax.annotation.Nullable;

public enum AllowedPetFood
{
    APPLE(Items.APPLE, 4, 2.4F),
    RAW_PORKCHOP(Items.PORKCHOP, 3, 1.8F),
    COOKED_PORKCHOP(Items.COOKED_PORKCHOP, 8, 12.8F),
    GOLDEN_APPLE(Items.GOLDEN_APPLE, 4, 9.6F),
    ENCHANTED_GOLDEN_APPLE(Items.ENCHANTED_GOLDEN_APPLE, 4, 9.6F),
    RAW_COD(Items.COD, 2, 0.4F),
    RAW_SALMON(Items.SALMON, 2, 0.4F),
    TROPICAL_FISH(Items.TROPICAL_FISH, 1, 0.2F),
    COOKED_COD(Items.COOKED_COD, 5, 6.0F),
    COOKED_SALMON(Items.COOKED_SALMON, 6, 9.6F),
    MELON_SLICE(Items.MELON_SLICE, 2, 1.2F),
    DRIED_KELP(Items.DRIED_KELP, 1, 0.6F),
    RAW_BEEF(Items.BEEF, 3, 1.8F),
    STEAK(Items.COOKED_BEEF, 8, 12.8F),
    RAW_CHICKEN(Items.CHICKEN, 2, 1.2F),
    COOKED_CHICKEN(Items.COOKED_CHICKEN, 6, 7.2F),
    ROTTEN_FLESH(Items.ROTTEN_FLESH, 4, 0.8F),
    SPIDER_EYE(Items.SPIDER_EYE, 2, 3.2F),
    CARROT(Items.CARROT, 3, 3.6F),
    POTATO(Items.POTATO, 1, 0.6F),
    BAKED_POTATO(Items.BAKED_POTATO, 5, 6.0F),
    PUMPKIN_PIE(Items.PUMPKIN_PIE, 8, 4.8F),
    RAW_RABBIT(Items.RABBIT, 3, 1.8F),
    COOKED_RABBIT(Items.COOKED_RABBIT, 5, 6.0F),
    RAW_MUTTON(Items.MUTTON, 2, 1.2F),
    COOKED_MUTTON(Items.COOKED_MUTTON, 6, 9.6F),
    BEETROOT(Items.BEETROOT, 1, 1.2F),
    SWEET_BERRIES(Items.SWEET_BERRIES, 2, 0.4F),
    BROWN_MUSHROOM(Items.BROWN_MUSHROOM, 5, 5.8F),
    RED_MUSHROOM(Items.RED_MUSHROOM, 5, 5.8F),
    GOLDEN_CARROT(Items.GOLDEN_CARROT, 6, 14.4F),
    WHEAT_SEEDS(Items.WHEAT_SEEDS, 1, 0.2F),
    WHEAT(Items.WHEAT, 2, 0.6F),
    MILK_BUCKET(Items.MILK_BUCKET, 3, 6.0F),
    SALMON_BUCKET(Items.SALMON_BUCKET, 2, 0.4F),
    COD_BUCKET(Items.COD_BUCKET, 2, 0.4F),
    TROPICAL_FISH_BUCKET(Items.TROPICAL_FISH_BUCKET, 1, 0.2F),
    SUGAR_CANE(Items.SUGAR_CANE, 2, 0.2F),
    KELP(Items.KELP, 2, 1.0F),
    BAMBOO(Items.BAMBOO, 1, 0.1F),
    EGG(Items.EGG, 7, 7.0F),
    BONE(Items.BONE, 6, 3.0F),
    SUGAR(Items.SUGAR, 1, 0.0F),
    PUMPKIN_SEEDS(Items.PUMPKIN_SEEDS, 1, 0.1F),
    MELON_SEEDS(Items.MELON_SEEDS, 1, 0.1F),
    NETHER_WART(Items.NETHER_WART, 1, 0.1F),
    RABBIT_HIDE(Items.RABBIT_HIDE, 1, 0.1F),
    BEETROOT_SEEDS(Items.BEETROOT_SEEDS, 1, 0.1F);
    
    private final Item item;
    private final int food_points;
    private final float saturation_restored, effective_quality, saturation_ratio;
    private final Effect[] effects;
    
    AllowedPetFood(Item item, int food_points, float saturation_restored)
    {
        this(item, food_points, saturation_restored, (Effect[])null);
    }
    
    AllowedPetFood(Item item, int food_points, float saturation_restored, @Nullable Effect... effects)
    {
        this.item = item;
        this.food_points = food_points;
        this.saturation_restored = saturation_restored;
        this.effective_quality = food_points + saturation_restored;
        this.saturation_ratio = saturation_restored / food_points;
        if(effects != null)
        {
            this.effects = effects;
        }
        else
        {
            this.effects = new Effect[]{};
        }
    }
    
    public Item getItem()
    {
        return item;
    }
    
    public int getFoodPoints()
    {
        return food_points;
    }
    
    public float getSaturationRestored()
    {
        return saturation_restored;
    }
    
    public float getEffectiveQuality()
    {
        return effective_quality;
    }
    
    public float getSaturationRatio()
    {
        return saturation_ratio;
    }
    
    public Effect[] getEffects()
    {
        return effects;
    }
}
