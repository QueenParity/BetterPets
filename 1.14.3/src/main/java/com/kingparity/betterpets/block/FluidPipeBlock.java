package com.kingparity.betterpets.block;

import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.tileentity.FluidHolderTileEntity;
import com.kingparity.betterpets.tileentity.FluidPipeTileEntity;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FluidPipeBlock extends PetWaterloggedBlock
{
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    
    public FluidPipeBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false).with(DOWN, false));
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
