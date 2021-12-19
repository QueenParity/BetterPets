package com.kingparity.betterpets.init;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.block.TankBlock;
import com.kingparity.betterpets.block.WaterCollectorBlock;
import com.kingparity.betterpets.block.WaterFilterBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BetterPets.ID);
    
    public static final BlockBehaviour.Properties WOOD = BlockBehaviour.Properties.of(Material.WOOD).strength(0.5F).sound(SoundType.WOOD);
    public static final BlockBehaviour.Properties STONE = BlockBehaviour.Properties.of(Material.STONE).strength(0.5F).sound(SoundType.STONE);
    public static final BlockBehaviour.Properties IRON = BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL);
    
    public static final RegistryObject<Block> WATER_COLLECTOR = register("water_collector", new WaterCollectorBlock(WOOD));
    
    public static final RegistryObject<Block> WATER_FILTER = register("water_filter", new WaterFilterBlock(WOOD));
    
    //public static final RegistryObject<Block> FLUID_PIPE = register("fluid_pipe", new FluidPipeBlock(IRON));
    //public static final RegistryObject<Block> FLUID_PUMP = register("fluid_pump", new FluidPumpBlock(IRON));
    
    public static final RegistryObject<Block> TANK = register("tank", new TankBlock(IRON));
    
    public static final RegistryObject<LiquidBlock> FILTERED_WATER = register("filtered_water", new LiquidBlock(ModFluids.FLOWING_FILTERED_WATER, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()), null);
    
    private static <T extends Block> RegistryObject<T> register(String id, T block)
    {
        return register(id, block, itemBlock -> new BlockItem(itemBlock, new Item.Properties().tab(BetterPets.TAB)));
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
