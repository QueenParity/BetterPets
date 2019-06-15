package com.william.betterpets.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

import java.util.EnumSet;

public class BetterWolfFollowOwnerGoal extends Goal
{
    protected final BetterWolfEntity betterWolf;
    private LivingEntity betterWolfOwner;
    protected final IWorldReader world;
    private final double followSpeed;
    private final PathNavigator navigator;
    private int timeToRecalcPath;
    private final float maxDist;
    private final float minDist;
    private float oldWaterCost;
    
    public BetterWolfFollowOwnerGoal(BetterWolfEntity betterWolf, double followSpeedIn, float minDistIn, float maxDistIn)
    {
        this.betterWolf = betterWolf;
        this.world = betterWolf.world;
        this.followSpeed = followSpeedIn;
        this.navigator = betterWolf.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if(!(betterWolf.getNavigator() instanceof GroundPathNavigator) && !(betterWolf.getNavigator() instanceof FlyingPathNavigator))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        LivingEntity betterWolfOwner = this.betterWolf.getOwner();
        if(betterWolfOwner == null)
        {
            return false;
        }
        else if(betterWolfOwner instanceof PlayerEntity && betterWolfOwner.isSpectator())
        {
            return false;
        }
        else if(this.betterWolf.isSitting())
        {
            return false;
        }
        else if(this.betterWolf.isLayingDown())
        {
            return false;
        }
        else if(this.betterWolf.isSleeping())
        {
            return false;
        }
        else if(this.betterWolf.getDistanceSq(betterWolfOwner) < (double)(this.minDist * this.minDist))
        {
            return false;
        }
        else
        {
            this.betterWolfOwner = betterWolfOwner;
            return true;
        }
    }
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return !this.navigator.noPath() && this.betterWolf.getDistanceSq(this.betterWolfOwner) > (double)(this.maxDist * this.maxDist) && !this.betterWolf.isSitting() && !this.betterWolf.isLayingDown() && !this.betterWolf.isSleeping();
    }
    
    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.betterWolf.getPathPriority(PathNodeType.WATER);
        this.betterWolf.setPathPriority(PathNodeType.WATER, 0.0F);
    }
    
    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask()
    {
        this.betterWolfOwner = null;
        this.navigator.clearPath();
        this.betterWolf.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick()
    {
        this.betterWolf.getLookController().setLookPositionWithEntity(this.betterWolfOwner, 10.0F, (float)this.betterWolf.getVerticalFaceSpeed());
        if(!this.betterWolf.isSitting())
        {
            if(--this.timeToRecalcPath <= 0)
            {
                this.timeToRecalcPath = 10;
                if(!this.navigator.tryMoveToEntityLiving(this.betterWolfOwner, this.followSpeed))
                {
                    if(!this.betterWolf.getLeashed() && !this.betterWolf.isPassenger())
                    {
                        if(!(this.betterWolf.getDistanceSq(this.betterWolfOwner) < 144.0D))
                        {
                            int i = MathHelper.floor(this.betterWolfOwner.posX) - 2;
                            int j = MathHelper.floor(this.betterWolfOwner.posZ) - 2;
                            int k = MathHelper.floor(this.betterWolfOwner.getBoundingBox().minY);
                            
                            for(int l = 0; l <= 4; ++l)
                            {
                                for(int i1 = 0; i1 <= 4; ++i1)
                                {
                                    if((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isSpawnable(new BlockPos(i + l, k - 1, j + i1)))
                                    {
                                        this.betterWolf.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.betterWolf.rotationYaw, this.betterWolf.rotationPitch);
                                        this.navigator.clearPath();
                                        return;
                                    }
                                }
                            }
                            
                        }
                    }
                }
            }
        }
    }
    
    protected boolean isSpawnable(BlockPos pos)
    {
        BlockState blockstate = this.world.getBlockState(pos);
        return blockstate.canEntitySpawn(this.world, pos, this.betterWolf.getType()) && this.world.isAirBlock(pos.up()) && this.world.isAirBlock(pos.up(2));
    }
}