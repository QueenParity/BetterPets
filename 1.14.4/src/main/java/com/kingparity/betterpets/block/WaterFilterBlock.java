package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaterFilterBlock extends PetHorizontalBlock
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    //public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    
    public WaterFilterBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH)/*.with(ACTIVE, false)*/.with(HALF, DoubleBlockHalf.LOWER));
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape[] NORTH_WEST_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0.5, 0, 0.5, 2.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] NORTH_EAST_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(13.5, 0, 0.5, 15.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] SOUTH_EAST_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(13.5, 0, 13.5, 15.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] SOUTH_WEST_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0.5, 0, 13.5, 2.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] WATER_FILTER_STAND = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 5, 0, 16, 7, 16), Direction.SOUTH));
        final VoxelShape[] NORTH_WEST_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 8, 1, 2, 19, 2), Direction.SOUTH));
        final VoxelShape[] SOUTH_WEST_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 8, 14, 2, 19, 15), Direction.SOUTH));
        final VoxelShape[] NORTH_EAST_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 8, 1, 15, 19, 2), Direction.SOUTH));
        final VoxelShape[] SOUTH_EAST_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 8, 14, 15, 19, 15), Direction.SOUTH));
        final VoxelShape[] WEST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 29, 2, 2, 30, 14), Direction.SOUTH));
        final VoxelShape[] BASE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 7, 1, 15, 8, 15), Direction.SOUTH));
        final VoxelShape[] WEST_DOWN_STONE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1.4, 8, 2, 2, 18, 14), Direction.SOUTH));
        final VoxelShape[] EAST_DOWN_STONE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 8, 2, 14.6, 18, 14), Direction.SOUTH));
        final VoxelShape[] SOUTH_DOWN_STONE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 8, 14, 14, 18, 14.6), Direction.SOUTH));
        final VoxelShape[] NORTH_DOWN_STONE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 8, 1.4, 14, 18, 2), Direction.SOUTH));
        final VoxelShape[] EAST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 29, 2, 15, 30, 14), Direction.SOUTH));
        final VoxelShape[] NORTH_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 29, 1, 14, 30, 2), Direction.SOUTH));
        final VoxelShape[] SOUTH_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 29, 14, 14, 30, 15), Direction.SOUTH));
        final VoxelShape[] UP_STONE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 29, 2, 14, 29.6, 14), Direction.SOUTH));
        final VoxelShape[] SOUTH_EAST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 19, 14, 15, 30, 15), Direction.SOUTH));
        final VoxelShape[] NORTH_WEST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 19, 1, 2, 30, 2), Direction.SOUTH));
        final VoxelShape[] SOUTH_WEST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 19, 14, 2, 30, 15), Direction.SOUTH));
        final VoxelShape[] NORTH_EAST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 19, 1, 15, 30, 2), Direction.SOUTH));
        final VoxelShape[] EAST_UP_STONE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 19, 2, 14.6, 29, 14), Direction.SOUTH));
        final VoxelShape[] SOUTH_UP_STONE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 19, 14, 14, 29, 14.6), Direction.SOUTH));
        final VoxelShape[] NORTH_UP_STONE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 19, 1.4, 14, 29, 2), Direction.SOUTH));
        final VoxelShape[] WEST_UP_STONE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1.4, 19, 2, 2, 29, 14), Direction.SOUTH));
        final VoxelShape[] SOUTH_MID_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 18, 14, 14, 19, 15), Direction.SOUTH));
        final VoxelShape[] EAST_MID_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 18, 2, 15, 19, 14), Direction.SOUTH));
        final VoxelShape[] NORTH_MID_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 18, 1, 14, 19, 2), Direction.SOUTH));
        final VoxelShape[] WEST_MID_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 18, 2, 2, 19, 14), Direction.SOUTH));
    
        final VoxelShape WEST_MID_BEAM2 = Block.makeCuboidShape(1, 18, 2, 2, 19, 14);
        WEST_MID_BEAM2.withOffset(0, -16, 0);
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.get(DIRECTION);
            boolean top = state.get(HALF) == DoubleBlockHalf.UPPER;
            List<VoxelShape> shapes = new ArrayList<>();
            if(top)
            {
                shapes.add(NORTH_WEST_LEG[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(NORTH_EAST_LEG[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(SOUTH_EAST_LEG[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(SOUTH_WEST_LEG[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(WATER_FILTER_STAND[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(NORTH_WEST_DOWN_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(SOUTH_WEST_DOWN_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(NORTH_EAST_DOWN_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(SOUTH_EAST_DOWN_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(WEST_UP_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(BASE[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(WEST_DOWN_STONE[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(EAST_DOWN_STONE[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(SOUTH_DOWN_STONE[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(NORTH_DOWN_STONE[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(EAST_UP_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(NORTH_UP_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(SOUTH_UP_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(UP_STONE[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(SOUTH_EAST_UP_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(NORTH_WEST_UP_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(SOUTH_WEST_UP_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(NORTH_EAST_UP_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(EAST_UP_STONE[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(SOUTH_UP_STONE[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(NORTH_UP_STONE[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(WEST_UP_STONE[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(SOUTH_MID_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(EAST_MID_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(NORTH_MID_BEAM[direction.getIndex()].withOffset(0, -16, 0));
                shapes.add(WEST_MID_BEAM[direction.getIndex()].withOffset(0, -16, 0));
            }
            else
            {
                shapes.add(NORTH_WEST_LEG[direction.getIndex()]);
                shapes.add(NORTH_EAST_LEG[direction.getIndex()]);
                shapes.add(SOUTH_EAST_LEG[direction.getIndex()]);
                shapes.add(SOUTH_WEST_LEG[direction.getIndex()]);
                shapes.add(WATER_FILTER_STAND[direction.getIndex()]);
                shapes.add(NORTH_WEST_DOWN_BEAM[direction.getIndex()]);
                shapes.add(SOUTH_WEST_DOWN_BEAM[direction.getIndex()]);
                shapes.add(NORTH_EAST_DOWN_BEAM[direction.getIndex()]);
                shapes.add(SOUTH_EAST_DOWN_BEAM[direction.getIndex()]);
                shapes.add(WEST_UP_BEAM[direction.getIndex()]);
                shapes.add(BASE[direction.getIndex()]);
                shapes.add(WEST_DOWN_STONE[direction.getIndex()]);
                shapes.add(EAST_DOWN_STONE[direction.getIndex()]);
                shapes.add(SOUTH_DOWN_STONE[direction.getIndex()]);
                shapes.add(NORTH_DOWN_STONE[direction.getIndex()]);
                shapes.add(EAST_UP_BEAM[direction.getIndex()]);
                shapes.add(NORTH_UP_BEAM[direction.getIndex()]);
                shapes.add(SOUTH_UP_BEAM[direction.getIndex()]);
                shapes.add(UP_STONE[direction.getIndex()]);
                shapes.add(SOUTH_EAST_UP_BEAM[direction.getIndex()]);
                shapes.add(NORTH_WEST_UP_BEAM[direction.getIndex()]);
                shapes.add(SOUTH_WEST_UP_BEAM[direction.getIndex()]);
                shapes.add(NORTH_EAST_UP_BEAM[direction.getIndex()]);
                shapes.add(EAST_UP_STONE[direction.getIndex()]);
                shapes.add(SOUTH_UP_STONE[direction.getIndex()]);
                shapes.add(NORTH_UP_STONE[direction.getIndex()]);
                shapes.add(WEST_UP_STONE[direction.getIndex()]);
                shapes.add(SOUTH_MID_BEAM[direction.getIndex()]);
                shapes.add(EAST_MID_BEAM[direction.getIndex()]);
                shapes.add(NORTH_MID_BEAM[direction.getIndex()]);
                shapes.add(WEST_MID_BEAM[direction.getIndex()]);
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
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockPos pos = context.getPos();
        if(pos.getY() < 255 && context.getWorld().getBlockState(pos.up()).isReplaceable(context))
        {
            return this.getDefaultState().with(DIRECTION, context.getNearestLookingDirection()).with(HALF, DoubleBlockHalf.LOWER);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
    }
    
    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        DoubleBlockHalf half = state.get(HALF);
        BlockPos blockpos = half == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState blockstate = world.getBlockState(blockpos);
        if(blockstate.getBlock() == this && blockstate.get(HALF) != half)
        {
            world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
            world.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
            ItemStack itemstack = player.getHeldItemMainhand();
            if(!world.isRemote && !player.isCreative())
            {
                Block.spawnDrops(state, world, pos, null, player, itemstack);
                Block.spawnDrops(blockstate, world, blockpos, null, player, itemstack);
            }
        }
    
        super.onBlockHarvested(world, pos, state, player);
    }
    
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader reader, BlockPos pos)
    {
        BlockPos blockpos = pos.down();
        BlockState blockstate = reader.getBlockState(blockpos);
        if(state.get(HALF) == DoubleBlockHalf.LOWER)
        {
            return Block.hasSolidSide(blockstate, reader, blockpos, Direction.UP);
        }
        else
        {
            return blockstate.getBlock() == this;
        }
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if(!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof WaterFilterTileEntity)
            {
                WaterFilterTileEntity waterFilter = (WaterFilterTileEntity)tileEntity;
                ItemStack heldItemStack = player.getHeldItem(hand);
                if(heldItemStack != ItemStack.EMPTY)
                {
                    Item heldItem = heldItemStack.getItem();
                    if(heldItem instanceof BucketItem)
                    {
                        String itemName = heldItem.getRegistryName().toString();
                        if(itemName.equals("minecraft:bucket"))
                        {
                            waterFilter.extractFluid(500, true, 0);
                            waterFilter.extractFluid(500, true, 1);
                            if(!player.abilities.isCreativeMode)
                            {
                                heldItemStack.shrink(1);
                                if(heldItemStack.isEmpty())
                                {
                                    player.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
                                }
                                else if(!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET)))
                                {
                                    player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                                }
                            }
                            
                            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                        else
                        {
                            if(waterFilter.receiveFluid(500, true, 0) == 0 || waterFilter.receiveFluid(500, true, 1) == 0)
                            {
                                if(!player.abilities.isCreativeMode)
                                {
                                    player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                                }
                                
                                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            }
                        }
                    }
                    else
                    {
                        NetworkHooks.openGui((ServerPlayerEntity)player, (WaterFilterTileEntity)world.getTileEntity(pos), pos);
                    }
                }
                else
                {
                    NetworkHooks.openGui((ServerPlayerEntity)player, (WaterFilterTileEntity)world.getTileEntity(pos), pos);
                }
            }
        }
        return true;
    }
    
    @Override
    public void tick(BlockState state, World world, BlockPos pos, Random random)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof WaterFilterTileEntity)
        {
            ((WaterFilterTileEntity)tileEntity).onScheduleTick();
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
        return ModTileEntities.WATER_FILTER.create();
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        //builder.add(ACTIVE);
        builder.add(HALF);
    }
}
