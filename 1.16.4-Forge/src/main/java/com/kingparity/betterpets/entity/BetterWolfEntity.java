package com.kingparity.betterpets.entity;

import com.kingparity.betterpets.entity.ai.goal.BetterWolfBegGoal;
import com.kingparity.betterpets.init.ModEntities;
import com.kingparity.betterpets.inventory.BetterWolfInventory;
import com.kingparity.betterpets.inventory.IAttachableChest;
import com.kingparity.betterpets.network.PacketHandler;
import com.kingparity.betterpets.network.message.MessageAttachChest;
import com.kingparity.betterpets.network.message.MessageOpenPetChest;
import com.kingparity.betterpets.network.message.MessageRemoveChest;
import com.kingparity.betterpets.util.InventoryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public class BetterWolfEntity extends TameableEntity implements IAngerable, IAttachableChest, IInventoryChangedListener
{
    private static final DataParameter<Boolean> CHEST = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> field_234232_bz_ = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.VARINT);
    public static final Predicate<LivingEntity> TARGET_ENTITIES = (p_213440_0_) -> {
        EntityType<?> entity = p_213440_0_.getType();
        return entity == EntityType.SHEEP || entity == EntityType.RABBIT || entity == EntityType.FOX;
    };
    
    private float headRotationCourse;
    private float headRotationCourseOld;
    private boolean isWet;
    private boolean isShaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    private static final RangedInteger field_234230_bG_ = TickRangeConverter.convertRange(20, 39);
    private UUID field_234231_bH_;
    
    private BetterWolfInventory inventory;
    
    public BetterWolfEntity(EntityType<? extends BetterWolfEntity> type, World world)
    {
        super(type, world);
        this.setTamed(false);
    }
    
    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(3, new BetterWolfEntity.AvoidEntityGoal(this, LlamaEntity.class, 24.0F, 1.5D, 1.5D));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new BetterWolfBegGoal(this, 8.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::func_233680_b_));
        this.targetSelector.addGoal(5, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, TARGET_ENTITIES));
        this.targetSelector.addGoal(6, new NonTamedTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.TARGET_DRY_BABY));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, false));
        this.targetSelector.addGoal(8, new ResetAngerGoal<>(this, true));
    }
    
    public static AttributeModifierMap.MutableAttribute prepareAttributes()
    {
        return MobEntity.func_233666_p_()
            .createMutableAttribute(Attributes.MOVEMENT_SPEED, (double)0.3F)
            .createMutableAttribute(Attributes.MAX_HEALTH, 8.0D)
            .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D);
    }
    
    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(CHEST, false);
        this.dataManager.register(BEGGING, false);
        this.dataManager.register(COLLAR_COLOR, DyeColor.BLUE.getId());
        this.dataManager.register(field_234232_bz_, 0);
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState block)
    {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }
    
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        if(compound.contains("CollarColor", 99))
        {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }
        
        if(compound.contains("Chest", Constants.NBT.TAG_BYTE))
        {
            this.setChest(compound.getBoolean("Chest"));
        }
        
        if(compound.contains("Inventory", Constants.NBT.TAG_LIST))
        {
            this.initInventory();
            InventoryUtil.readInventoryToNBT(compound, "Inventory", inventory);
        }
        
        this.readAngerNBT((ServerWorld)this.world, compound);
    }
    
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putByte("CollarColor", (byte)this.getCollarColor().getId());
        compound.putBoolean("Chest", this.hasChest());
        if(inventory != null)
        {
            InventoryUtil.writeInventoryToNBT(compound, "Inventory", inventory);
        }
        this.writeAngerNBT(compound);
    }
    
    @Override
    public boolean hasChest()
    {
        return this.dataManager.get(CHEST);
    }
    
    public void setChest(boolean chest)
    {
        this.dataManager.set(CHEST, chest);
    }
    
    private void initInventory()
    {
        BetterWolfInventory original = this.inventory;
        this.inventory = new BetterWolfInventory(this, 17);
        if(original != null)
        {
            for(int i = 0; i < original.getSizeInventory(); i++)
            {
                ItemStack stack = original.getStackInSlot(i);
                if(!stack.isEmpty())
                {
                    this.inventory.setInventorySlotContents(i, stack.copy());
                }
            }
        }
        this.inventory.addListener(this);
    }
    
    private void updateSlots()
    {
        if(this.world.isRemote)
        {
            ItemStack chest = this.inventory.getStackInSlot(1);
            if(chest.getItem() == Items.CHEST && !this.hasChest())
            {
                this.setChest(true);
                PacketHandler.instance.sendToServer(new MessageAttachChest(this.getEntityId()));
            }
            else if(chest.getItem() != Items.CHEST && this.hasChest())
            {
                this.setChest(false);
                PacketHandler.instance.sendToServer(new MessageRemoveChest(this.getEntityId()));
            }
        }
    }
    
    @Override
    public void onInventoryChanged(IInventory invBasic)
    {
        this.updateSlots();
    }
    
    @Override
    public BetterWolfInventory getInventory()
    {
        if(this.inventory == null)
        {
            this.initInventory();
        }
        return this.inventory;
    }
    
    @Override
    public void attachChest(ItemStack stack)
    {
        if(!stack.isEmpty() && stack.getItem() == Items.CHEST)
        {
            this.setChest(true);
            
            CompoundNBT itemTag = stack.getTag();
            if(itemTag != null)
            {
                CompoundNBT blockEntityTag = itemTag.getCompound("BlockEntityTag");
                if(!blockEntityTag.isEmpty() && blockEntityTag.contains("Items", Constants.NBT.TAG_LIST))
                {
                    NonNullList<ItemStack> chestInventory = NonNullList.withSize(17, ItemStack.EMPTY);
                    ItemStackHelper.loadAllItems(blockEntityTag, chestInventory);
                    for(int i = 2; i < chestInventory.size(); i++)
                    {
                        this.inventory.setInventorySlotContents(i, chestInventory.get(i));
                    }
                }
            }
        }
    }
    
    @Override
    public void removeChest()
    {
        if(this.inventory != null)
        {
            Vector3d target = new Vector3d(0, 0.75, -0.75).rotateYaw(-this.rotationYaw * 0.017453292F).add(this.getPositionVec());
            InventoryUtil.dropInventoryItems(world, target.x, target.y, target.z, this.inventory, 2);
            this.setChest(false);
            world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            //world.addEntity(new ItemEntity(world, target.x, target.y, target.z, new ItemStack(Blocks.CHEST)));
        }
    }
    
    @Override
    protected SoundEvent getAmbientSound()
    {
        if(this.func_233678_J__())
        {
            return SoundEvents.ENTITY_WOLF_GROWL;
        }
        else if(this.rand.nextInt(3) == 0)
        {
            return this.isTamed() && this.getHealth() < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        }
        else
        {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    }
    
    @Override
    public ITextComponent getPetChestName()
    {
        return this.getDisplayName();
    }
    
    @Override
    public void openInventory(PlayerEntity player)
    {
        Vector3d target = new Vector3d(0, 0.75, -0.75).rotateYaw(-this.rotationYaw * 0.017453292F).add(this.getPositionVec());
        this.world.playSound(null, target.x, target.y, target.z, SoundEvents.BLOCK_CHEST_OPEN, this.getSoundCategory(), 0.5F, 0.9F);
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return SoundEvents.ENTITY_WOLF_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }
    
    @Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }
    
    @Override
    public void livingTick()
    {
        super.livingTick();
        if(!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround)
        {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.world.setEntityState(this, (byte)8);
        }
        
        if(!this.world.isRemote)
        {
            this.func_241359_a_((ServerWorld)this.world, true);
        }
    }
    
    @Override
    public void tick()
    {
        super.tick();
        if(this.isAlive())
        {
            this.headRotationCourseOld = this.headRotationCourse;
            if(this.isBegging())
            {
                this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
            }
            else
            {
                this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
            }
            
            if(this.isInWaterRainOrBubbleColumn())
            {
                this.isWet = true;
                if(this.isShaking && !this.world.isRemote)
                {
                    this.world.setEntityState(this, (byte)56);
                    this.func_242326_eZ();
                }
            }
            else if((this.isWet || this.isShaking) && this.isShaking)
            {
                if(this.timeWolfIsShaking == 0.0F)
                {
                    this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                }
    
                this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
                this.timeWolfIsShaking += 0.05F;
                if(this.prevTimeWolfIsShaking >= 2.0F)
                {
                    this.isWet = false;
                    this.isShaking = false;
                    this.prevTimeWolfIsShaking = 0.0F;
                    this.timeWolfIsShaking = 0.0F;
                }
    
                if(this.timeWolfIsShaking > 0.4F)
                {
                    float posY = (float)this.getPosY();
                    int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);
                    Vector3d vector3d = this.getMotion();
                    
                    for(int j = 0; j < i; j++)
                    {
                        float offsetX = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        float offsetZ = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        this.world.addParticle(ParticleTypes.SPLASH, this.getPosX() + (double)offsetX, (double)(posY + 0.8F), this.getPosZ() + (double)offsetZ, vector3d.x, vector3d.y, vector3d.z);
                    }
                }
            }
        }
    }
    
    private void func_242326_eZ()
    {
        this.isShaking = false;
        this.timeWolfIsShaking = 0.0F;
        this.prevTimeWolfIsShaking = 0.0F;
    }
    
    @Override
    public void onDeath(DamageSource cause)
    {
        this.isWet = false;
        this.isShaking = false;
        this.prevTimeWolfIsShaking = 0.0F;
        this.timeWolfIsShaking = 0.0F;
        super.onDeath(cause);
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean isWolfWet()
    {
        return this.isWet;
    }
    
    @OnlyIn(Dist.CLIENT)
    public float getShadingWhileWet(float partialTicks)
    {
        return Math.min(0.5F + MathHelper.lerp(partialTicks, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) / 2.0F * 0.5F, 1.0F);
    }
    
    @OnlyIn(Dist.CLIENT)
    public float getShakeAngle(float partialTicks, float offset)
    {
        float f = (MathHelper.lerp(partialTicks, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) + offset) / 1.8F;
        if(f < 0.0F)
        {
            f = 0.0F;
        }
        else if(f > 1.0F)
        {
            f = 1.0F;
        }
    
        return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }
    
    @OnlyIn(Dist.CLIENT)
    public float getInterestedAngle(float partialTicks)
    {
        return MathHelper.lerp(partialTicks, this.headRotationCourseOld, this.headRotationCourse) * 0.15F * (float)Math.PI;
    }
    
    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size)
    {
        return size.height * 0.8F;
    }
    
    @Override
    public int getVerticalFaceSpeed()
    {
        return this.isEntitySleeping() ? 20 : super.getVerticalFaceSpeed();
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if(this.isInvulnerableTo(source))
        {
            return false;
        }
        else
        {
            Entity entity = source.getTrueSource();
            this.func_233687_w_(false);
            if(entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity))
            {
                amount = (amount + 1.0F) / 2.0F;
            }
            
            return super.attackEntityFrom(source, amount);
        }
    }
    
    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if(flag)
        {
            this.applyEnchantments(this, entity);
        }
        
        return flag;
    }
    
    @Override
    public void setTamed(boolean tamed)
    {
        super.setTamed(tamed);
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
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand)
    {
        if(player.isCrouching())
        {
            if(this.world.isRemote)
            {
                PacketHandler.instance.sendToServer(new MessageOpenPetChest(this.getEntityId()));
                Minecraft.getInstance().player.swingArm(Hand.MAIN_HAND);
                return ActionResultType.SUCCESS;
            }
            else
            {
                return super.func_230254_b_(player, hand);
            }
        }
        else
        {
            ItemStack itemstack = player.getHeldItem(hand);
            Item item = itemstack.getItem();
            if(this.world.isRemote)
            {
                boolean flag = this.isOwner(player) || this.isTamed() || item == Items.BONE && !this.isTamed() && !this.func_233678_J__();
                return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
            }
            else
            {
                if(this.isTamed())
                {
                    if(this.isBreedingItem(itemstack) && this.getHealth() < this.getMaxHealth())
                    {
                        if(!player.abilities.isCreativeMode)
                        {
                            itemstack.shrink(1);
                        }
                
                        this.heal((float) item.getFood().getHealing());
                        return ActionResultType.SUCCESS;
                    }
            
                    if(!(item instanceof DyeItem))
                    {
                        ActionResultType actionresulttype = super.func_230254_b_(player, hand);
                        if((!actionresulttype.isSuccessOrConsume() || this.isChild()) && this.isOwner(player))
                        {
                            this.func_233687_w_(!this.isSitting());
                            this.isJumping = false;
                            this.navigator.clearPath();
                            this.setAttackTarget((LivingEntity) null);
                            return ActionResultType.SUCCESS;
                        }
                
                        return actionresulttype;
                    }
            
                    DyeColor dyecolor = ((DyeItem) item).getDyeColor();
                    if(dyecolor != this.getCollarColor())
                    {
                        this.setCollarColor(dyecolor);
                        if(!player.abilities.isCreativeMode)
                        {
                            itemstack.shrink(1);
                        }
                
                        return ActionResultType.SUCCESS;
                    }
                }
                else if(item == Items.BONE && !this.func_233678_J__())
                {
                    if(!player.abilities.isCreativeMode)
                    {
                        itemstack.shrink(1);
                    }
            
                    if(this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player))
                    {
                        this.setTamedBy(player);
                        this.navigator.clearPath();
                        this.setAttackTarget((LivingEntity) null);
                        this.func_233687_w_(true);
                        this.world.setEntityState(this, (byte) 7);
                    }
                    else
                    {
                        this.world.setEntityState(this, (byte) 6);
                    }
            
                    return ActionResultType.SUCCESS;
                }
        
                return super.func_230254_b_(player, hand);
            }
        }
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if(id == 8)
        {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        }
        else if(id == 56)
        {
            this.func_242326_eZ();
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public float getTailRotation()
    {
        if(this.func_233678_J__())
        {
            return 1.5393804F;
        }
        else
        {
            return this.isTamed() ? (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * (float) Math.PI : ((float) Math.PI / 5F);
        }
    }
    
    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
        Item item = stack.getItem();
        return item.isFood() && item.getFood().isMeat();
    }
    
    @Override
    public int getMaxSpawnedInChunk()
    {
        return 8;
    }
    
    @Override
    public int getAngerTime()
    {
        return this.dataManager.get(field_234232_bz_);
    }
    
    @Override
    public void setAngerTime(int time)
    {
        this.dataManager.set(field_234232_bz_, time);
    }
    
    @Override
    public void func_230258_H__()
    {
        this.setAngerTime(field_234230_bG_.getRandomWithinRange(this.rand));
    }
    
    @Nullable
    @Override
    public UUID getAngerTarget()
    {
        return this.field_234231_bH_;
    }
    
    @Override
    public void setAngerTarget(@Nullable UUID target)
    {
        this.field_234231_bH_ = target;
    }
    
    public DyeColor getCollarColor()
    {
        return DyeColor.byId(this.dataManager.get(COLLAR_COLOR));
    }
    
    public void setCollarColor(DyeColor collarColor)
    {
        this.dataManager.set(COLLAR_COLOR, collarColor.getId());
    }
    
    @Override
    public AgeableEntity func_241840_a(ServerWorld world, AgeableEntity entity)
    {
        BetterWolfEntity betterWolf = ModEntities.BETTER_WOLF.get().create(world);
        UUID uuid = this.getOwnerId();
        if(uuid != null)
        {
            betterWolf.setOwnerId(uuid);
            betterWolf.setTamed(true);
        }
        
        return betterWolf;
    }
    
    public void setBegging(boolean beg)
    {
        this.dataManager.set(BEGGING, beg);
    }
    
    @Override
    public boolean canMateWith(AnimalEntity otherAnimal)
    {
        if(otherAnimal == this)
        {
            return false;
        }
        else if(!this.isTamed())
        {
            return false;
        }
        else if(!(otherAnimal instanceof BetterWolfEntity))
        {
            return false;
        }
        else
        {
            BetterWolfEntity betterWolf = (BetterWolfEntity)otherAnimal;
            if(!betterWolf.isTamed())
            {
                return false;
            }
            else if(betterWolf.isEntitySleeping())
            {
                return false;
            }
            else
            {
                return this.isInLove() && betterWolf.isInLove();
            }
        }
    }
    
    public boolean isBegging()
    {
        return this.dataManager.get(BEGGING);
    }
    
    @Override
    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner)
    {
        if(!(target instanceof CreeperEntity) && !(target instanceof GhastEntity))
        {
            if(target instanceof BetterWolfEntity)
            {
                BetterWolfEntity betterWolf = (BetterWolfEntity)target;
                return !betterWolf.isTamed() || betterWolf.getOwner() != owner;
            }
            else if(target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canAttackPlayer((PlayerEntity)target))
            {
                return false;
            }
            else if(target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame())
            {
                return false;
            }
            else
            {
                return !(target instanceof TameableEntity) || !((TameableEntity)target).isTamed();
            }
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public boolean canBeLeashedTo(PlayerEntity player)
    {
        return !this.func_233678_J__() && super.canBeLeashedTo(player);
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public Vector3d func_241205_ce_()
    {
        return new Vector3d(0.0D, (double)(0.6F * this.getEyeHeight()), (double)(this.getWidth() * 0.4F));
    }
    
    class AvoidEntityGoal<T extends LivingEntity> extends net.minecraft.entity.ai.goal.AvoidEntityGoal<T>
    {
        private final BetterWolfEntity betterWolf;
        
        public AvoidEntityGoal(BetterWolfEntity betterWolf, Class<T> entityClassToAvoid, float avoidDistance, double farSpeed, double nearSpeed)
        {
            super(betterWolf, entityClassToAvoid, avoidDistance, farSpeed, nearSpeed);
            this.betterWolf = betterWolf;
        }
    
        @Override
        public boolean shouldExecute()
        {
            if(super.shouldExecute() && this.avoidTarget instanceof LlamaEntity)
            {
                return !this.betterWolf.isTamed() && this.avoidLlama((LlamaEntity)this.avoidTarget);
            }
            else
            {
                return false;
            }
        }
        
        private boolean avoidLlama(LlamaEntity llama)
        {
            return llama.getStrength() >= BetterWolfEntity.this.rand.nextInt(5);
        }
    
        @Override
        public void startExecuting()
        {
            BetterWolfEntity.this.setAttackTarget(null);
            super.startExecuting();
        }
    
        @Override
        public void tick()
        {
            BetterWolfEntity.this.setAttackTarget(null);
            super.tick();
        }
    }
}