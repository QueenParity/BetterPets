package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.util.IAttachableChest;
import com.kingparity.betterpets.util.IPetContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageOpenPetContainer
{
    private int entityId;
    
    public MessageOpenPetContainer(int entityId)
    {
        this.entityId = entityId;
    }
    
    //fromBytes
    public static void encode(MessageOpenPetContainer pkt, PacketBuffer buf)
    {
        buf.writeVarInt(pkt.entityId);
    }
    
    //toBytes
    public static MessageOpenPetContainer decode(PacketBuffer buf)
    {
        int entityId = buf.readVarInt();
        return new MessageOpenPetContainer(entityId);
    }
    
    //onMessage
    public static class Handler
    {
        public static void handle(final MessageOpenPetContainer pkt, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                World world = player.world;
                Entity targetEntity = world.getEntityByID(pkt.entityId);
                if(targetEntity != null && targetEntity instanceof IPetContainer)
                {
                    float reachDistance = (float)player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
                    if(player.getDistance(targetEntity) < reachDistance)
                    {
                        if(targetEntity instanceof IAttachableChest)
                        {
                            IAttachableChest attachableChest = (IAttachableChest)targetEntity;
                            BetterWolfEntity betterWolf = (BetterWolfEntity)targetEntity;
                            attachableChest.getInventory().openGui(player, betterWolf);
                        }
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
