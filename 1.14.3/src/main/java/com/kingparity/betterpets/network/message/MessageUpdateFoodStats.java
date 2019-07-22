package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.BetterPetMod;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateFoodStats
{
    private int entityId;
    private int foodLevel;
    private float saturation, exhaustion;
    
    public MessageUpdateFoodStats(int entityId, int foodLevel, float saturation, float exhaustion)
    {
        this.entityId = entityId;
        this.foodLevel = foodLevel;
        this.saturation = saturation;
        this.exhaustion = exhaustion;
    }
    
    //fromBytes
    public static void encode(MessageUpdateFoodStats pkt, PacketBuffer buf)
    {
        buf.writeInt(pkt.entityId);
        buf.writeInt(pkt.foodLevel);
        buf.writeFloat(pkt.saturation);
        buf.writeFloat(pkt.exhaustion);
    }
    
    //toBytes
    public static MessageUpdateFoodStats decode(PacketBuffer buf)
    {
        int entityId = buf.readInt();
        int thirstLevel = buf.readInt();
        float saturation = buf.readFloat();
        float exhaustion = buf.readFloat();
        return new MessageUpdateFoodStats(entityId, thirstLevel, saturation, exhaustion);
    }
    
    //onMessage
    public static class Handler
    {
        public static void handle(final MessageUpdateFoodStats pkt, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> BetterPetMod.PROXY.syncFoodStats(pkt));
            ctx.get().setPacketHandled(true);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getEntityId()
    {
        return entityId;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getFoodLevel()
    {
        return foodLevel;
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
}