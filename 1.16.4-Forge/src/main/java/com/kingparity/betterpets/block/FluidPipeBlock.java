package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.tileentity.FluidPipeTileEntity;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class FluidPipeBlock extends Block
{
    public static final DirectionProperty DIRECTION = DirectionProperty.create("facing", Direction.values());
    public static final BooleanProperty[] CONNECTED_PIPES = Util.make(() ->
    {
        BooleanProperty[] directions = new BooleanProperty[Direction.values().length];
        for(Direction facing : Direction.values())
        {
            directions[facing.getIndex()] = BooleanProperty.create("pipe_" + facing.getName2());
        }
        return directions;
    });
    
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public FluidPipeBlock(AbstractBlock.Properties properties)
    {
        super(properties);
        BlockState defaultState = this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH);
        for(Direction facing : Direction.values())
        {
            defaultState = defaultState.with(CONNECTED_PIPES[facing.getIndex()], false);
        }
        this.setDefaultState(defaultState);
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape NODE = Block.makeCuboidShape(5, 5, 5, 11, 11, 11);
        final VoxelShape PIPE_UP = Block.makeCuboidShape(6, 11, 6, 10, 16, 10);
        final VoxelShape PIPE_DOWN = Block.makeCuboidShape(6, 0, 6, 10, 5, 10);
        final VoxelShape PIPE_HORIZONTAL = Block.makeCuboidShape(11, 6, 6, 16, 10, 10);
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            boolean up = state.get(CONNECTED_PIPES[Direction.UP.getIndex()]);
            boolean down = state.get(CONNECTED_PIPES[Direction.DOWN.getIndex()]);
            boolean north = state.get(CONNECTED_PIPES[Direction.NORTH.getIndex()]);
            boolean east = state.get(CONNECTED_PIPES[Direction.EAST.getIndex()]);
            boolean south = state.get(CONNECTED_PIPES[Direction.SOUTH.getIndex()]);
            boolean west = state.get(CONNECTED_PIPES[Direction.WEST.getIndex()]);
            
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
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos)
    {
        return this.getPipeState(state, world, pos);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState state = super.getStateForPlacement(context).with(DIRECTION, context.getFace().getOpposite());
        return this.getPipeState(state, context.getWorld(), context.getPos());
    }
    
    protected BlockState getPipeState(BlockState state, IWorld world, BlockPos pos)
    {
        for(Direction facing : Direction.values())
        {
            state = state.with(CONNECTED_PIPES[facing.getIndex()], false);
            
            BlockPos adjacentPos = pos.offset(facing);
            BlockState adjacentState = world.getBlockState(adjacentPos);
            if(adjacentState.hasTileEntity())
            {
                IFluidHandler fluidHandler = world.getTileEntity(adjacentPos).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);
                if(fluidHandler != null)
                {
                    state = state.with(CONNECTED_PIPES[facing.getIndex()], true);
                }
            }
        }
        return state;
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
        builder.add(CONNECTED_PIPES);
    }
    
    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new FluidPipeTileEntity();
    }
}