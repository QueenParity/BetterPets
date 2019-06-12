package com.william.betterpets.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BetterWolfLayDownGoal extends Goal
{
    private final BetterWolfEntity betterWolf;
    private boolean isLayingDown;
    
    public BetterWolfLayDownGoal(BetterWolfEntity betterWolf)
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
        return this.isLayingDown;
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
                return (!(this.betterWolf.getDistanceSq(livingentity) < 144.0D) || livingentity.getRevengeTarget() == null) && this.isLayingDown;
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
        this.betterWolf.setLayingDown(true);
    }
    
    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask()
    {
        this.betterWolf.setLayingDown(false);
    }
    
    /**
     * Sets the layingDown flag.
     */
    public void setLayingDown(boolean layingDown)
    {
        this.isLayingDown = layingDown;
    }
}