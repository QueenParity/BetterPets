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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collections;

public class WaterFilterScreen extends ContainerScreen<WaterFilterContainer>
{
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(Reference.ID + ":textures/gui/water_filter.png");
    
    public WaterFilterScreen(WaterFilterContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        this.ySize = 175;
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
    
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
    
        TileEntity tileEntity = this.playerInventory.player.world.getTileEntity(this.getContainer().getPos());
        WaterFilterTileEntity waterFilter = tileEntity instanceof WaterFilterTileEntity ? (WaterFilterTileEntity)tileEntity : null;
        if(waterFilter != null)
        {
            if(this.isMouseWithinRegion(startX + 53, startY + 18, 16, 59, mouseX, mouseY))
            {
                if(waterFilter.getFluidAmount(0) > 0)
                {
                    this.renderTooltip(Arrays.asList(new TranslationTextComponent("block.minecraft.water").getFormattedText(), TextFormatting.GRAY.toString() + waterFilter.getFluidAmount(0) + "/" + (waterFilter.getCapacity() / 2) + " mB"), mouseX, mouseY);
                }
                else
                {
                    this.renderTooltip(Collections.singletonList("No Fluid"), mouseX, mouseY);
                }
            }
            else if(this.isMouseWithinRegion(startX + 131, startY + 18, 16, 59, mouseX, mouseY))
            {
                if(waterFilter.getFluidAmount(1) > 0)
                {
                    this.renderTooltip(Arrays.asList(new TranslationTextComponent(String.format("block.%s.filtered_water", Reference.ID)).getFormattedText(), TextFormatting.GRAY.toString() + waterFilter.getFluidAmount(1) + "/" + (waterFilter.getCapacity() / 2) + " mB"), mouseX, mouseY);
                }
                else
                {
                    this.renderTooltip(Collections.singletonList(new TranslationTextComponent(String.format("%s.water_filter_info.no_fluid", Reference.ID)).getFormattedText()), mouseX, mouseY);
                }
            }
        }
        
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
    
        TileEntity tileEntity = this.playerInventory.player.world.getTileEntity(this.getContainer().getPos());
        WaterFilterTileEntity waterFilter = tileEntity instanceof WaterFilterTileEntity ? (WaterFilterTileEntity)tileEntity : null;
        if(waterFilter != null)
        {
            this.drawWaterTank(waterFilter, startX + 53, startY + 18, waterFilter.getFluidAmount(1) / (double)(waterFilter.getCapacity() / 2), false);
            this.drawWaterTank(waterFilter, startX + 131, startY + 18, waterFilter.getFluidAmount(0) / (double)(waterFilter.getCapacity() / 2), true);
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
    
    private void drawWaterTank(WaterFilterTileEntity waterFilter, int x, int y, double level, boolean isFiltered)
    {
        if(isFiltered)
        {
            FluidUtils.drawFluidTankInGUI(waterFilter, x, y, level, 59, 51455);
        }
        else
        {
            FluidUtils.drawFluidTankInGUI(waterFilter, x, y, level, 59, 4159204);
        }
        Minecraft.getInstance().getTextureManager().bindTexture(GUI_TEXTURES);
        this.blit(x, y, 176, 18, 16, 59);
    }
    
    private boolean isMouseWithinRegion(int x, int y, int width, int height, int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}