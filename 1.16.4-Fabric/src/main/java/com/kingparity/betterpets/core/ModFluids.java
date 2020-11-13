package com.kingparity.betterpets.core;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.fluid.FilteredWaterFluid;
import com.kingparity.betterpets.names.FluidNames;
import com.kingparity.betterpets.names.ItemNames;
import com.kingparity.betterpets.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModFluids implements ModInitializer
{
    public static FlowableFluid STILL_FILTERED_WATER;
    public static FlowableFluid FLOWING_FILTERED_WATER;
    
    public static Item FILTERED_WATER_BUCKET;
    
    public static Block FILTERED_WATER;
    
    private static <T extends FlowableFluid> T register(String name, T fluid)
    {
        return Registry.register(Registry.FLUID, new Identifier(Reference.ID, name), fluid);
    }
    
    private static <T extends Item> T register(String name, T item)
    {
        return Registry.register(Registry.ITEM, new Identifier(Reference.ID, name), item);
    }
    
    private static <T extends Block> T register(String name, T block)
    {
        return Registry.register(Registry.BLOCK, new Identifier(Reference.ID, name), block);
    }
    
    @Override
    public void onInitialize()
    {
        STILL_FILTERED_WATER = register(FluidNames.FILTERED_WATER, new FilteredWaterFluid.Still());
        FLOWING_FILTERED_WATER = register(FluidNames.FLOWING_FILTERED_WATER, new FilteredWaterFluid.Flowing());
        FILTERED_WATER_BUCKET = register(ItemNames.FILTERED_WATER_BUCKET, new BucketItem(STILL_FILTERED_WATER, new FabricItemSettings().recipeRemainder(Items.BUCKET).maxCount(1).group(BetterPets.GROUP)));
        FILTERED_WATER = register(FluidNames.FILTERED_WATER, new FluidBlock(STILL_FILTERED_WATER, FabricBlockSettings.copy(Blocks.WATER)){});
        
        /*STILL_FILTERED_WATER = Registry.register(Registry.FLUID, new Identifier(Reference.ID, FluidNames.FILTERED_WATER), new FilteredWaterFluid.Still());
        FLOWING_FILTERED_WATER = Registry.register(Registry.FLUID, new Identifier(Reference.ID, FluidNames.FLOWING_FILTERED_WATER), new FilteredWaterFluid.Flowing());
        FILTERED_WATER_BUCKET = Registry.register(Registry.ITEM, new Identifier(Reference.ID, ItemNames.FILTERED_WATER_BUCKET), new BucketItem(STILL_FILTERED_WATER, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(BetterPets.GROUP)));
        FILTERED_WATER = Registry.register(Registry.BLOCK, new Identifier(Reference.ID, FluidNames.FILTERED_WATER), new FluidBlock(STILL_FILTERED_WATER, FabricBlockSettings.copy(Blocks.WATER)){});*/
    }
}