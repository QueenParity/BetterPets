package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.blockentity.WaterFilterBlockEntity;
import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.BlockEntityUtil;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WaterFilterBlock extends RotatedBlockObjectEntity
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public WaterFilterBlock(Properties properties)
    {
        super(properties);
        SHAPES = this.generateShapes(this.getStateDefinition().getPossibleStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        /*final VoxelShape[] BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0, 0, 0, 16, 1, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_NW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0, 1, 0, 1, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_NE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(15, 1, 0, 16, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_SE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(15, 1, 15, 16, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_SW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0, 1, 15, 1, 15, 16), Direction.SOUTH));
        final VoxelShape[] TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0, 15, 0, 16, 16, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_S = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(7, 1, 15, 9, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_N = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(7, 1, 0, 9, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0.5, 1, 0.5, 15.5, 15, 15.5), Direction.SOUTH));*/
        
        final VoxelShape[] BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0, 0, 0, 16, 1, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_NW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0, 1, 0, 1, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_NE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(15, 1, 0, 16, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_SE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(15, 1, 15, 16, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_SW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0, 1, 15, 1, 15, 16), Direction.SOUTH));
        final VoxelShape[] TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0, 15, 0, 16, 16, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_S = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(7, 1, 15, 9, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_N = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(7, 1, 0, 9, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS_NW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 1, 0.25, 7, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS_NE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(9, 1, 0.25, 15, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS_SE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(9, 1, 15, 15, 15, 15.75), Direction.SOUTH));
        final VoxelShape[] GLASS_SW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(1, 1, 15, 7, 15, 15.75), Direction.SOUTH));
        final VoxelShape[] GLASS_W = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(0.25, 1, 1, 1, 15, 15), Direction.SOUTH));
        final VoxelShape[] GLASS_E = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(15, 1, 1, 15.75, 15, 15), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(7, 1, 1, 9, 1.5, 15), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(7, 14.5, 1, 9, 15, 15), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_NORTH = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(7, 1.5, 1, 9, 14.5, 1.5), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_SOUTH = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(7, 1.5, 14.5, 9, 14.5, 15), Direction.SOUTH));
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.getValue(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            /*shapes.add(BOTTOM[direction.get2DDataValue()]);
            shapes.add(BEAM_NW[direction.get2DDataValue()]);
            shapes.add(BEAM_NE[direction.get2DDataValue()]);
            shapes.add(BEAM_SE[direction.get2DDataValue()]);
            shapes.add(BEAM_SW[direction.get2DDataValue()]);
            shapes.add(TOP[direction.get2DDataValue()]);
            shapes.add(BEAM_S[direction.get2DDataValue()]);
            shapes.add(BEAM_N[direction.get2DDataValue()]);
            shapes.add(GLASS[direction.get2DDataValue()]);*/
            
            shapes.add(BOTTOM[direction.get2DDataValue()]);
            shapes.add(BEAM_NW[direction.get2DDataValue()]);
            shapes.add(BEAM_NE[direction.get2DDataValue()]);
            shapes.add(BEAM_SE[direction.get2DDataValue()]);
            shapes.add(BEAM_SW[direction.get2DDataValue()]);
            shapes.add(TOP[direction.get2DDataValue()]);
            shapes.add(BEAM_S[direction.get2DDataValue()]);
            shapes.add(BEAM_N[direction.get2DDataValue()]);
            shapes.add(GLASS_NW[direction.get2DDataValue()]);
            shapes.add(GLASS_NE[direction.get2DDataValue()]);
            shapes.add(GLASS_SE[direction.get2DDataValue()]);
            shapes.add(GLASS_SW[direction.get2DDataValue()]);
            shapes.add(GLASS_W[direction.get2DDataValue()]);
            shapes.add(GLASS_E[direction.get2DDataValue()]);
            shapes.add(BEAM_INNER_BOTTOM[direction.get2DDataValue()]);
            shapes.add(BEAM_INNER_TOP[direction.get2DDataValue()]);
            shapes.add(BEAM_INNER_NORTH[direction.get2DDataValue()]);
            shapes.add(BEAM_INNER_SOUTH[direction.get2DDataValue()]);
            
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
        return createTickerHelper(blockEntity, ModBlockEntities.WATER_FILTER.get(), WaterFilterBlockEntity::tick);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if(!level.isClientSide)
        {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(!FluidUtil.interactWithFluidHandler(player, hand, level, pos, result.getDirection()))
            {
                if(blockEntity instanceof MenuProvider)
                {
                    NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) blockEntity, pos);
                }
            }
            BlockEntityUtil.sendUpdatePacket(blockEntity, (ServerPlayer) player);
            
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new WaterFilterBlockEntity(pos, state);
    }
}
