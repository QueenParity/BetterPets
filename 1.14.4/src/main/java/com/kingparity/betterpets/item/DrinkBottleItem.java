package com.kingparity.betterpets.item;

import com.kingparity.betterpets.core.ModDrinks;
import com.kingparity.betterpets.core.ModRegistries;
import com.kingparity.betterpets.drink.Drink;
import com.kingparity.betterpets.drink.DrinkUtils;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.stats.thirst.PetThirstStats;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DrinkBottleItem extends Item
{
    private static final int capacity = 1000;
    
    public DrinkBottleItem(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        if(stack.getTag().contains("Drink"))
        {
            Drink drink = Drink.getDrinkTypeForName(stack.getTag().getString("Drink"));
            tooltip.add(new TranslationTextComponent(drink.getName()).applyTextStyle(TextFormatting.BLUE));
            tooltip.add(new StringTextComponent(stack.getTag().getInt("Amount") + "/" + stack.getTag().getInt("Capacity") + " mB").applyTextStyle(TextFormatting.BLUE));
        }
        else
        {
            tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + I18n.format("drink.none")));;
        }
    }
    
    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 32;
    }
    
    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.DRINK;
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity livingEntity)
    {
        BetterWolfEntity betterWolf  = livingEntity instanceof BetterWolfEntity ? (BetterWolfEntity)livingEntity : null;
        this.onDrinkItem(betterWolf, stack);
        CompoundNBT tag = stack.getTag();
        tag.putInt("Amount", tag.getInt("Amount") - 5);
        return stack;
    }
    
    private void onDrinkItem(BetterWolfEntity betterWolf, ItemStack stack)
    {
        if(!betterWolf.world.isRemote)
        {
            PetThirstStats stats = betterWolf.getPetThirstStats();
            Drink drink = DrinkUtils.getDrinkFromItem(stack);
            stats.addStats(drink.getThirstReplenish(), drink.getSaturationReplenish());
            stats.attemptToPoison(drink.getPoisonChance());
            betterWolf.addStat(Stats.ITEM_USED.get(this));
        }
    }
    
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if(this.isInGroup(group))
        {
            for(Drink drink : ModRegistries.DRINKS)
            {
                if(drink != ModDrinks.EMPTY)
                {
                    items.add(DrinkUtils.setDrinkInItemStack(new ItemStack(this), drink, capacity));
                }
            }
        }
    }
    
    public static class ColorHandler implements IItemColor
    {
        @Override
        public int getColor(ItemStack stack, int tintIndex)
        {
            Item item = stack.getItem();
            if(item instanceof DrinkBottleItem)
            {
                if(tintIndex == 0)
                {
                    return DrinkUtils.getDrinkFromItem(stack).getDrinkColor();
                }
                return 16777215;
            }
            else
            {
                throw new IllegalStateException("You're trying to register the wrong item! " + item.getRegistryName());
            }
        }
    }
}