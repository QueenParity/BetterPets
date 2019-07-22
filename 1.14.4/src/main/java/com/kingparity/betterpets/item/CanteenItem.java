package com.kingparity.betterpets.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CanteenItem extends Item
{
    public CanteenItem(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        if(stack.hasTag())
        {
            boolean isFiltered = stack.getTag().getBoolean("isFiltered");
            int liquidAmount = stack.getTag().getInt("liquidAmount");
            int maxLiquid = stack.getTag().getInt("maxLiquid");
            float poisonChance = stack.getTag().getFloat("poisonChance");
    
            String isFilteredStr = (Boolean.toString(isFiltered)).substring(0, 1).toUpperCase() + Boolean.toString(isFiltered).substring(1);
            tooltip.add(new TranslationTextComponent(TextFormatting.GREEN + I18n.format("betterpets.canteen_info.isFiltered") + ": " + TextFormatting.RESET + isFilteredStr));
            tooltip.add(new TranslationTextComponent(TextFormatting.GREEN + I18n.format("betterpets.canteen_info.liquidAmount") + ": " + TextFormatting.RESET + liquidAmount));
            tooltip.add(new TranslationTextComponent(TextFormatting.GREEN + I18n.format("betterpets.canteen_info.maxLiquid") + ": " + TextFormatting.RESET + maxLiquid));
            tooltip.add(new TranslationTextComponent(TextFormatting.GREEN + I18n.format("betterpets.canteen_info.poisonChance") + ": " + TextFormatting.RESET + poisonChance));
        }
    }
    
    /*@Override
    public ItemStack onItemUseFinish(ItemStack canteen, World world, LivingEntity entity)
    {
        int liquidAmount = canteen.getTag().getInt("liquidAmount");
        if(entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity)entity;
            if(liquidAmount > 0)
            {
                int thirstReplenish = canteen.getTag().getInt("thirstReplenishAmount");
                float saturationReplenish = canteen.getTag().getFloat("saturationReplenishAmount");
                float poisonChance = canteen.getTag().getFloat("poisonChance");
                PetThirstStats stats = BetterPetMod.PROXY.getStatsByUUID(player.getUniqueID());
                stats.addStats(thirstReplenish, saturationReplenish);
                stats.attemptToPoison(poisonChance);
                return new ItemStack(ModItems.CANTEEN);
            }
        }
        return canteen;
    }*/
    
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
    
    /*@Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        int liquidAmount = player.getHeldItem(hand).getTag().getInt("liquidAmount");
        if(liquidAmount > 0)
        {
            PetThirstStats stats = world.isRemote ? BetterPetMod.PROXY.getClientStats() : BetterPetMod.PROXY.getStatsByUUID(player.getUniqueID());
            if(stats.canDrink() || player.abilities.isCreativeMode)
            {
                player.setActiveHand(hand);
            }
        }
        return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }*/
}
