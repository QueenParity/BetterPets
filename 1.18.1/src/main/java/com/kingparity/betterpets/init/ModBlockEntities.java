package com.kingparity.betterpets.init;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.blockentity.TankBlockEntity;
import com.kingparity.betterpets.blockentity.WaterCollectorBlockEntity;
import com.kingparity.betterpets.blockentity.WaterFilterBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BetterPets.ID);
    
    public static final RegistryObject<BlockEntityType<WaterCollectorBlockEntity>> WATER_COLLECTOR = register("water_collector", WaterCollectorBlockEntity::new, () -> new Block[]
        {
            ModBlocks.WATER_COLLECTOR.get()
        });
    
    public static final RegistryObject<BlockEntityType<WaterFilterBlockEntity>> WATER_FILTER = register("water_filter", WaterFilterBlockEntity::new, () -> new Block[]
        {
            ModBlocks.WATER_FILTER.get()
        });
    
    public static final RegistryObject<BlockEntityType<TankBlockEntity>> TANK = register("tank", TankBlockEntity::new, () -> new Block[]
        {
            ModBlocks.TANK.get()
        });
    
    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String id, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<Block[]> validBlocksSupplier)
    {
        return BLOCK_ENTITY_TYPES.register(id, () -> BlockEntityType.Builder.of(supplier, validBlocksSupplier.get()).build(null));
    }
}
