package com.kingparity.betterpets.entity.ai;

import com.kingparity.betterpets.entity.BetterWolf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BetterWolfBegGoal extends Goal
{
    private final BetterWolf betterWolf;
    @Nullable
    private Player player;
    private final Level level;
    private final float lookDistance;
    private int lookTime;
    private final TargetingConditions begTargeting;
    
    public BetterWolfBegGoal(BetterWolf betterWolf, float lookDistance)
    {
        this.betterWolf = betterWolf;
        this.level = betterWolf.level;
        this.lookDistance = lookDistance;
        this.begTargeting = TargetingConditions.forNonCombat().range((double) lookDistance);
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }
    
    public boolean canUse()
    {
        this.player = this.level.getNearestPlayer(this.begTargeting, this.betterWolf);
        return this.player == null ? false : this.playerHoldingInteresting(this.player);
    }
    
    public boolean canContinueToUse()
    {
        if(!this.player.isAlive())
        {
            return false;
        }
        else if(this.betterWolf.distanceToSqr(this.player) > (double) (this.lookDistance * this.lookDistance))
        {
            return false;
        }
        else
        {
            return this.lookTime > 0 && this.playerHoldingInteresting(this.player);
        }
    }
    
    public void start()
    {
        this.betterWolf.setIsInterested(true);
        this.lookTime = this.adjustedTickDelay(40 + this.betterWolf.getRandom().nextInt(40));
    }
    
    public void stop()
    {
        this.betterWolf.setIsInterested(false);
        this.player = null;
    }
    
    public void tick()
    {
        this.betterWolf.getLookControl().setLookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ(), 10.0F, (float) this.betterWolf.getMaxHeadXRot());
        --this.lookTime;
    }
    
    private boolean playerHoldingInteresting(Player player)
    {
        for(InteractionHand interactionhand : InteractionHand.values())
        {
            ItemStack itemstack = player.getItemInHand(interactionhand);
            if(this.betterWolf.isTame() && itemstack.is(Items.BONE))
            {
                return true;
            }
            
            if(this.betterWolf.isFood(itemstack))
            {
                return true;
            }
        }
        
        return false;
    }
}
