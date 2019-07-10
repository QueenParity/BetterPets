package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FluidPumpBlock extends DirectionalBlock
{
    public static final BooleanProperty[] CONNECTED_PIPES;
    
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    static
    {
        CONNECTED_PIPES = new BooleanProperty[Direction.values().length];
        for(Direction direction : Direction.values())
        {
            CONNECTED_PIPES[direction.getIndex()] = BooleanProperty.create("pipe_" + direction.getName());
        }
    }
    
    public FluidPumpBlock(Properties properties)
    {
        super(properties);
        BlockState defaultState = this.getStateContainer().getBaseState().with(FACING, Direction.NORTH);
        for(Direction direction : Direction.values())
        {
            defaultState = defaultState.with(CONNECTED_PIPES[direction.getIndex()], false);
        }
        this.setDefaultState(defaultState);
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape[] CUBE1 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(4.5, 10.5, 12, 11.5, 11.5, 13), Direction.SOUTH));
        final VoxelShape[] CUBE2 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(4.5, 4.5, 12, 11.5, 5.5, 13), Direction.SOUTH));
        final VoxelShape[] CUBE3 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(4.5, 5.5, 12, 11.5, 10.5, 13), Direction.SOUTH));
        final VoxelShape[] CUBE4 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(3, 3, 13, 13, 13, 16), Direction.SOUTH));
        final VoxelShape[] CUBE5 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.6, 5.6, 10.5, 10.4, 10.4, 12), Direction.SOUTH));
        final VoxelShape[] CUBE6 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.5, 9.5, 10.5, 6.5, 10.5, 12.5), Direction.SOUTH));
        final VoxelShape[] CUBE7 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.5, 9.5, 10.5, 10.5, 10.5, 12.5), Direction.SOUTH));
        final VoxelShape[] CUBE8 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.5, 5.5, 10.5, 10.5, 6.5, 12.5), Direction.SOUTH));
        final VoxelShape[] CUBE9 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.5, 5.5, 10.5, 6.5, 6.5, 12.5), Direction.SOUTH));
        final VoxelShape[] WEST_DOWN_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.5, 5.5, 6.5, 6.5, 6.5, 9.5), Direction.SOUTH));
        final VoxelShape[] EAST_DOWN_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.5, 5.5, 6.5, 10.5, 6.5, 9.5), Direction.SOUTH));
        final VoxelShape[] EAST_UP_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.5, 9.5, 6.5, 10.5, 10.5, 9.5), Direction.SOUTH));
        final VoxelShape[] WEST_UP_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.5, 9.5, 6.5, 6.5, 10.5, 9.5), Direction.SOUTH));
        final VoxelShape[] NORTH_WEST_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.5, 5.5, 5.5, 6.5, 10.5, 6.5), Direction.SOUTH));
        final VoxelShape[] SOUTH_WEST_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.5, 5.5, 9.5, 6.5, 10.5, 10.5), Direction.SOUTH));
        final VoxelShape[] SOUTH_EAST_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.5, 5.5, 9.5, 10.5, 10.5, 10.5), Direction.SOUTH));
        final VoxelShape[] NORTH_EAST_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.5, 5.5, 5.5, 10.5, 10.5, 6.5), Direction.SOUTH));
        final VoxelShape[] NORTH_DOWN_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 5.5, 5.5, 9.5, 6.5, 6.5), Direction.SOUTH));
        final VoxelShape[] NORTH_UP_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 9.5, 5.5, 9.5, 10.5, 6.5), Direction.SOUTH));
        final VoxelShape[] SOUTH_UP_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 9.5, 9.5, 9.5, 10.5, 10.5), Direction.SOUTH));
        final VoxelShape[] SOUTH_DOWN_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 5.5, 9.5, 9.5, 6.5, 10.5), Direction.SOUTH));
        final VoxelShape[] WEST_SIDE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.6, 6.5, 6.5, 6.6, 9.5, 9.5), Direction.SOUTH));
        final VoxelShape[] NORTH_SIDE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 6.5, 5.6, 9.5, 9.5, 6.6), Direction.SOUTH));
        final VoxelShape[] EAST_SIDE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.4, 6.5, 6.5, 10.4, 9.5, 9.5), Direction.SOUTH));
        final VoxelShape[] UP_SIDE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 9.4, 6.5, 9.5, 10.4, 9.5), Direction.SOUTH));
        final VoxelShape[] DOWN_SIDE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 5.6, 6.5, 9.5, 6.6, 9.5), Direction.SOUTH));
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.get(FACING);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(CUBE1[direction.getIndex()]);
            shapes.add(CUBE2[direction.getIndex()]);
            shapes.add(CUBE3[direction.getIndex()]);
            shapes.add(CUBE4[direction.getIndex()]);
            shapes.add(CUBE5[direction.getIndex()]);
            shapes.add(CUBE6[direction.getIndex()]);
            shapes.add(CUBE7[direction.getIndex()]);
            shapes.add(CUBE8[direction.getIndex()]);
            shapes.add(CUBE9[direction.getIndex()]);
            shapes.add(WEST_DOWN_EDGE[direction.getIndex()]);
            shapes.add(EAST_DOWN_EDGE[direction.getIndex()]);
            shapes.add(EAST_UP_EDGE[direction.getIndex()]);
            shapes.add(WEST_UP_EDGE[direction.getIndex()]);
            shapes.add(NORTH_WEST_EDGE[direction.getIndex()]);
            shapes.add(SOUTH_WEST_EDGE[direction.getIndex()]);
            shapes.add(SOUTH_EAST_EDGE[direction.getIndex()]);
            shapes.add(NORTH_EAST_EDGE[direction.getIndex()]);
            shapes.add(NORTH_DOWN_EDGE[direction.getIndex()]);
            shapes.add(NORTH_UP_EDGE[direction.getIndex()]);
            shapes.add(SOUTH_UP_EDGE[direction.getIndex()]);
            shapes.add(SOUTH_DOWN_EDGE[direction.getIndex()]);
            shapes.add(WEST_SIDE[direction.getIndex()]);
            shapes.add(NORTH_SIDE[direction.getIndex()]);
            shapes.add(EAST_SIDE[direction.getIndex()]);
            shapes.add(UP_SIDE[direction.getIndex()]);
            shapes.add(DOWN_SIDE[direction.getIndex()]);
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState newState, IWorld world, BlockPos pos, BlockPos newPos)
    {
        Direction originalDirection = state.get(FACING).getOpposite();
        for(Direction direction : Direction.values())
        {
            if(direction != originalDirection)
            {
                BlockPos adjacentPos = pos.offset(direction);
                BlockState adjacentState = world.getBlockState(adjacentPos);
                if(adjacentState.getBlock() instanceof FluidPipeBlock)
                {
                    state = state.with(CONNECTED_PIPES[direction.getIndex()], true);
                }
                else if(adjacentState.getBlock() instanceof LeverBlock)
                {
                    AttachFace leverFace = adjacentState.get(LeverBlock.FACE);
                    Direction leverDirection = adjacentState.get(LeverBlock.HORIZONTAL_FACING).getOpposite();
                    if(leverFace == AttachFace.CEILING)
                    {
                        if(adjacentPos.offset(Direction.DOWN).equals(pos))
                        {
                            state = state.with(CONNECTED_PIPES[Direction.DOWN.getIndex()], true);
                        }
                    }
                    else if(leverFace == AttachFace.FLOOR)
                    {
                        if(adjacentPos.offset(Direction.UP).equals(pos))
                        {
                            state = state.with(CONNECTED_PIPES[Direction.UP.getIndex()], true);
                        }
                    }
                    else
                    {
                        if(adjacentPos.offset(leverDirection).equals(pos))
                        {
                            state = state.with(CONNECTED_PIPES[direction.getIndex()], true);
                        }
                    }
                }
            }
        }
        return state;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(FACING);
        builder.add(CONNECTED_PIPES);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }
    
    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ModTileEntities.FLUID_PUMP.create();
    }
}
