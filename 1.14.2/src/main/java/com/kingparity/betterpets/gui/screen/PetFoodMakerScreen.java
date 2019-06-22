package com.kingparity.betterpets.gui.screen;

import com.kingparity.betterpets.gui.GuiImageButton;
import com.kingparity.betterpets.gui.container.PetFoodMakerContainer;
import com.kingparity.betterpets.gui.slot.PetFoodMakerInputSlot;
import com.kingparity.betterpets.network.PacketHandler;
import com.kingparity.betterpets.network.message.MessageCraftPetFood;
import com.kingparity.betterpets.util.AllowedPetFood;
import com.kingparity.betterpets.util.Reference;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PetFoodMakerScreen extends ContainerScreen<PetFoodMakerContainer>
{
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(Reference.ID + ":textures/gui/pet_food_maker.png");
    private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(Reference.ID + ":textures/gui/pet_food_maker_buttons.png");
    private static final ResourceLocation BAR_TEXTURES = new ResourceLocation(Reference.ID + ":textures/gui/bar.png");
    
    private int food_points, saturation, effectiveness;
    
    public PetFoodMakerScreen(PetFoodMakerContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        this.food_points = 0;
        this.saturation = 0;
        this.effectiveness = 0;
        this.ySize = 180;
    }
    
    @Override
    protected void init()
    {
        super.init();
        this.addButton(new GuiImageButton(this.guiLeft + 120, this.height / 2 - 30, 0, 0, 35, 18, BUTTON_TEXTURES, (action) -> {
            System.out.println("clicked lol");
            PacketHandler.sendToServer(new MessageCraftPetFood(this.getContainer().getPos(), this.food_points, this.saturation, this.effectiveness));
        }));
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.title.getFormattedText();
        this.font.drawString(s, (float)(this.xSize / 2 - this.font.getStringWidth(s) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
        this.font.drawString("Craft", this.guiLeft - 107, this.height / 2 - 104, -1);
        
        this.drawIcons();
    }
    
    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
    }
    
    private void drawIcons()
    {
        this.minecraft.getTextureManager().bindTexture(Screen.GUI_ICONS_LOCATION);
        GlStateManager.color4f(1, 1, 1, 1);
        
        //HUNGER ICON
        int hYPos = 21;
        int hXPos = (this.xSize / 2) - 84;
        //background
        this.blit(hXPos, hYPos, 16, 27, 9, 9);
        //overlay
        this.blit(hXPos, hYPos, 52, 27, 9, 9);
    
        this.minecraft.getTextureManager().bindTexture(BAR_TEXTURES);
        GlStateManager.color4f(1, 0.5F, 0, 1);
        this.food_points = 0;
        int x = 0;
        for(Slot slot : this.getContainer().inventorySlots)
        {
            if(slot instanceof PetFoodMakerInputSlot)
            {
                PetFoodMakerInputSlot inputSlot = (PetFoodMakerInputSlot)slot;
                for(AllowedPetFood food : AllowedPetFood.values())
                {
                    if(food.getItem() == inputSlot.getStack().getItem())
                    {
                        this.food_points = (food.getFoodPoints() * inputSlot.getStack().getCount());
                        x++;
                        break;
                    }
                }
            }
        }
        if(x != 0)
        {
            this.food_points = this.food_points / x;
        }
        else
        {
            this.food_points = 0;
        }
        this.createBar(hXPos + 11, hYPos - 4, this.food_points);
    
        this.minecraft.getTextureManager().bindTexture(Screen.GUI_ICONS_LOCATION);
        GlStateManager.color4f(1, 1, 1, 1);
        
        int j5 = 0;
        if(this.playerInventory.player.world.getWorldInfo().isHardcore())
        {
            j5 = 5;
        }
    
        //SATURATION ICON
        int sYPos = 43;
        int sXPos = (this.xSize / 2) - 84;
        //background
        this.blit(sXPos, sYPos, 16, 0, 9, 9);
        //overlay
        this.blit(sXPos, sYPos, 160, 9 * j5, 9, 9);
        //this.blit(sXPos, sYPos, 52, 27, 9, 9);
        
        this.minecraft.getTextureManager().bindTexture(BAR_TEXTURES);
        GlStateManager.color4f(1, 1, 0, 1);
        this.saturation = 0;
        int y = 0;
        for(Slot slot : this.getContainer().inventorySlots)
        {
            if(slot instanceof PetFoodMakerInputSlot)
            {
                PetFoodMakerInputSlot inputSlot = (PetFoodMakerInputSlot)slot;
                for(AllowedPetFood food : AllowedPetFood.values())
                {
                    if(food.getItem() == inputSlot.getStack().getItem())
                    {
                        this.saturation = (int)(food.getSaturationRestored() * inputSlot.getStack().getCount());
                        y++;
                        break;
                    }
                }
            }
        }
        if(y != 0)
        {
            this.saturation = this.saturation / y;
        }
        else
        {
            this.saturation = 0;
        }
        this.createBar(sXPos + 11, sYPos - 4, this.saturation);
    
        this.minecraft.getTextureManager().bindTexture(Screen.GUI_ICONS_LOCATION);
        GlStateManager.color4f(1, 1, 1, 1);
    
        //EFFECTIVENESS ICON
        int eYPos = 65;
        int eXPos = (this.xSize / 2) - 84;
        //effectiveness
        this.blit(eXPos, eYPos, 34, 9, 9, 9);
    
        this.minecraft.getTextureManager().bindTexture(BAR_TEXTURES);
        GlStateManager.color4f(0.2F, 0.768F, 0.878F, 1);
        this.effectiveness = 0;
        int z = 0;
        for(Slot slot : this.getContainer().inventorySlots)
        {
            if(slot instanceof PetFoodMakerInputSlot)
            {
                PetFoodMakerInputSlot inputSlot = (PetFoodMakerInputSlot)slot;
                for(AllowedPetFood food : AllowedPetFood.values())
                {
                    if(food.getItem() == inputSlot.getStack().getItem())
                    {
                        this.effectiveness = (int)(food.getEffectiveQuality() * inputSlot.getStack().getCount());
                        z++;
                        break;
                    }
                }
            }
        }
        if(z != 0)
        {
            this.effectiveness = this.effectiveness / z;
        }
        else
        {
            this.effectiveness = 0;
        }
        this.createBar(eXPos + 11, eYPos - 4, this.effectiveness);
    }
    
    private void createBar(int posX, int posY, int width)
    {
        if(width > 60)
        {
            this.blit(posX, posY, 0, 0, 60, 16);
        }
        else
        {
            this.blit(posX, posY, 0, 0, width, 16);
        }
    }
}