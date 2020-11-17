package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.init.ModTileEntities;
import com.kingparity.betterpets.util.FluidUtils;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TankTileEntity extends TileFluidHandlerSynced implements ITickableTileEntity
{
    public TankTileEntity()
    {
        super(ModTileEntities.TANK.get(), 16000);
    }
    
    @Override
    public void tick()
    {
        if(!this.world.isRemote)
        {
            TileEntity tileEntity = this.world.getTileEntity(this.pos.down());
            if(tileEntity instanceof TankTileEntity)
            {
                IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
                if(fluidHandler != null)
                {
                    FluidUtils.transferFluid(this.tank, fluidHandler, this.getFluidLevel());
                }
                else
                {
                    throw new IllegalStateException("well this was unexpected :/ and is impossible");
                }
            }
        }
    }
}