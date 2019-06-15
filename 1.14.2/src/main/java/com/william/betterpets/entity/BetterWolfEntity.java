package com.william.betterpets.entity;

import com.william.betterpets.init.BetterPetEntities;
import com.william.betterpets.init.BetterPetItems;
import com.william.betterpets.network.PacketHandler;
import com.william.betterpets.network.message.MessageAttachChest;
import com.william.betterpets.network.message.MessageOpenStorage;
import com.william.betterpets.network.message.MessageRemoveChest;
import com.william.betterpets.util.IAttachableChest;
import com.william.betterpets.util.InventoryUtil;
import com.william.betterpets.util.StorageInventory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class BetterWolfEntity extends TameableEntity implements IAttachableChest, IInventoryChangedListener
{
    private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.VARINT);
    
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> CHEST = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.BOOLEAN);
    //private static final DataParameter<Boolean> HAS_ARMOR = EntityDataManager.createKey(EntityBetterWolf.class, DataSerializers.BOOLEAN);
    //private static final DataParameter<ItemStack> ARMOR_ITEM = EntityDataManager.createKey(EntityBetterWolf.class, DataSerializers.ITEM_STACK);
    
    private static final DataParameter<Boolean> HAS_HAT = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> HAT_ITEM = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.ITEMSTACK);
    
    private static final DataParameter<Boolean> LAYING_DOWN = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.BOOLEAN);
    
    public static final Predicate<LivingEntity> field_213441_bD = (p_213440_0_) -> {
        EntityType<?> entitytype = p_213440_0_.getType();
        return entitytype == EntityType.SHEEP || entitytype == EntityType.RABBIT || entitytype == EntityType.FOX;
    };
    private float headRotationCourse;
    private float headRotationCourseOld;
    private boolean isWet;
    private boolean isShaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    private StorageInventory inventory;
    protected BetterWolfLayDownGoal layDownGoal;
    protected BetterWolfSleepGoal sleepGoal;
    
    public BetterWolfEntity(EntityType<? extends BetterWolfEntity> betterWolf, World world)
    {
        super(betterWolf, world);
        this.setTamed(false);
    }
    
    @Override
    protected void registerGoals()
    {
        this.field_70911_d = new SitGoal(this);
        this.layDownGoal = new BetterWolfLayDownGoal(this);
        this.sleepGoal = new BetterWolfSleepGoal(this);
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, this.field_70911_d);
        this.goalSelector.addGoal(2, this.layDownGoal);
        this.goalSelector.addGoal(2, this.sleepGoal);
        this.goalSelector.addGoal(3, new BetterWolfEntity.AvoidEntityGoal(this, LlamaEntity.class, 24.0F, 1.5D, 1.5D));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new BetterWolfFollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new BetterWolfBegGoal(this, 8.0F));
        this.goalSelector.addGoal(10, new BetterWolfLookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new BetterWolfLookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, field_213441_bD));
        this.targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.TARGET_DRY_BABY));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, false));
    }
    
    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.3F);
        if(this.isTamed())
        {
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        }
        else
        {
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        }
        
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }
    
    /**
     * Sets the active target the Task system uses for tracking
     */
    @Override
    public void setAttackTarget(@Nullable LivingEntity entitylivingbaseIn)
    {
        super.setAttackTarget(entitylivingbaseIn);
        if(entitylivingbaseIn == null)
        {
            this.setAngry(false);
        }
        else if(!this.isTamed())
        {
            this.setAngry(true);
        }
        
    }
    
    @Override
    protected void updateAITasks()
    {
        this.dataManager.set(DATA_HEALTH_ID, this.getHealth());
    }
    
    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(DATA_HEALTH_ID, this.getHealth());
        this.dataManager.register(BEGGING, false);
        this.dataManager.register(COLLAR_COLOR, DyeColor.RED.getId());
        
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(CHEST, false);
        //this.dataManager.register(HAS_ARMOR, false);
        //this.dataManager.register(ARMOR_ITEM, ItemStack.EMPTY);
        
        this.dataManager.register(HAS_HAT, false);
        this.dataManager.register(HAT_ITEM, ItemStack.EMPTY);
        
        this.dataManager.register(LAYING_DOWN, false);
        this.dataManager.register(SLEEPING, false);
    }
    
    private void initInventory()
    {
        Inventory original = this.inventory;
        this.inventory = new StorageInventory(17, this);
        // Copies the inventory if it exists already over to the new instance
        if(original != null)
        {
            original.removeListener(this);
            int x = Math.min(original.getSizeInventory(), this.inventory.getSizeInventory());
            
            for(int i = 0; i < x; ++i)
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
        if(!this.world.isRemote)
        {
            ItemStack chest = this.inventory.getStackInSlot(1);
            if(chest.getItem() == BetterPetItems.PET_CHEST && !this.hasChest())
            {
                this.setChest(true);
                PacketHandler.sendToServer(new MessageAttachChest(this.getEntityId()));
            }
            else if(chest.getItem() != BetterPetItems.PET_CHEST && this.hasChest())
            {
                this.setChest(false);
                PacketHandler.sendToServer(new MessageRemoveChest(this.getEntityId()));
            }
        }
    }
    
    @Override
    public void onInventoryChanged(IInventory iInventory)
    {
        this.updateSlots();
        ItemStack stack = inventory.getStackInSlot(0);
        this.setHatItem(stack);
        //this.setArmorItemStack(stack);
    }
    
    public void equipHat(ItemStack itemStack)
    {
        if(this.canEquipItem(itemStack))
        {
            this.inventory.setInventorySlotContents(0, itemStack);
        }
    }
    
    @Override
    protected boolean canEquipItem(ItemStack stack)
    {
        return stack.getItem() == BetterPetItems.PET_BIRTHDAY_HAT && (!this.hasHat() || stack.isEmpty());
    }
    
    @Override
    public StorageInventory getInventory()
    {
        if(/*this.hasChest() && */inventory == null)
        {
            this.initInventory();
        }
        return inventory;
    }
    
    @Override
    public void attachChest(ItemStack stack)
    {
        if(!stack.isEmpty() && stack.getItem() == BetterPetItems.PET_CHEST)
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
                    for(int i = 0; i < chestInventory.size(); i++)
                    {
                        this.inventory.setInventorySlotContents(i, chestInventory.get(i));
                    }
                }
            }
            world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }
    
    @Override
    public void removeChest()
    {
        if(this.inventory != null)
        {
            Vec3d target = new Vec3d(0, 0.75, -0.75).rotateYaw(-this.rotationYaw * 0.017453292F).add(getPositionVector());
            //InventoryUtil.dropInventoryItems(world, target.x, target.y, target.z, this.inventory);
            for(int i = 2; i < inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = inventory.getStackInSlot(i);
                
                if(!itemstack.isEmpty())
                {
                    InventoryUtil.spawnItemStack(world, target.x, target.y, target.z, itemstack);
                    //spawnItemStack(worldIn, x, y, z, itemstack);
                }
            }
            this.setChest(false);
            world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            //world.spawnEntity(new EntityItem(world, target.x, target.y, target.z, new ItemStack(Blocks.CHEST)));
        }
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn)
    {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }
    
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        /*compound.putBoolean("Angry", this.isAngry());
        compound.putByte("CollarColor", (byte)this.getCollarColor().getId());*/
        
        compound.putBoolean("LayingDown", this.isLayingDown());
        compound.putBoolean("Sleeping", this.isSleeping());
        
        compound.putBoolean("chest", this.hasChest());
        //compound.putBoolean("hasArmor", this.hasArmor());
        compound.putByte("CollarColor", (byte)this.getCollarColor().getId());
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("hasHat", this.hasHat());
        
        if(this.hasHat())
        {
            ItemStack stack = getHatItem();
            
            if(!stack.isEmpty())
            {
                compound.put("hatItem", stack.write(new CompoundNBT()));
            }
            else
            {
                compound.remove("hatItem");
            }
        }
        
        /*if(this.hasArmor())
        {
            ItemStack stack = getArmorItem();
            
            if(!stack.isEmpty())
            {
                compound.put("armorItem", stack.write(new CompoundNBT()));
            }
            else
            {
                compound.remove("armorItem");
            }
        }*/
        
        if(inventory != null)
        {
            InventoryUtil.writeInventoryToNBT(compound, "inventory", inventory);
        }
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        /*this.setAngry(compound.getBoolean("Angry"));
        if(compound.contains("CollarColor", 99))
        {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }*/
        if(this.layDownGoal != null)
        {
            this.layDownGoal.setLayingDown(compound.getBoolean("LayingDown"));
        }
        
        this.setLayingDown(compound.getBoolean("LayingDown"));
        
        if(this.sleepGoal != null)
        {
            this.sleepGoal.setSleeping(compound.getBoolean("Sleeping"));
        }
        
        this.setSleeping(compound.getBoolean("Sleeping"));
        
        if(compound.contains("chest", Constants.NBT.TAG_BYTE))
        {
            this.setChest(compound.getBoolean("chest"));
        }
        /*if(compound.contains("hasArmor", Constants.NBT.TAG_BYTE))
        {
            this.setArmor(compound.getBoolean("hasArmor"));
        }*/
        if(compound.contains("hasHat", Constants.NBT.TAG_BYTE))
        {
            this.setHat(compound.getBoolean("hasHat"));
        }
        if(compound.contains("CollarColor", 99))
        {
            this.setCollarColor(DyeColor.byId(compound.getByte("CollarColor")));
        }
        this.setVariant(compound.getInt("Variant"));
        
        CompoundNBT hatTags = compound.getCompound("hatItem");
        
        if(!hatTags.isEmpty())
        {
            ItemStack stack = ItemStack.read(hatTags);
            
            this.equipHat(stack);
        }
        
        /*NBTTagCompound armorTags = compound.getCompoundTag("armorItem");
        
        if(!armorTags.hasNoTags())
        {
            ItemStack stack = new ItemStack(armorTags);
            
            this.equipArmor(stack);
        }*/
        
        if(compound.contains("inventory", Constants.NBT.TAG_LIST))
        {
            this.initInventory();
            InventoryUtil.readInventoryToNBT(compound, "inventory", inventory);
        }
    }
    
    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason spawnReason, @Nullable ILivingEntityData livingData, @Nullable CompoundNBT compound)
    {
        livingData = super.onInitialSpawn(world, difficulty, spawnReason, livingData, compound);
        this.setVariant(getRandomVariant(this.world.rand));
        
        return livingData;
    }
    
    public static int getRandomVariant(Random random)
    {
        return random.nextInt(7);
    }
    
    public void setLayingDown(boolean layingDown)
    {
        this.dataManager.set(LAYING_DOWN, layingDown);
    }
    
    public void setSleeping(boolean sleeping)
    {
        this.dataManager.set(SLEEPING, sleeping);
    }
    
    public void setChest(boolean chest)
    {
        this.dataManager.set(CHEST, chest);
    }
    
    /*public void setArmor(boolean hasArmor)
    {
        this.dataManager.set(HAS_ARMOR, hasArmor);
    }*/
    
    public void setHat(boolean hat)
    {
        this.dataManager.set(HAS_HAT, hat);
    }
    
    public void setCollarColor(DyeColor collarcolor)
    {
        this.dataManager.set(COLLAR_COLOR, collarcolor.getId());
    }
    
    public void setVariant(int variant)
    {
        this.dataManager.set(VARIANT, variant);
    }
    
    public void setHatItem(ItemStack hat)
    {
        this.dataManager.set(HAT_ITEM, hat);
    }
    
    /*public void setArmorItem(ItemStack stack)
    {
        this.dataManager.set(ARMOR_ITEM, stack);
    }*/
    
    public void setBegging(boolean beg)
    {
        this.dataManager.set(BEGGING, beg);
    }
    
    public boolean isLayingDown()
    {
        return this.dataManager.get(LAYING_DOWN);
    }
    
    public boolean isSleeping()
    {
        return this.dataManager.get(SLEEPING);
    }
    
    @Override
    public boolean hasChest()
    {
        return this.dataManager.get(CHEST);
    }
    
    /*public boolean hasArmor()
    {
        return this.dataManager.get(HAS_ARMOR);
    }*/
    
    public boolean hasHat()
    {
        return this.dataManager.get(HAS_HAT);
    }
    
    public DyeColor getCollarColor()
    {
        return DyeColor.byId(this.dataManager.get(COLLAR_COLOR));
    }
    
    public int getVariant()
    {
        return this.dataManager.get(VARIANT);
    }
    
    public ItemStack getHatItem()
    {
        ItemStack stack = this.dataManager.get(HAT_ITEM);
        
        if(stack.getItem() != BetterPetItems.PET_BIRTHDAY_HAT)
        {
            this.dataManager.set(HAT_ITEM, ItemStack.EMPTY);
            return ItemStack.EMPTY;
        }
        
        return stack;
    }
    
    /*public ItemStack getArmorItem()
    {
        ItemStack stack = this.dataManager.get(ARMOR_ITEM);
        
        if(!isArmor(stack))
        {
            this.dataManager.set(ARMOR_ITEM, ItemStack.EMPTY);
            return ItemStack.EMPTY;
        }
        
        return stack;
    }*/
    
    public boolean isBegging()
    {
        return this.dataManager.get(BEGGING);
    }
    
    public String getTexture()
    {
        switch(this.getVariant())
        {
            case 0:
                return "black";
            case 1:
                return "ice";
            case 2:
                return "snow";
            case 3:
                return "gold";
            case 4:
                return "orange";
            case 5:
                return "brown";
            default:
                return "gray";
        }
    }
    
    @Override
    protected SoundEvent getAmbientSound()
    {
        if(this.isAngry())
        {
            return SoundEvents.ENTITY_WOLF_GROWL;
        }
        else if(this.rand.nextInt(3) == 0)
        {
            return this.isTamed() && this.dataManager.get(DATA_HEALTH_ID) < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        }
        else
        {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_WOLF_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }
    
    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }
    
    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
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
        
        if(!this.world.isRemote && this.getAttackTarget() == null && this.isAngry())
        {
            this.setAngry(false);
        }
        
    }
    
    /**
     * Called to update the entity's position/logic.
     */
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
                this.isShaking = false;
                this.timeWolfIsShaking = 0.0F;
                this.prevTimeWolfIsShaking = 0.0F;
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
                    float f = (float)this.getBoundingBox().minY;
                    int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);
                    Vec3d vec3d = this.getMotion();
                    
                    for(int j = 0; j < i; ++j)
                    {
                        float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        this.world.addParticle(ParticleTypes.SPLASH, this.posX + (double)f1, (double)(f + 0.8F), this.posZ + (double)f2, vec3d.x, vec3d.y, vec3d.z);
                    }
                }
            }
            
        }
    }
    
    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource cause)
    {
        this.isWet = false;
        this.isShaking = false;
        this.prevTimeWolfIsShaking = 0.0F;
        this.timeWolfIsShaking = 0.0F;
        super.onDeath(cause);
        if(inventory != null)
        {
            InventoryHelper.dropInventoryItems(world, this, inventory);
        }
    }
    
    /**
     * True if the wolf is wet
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isWolfWet()
    {
        return this.isWet;
    }
    
    /**
     * Used when calculating the amount of shading to apply while the wolf is wet.
     */
    @OnlyIn(Dist.CLIENT)
    public float getShadingWhileWet(float p_70915_1_)
    {
        return 0.75F + MathHelper.lerp(p_70915_1_, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) / 2.0F * 0.25F;
    }
    
    @OnlyIn(Dist.CLIENT)
    public float getShakeAngle(float p_70923_1_, float p_70923_2_)
    {
        float f = (MathHelper.lerp(p_70923_1_, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) + p_70923_2_) / 1.8F;
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
    public float getInterestedAngle(float p_70917_1_)
    {
        return MathHelper.lerp(p_70917_1_, this.headRotationCourseOld, this.headRotationCourse) * 0.15F * (float)Math.PI;
    }
    
    @Override
    protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_)
    {
        return p_213348_2_.height * 0.8F;
    }
    
    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    @Override
    public int getVerticalFaceSpeed()
    {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }
    
    /**
     * Called when the entity is attacked.
     */
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
            if(this.field_70911_d != null)
            {
                this.field_70911_d.setSitting(false);
            }
            
            if(entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity))
            {
                amount = (amount + 1.0F) / 2.0F;
            }
            
            return super.attackEntityFrom(source, amount);
        }
    }
    
    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
        if(flag)
        {
            this.applyEnchantments(this, entityIn);
        }
        
        return flag;
    }
    
    @Override
    public void setTamed(boolean tamed)
    {
        super.setTamed(tamed);
        if(tamed)
        {
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        }
        else
        {
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        }
        
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        Item item = itemstack.getItem();
        if(this.isTamed())
        {
            if(player.isSneaking())
            {
                if(!this.world.isRemote)
                {
                    PacketHandler.sendToServer(new MessageOpenStorage(this.getEntityId()));
                }
                return true;
            }
            else if(!itemstack.isEmpty())
            {
                if(item.isFood())
                {
                    if(item.getFood().isMeat() && this.dataManager.get(DATA_HEALTH_ID) < 20.0F)
                    {
                        if(!player.abilities.isCreativeMode)
                        {
                            itemstack.shrink(1);
                        }
    
                        this.heal((float)item.getFood().getHealing());
                        return true;
                    }
                }
                else if(item instanceof DyeItem)
                {
                    DyeColor dyecolor = ((DyeItem)item).getDyeColor();
                    if(dyecolor != this.getCollarColor())
                    {
                        this.setCollarColor(dyecolor);
                        if(!player.abilities.isCreativeMode)
                        {
                            itemstack.shrink(1);
                        }
    
                        return true;
                    }
                }
            }
            
            if(this.isOwner(player) && !this.world.isRemote && !this.isBreedingItem(itemstack))
            {
                if(!this.isSitting() && !this.isLayingDown())
                {
                    this.field_70911_d.setSitting(true);
                    this.layDownGoal.setLayingDown(false);
                }
                else if(this.isSitting() && !this.isLayingDown())
                {
                    this.field_70911_d.setSitting(false);
                    this.layDownGoal.setLayingDown(true);
                }
                else
                {
                    this.field_70911_d.setSitting(false);
                    this.layDownGoal.setLayingDown(false);
                }
                this.sleepGoal.setSleeping(false);
                //this.field_70911_d.setSitting(!this.isSitting());
                this.isJumping = false;
                this.navigator.clearPath();
                this.setAttackTarget((LivingEntity)null);
            }
        }
        else if(item == Items.BONE && !this.isAngry())
        {
            if(!player.abilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }
            
            if(!this.world.isRemote)
            {
                if(this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player))
                {
                    this.setTamedBy(player);
                    this.navigator.clearPath();
                    this.setAttackTarget(null);
                    this.field_70911_d.setSitting(true);
                    this.setHealth(20.0F);
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte)7);
                }
                else
                {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }
            
            return true;
        }
        
        return super.processInteract(player, hand);
    }
    
    /**
     * Handler for {@link World#setEntityState}
     */
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
        else
        {
            super.handleStatusUpdate(id);
        }
        
    }
    
    @OnlyIn(Dist.CLIENT)
    public float getTailRotation()
    {
        if(this.isAngry())
        {
            return 1.5393804F;
        }
        else
        {
            return this.isTamed() ? (0.55F - (this.getMaxHealth() - this.dataManager.get(DATA_HEALTH_ID)) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
        }
    }
    
    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
        Item item = stack.getItem();
        return item.isFood() && item.getFood().isMeat();
    }
    
    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk()
    {
        return 8;
    }
    
    /**
     * Determines whether this wolf is angry or not.
     */
    public boolean isAngry()
    {
        return (this.dataManager.get(TAMED) & 2) != 0;
    }
    
    /**
     * Sets whether this wolf is angry or not.
     */
    public void setAngry(boolean angry)
    {
        byte b0 = this.dataManager.get(TAMED);
        if(angry)
        {
            this.dataManager.set(TAMED, (byte)(b0 | 2));
        }
        else
        {
            this.dataManager.set(TAMED, (byte)(b0 & -3));
        }
        
    }
    
    
    @Override
    public BetterWolfEntity createChild(AgeableEntity ageable)
    {
        BetterWolfEntity betterWolf = BetterPetEntities.BETTER_WOLF.create(this.world);//EntityType.WOLF.create(this.world);
        UUID uuid = this.getOwnerId();
        if(uuid != null)
        {
            betterWolf.setOwnerId(uuid);
            betterWolf.setTamed(true);
        }
        
        return betterWolf;
    }
    
    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
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
            else if(betterWolf.isSitting())
            {
                return false;
            }
            else
            {
                return this.isInLove() && betterWolf.isInLove();
            }
        }
    }
    
    public boolean isHat(ItemStack stack)
    {
        return stack.getItem() == BetterPetItems.PET_BIRTHDAY_HAT;
    }
    
    @Override
    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner)
    {
        if(!(target instanceof CreeperEntity) && !(target instanceof GhastEntity))
        {
            if(target instanceof BetterWolfEntity)
            {
                BetterWolfEntity betterWolf = (BetterWolfEntity)target;
                if(betterWolf.isTamed() && betterWolf.getOwner() == owner)
                {
                    return false;
                }
            }
            
            if(target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canAttackPlayer((PlayerEntity)target))
            {
                return false;
            }
            else if(target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame())
            {
                return false;
            }
            else
            {
                return !(target instanceof CatEntity) || !((CatEntity)target).isTamed();
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
        return !this.isAngry() && super.canBeLeashedTo(player);
    }
    
    class AvoidEntityGoal<T extends LivingEntity> extends net.minecraft.entity.ai.goal.AvoidEntityGoal<T>
    {
        private final BetterWolfEntity field_190856_d;
        
        public AvoidEntityGoal(BetterWolfEntity wolfIn, Class<T> p_i47251_3_, float p_i47251_4_, double p_i47251_5_, double p_i47251_7_)
        {
            super(wolfIn, p_i47251_3_, p_i47251_4_, p_i47251_5_, p_i47251_7_);
            this.field_190856_d = wolfIn;
        }
        
        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute()
        {
            if(super.shouldExecute() && this.field_75376_d instanceof LlamaEntity)
            {
                return !this.field_190856_d.isTamed() && this.avoidLlama((LlamaEntity)this.field_75376_d);
            }
            else
            {
                return false;
            }
        }
        
        private boolean avoidLlama(LlamaEntity p_190854_1_)
        {
            return p_190854_1_.getStrength() >= BetterWolfEntity.this.rand.nextInt(5);
        }
        
        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting()
        {
            BetterWolfEntity.this.setAttackTarget(null);
            super.startExecuting();
        }
        
        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void tick()
        {
            BetterWolfEntity.this.setAttackTarget(null);
            super.tick();
        }
    }
}
