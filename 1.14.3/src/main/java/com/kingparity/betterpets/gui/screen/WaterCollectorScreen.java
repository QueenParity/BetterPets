package com.kingparity.betterpets.gui.screen;

import com.kingparity.betterpets.gui.container.WaterCollectorContainer;
import com.kingparity.betterpets.tileentity.WaterCollectorTileEntity;
import com.kingparity.betterpets.util.FluidUtils;
import com.kingparity.betterpets.util.Reference;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class WaterCollectorScreen extends ContainerScreen<WaterCollectorContainer>
{
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(Reference.ID + ":textures/gui/water_collector.png");
    
    public WaterCollectorScreen(WaterCollectorContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
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
        
        //WaterCollectorTileEntity waterCollector = (WaterCollectorTileEntity)this.playerInventory.player.world.getTileEntity(this.getContainer().getPos());
        //this.drawWaterTank(waterCollector.getWaterFluidStack(), (this.xSize / 2) + 15, 17, waterCollector.getRemainingWater() / 10000);
    }
    
    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        this.blit(startX, startY, 0, 0, this.xSize, this.ySize);
    }
    
    private void drawWaterTank(FluidStack fluid, int x, int y, double level)
    {
        FluidUtils.drawFluidTankInGUI(fluid, x, y, level, 60);
        Minecraft.getInstance().getTextureManager().bindTexture(GUI_TEXTURES);
        this.blit(x, y, 176, 0, 16, 60);
    }
}