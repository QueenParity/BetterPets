package com.kingparity.betterpets.common;

import com.kingparity.betterpets.block.WaterCollectorBlock;
import com.kingparity.betterpets.block.WaterFilterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class CommonEvents
{
    public static final DataParameter<Optional<BlockPos>> WATER_COLLECTOR = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    public static final DataParameter<Optional<BlockPos>> WATER_FILTER = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    
    @SubscribeEvent
    public void onEntityInit(EntityEvent.EntityConstructing event)
    {
        if(event.getEntity() instanceof PlayerEntity)
        {
            event.getEntity().getDataManager().register(WATER_COLLECTOR, Optional.empty());
            event.getEntity().getDataManager().register(WATER_FILTER, Optional.empty());
        }
    }
    
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event)
    {
        if(event.getEntityPlayer().getDataManager().get(WATER_COLLECTOR).isPresent())
        {
            event.setCanceled(true);
        }
    
        if(event.getEntityPlayer().getDataManager().get(WATER_FILTER).isPresent())
        {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event)
    {
        BlockState state = event.getWorld().getBlockState(event.getPos());
        if(event.getEntityPlayer().getDataManager().get(WATER_COLLECTOR).isPresent())
        {
            if(!(state.getBlock() instanceof WaterCollectorBlock || state.getBlock() instanceof WaterFilterBlock))
            {
                event.setCanceled(true);
            }
        }
    
        if(!(state.getBlock() instanceof WaterFilterBlock) && event.getEntityPlayer().getDataManager().get(WATER_FILTER).isPresent())
        {
            event.setCanceled(true);
        }
    }
}
