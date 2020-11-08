package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.tileentity.WaterCollectorTileEntity;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class WaterCollectorBlock extends PetHorizontalBlock
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public WaterCollectorBlock(Properties properties)
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
            shapes.add(BACK_LEFT_LEG[direction.getIndex()]);
            shapes.add(BACK_RIGHT_LEG[direction.getIndex()]);
            shapes.add(FRONT_RIGHT_LEG[direction.getIndex()]);
            shapes.add(FRONT_LEFT_LEG[direction.getIndex()]);
            shapes.add(WATER_TANK_STAND[direction.getIndex()]);
            shapes.add(TANK_3[direction.getIndex()]);
            shapes.add(TANK_4[direction.getIndex()]);
            shapes.add(TANK_5[direction.getIndex()]);
            shapes.add(TANK_6[direction.getIndex()]);
            shapes.add(TANK_7[direction.getIndex()]);
            shapes.add(TANK_8[direction.getIndex()]);
            shapes.add(TANK_10[direction.getIndex()]);
            shapes.add(TANK_11[direction.getIndex()]);
            shapes.add(TANK_12[direction.getIndex()]);
            shapes.add(TANK_13[direction.getIndex()]);
            shapes.add(TANK_14[direction.getIndex()]);
            shapes.add(TANK_15[direction.getIndex()]);
            shapes.add(TANK_16[direction.getIndex()]);
            shapes.add(TANK_18[direction.getIndex()]);
            shapes.add(TANK_19[direction.getIndex()]);
            shapes.add(TANK_20[direction.getIndex()]);
            shapes.add(TANK_21[direction.getIndex()]);
            shapes.add(TANK_22[direction.getIndex()]);
            shapes.add(TANK_23[direction.getIndex()]);
            shapes.add(TANK_24[direction.getIndex()]);
            shapes.add(TANK_25[direction.getIndex()]);
            shapes.add(TANK_26[direction.getIndex()]);
            shapes.add(TANK_27[direction.getIndex()]);
            shapes.add(TANK_28[direction.getIndex()]);
            shapes.add(TANK_29[direction.getIndex()]);
            shapes.add(TANK_30[direction.getIndex()]);
            shapes.add(TANK_31[direction.getIndex()]);
            shapes.add(TANK_32[direction.getIndex()]);
            shapes.add(TANK_INSIDE_1[direction.getIndex()]);
            shapes.add(TANK_2[direction.getIndex()]);
            shapes.add(TANK_INSIDE_2[direction.getIndex()]);
            shapes.add(TANK_INSIDE_3[direction.getIndex()]);
            shapes.add(TANK_INSIDE_4[direction.getIndex()]);
            
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
    {
        return SHAPES.get(state);
    }
    
    /*@Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
    {
        return SHAPES.get(state);
    }*/

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return SHAPES.get(state);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if(!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof WaterCollectorTileEntity)
            {
                WaterCollectorTileEntity waterCollector = (WaterCollectorTileEntity)tileEntity;
                ItemStack heldItemStack = player.getHeldItem(hand);
                if(heldItemStack != ItemStack.EMPTY)
                {
                    Item heldItem = heldItemStack.getItem();
                    if(heldItem instanceof BucketItem)
                    {
                        String itemName = heldItem.getRegistryName().toString();
                        if(itemName.equals("minecraft:bucket"))
                        {
                            if(waterCollector.getFluidTank().drain(1000, IFluidHandler.FluidAction.EXECUTE).getAmount() != 0)
                            {
                                if (!player.abilities.isCreativeMode)
                                {
                                    heldItemStack.shrink(1);
                                    if (heldItemStack.isEmpty())
                                    {
                                        player.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
                                    }
                                    else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET)))
                                    {
                                        player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                                    }
                                }

                                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            }
                        }
                        else
                        {
                            //if(waterCollector.receiveFluid(1000, true, false) != 0)
                            if(waterCollector.getFluidTank().fill(new FluidStack(Fluids.WATER.getFluid(), 1000), IFluidHandler.FluidAction.EXECUTE) != 0)
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