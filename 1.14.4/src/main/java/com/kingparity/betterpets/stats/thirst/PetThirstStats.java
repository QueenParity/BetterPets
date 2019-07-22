package com.kingparity.betterpets.stats.thirst;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.network.PacketHandler;
import com.kingparity.betterpets.network.message.MessageUpdateThirstStats;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.biome.DesertBiome;

import java.lang.reflect.Field;
import java.util.Random;

public class PetThirstStats
{
    public int thirstLevel = 20;
    public float thirstSaturationLevel;
    public float thirstExhaustionLevel;
    public int thirstTimer;
    public boolean poisoned;
    public int poisonTimer;
    
    public int movementSpeed;
    
    public transient int lastThirstLevel;
    public transient float lastSaturation;
    public transient boolean lastPoisoned;
    
    public transient Random random = new Random();
    public transient DamageSource thirstDmgSource = new DamageThirst();
    
    public transient Field foodTimer;
    
    public PetThirstStats()
    {
        lastThirstLevel = -1; // Trigger a refresh when this class is loaded.
        resetStats();
    }
    
    public void tick(BetterWolfEntity betterWolf)
    {
        if(this.thirstExhaustionLevel > 5.0f)
        {
            this.thirstExhaustionLevel -= 5.0f;
            if(this.thirstSaturationLevel > 0.0f)
            {
                this.thirstSaturationLevel = Math.max(this.thirstSaturationLevel - 1.0f, 0);
            }
            else if(betterWolf.world.getDifficulty() != Difficulty.PEACEFUL)
            {
                this.thirstLevel = Math.max(thirstLevel - 1, 0);
            }
        }
        
        if(thirstLevel <= 6)
        {
            betterWolf.setSprinting(false);
            if(thirstLevel == 0)
            {
                if(thirstTimer++ > 200)
                {
                    if(betterWolf.getHealth() > 10.0f || betterWolf.world.getDifficulty() == Difficulty.HARD || (betterWolf.world.getDifficulty() == Difficulty.NORMAL && betterWolf.getHealth() > 1.0f))
                    {
                        thirstTimer = 0;
                        betterWolf.attackEntityFrom(this.thirstDmgSource, 1);
                    }
                }
            }
        }
        
        int ms = betterWolf.isPassenger() ? 10 : movementSpeed;
        float exhaustMultiplier = betterWolf.world.isDaytime() ? 1.0f : 0.9f;
        exhaustMultiplier *= betterWolf.world.getBiomeBody(betterWolf.getPosition()) instanceof DesertBiome ? 2.0f : 1.0f;
        
        if(betterWolf.areEyesInFluid(FluidTags.WATER, true) || betterWolf.isInWater())
        {
            addExhaustion(0.03f * ms * 0.003f * exhaustMultiplier);
        }
        else if(betterWolf.onGround)
        {
            addExhaustion(0.06f * ms * 0.018f * exhaustMultiplier);
        }
        else if(!betterWolf.isPassenger())
        { // must be in the air/jumping
            addExhaustion(0.06f * ms * 0.025f * exhaustMultiplier);
        }
        
        if(poisoned && thirstLevel > 0)
        {
            if(poisonTimer++ < Reference.POISON_DURATION)
            {
                if(betterWolf.getHealth() > 1.0f && betterWolf.world.getDifficulty() != Difficulty.PEACEFUL && thirstTimer++ > 200)
                {
                    thirstTimer = 0;
                    betterWolf.attackEntityFrom(this.thirstDmgSource, 1);
                }
            }
            else
            {
                poisoned = false;
                poisonTimer = 0;
            }
        }
        
        if(foodTimer == null)
        {
            try
            {
                foodTimer = betterWolf.getPetFoodStats().getClass().getDeclaredField("foodTimer");
                foodTimer.setAccessible(true);
            }
            catch(NoSuchFieldException e)
            {
                e.printStackTrace();
            }
        }
        
        if(thirstLevel < 16 || poisoned)
        {
            try
            {
                foodTimer.setInt(betterWolf.getPetFoodStats(), 0);
            }
            catch(IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    
        // Only send packet update if the thirst level or saturation has changed.
        if(lastThirstLevel != thirstLevel || lastSaturation != thirstSaturationLevel || lastPoisoned != poisoned)
        {
            syncStats(betterWolf);
        }
    }
    
    public void syncStats(BetterWolfEntity betterWolf)
    {
        betterWolf.world.getPlayers().forEach((player) -> PacketHandler.sendTo(new MessageUpdateThirstStats(betterWolf.getEntityId(), this.thirstLevel, this.thirstSaturationLevel, this.thirstExhaustionLevel, this.poisoned), (ServerPlayerEntity)player));
        lastThirstLevel = thirstLevel;
        lastSaturation = thirstSaturationLevel;
        lastPoisoned = poisoned;
    }
    
    public void addStats(int heal, float sat)
    {
        thirstLevel = Math.min(thirstLevel + heal, 20);
        this.thirstSaturationLevel = Math.min(this.thirstSaturationLevel + (heal * sat * 2.0f), thirstLevel);
    }
    
    public void addExhaustion(float thirstExhaustionLevel)
    {
        this.thirstExhaustionLevel = Math.min(this.thirstExhaustionLevel + thirstExhaustionLevel, 40.0f);
    }
    
    public void attemptToPoison(float chance)
    {
        if(random.nextFloat() < chance)
        {
            poisoned = true;
        }
    }
    
    public boolean canDrink()
    {
        return thirstLevel < 20;
    }
    
    public int getMovementSpeed(BetterWolfEntity betterWolf)
    {
        double x = betterWolf.posX - betterWolf.prevPosX;
        double y = betterWolf.posY - betterWolf.prevPosY;
        double z = betterWolf.posZ - betterWolf.prevPosZ;
        return (int)Math.round(100.0d * Math.sqrt(x * x + y * y + z * z));
    }
    
    public void resetStats()
    {
        thirstLevel = 20;
        thirstSaturationLevel = 5.0F;
        thirstExhaustionLevel = 0.0F;
        poisoned = false;
        poisonTimer = 0;
    }
    
    /**
     * Reads the thirst data for the betterWolf.
     */
    public void read(CompoundNBT compound)
    {
        if(compound.contains("thirstLevel", 99))
        {
            this.thirstLevel = compound.getInt("thirstLevel");
            this.thirstTimer = compound.getInt("thirstTickTimer");
            this.thirstSaturationLevel = compound.getFloat("thirstSaturationLevel");
            this.thirstExhaustionLevel = compound.getFloat("thirstExhaustionLevel");
        }
        
    }
    
    /**
     * Writes the thirst data for the betterWolf.
     */
    public void write(CompoundNBT compound)
    {
        compound.putInt("thirstLevel", this.thirstLevel);
        compound.putInt("thirstTickTimer", this.thirstTimer);
        compound.putFloat("thirstSaturationLevel", this.thirstSaturationLevel);
        compound.putFloat("thirstExhaustionLevel", this.thirstExhaustionLevel);
    }
    
    public static class DamageThirst extends DamageSource
    {
        public DamageThirst()
        {
            super("thirst");
            setDamageBypassesArmor();
            setDamageIsAbsolute();
        }
        
        @Override
        public ITextComponent getDeathMessage(LivingEntity entity)
        {
            if(entity instanceof BetterWolfEntity)
            {
                BetterWolfEntity betterWolf = (BetterWolfEntity)entity;
                return new StringTextComponent(betterWolf.getDisplayName() + "'s body is now made up of 0% water!");
            }
            return super.getDeathMessage(entity);
        }
    }
}