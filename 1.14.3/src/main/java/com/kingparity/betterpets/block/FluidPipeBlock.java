package com.kingparity.betterpets.block;

import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.tileentity.FluidPipeTileEntity;
import com.kingparity.betterpets.tileentity.FluidTankTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FluidPipeBlock extends PetWaterloggedBlock
{
    public static final BooleanProperty north = BooleanProperty.create("north");
    public static final BooleanProperty east = BooleanProperty.create("east");
    public static final BooleanProperty south = BooleanProperty.create("south");
    public static final BooleanProperty west = BooleanProperty.create("west");
    public static final BooleanProperty up = BooleanProperty.create("up");
    public static final BooleanProperty down = BooleanProperty.create("down");
    
    public FluidPipeBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(north, false).with(east, false).with(south, false).with(west, false).with(up, false).with(down, false));
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState newState, IWorld world, BlockPos pos, BlockPos newPos)
    {
        FluidPipeTileEntity te = (FluidPipeTileEntity)world.getTileEntity(pos);
        boolean u = false;
        boolean d = false;
        boolean n = false;
        boolean s = false;
        boolean w = false;
        boolean e = false;
        for(Direction face : FluidPipeTileEntity.faces)
        {
            BlockPos fp = pos.offset(face);
            TileEntity te2 = world.getTileEntity(fp);
            if(te2 != null)
            {
                if(te2 instanceof FluidTankTileEntity)
                {
                    switch(face)
                    {
                        case DOWN:
                            d = true;
                            break;
                        case UP:
                            u = true;
                            break;
                        case NORTH:
                            n = true;
                            break;
                        case SOUTH:
                            s = true;
                            break;
                        case WEST:
                            w = true;
                            break;
                        case EAST:
                            e = true;
                            break;
                    }
                }
            }
        }
        te.setUp(u);
        te.setDown(d);
        te.setNorth(n);
        te.setSouth(s);
        te.setWest(w);
        te.setEast(e);
        return state.with(up, te.isUp()).with(down, te.isDown()).with(north, te.isNorth()).with(south, te.isSouth()).with(west, te.isWest()).with(east, te.isEast());
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        FluidPipeTileEntity te = (FluidPipeTileEntity)world.getTileEntity(pos);
        boolean u = false;
        boolean d = false;
        boolean n = false;
        boolean s = false;
        boolean w = false;
        boolean e = false;
        for(Direction face : FluidPipeTileEntity.faces)
        {
            BlockPos fp = pos.offset(face);
            TileEntity te2 = world.getTileEntity(fp);
            if(te2 != null)
            {
                if(te2 instanceof FluidTankTileEntity)
                {
                    switch(face)
                    {
                        case DOWN:
                            d = true;
                            break;
                        case UP:
                            u = true;
                            break;
                        case NORTH:
                            n = true;
                            break;
                        case SOUTH:
                            s = true;
                            break;
                        case WEST:
                            w = true;
                            break;
                        case EAST:
                            e = true;
                            break;
                    }
                }
            }
        }
        return super.getStateForPlacement(context).with(up, u).with(down, d).with(north, n).with(south, s).with(west, w).with(east, e);
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack stack)
    {
        FluidPipeTileEntity cable = (FluidPipeTileEntity)world.getTileEntity(pos);
        boolean u = false;
        boolean d = false;
        boolean n = false;
        boolean s = false;
        boolean w = false;
        boolean e = false;
        for(Direction face : FluidPipeTileEntity.faces)
        {
            BlockPos fp = pos.offset(face);
            TileEntity cable2 = world.getTileEntity(fp);
            if(cable2 != null)
            {
                if(cable2 instanceof FluidTankTileEntity)
                {
                    switch(face)
                    {
                        case DOWN:
                            d = true;
                            break;
                        case UP:
                            u = true;
                            break;
                        case NORTH:
                            n = true;
                            break;
                        case SOUTH:
                            s = true;
                            break;
                        case WEST:
                            w = true;
                            break;
                        case EAST:
                            e = true;
                            break;
                    }
                }
            }
        }
        cable.setUp(u);
        cable.setDown(d);
        cable.setNorth(n);
        cable.setSouth(s);
        cable.setWest(w);
        cable.setEast(e);
        world.notifyBlockUpdate(pos, state, state.with(up, cable.isUp()).with(down, cable.isDown()).with(north, cable.isNorth()).with(south, cable.isSouth()).with(west, cable.isWest()).with(east, cable.isEast()), 2);
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(north);
        builder.add(east);
        builder.add(south);
        builder.add(west);
        builder.add(up);
        builder.add(down);
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
