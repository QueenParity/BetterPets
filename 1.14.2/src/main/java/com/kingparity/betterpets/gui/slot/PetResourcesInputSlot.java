package com.kingparity.betterpets.gui.slot;

import com.kingparity.betterpets.util.AllowedPetFood;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class PetResourcesInputSlot extends Slot
{
    public PetResourcesInputSlot(IInventory inventory, int index, int xPosition, int yPosition)
    {
        super(inventory, index, xPosition, yPosition);
    }
    
    @Override
    public boolean isItemValid(ItemStack stack)
    {
        /*Item[] badFood = {Items.MUSHROOM_STEW, Items.BREAD, Items.PUFFERFISH, Items.CAKE, Items.COOKIE, Items.POISONOUS_POTATO, Items.RABBIT_STEW, Items.BEETROOT_SOUP};
        Item[] goodFood = {Items.BROWN_MUSHROOM, Items.RED_MUSHROOM,
                //Items.TUBE_CORAL, Items.BRAIN_CORAL, Items.BUBBLE_CORAL, Items.FIRE_CORAL, Items.HORN_CORAL,
                //Items.DEAD_BRAIN_CORAL, Items.DEAD_BUBBLE_CORAL, Items.DEAD_FIRE_CORAL, Items.DEAD_HORN_CORAL, Items.DEAD_TUBE_CORAL,
                //Items.TUBE_CORAL_FAN, Items.BRAIN_CORAL_FAN, Items.BUBBLE_CORAL_FAN, Items.FIRE_CORAL_FAN, Items.HORN_CORAL_FAN,
                //Items.DEAD_TUBE_CORAL_FAN, Items.DEAD_BRAIN_CORAL_FAN, Items.DEAD_BUBBLE_CORAL_FAN, Items.DEAD_FIRE_CORAL_FAN, Items.DEAD_HORN_CORAL_FAN,
                Items.GOLDEN_CARROT, Items.WHEAT_SEEDS, Items.WHEAT, Items.MILK_BUCKET, Items.SALMON_BUCKET, Items.COD_BUCKET, Items.TROPICAL_FISH_BUCKET,
                Items.SUGAR_CANE, Items.KELP, Items.BAMBOO, Items.EGG, Items.BONE, Items.SUGAR,
                Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.NETHER_WART, Items.RABBIT_HIDE, Items.BEETROOT_SEEDS};
        Item item = stack.getItem();
        if(item.getGroup() == ItemGroup.FOOD)
        {
            for(Item bad : badFood)
            {
                if(item != bad)
                {
                    return true;
                }
            }
        }
        else
        {
            for(Item good : goodFood)
            {
                if(item == good)
                {
                    return true;
                }
            }
        }*/
        for(AllowedPetFood petFood : AllowedPetFood.values())
        {
            if(petFood.getItem() == stack.getItem())
            {
                return true;
            }
        }
        return false;
    }
}