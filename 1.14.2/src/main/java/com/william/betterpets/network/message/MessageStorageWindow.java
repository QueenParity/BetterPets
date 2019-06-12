package com.william.betterpets.network.message;

import com.william.betterpets.BetterPets;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStorageWindow
{
    private int windowId;
    private int entityId;
    
    public MessageStorageWindow(int windowId, int entityId)
    {
        this.windowId = windowId;
        this.entityId = entityId;
    }
    
    //fromBytes
    public static void encode(MessageStorageWindow pkt, PacketBuffer buf)
    {
        buf.writeVarInt(pkt.windowId);
        buf.writeVarInt(pkt.entityId);
    }
    
    //toBytes
    public static MessageStorageWindow decode(PacketBuffer buf)
    {
        int windowId = buf.readVarInt();
        int entityId = buf.readVarInt();
        return new MessageStorageWindow(windowId, entityId);
    }
    
    //onMessage
    public static class Handler
    {
        public static void handle(final MessageStorageWindow pkt, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> BetterPets.PROXY.openStorageWindow(pkt));
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
    
    /*public void foo(Integer i)
    {
    
    }
    
    public static void main(String[] args)
    {
        biConsumer(MessageOpenStorage::foo);
    }*/
}
