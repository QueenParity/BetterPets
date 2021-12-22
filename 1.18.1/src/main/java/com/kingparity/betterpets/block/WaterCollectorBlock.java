package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.blockentity.WaterCollectorBlockEntity;
import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WaterCollectorBlock extends RotatedBlockObjectEntity
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public WaterCollectorBlock(Properties properties)
    {
        super(properties);
        SHAPES = this.generateShapes(this.getStateDefinition().getPossibleStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape[] BACK_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0.5, 0, 0.5, 2.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] BACK_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(13.5, 0, 0.5, 15.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] FRONT_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(13.5, 0, 13.5, 15.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] FRONT_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0.5, 0, 13.5, 2.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] WATER_TANK_STAND = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0, 5, 0, 16, 7, 16), Direction.SOUTH));
        final VoxelShape[] TANK_3 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 11, 1, 15, 12, 2), Direction.SOUTH));
        final VoxelShape[] TANK_4 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 13, 1, 15, 14, 2), Direction.SOUTH));
        final VoxelShape[] TANK_5 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 15, 1, 15, 16, 2), Direction.SOUTH));
        final VoxelShape[] TANK_6 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 17, 1, 15, 18, 2), Direction.SOUTH));
        final VoxelShape[] TANK_7 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 19, 1, 15, 20, 2), Direction.SOUTH));
        final VoxelShape[] TANK_8 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 21, 1, 15, 22, 2), Direction.SOUTH));
        final VoxelShape[] TANK_10 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 9, 14, 15, 10, 15), Direction.SOUTH));
        final VoxelShape[] TANK_11 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 11, 14, 15, 12, 15), Direction.SOUTH));
        final VoxelShape[] TANK_12 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 13, 14, 15, 14, 15), Direction.SOUTH));
        final VoxelShape[] TANK_13 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 15, 14, 15, 16, 15), Direction.SOUTH));
        final VoxelShape[] TANK_14 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 17, 14, 15, 18, 15), Direction.SOUTH));
        final VoxelShape[] TANK_15 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 19, 14, 15, 20, 15), Direction.SOUTH));
        final VoxelShape[] TANK_16 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 21, 14, 15, 22, 15), Direction.SOUTH));
        final VoxelShape[] TANK_18 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 9, 2, 2, 10, 14), Direction.SOUTH));
        final VoxelShape[] TANK_19 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 11, 2, 2, 12, 14), Direction.SOUTH));
        final VoxelShape[] TANK_20 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 13, 2, 2, 14, 14), Direction.SOUTH));
        final VoxelShape[] TANK_21 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 15, 2, 2, 16, 14), Direction.SOUTH));
        final VoxelShape[] TANK_22 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 17, 2, 2, 18, 14), Direction.SOUTH));
        final VoxelShape[] TANK_23 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 19, 2, 2, 20, 14), Direction.SOUTH));
        final VoxelShape[] TANK_24 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 21, 2, 2, 22, 14), Direction.SOUTH));
        final VoxelShape[] TANK_25 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 7, 1, 15, 8, 15), Direction.SOUTH));
        final VoxelShape[] TANK_26 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(14, 9, 2, 15, 10, 14), Direction.SOUTH));
        final VoxelShape[] TANK_27 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(14, 11, 2, 15, 12, 14), Direction.SOUTH));
        final VoxelShape[] TANK_28 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(14, 13, 2, 15, 14, 14), Direction.SOUTH));
        final VoxelShape[] TANK_29 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(14, 15, 2, 15, 16, 14), Direction.SOUTH));
        final VoxelShape[] TANK_30 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(14, 17, 2, 15, 18, 14), Direction.SOUTH));
        final VoxelShape[] TANK_31 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(14, 19, 2, 15, 20, 14), Direction.SOUTH));
        final VoxelShape[] TANK_32 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(14, 21, 2, 15, 22, 14), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_1 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1.2, 7, 1.2, 14.8, 21, 1.8), Direction.SOUTH));
        final VoxelShape[] TANK_2 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 9, 1, 15, 10, 2), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_2 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1.2, 7, 14.2, 14.8, 21, 14.8), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_3 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1.2, 7, 1.8, 1.8, 21, 14.2), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_4 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(14.2, 7, 1.8, 14.8, 21, 14.2), Direction.SOUTH));
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.getValue(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(BACK_LEFT_LEG[direction.get2DDataValue()]);
            shapes.add(BACK_RIGHT_LEG[direction.get2DDataValue()]);
            shapes.add(FRONT_RIGHT_LEG[direction.get2DDataValue()]);
            shapes.add(FRONT_LEFT_LEG[direction.get2DDataValue()]);
            shapes.add(WATER_TANK_STAND[direction.get2DDataValue()]);
            shapes.add(TANK_3[direction.get2DDataValue()]);
            shapes.add(TANK_4[direction.get2DDataValue()]);
            shapes.add(TANK_5[direction.get2DDataValue()]);
            shapes.add(TANK_6[direction.get2DDataValue()]);
            shapes.add(TANK_7[direction.get2DDataValue()]);
            shapes.add(TANK_8[direction.get2DDataValue()]);
            shapes.add(TANK_10[direction.get2DDataValue()]);
            shapes.add(TANK_11[direction.get2DDataValue()]);
            shapes.add(TANK_12[direction.get2DDataValue()]);
            shapes.add(TANK_13[direction.get2DDataValue()]);
            shapes.add(TANK_14[direction.get2DDataValue()]);
            shapes.add(TANK_15[direction.get2DDataValue()]);
            shapes.add(TANK_16[direction.get2DDataValue()]);
            shapes.add(TANK_18[direction.get2DDataValue()]);
            shapes.add(TANK_19[direction.get2DDataValue()]);
            shapes.add(TANK_20[direction.get2DDataValue()]);
            shapes.add(TANK_21[direction.get2DDataValue()]);
            shapes.add(TANK_22[direction.get2DDataValue()]);
            shapes.add(TANK_23[direction.get2DDataValue()]);
            shapes.add(TANK_24[direction.get2DDataValue()]);
            shapes.add(TANK_25[direction.get2DDataValue()]);
            shapes.add(TANK_26[direction.get2DDataValue()]);
            shapes.add(TANK_27[direction.get2DDataValue()]);
            shapes.add(TANK_28[direction.get2DDataValue()]);
            shapes.add(TANK_29[direction.get2DDataValue()]);
            shapes.add(TANK_30[direction.get2DDataValue()]);
            shapes.add(TANK_31[direction.get2DDataValue()]);
            shapes.add(TANK_32[direction.get2DDataValue()]);
            shapes.add(TANK_INSIDE_1[direction.get2DDataValue()]);
            shapes.add(TANK_2[direction.get2DDataValue()]);
            shapes.add(TANK_INSIDE_2[direction.get2DDataValue()]);
            shapes.add(TANK_INSIDE_3[direction.get2DDataValue()]);
            shapes.add(TANK_INSIDE_4[direction.get2DDataValue()]);
            
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity)
    {
        return createTickerHelper(blockEntity, ModBlockEntities.WATER_COLLECTOR.get(), WaterCollectorBlockEntity::tick);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if(!level.isClientSide)
        {
            FluidUtil.interactWithFluidHandler(player, hand, level, pos, result.getDirection());
    
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new WaterCollectorBlockEntity(pos, state);
    }
}
