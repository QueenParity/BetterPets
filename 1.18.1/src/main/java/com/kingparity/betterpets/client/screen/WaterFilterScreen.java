package com.kingparity.betterpets.client.screen;

import com.google.common.collect.Lists;
import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.blockentity.WaterFilterBlockEntity;
import com.kingparity.betterpets.init.ModFluids;
import com.kingparity.betterpets.inventory.menu.WaterFilterMenu;
import com.kingparity.betterpets.util.FluidUtils;
import com.kingparity.betterpets.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Collections;

public class WaterFilterScreen extends AbstractContainerScreen<WaterFilterMenu>
{
    private static final ResourceLocation GUI = new ResourceLocation(BetterPets.ID, "textures/gui/water_filter.png");
    
    private Inventory playerInventory;
    private WaterFilterBlockEntity waterFilter;
    
    public WaterFilterScreen(WaterFilterMenu container, Inventory playerInventory, Component title)
    {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.waterFilter = container.getWaterFilter();
        this.imageWidth = 176;
        this.imageHeight = 180;
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;
        
        if(this.waterFilter.getWaterFluidStack() != null)
        {
            FluidStack stack = this.waterFilter.getWaterFluidStack();
            if(this.isMouseWithinRegion(startX + 53, startY + 20, 16, 59, mouseX, mouseY))
            {
                if(stack.getAmount() > 0)
                {
                    this.renderTooltip(matrixStack, Lists.transform(Arrays.asList(new TextComponent(stack.getDisplayName().getString()), new TextComponent(ChatFormatting.GRAY.toString() + this.waterFilter.getWaterLevel() + "/" + this.waterFilter.getWaterTank().getCapacity() + " mB")), Component::getVisualOrderText), mouseX, mouseY);
                }
                else
                {
                    this.renderTooltip(matrixStack, Lists.transform(Collections.singletonList(new TextComponent("No Fluid")), Component::getVisualOrderText), mouseX, mouseY);
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
                    this.renderTooltip(matrixStack, Lists.transform(Arrays.asList(new TextComponent(stack.getDisplayName().getString()), new TextComponent(ChatFormatting.GRAY.toString() + this.waterFilter.getFilteredWaterLevel() + "/" + this.waterFilter.getFilteredWaterTank().getCapacity() + " mB")), Component::getVisualOrderText), mouseX, mouseY);
                }
                else
                {
                    this.renderTooltip(matrixStack, Lists.transform(Collections.singletonList(new TextComponent("No Fluid")), TextComponent::getVisualOrderText), mouseX, mouseY);
                }
            }
        }
        
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
    {
        this.minecraft.font.draw(poseStack, this.waterFilter.getDisplayName().getString(), 8, 6, 4210752);
        this.minecraft.font.draw(poseStack, this.playerInventory.getDisplayName().getString(), 8, this.imageHeight - 96 + 2, 4210752);
    }
    
    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;
        
        RenderSystem.setShaderTexture(0, GUI);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        this.blit(poseStack, startX, startY, 0, 0, this.imageWidth, this.imageHeight);
        
        if(this.waterFilter.getRemainingFuel() >= 0)
        {
            int remainingFuel = (int) (14 * (this.waterFilter.getRemainingFuel() / (double) this.waterFilter.getFuelMaxProgress()));
            this.blit(poseStack, startX + 29, startY + 35 + 14 - remainingFuel, 176, 14 - remainingFuel, 14, remainingFuel + 1);
        }
        
        int waterColor = Fluids.WATER.getSource().getAttributes().getColor(this.waterFilter.getLevel(), this.waterFilter.getBlockPos());
        int filteredWaterColor = ModFluids.FILTERED_WATER.get().getAttributes().getColor(this.waterFilter.getLevel(), this.waterFilter.getBlockPos());
        
        if(this.waterFilter.canFilterClient())
        {
            int left = startX + 71;
            int top = startY + 36;
            int right = left + 58;
            int bottom = top + 26;
            double filterPercentage = this.waterFilter.getFilterProgress() / (double) 58;
            double percentage = Mth.clamp(filterPercentage, 0, 1);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderUtil.drawGradientRectHorizontal(left, top, right, bottom, waterColor, filteredWaterColor);
            this.blit(poseStack, left, top, 176, 14, 58, 26);
            int filterProgress = (int) (58 * percentage);
            this.blit(poseStack, left + filterProgress, top, 71 + filterProgress, 36, 58 - filterProgress, 26);
        }
        
        this.drawFluidTank(this.waterFilter.getWaterFluidStack(), poseStack, startX + 53, startY + 20, this.waterFilter.getWaterLevel() / (double) this.waterFilter.getWaterTank().getCapacity(), waterColor);
        this.drawFluidTank(this.waterFilter.getFilteredWaterFluidStack(), poseStack, startX + 131, startY + 20, this.waterFilter.getFilteredWaterLevel() / (double) this.waterFilter.getFilteredWaterTank().getCapacity(), filteredWaterColor);
    }
    
    private void drawFluidTank(FluidStack fluid, PoseStack poseStack, int x, int y, double level, int color)
    {
        FluidUtils.drawFluidTankInGUI(fluid, x, y, level, 59, color);
        RenderSystem.setShaderTexture(0, GUI);
        this.blit(poseStack, x, y, 176, 44, 16, 59);
    }
    
    private boolean isMouseWithinRegion(int x, int y, int width, int height, int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
    
    private int getCombinedLight(BlockAndTintGetter reader, BlockPos pos)
    {
        int i = LevelRenderer.getLightColor(reader, pos);
        int j = LevelRenderer.getLightColor(reader, pos.above());
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
    }
}
