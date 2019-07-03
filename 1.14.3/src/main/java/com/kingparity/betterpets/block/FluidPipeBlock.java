package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.tileentity.FluidPipeTileEntity;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FluidPipeBlock extends PetWaterloggedBlock
{
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    //public static final BooleanProperty UP = BlockStateProperties.UP;
    //public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public FluidPipeBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false)/*.with(UP, false).with(DOWN, false)*/);
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape FLUID_PIPE_MIDDLE = Block.makeCuboidShape(5.0, 5.0, 5.0, 11.0, 11.0, 11.0);
        final VoxelShape FLUID_PIPE_END = Block.makeCuboidShape(11.0, 5.0, 5.0, 16.0, 11.0, 11.0);
    
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            boolean north = state.get(NORTH);
            boolean east = state.get(EAST);
            boolean south = state.get(SOUTH);
            boolean west = state.get(WEST);
    
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(FLUID_PIPE_MIDDLE);
    
            if(north)
            {
                shapes.add(VoxelShapeHelper.rotate(FLUID_PIPE_END, Direction.NORTH));
            }
            if(east)
            {
                shapes.add(VoxelShapeHelper.rotate(FLUID_PIPE_END, Direction.EAST));
            }
            if(south)
            {
                shapes.add(VoxelShapeHelper.rotate(FLUID_PIPE_END, Direction.SOUTH));
            }
            if(west)
            {
                shapes.add(VoxelShapeHelper.rotate(FLUID_PIPE_END, Direction.WEST));
            }
    
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState newState, IWorld world, BlockPos pos, BlockPos newPos)
    {
        Block northBlock = world.getBlockState(pos.north()).getBlock();
        Block eastBlock = world.getBlockState(pos.east()).getBlock();
        Block southBlock = world.getBlockState(pos.south()).getBlock();
        Block westBlock = world.getBlockState(pos.west()).getBlock();
        boolean north = northBlock == this || northBlock instanceof WaterCollectorBlock || northBlock instanceof WaterFilterBlock;
        boolean east = eastBlock == this || eastBlock instanceof WaterCollectorBlock || eastBlock instanceof WaterFilterBlock;
        boolean south = southBlock == this || southBlock instanceof WaterCollectorBlock || southBlock instanceof WaterFilterBlock;
        boolean west = westBlock == this || westBlock instanceof WaterCollectorBlock || westBlock instanceof WaterFilterBlock;
        //boolean up = world.getBlockState(pos.up()).getBlock() == this;
        //boolean down = world.getBlockState(pos.down()).getBlock() == this;
        return state.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west)/*.with(UP, up).with(DOWN, down)*/;
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Block northBlock = world.getBlockState(pos.north()).getBlock();
        Block eastBlock = world.getBlockState(pos.east()).getBlock();
        Block southBlock = world.getBlockState(pos.south()).getBlock();
        Block westBlock = world.getBlockState(pos.west()).getBlock();
        boolean north = northBlock == this || northBlock instanceof WaterCollectorBlock || northBlock instanceof WaterFilterBlock;
        boolean east = eastBlock == this || eastBlock instanceof WaterCollectorBlock || eastBlock instanceof WaterFilterBlock;
        boolean south = southBlock == this || southBlock instanceof WaterCollectorBlock || southBlock instanceof WaterFilterBlock;
        boolean west = westBlock == this || westBlock instanceof WaterCollectorBlock || westBlock instanceof WaterFilterBlock;
        //boolean up = world.getBlockState(pos.up()).getBlock() == this;
        //boolean down = world.getBlockState(pos.down()).getBlock() == this;
        return super.getStateForPlacement(context).with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west)/*.with(UP, up).with(DOWN, down)*/;
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(NORTH);
        builder.add(EAST);
        builder.add(SOUTH);
        builder.add(WEST);
        //builder.add(UP);
        //builder.add(DOWN);
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
        return ModTileEntities.FLUID_PIPE.create();
    }
    
    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}
