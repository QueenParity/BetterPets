package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.ThirstStats;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageThirstStats
{
    private int thirstLevel;
    private float saturation, exhaustion;
    private boolean poisoned;
    
    public MessageThirstStats(int thirstLevel, float saturation, float exhaustion, boolean poisoned)
    {
        this.thirstLevel = thirstLevel;
        this.saturation = saturation;
        this.exhaustion = exhaustion;
        this.poisoned = poisoned;
    }
    
    //fromBytes
    public static void encode(MessageThirstStats pkt, PacketBuffer buf)
    {
        buf.writeInt(pkt.thirstLevel);
        buf.writeFloat(pkt.saturation);
        buf.writeFloat(pkt.exhaustion);
        buf.writeBoolean(pkt.poisoned);
    }
    
    //toBytes
    public static MessageThirstStats decode(PacketBuffer buf)
    {
        int thirstLevel = buf.readInt();
        float saturation = buf.readFloat();
        float exhaustion = buf.readFloat();
        boolean poisoned = buf.readBoolean();
        return new MessageThirstStats(thirstLevel, saturation, exhaustion, poisoned);
    }
    
    public void handleClientSide()
    {
        ThirstStats stats = BetterPets.PROXY.getClientStats();
        stats.thirstLevel = this.thirstLevel;
        stats.saturation = this.saturation;
        stats.exhaustion = this.exhaustion;
        stats.poisoned = this.poisoned;
    }
    
    //onMessage
    public static class Handler
    {
        public static void handle(final MessageThirstStats pkt, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                pkt.handleClientSide();
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
