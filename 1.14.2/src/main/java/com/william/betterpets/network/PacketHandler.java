package com.william.betterpets.network;

import com.william.betterpets.network.message.MessageAttachChest;
import com.william.betterpets.network.message.MessageOpenStorage;
import com.william.betterpets.network.message.MessageRemoveChest;
import com.william.betterpets.network.message.MessageStorageWindow;
import com.william.betterpets.util.Reference;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler
{
	/*public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ID);
	private static int messageId = 0;

    private static enum Side
    {
        CLIENT, SERVER, BOTH;
    }*/
    
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Reference.ID, "main_channel")).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
    
    public static void register()
    {
        HANDLER.registerMessage(1, MessageAttachChest.class, MessageAttachChest::encode, MessageAttachChest::decode, MessageAttachChest.Handler::handle);
        HANDLER.registerMessage(2, MessageRemoveChest.class, MessageRemoveChest::encode, MessageRemoveChest::decode, MessageRemoveChest.Handler::handle);
        HANDLER.registerMessage(3, MessageOpenStorage.class, MessageOpenStorage::encode, MessageOpenStorage::decode, MessageOpenStorage.Handler::handle);
        HANDLER.registerMessage(4, MessageStorageWindow.class, MessageStorageWindow::encode, MessageStorageWindow::decode, MessageStorageWindow.Handler::handle);
	    /*registerMessage(MessageAttachChest.class, Side.SERVER);
        registerMessage(MessageRemoveChest.class, Side.SERVER);
		registerMessage(MessageOpenStorage.class, Side.SERVER);
        registerMessage(MessageStorageWindow.class, Side.CLIENT);*/
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

	/*private static void registerMessage(Class packet, Side side)
    {
        if (side != Side.CLIENT)
            registerMessage(packet, net.minecraftforge.fml.relauncher.Side.SERVER);

        if (side != Side.SERVER)
            registerMessage(packet, net.minecraftforge.fml.relauncher.Side.CLIENT);
    }

    private static void registerMessage(Class packet, net.minecraftforge.fml.relauncher.Side side)
    {
        INSTANCE.registerMessage(packet, packet, messageId++, side);
    }*/
}
