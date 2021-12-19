package com.kingparity.betterpets.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kingparity.betterpets.blockentity.TankBlockEntity;
import com.kingparity.betterpets.util.BlockEntityUtil;
import com.kingparity.betterpets.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TankBlock extends Block implements EntityBlock
{
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    
    public TankBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(UP, false).setValue(DOWN, false));
        SHAPES = this.generateShapes(this.getStateDefinition().getPossibleStates());
    }
    
    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        final VoxelShape TOP = Block.box(1, 15, 1, 15, 16, 15);
        final VoxelShape BOTTOM = Block.box(1, 0, 1, 15, 1, 15);
        
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            boolean up = state.getValue(UP);
            boolean down = state.getValue(DOWN);
            
            List<VoxelShape> shapes = new ArrayList<>();
            int inner_start = 1;
            int inner_end = 15;
            if(up)
            {
                inner_end++;
            }
            else
            {
                shapes.add(TOP);
            }
            
            if(down)
            {
                inner_start--;
            }
            else
            {
                shapes.add(BOTTOM);
            }
            
            final VoxelShape GLASS_NORTH = Block.box(2, inner_start, 1.5, 14, inner_end, 2);
            final VoxelShape GLASS_SOUTH = Block.box(2, inner_start, 14, 14, inner_end, 14.5);
            final VoxelShape GLASS_WEST = Block.box(1.5, inner_start, 2, 2, inner_end, 14);
            final VoxelShape GLASS_EAST = Block.box(14, inner_start, 2, 14.5, inner_end, 14);
            final VoxelShape BEAM_NORTH_WEST = Block.box(1, inner_start, 1, 2, inner_end, 2);
            final VoxelShape BEAM_SOUTH_WEST = Block.box(1, inner_start, 14, 2, inner_end, 15);
            final VoxelShape BEAM_SOUTH_EAST = Block.box(14, inner_start, 14, 15, inner_end, 15);
            final VoxelShape BEAM_NORTH_EAST = Block.box(14, inner_start, 1, 15, inner_end, 2);
            
            shapes.add(GLASS_NORTH);
            shapes.add(GLASS_SOUTH);
            shapes.add(GLASS_WEST);
            shapes.add(GLASS_EAST);
            shapes.add(BEAM_NORTH_WEST);
            shapes.add(BEAM_SOUTH_WEST);
            shapes.add(BEAM_SOUTH_EAST);
            shapes.add(BEAM_NORTH_EAST);
            
            
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state);
    }
    
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos newPos)
    {
        boolean up = level.getBlockState(pos.above()).getBlock() == this;
        boolean down = level.getBlockState(pos.below()).getBlock() == this;
        return state.setValue(UP, up).setValue(DOWN, down);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(UP);
        builder.add(DOWN);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if(!level.isClientSide)
        {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            BlockPos tankPos = pos;
            
            while(blockEntity instanceof TankBlockEntity)
            {
                tankPos = tankPos.below();
                blockEntity = level.getBlockEntity(tankPos);
            }
            
            tankPos = tankPos.above();
            blockEntity = level.getBlockEntity(tankPos);
            
            while(blockEntity instanceof TankBlockEntity)
            {
                TankBlockEntity tank = (TankBlockEntity)blockEntity;
                if(tank.getTank().getFluidAmount() <= tank.getTank().getCapacity() - 1000 || !(level.getBlockEntity(tankPos.above()) instanceof TankBlockEntity))
                {
                    if(!FluidUtil.interactWithFluidHandler(player, hand, level, tankPos, result.getDirection()))
                    {
                        tankPos = tankPos.below();
                        FluidUtil.interactWithFluidHandler(player, hand, level, tankPos, result.getDirection());
                    }
                    break;
                }
                else
                {
                    tankPos = tankPos.above();
                    blockEntity = level.getBlockEntity(tankPos);
                }
            }
            //BlockEntityUtil.sendUpdatePacket(level.getBlockEntity(pos), (ServerPlayer) player);
            return InteractionResult.SUCCESS;
            //return FluidUtil.interactWithFluidHandler(player, hand, level, tankPos, result.getDirection()) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new TankBlockEntity(pos, state);
    }
}