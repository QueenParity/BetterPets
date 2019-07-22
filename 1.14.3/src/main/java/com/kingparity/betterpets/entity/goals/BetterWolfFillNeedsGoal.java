package com.kingparity.betterpets.entity.goals;

import com.kingparity.betterpets.core.ModSounds;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;

public class BetterWolfFillNeedsGoal extends Goal
{
    private final BetterWolfEntity betterWolf;
    private int timeoutCounter;
    private int tickCounter;
    
    public BetterWolfFillNeedsGoal(BetterWolfEntity betterWolf)
    {
        this.betterWolf = betterWolf;
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        return this.betterWolf.getPetThirstStats().thirstLevel < 20.0F;
    }
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return this.shouldExecute() && timeoutCounter > 0;
    }
    
    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.timeoutCounter = 500 + this.betterWolf.getRNG().nextInt(500);
        this.tickCounter = 250;
    }
    
    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask()
    {
        super.resetTask();
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick()
    {
        boolean outOfWater = true;
        for(int i = 0; i < this.betterWolf.getInventory().getSizeInventory(); i++)
        {
            ItemStack stack = this.betterWolf.getStackInSlot(i);
            if(stack.hasTag())
            {
                if(stack.getTag().contains("Amount"))
                {
                    int amount = stack.getTag().getInt("Amount");
                    if(amount > 0)
                    {
                        outOfWater = false;
                        break;
                    }
                }
            }
        }
        --this.tickCounter;
        if(outOfWater && this.betterWolf.getPetThirstStats().thirstLevel < 10.0F && tickCounter <= 0)
        {
            this.betterWolf.playSound(ModSounds.BETTER_WOLF_BARK, this.betterWolf.getSoundVolume(), this.betterWolf.getSoundPitch());
            this.tickCounter = 250;
        }
        --this.timeoutCounter;
    }
}