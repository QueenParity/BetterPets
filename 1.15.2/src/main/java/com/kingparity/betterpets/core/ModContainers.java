package com.kingparity.betterpets.core;

import com.kingparity.betterpets.inventory.IWolfChest;
import com.kingparity.betterpets.inventory.container.WaterFilterContainer;
import com.kingparity.betterpets.inventory.container.WolfChestContainer;
import com.kingparity.betterpets.names.ContainerNames;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers
{
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Reference.ID);

    public static final RegistryObject<ContainerType<WaterFilterContainer>> WATER_FILTER = register(ContainerNames.WATER_FILTER, (IContainerFactory<WaterFilterContainer>) (windowId, playerInventory, data) -> {
        WaterFilterTileEntity waterFilter = (WaterFilterTileEntity)playerInventory.player.world.getTileEntity(data.readBlockPos());
        return new WaterFilterContainer(windowId, playerInventory, waterFilter);
    });
    
    public static final RegistryObject<ContainerType<WolfChestContainer>> WOLF_CHEST = register(ContainerNames.WOLF_CHEST, (IContainerFactory<WolfChestContainer>) (windowId, playerInventory, data) -> {
        IWolfChest storage = (IWolfChest) playerInventory.player.world.getEntityByID(data.readVarInt());
        return new WolfChestContainer(windowId, playerInventory, storage, playerInventory.player);
    });

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String id, ContainerType.IFactory<T> factory)
    {
        return CONTAINER_TYPES.register(id, () -> new ContainerType<>(factory));
    }
}