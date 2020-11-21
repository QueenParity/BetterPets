package com.kingparity.betterpets.entity.ai.goal;

import com.kingparity.betterpets.entity.BetterWolfEntity;
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
    private final EntityPredicate playerPredicate;
    
    public BetterWolfBegGoal(BetterWolfEntity betterWolf, float minPlayerDistance)
    {
        this.betterWolf = betterWolf;
        this.world = betterWolf.world;
        this.minPlayerDistance = minPlayerDistance;
        this.playerPredicate = (new EntityPredicate()).setDistance((double)minPlayerDistance).allowInvulnerable().allowFriendlyFire().setSkipAttackChecks();
        this.setMutexFlags(EnumSet.of(Flag.LOOK));
    }
    
    @Override
    public boolean shouldExecute()
    {
        this.player = this.world.getClosestPlayer(this.playerPredicate, this.betterWolf);
        return this.player == null ? false : this.hasTemptationItemInHand(this.player);
    }
    
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
    
    @Override
    public void startExecuting()
    {
        this.betterWolf.setBegging(true);
        this.timeoutCounter = 40 + this.betterWolf.getRNG().nextInt(40);
    }
    
    @Override
    public void resetTask()
    {
        this.betterWolf.setBegging(false);
        this.player = null;
    }
    
    @Override
    public void tick()
    {
        this.betterWolf.getLookController().setLookPosition(this.player.getPosX(), this.player.getPosYEye(), this.player.getPosZ(), 10.0F, (float)this.betterWolf.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }
    
    private boolean hasTemptationItemInHand(PlayerEntity player)
    {
        for(Hand hand : Hand.values())
        {
            ItemStack stack = player.getHeldItem(hand);
            if(this.betterWolf.isTamed() && stack.getItem() == Items.BONE)
            {
                return true;
            }
            
            if(this.betterWolf.isBreedingItem(stack))
            {
                return true;
            }
        }
        
        return false;
    }
}