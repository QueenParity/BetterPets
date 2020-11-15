package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.tileentity.WaterCollectorTileEntity;
import com.kingparity.betterpets.util.TileEntityUtil;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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

import java.util.ArrayList;
import java.util.List;

public class WaterCollectorBlock extends RotatedBlockObject
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public WaterCollectorBlock(AbstractBlock.Properties properties)
    {
        super(properties);
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape[] BACK_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0.5, 0, 0.5, 2.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] BACK_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(13.5, 0, 0.5, 15.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] FRONT_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(13.5, 0, 13.5, 15.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] FRONT_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0.5, 0, 13.5, 2.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] WATER_TANK_STAND = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 5, 0, 16, 7, 16), Direction.SOUTH));
        final VoxelShape[] TANK_3 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 11, 1, 15, 12, 2), Direction.SOUTH));
        final VoxelShape[] TANK_4 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 13, 1, 15, 14, 2), Direction.SOUTH));
        final VoxelShape[] TANK_5 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 15, 1, 15, 16, 2), Direction.SOUTH));
        final VoxelShape[] TANK_6 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 17, 1, 15, 18, 2), Direction.SOUTH));
        final VoxelShape[] TANK_7 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 19, 1, 15, 20, 2), Direction.SOUTH));
        final VoxelShape[] TANK_8 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 21, 1, 15, 22, 2), Direction.SOUTH));
        final VoxelShape[] TANK_10 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 9, 14, 15, 10, 15), Direction.SOUTH));
        final VoxelShape[] TANK_11 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 11, 14, 15, 12, 15), Direction.SOUTH));
        final VoxelShape[] TANK_12 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 13, 14, 15, 14, 15), Direction.SOUTH));
        final VoxelShape[] TANK_13 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 15, 14, 15, 16, 15), Direction.SOUTH));
        final VoxelShape[] TANK_14 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 17, 14, 15, 18, 15), Direction.SOUTH));
        final VoxelShape[] TANK_15 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 19, 14, 15, 20, 15), Direction.SOUTH));
        final VoxelShape[] TANK_16 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 21, 14, 15, 22, 15), Direction.SOUTH));
        final VoxelShape[] TANK_18 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 9, 2, 2, 10, 14), Direction.SOUTH));
        final VoxelShape[] TANK_19 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 11, 2, 2, 12, 14), Direction.SOUTH));
        final VoxelShape[] TANK_20 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 13, 2, 2, 14, 14), Direction.SOUTH));
        final VoxelShape[] TANK_21 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 15, 2, 2, 16, 14), Direction.SOUTH));
        final VoxelShape[] TANK_22 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 17, 2, 2, 18, 14), Direction.SOUTH));
        final VoxelShape[] TANK_23 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 19, 2, 2, 20, 14), Direction.SOUTH));
        final VoxelShape[] TANK_24 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 21, 2, 2, 22, 14), Direction.SOUTH));
        final VoxelShape[] TANK_25 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 7, 1, 15, 8, 15), Direction.SOUTH));
        final VoxelShape[] TANK_26 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 9, 2, 15, 10, 14), Direction.SOUTH));
        final VoxelShape[] TANK_27 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 11, 2, 15, 12, 14), Direction.SOUTH));
        final VoxelShape[] TANK_28 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 13, 2, 15, 14, 14), Direction.SOUTH));
        final VoxelShape[] TANK_29 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 15, 2, 15, 16, 14), Direction.SOUTH));
        final VoxelShape[] TANK_30 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 17, 2, 15, 18, 14), Direction.SOUTH));
        final VoxelShape[] TANK_31 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 19, 2, 15, 20, 14), Direction.SOUTH));
        final VoxelShape[] TANK_32 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 21, 2, 15, 22, 14), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_1 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1.2, 7, 1.2, 14.8, 21, 1.8), Direction.SOUTH));
        final VoxelShape[] TANK_2 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 9, 1, 15, 10, 2), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_2 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1.2, 7, 14.2, 14.8, 21, 14.8), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_3 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1.2, 7, 1.8, 1.8, 21, 14.2), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_4 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14.2, 7, 1.8, 14.8, 21, 14.2), Direction.SOUTH));
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.get(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(BACK_LEFT_LEG[direction.getHorizontalIndex()]);
            shapes.add(BACK_RIGHT_LEG[direction.getHorizontalIndex()]);
            shapes.add(FRONT_RIGHT_LEG[direction.getHorizontalIndex()]);
            shapes.add(FRONT_LEFT_LEG[direction.getHorizontalIndex()]);
            shapes.add(WATER_TANK_STAND[direction.getHorizontalIndex()]);
            shapes.add(TANK_3[direction.getHorizontalIndex()]);
            shapes.add(TANK_4[direction.getHorizontalIndex()]);
            shapes.add(TANK_5[direction.getHorizontalIndex()]);
            shapes.add(TANK_6[direction.getHorizontalIndex()]);
            shapes.add(TANK_7[direction.getHorizontalIndex()]);
            shapes.add(TANK_8[direction.getHorizontalIndex()]);
            shapes.add(TANK_10[direction.getHorizontalIndex()]);
            shapes.add(TANK_11[direction.getHorizontalIndex()]);
            shapes.add(TANK_12[direction.getHorizontalIndex()]);
            shapes.add(TANK_13[direction.getHorizontalIndex()]);
            shapes.add(TANK_14[direction.getHorizontalIndex()]);
            shapes.add(TANK_15[direction.getHorizontalIndex()]);
            shapes.add(TANK_16[direction.getHorizontalIndex()]);
            shapes.add(TANK_18[direction.getHorizontalIndex()]);
            shapes.add(TANK_19[direction.getHorizontalIndex()]);
            shapes.add(TANK_20[direction.getHorizontalIndex()]);
            shapes.add(TANK_21[direction.getHorizontalIndex()]);
            shapes.add(TANK_22[direction.getHorizontalIndex()]);
            shapes.add(TANK_23[direction.getHorizontalIndex()]);
            shapes.add(TANK_24[direction.getHorizontalIndex()]);
            shapes.add(TANK_25[direction.getHorizontalIndex()]);
            shapes.add(TANK_26[direction.getHorizontalIndex()]);
            shapes.add(TANK_27[direction.getHorizontalIndex()]);
            shapes.add(TANK_28[direction.getHorizontalIndex()]);
            shapes.add(TANK_29[direction.getHorizontalIndex()]);
            shapes.add(TANK_30[direction.getHorizontalIndex()]);
            shapes.add(TANK_31[direction.getHorizontalIndex()]);
            shapes.add(TANK_32[direction.getHorizontalIndex()]);
            shapes.add(TANK_INSIDE_1[direction.getHorizontalIndex()]);
            shapes.add(TANK_2[direction.getHorizontalIndex()]);
            shapes.add(TANK_INSIDE_2[direction.getHorizontalIndex()]);
            shapes.add(TANK_INSIDE_3[direction.getHorizontalIndex()]);
            shapes.add(TANK_INSIDE_4[direction.getHorizontalIndex()]);
            
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
            FluidUtil.interactWithFluidHandler(player, hand, world, pos, result.getFace());
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
        return new WaterCollectorTileEntity();
    }
}
