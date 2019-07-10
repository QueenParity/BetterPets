package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WaterFilterBlock extends PetHorizontalBlock
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public WaterFilterBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape[] BACK_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0.5, 0, 0.5, 2.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] BACK_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(13.5, 0, 0.5, 15.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] FRONT_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(13.5, 0, 13.5, 15.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] FRONT_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0.5, 0, 13.5, 2.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] WATER_TANK_STAND = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 5, 0, 16, 6, 16), Direction.SOUTH));
        final VoxelShape[] BACK_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5, 20, 1, 15, 21, 2), Direction.SOUTH));
        final VoxelShape[] FRONT_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5, 20, 14, 15, 21, 15), Direction.SOUTH));
        final VoxelShape[] LEFT_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5, 20, 2, 6, 21, 14), Direction.SOUTH));
        final VoxelShape[] TANK_BASE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5, 6, 1, 15, 7, 15), Direction.SOUTH));
        final VoxelShape[] RIGHT_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 20, 2, 15, 21, 14), Direction.SOUTH));
        final VoxelShape[] BACK_LEFT_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5, 7, 1, 6, 20, 2), Direction.SOUTH));
        final VoxelShape[] BACK_LEFT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6, 20, 2, 10, 21, 8), Direction.SOUTH));
        final VoxelShape[] BACK_RIGHT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(10, 20, 2, 14, 21, 8), Direction.SOUTH));
        final VoxelShape[] FRONT_LEFT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6, 20, 8, 10, 21, 14), Direction.SOUTH));
        final VoxelShape[] FRONT_RIGHT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(10, 20, 8, 14, 21, 14), Direction.SOUTH));
        final VoxelShape[] FRONT_LEFT_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5, 7, 14, 6, 20, 15), Direction.SOUTH));
        final VoxelShape[] FRONT_RIGHT_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 7, 14, 15, 20, 15), Direction.SOUTH));
        final VoxelShape[] BACK_RIGHT_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 7, 1, 15, 20, 2), Direction.SOUTH));
        final VoxelShape[] BACK_BOTTOM_LEFT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6, 7, 1, 10, 14, 2), Direction.SOUTH));
        final VoxelShape[] BACK_BOTTOM_RIGHT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(10, 7, 1, 14, 14, 2), Direction.SOUTH));
        final VoxelShape[] BACK_TOP_RIGHT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(10, 14, 1, 14, 20, 2), Direction.SOUTH));
        final VoxelShape[] BACK_TOP_LEFT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6, 14, 1, 10, 20, 2), Direction.SOUTH));
        final VoxelShape[] FRONT_BOTTOM_LEFT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6, 7, 14, 10, 14, 15), Direction.SOUTH));
        final VoxelShape[] FRONT_TOP_LEFT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(6, 14, 14, 10, 20, 15), Direction.SOUTH));
        final VoxelShape[] FRONT_TOP_RIGHT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(10, 14, 14, 14, 20, 15), Direction.SOUTH));
        final VoxelShape[] FRONT_BOTTOM_RIGHT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(10, 7, 14, 14, 14, 15), Direction.SOUTH));
        final VoxelShape[] RIGHT_BOTTOM_BACK_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 7, 2, 15, 14, 8), Direction.SOUTH));
        final VoxelShape[] RIGHT_BOTTOM_FRONT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 7, 8, 15, 14, 14), Direction.SOUTH));
        final VoxelShape[] RIGHT_TOP_FRONT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 14, 8, 15, 20, 14), Direction.SOUTH));
        final VoxelShape[] RIGHT_TOP_BACK_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 14, 2, 15, 20, 8), Direction.SOUTH));
        final VoxelShape[] LEFT_TOP_FRONT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5, 14, 8, 6, 20, 14), Direction.SOUTH));
        final VoxelShape[] LEFT_BOTTOM_FRONT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5, 7, 8, 6, 14, 14), Direction.SOUTH));
        final VoxelShape[] LEFT_BOTTOM_BACK_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5, 7, 2, 6, 14, 8), Direction.SOUTH));
        final VoxelShape[] LEFT_TOP_BACK_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(5, 14, 2, 6, 20, 8), Direction.SOUTH));
        final VoxelShape[] FILTER_BASE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 6, 4, 5, 7, 12), Direction.SOUTH));
        final VoxelShape[] FILTER_FRONT_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 17, 11, 5, 18, 12), Direction.SOUTH));
        final VoxelShape[] FILTER_BACK_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 17, 4, 5, 18, 5), Direction.SOUTH));
        final VoxelShape[] FILTER_LEFT_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 17, 5, 2, 18, 11), Direction.SOUTH));
        final VoxelShape[] FILTER_FRONT_LEFT_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 7, 11, 2, 17, 12), Direction.SOUTH));
        final VoxelShape[] FILTER_BACK_LEFT_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 7, 4, 2, 17, 5), Direction.SOUTH));
        final VoxelShape[] FILTER_LEFT_BOTTOM_BACK_LEFT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 7, 5, 2, 12, 8), Direction.SOUTH));
        final VoxelShape[] FILTER_LEFT_BOTTOM_FRONT_LEFT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 7, 8, 2, 12, 11), Direction.SOUTH));
        final VoxelShape[] FILTER_LEFT_TOP_FRONT_LEFT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 12, 8, 2, 17, 11), Direction.SOUTH));
        final VoxelShape[] FILTER_LEFT_TOP_BACK_LEFT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 12, 5, 2, 17, 8), Direction.SOUTH));
        final VoxelShape[] FILTER_LEFT_BOTTOM_FRONT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 7, 11, 5, 12, 12), Direction.SOUTH));
        final VoxelShape[] FILTER_LEFT_TOP_FRONT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 12, 11, 5, 17, 12), Direction.SOUTH));
        final VoxelShape[] FILTER_LEFT_BOTTOM_BACK = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 7, 4, 5, 12, 5), Direction.SOUTH));
        final VoxelShape[] FILTER_LEFT_TOP_BACK = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 12, 4, 5, 17, 5), Direction.SOUTH));
        final VoxelShape[] FILTER_TOP_FRONT_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 17, 8, 5, 18, 11), Direction.SOUTH));
        final VoxelShape[] FILTER_TOP_BACK_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 17, 5, 5, 18, 8), Direction.SOUTH));
    
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.get(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(BACK_LEFT_LEG[direction.getIndex()]);
            shapes.add(BACK_RIGHT_LEG[direction.getIndex()]);
            shapes.add(FRONT_RIGHT_LEG[direction.getIndex()]);
            shapes.add(FRONT_LEFT_LEG[direction.getIndex()]);
            shapes.add(WATER_TANK_STAND[direction.getIndex()]);
            shapes.add(BACK_BEAM[direction.getIndex()]);
            shapes.add(FRONT_BEAM[direction.getIndex()]);
            shapes.add(LEFT_BEAM[direction.getIndex()]);
            shapes.add(TANK_BASE[direction.getIndex()]);
            shapes.add(RIGHT_BEAM[direction.getIndex()]);
            shapes.add(BACK_LEFT_BEAM[direction.getIndex()]);
            shapes.add(BACK_LEFT_GLASS[direction.getIndex()]);
            shapes.add(BACK_RIGHT_GLASS[direction.getIndex()]);
            shapes.add(FRONT_LEFT_GLASS[direction.getIndex()]);
            shapes.add(FRONT_RIGHT_GLASS[direction.getIndex()]);
            shapes.add(FRONT_LEFT_BEAM[direction.getIndex()]);
            shapes.add(FRONT_RIGHT_BEAM[direction.getIndex()]);
            shapes.add(BACK_RIGHT_BEAM[direction.getIndex()]);
            shapes.add(BACK_BOTTOM_LEFT_GLASS[direction.getIndex()]);
            shapes.add(BACK_BOTTOM_RIGHT_GLASS[direction.getIndex()]);
            shapes.add(BACK_TOP_RIGHT_GLASS[direction.getIndex()]);
            shapes.add(BACK_TOP_LEFT_GLASS[direction.getIndex()]);
            shapes.add(FRONT_BOTTOM_LEFT_GLASS[direction.getIndex()]);
            shapes.add(FRONT_TOP_LEFT_GLASS[direction.getIndex()]);
            shapes.add(FRONT_TOP_RIGHT_GLASS[direction.getIndex()]);
            shapes.add(FRONT_BOTTOM_RIGHT_GLASS[direction.getIndex()]);
            shapes.add(RIGHT_BOTTOM_BACK_GLASS[direction.getIndex()]);
            shapes.add(RIGHT_BOTTOM_FRONT_GLASS[direction.getIndex()]);
            shapes.add(RIGHT_TOP_FRONT_GLASS[direction.getIndex()]);
            shapes.add(RIGHT_TOP_BACK_GLASS[direction.getIndex()]);
            shapes.add(LEFT_TOP_FRONT_GLASS[direction.getIndex()]);
            shapes.add(LEFT_BOTTOM_FRONT_GLASS[direction.getIndex()]);
            shapes.add(LEFT_BOTTOM_BACK_GLASS[direction.getIndex()]);
            shapes.add(LEFT_TOP_BACK_GLASS[direction.getIndex()]);
            shapes.add(FILTER_BASE[direction.getIndex()]);
            shapes.add(FILTER_FRONT_BEAM[direction.getIndex()]);
            shapes.add(FILTER_BACK_BEAM[direction.getIndex()]);
            shapes.add(FILTER_LEFT_BEAM[direction.getIndex()]);
            shapes.add(FILTER_FRONT_LEFT_BEAM[direction.getIndex()]);
            shapes.add(FILTER_BACK_LEFT_BEAM[direction.getIndex()]);
            shapes.add(FILTER_LEFT_BOTTOM_BACK_LEFT[direction.getIndex()]);
            shapes.add(FILTER_LEFT_BOTTOM_FRONT_LEFT[direction.getIndex()]);
            shapes.add(FILTER_LEFT_TOP_FRONT_LEFT[direction.getIndex()]);
            shapes.add(FILTER_LEFT_TOP_BACK_LEFT[direction.getIndex()]);
            shapes.add(FILTER_LEFT_BOTTOM_FRONT[direction.getIndex()]);
            shapes.add(FILTER_LEFT_TOP_FRONT[direction.getIndex()]);
            shapes.add(FILTER_LEFT_BOTTOM_BACK[direction.getIndex()]);
            shapes.add(FILTER_LEFT_TOP_BACK[direction.getIndex()]);
            shapes.add(FILTER_TOP_FRONT_GLASS[direction.getIndex()]);
            shapes.add(FILTER_TOP_BACK_GLASS[direction.getIndex()]);
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
                            waterFilter.extractFluid(1000, true);
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
                            if(waterFilter.receiveFluid(1000, true) == 0)
                            {
                                if(!player.abilities.isCreativeMode)
                                {
                                    player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                                }
                            
                                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ModTileEntities.WATER_FILTER.create();
    }
    
    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
}
