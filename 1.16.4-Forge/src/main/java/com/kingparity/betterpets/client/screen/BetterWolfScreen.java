package com.kingparity.betterpets.client.screen;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.inventory.container.PetChestContainer;
import com.kingparity.betterpets.util.Reference;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
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
    private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.ID + ":textures/gui/better_wolf.png");
    private final BetterWolfEntity betterWolf;
    private final PlayerInventory playerInventory;
    private final int inventoryRows;
    private float mousePosX;
    private float mousePosY;
    
    public BetterWolfScreen(PetChestContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        this.betterWolf = this.getContainer().getBetterWolf();
        this.playerInventory = playerInventory;
        this.passEvents = false;
        this.inventoryRows = container.getPetChestInventory().getSizeInventory() / 5;
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