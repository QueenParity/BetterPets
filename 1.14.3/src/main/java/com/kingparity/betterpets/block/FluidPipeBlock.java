package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.tileentity.FluidHolderTileEntity;
import com.kingparity.betterpets.tileentity.FluidPipeTileEntity;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public FluidPipeBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false).with(DOWN, false));
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape WEST_DOWN_EDGE = Block.makeCuboidShape(5.5, 5.5, 6.5, 6.5, 6.5, 9.5);
        final VoxelShape EAST_DOWN_EDGE = Block.makeCuboidShape(9.5, 5.5, 6.5, 10.5, 6.5, 9.5);
        final VoxelShape EAST_UP_EDGE = Block.makeCuboidShape(9.5, 9.5, 6.5, 10.5, 10.5, 9.5);
        final VoxelShape WEST_UP_EDGE = Block.makeCuboidShape(5.5, 9.5, 6.5, 6.5, 10.5, 9.5);
        final VoxelShape NORTH_WEST_EDGE = Block.makeCuboidShape(5.5, 5.5, 5.5, 6.5, 10.5, 6.5);
        final VoxelShape SOUTH_WEST_EDGE = Block.makeCuboidShape(5.5, 5.5, 9.5, 6.5, 10.5, 10.5);
        final VoxelShape SOUTH_EAST_EDGE = Block.makeCuboidShape(9.5, 5.5, 9.5, 10.5, 10.5, 10.5);
        final VoxelShape NORTH_EAST_EDGE = Block.makeCuboidShape(9.5, 5.5, 5.5, 10.5, 10.5, 6.5);
        final VoxelShape NORTH_DOWN_EDGE = Block.makeCuboidShape(6.5, 5.5, 5.5, 9.5, 6.5, 6.5);
        final VoxelShape NORTH_UP_EDGE = Block.makeCuboidShape(6.5, 9.5, 5.5, 9.5, 10.5, 6.5);
        final VoxelShape SOUTH_UP_EDGE = Block.makeCuboidShape(6.5, 9.5, 9.5, 9.5, 10.5, 10.5);
        final VoxelShape SOUTH_DOWN_EDGE = Block.makeCuboidShape(6.5, 5.5, 9.5, 9.5, 6.5, 10.5);
        final VoxelShape WEST_SIDE = Block.makeCuboidShape(5.6, 6.5, 6.5, 6.6, 9.5, 9.5);
        final VoxelShape SOUTH_SIDE = Block.makeCuboidShape(6.5, 6.5, 9.4, 9.5, 9.5, 10.4);
        final VoxelShape NORTH_SIDE = Block.makeCuboidShape(6.5, 6.5, 5.6, 9.5, 9.5, 6.6);
        final VoxelShape EAST_SIDE = Block.makeCuboidShape(9.4, 6.5, 6.5, 10.4, 9.5, 9.5);
        final VoxelShape UP_SIDE = Block.makeCuboidShape(6.5, 9.4, 6.5, 9.5, 10.4, 9.5);
        final VoxelShape DOWN_SIDE = Block.makeCuboidShape(6.5, 5.6, 6.5, 9.5, 6.6, 9.5);
    
        final VoxelShape[] CONNECTION_SOUTH_WEST_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.5, 5.5, 5.5, 6.5, 10.5, 6.5), Direction.SOUTH));
        final VoxelShape[] CONNECTION_SOUTH_EAST_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.5, 5.5, 5.5, 10.5, 10.5, 6.5), Direction.SOUTH));
        final VoxelShape[] CONNECTION_SOUTH_DOWN_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 5.5, 5.5, 9.5, 6.5, 6.5), Direction.SOUTH));
        final VoxelShape[] CONNECTION_SOUTH_UP_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 9.5, 5.5, 9.5, 10.5, 6.5), Direction.SOUTH));
        final VoxelShape[] CONNECTION_NORTH_DOWN_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 5.5, 0, 9.5, 6.5, 1), Direction.SOUTH));
        final VoxelShape[] CONNECTION_NORTH_UP_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 9.5, 0, 9.5, 10.5, 1), Direction.SOUTH));
        final VoxelShape[] CONNECTION_WEST_DOWN_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.5, 5.5, 1, 6.5, 6.5, 5.5), Direction.SOUTH));
        final VoxelShape[] CONNECTION_WEST_UP_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.5, 9.5, 1, 6.5, 10.5, 5.5), Direction.SOUTH));
        final VoxelShape[] CONNECTION_EAST_UP_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.5, 9.5, 1, 10.5, 10.5, 5.5), Direction.SOUTH));
        final VoxelShape[] CONNECTION_EAST_DOWN_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.5, 5.5, 1, 10.5, 6.5, 5.5), Direction.SOUTH));
        final VoxelShape[] CONNECTION_NORTH_WEST_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.5, 5.5, 0, 6.5, 10.5, 1), Direction.SOUTH));
        final VoxelShape[] CONNECTION_NORTH_EAST_EDGE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.5, 5.5, 0, 10.5, 10.5, 1), Direction.SOUTH));
        final VoxelShape[] CONNECTION_UP_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 9.4, 1, 9.5, 10.4, 5.5), Direction.SOUTH));
        final VoxelShape[] CONNECTION_DOWN_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6.5, 5.6, 1, 9.5, 6.6, 5.5), Direction.SOUTH));
        final VoxelShape[] CONNECTION_WEST_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5.6, 6.5, 1, 6.6, 9.5, 5.5), Direction.SOUTH));
        final VoxelShape[] CONNECTION_EAST_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9.4, 6.5, 1, 10.4, 9.5, 5.5), Direction.SOUTH));
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            boolean north = state.get(NORTH);
            boolean east = state.get(EAST);
            boolean south = state.get(SOUTH);
            boolean west = state.get(WEST);
            boolean up = state.get(UP);
            boolean down = state.get(DOWN);
            
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(WEST_DOWN_EDGE);
            shapes.add(EAST_DOWN_EDGE);
            shapes.add(EAST_UP_EDGE);
            shapes.add(WEST_UP_EDGE);
            shapes.add(NORTH_WEST_EDGE);
            shapes.add(SOUTH_WEST_EDGE);
            shapes.add(SOUTH_EAST_EDGE);
            shapes.add(NORTH_EAST_EDGE);
            shapes.add(NORTH_DOWN_EDGE);
            shapes.add(NORTH_UP_EDGE);
            shapes.add(SOUTH_UP_EDGE);
            shapes.add(SOUTH_DOWN_EDGE);
            
            if(!north)
            {
                shapes.add(NORTH_SIDE);
            }
            else
            {
                shapes.add(CONNECTION_SOUTH_WEST_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_SOUTH_EAST_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_SOUTH_DOWN_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_SOUTH_UP_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_NORTH_DOWN_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_NORTH_UP_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_WEST_DOWN_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_WEST_UP_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_EAST_UP_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_EAST_DOWN_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_NORTH_WEST_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_NORTH_EAST_EDGE[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_UP_GLASS[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_DOWN_GLASS[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_WEST_GLASS[Direction.NORTH.getIndex()]);
                shapes.add(CONNECTION_EAST_GLASS[Direction.NORTH.getIndex()]);
            }
            
            if(!east)
            {
                shapes.add(EAST_SIDE);
            }
            else
            {
                shapes.add(CONNECTION_SOUTH_WEST_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_SOUTH_EAST_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_SOUTH_DOWN_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_SOUTH_UP_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_NORTH_DOWN_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_NORTH_UP_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_WEST_DOWN_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_WEST_UP_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_EAST_UP_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_EAST_DOWN_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_NORTH_WEST_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_NORTH_EAST_EDGE[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_UP_GLASS[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_DOWN_GLASS[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_WEST_GLASS[Direction.EAST.getIndex()]);
                shapes.add(CONNECTION_EAST_GLASS[Direction.EAST.getIndex()]);
            }
            
            if(!south)
            {
                shapes.add(SOUTH_SIDE);
            }
            else
            {
                shapes.add(CONNECTION_SOUTH_WEST_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_SOUTH_EAST_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_SOUTH_DOWN_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_SOUTH_UP_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_NORTH_DOWN_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_NORTH_UP_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_WEST_DOWN_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_WEST_UP_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_EAST_UP_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_EAST_DOWN_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_NORTH_WEST_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_NORTH_EAST_EDGE[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_UP_GLASS[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_DOWN_GLASS[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_WEST_GLASS[Direction.SOUTH.getIndex()]);
                shapes.add(CONNECTION_EAST_GLASS[Direction.SOUTH.getIndex()]);
            }
            
            if(!west)
            {
                shapes.add(WEST_SIDE);
            }
            else
            {
                shapes.add(CONNECTION_SOUTH_WEST_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_SOUTH_EAST_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_SOUTH_DOWN_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_SOUTH_UP_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_NORTH_DOWN_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_NORTH_UP_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_WEST_DOWN_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_WEST_UP_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_EAST_UP_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_EAST_DOWN_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_NORTH_WEST_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_NORTH_EAST_EDGE[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_UP_GLASS[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_DOWN_GLASS[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_WEST_GLASS[Direction.WEST.getIndex()]);
                shapes.add(CONNECTION_EAST_GLASS[Direction.WEST.getIndex()]);
            }
            
            if(!up)
            {
                shapes.add(UP_SIDE);
            }
            else
            {
                shapes.add(CONNECTION_SOUTH_WEST_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_SOUTH_EAST_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_SOUTH_DOWN_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_SOUTH_UP_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_NORTH_DOWN_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_NORTH_UP_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_WEST_DOWN_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_WEST_UP_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_EAST_UP_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_EAST_DOWN_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_NORTH_WEST_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_NORTH_EAST_EDGE[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_UP_GLASS[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_DOWN_GLASS[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_WEST_GLASS[Direction.UP.getIndex()]);
                shapes.add(CONNECTION_EAST_GLASS[Direction.UP.getIndex()]);
            }
            
            if(!down)
            {
                shapes.add(DOWN_SIDE);
            }
            else
            {
                shapes.add(CONNECTION_SOUTH_WEST_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_SOUTH_EAST_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_SOUTH_DOWN_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_SOUTH_UP_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_NORTH_DOWN_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_NORTH_UP_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_WEST_DOWN_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_WEST_UP_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_EAST_UP_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_EAST_DOWN_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_NORTH_WEST_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_NORTH_EAST_EDGE[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_UP_GLASS[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_DOWN_GLASS[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_WEST_GLASS[Direction.DOWN.getIndex()]);
                shapes.add(CONNECTION_EAST_GLASS[Direction.DOWN.getIndex()]);
            }
            
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
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState newState, IWorld world, BlockPos pos, BlockPos newPos)
    {
        boolean north = false;
        boolean east = false;
        boolean south = false;
        boolean west = false;
        boolean up = false;
        boolean down = false;
        for(Direction face : Direction.values())
        {
            BlockPos adjacentPos = pos.offset(face);
            TileEntity adjacentTileEntity = world.getTileEntity(adjacentPos);
            if(adjacentTileEntity != null)
            {
                if(adjacentTileEntity instanceof FluidHolderTileEntity)
                {
                    switch(face)
                    {
                        case NORTH:
                            north = true;
                            break;
                        case EAST:
                            east = true;
                            break;
                        case SOUTH:
                            south = true;
                            break;
                        case WEST:
                            west = true;
                            break;
                        case UP:
                            up = true;
                            break;
                        case DOWN:
                            down = true;
                            break;
                    }
                }
            }
        }
        return state.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west).with(UP, up).with(DOWN, down);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        boolean north = false;
        boolean east = false;
        boolean south = false;
        boolean west = false;
        boolean up = false;
        boolean down = false;
        for(Direction face : Direction.values())
        {
            BlockPos adjacentPos = pos.offset(face);
            TileEntity adjacentTileEntity = world.getTileEntity(adjacentPos);
            if(adjacentTileEntity != null)
            {
                if(adjacentTileEntity instanceof FluidHolderTileEntity)
                {
                    switch(face)
                    {
                        case NORTH:
                            north = true;
                            break;
                        case EAST:
                            east = true;
                            break;
                        case SOUTH:
                            south = true;
                            break;
                        case WEST:
                            west = true;
                            break;
                        case UP:
                            up = true;
                            break;
                        case DOWN:
                            down = true;
                            break;
                    }
                }
            }
        }
        return super.getStateForPlacement(context).with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west).with(UP, up).with(DOWN, down);
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack stack)
    {
        FluidPipeTileEntity pipe = (FluidPipeTileEntity)world.getTileEntity(pos);
        boolean north = false;
        boolean east = false;
        boolean south = false;
        boolean west = false;
        boolean up = false;
        boolean down = false;
        for(Direction face : Direction.values())
        {
            BlockPos adjacentPos = pos.offset(face);
            TileEntity adjacentTileEntity = world.getTileEntity(adjacentPos);
            if(adjacentTileEntity != null)
            {
                if(adjacentTileEntity instanceof FluidHolderTileEntity)
                {
                    switch(face)
                    {
                        case NORTH:
                            north = true;
                            break;
                        case EAST:
                            east = true;
                            break;
                        case SOUTH:
                            south = true;
                            break;
                        case WEST:
                            west = true;
                            break;
                        case UP:
                            up = true;
                            break;
                        case DOWN:
                            down = true;
                            break;
                    }
                }
            }
        }
        world.notifyBlockUpdate(pos, state, state.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west).with(UP, up).with(DOWN, down), 2);
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(NORTH);
        builder.add(EAST);
        builder.add(SOUTH);
        builder.add(WEST);
        builder.add(UP);
        builder.add(DOWN);
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
}
