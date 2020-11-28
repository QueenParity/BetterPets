package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.BetterPets;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateThirstStats implements IMessage<MessageUpdateThirstStats>
{
    private int entityId;
    private int thirstLevel;
    private float saturation, exhaustion;
    private boolean poisoned;
    
    public MessageUpdateThirstStats()
    {
    
    }
    
    public MessageUpdateThirstStats(int entityId, int thirstLevel, float saturation, float exhaustion, boolean poisoned)
    {
        this.entityId = entityId;
        this.thirstLevel = thirstLevel;
        this.saturation = saturation;
        this.exhaustion = exhaustion;
        this.poisoned = poisoned;
    }
    
    @Override
    public void encode(MessageUpdateThirstStats message, PacketBuffer buffer)
    {
        buffer.writeInt(message.entityId);
        buffer.writeInt(message.thirstLevel);
        buffer.writeFloat(message.saturation);
        buffer.writeFloat(message.exhaustion);
        buffer.writeBoolean(message.poisoned);
    }
    
    @Override
    public MessageUpdateThirstStats decode(PacketBuffer buffer)
    {
        int entityId = buffer.readInt();
        int thirstLevel = buffer.readInt();
        float saturation = buffer.readFloat();
        float exhaustion = buffer.readFloat();
        boolean poisoned = buffer.readBoolean();
        return new MessageUpdateThirstStats(entityId, thirstLevel, saturation, exhaustion, poisoned);
    }
    
    @Override
    public void handle(MessageUpdateThirstStats message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> BetterPets.PROXY.syncThirstStats(message));
        supplier.get().setPacketHandled(true);
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
