package com.kingparity.betterpets.block;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.block.entity.BaseTileEntity;
import com.kingparity.betterpets.block.entity.WaterFilterTileEntity;
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

public class WaterFilterBlock extends HorizontalFacingBlock implements BlockEntityProvider, AttributeProvider
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public WaterFilterBlock(FabricBlockSettings settings)
    {
        super(settings.nonOpaque());
        this.setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        /*final VoxelShape[] BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 0, 0, 16, 1, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_NW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 1, 0, 1, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_NE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(15, 1, 0, 16, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_SE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(15, 1, 15, 16, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_SW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 1, 15, 1, 15, 16), Direction.SOUTH));
        final VoxelShape[] TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 15, 0, 16, 16, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_S = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(7, 1, 15, 9, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_N = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(7, 1, 0, 9, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0.5, 1, 0.5, 15.5, 15, 15.5), Direction.SOUTH));*/
    
        final VoxelShape[] BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 0, 0, 16, 1, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_NW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 1, 0, 1, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_NE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(15, 1, 0, 16, 15, 1), Direction.SOUTH));
        final VoxelShape[] BEAM_SE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(15, 1, 15, 16, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_SW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 1, 15, 1, 15, 16), Direction.SOUTH));
        final VoxelShape[] TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 15, 0, 16, 16, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_S = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(7, 1, 15, 9, 15, 16), Direction.SOUTH));
        final VoxelShape[] BEAM_N = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(7, 1, 0, 9, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS_NW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 1, 0.25, 7, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS_NE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(9, 1, 0.25, 15, 15, 1), Direction.SOUTH));
        final VoxelShape[] GLASS_SE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(9, 1, 15, 15, 15, 15.75), Direction.SOUTH));
        final VoxelShape[] GLASS_SW = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 1, 15, 7, 15, 15.75), Direction.SOUTH));
        final VoxelShape[] GLASS_W = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0.25, 1, 1, 1, 15, 15), Direction.SOUTH));
        final VoxelShape[] GLASS_E = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(15, 1, 1, 15.75, 15, 15), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(7, 1, 1, 9, 1.5, 15), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(7, 14.5, 1, 9, 15, 15), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_NORTH = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(7, 1.5, 1, 9, 14.5, 1.5), Direction.SOUTH));
        final VoxelShape[] BEAM_INNER_SOUTH = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(7, 1.5, 14.5, 9, 14.5, 15), Direction.SOUTH));
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.get(FACING);
            List<VoxelShape> shapes = new ArrayList<>();
            /*shapes.add(BOTTOM[direction.getId()]);
            shapes.add(BEAM_NW[direction.getId()]);
            shapes.add(BEAM_NE[direction.getId()]);
            shapes.add(BEAM_SE[direction.getId()]);
            shapes.add(BEAM_SW[direction.getId()]);
            shapes.add(TOP[direction.getId()]);
            shapes.add(BEAM_S[direction.getId()]);
            shapes.add(BEAM_N[direction.getId()]);
            shapes.add(GLASS[direction.getId()]);*/
    
            shapes.add(BOTTOM[direction.getId()]);
            shapes.add(BEAM_NW[direction.getId()]);
            shapes.add(BEAM_NE[direction.getId()]);
            shapes.add(BEAM_SE[direction.getId()]);
            shapes.add(BEAM_SW[direction.getId()]);
            shapes.add(TOP[direction.getId()]);
            shapes.add(BEAM_S[direction.getId()]);
            shapes.add(BEAM_N[direction.getId()]);
            shapes.add(GLASS_NW[direction.getId()]);
            shapes.add(GLASS_NE[direction.getId()]);
            shapes.add(GLASS_SE[direction.getId()]);
            shapes.add(GLASS_SW[direction.getId()]);
            shapes.add(GLASS_W[direction.getId()]);
            shapes.add(GLASS_E[direction.getId()]);
            shapes.add(BEAM_INNER_BOTTOM[direction.getId()]);
            shapes.add(BEAM_INNER_TOP[direction.getId()]);
            shapes.add(BEAM_INNER_NORTH[direction.getId()]);
            shapes.add(BEAM_INNER_SOUTH[direction.getId()]);
            
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
        if(blockEntity instanceof WaterFilterTileEntity)
        {
            WaterFilterTileEntity waterFilter = (WaterFilterTileEntity)blockEntity;
            list.offer(waterFilter.fluidInv, SHAPES.get(state));
        }
    }
    
    /*@Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        WaterCollectorBlockEntity blockEntity = (WaterCollectorBlockEntity)world.getBlockEntity(pos);
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
        return new WaterFilterTileEntity();
    }
}
