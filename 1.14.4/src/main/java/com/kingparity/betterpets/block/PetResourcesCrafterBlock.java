package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.core.ModTileEntities;
import com.kingparity.betterpets.tileentity.PetResourcesCrafterTileEntity;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;

public class PetResourcesCrafterBlock extends DirectionalBlock// implements IWaterLoggable
{
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public PetResourcesCrafterBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape[] NORTH_FACE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 0, 0, 16, 16, 4), Direction.SOUTH));
        final VoxelShape[] SOUTH_FACE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(0, 0, 12, 16, 16, 16), Direction.SOUTH));
        final VoxelShape[] MIDDLE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.makeCuboidShape(2, 2, 4, 14, 14, 12), Direction.SOUTH));
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.get(FACING);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(NORTH_FACE[direction.getIndex()]);
            shapes.add(SOUTH_FACE[direction.getIndex()]);
            shapes.add(MIDDLE[direction.getIndex()]);
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }
    
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
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
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite().getOpposite());
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if(!world.isRemote)
        {
            NetworkHooks.openGui((ServerPlayerEntity)player, (PetResourcesCrafterTileEntity)world.getTileEntity(pos), pos);
        }
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ModTileEntities.PET_RESOURCES_CRAFTER.create();
    }
    
    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
}
