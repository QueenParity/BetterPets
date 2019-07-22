package com.kingparity.betterpets.gui.screen;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.gui.container.BetterWolfContainer;
import com.kingparity.betterpets.util.Reference;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BetterWolfScreen extends ContainerScreen<BetterWolfContainer>
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.ID + ":textures/gui/better_wolf_old.png");
    public static final ResourceLocation THIRST_BAR_ICONS = new ResourceLocation(Reference.ID + ":textures/gui/thirst_bar.png");
    private final BetterWolfEntity theWolf;
    private float mousePosx;
    private float mousePosy;
    
    public BetterWolfScreen(BetterWolfContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        this.passEvents = false;
        this.theWolf = this.getContainer().getTheWolf();
    }
    
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 94, 0x404040);
        
        drawHealth();
        drawHunger();
        drawThirst();
    }
    
    private void drawHealth()
    {
        GlStateManager.pushMatrix();
        {
            GlStateManager.color4f(1, 1, 1, 1);
        
            this.minecraft.getTextureManager().bindTexture(Screen.GUI_ICONS_LOCATION);
        
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
        GlStateManager.popMatrix();
    }
    
    private void drawHunger()
    {
        GlStateManager.pushMatrix();
        {
            GlStateManager.color4f(1, 1, 1, 1);
            
            this.minecraft.getTextureManager().bindTexture(Screen.GUI_ICONS_LOCATION);
            
            int hunger = (int)Math.ceil(this.theWolf.getPetFoodStats().getFoodLevel());
            float maxHunger = 20.0F;
            int rowOffset = (int)Math.min((maxHunger + 0.5F) / 2, 30);
            int yPos = 5 - 16;
            for(int columOffset = 0; rowOffset > 0; columOffset += 20)
            {
                int rowIterate = Math.min(rowOffset, 10);
                rowOffset -= rowIterate;
                
                for(int row = rowIterate - 1; row >= 0; row--)
                {
                    int xPos = (this.xSize / 2) + row * 8 - 4;
                    
                    this.blit(xPos, yPos, 16, 27, 9, 9);
                    
                    if(row * 2 + 1 + columOffset < hunger)
                    {
                        this.blit(xPos, yPos, 52, 27, 9, 9);
                    }
                    if(row * 2 + 1 + columOffset == hunger)
                    {
                        this.blit(xPos, yPos, 61, 27, 9, 9);
                    }
                }
            }
        }
        GlStateManager.popMatrix();
    }
    
    private void drawThirst()
    {
        GlStateManager.pushMatrix();
        {
            GlStateManager.color4f(1, 1, 1, 1);
    
            this.minecraft.getTextureManager().bindTexture(THIRST_BAR_ICONS);
            
            int thirstLevel = this.theWolf.getPetThirstStats().thirstLevel;
            int startX = ((this.xSize) / 2) - 4;
            int startY = 5 - 32;
            int poisonModifier = this.theWolf.getPetThirstStats().poisoned ? 16 : 0;
    
            for(int i = 0; i < 10; i++)
            {
                this.blit(startX + i*8, startY, 1, 1, 7, 9); //empty thirst droplet
                if(thirstLevel % 2 != 0 && 10 - i - 1 == thirstLevel / 2)
                {
                    this.blit(startX + i*8, startY, 17 + poisonModifier, 1, 7, 9);
                }
                else if(thirstLevel/2 >= 10 - i)
                {
                    this.blit(startX + i*8, startY, 9 + poisonModifier, 1, 7, 9);
                }
            }
        }
        GlStateManager.popMatrix();
    }
    
    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1, 1, 1, 1);
        this.minecraft.getTextureManager().bindTexture(TEXTURES);
        int positionX = (this.width - this.xSize) / 2;
        int positionY = (this.height - this.ySize) / 2;
        this.blit(positionX, positionY, 0, 0, this.xSize, this.ySize);
        
        this.blit(positionX + 7, positionY + 17, this.xSize, 36, 18, 18);
        
        if(/*this.wolfArmor.getHasChest()*/theWolf.hasChest())
        {
            this.blit(positionX + 79, positionY + 17, 0, this.ySize, 5 * 18, 54);
        }
        
        InventoryScreen.drawEntityOnScreen(positionX + 51, positionY + 60, 30, (float)(positionX + 51) - this.mousePosx, (float)(positionY + 42) - this.mousePosy, this.theWolf);
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.mousePosx = (float)mouseX;
        this.mousePosy = (float)mouseY;
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}