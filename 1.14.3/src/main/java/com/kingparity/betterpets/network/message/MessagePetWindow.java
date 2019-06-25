package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.BetterPets;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessagePetWindow
{
    private int windowId;
    private int entityId;
    
    public MessagePetWindow(int windowId, int entityId)
    {
        this.windowId = windowId;
        this.entityId = entityId;
    }
    
    //fromBytes
    public static void encode(MessagePetWindow pkt, PacketBuffer buf)
    {
        buf.writeVarInt(pkt.windowId);
        buf.writeVarInt(pkt.entityId);
    }
    
    //toBytes
    public static MessagePetWindow decode(PacketBuffer buf)
    {
        int windowId = buf.readVarInt();
        int entityId = buf.readVarInt();
        return new MessagePetWindow(windowId, entityId);
    }
    
    //onMessage
    public static class Handler
    {
        public static void handle(final MessagePetWindow pkt, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> BetterPets.PROXY.openPetWindow(pkt));
            ctx.get().setPacketHandled(true);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getWindowId()
    {
        return this.windowId;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getEntityId()
    {
        return this.entityId;
    }
}
