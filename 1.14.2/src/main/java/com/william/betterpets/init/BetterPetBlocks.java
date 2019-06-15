package com.william.betterpets.init;

import com.william.betterpets.block.PropertiesBlock;
import com.william.betterpets.testGui.TestGuiBlock;
import com.william.betterpets.util.Reference;
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

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterPetBlocks
{
    private static final List<Block> BLOCKS = new LinkedList<>();
    
    public static List<Block> getBlocks()
    {
        return Collections.unmodifiableList(BLOCKS);
    }
    
    public static Item.Properties tabMisc = new Item.Properties().group(ItemGroup.MISC);
    
    public static Block TEST_GUI_BLOCK;
    
    private static final Block TEST_GUI_BLOCK_PROPERTIES = new PropertiesBlock(Material.IRON, MaterialColor.BLACK, 5.0F, 10.0F);
    
    @SubscribeEvent
    public static void addBlocks(final RegistryEvent.Register<Block> event)
    {
        TEST_GUI_BLOCK = register(null/*"test_gui_block"*/, new TestGuiBlock(TEST_GUI_BLOCK_PROPERTIES), tabMisc);
        
    
        BLOCKS.stream().forEach(item -> event.getRegistry().register(item));
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
