package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.inventory.IPetChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class MessageOpenPetChest implements IMessage<MessageOpenPetChest>
{
    private int entityId;
    
    public MessageOpenPetChest()
    {
    
    }
    
    public MessageOpenPetChest(int entityId)
    {
        this.entityId = entityId;
    }
    
    @Override
    public void encode(MessageOpenPetChest message, PacketBuffer buffer)
    {
        buffer.writeInt(message.entityId);
    }
    
    @Override
    public MessageOpenPetChest decode(PacketBuffer buffer)
    {
        return new MessageOpenPetChest(buffer.readInt());
    }
    
    @Override
    public void handle(MessageOpenPetChest message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                World world = player.world;
                Entity targetEntity = world.getEntityByID(message.entityId);
                if(targetEntity instanceof IPetChest)
                {
                    IPetChest storage = (IPetChest) targetEntity;
                    float reachDistance = (float) player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
                    if(player.getDistance(targetEntity) < reachDistance)
                    {
                        if(targetEntity instanceof BetterWolfEntity)
                        {
                            BetterWolfEntity betterWolf = (BetterWolfEntity)targetEntity;
                            NetworkHooks.openGui(player, storage.getPetChestContainerProvider(betterWolf), buffer -> buffer.writeVarInt(message.entityId));
                        }
                    }
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}