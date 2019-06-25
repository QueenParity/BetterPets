package com.kingparity.betterpets;

import com.kingparity.betterpets.client.KeyBinds;
import com.kingparity.betterpets.network.PacketHandler;
import com.kingparity.betterpets.network.message.MessageThirstStats;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.biome.DesertBiome;

import java.lang.reflect.Field;
import java.util.Random;

public class ThirstStats
{
    public int thirstLevel;
    public float saturation;
    public float exhaustion;
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
    
    public ThirstStats()
    {
        lastThirstLevel = -1; // Trigger a refresh when this class is loaded.
        resetStats();
    }
    
    public void update(PlayerEntity player)
    {
        if(exhaustion > 5.0f)
        {
            exhaustion -= 5.0f;
            if(saturation > 0.0f)
            {
                saturation = Math.max(saturation - 1.0f, 0);
            }
            else if(player.world.getDifficulty() != Difficulty.PEACEFUL)
            {
                thirstLevel = Math.max(thirstLevel - 1, 0);
            }
        }
        
        if(thirstLevel <= 6)
        {
            player.setSprinting(false);
            if(thirstLevel == 0)
            {
                if(thirstTimer++ > 200)
                {
                    if(player.getHealth() > 10.0f || player.world.getDifficulty() == Difficulty.HARD || (player.world.getDifficulty() == Difficulty.NORMAL && player.getHealth() > 1.0f))
                    {
                        thirstTimer = 0;
                        player.attackEntityFrom(this.thirstDmgSource, 1);
                    }
                }
            }
        }
        
        int ms = player.isPassenger() ? 10 : movementSpeed;
        float exhaustMultiplier = player.world.isDaytime() ? 1.0f : 0.9f;
        exhaustMultiplier *= player.world.getBiomeBody(player.getPosition()) instanceof DesertBiome ? 2.0f : 1.0f;
        
        if(player.areEyesInFluid(FluidTags.WATER, true) || player.isInWater())
        {
            addExhaustion(0.03f * ms * 0.003f * exhaustMultiplier);
        }
        else if(player.onGround)
        {
            if(player.isSprinting())
            {
                addExhaustion(0.06f * ms * 0.018f * exhaustMultiplier);
            }
            else
            {
                addExhaustion(0.01f * ms * 0.018f * exhaustMultiplier);
            }
        }
        else if(!player.isPassenger())
        { // must be in the air/jumping
            if(player.isSprinting())
            {
                addExhaustion(0.06f * ms * 0.025f * exhaustMultiplier);
            }
            else
            {
                addExhaustion(0.01f * ms * 0.025f * exhaustMultiplier);
            }
        }
        
        if(poisoned && thirstLevel > 0)
        {
            if(poisonTimer++ < Reference.POISON_DURATION)
            {
                if(player.getHealth() > 1.0f && player.world.getDifficulty() != Difficulty.PEACEFUL && thirstTimer++ > 200)
                {
                    thirstTimer = 0;
                    player.attackEntityFrom(this.thirstDmgSource, 1);
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
                foodTimer = player.getFoodStats().getClass().getDeclaredField("foodTimer");
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
                foodTimer.setInt(player.getFoodStats(), 0);
            }
            catch(IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        
        // Only send packet update if the thirst level or saturation has changed.
        if(lastThirstLevel != thirstLevel || lastSaturation != saturation || lastPoisoned != poisoned)
        {
            PacketHandler.sendTo(new MessageThirstStats(this.thirstLevel, this.saturation, this.exhaustion, this.poisoned), (ServerPlayerEntity)player);
            lastThirstLevel = thirstLevel;
            lastSaturation = saturation;
            lastPoisoned = poisoned;
        }
        
        if(KeyBinds.KEY_J.isKeyDown())
        {
            thirstLevel = Math.max(thirstLevel - 1, 0);
        }
        else if(KeyBinds.KEY_K.isKeyDown())
        {
            thirstLevel = Math.min(thirstLevel + 1, 20);
        }
    }
    
    public void addStats(int heal, float sat)
    {
        thirstLevel = Math.min(thirstLevel + heal, 20);
        saturation = Math.min(saturation + (heal * sat * 2.0f), thirstLevel);
    }
    
    public void addExhaustion(float exhaustion)
    {
        this.exhaustion = Math.min(this.exhaustion + exhaustion, 40.0f);
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
    
    public int getMovementSpeed(PlayerEntity player)
    {
        double x = player.posX - player.prevPosX;
        double y = player.posY - player.prevPosY;
        double z = player.posZ - player.prevPosZ;
        return (int)Math.round(100.0d * Math.sqrt(x * x + y * y + z * z));
    }
    
    public void resetStats()
    {
        thirstLevel = 20;
        saturation = 5f;
        exhaustion = 0f;
        poisoned = false;
        poisonTimer = 0;
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
            if(entity instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity)entity;
                return new StringTextComponent(player.getDisplayName() + "'s body is now made up of 0% water!");
            }
            return super.getDeathMessage(entity);
        }
    }
}