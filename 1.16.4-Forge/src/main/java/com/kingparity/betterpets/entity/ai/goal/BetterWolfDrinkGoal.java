package com.kingparity.betterpets.entity.ai.goal;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.entity.ai.goal.Goal;

public class BetterWolfDrinkGoal extends Goal
{
    private final BetterWolfEntity betterWolf;
    
    public BetterWolfDrinkGoal(BetterWolfEntity betterWolf)
    {
        this.betterWolf = betterWolf;
    }
    
    @Override
    public boolean shouldExecute()
    {
        return this.betterWolf.getPetThirstStats().thirstLevel < 6 && this.betterWolf.isInWaterRainOrBubbleColumn();
    }
    
    @Override
    public boolean shouldContinueExecuting()
    {
        return this.betterWolf.getPetThirstStats().thirstLevel < 18 && this.betterWolf.isInWaterRainOrBubbleColumn();
    }
    
    @Override
    public void tick()
    {
        System.out.println("drink");
        this.betterWolf.getPetThirstStats().thirstLevel += 1;
    }
}