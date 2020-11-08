package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.TileEntityUtil;
import com.kingparity.betterpets.util.VoxelShapeHelper;
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

public class WaterFilterBlock extends PetHorizontalBlock
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public WaterFilterBlock(Properties properties)
    {
        super(properties);
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape[] NORTH_WEST_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 0, 1, 2, 16, 2), Direction.SOUTH));
        final VoxelShape[] NORTH_EAST_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 0, 1, 15, 16, 2), Direction.SOUTH));
        final VoxelShape[] SOUTH_EAST_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 0, 14, 15, 16, 15), Direction.SOUTH));
        final VoxelShape[] SOUTH_WEST_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 0, 14, 2, 16, 15), Direction.SOUTH));
        final VoxelShape[] WEST_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 0, 2, 2, 1, 14), Direction.SOUTH));
        final VoxelShape[] EAST_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 0, 2, 15, 1, 14), Direction.SOUTH));
        final VoxelShape[] SOUTH_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 0, 14, 14, 1, 15), Direction.SOUTH));
        final VoxelShape[] NORTH_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 0, 1, 14, 1, 2), Direction.SOUTH));
        final VoxelShape[] WEST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 15, 2, 2, 16, 14), Direction.SOUTH));
        final VoxelShape[] EAST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 15, 2, 15, 16, 14), Direction.SOUTH));
        final VoxelShape[] SOUTH_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 15, 14, 14, 16, 15), Direction.SOUTH));
        final VoxelShape[] NORTH_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 15, 1, 14, 16, 2), Direction.SOUTH));
        final VoxelShape[] WEST_FACE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1.3, 1, 2, 1.7, 15, 14), Direction.SOUTH));
        final VoxelShape[] EAST_FACE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14.3, 1, 2, 14.7, 15, 14), Direction.SOUTH));
        final VoxelShape[] NORTH_FACE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 1, 1.3, 14, 15, 1.7), Direction.SOUTH));
        final VoxelShape[] SOUTH_FACE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 1, 14.3, 14, 15, 14.7), Direction.SOUTH));
        final VoxelShape[] UP_FACE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 15.3, 2, 14, 15.7, 14), Direction.SOUTH));
        final VoxelShape[] DOWN_FACE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 0.3, 2, 14, 0.7, 14), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.get(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(NORTH_WEST_BEAM[direction.getIndex()]);
            shapes.add(NORTH_EAST_BEAM[direction.getIndex()]);
            shapes.add(SOUTH_EAST_BEAM[direction.getIndex()]);
            shapes.add(SOUTH_WEST_BEAM[direction.getIndex()]);
            shapes.add(WEST_DOWN_BEAM[direction.getIndex()]);
            shapes.add(EAST_DOWN_BEAM[direction.getIndex()]);
            shapes.add(SOUTH_DOWN_BEAM[direction.getIndex()]);
            shapes.add(NORTH_DOWN_BEAM[direction.getIndex()]);
            shapes.add(WEST_UP_BEAM[direction.getIndex()]);
            shapes.add(EAST_UP_BEAM[direction.getIndex()]);
            shapes.add(SOUTH_UP_BEAM[direction.getIndex()]);
            shapes.add(NORTH_UP_BEAM[direction.getIndex()]);
            shapes.add(WEST_FACE[direction.getIndex()]);
            shapes.add(EAST_FACE[direction.getIndex()]);
            shapes.add(NORTH_FACE[direction.getIndex()]);
            shapes.add(SOUTH_FACE[direction.getIndex()]);
            shapes.add(UP_FACE[direction.getIndex()]);
            shapes.add(DOWN_FACE[direction.getIndex()]);

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
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result)
    {
        if(!world.isRemote)
        {
            if(!FluidUtil.interactWithFluidHandler(playerEntity, hand, world, pos, result.getFace()))
            {
                TileEntity tileEntity = world.getTileEntity(pos);
                if(tileEntity instanceof INamedContainerProvider)
                {
                    TileEntityUtil.sendUpdatePacket(tileEntity, (ServerPlayerEntity) playerEntity);
                    NetworkHooks.openGui((ServerPlayerEntity) playerEntity, (INamedContainerProvider) tileEntity, pos);
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(state.getBlock() != newState.getBlock())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if(tileentity instanceof IInventory)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
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