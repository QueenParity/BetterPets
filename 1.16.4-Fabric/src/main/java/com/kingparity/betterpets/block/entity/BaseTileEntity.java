package com.kingparity.betterpets.block.entity;

import alexiil.mc.lib.attributes.CombinableAttribute;
import alexiil.mc.lib.attributes.SearchOptions;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

public abstract class BaseTileEntity extends BlockEntity implements BlockEntityClientSerializable
{
    public BaseTileEntity(BlockEntityType<?> type)
    {
        super(type);
    }
    
    @Nonnull
    public <T> T getNeighborAttribute(CombinableAttribute<T> attribute, Direction direction)
    {
        return attribute.get(getWorld(), getPos().offset(direction), SearchOptions.inDirection(direction));
    }
    
    public DefaultedList<ItemStack> removeItemsForDrop()
    {
        return DefaultedList.of();
    }
    
    protected void sendPacket(ServerWorld world, CompoundTag tag)
    {
        tag.putString("id", BlockEntityType.getId(getType()).toString());
        sendPacket(world, new BlockEntityUpdateS2CPacket(getPos(), 127, tag));
    }
    
    protected void sendPacket(ServerWorld world, BlockEntityUpdateS2CPacket packet)
    {
        world.getPlayers(player -> player.squaredDistanceTo(Vec3d.of(getPos())) < 24 * 24)
            .forEach(player -> player.networkHandler.sendPacket(packet));
    }
    
    @Override
    public CompoundTag toClientTag(CompoundTag tag)
    {
        return tag;
    }
    
    @Override
    public void fromClientTag(CompoundTag tag) {}
    
    public void onPlacedBy(LivingEntity placer, ItemStack stack) {}
    
    public ActionResult onUse(PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        return ActionResult.PASS;
    }
}