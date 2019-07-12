package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
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
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;

public class WaterFilterBlock extends PetHorizontalBlock
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    
    public WaterFilterBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH).with(ACTIVE, false));
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape[] NORTH_WEST_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0.5, 0, 0.5, 2.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] NORTH_EAST_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(13.5, 0, 0.5, 15.5, 5, 2.5), Direction.SOUTH));
        final VoxelShape[] SOUTH_EAST_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(13.5, 0, 13.5, 15.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] SOUTH_WEST_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0.5, 0, 13.5, 2.5, 5, 15.5), Direction.SOUTH));
        final VoxelShape[] WATER_FILTER_STAND = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 5, 0, 16, 6, 16), Direction.SOUTH));
        final VoxelShape[] NORTH_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 20, 1, 15, 21, 2), Direction.SOUTH));
        final VoxelShape[] SOUTH_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 20, 14, 15, 21, 15), Direction.SOUTH));
        final VoxelShape[] WEST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 20, 2, 2, 21, 14), Direction.SOUTH));
        final VoxelShape[] TANK_BASE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 6, 1, 15, 7, 15), Direction.SOUTH));
        final VoxelShape[] EAST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 20, 2, 15, 21, 14), Direction.SOUTH));
        final VoxelShape[] NORTH_WEST_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 7, 1, 2, 13, 2), Direction.SOUTH));
        final VoxelShape[] UP_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 20, 2, 14, 21, 14), Direction.SOUTH));
        final VoxelShape[] SOUTH_WEST_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 7, 14, 2, 13, 15), Direction.SOUTH));
        final VoxelShape[] SOUTH_EAST_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 7, 14, 15, 13, 15), Direction.SOUTH));
        final VoxelShape[] NORTH_EAST_DOWN_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 7, 1, 15, 13, 2), Direction.SOUTH));
        final VoxelShape[] NORTH_DOWN_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 7, 1, 14, 13, 2), Direction.SOUTH));
        final VoxelShape[] SOUTH_DOWN_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 7, 14, 14, 13, 15), Direction.SOUTH));
        final VoxelShape[] EAST_DOWN_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 7, 2, 15, 13, 14), Direction.SOUTH));
        final VoxelShape[] WEST_DOWN_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 7, 2, 2, 13, 14), Direction.SOUTH));
        final VoxelShape[] EAST_MID_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 13, 2, 15, 14, 14), Direction.SOUTH));
        final VoxelShape[] WEST_MID_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 13, 2, 2, 14, 14), Direction.SOUTH));
        final VoxelShape[] SOUTH_MID_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 13, 14, 15, 14, 15), Direction.SOUTH));
        final VoxelShape[] NORTH_MID_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 13, 1, 15, 14, 2), Direction.SOUTH));
        final VoxelShape[] EAST_UP_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 14, 2, 15, 20, 14), Direction.SOUTH));
        final VoxelShape[] WEST_UP_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 14, 2, 2, 20, 14), Direction.SOUTH));
        final VoxelShape[] SOUTH_UP_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 14, 14, 14, 20, 15), Direction.SOUTH));
        final VoxelShape[] SOUTH_WEST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 14, 14, 2, 20, 15), Direction.SOUTH));
        final VoxelShape[] SOUTH_EAST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 14, 14, 15, 20, 15), Direction.SOUTH));
        final VoxelShape[] NORTH_EAST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(14, 14, 1, 15, 20, 2), Direction.SOUTH));
        final VoxelShape[] NORTH_UP_GLASS = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 14, 1, 14, 20, 2), Direction.SOUTH));
        final VoxelShape[] NORTH_WEST_UP_BEAM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(1, 14, 1, 2, 20, 2), Direction.SOUTH));
    
        final VoxelShape[] SEPERATOR = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 13, 2, 14, 14, 14), Direction.SOUTH));
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.get(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(NORTH_WEST_LEG[direction.getIndex()]);
            shapes.add(NORTH_EAST_LEG[direction.getIndex()]);
            shapes.add(SOUTH_EAST_LEG[direction.getIndex()]);
            shapes.add(SOUTH_WEST_LEG[direction.getIndex()]);
            shapes.add(WATER_FILTER_STAND[direction.getIndex()]);
            shapes.add(NORTH_UP_BEAM[direction.getIndex()]);
            shapes.add(SOUTH_UP_BEAM[direction.getIndex()]);
            shapes.add(WEST_UP_BEAM[direction.getIndex()]);
            shapes.add(TANK_BASE[direction.getIndex()]);
            shapes.add(EAST_UP_BEAM[direction.getIndex()]);
            shapes.add(NORTH_WEST_DOWN_BEAM[direction.getIndex()]);
            shapes.add(UP_GLASS[direction.getIndex()]);
            shapes.add(SOUTH_WEST_DOWN_BEAM[direction.getIndex()]);
            shapes.add(SOUTH_EAST_DOWN_BEAM[direction.getIndex()]);
            shapes.add(NORTH_EAST_DOWN_BEAM[direction.getIndex()]);
            shapes.add(NORTH_DOWN_GLASS[direction.getIndex()]);
            shapes.add(SOUTH_DOWN_GLASS[direction.getIndex()]);
            shapes.add(EAST_DOWN_GLASS[direction.getIndex()]);
            shapes.add(WEST_DOWN_GLASS[direction.getIndex()]);
            shapes.add(EAST_MID_BEAM[direction.getIndex()]);
            shapes.add(WEST_MID_BEAM[direction.getIndex()]);
            shapes.add(SOUTH_MID_BEAM[direction.getIndex()]);
            shapes.add(NORTH_MID_BEAM[direction.getIndex()]);
            shapes.add(EAST_UP_GLASS[direction.getIndex()]);
            shapes.add(WEST_UP_GLASS[direction.getIndex()]);
            shapes.add(SOUTH_UP_GLASS[direction.getIndex()]);
            shapes.add(SOUTH_WEST_UP_BEAM[direction.getIndex()]);
            shapes.add(SOUTH_EAST_UP_BEAM[direction.getIndex()]);
            shapes.add(NORTH_EAST_UP_BEAM[direction.getIndex()]);
            shapes.add(NORTH_UP_GLASS[direction.getIndex()]);
            shapes.add(NORTH_WEST_UP_BEAM[direction.getIndex()]);
            
            if(state.get(ACTIVE))
            {
                shapes.add(SEPERATOR[direction.getIndex()]);
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
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(ACTIVE);
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
