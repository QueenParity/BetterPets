package com.kingparity.betterpets.blockentity;

import net.minecraft.nbt.CompoundTag;

/**
 * Author: MrCrayfish
 */
public interface IFluidTankWriter
{
    void writeTanks(CompoundTag compound);
    
    boolean areTanksEmpty();
}