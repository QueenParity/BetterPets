package com.kingparity.betterpets.init;

import com.kingparity.betterpets.block.PetResourcesCrafterBlock;
import com.kingparity.betterpets.block.PropertiesBlock;
import com.kingparity.betterpets.block.WaterCollectorBlock;
import com.kingparity.betterpets.block.WaterFilterBlock;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterPetBlocks
{
    public static Block PET_RESOURCES_CRAFTER, WATER_COLLECTOR, WATER_FILTER;
    
    public static Item.Properties tabMisc = new Item.Properties().group(ItemGroup.MISC);
    
    private static final Block PET_RESOURCES_CRAFTER_PROPERTIES = new PropertiesBlock(Material.IRON, MaterialColor.BLACK, 5.0F, 10.0F);
    private static final Block WATER_COLLECTOR_PROPERTIES = new PropertiesBlock(Material.IRON, MaterialColor.BLACK, 5.0F, 10.0F);
    private static final Block WATER_FILTER_PROPERTIES = new PropertiesBlock(Material.IRON, MaterialColor.BLACK, 5.0F, 10.0F);
    
    private static final List<Block> BLOCKS = new LinkedList<>();
    
    public static List<Block> getBlocks()
    {
        return Collections.unmodifiableList(BLOCKS);
    }
    
    static
    {
        PET_RESOURCES_CRAFTER = register("pet_resources_crafter", new PetResourcesCrafterBlock(PET_RESOURCES_CRAFTER_PROPERTIES), tabMisc);
        WATER_COLLECTOR = register("water_collector", new WaterCollectorBlock(WATER_COLLECTOR_PROPERTIES), tabMisc);
        WATER_FILTER = register("water_filter", new WaterFilterBlock(WATER_FILTER_PROPERTIES), tabMisc);
    }
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event)
    {
        BLOCKS.forEach(item -> event.getRegistry().register(item));
    }
    
    private static Block register(String name, PropertiesBlock propertiesBlock, Item.Properties itemBuilder)
    {
        Block block = new Block(Block.Properties.from(propertiesBlock));
        return register(name, block, new BlockItem(block, itemBuilder));
    }
    
    private static Block register(String name, Block block, Item.Properties itemBuilder)
    {
        return register(name, block, new BlockItem(block, itemBuilder));
    }
    
    private static Block register(String name, Block block, BlockItem itemBlock)
    {
        block.setRegistryName(new ResourceLocation(Reference.ID, name));
        if(block.getRegistryName() != null)
        {
            itemBlock.setRegistryName(block.getRegistryName());
        }
        else
        {
            throw new NullPointerException("Block registry name null!");
        }
        BLOCKS.add(block);
        BetterPetItems.add(itemBlock);
        return block;
    }
}
