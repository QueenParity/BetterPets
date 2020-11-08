package com.kingparity.betterpets.entity;

import com.kingparity.betterpets.core.ModEntities;
import com.kingparity.betterpets.entity.goal.BetterWolfBegGoal;
import com.kingparity.betterpets.inventory.IWolfChest;
import com.kingparity.betterpets.inventory.WolfChestInventory;
import com.kingparity.betterpets.util.InventoryUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public class BetterWolfEntity extends TameableEntity implements IWolfChest
{
    private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> CHEST = EntityDataManager.createKey(BetterWolfEntity.class, DataSerializers.BOOLEAN);
    public static final Predicate<LivingEntity> TARGET_ENTITIES = (entity) -> {
        EntityType<?> type = entity.getType();
        return type == EntityType.SHEEP || type == EntityType.RABBIT || type == EntityType.FOX;
    };

    private WolfChestInventory inventory;
    
    private float headRotationCourse;
    private float headRotationCourseOld;
    private boolean isWet;
    private boolean isShaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;

    public BetterWolfEntity(EntityType<? extends TameableEntity> type, World world)
    {
        super(type, world);
        this.setTamed(false);
    }

    @Override
    protected void registerGoals()
    {
        this.sitGoal = new SitGoal(this);
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, this.sitGoal);
        this.goalSelector.addGoal(3, new BetterWolfEntity.BetterWolfAvoidEntityGoal(this, LlamaEntity.class, 24.0F, 1.5D, 1.5D));
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
        this.targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, TARGET_ENTITIES));
        this.targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.TARGET_DRY_BABY));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, false));
    }
    
    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(BEGGING, false);
        this.dataManager.register(COLLAR_COLOR, DyeColor.BLUE.getId());
        this.dataManager.register(CHEST, false);
    }
    
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        this.setAngry(compound.getBoolean("Angry"));
        if(compound.contains("CollarColor", 99))
        {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }
        if(compound.contains("Chest", Constants.NBT.TAG_BYTE))
        {
            this.setChest(compound.getBoolean("Chest"));
            if(compound.contains("Inventory", Constants.NBT.TAG_LIST))
            {
                this.initInventory();
                InventoryUtil.readInventoryToNBT(compound, "Inventory", inventory);
            }
        }
    }
    
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("Angry", this.isAngry());
        compound.putByte("CollarColor", (byte)this.getCollarColor().getId());
        compound.putBoolean("Chest", this.hasChest());
        if(this.hasChest() && inventory != null)
        {
            InventoryUtil.writeInventoryToNBT(compound, "Inventory", inventory);
        }
    }
    
    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable)
    {
        BetterWolfEntity betterWolf = ModEntities.BETTER_WOLF.get().create(this.world);
        UUID uuid = this.getOwnerId();
        if(uuid != null)
        {
            betterWolf.setOwnerId(uuid);
            betterWolf.setTamed(true);
        }

        return betterWolf;
    }
    
    public boolean isAngry()
    {
        return (this.dataManager.get(TAMED) & 2) != 0;
    }
    
    public void setAngry(boolean angry)
    {
        byte tamed = this.dataManager.get(TAMED);
        if(angry)
        {
            this.dataManager.set(TAMED, (byte)(tamed | 2));
        }
        else
        {
            this.dataManager.set(TAMED, (byte)(tamed & -3));
        }
    }
    
    public boolean isBegging()
    {
        return this.dataManager.get(BEGGING);
    }
    
    public void setBegging(boolean beg)
    {
        this.dataManager.set(BEGGING, beg);
    }
    
    public DyeColor getCollarColor()
    {
        return DyeColor.byId(this.dataManager.get(COLLAR_COLOR));
    }
    
    public void setCollarColor(DyeColor collarcolor)
    {
        this.dataManager.set(COLLAR_COLOR, collarcolor.getId());
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
        WolfChestInventory original = this.inventory;
        this.inventory = new WolfChestInventory(this, 27);
        // Copies the inventory if it exists already over to the new instance
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
    }

    @Override
    public void onDeath(DamageSource cause)
    {
        this.isWet = false;
        this.isShaking = false;
        this.prevTimeWolfIsShaking = 0.0F;
        this.timeWolfIsShaking = 0.0F;
        if(this.hasChest() && inventory != null)
        {
            InventoryHelper.dropInventoryItems(world, this, inventory);
        }
        super.onDeath(cause);
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
    public float getShadingWhileWet(float partialTicks)
    {
        return 0.75F + MathHelper.lerp(partialTicks, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) / 2.0F * 0.25F;
    }
    
    @OnlyIn(Dist.CLIENT)
    public float getShakeAngle(float partialTicks, float p_70923_2_)
    {
        float f = (MathHelper.lerp(partialTicks, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) + p_70923_2_) / 1.8F;
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
            return this.isTamed() ? (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
        }
    }

    @Override
    public WolfChestInventory getInventory()
    {
        if(this.hasChest() && this.inventory == null)
        {
            this.initInventory();
        }
        return this.inventory;
    }

    @Override
    public void attachChest(ItemStack stack)
    {
        if(!stack.isEmpty() && stack.getItem() == Item.getItemFromBlock(Blocks.CHEST))
        {
            this.setChest(true);
            this.initInventory();

            CompoundNBT itemTag = stack.getTag();
            if(itemTag != null)
            {
                CompoundNBT blockEntityTag = itemTag.getCompound("BlockEntityTag");
                if(!blockEntityTag.isEmpty() && blockEntityTag.contains("Items", Constants.NBT.TAG_LIST))
                {
                    NonNullList<ItemStack> chestInventory = NonNullList.withSize(27, ItemStack.EMPTY);
                    ItemStackHelper.loadAllItems(blockEntityTag, chestInventory);
                    for(int i = 0; i < chestInventory.size(); i++)
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
            Vec3d target = new Vec3d(0, 0.75, -0.75).rotateYaw(-this.rotationYaw * 0.017453292F).add(getPositionVector());
            InventoryUtil.dropInventoryItems(world, target.x, target.y, target.z, this.inventory);
            this.inventory = null;
            this.setChest(false);
            world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.addEntity(new ItemEntity(world, target.x, target.y, target.z, new ItemStack(Blocks.CHEST)));
        }
    }

    @Override
    public ITextComponent getWolfChestName()
    {
        return this.getDisplayName();
    }

    private class BetterWolfAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T>
    {
        private final BetterWolfEntity betterWolf;

        public BetterWolfAvoidEntityGoal(BetterWolfEntity betterWolf, Class<T> entityClassToAvoid, float avoidDistance, double farSpeed, double nearSpeed)
        {
            super(betterWolf, entityClassToAvoid, avoidDistance, farSpeed, nearSpeed);
            this.betterWolf = betterWolf;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
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

        private boolean avoidLlama(LlamaEntity llamaIn)
        {
            return llamaIn.getStrength() >= BetterWolfEntity.this.rand.nextInt(5);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting()
        {
            BetterWolfEntity.this.setAttackTarget((LivingEntity)null);
            super.startExecuting();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void tick()
        {
            BetterWolfEntity.this.setAttackTarget((LivingEntity)null);
            super.tick();
        }
    }
}
