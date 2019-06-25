package com.kingparity.betterpets.network.message;

import com.kingparity.betterpets.gui.container.PetResourcesCrafterContainer;
import com.kingparity.betterpets.init.BetterPetItems;
import com.kingparity.betterpets.item.PetFoodItem;
import com.kingparity.betterpets.tileentity.PetResourcesCrafterTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCraftPetFood
{
    private BlockPos pos;
    private int food_points;
    private float saturation;
    private float effectiveness;
    
    public MessageCraftPetFood(BlockPos pos, int food_points, float saturation, float effectiveness)
    {
        this.pos = pos;
        this.food_points = food_points;
        this.saturation = saturation;
        this.effectiveness = effectiveness;
    }
    
    //fromBytes
    public static void encode(MessageCraftPetFood pkt, PacketBuffer buf)
    {
        buf.writeBlockPos(pkt.pos);
        buf.writeVarInt(pkt.food_points);
        buf.writeFloat(pkt.saturation);
        buf.writeFloat(pkt.effectiveness);
    }
    
    //toBytes
    public static MessageCraftPetFood decode(PacketBuffer buf)
    {
        BlockPos pos = buf.readBlockPos();
        int food_points = buf.readVarInt();
        float saturation = buf.readFloat();
        float effectiveness = buf.readFloat();
        return new MessageCraftPetFood(pos, food_points, saturation, effectiveness);
    }
    
    //onMessage
    public static class Handler
    {
        public static void handle(final MessageCraftPetFood pkt, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                PlayerEntity player = ctx.get().getSender();
                World world = player.world;
                if(player.openContainer instanceof PetResourcesCrafterContainer)
                {
                    PetResourcesCrafterContainer container = (PetResourcesCrafterContainer)player.openContainer;
                    if(container.getPos().equals(pkt.pos))
                    {
                        PetResourcesCrafterTileEntity tileEntity = (PetResourcesCrafterTileEntity)world.getTileEntity(pkt.pos);
                        
                        boolean flag = false;
                        
                        for(int i = 0; i < tileEntity.getSizeInventory(); i++)
                        {
                            if(tileEntity.getStackInSlot(i) != ItemStack.EMPTY)
                            {
                                flag = true;
                                break;
                            }
                        }
                        if(flag)
                        {
                            for(int i = 0; i < tileEntity.getSizeInventory(); i++)
                            {
                                tileEntity.removeStackFromSlot(i);
                            }
                            PetFoodItem food = BetterPetItems.PET_FOOD;
                            food.setFoodPoints(pkt.food_points);
                            food.setSaturationRestored(pkt.saturation);
                            tileEntity.setInventorySlotContents(0, new ItemStack(food));
                        }
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}