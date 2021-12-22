package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.blockentity.FluidPipeBlockEntity;
import com.kingparity.betterpets.init.ModBlockEntities;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FluidPipeBlock extends BaseEntityBlock
{
    public static final DirectionProperty DIRECTION = DirectionProperty.create("facing", Direction.values());
    public static final BooleanProperty[] CONNECTED_PIPES = Util.make(() ->
    {
        BooleanProperty[] directions = new BooleanProperty[Direction.values().length];
        for(Direction facing : Direction.values())
        {
            directions[facing.get3DDataValue()] = BooleanProperty.create("pipe_" + facing.getName());
        }
        return directions;
    });
    
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public FluidPipeBlock(Properties properties)
    {
        super(properties);
        BlockState defaultState = this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH);
        for(Direction facing : Direction.values())
        {
            defaultState = defaultState.setValue(CONNECTED_PIPES[facing.get3DDataValue()], false);
        }
        this.registerDefaultState(defaultState);
        SHAPES = this.generateShapes(this.getStateDefinition().getPossibleStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape NODE = Block.box(5, 5, 5, 11, 11, 11);
        final VoxelShape PIPE_UP = Block.box(6, 11, 6, 10, 16, 10);
        final VoxelShape PIPE_DOWN = Block.box(6, 0, 6, 10, 5, 10);
        final VoxelShape PIPE_HORIZONTAL = Block.box(11, 6, 6, 16, 10, 10);
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            boolean up = state.getValue(CONNECTED_PIPES[Direction.UP.get3DDataValue()]);
            boolean down = state.getValue(CONNECTED_PIPES[Direction.DOWN.get3DDataValue()]);
            boolean north = state.getValue(CONNECTED_PIPES[Direction.NORTH.get3DDataValue()]);
            boolean east = state.getValue(CONNECTED_PIPES[Direction.EAST.get3DDataValue()]);
            boolean south = state.getValue(CONNECTED_PIPES[Direction.SOUTH.get3DDataValue()]);
            boolean west = state.getValue(CONNECTED_PIPES[Direction.WEST.get3DDataValue()]);
            
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(NODE);
            if(up)
            {
                shapes.add(PIPE_UP);
            }
            if(down)
            {
                shapes.add(PIPE_DOWN);
            }
            if(north)
            {
                shapes.add(VoxelShapeHelper.rotate(PIPE_HORIZONTAL, Direction.NORTH));
            }
            if(east)
            {
                shapes.add(VoxelShapeHelper.rotate(PIPE_HORIZONTAL, Direction.EAST));
            }
            if(south)
            {
                shapes.add(VoxelShapeHelper.rotate(PIPE_HORIZONTAL, Direction.SOUTH));
            }
            if(west)
            {
                shapes.add(VoxelShapeHelper.rotate(PIPE_HORIZONTAL, Direction.WEST));
            }
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
    
    @Override
    public RenderShape getRenderShape(BlockState p_49232_)
    {
        return RenderShape.MODEL;
    }
    
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos newPos)
    {
        FluidPipeBlockEntity.updateLinks(level, pos, state, level.getBlockEntity(pos, ModBlockEntities.FLUID_PIPE.get()).get());
        return this.getPipeState(state, level, pos);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(DIRECTION);
        builder.add(CONNECTED_PIPES);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = super.getStateForPlacement(context).setValue(DIRECTION, context.getHorizontalDirection().getOpposite());
        return this.getPipeState(state, context.getLevel(), context.getClickedPos());
    }
    
    protected BlockState getPipeState(BlockState state, LevelAccessor level, BlockPos pos)
    {
        for(Direction facing : Direction.values())
        {
            state = state.setValue(CONNECTED_PIPES[facing.get3DDataValue()], false);
            
            BlockPos adjacentPos = pos.relative(facing);
            BlockState adjacentState = level.getBlockState(adjacentPos);
            if(adjacentState.hasBlockEntity())
            {
                IFluidHandler fluidHandler = level.getBlockEntity(adjacentPos).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);
                if(fluidHandler != null)
                {
                    state = state.setValue(CONNECTED_PIPES[facing.get3DDataValue()], true);
                }
            }
        }
        return state;
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity)
    {
        return createTickerHelper(blockEntity, ModBlockEntities.FLUID_PIPE.get(), FluidPipeBlockEntity::tick);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new FluidPipeBlockEntity(pos, state);
    }
}