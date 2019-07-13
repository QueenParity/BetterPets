package com.kingparity.betterpets.tileentity;

import com.kingparity.betterpets.block.WaterFilterBlock;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;

import java.util.Iterator;

public abstract class BasicLootTileEntity extends LockableLootTileEntity
{
    private NonNullList<ItemStack> stacks;
    private String ID;
    private int playerCount;
    
    public BasicLootTileEntity(TileEntityType<?> tileEntityType, String id)
    {
        super(tileEntityType);
        this.ID = id;
        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
    }
    
    @Override
    public abstract int getSizeInventory();
    
    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent(String.format("container.%s.%s", Reference.ID, ID));
    }
    
    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return stacks;
    }
    
    @Override
    protected void setItems(NonNullList<ItemStack> stacks)
    {
        this.stacks = stacks;
    }
    
    @Override
    protected abstract Container createMenu(int id, PlayerInventory player);
    
    @Override
    public void openInventory(PlayerEntity player)
    {
        if(!player.isSpectator())
        {
            if(this.playerCount < 0)
            {
                this.playerCount = 0;
            }
            this.playerCount++;
            
            this.scheduleTick();
        }
    }
    
    @Override
    public void closeInventory(PlayerEntity player)
    {
        if(!player.isSpectator())
        {
            this.playerCount--;
        }
    }
    
    private void scheduleTick()
    {
        this.world.getPendingBlockTicks().scheduleTick(this.getPos(), this.getBlockState().getBlock(), 5);
    }
    
    public void onScheduleTick()
    {
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        World world = this.getWorld();
        if(world != null)
        {
            this.playerCount = ChestTileEntity.func_213976_a(world, this, x, y, z); //Gets a count of players around using this inventory
            if(this.playerCount > 0)
            {
                this.scheduleTick();
            }
            else
            {
                BlockState state = this.getBlockState();
                if(!(state.getBlock() instanceof WaterFilterBlock))
                {
                    this.remove();
                }
            }
        }
    }
    
    @Override
    public boolean isEmpty()
    {
        Iterator it = this.stacks.iterator();
        ItemStack stack;
        do
        {
            if(!it.hasNext())
            {
                return true;
            }
            stack = (ItemStack)it.next();
        }
        while(stack.isEmpty());
        return false;
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        if(!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.stacks);
        }
        return compound;
    }
    
    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        this.stacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if(!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.stacks);
        }
    }
    
    public void syncToClient()
    {
        this.markDirty();
        if(!world.isRemote)
        {
            if(world instanceof ServerWorld)
            {
                ServerWorld server = (ServerWorld)world;
                ChunkPos chunkPos = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
                ChunkManager manager = (server.getChunkProvider()).chunkManager;
                if(manager != null)
                {
                    SUpdateTileEntityPacket packet = getUpdatePacket();
                    if(packet != null)
                    {
                        manager.getTrackingPlayers(chunkPos, false).forEach(e -> e.connection.sendPacket(packet));
                    }
                }
            }
        }
    }
    
    @Override
    public CompoundNBT getUpdateTag()
    {
        return write(new CompoundNBT());
    }
    
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        read(pkt.getNbtCompound());
    }
}
