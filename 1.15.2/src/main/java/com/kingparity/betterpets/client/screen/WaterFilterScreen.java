package com.kingparity.betterpets.client.screen;

import com.kingparity.betterpets.core.ModFluids;
import com.kingparity.betterpets.inventory.container.WaterFilterContainer;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.FluidUtils;
import com.kingparity.betterpets.util.Reference;
import com.kingparity.betterpets.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Collections;

public class WaterFilterScreen extends ContainerScreen<WaterFilterContainer>
{
    private static final ResourceLocation GUI = new ResourceLocation(Reference.ID, "textures/gui/water_filter.png");

    private PlayerInventory playerInventory;
    private WaterFilterTileEntity waterFilterTileEntity;

    public WaterFilterScreen(WaterFilterContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.waterFilterTileEntity = container.getWaterFilter();
        this.xSize = 176;
        this.ySize = 180;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        if(this.waterFilterTileEntity.getWaterFluidStack() != null)
        {
            FluidStack stack = this.waterFilterTileEntity.getWaterFluidStack();
            if(this.isMouseWithinRegion(startX + 33, startY + 20, 16, 59, mouseX, mouseY))
            {
                if(stack.getAmount() > 0)
                {
                    this.renderTooltip(Arrays.asList(stack.getDisplayName().getFormattedText(), TextFormatting.GRAY.toString() + this.waterFilterTileEntity.getWaterLevel() + "/" + this.waterFilterTileEntity.getWaterTank().getCapacity() + " mB"), mouseX, mouseY);
                }
                else
                {
                    this.renderTooltip(Collections.singletonList("No Fluid"), mouseX, mouseY);
                }
            }
        }

        if(this.waterFilterTileEntity.getFilteredWaterFluidStack() != null)
        {
            FluidStack stack = this.waterFilterTileEntity.getFilteredWaterFluidStack();
            if(this.isMouseWithinRegion(startX + 151, startY + 20, 16, 59, mouseX, mouseY))
            {
                if(stack.getAmount() > 0)
                {
                    this.renderTooltip(Arrays.asList(stack.getDisplayName().getFormattedText(), TextFormatting.GRAY.toString() + this.waterFilterTileEntity.getFilteredWaterLevel() + "/" + this.waterFilterTileEntity.getFilteredWaterTank().getCapacity() + " mB"), mouseX, mouseY);
                }
                else
                {
                    this.renderTooltip(Collections.singletonList("No Fluid"), mouseX, mouseY);
                }
            }
        }

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.minecraft.fontRenderer.drawString(this.waterFilterTileEntity.getDisplayName().getFormattedText(), 8, 6, 4210752);
        this.minecraft.fontRenderer.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(startX, startY, 0, 0, this.xSize, this.ySize);

        if(this.waterFilterTileEntity.getRemainingFilteredWater() >= 0)
        {
            int remainingFuel = (int) (14 * (this.waterFilterTileEntity.getRemainingFilteredWater() / (double) this.waterFilterTileEntity.getFilterMaxProgress()));
            this.blit(startX + 9, startY + 31 + 14 - remainingFuel, 176, 14 - remainingFuel, 14, remainingFuel + 1);
        }

        if(this.waterFilterTileEntity.canFilter())
        {
            int waterColorRGB = this.waterFilterTileEntity.getWaterFluidStack().getFluid().getAttributes().getColor(this.waterFilterTileEntity.getWorld(), this.waterFilterTileEntity.getPos());
            int filteredWaterColorRGB = FluidUtils.getAverageFluidColor(this.waterFilterTileEntity.getFilteredWaterFluidStack().getFluid());
            int waterColor = (130 << 24) | waterColorRGB;
            int filteredWaterColor = (130 << 24) | filteredWaterColorRGB;
            double extractionPercentage = this.waterFilterTileEntity.getFilterProgress() / (double) 100;

            int left = startX + 51;
            int top = startY + 36;
            int right = left + 98;
            int bottom = top + 26;
            double percentageItem = MathHelper.clamp(extractionPercentage, 0, 1);
            RenderUtil.drawGradientRectHorizontal(left, top, right, bottom, waterColor, filteredWaterColor);
            this.blit(left, top, 0, 180, 98, 26);
            int extractionProgress = (int) (98 * percentageItem + 1);
            this.blit(left + extractionProgress, top, 51 + extractionProgress, 36, 98 - extractionProgress, 26);
        }

        this.drawFluidTank(this.waterFilterTileEntity.getWaterFluidStack(), startX + 33, startY + 20, this.waterFilterTileEntity.getWaterLevel() / (double) this.waterFilterTileEntity.getWaterTank().getCapacity(), this.waterFilterTileEntity.getWorld(), this.waterFilterTileEntity.getPos());
        this.drawFluidTank(this.waterFilterTileEntity.getFilteredWaterFluidStack(), startX + 151, startY + 20, this.waterFilterTileEntity.getFilteredWaterLevel() / (double) this.waterFilterTileEntity.getFilteredWaterTank().getCapacity(), this.waterFilterTileEntity.getWorld(), this.waterFilterTileEntity.getPos());
    }

    private void drawFluidTank(FluidStack fluid, int x, int y, double level, World world, BlockPos pos)
    {
        FluidUtils.drawFluidTankInGUI(fluid, x, y, (float)level, 59, world, pos);
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(x, y, 176, 44, 16, 59);
    }

    private void drawSmallFluidTank(FluidStack fluid, int x, int y, double level, World world, BlockPos pos)
    {
        FluidUtils.drawFluidTankInGUI(fluid, x, y, (float)level, 29, world, pos);
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(x, y, 176, 44, 16, 29);
    }

    private boolean isMouseWithinRegion(int x, int y, int width, int height, int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
