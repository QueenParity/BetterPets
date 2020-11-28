package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.BetterPets;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateFoodStats implements IMessage<MessageUpdateFoodStats>
{
    private int entityId;
    private int foodLevel;
    private float saturation, exhaustion;
    
    public MessageUpdateFoodStats()
    {
    
    }
    
    public MessageUpdateFoodStats(int entityId, int foodLevel, float saturation, float exhaustion)
    {
        this.entityId = entityId;
        this.foodLevel = foodLevel;
        this.saturation = saturation;
        this.exhaustion = exhaustion;
    }
    
    @Override
    public void encode(MessageUpdateFoodStats message, PacketBuffer buffer)
    {
        buffer.writeInt(message.entityId);
        buffer.writeInt(message.foodLevel);
        buffer.writeFloat(message.saturation);
        buffer.writeFloat(message.exhaustion);
    }
    
    @Override
    public MessageUpdateFoodStats decode(PacketBuffer buffer)
    {
        int entityId = buffer.readInt();
        int thirstLevel = buffer.readInt();
        float saturation = buffer.readFloat();
        float exhaustion = buffer.readFloat();
        return new MessageUpdateFoodStats(entityId, thirstLevel, saturation, exhaustion);
    }
    
    @Override
    public void handle(MessageUpdateFoodStats message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> BetterPets.PROXY.syncFoodStats(message));
        supplier.get().setPacketHandled(true);
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
