package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.inventory.IAttachableChest;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRemoveChest implements IMessage<MessageRemoveChest>
{
    private int entityId;
    
    public MessageRemoveChest()
    {
    
    }
    
    public MessageRemoveChest(int entityId)
    {
        this.entityId = entityId;
    }
    
    @Override
    public void encode(MessageRemoveChest message, PacketBuffer buffer)
    {
        buffer.writeInt(message.entityId);
    }
    
    @Override
    public MessageRemoveChest decode(PacketBuffer buffer)
    {
        return new MessageRemoveChest(buffer.readInt());
    }
    
    @Override
    public void handle(MessageRemoveChest message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                World world = player.world;
                Entity targetEntity = world.getEntityByID(message.entityId);
                if(targetEntity instanceof IAttachableChest)
                {
                    float reachDistance = (float) player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
                    if(player.getDistance(targetEntity) < reachDistance)
                    {
                        IAttachableChest attachableChest = (IAttachableChest) targetEntity;
                        if(attachableChest.hasChest())
                        {
                            attachableChest.removeChest();
                            world.playSound(null, targetEntity.getPosX(), targetEntity.getPosY(), targetEntity.getPosZ(), SoundType.WOOD.getBreakSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}