package com.kingparity.betterpets.entity.goals;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;

import java.util.EnumSet;

public class BetterWolfLookAtGoal extends Goal
{
    protected final BetterWolfEntity betterWolf;
    protected Entity closestEntity;
    protected final float maxDistance;
    private int lookTime;
    private final float chance;
    protected final Class<? extends LivingEntity> watchedClass;
    protected final EntityPredicate entityPredicate;
    
    public BetterWolfLookAtGoal(BetterWolfEntity betterWolf, Class<? extends LivingEntity> watchTargetClass, float maxDistance)
    {
        this(betterWolf, watchTargetClass, maxDistance, 0.02F);
    }
    
    public BetterWolfLookAtGoal(BetterWolfEntity betterWolf, Class<? extends LivingEntity> watchTargetClass, float maxDistance, float chanceIn)
    {
        this.betterWolf = betterWolf;
        this.watchedClass = watchTargetClass;
        this.maxDistance = maxDistance;
        this.chance = chanceIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
        if(watchTargetClass == PlayerEntity.class)
        {
            this.entityPredicate = (new EntityPredicate()).setDistance((double)maxDistance).allowFriendlyFire().allowInvulnerable().setSkipAttackChecks().setCustomPredicate((p_220715_1_) -> {
                return EntityPredicates.notRiding(betterWolf).test(p_220715_1_);
            });
        }
        else
        {
            this.entityPredicate = (new EntityPredicate()).setDistance((double)maxDistance).allowFriendlyFire().allowInvulnerable().setSkipAttackChecks();
        }
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if(this.betterWolf.getRNG().nextFloat() >= this.chance)
        {
            return false;
        }
        else if(this.betterWolf.isSleeping())
        {
            return false;
        }
        else if(this.betterWolf.isLayingDown())
        {
            return false;
        }
        else
        {
            if(this.betterWolf.getAttackTarget() != null)
            {
                this.closestEntity = this.betterWolf.getAttackTarget();
            }
            
            if(this.watchedClass == PlayerEntity.class)
            {
                this.closestEntity = this.betterWolf.world.func_217372_a(this.entityPredicate, this.betterWolf, this.betterWolf.posX, this.betterWolf.posY + (double)this.betterWolf.getEyeHeight(), this.betterWolf.posZ);
            }
            else
            {
                this.closestEntity = this.betterWolf.world.func_217360_a(this.watchedClass, this.entityPredicate, this.betterWolf, this.betterWolf.posX, this.betterWolf.posY + (double)this.betterWolf.getEyeHeight(), this.betterWolf.posZ, this.betterWolf.getBoundingBox().grow((double)this.maxDistance, 3.0D, (double)this.maxDistance));
            }
            
            return this.closestEntity != null;
        }
    }
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        if(!this.closestEntity.isAlive())
        {
            return false;
        }
        else if(this.betterWolf.getDistanceSq(this.closestEntity) > (double)(this.maxDistance * this.maxDistance))
        {
            return false;
        }
        else if(this.betterWolf.isSleeping())
        {
            return false;
        }
        else if(this.betterWolf.isLayingDown())
        {
            return false;
        }
        else
        {
            return this.lookTime > 0;
        }
    }
    
    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.lookTime = 40 + this.betterWolf.getRNG().nextInt(40);
    }
    
    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask()
    {
        this.closestEntity = null;
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick()
    {
        this.betterWolf.getLookController().func_220679_a(this.closestEntity.posX, this.closestEntity.posY + (double)this.closestEntity.getEyeHeight(), this.closestEntity.posZ);
        --this.lookTime;
    }
}