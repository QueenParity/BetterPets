package com.kingparity.betterpets.core;

import com.kingparity.betterpets.functions.CopyFluidTanks;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;

/**
 * Author: MrCrayfish
 */
public class ModLootFunctions
{
    public static void register()
    {
        LootFunctionManager.registerFunction(new CopyFluidTanks.Serializer());
    }
}