package com.kingparity.betterpets.core;

import com.kingparity.betterpets.crafting.WaterFilterRecipeSerializer;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipeSerializers
{
    private static final List<IRecipeSerializer> RECIPES = new ArrayList<>();

    public static final WaterFilterRecipeSerializer WATER_FILTER = register(Reference.ID + ":water_filter", new WaterFilterRecipeSerializer());

    private static <T extends IRecipeSerializer<? extends IRecipe<?>>> T register(String name, T t)
    {
        t.setRegistryName(new ResourceLocation(name));
        RECIPES.add(t);
        return t;
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        RECIPES.forEach(serializer -> event.getRegistry().register(serializer));
        RECIPES.clear();
    }
}
