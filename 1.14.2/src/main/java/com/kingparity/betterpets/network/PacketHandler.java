package com.kingparity.betterpets.network;

import com.kingparity.betterpets.network.message.*;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler
{
    /**
     * Credit: ProjectE PacketHandler.java
     */
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Reference.ID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    
    public static void register()
    {
        HANDLER.registerMessage(1, MessageAttachChest.class, MessageAttachChest::encode, MessageAttachChest::decode, MessageAttachChest.Handler::handle);
        HANDLER.registerMessage(2, MessageRemoveChest.class, MessageRemoveChest::encode, MessageRemoveChest::decode, MessageRemoveChest.Handler::handle);
        HANDLER.registerMessage(3, MessageOpenPetContainer.class, MessageOpenPetContainer::encode, MessageOpenPetContainer::decode, MessageOpenPetContainer.Handler::handle);
        HANDLER.registerMessage(4, MessagePetWindow.class, MessagePetWindow::encode, MessagePetWindow::decode, MessagePetWindow.Handler::handle);
        HANDLER.registerMessage(5, MessageCraftPetFood.class, MessageCraftPetFood::encode, MessageCraftPetFood::decode, MessageCraftPetFood.Handler::handle);
    }
    
    /**
     * Credit: ProjectE PacketHandler.java
     * Sends a packet to the server.<br>
     * Must be called Client side.
     */
    public static void sendToServer(Object msg)
    {
        HANDLER.sendToServer(msg);
    }
    
    /**
     * Credit: ProjectE PacketHandler.java
     * Send a packet to a specific player.<br>
     * Must be called Server side.
     */
    public static void sendTo(Object msg, ServerPlayerEntity player)
    {
        if(!(player instanceof FakePlayer))
        {
            HANDLER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
