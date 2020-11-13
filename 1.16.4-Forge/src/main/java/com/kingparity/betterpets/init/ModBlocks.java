package com.kingparity.betterpets.init;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.block.WaterCollectorBlock;
import com.kingparity.betterpets.block.WaterFilterBlock;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.ID);
    
    public static final AbstractBlock.Properties WOOD = AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(0.5F).sound(SoundType.WOOD);
    public static final AbstractBlock.Properties STONE = AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(0.5F).sound(SoundType.STONE);
    
    public static final RegistryObject<Block> OAK_WATER_COLLECTOR = register("oak_water_collector", new WaterCollectorBlock(WOOD));
    
    public static final RegistryObject<Block> OAK_WATER_FILTER = register("oak_water_filter", new WaterFilterBlock(WOOD));
    
    private static <T extends Block> RegistryObject<T> register(String id, T block)
    {
        return register(id, block, itemBlock -> new BlockItem(itemBlock, new Item.Properties().group(BetterPets.TAB)));
    }
    
    private static <T extends Block> RegistryObject<T> register(String id, T block, Function<T, BlockItem> supplier)
    {
        if(supplier != null)
        {
            ModItems.ITEMS.register(id, () -> supplier.apply(block));
        }
        return ModBlocks.BLOCKS.register(id, () -> block);
    }
}
