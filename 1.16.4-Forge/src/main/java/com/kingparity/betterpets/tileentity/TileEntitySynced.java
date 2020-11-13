package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.util.TileEntityUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public class TileEntitySynced extends TileEntity
{
    public TileEntitySynced(TileEntityType<?> tileEntityType)
    {
        super(tileEntityType);
    }
    
    public void syncToClient()
    {
        this.markDirty();
        TileEntityUtil.sendUpdatePacket(this);
    }
    
    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }
    
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(this.getPos(), 0, this.getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        this.read(null, pkt.getNbtCompound());
    }
}
