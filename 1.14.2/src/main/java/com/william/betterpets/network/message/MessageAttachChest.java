package com.william.betterpets.network.message;

import com.william.betterpets.init.BetterPetItems;
import com.william.betterpets.util.IAttachableChest;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageAttachChest
{
    private int entityId;
    
    public MessageAttachChest(int entityId)
    {
        this.entityId = entityId;
    }
    
    //fromBytes
    public static void encode(MessageAttachChest pkt, PacketBuffer buf)
    {
        buf.writeVarInt(pkt.entityId);
    }
    
    //toBytes
    public static MessageAttachChest decode(PacketBuffer buf)
    {
        int entityId = buf.readVarInt();
        return new MessageAttachChest(entityId);
    }
    
    //onMessage
    public static class Handler
    {
        public static void handle(final MessageAttachChest pkt, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                World world = player.world;
                Entity targetEntity = world.getEntityByID(pkt.entityId);
                if(targetEntity != null && targetEntity instanceof IAttachableChest)
                {
                    float reachDistance = (float)player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
                    if(player.getDistance(targetEntity) < reachDistance)
                    {
                        IAttachableChest attachableChest = (IAttachableChest)targetEntity;
                        if(!attachableChest.hasChest())
                        {
                            attachableChest.attachChest(new ItemStack(BetterPetItems.PET_CHEST));
                            world.playSound(null, targetEntity.posX, targetEntity.posY, targetEntity.posZ, SoundType.WOOD.getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
    
    /*public void foo(Integer i)
    {
    
    }
    
    public static void main(String[] args)
    {
        biConsumer(MessageOpenStorage::foo);
    }*/
}
