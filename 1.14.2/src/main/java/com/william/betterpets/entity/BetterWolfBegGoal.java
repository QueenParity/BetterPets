package com.william.betterpets.entity;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.EnumSet;

public class BetterWolfBegGoal extends Goal
{
    private final BetterWolfEntity betterWolf;
    private PlayerEntity player;
    private final World world;
    private final float minPlayerDistance;
    private int timeoutCounter;
    private final EntityPredicate entityPredicate;
    
    public BetterWolfBegGoal(BetterWolfEntity betterWolf, float minDistance)
    {
        this.betterWolf = betterWolf;
        this.world = betterWolf.world;
        this.minPlayerDistance = minDistance;
        this.entityPredicate = (new EntityPredicate()).func_221013_a((double)minDistance).func_221008_a().func_221011_b().func_221009_d();
        this.setMutexFlags(EnumSet.of(Flag.LOOK));
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        this.player = this.world.func_217370_a(this.entityPredicate, this.betterWolf);
        return this.player != null && this.hasTemptationItemInHand(this.player);
    }
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        if(!this.player.isAlive())
        {
            return false;
        }
        else if(this.betterWolf.getDistanceSq(this.player) > (double)(this.minPlayerDistance * this.minPlayerDistance))
        {
            return false;
        }
        else
        {
            return this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
        }
    }
    
    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.betterWolf.setBegging(true);
        this.timeoutCounter = 40 + this.betterWolf.getRNG().nextInt(40);
    }
    
    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask()
    {
        this.betterWolf.setBegging(false);
        this.player = null;
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick()
    {
        this.betterWolf.getLookHelper().setLookPosition(this.player.posX, this.player.posY + (double)this.player.getEyeHeight(), this.player.posZ, 10.0F, (float)this.betterWolf.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }
    
    /**
     * Gets if the Player has the Bone in the hand.
     */
    private boolean hasTemptationItemInHand(PlayerEntity player)
    {
        for(Hand hand : Hand.values())
        {
            ItemStack itemstack = player.getHeldItem(hand);
            if(this.betterWolf.isTamed() && itemstack.getItem() == Items.BONE)
            {
                return true;
            }
            
            if(this.betterWolf.isBreedingItem(itemstack))
            {
                return true;
            }
        }
        
        return false;
    }
}