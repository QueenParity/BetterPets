package com.kingparity.betterpets.block;

import com.kingparity.betterpets.init.BetterPetItems;
import com.kingparity.betterpets.init.BetterPetSounds;
import com.kingparity.betterpets.tileentity.WaterCollectorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class WaterCollectorBlock extends RotatedBlock
{
    public WaterCollectorBlock(Block block)
    {
        super(Block.Properties.from(block));
    }
    
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
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
                            if(waterCollector.removeFluid(1000))
                            {
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
                        }
                        else
                        {
                            if(waterCollector.addFluid(1000))
                            {
                                if(!player.abilities.isCreativeMode)
                                {
                                    player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                                }
                                
                                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            }
                        }
                    }
                    else if(waterCollector.getTubingEntity() != null && waterCollector.getTubingEntity().getEntityId() == player.getEntityId())
                    {
                        waterCollector.setTubingEntity(null);
                        world.playSound(null, pos, BetterPetSounds.TUBE_PUT_DOWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                    else if(heldItem == BetterPetItems.FLUID_TUBE)
                    {
                        waterCollector.setTubingEntity(player);
                        if(!player.abilities.isCreativeMode)
                        {
                            heldItemStack.shrink(1);
                        }
                        world.playSound(null, pos, BetterPetSounds.TUBE_PICK_UP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                }
                else if(waterCollector.getTubingEntity() != null && waterCollector.getTubingEntity().getEntityId() == player.getEntityId())
                {
                    waterCollector.setTubingEntity(null);
                    if(!player.abilities.isCreativeMode)
                    {
                        if(!player.addItemStackToInventory(new ItemStack(BetterPetItems.FLUID_TUBE)))
                        {
                            player.dropItem(new ItemStack(BetterPetItems.FLUID_TUBE), false);
                        }
                    }
                    world.playSound(null, pos, BetterPetSounds.TUBE_PUT_DOWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new WaterCollectorTileEntity();
    }
    
    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
}
