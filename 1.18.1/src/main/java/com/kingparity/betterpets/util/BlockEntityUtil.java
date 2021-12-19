package com.kingparity.betterpets.util;


import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;

/**
 * Author: MrCrayfish
 */
public class BlockEntityUtil
{
    /**
     * Sends an update packet to clients tracking a tile entity.
     *
     * @param tileEntity the tile entity to update
     */
    public static void sendUpdatePacket(BlockEntity tileEntity)
    {
        Packet<ClientGamePacketListener> packet = tileEntity.getUpdatePacket();
        if(packet != null)
        {
            sendUpdatePacket(tileEntity.getLevel(), tileEntity.getBlockPos(), packet);
        }
    }
    
    /**
     * Sends an update packet to clients tracking a tile entity with a specific CompoundTag
     *
     * @param blockEntity the tile entity to update
     */
    public static void sendUpdatePacket(BlockEntity blockEntity, CompoundTag compound)
    {
        ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(blockEntity, e -> compound);
        sendUpdatePacket(blockEntity.getLevel(), blockEntity.getBlockPos(), packet);
    }
    
    public static void sendUpdatePacket(BlockEntity blockEntity, ServerPlayer player)
    {
        sendUpdatePacket(blockEntity, blockEntity.getUpdateTag(), player);
    }
    
    private static void sendUpdatePacket(BlockEntity blockEntity, CompoundTag compound, ServerPlayer player)
    {
        ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(blockEntity, e -> compound);
        player.connection.send(packet);
    }
    
    public static void sendUpdatePacketSimple(BlockEntity blockEntity, CompoundTag compound)
    {
        ResourceLocation id = BlockEntityType.getKey(blockEntity.getType());
        compound.putString("id", id.toString());
        compound.putInt("x", blockEntity.getBlockPos().getX());
        compound.putInt("y", blockEntity.getBlockPos().getY());
        compound.putInt("z", blockEntity.getBlockPos().getZ());
        ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(blockEntity, e -> compound);
        sendUpdatePacket(blockEntity.getLevel(), blockEntity.getBlockPos(), packet);
    }
    
    private static void sendUpdatePacket(Level level, BlockPos pos, Packet<ClientGamePacketListener> packet)
    {
        if(level instanceof ServerLevel)
        {
            ServerLevel server = (ServerLevel)level;
            List<ServerPlayer> players = server.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false);
            players.forEach(player -> player.connection.send(packet));
        }
    }
}