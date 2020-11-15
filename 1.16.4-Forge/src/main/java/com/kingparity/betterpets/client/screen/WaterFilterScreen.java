package com.kingparity.betterpets.client.screen;

import com.google.common.collect.Lists;
import com.kingparity.betterpets.init.ModFluids;
import com.kingparity.betterpets.inventory.container.WaterFilterContainer;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.FluidUtils;
import com.kingparity.betterpets.util.Reference;
import com.kingparity.betterpets.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Collections;

public class WaterFilterScreen extends ContainerScreen<WaterFilterContainer>
{
    private static final ResourceLocation GUI = new ResourceLocation(Reference.ID + ":textures/gui/water_filter.png");
    
    private PlayerInventory playerInventory;
    private WaterFilterTileEntity waterFilter;
    
    public WaterFilterScreen(WaterFilterContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.waterFilter = container.getWaterFilter();
        this.xSize = 176;
        this.ySize = 180;
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        
        if(this.waterFilter.getWaterFluidStack() != null)
        {
            FluidStack stack = this.waterFilter.getWaterFluidStack();
            if(this.isMouseWithinRegion(startX + 53, startY + 20, 16, 59, mouseX, mouseY))
            {
                if(stack.getAmount() > 0)
                {
                    this.renderTooltip(matrixStack, Lists.transform(Arrays.asList(new StringTextComponent(stack.getDisplayName().getString()), new StringTextComponent(TextFormatting.GRAY.toString() + this.waterFilter.getWaterLevel() + "/" + this.waterFilter.getWaterTank().getCapacity() + " mB")), ITextComponent::func_241878_f), mouseX, mouseY);
                }
                else
                {
                    this.renderTooltip(matrixStack, Lists.transform(Collections.singletonList(new StringTextComponent("No Fluid")), ITextComponent::func_241878_f), mouseX, mouseY);
                }
            }
        }
    
        if(this.waterFilter.getFilteredWaterFluidStack() != null)
        {
            FluidStack stack = this.waterFilter.getFilteredWaterFluidStack();
            if(this.isMouseWithinRegion(startX + 131, startY + 20, 16, 59, mouseX, mouseY))
            {
                if(stack.getAmount() > 0)
                {
                    this.renderTooltip(matrixStack, Lists.transform(Arrays.asList(new StringTextComponent(stack.getDisplayName().getString()), new StringTextComponent(TextFormatting.GRAY.toString() + this.waterFilter.getFilteredWaterLevel() + "/" + this.waterFilter.getFilteredWaterTank().getCapacity() + " mB")), ITextComponent::func_241878_f), mouseX, mouseY);
                }
                else
                {
                    this.renderTooltip(matrixStack, Lists.transform(Collections.singletonList(new StringTextComponent("No Fluid")), ITextComponent::func_241878_f), mouseX, mouseY);
                }
            }
        }
        
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        this.minecraft.fontRenderer.drawString(matrixStack, this.waterFilter.getDisplayName().getString(), 8, 6, 4210752);
        this.minecraft.fontRenderer.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), 8, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(matrixStack, startX, startY, 0, 0, this.xSize, this.ySize);
    
        if(this.waterFilter.getRemainingFuel() >= 0)
        {
            int remainingFuel = (int) (14 * (this.waterFilter.getRemainingFuel() / (double) this.waterFilter.getFuelMaxProgress()));
            this.blit(matrixStack, startX + 29, startY + 35 + 14 - remainingFuel, 176, 14 - remainingFuel, 14, remainingFuel + 1);
        }
    
        int waterColor = Fluids.WATER.getFluid().getAttributes().getColor(this.waterFilter.getWorld(), this.waterFilter.getPos());
        int filteredWaterColor = ModFluids.FILTERED_WATER.get().getAttributes().getColor(this.waterFilter.getWorld(), this.waterFilter.getPos());
        
        if(this.waterFilter.canFilterClient())
        {
            int left = startX + 71;
            int top = startY + 36;
            int right = left + 58;
            int bottom = top + 26;
            double filterPercentage = this.waterFilter.getFilterProgress() / (double) 58;
            double percentage = MathHelper.clamp(filterPercentage, 0, 1);
            RenderUtil.drawGradientRectHorizontal(left, top, right, bottom, waterColor, filteredWaterColor);
            this.blit(matrixStack, left, top, 176, 14, 58, 26);
            int filterProgress = (int) (58 * percentage);
            this.blit(matrixStack, left + filterProgress, top, 71 + filterProgress, 36, 58 - filterProgress, 26);
        }
        
        this.drawFluidTank(this.waterFilter.getWaterFluidStack(), matrixStack, startX + 53, startY + 20, this.waterFilter.getWaterLevel() / (double) this.waterFilter.getWaterTank().getCapacity(), waterColor);
        this.drawFluidTank(this.waterFilter.getFilteredWaterFluidStack(), matrixStack, startX + 131, startY + 20, this.waterFilter.getFilteredWaterLevel() / (double) this.waterFilter.getFilteredWaterTank().getCapacity(), filteredWaterColor);
    }
    
    private void drawFluidTank(FluidStack fluid, MatrixStack matrixStack, int x, int y, double level, int color)
    {
        FluidUtils.drawFluidTankInGUI(fluid, x, y, level, 59, color);
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(matrixStack, x, y, 176, 44, 16, 59);
    }
    
    private boolean isMouseWithinRegion(int x, int y, int width, int height, int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
    
    private int getCombinedLight(IBlockDisplayReader lightReader, BlockPos pos)
    {
        int i = WorldRenderer.getCombinedLight(lightReader, pos);
        int j = WorldRenderer.getCombinedLight(lightReader, pos.up());
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
    }
}