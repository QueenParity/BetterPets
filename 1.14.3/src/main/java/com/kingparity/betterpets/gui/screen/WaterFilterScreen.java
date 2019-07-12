package com.kingparity.betterpets.gui.screen;

import com.kingparity.betterpets.gui.container.WaterFilterContainer;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.FluidUtils;
import com.kingparity.betterpets.util.Reference;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class WaterFilterScreen extends ContainerScreen<WaterFilterContainer>
{
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(Reference.ID + ":textures/gui/water_filter.png");
    
    public WaterFilterScreen(WaterFilterContainer container, PlayerInventory playerInventory, ITextComponent title)
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
        
        TileEntity tileEntity = this.playerInventory.player.world.getTileEntity(this.getContainer().getPos());
        WaterFilterTileEntity waterFilter = tileEntity instanceof WaterFilterTileEntity ? (WaterFilterTileEntity)tileEntity : null;
        if(waterFilter != null)
        {
            int startX = (this.width - this.xSize) / 2;
            int startY = (this.height - this.ySize) / 2;
            this.drawWaterTank(waterFilter, startX, startY, waterFilter.getFluidAmount(1) / (waterFilter.getCapacity() / 2), false);
        }
        else if(tileEntity != null)
        {
            System.out.println(tileEntity.getType().getRegistryName());
        }
        else
        {
            System.out.println(this.getContainer().getPos());
        }
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
    
    private void drawWaterTank(WaterFilterTileEntity waterFilter, int x, int y, double level, boolean isFiltered)
    {
        if(isFiltered)
        {
            FluidUtils.drawFluidTankInGUI(waterFilter, x, y, level, 59, 4159204);
        }
        else
        {
            FluidUtils.drawFluidTankInGUI(waterFilter, x, y, level, 59, 5628096);
        }
        Minecraft.getInstance().getTextureManager().bindTexture(GUI_TEXTURES);
        this.blit(x, y, 176, 18, 16, 59);
    }
}