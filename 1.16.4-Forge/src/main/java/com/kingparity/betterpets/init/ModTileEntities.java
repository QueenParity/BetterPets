package com.kingparity.betterpets.init;

import com.kingparity.betterpets.tileentity.FluidPipeTileEntity;
import com.kingparity.betterpets.tileentity.FluidPumpTileEntity;
import com.kingparity.betterpets.tileentity.WaterCollectorTileEntity;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModTileEntities
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Reference.ID);
    
    public static final RegistryObject<TileEntityType<WaterCollectorTileEntity>> WATER_COLLECTOR = register("water_collector", WaterCollectorTileEntity::new, () -> new Block[]
        {
            ModBlocks.WATER_COLLECTOR.get()
        });
    
    public static final RegistryObject<TileEntityType<WaterFilterTileEntity>> WATER_FILTER = register("water_filter", WaterFilterTileEntity::new, () -> new Block[]
        {
            ModBlocks.WATER_FILTER.get()
        });
    
    public static final RegistryObject<TileEntityType<FluidPipeTileEntity>> FLUID_PIPE = register("fluid_pipe", FluidPipeTileEntity::new, () -> new Block[]
        {
            ModBlocks.FLUID_PIPE.get()
        });
    
    public static final RegistryObject<TileEntityType<FluidPumpTileEntity>> FLUID_PUMP = register("fluid_pump", FluidPumpTileEntity::new, () -> new Block[]
        {
            ModBlocks.FLUID_PUMP.get()
        });
    
    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String id, Supplier<T> factoryIn, Supplier<Block[]> validBlocksSupplier)
    {
        return TILE_ENTITY_TYPES.register(id, () -> TileEntityType.Builder.create(factoryIn, validBlocksSupplier.get()).build(null));
    }
}