package com.kingparity.betterpets.block;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.block.entity.BaseTileEntity;
import com.kingparity.betterpets.block.entity.WaterCollectorTileEntity;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WaterCollectorBlock extends HorizontalFacingBlock implements BlockEntityProvider, AttributeProvider
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public WaterCollectorBlock(FabricBlockSettings settings)
    {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape[] BACK_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0.5, 0, 0.5, 2.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] BACK_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(13.5, 0, 0.5, 15.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] FRONT_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(13.5, 0, 13.5, 15.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] FRONT_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0.5, 0, 13.5, 2.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] WATER_TANK_STAND = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 5, 0, 16, 7, 16), Direction.SOUTH));
        final VoxelShape[] TANK_3 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 11, 1, 15, 12, 2), Direction.SOUTH));
        final VoxelShape[] TANK_4 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 13, 1, 15, 14, 2), Direction.SOUTH));
        final VoxelShape[] TANK_5 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 15, 1, 15, 16, 2), Direction.SOUTH));
        final VoxelShape[] TANK_6 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 17, 1, 15, 18, 2), Direction.SOUTH));
        final VoxelShape[] TANK_7 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 19, 1, 15, 20, 2), Direction.SOUTH));
        final VoxelShape[] TANK_8 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 21, 1, 15, 22, 2), Direction.SOUTH));
        final VoxelShape[] TANK_10 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 9, 14, 15, 10, 15), Direction.SOUTH));
        final VoxelShape[] TANK_11 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 11, 14, 15, 12, 15), Direction.SOUTH));
        final VoxelShape[] TANK_12 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 13, 14, 15, 14, 15), Direction.SOUTH));
        final VoxelShape[] TANK_13 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 15, 14, 15, 16, 15), Direction.SOUTH));
        final VoxelShape[] TANK_14 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 17, 14, 15, 18, 15), Direction.SOUTH));
        final VoxelShape[] TANK_15 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 19, 14, 15, 20, 15), Direction.SOUTH));
        final VoxelShape[] TANK_16 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 21, 14, 15, 22, 15), Direction.SOUTH));
        final VoxelShape[] TANK_18 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 9, 2, 2, 10, 14), Direction.SOUTH));
        final VoxelShape[] TANK_19 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 11, 2, 2, 12, 14), Direction.SOUTH));
        final VoxelShape[] TANK_20 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 13, 2, 2, 14, 14), Direction.SOUTH));
        final VoxelShape[] TANK_21 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 15, 2, 2, 16, 14), Direction.SOUTH));
        final VoxelShape[] TANK_22 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 17, 2, 2, 18, 14), Direction.SOUTH));
        final VoxelShape[] TANK_23 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 19, 2, 2, 20, 14), Direction.SOUTH));
        final VoxelShape[] TANK_24 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 21, 2, 2, 22, 14), Direction.SOUTH));
        final VoxelShape[] TANK_25 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 7, 1, 15, 8, 15), Direction.SOUTH));
        final VoxelShape[] TANK_26 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 9, 2, 15, 10, 14), Direction.SOUTH));
        final VoxelShape[] TANK_27 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 11, 2, 15, 12, 14), Direction.SOUTH));
        final VoxelShape[] TANK_28 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 13, 2, 15, 14, 14), Direction.SOUTH));
        final VoxelShape[] TANK_29 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 15, 2, 15, 16, 14), Direction.SOUTH));
        final VoxelShape[] TANK_30 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 17, 2, 15, 18, 14), Direction.SOUTH));
        final VoxelShape[] TANK_31 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 19, 2, 15, 20, 14), Direction.SOUTH));
        final VoxelShape[] TANK_32 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 21, 2, 15, 22, 14), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_1 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1.2, 7, 1.2, 14.8, 21, 1.8), Direction.SOUTH));
        final VoxelShape[] TANK_2 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 9, 1, 15, 10, 2), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_2 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1.2, 7, 14.2, 14.8, 21, 14.8), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_3 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1.2, 7, 1.8, 1.8, 21, 14.2), Direction.SOUTH));
        final VoxelShape[] TANK_INSIDE_4 = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14.2, 7, 1.8, 14.8, 21, 14.2), Direction.SOUTH));
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.get(FACING);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(BACK_LEFT_LEG[direction.getId()]);
            shapes.add(BACK_RIGHT_LEG[direction.getId()]);
            shapes.add(FRONT_RIGHT_LEG[direction.getId()]);
            shapes.add(FRONT_LEFT_LEG[direction.getId()]);
            shapes.add(WATER_TANK_STAND[direction.getId()]);
            shapes.add(TANK_3[direction.getId()]);
            shapes.add(TANK_4[direction.getId()]);
            shapes.add(TANK_5[direction.getId()]);
            shapes.add(TANK_6[direction.getId()]);
            shapes.add(TANK_7[direction.getId()]);
            shapes.add(TANK_8[direction.getId()]);
            shapes.add(TANK_10[direction.getId()]);
            shapes.add(TANK_11[direction.getId()]);
            shapes.add(TANK_12[direction.getId()]);
            shapes.add(TANK_13[direction.getId()]);
            shapes.add(TANK_14[direction.getId()]);
            shapes.add(TANK_15[direction.getId()]);
            shapes.add(TANK_16[direction.getId()]);
            shapes.add(TANK_18[direction.getId()]);
            shapes.add(TANK_19[direction.getId()]);
            shapes.add(TANK_20[direction.getId()]);
            shapes.add(TANK_21[direction.getId()]);
            shapes.add(TANK_22[direction.getId()]);
            shapes.add(TANK_23[direction.getId()]);
            shapes.add(TANK_24[direction.getId()]);
            shapes.add(TANK_25[direction.getId()]);
            shapes.add(TANK_26[direction.getId()]);
            shapes.add(TANK_27[direction.getId()]);
            shapes.add(TANK_28[direction.getId()]);
            shapes.add(TANK_29[direction.getId()]);
            shapes.add(TANK_30[direction.getId()]);
            shapes.add(TANK_31[direction.getId()]);
            shapes.add(TANK_32[direction.getId()]);
            shapes.add(TANK_INSIDE_1[direction.getId()]);
            shapes.add(TANK_2[direction.getId()]);
            shapes.add(TANK_INSIDE_2[direction.getId()]);
            shapes.add(TANK_INSIDE_3[direction.getId()]);
            shapes.add(TANK_INSIDE_4[direction.getId()]);
            
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(Properties.HORIZONTAL_FACING);
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx)
    {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }
    
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.onPlaced(world, pos, state, placer, stack);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof BaseTileEntity)
        {
            ((BaseTileEntity)blockEntity).onPlacedBy(placer, stack);
        }
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof BaseTileEntity)
        {
            return ((BaseTileEntity)blockEntity).onUse(player, hand, hit);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
    
    @Override
    public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> list)
    {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof WaterCollectorTileEntity)
        {
            WaterCollectorTileEntity waterCollector = (WaterCollectorTileEntity)blockEntity;
            list.offer(waterCollector.fluidInv, SHAPES.get(state));
        }
    }
    
    /*@Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        WaterCollectorTileEntity blockEntity = (WaterCollectorTileEntity)world.getBlockEntity(pos);
        ItemStack itemStack = player.getStackInHand(hand);
        
        Item item = itemStack.getItem();
        if(item == Items.WATER_BUCKET)
        {
            if(!world.isClient)
            {
                if(blockEntity.getTankWater().getBuckets() < blockEntity.getTankWater().getCapacity())
                {
                    if(!player.abilities.creativeMode)
                    {
                        player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                    }
                    
                    blockEntity.getTankWater().fill(1000, false);
                    world.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
            
            return ActionResult.success(world.isClient);
        }
        else if(item == Items.BUCKET)
        {
            if(!world.isClient)
            {
                if(!blockEntity.getTankWater().isEmpty())
                {
                    if(!player.abilities.creativeMode)
                    {
                        itemStack.decrement(1);
                        if(itemStack.isEmpty())
                        {
                            player.setStackInHand(hand, new ItemStack(Items.WATER_BUCKET));
                        }
                        else if(!player.inventory.insertStack(new ItemStack(Items.WATER_BUCKET)))
                        {
                            player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    }
                    blockEntity.getTankWater().drain(1000, false);
                    world.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
            
            return ActionResult.success(world.isClient);
        }
        else
        {
            return ActionResult.PASS;
        }
    }*/
    
    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new WaterCollectorTileEntity();
    }
}
