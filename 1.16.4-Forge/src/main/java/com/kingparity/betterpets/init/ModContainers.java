package com.kingparity.betterpets.init;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.inventory.container.PetChestContainer;
import com.kingparity.betterpets.inventory.container.WaterFilterContainer;
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
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.ID);
    
    public static final RegistryObject<ContainerType<WaterFilterContainer>> WATER_FILTER = register("water_filter", (IContainerFactory<WaterFilterContainer>) (windowId, playerInventory, data) -> {
        WaterFilterTileEntity waterFilter = (WaterFilterTileEntity)playerInventory.player.world.getTileEntity(data.readBlockPos());
        return new WaterFilterContainer(windowId, playerInventory, waterFilter);
    });
    
    public static final RegistryObject<ContainerType<PetChestContainer>> BETTER_WOLF = register("better_wolf", (IContainerFactory<PetChestContainer>) (windowId, playerInventory, data) -> {
        BetterWolfEntity betterWolf = (BetterWolfEntity)playerInventory.player.world.getEntityByID(data.readVarInt());
        return new PetChestContainer(windowId, playerInventory, betterWolf, betterWolf);
    });
    
    private static <T extends Container> RegistryObject<ContainerType<T>> register(String id, ContainerType.IFactory<T> factory)
    {
        return CONTAINER_TYPES.register(id, () -> new ContainerType<>(factory));
    }
}