package com.william.betterpets.network.message;

import com.william.betterpets.util.IAttachableChest;
import com.william.betterpets.util.IStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRemoveChest
{
    private int entityId;
    
    public MessageRemoveChest(int entityId)
    {
        this.entityId = entityId;
    }
    
    //fromBytes
    public static void encode(MessageRemoveChest pkt, PacketBuffer buf)
    {
        buf.writeVarInt(pkt.entityId);
    }
    
    //toBytes
    public static MessageRemoveChest decode(PacketBuffer buf)
    {
        int entityId = buf.readVarInt();
        return new MessageRemoveChest(entityId);
    }
    
    //onMessage
    public static class Handler
    {
        public static void handle(final MessageRemoveChest pkt, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                World world = player.world;
                Entity targetEntity = world.getEntityByID(pkt.entityId);
                if(targetEntity != null && targetEntity instanceof IStorage)
                {
                    float reachDistance = (float)player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
                    if(player.getDistance(targetEntity) < reachDistance)
                    {
                        if(targetEntity instanceof IAttachableChest)
                        {
                            IAttachableChest attachableChest = (IAttachableChest)targetEntity;
                            attachableChest.removeChest();
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
