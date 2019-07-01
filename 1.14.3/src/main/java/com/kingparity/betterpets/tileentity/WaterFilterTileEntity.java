package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.common.CommonEvents;
import com.kingparity.betterpets.init.BetterPetTileEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Optional;

public class WaterFilterTileEntity extends TileEntity implements ITickableTileEntity
{
    public final int MAX_CONTENTS = 12000;       // 12 buckets
    
    private int fluidAmount = 0;
    private int tubingEntityId;
    private BlockPos tubingTileEntityPos;
    
    private PlayerEntity tubingEntity;
    
    public WaterFilterTileEntity()
    {
        super(BetterPetTileEntities.WATER_FILTER_TILE_ENTITY);
    }
    
    public void fillTank()
    {
        fluidAmount = MAX_CONTENTS;
        syncToClient();
    }
    
    public boolean addFluid(int amount)
    {
        if(fluidAmount + amount <= MAX_CONTENTS)
        {
            fluidAmount += amount;
            syncToClient();
            return true;
        }
        return false;
    }
    
    public boolean removeFluid(int amount)
    {
        if(fluidAmount >= amount)
        {
            fluidAmount -= amount;
            syncToClient();
            return true;
        }
        return false;
    }
    
    public boolean hasFluid()
    {
        return fluidAmount != 0;
    }
    
    public int getCapacity()
    {
        return MAX_CONTENTS;
    }
    
    public int getFluidAmount()
    {
        return fluidAmount;
    }
    
    public ResourceLocation getStill()
    {
        return new ResourceLocation("block/water_still");
    }
    
    public ResourceLocation getFlowing()
    {
        return new ResourceLocation("block/water_flow");
    }
    
    public PlayerEntity getTubingEntity()
    {
        return tubingEntity;
    }
    
    @Nullable
    public BlockPos getTubingTileEntityPos()
    {
        return tubingTileEntityPos;
    }
    
    @Nullable
    public TileEntity getTubingTileEntity()
    {
        return world.getTileEntity(tubingTileEntityPos);
    }
    
    public void setTubingTileEntityPos(BlockPos tubingTileEntityPos)
    {
        this.tubingTileEntityPos = tubingTileEntityPos;
        this.syncToClient();
    }
    
    public void setTubingEntity(@Nullable PlayerEntity entity)
    {
        if(!world.isRemote)
        {
            if(entity != null && entity.getDataManager().get(CommonEvents.WATER_COLLECTOR).isPresent())
            {
                BlockPos collectorPos = entity.getDataManager().get(CommonEvents.WATER_COLLECTOR).get();
                this.tubingTileEntityPos = collectorPos;
                
                TileEntity tileEntity = entity.world.getTileEntity(collectorPos);
                if(tileEntity instanceof WaterCollectorTileEntity)
                {
                    WaterCollectorTileEntity waterCollector = (WaterCollectorTileEntity)tileEntity;
                    waterCollector.setTubingTileEntityPos(this.pos);
                    waterCollector.setTubingEntity(null);
                }
                else
                {
                    System.out.println("erm............");
                }
            }
            else
            {
                if(tubingEntity != null)
                {
                    tubingEntity.getDataManager().set(CommonEvents.WATER_FILTER, Optional.empty());
                }
                this.tubingEntity = null;
                this.tubingEntityId = -1;
                if(entity != null)
                {
                    this.tubingEntityId = entity.getEntityId();
                    entity.getDataManager().set(CommonEvents.WATER_FILTER, Optional.ofNullable(this.getPos()));
                }
            }
            this.syncToClient();
        }
    }
    
    @Override
    public void tick()
    {
        if(tubingEntityId != -1)
        {
            if(tubingEntity == null)
            {
                Entity entity = world.getEntityByID(tubingEntityId);
                if(entity instanceof PlayerEntity)
                {
                    tubingEntity = (PlayerEntity)entity;
                }
                else if(!world.isRemote)
                {
                    tubingEntityId = -1;
                    this.syncToClient();
                }
            }
        }
        else if(world.isRemote && tubingEntity != null)
        {
            tubingEntity = null;
        }
        
        if(!world.isRemote)
        {
            if(tubingEntity != null)
            {
                if(Math.sqrt(tubingEntity.getDistanceSq(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ())) > 6.0 || !tubingEntity.isAlive())
                {
                    if(tubingEntity.isAlive())
                    {
                        world.playSound(null, tubingEntity.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                    tubingEntity.getDataManager().set(CommonEvents.WATER_FILTER, Optional.empty());
                    tubingEntityId = -1;
                    tubingEntity = null;
                    this.syncToClient();
                    //TODO add breaking sound
                }
            }
            
            if(fluidAmount < MAX_CONTENTS)
            {
                if(world.isRaining())
                {
                    if(world.rand.nextInt(20) == 1)
                    {
                        float temperature = world.getBiome(pos).getTemperature(pos);
                        if(!(temperature < 0.15F))
                        {
                            if(world.isThundering())
                            {
                                temperature += 1.0F;
                            }
                            if(!this.addFluid(Math.round(500 * temperature)))
                            {
                                this.fillTank();
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        if(compound.contains("FluidAmount", Constants.NBT.TAG_INT))
        {
            this.fluidAmount = compound.getInt("FluidAmount");
        }
        if(compound.contains("TubingEntityId", Constants.NBT.TAG_INT))
        {
            this.tubingEntityId = compound.getInt("TubingEntityId");
        }
        if(compound.contains("TubingTileEntityPos", Constants.NBT.TAG_COMPOUND))
        {
            this.tubingTileEntityPos = NBTUtil.readBlockPos(compound.getCompound("TubingTileEntityPos"));
        }
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("FluidAmount", fluidAmount);
        if(tubingTileEntityPos != null)
        {
            compound.put("TubingTileEntityPos", NBTUtil.writeBlockPos(this.tubingTileEntityPos));
        }
        return compound;
    }
    
    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT compound = write(new CompoundNBT());
        compound.putInt("TubingEntityId", this.tubingEntityId);
        return compound;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 3536.0D;
    }
    
    public void syncToClient()
    {
        this.markDirty();
        if(!world.isRemote)
        {
            if(world instanceof ServerWorld)
            {
                ServerWorld server = (ServerWorld)world;
                ChunkPos chunkPos = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
                ChunkManager manager = (server.getChunkProvider()).chunkManager;
                if(manager != null)
                {
                    SUpdateTileEntityPacket packet = getUpdatePacket();
                    if(packet != null)
                    {
                        manager.getTrackingPlayers(chunkPos, false).forEach(e -> e.connection.sendPacket(packet));
                    }
                }
            }
        }
    }
    
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
    {
        read(packet.getNbtCompound());
    }
}