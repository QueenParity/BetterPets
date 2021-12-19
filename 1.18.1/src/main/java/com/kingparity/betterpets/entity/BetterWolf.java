package com.kingparity.betterpets.entity;

import com.kingparity.betterpets.entity.ai.BetterWolfBegGoal;
import com.kingparity.betterpets.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class BetterWolf extends TamableAnimal implements NeutralMob
{
    private static final EntityDataAccessor<Boolean> INTERESTED = SynchedEntityData.defineId(BetterWolf.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLLAR_COLOR = SynchedEntityData.defineId(BetterWolf.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> REMAINING_ANGER_TIME = SynchedEntityData.defineId(BetterWolf.class, EntityDataSerializers.INT);
    
    public static final Predicate<LivingEntity> PREY_SELECTOR = (target) -> {
        EntityType<?> entity = target.getType();
        return entity == EntityType.SHEEP || entity == EntityType.RABBIT || entity == EntityType.FOX;
    };
    
    private float interestedAngle;
    private float prevInterestedAngle;
    private boolean isWet;
    private boolean isShaking;
    private float shakeAnim;
    private float prevShakeAnim;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    
    private UUID persistentAngerTarget;
    
    public BetterWolf(Level level)
    {
        super(ModEntities.BETTER_WOLF.get(), level);
        this.setTame(false);
    }
    
    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new BetterWolf.BetterWolfAvoidEntityGoal<>(this, Llama.class, 24.0F, 1.5D, 1.5D));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new BetterWolfBegGoal(this, 8.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }
    
    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
    }
    
    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(INTERESTED, false);
        this.entityData.define(COLLAR_COLOR, DyeColor.BLUE.getId());
        this.entityData.define(REMAINING_ANGER_TIME, 0);
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState state)
    {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound)
    {
        super.addAdditionalSaveData(compound);
        compound.putByte("CollarColor", (byte)this.getCollarColor().getId());
        this.addPersistentAngerSaveData(compound);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound)
    {
        super.readAdditionalSaveData(compound);
        if(compound.contains("CollarColor", 99))
        {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }
    
        this.readPersistentAngerSaveData(this.level, compound);
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        if(this.isAngry())
        {
            return SoundEvents.WOLF_GROWL;
        }
        else if(this.random.nextInt(3) == 0)
        {
            return this.isTame() && this.getHealth() < 10.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT;
        }
        else
        {
            return SoundEvents.WOLF_AMBIENT;
        }
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return SoundEvents.WOLF_HURT;
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.WOLF_DEATH;
    }
    
    @Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }
    
    @Override
    public void aiStep()
    {
        super.aiStep();
        if(!this.level.isClientSide() && this.isWet && !this.isShaking && !this.isPathFinding() && this.onGround)
        {
            this.isShaking = true;
            this.shakeAnim = 0.0F;
            this.prevShakeAnim = 0.0F;
            this.level.broadcastEntityEvent(this, (byte)8);
        }
        
        if(!this.level.isClientSide)
        {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
    }
    
    @Override
    public void tick()
    {
        super.tick();
        if(this.isAlive())
        {
            this.prevInterestedAngle = this.interestedAngle;
            if(this.isInterested())
            {
                this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
            }
            else
            {
                this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;
            }
            
            if(this.isInWaterOrBubble())
            {
                this.isWet = true;
                if(this.isShaking && !this.level.isClientSide)
                {
                    this.level.broadcastEntityEvent(this, (byte)56);
                    this.cancelShake();
                }
            }
            else if((this.isWet || this.isShaking) && this.isShaking)
            {
                if(this.shakeAnim == 0.0F)
                {
                    this.playSound(SoundEvents.WOLF_SHAKE, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    this.gameEvent(GameEvent.WOLF_SHAKING);
                }
                
                this.prevShakeAnim = this.shakeAnim;
                this.shakeAnim += 0.5F;
                if(this.prevShakeAnim >= 2.0F)
                {
                    this.isWet = false;
                    this.isShaking = false;
                    this.prevShakeAnim = 0.0F;
                    this.shakeAnim = 0.0F;
                }
                
                if(this.shakeAnim > 0.4F)
                {
                    float f = (float)this.getY();
                    int i = (int)(Mth.sin((this.shakeAnim - 0.4F) * (float)Math.PI) * 7.0F);
                    Vec3 vec3 = this.getDeltaMovement();
    
                    for(int j = 0; j < i; ++j)
                    {
                        float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                        float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                        this.level.addParticle(ParticleTypes.SPLASH, this.getX() + (double)f1, (double)(f + 0.8F), this.getZ() + (double)f2, vec3.x, vec3.y, vec3.z);
                    }
                }
            }
        }
    }
    
    private void cancelShake()
    {
        this.isShaking = false;
        this.shakeAnim = 0.0F;
        this.prevShakeAnim = 0.0F;
    }
    
    @Override
    public void die(DamageSource source)
    {
        this.isWet = false;
        this.isShaking = false;
        this.shakeAnim = 0.0F;
        this.prevShakeAnim = 0.0F;
        super.die(source);
    }
    
    public boolean isWet()
    {
        return this.isWet;
    }
    
    public float getWetShade(float p_30447_)
    {
        return Math.min(0.5F + Mth.lerp(p_30447_, this.prevShakeAnim, this.shakeAnim) / 2.0F * 0.5F, 1.0F);
    }
    
    public float getBodyRollAngle(float p_30433_, float p_30434_)
    {
        float f = (Mth.lerp(p_30433_, this.prevShakeAnim, this.shakeAnim) + p_30434_) / 1.8F;
        if(f < 0.0F)
        {
            f = 0.0F;
        }
        else if(f > 1.0F)
        {
            f = 1.0F;
        }
        
        return Mth.sin(f * (float)Math.PI) * Mth.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }
    
    public float getHeadRollAngle(float p_30449_)
    {
        return Mth.lerp(p_30449_, this.prevInterestedAngle, this.interestedAngle) * 0.15F * (float)Math.PI;
    }
    
    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions p_30410_)
    {
        return p_30410_.height * 0.8F;
    }
    
    @Override
    public int getMaxHeadXRot()
    {
        return this.isInSittingPose() ? 20 : super.getMaxHeadXRot();
    }
    
    @Override
    public boolean hurt(DamageSource source, float p_30387_)
    {
        if(this.isInvulnerableTo(source))
        {
            return false;
        }
        else
        {
            Entity entity = source.getEntity();
            this.setOrderedToSit(false);
            if(entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow))
            {
                p_30387_ = (p_30387_ + 1.0F) / 2.0F;
            }
            
            return super.hurt(source, p_30387_);
        }
    }
    
    @Override
    public boolean doHurtTarget(Entity target)
    {
        boolean flag = target.hurt(DamageSource.mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if(flag)
        {
            this.doEnchantDamageEffects(this, target);
        }
        
        return flag;
    }
    
    @Override
    public void setTame(boolean tamed)
    {
        super.setTame(tamed);
        if(tamed)
        {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            this.setHealth(20.0F);
        }
        else
        {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
        }
        
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand)
    {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        if(this.level.isClientSide)
        {
            boolean flag = this.isOwnedBy(player) || this.isTame() || itemstack.is(Items.BONE) && !this.isTame() && !this.isAngry();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        else
        {
            if(this.isTame())
            {
                if(this.isFood(itemstack) && this.getHealth() < this.getMaxHealth())
                {
                    if(!player.getAbilities().instabuild)
                    {
                        itemstack.shrink(1);
                    }
    
                    this.heal((float) item.getFoodProperties().getNutrition());
                    this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
                    return InteractionResult.SUCCESS;
                }
    
                if(!(item instanceof DyeItem))
                {
                    InteractionResult interactionresult = super.mobInteract(player, hand);
                    if((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(player))
                    {
                        this.setOrderedToSit(!this.isOrderedToSit());
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget((LivingEntity) null);
                        return InteractionResult.SUCCESS;
                    }
    
                    return interactionresult;
                }
    
                DyeColor dyecolor = ((DyeItem) item).getDyeColor();
                if(dyecolor != this.getCollarColor())
                {
                    this.setCollarColor(dyecolor);
                    if(!player.getAbilities().instabuild)
                    {
                        itemstack.shrink(1);
                    }
    
                    return InteractionResult.SUCCESS;
                }
            }
            else if(itemstack.is(Items.BONE) && !this.isAngry())
            {
                if(!player.getAbilities().instabuild)
                {
                    itemstack.shrink(1);
                }
    
                if(this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player))
                {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget((LivingEntity) null);
                    this.setOrderedToSit(true);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                }
                else
                {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }
    
                return InteractionResult.SUCCESS;
            }
    
            return super.mobInteract(player, hand);
        }
    }
    
    @Override
    public void handleEntityEvent(byte p_30379_)
    {
        if(p_30379_ == 8)
        {
            this.isShaking = true;
            this.shakeAnim = 0.0F;
            this.prevShakeAnim = 0.0F;
        }
        else if(p_30379_ == 56)
        {
            this.cancelShake();
        }
        else
        {
            super.handleEntityEvent(p_30379_);
        }
    }
    
    public float getTailAngle()
    {
        if(this.isAngry())
        {
            return 1.5393804F;
        }
        else
        {
            return this.isTame() ? (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * (float) Math.PI : ((float) Math.PI / 5F);
        }
    }
    
    @Override
    public boolean isFood(ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        return item.isEdible() && item.getFoodProperties().isMeat();
    }
    
    @Override
    public int getMaxSpawnClusterSize()
    {
        return 8;
    }
    
    @Override
    public int getRemainingPersistentAngerTime()
    {
        return this.entityData.get(REMAINING_ANGER_TIME);
    }
    
    @Override
    public void setRemainingPersistentAngerTime(int time)
    {
        this.entityData.set(REMAINING_ANGER_TIME, time);
    }
    
    @Override
    public void startPersistentAngerTimer()
    {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }
    
    @Nullable
    @Override
    public UUID getPersistentAngerTarget()
    {
        return this.persistentAngerTarget;
    }
    
    @Override
    public void setPersistentAngerTarget(UUID persistentAngerTarget)
    {
        this.persistentAngerTarget = persistentAngerTarget;
    }
    
    public DyeColor getCollarColor()
    {
        return DyeColor.byId(this.entityData.get(COLLAR_COLOR));
    }
    
    public void setCollarColor(DyeColor color)
    {
        this.entityData.set(COLLAR_COLOR, color.getId());
    }
    
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageableMob)
    {
        BetterWolf betterWolf = ModEntities.BETTER_WOLF.get().create(level);
        UUID uuid = this.getOwnerUUID();
        if(uuid != null)
        {
            betterWolf.setOwnerUUID(uuid);
            betterWolf.setTame(true);
        }
        
        return betterWolf;
    }
    
    public void setIsInterested(boolean isInterested)
    {
        this.entityData.set(INTERESTED, isInterested);
    }
    
    @Override
    public boolean canMate(Animal animal)
    {
        if(animal == this)
        {
            return false;
        }
        else if(!this.isTame())
        {
            return false;
        }
        else if(!(animal instanceof Wolf))
        {
            return false;
        }
        else
        {
            Wolf wolf = (Wolf) animal;
            if(!wolf.isTame())
            {
                return false;
            }
            else if(wolf.isInSittingPose())
            {
                return false;
            }
            else
            {
                return this.isInLove() && wolf.isInLove();
            }
        }
    }
    
    public boolean isInterested()
    {
        return this.entityData.get(INTERESTED);
    }
    
    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner)
    {
        if(!(target instanceof Creeper) && !(target instanceof Ghast))
        {
            if(target instanceof Wolf)
            {
                Wolf wolf = (Wolf) target;
                return !wolf.isTame() || wolf.getOwner() != owner;
            }
            else if(target instanceof Player && owner instanceof Player && !((Player) owner).canHarmPlayer((Player) target))
            {
                return false;
            }
            else if(target instanceof AbstractHorse && ((AbstractHorse) target).isTamed())
            {
                return false;
            }
            else
            {
                return !(target instanceof TamableAnimal) || !((TamableAnimal) target).isTame();
            }
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public boolean canBeLeashed(Player player)
    {
        return !this.isAngry() && super.canBeLeashed(player);
    }
    
    @Override
    public Vec3 getLeashOffset()
    {
        return new Vec3(0.0D, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }
    
    public static boolean checkBetterWolfSpawnRules(EntityType<BetterWolf> betterWolf, LevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, Random random)
    {
        return accessor.getBlockState(pos.below()).is(BlockTags.WOLVES_SPAWNABLE_ON) && isBrightEnoughToSpawn(accessor, pos);
    }
    
    class BetterWolfAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T>
    {
        private final BetterWolf betterWolf;
        
        public BetterWolfAvoidEntityGoal(BetterWolf betterWolf, Class<T> p_30455_, float p_30456_, double p_30457_, double p_30458_)
        {
            super(betterWolf, p_30455_, p_30456_, p_30457_, p_30458_);
            this.betterWolf = betterWolf;
        }
        
        @Override
        public boolean canUse()
        {
            if(super.canUse() && this.toAvoid instanceof Llama)
            {
                return !this.betterWolf.isTame() && this.avoidLlama((Llama)this.toAvoid);
            }
            else
            {
                return false;
            }
        }
        
        private boolean avoidLlama(Llama llama)
        {
            return llama.getStrength() >= BetterWolf.this.random.nextInt(5);
        }
        
        @Override
        public void start()
        {
            BetterWolf.this.setTarget(null);
        }
    
        @Override
        public void tick()
        {
            BetterWolf.this.setTarget(null);
            super.tick();
        }
    }
}
