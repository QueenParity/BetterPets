package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.BetterPetMod;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageMovementSpeed
{
    private UUID uuid;
    private int movementSpeed = 0;
    
    public MessageMovementSpeed(UUID uuid, int movementSpeed)
    {
        this.uuid = uuid;
        this.movementSpeed = movementSpeed;
    }
    
    //fromBytes
    public static void encode(MessageMovementSpeed pkt, PacketBuffer buf)
    {
        buf.writeUniqueId(pkt.uuid);
        buf.writeInt(pkt.movementSpeed);
    }
    
    //toBytes
    public static MessageMovementSpeed decode(PacketBuffer buf)
    {
        UUID uuid = buf.readUniqueId();
        int movementSpeed = buf.readInt();
        return new MessageMovementSpeed(uuid, movementSpeed);
    }
    
    public void handleServerSide()
    {
        BetterPetMod.PROXY.getStatsByUUID(uuid).movementSpeed = movementSpeed;
    }
    
    //onMessage
    public static class Handler
    {
        public static void handle(final MessageMovementSpeed pkt, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> pkt.handleServerSide());
            ctx.get().setPacketHandled(true);
        }
    }
}
