package com.kingparity.betterpets.block;

import com.kingparity.betterpets.tileentity.WaterCollectorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class WaterCollectorBlock extends Block
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
            /*if(!FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getFace()))
            {
                TileEntity tileEntity = world.getTileEntity(pos);
                if(tileEntity instanceof WaterCollectorTileEntity)
                {
                    NetworkHooks.openGui((ServerPlayerEntity)player, (WaterCollectorTileEntity)world.getTileEntity(pos), pos);
                }
            }*/
            FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getFace());
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
