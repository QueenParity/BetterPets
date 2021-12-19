package com.kingparity.betterpets.init;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.blockentity.WaterFilterBlockEntity;
import com.kingparity.betterpets.inventory.menu.WaterFilterMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus
{
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, BetterPets.ID);
    
    public static final RegistryObject<MenuType<WaterFilterMenu>> WATER_FILTER = register("water_filter", (IContainerFactory<WaterFilterMenu>) (windowId, playerInventory, data) -> {
        WaterFilterBlockEntity waterFilter = (WaterFilterBlockEntity)playerInventory.player.level.getBlockEntity(data.readBlockPos());
        return new WaterFilterMenu(windowId, playerInventory, waterFilter);
    });
    
    /*public static final RegistryObject<ContainerType<PetChestContainer>> BETTER_WOLF = register("better_wolf", (IContainerFactory<PetChestContainer>) (windowId, playerInventory, data) -> {
        BetterWolfEntity betterWolf = (BetterWolfEntity)playerInventory.player.world.getEntityByID(data.readVarInt());
        return new PetChestContainer(windowId, playerInventory, betterWolf, betterWolf);
    });*/
    
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String id, MenuType.MenuSupplier<T> factory)
    {
        return MENU_TYPES.register(id, () -> new MenuType<>(factory));
    }
}
