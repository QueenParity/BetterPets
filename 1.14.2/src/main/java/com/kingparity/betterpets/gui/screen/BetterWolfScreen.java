package com.kingparity.betterpets.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.gui.container.BetterWolfContainer;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BetterWolfScreen extends ContainerScreen<BetterWolfContainer>
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.ID + ":textures/gui/better_wolf_old.png");
    private final BetterWolfEntity theWolf;
    private float mousePosx;
    private float mousePosy;
    
    public BetterWolfScreen(BetterWolfContainer container, PlayerInventory playerInventory, BetterWolfEntity theWolf)
    {
        super(container, playerInventory, theWolf.getDisplayName());
        this.theWolf = theWolf;
        this.passEvents = false;
    }
    
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 94, 0x404040);
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