package com.kingparity.betterpets.core;

import com.kingparity.betterpets.crafting.WaterFilterRecipeSerializer;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModRecipeSerializers
{
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Reference.ID);

    public static final RegistryObject<WaterFilterRecipeSerializer> WATER_FILTER = RECIPE_SERIALIZERS.register("water_filter", WaterFilterRecipeSerializer::new);
}
