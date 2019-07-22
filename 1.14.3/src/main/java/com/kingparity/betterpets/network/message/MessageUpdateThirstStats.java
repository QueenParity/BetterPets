package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.BetterPetMod;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateThirstStats
{
    private int entityId;
    private int thirstLevel;
    private float saturation, exhaustion;
    private boolean poisoned;
    
    public MessageUpdateThirstStats(int entityId, int thirstLevel, float saturation, float exhaustion, boolean poisoned)
    {
        this.entityId = entityId;
        this.thirstLevel = thirstLevel;
        this.saturation = saturation;
        this.exhaustion = exhaustion;
        this.poisoned = poisoned;
    }
    
    //fromBytes
    public static void encode(MessageUpdateThirstStats pkt, PacketBuffer buf)
    {
        buf.writeInt(pkt.entityId);
        buf.writeInt(pkt.thirstLevel);
        buf.writeFloat(pkt.saturation);
        buf.writeFloat(pkt.exhaustion);
        buf.writeBoolean(pkt.poisoned);
    }
    
    //toBytes
    public static MessageUpdateThirstStats decode(PacketBuffer buf)
    {
        int entityId = buf.readInt();
        int thirstLevel = buf.readInt();
        float saturation = buf.readFloat();
        float exhaustion = buf.readFloat();
        boolean poisoned = buf.readBoolean();
        return new MessageUpdateThirstStats(entityId, thirstLevel, saturation, exhaustion, poisoned);
    }
    
    //onMessage
    public static class Handler
    {
        public static void handle(final MessageUpdateThirstStats pkt, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> BetterPetMod.PROXY.syncThirstStats(pkt));
            ctx.get().setPacketHandled(true);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getEntityId()
    {
        return entityId;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getThirstLevel()
    {
        return thirstLevel;
    }
    
    @OnlyIn(Dist.CLIENT)
    public float getSaturation()
    {
        return saturation;
    }
    
    @OnlyIn(Dist.CLIENT)
    public float getExhaustion()
    {
        return exhaustion;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean isPoisoned()
    {
        return poisoned;
    }
}