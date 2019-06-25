package com.kingparity.betterpets.entity.goals;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BetterWolfLookRandomlyGoal extends Goal
{
    private final BetterWolfEntity betterWolf;
    private double lookX;
    private double lookZ;
    private int idleTime;
    
    public BetterWolfLookRandomlyGoal(BetterWolfEntity betterWolf)
    {
        this.betterWolf = betterWolf;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        return this.betterWolf.getRNG().nextFloat() < 0.02F;
    }
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return this.idleTime >= 0;
    }
    
    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        double d0 = (Math.PI * 2D) * this.betterWolf.getRNG().nextDouble();
        this.lookX = Math.cos(d0);
        this.lookZ = Math.sin(d0);
        this.idleTime = 20 + this.betterWolf.getRNG().nextInt(20);
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick()
    {
        --this.idleTime;
        this.betterWolf.getLookController().func_220679_a(this.betterWolf.posX + this.lookX, this.betterWolf.posY + (double)this.betterWolf.getEyeHeight(), this.betterWolf.posZ + this.lookZ);
    }
}