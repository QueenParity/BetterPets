package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageMovementSpeed
{
    private int entityId;
    private int movementSpeed = 0;
    
    public MessageMovementSpeed(int entityId, int movementSpeed)
    {
        this.entityId = entityId;
        this.movementSpeed = movementSpeed;
    }
    
    //fromBytes
    public static void encode(MessageMovementSpeed pkt, PacketBuffer buf)
    {
        buf.writeVarInt(pkt.entityId);
        buf.writeVarInt(pkt.movementSpeed);
    }
    
    //toBytes
    public static MessageMovementSpeed decode(PacketBuffer buf)
    {
        int entityId = buf.readVarInt();
        int movementSpeed = buf.readVarInt();
        return new MessageMovementSpeed(entityId, movementSpeed);
    }
    
    //onMessage
    public static class Handler
    {
        public static void handle(final MessageMovementSpeed pkt, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                PlayerEntity player = ctx.get().getSender();
                World world = player.world;
                Entity entity = world.getEntityByID(pkt.entityId);
                if(entity instanceof BetterWolfEntity)
                {
                    ((BetterWolfEntity)entity).getPetThirstStats().movementSpeed = pkt.movementSpeed;
                }
                else
                {
                    System.out.println(entity != null ? entity.getPosition() : "uhhhh");
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
