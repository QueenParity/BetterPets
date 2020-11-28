package com.kingparity.betterpets.client.screen;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.inventory.container.PetChestContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BetterWolfScreen extends ContainerScreen<PetChestContainer>
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(BetterPets.ID + ":textures/gui/better_wolf.png");
    private static final ResourceLocation THIRST_BAR_ICONS = new ResourceLocation(BetterPets.ID + ":textures/gui/thirst_bar.png");
    private final BetterWolfEntity betterWolf;
    private final PlayerInventory playerInventory;
    private float mousePosX;
    private float mousePosY;
    
    public BetterWolfScreen(PetChestContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        this.betterWolf = this.getContainer().getBetterWolf();
        this.playerInventory = playerInventory;
        this.passEvents = false;
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        this.mousePosX = (float)mouseX;
        this.mousePosY = (float)mouseY;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y)
    {
        this.minecraft.fontRenderer.drawString(matrixStack, this.getTitle().getString(), 8, 6, 4210752);
        this.minecraft.fontRenderer.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), 8, this.ySize - 94, 4210752);
        
        drawHealth(matrixStack);
        drawHunger(matrixStack);
        drawThirst(matrixStack);
    }
    
    private void drawHealth(MatrixStack matrixStack)
    {
        RenderSystem.pushMatrix();
        {
            RenderSystem.color4f(1, 1, 1, 1);
            
            this.minecraft.getTextureManager().bindTexture(Screen.GUI_ICONS_LOCATION);
            
            int health = (int)Math.ceil(this.betterWolf.getHealth());
            float maxHealth = this.betterWolf.getMaxHealth();
            int rowOffset = (int)Math.min((maxHealth + 0.5F) / 2, 30);
            int yPos = 5;
            for(int columOffset = 0; rowOffset > 0; columOffset += 20)
            {
                int rowIterate = Math.min(rowOffset, 10);
                rowOffset -= rowIterate;
                
                for(int row = rowIterate - 1; row >= 0; row--)
                {
                    int xPos = (this.xSize / 2) + row * 8 - 4;
                    
                    this.blit(matrixStack, xPos, yPos, 16, 0, 9, 9);
                    
                    if(row * 2 + 1 + columOffset < health)
                    {
                        this.blit(matrixStack, xPos, yPos, 52, 0, 9, 9);
                    }
                    if(row * 2 + 1 + columOffset == health)
                    {
                        this.blit(matrixStack, xPos, yPos, 61, 0, 9, 9);
                    }
                }
            }
        }
        RenderSystem.popMatrix();
    }
    
    private void drawHunger(MatrixStack matrixStack)
    {
        RenderSystem.pushMatrix();
        {
            RenderSystem.color4f(1, 1, 1, 1);
            
            this.minecraft.getTextureManager().bindTexture(Screen.GUI_ICONS_LOCATION);
            
            int hunger = (int)Math.ceil(this.betterWolf.getPetFoodStats().getFoodLevel());
            int yPos = 5 - 16;
            
            for(int i = 0; i < 10; i++)
            {
                int xPos = (this.xSize / 2) + i * 8 - 4;
                
                this.blit(matrixStack, xPos, yPos, 16, 27, 9, 9);
                
                if(hunger % 2 != 0 && 10 - i - 1 == hunger / 2)
                {
                    this.blit(matrixStack, xPos, yPos, 61, 27, 9, 9);
                }
                else if(hunger / 2 >= 10 - i)
                {
                    this.blit(matrixStack, xPos, yPos, 52, 27, 9, 9);
                }
            }
        }
        RenderSystem.popMatrix();
    }
    
    private void drawThirst(MatrixStack matrixStack)
    {
        RenderSystem.pushMatrix();
        {
            RenderSystem.color4f(1, 1, 1, 1);
            
            this.minecraft.getTextureManager().bindTexture(THIRST_BAR_ICONS);
            
            int thirstLevel = this.betterWolf.getPetThirstStats().thirstLevel;
            int startX = ((this.xSize) / 2) - 4;
            int startY = 5 - 32;
            int poisonModifier = this.betterWolf.getPetThirstStats().poisoned ? 16 : 0;
            
            for(int i = 0; i < 10; i++)
            {
                this.blit(matrixStack, startX + i*8, startY, 1, 1, 7, 9); //empty thirst droplet
                if(thirstLevel % 2 != 0 && 10 - i - 1 == thirstLevel / 2)
                {
                    this.blit(matrixStack, startX + i*8, startY, 17 + poisonModifier, 1, 7, 9);
                }
                else if(thirstLevel/2 >= 10 - i)
                {
                    this.blit(matrixStack, startX + i*8, startY, 9 + poisonModifier, 1, 7, 9);
                }
            }
        }
        RenderSystem.popMatrix();
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURES);
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, startX, startY, 0, 0, this.xSize, this.ySize);
        this.blit(matrixStack, startX + 7, startY + 17, this.xSize, 36, 18, 18);
        
        if(this.betterWolf.hasChest())
        {
            this.blit(matrixStack, startX + 79, startY + 17, 0, this.ySize, 5 * 18, 54);
        }
        
        InventoryScreen.drawEntityOnScreen(startX + 51, startY + 60, 30, (float)(startX + 51) - this.mousePosX, (float)(startY + 42) - this.mousePosY, this.betterWolf);
    }
}