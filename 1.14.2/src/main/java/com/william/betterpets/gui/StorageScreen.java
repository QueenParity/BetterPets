package com.william.betterpets.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.william.betterpets.entity.BetterWolfEntity;
import com.william.betterpets.gui.container.StorageContainer;
import com.william.betterpets.util.Reference;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public class StorageScreen extends ContainerScreen<StorageContainer>
{
    //private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.ID + ":textures/gui/better_wolf_old.png");
    //private static final ResourceLocation BUTTON_ICONS = new ResourceLocation(Reference.ID + ":textures/gui/icons.png");
    
    private BetterWolfEntity theWolf;
    
    private float screenPositionX;
    private float screenPositionY;
    
    //private GuiImageButton trainPet;
    
    @SuppressWarnings("ConstantConditions")
    public StorageScreen(StorageContainer container, @Nonnull PlayerInventory playerInventory, @Nonnull BetterWolfEntity theWolf)
    {
        super(container, playerInventory, theWolf.getDisplayName());
        //this.theWolf.hasCustomName() ? this.theWolf.getCustomName().getString() : I18n.format("entity.Wolf.name");
        //this.wolfArmor = theWolf.getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
        this.theWolf = theWolf;
        this.passEvents = false;
        
        //this.xSize = 209;
    }
    
    /*@Override
    public void init()
    {
        super.init();
        this.buttons.add(trainPet = new GuiImageButton(this.guiLeft + 5, this.height / 2 - 30, 0, 0, 18, 18, BUTTON_ICONS, (action) -> {
            System.out.println("clicked lol");
        }));
        //this.buttonList.add(trainPet = new GuiImageButton(this.guiLeft + 104, this.height / 2 - 22, 91, 233, 14, 14, new ResourceLocation("textures/gui/container/beacon.png")));
    }*/
    
    
    
    /*@Override
    protected void actionPerformed(Button button) throws IOException
    {
        if(button == trainPet)
        {
            System.out.println("clicked lol");
            //this.theWolf.getAiLayDown().setLayingDown(!this.theWolf.isLayingDown());
        }
        else
        {
            System.out.println("I'm crying");
        }
    }*/
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String wolfName = this.title.getFormattedText();
        
        /*this.font.drawString(this.wolfInventory.hasCustomName()
                ? this.wolfInventory.getName()
                : I18n.format(this.wolfInventory.getName(), wolfName), 8, 6, 0x404040);*/
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 94, 0x404040);
        
        //this.fontRenderer.drawString("Armor", 300, 50, 0x404040);
        //this.fontRenderer.drawString("Chest", 300, 80, 0x404040);
        
        //this.drawWolfHealthAndArmor();
        
        this.drawHealth();
        
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
    
    private void drawHealth()
    {
        GlStateManager.color4f(1, 1, 1, 1);
        
        this.minecraft.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
        
        int health = (int)Math.ceil(this.theWolf.getHealth());
        float maxHealth = this.theWolf.getMaxHealth();
        int rowOffset = (int)Math.min((maxHealth + 0.5F) / 2, 30);
        int yPos = 5;
        for(int columOffset = 0; rowOffset > 0; columOffset += 20)
        {
            int rowIterate = Math.min(rowOffset, 10);
            rowOffset -= rowIterate;
            
            for(int row = rowIterate - 1; row >= 0; row--)
            {
                int xPos = (this.xSize / 2) + row * 8 - 4;
                
                this.blit(xPos, yPos, 16, 0, 9, 9);
                
                if(row * 2 + 1 + columOffset < health)
                {
                    this.blit(xPos, yPos, 52, 0, 9, 9);
                }
                if(row * 2 + 1 + columOffset == health)
                {
                    this.blit(xPos, yPos, 61, 0, 9, 9);
                }
            }
        }
    }
    
    /**
     * Draws the screen and all components in it.
     *
     * @param mouseX The X position of the mouse
     * @param mouseY The Y position of the mouse
     * @param partialTicks The tick state of the client.
     */
    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.screenPositionX = (float)mouseX;
        this.screenPositionY = (float)mouseY;
        super.render(mouseX, mouseY, partialTicks);
    
        /*for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1)
        {
            Slot slot = this.inventorySlots.inventorySlots.get(i1);
        
            if (slot.isEnabled())
            {
                this.drawSlot(slot);
            }
        
            if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.isEnabled())
            {
                this.hoveredSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                int j1 = slot.xPos;
                int k1 = slot.yPos;
                GlStateManager.colorMask(true, true, true, false);
                this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }*/
        
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    
    
    /*public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    
    }*/
    
    /**
     * Draws the background layer of the inventory.
     *
     * @param partialTicks The tick state of the client
     * @param mouseX The X position of the mouse
     * @param mouseY The Y position of the mouse
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1, 1, 1, 1);
        
        this.minecraft.getTextureManager().bindTexture(TEXTURES);
        
        int positionX = (this.width - this.xSize) / 2;
        int positionY = (this.height - this.ySize) / 2;
        this.blit(positionX, positionY, 0, 0, this.xSize, this.ySize);
        
        if(/*this.wolfArmor.getHasArmor()*/true)
        {
            this.blit(positionX + 7, positionY + 17, this.xSize, 36, 18, 18);
        }
        
        if(/*this.wolfArmor.getHasChest()*/theWolf.hasChest())
        {
            //this.drawTexturedModalRect(positionX + 97, positionY + 17, this.xSize, 0, 54, 36);
            this.blit(positionX + 79, positionY + 17, 0, this.ySize, 5 * 18, 54);
        }
        
        InventoryScreen.drawEntityOnScreen(positionX + 51, positionY + 60, 30, (float)(positionX + 51) - this.screenPositionX, (float)(positionY + 42) - this.screenPositionY, this.theWolf);
    }
}