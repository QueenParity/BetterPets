package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageMovementSpeed implements IMessage<MessageMovementSpeed>
{
    private int entityId;
    private int movementSpeed = 0;
    
    public MessageMovementSpeed()
    {
    
    }
    
    public MessageMovementSpeed(int entityId, int movementSpeed)
    {
        this.entityId = entityId;
        this.movementSpeed = movementSpeed;
    }
    
    @Override
    public void encode(MessageMovementSpeed message, PacketBuffer buffer)
    {
        buffer.writeInt(message.entityId);
        buffer.writeInt(message.movementSpeed);
    }
    
    @Override
    public MessageMovementSpeed decode(PacketBuffer buffer)
    {
        int entityId = buffer.readInt();
        int movementSpeed = buffer.readInt();
        return new MessageMovementSpeed(entityId, movementSpeed);
    }
    
    @Override
    public void handle(MessageMovementSpeed message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            PlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                World world = player.world;
                Entity entity = world.getEntityByID(message.entityId);
                if(entity instanceof BetterWolfEntity)
                {
                    ((BetterWolfEntity)entity).getPetThirstStats().movementSpeed = message.movementSpeed;
                }
                else
                {
                    System.out.println(entity != null ? entity.getPosition() : "uhhhh");
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
