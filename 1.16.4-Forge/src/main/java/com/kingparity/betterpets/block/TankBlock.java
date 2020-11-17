package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.tileentity.TankTileEntity;
import com.kingparity.betterpets.util.TileEntityUtil;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

import java.util.ArrayList;
import java.util.List;

public class TankBlock extends Block
{
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public TankBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(UP, false).with(DOWN, false));
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape TOP = Block.makeCuboidShape(1, 15, 1, 15, 16, 15);
        final VoxelShape BOTTOM = Block.makeCuboidShape(1, 0, 1, 15, 1, 15);
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            boolean up = state.get(UP);
            boolean down = state.get(DOWN);
            
            List<VoxelShape> shapes = new ArrayList<>();
            int inner_start = 1;
            int inner_end = 15;
            if(up)
            {
                inner_end++;
            }
            else
            {
                shapes.add(TOP);
            }
            
            if(down)
            {
                inner_start--;
            }
            else
            {
                shapes.add(BOTTOM);
            }
            
            final VoxelShape GLASS_NORTH = Block.makeCuboidShape(2, inner_start, 1.5, 14, inner_end, 2);
            final VoxelShape GLASS_SOUTH = Block.makeCuboidShape(2, inner_start, 14, 14, inner_end, 14.5);
            final VoxelShape GLASS_WEST = Block.makeCuboidShape(1.5, inner_start, 2, 2, inner_end, 14);
            final VoxelShape GLASS_EAST = Block.makeCuboidShape(14, inner_start, 2, 14.5, inner_end, 14);
            final VoxelShape BEAM_NORTH_WEST = Block.makeCuboidShape(1, inner_start, 1, 2, inner_end, 2);
            final VoxelShape BEAM_SOUTH_WEST = Block.makeCuboidShape(1, inner_start, 14, 2, inner_end, 15);
            final VoxelShape BEAM_SOUTH_EAST = Block.makeCuboidShape(14, inner_start, 14, 15, inner_end, 15);
            final VoxelShape BEAM_NORTH_EAST = Block.makeCuboidShape(14, inner_start, 1, 15, inner_end, 2);
            
            shapes.add(GLASS_NORTH);
            shapes.add(GLASS_SOUTH);
            shapes.add(GLASS_WEST);
            shapes.add(GLASS_EAST);
            shapes.add(BEAM_NORTH_WEST);
            shapes.add(BEAM_SOUTH_WEST);
            shapes.add(BEAM_SOUTH_EAST);
            shapes.add(BEAM_NORTH_EAST);
            
            
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
        boolean up = world.getBlockState(pos.up()).getBlock() == this;
        boolean down = world.getBlockState(pos.down()).getBlock() == this;
        return state.with(UP, up).with(DOWN, down);
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(UP);
        builder.add(DOWN);
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result)
    {
        if(!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            BlockPos tankPos = pos;
            
            while(tileEntity instanceof TankTileEntity)
            {
                tankPos = tankPos.down();
                tileEntity = world.getTileEntity(tankPos);
            }
            
            tankPos = tankPos.up();
            tileEntity = world.getTileEntity(tankPos);
            
            while(tileEntity instanceof TankTileEntity)
            {
                TankTileEntity tank = (TankTileEntity)tileEntity;
                if(tank.getFluidLevel() <= tank.getCapacity() - 1000 || !(world.getTileEntity(tankPos.up()) instanceof TankTileEntity))
                {
                    if(!FluidUtil.interactWithFluidHandler(player, hand, world, tankPos, result.getFace()))
                    {
                        tankPos = tankPos.down();
                        FluidUtil.interactWithFluidHandler(player, hand, world, tankPos, result.getFace());
                    }
                    break;
                }
                else
                {
                    tankPos = tankPos.up();
                    tileEntity = world.getTileEntity(tankPos);
                }
            }
            TileEntityUtil.sendUpdatePacket(world.getTileEntity(pos), (ServerPlayerEntity) player);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }
    
    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TankTileEntity();
    }
}