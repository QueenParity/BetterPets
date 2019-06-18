package com.kingparity.betterpets.entity.goals;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BetterWolfSleepGoal extends Goal
{
    private final BetterWolfEntity betterWolf;
    private boolean isSleeping;
    
    public BetterWolfSleepGoal(BetterWolfEntity betterWolf)
    {
        this.betterWolf = betterWolf;
        this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
    }
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return this.isSleeping;
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if(!this.betterWolf.isTamed())
        {
            return false;
        }
        else if(this.betterWolf.isInWaterOrBubbleColumn())
        {
            return false;
        }
        else if(!this.betterWolf.onGround)
        {
            return false;
        }
        else
        {
            LivingEntity livingentity = this.betterWolf.getOwner();
            if(livingentity == null)
            {
                return true;
            }
            else
            {
                return (!(this.betterWolf.getDistanceSq(livingentity) < 144.0D) || livingentity.getRevengeTarget() == null) && this.isSleeping;
            }
        }
    }
    
    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.betterWolf.getNavigator().clearPath();
        this.betterWolf.setSleeping(true);
    }
    
    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask()
    {
        this.betterWolf.setSleeping(false);
    }
    
    /**
     * Sets the sleeping flag.
     */
    public void setSleeping(boolean sleeping)
    {
        this.isSleeping = sleeping;
    }
}