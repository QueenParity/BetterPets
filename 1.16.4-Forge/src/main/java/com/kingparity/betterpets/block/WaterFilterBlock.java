package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.TileEntityUtil;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;

public class WaterFilterBlock extends RotatedBlockObject
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public WaterFilterBlock(AbstractBlock.Properties properties)
    {
        super(properties);
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        /*final VoxelShape[] BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 0, 0, 16, 1, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_NW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 1, 0, 1, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_NE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(15, 1, 0, 16, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_SE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(15, 1, 15, 16, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_SW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 1, 15, 1, 15, 16), Direction.SOUTH));
        final VoxelShape[] TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 15, 0, 16, 16, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_S = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(7, 1, 15, 9, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_N = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(7, 1, 0, 9, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0.5, 1, 0.5, 15.5, 15, 15.5), Direction.SOUTH));*/
        
        final VoxelShape[] BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 0, 0, 16, 1, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_NW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 1, 0, 1, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_NE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(15, 1, 0, 16, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_SE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(15, 1, 15, 16, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_SW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 1, 15, 1, 15, 16), Direction.SOUTH));
        final VoxelShape[] TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 15, 0, 16, 16, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_S = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(7, 1, 15, 9, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_N = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(7, 1, 0, 9, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS_NW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 1, 0.25, 7, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS_NE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9, 1, 0.25, 15, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS_SE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(9, 1, 15, 15, 15, 15.75), Direction.SOUTH));
        final VoxelShape[] GLASS_SW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 1, 15, 7, 15, 15.75), Direction.SOUTH));
        final VoxelShape[] GLASS_W = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0.25, 1, 1, 1, 15, 15), Direction.SOUTH));
        final VoxelShape[] GLASS_E = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(15, 1, 1, 15.75, 15, 15), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(7, 1, 1, 9, 1.5, 15), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(7, 14.5, 1, 9, 15, 15), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_NORTH = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(7, 1.5, 1, 9, 14.5, 1.5), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_SOUTH = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(7, 1.5, 14.5, 9, 14.5, 15), Direction.SOUTH));
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.get(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            /*shapes.add(BOTTOM[direction.getHorizontalIndex()]);
            shapes.add(BEAM_NW[direction.getHorizontalIndex()]);
            shapes.add(BEAM_NE[direction.getHorizontalIndex()]);
            shapes.add(BEAM_SE[direction.getHorizontalIndex()]);
            shapes.add(BEAM_SW[direction.getHorizontalIndex()]);
            shapes.add(TOP[direction.getHorizontalIndex()]);
            shapes.add(BEAM_S[direction.getHorizontalIndex()]);
            shapes.add(BEAM_N[direction.getHorizontalIndex()]);
            shapes.add(GLASS[direction.getHorizontalIndex()]);*/
            
            shapes.add(BOTTOM[direction.getHorizontalIndex()]);
            shapes.add(BEAM_NW[direction.getHorizontalIndex()]);
            shapes.add(BEAM_NE[direction.getHorizontalIndex()]);
            shapes.add(BEAM_SE[direction.getHorizontalIndex()]);
            shapes.add(BEAM_SW[direction.getHorizontalIndex()]);
            shapes.add(TOP[direction.getHorizontalIndex()]);
            shapes.add(BEAM_S[direction.getHorizontalIndex()]);
            shapes.add(BEAM_N[direction.getHorizontalIndex()]);
            shapes.add(GLASS_NW[direction.getHorizontalIndex()]);
            shapes.add(GLASS_NE[direction.getHorizontalIndex()]);
            shapes.add(GLASS_SE[direction.getHorizontalIndex()]);
            shapes.add(GLASS_SW[direction.getHorizontalIndex()]);
            shapes.add(GLASS_W[direction.getHorizontalIndex()]);
            shapes.add(GLASS_E[direction.getHorizontalIndex()]);
            shapes.add(BEAM_INNER_BOTTOM[direction.getHorizontalIndex()]);
            shapes.add(BEAM_INNER_TOP[direction.getHorizontalIndex()]);
            shapes.add(BEAM_INNER_NORTH[direction.getHorizontalIndex()]);
            shapes.add(BEAM_INNER_SOUTH[direction.getHorizontalIndex()]);
            
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
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result)
    {
        if(!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(!FluidUtil.interactWithFluidHandler(player, hand, world, pos, result.getFace()))
            {
                if(tileEntity instanceof INamedContainerProvider)
                {
                    NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, pos);
                }
            }
            TileEntityUtil.sendUpdatePacket(tileEntity, (ServerPlayerEntity) player);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }
    
    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(state.getBlock() != newState.getBlock())
        {
            TileEntity tileentity = world.getTileEntity(pos);
            if(tileentity instanceof IInventory)
            {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileentity);
                world.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }
    
    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new WaterFilterTileEntity();
    }
}