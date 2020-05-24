package com.kingparity.betterpets.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class FluidUtils
{
    private static final Map<ResourceLocation, Integer> CACHE_FLUID_COLOR = new HashMap<>();

    @OnlyIn(Dist.CLIENT)
    public static void clearCacheFluidColor()
    {
        CACHE_FLUID_COLOR.clear();
    }

    @OnlyIn(Dist.CLIENT)
    public static int getAverageFluidColor(Fluid fluid)
    {
        int fluidColor = -1;
        Integer colorCashed = CACHE_FLUID_COLOR.get(fluid.getRegistryName());
        if (colorCashed != null )
        {
            fluidColor = colorCashed;
        }
        else
        {
            TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(fluid.getFluid().getAttributes().getStillTexture());
            if(sprite != null)
            {
                long totalRed = 0;
                long totalGreen = 0;
                long totalBlue = 0;
                int pixelCount = sprite.getWidth() * sprite.getHeight();
                int red, green, blue;
                for(int i = 0; i < sprite.getHeight(); i++)
                {
                    for(int j = 0; j < sprite.getWidth(); j++)
                    {
                        int color = sprite.getPixelRGBA(0, j, i);
                        red = color & 255;
                        green = color >> 8 & 255;
                        blue = color >> 16 & 255;
                        totalRed += red * red;
                        totalGreen += green * green;
                        totalBlue += blue * blue;
                    }
                }
                fluidColor = (((int) Math.sqrt(totalRed / pixelCount) & 255) << 16)
                        | (((int) Math.sqrt(totalGreen / pixelCount) & 255) << 8) | (((int) Math.sqrt(totalBlue / pixelCount) & 255));
            }
            CACHE_FLUID_COLOR.put(fluid.getRegistryName(), fluidColor);
        }
        return fluidColor;
    }

    public static int transferFluid(IFluidHandler source, IFluidHandler target, int maxAmount)
    {
        FluidStack drained = source.drain(maxAmount, IFluidHandler.FluidAction.SIMULATE);
        if(drained.getAmount() > 0)
        {
            int filled = target.fill(drained, IFluidHandler.FluidAction.SIMULATE);
            if(filled > 0)
            {
                drained = source.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                return target.fill(drained, IFluidHandler.FluidAction.EXECUTE);
            }
        }
        return 0;
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawFluidTankInGUI(FluidStack fluid, float x, float y, float percent, int height, World world, BlockPos pos)
    {
        if(fluid == null || fluid.isEmpty())
        {
            return;
        }

        TextureAtlasSprite sprite = ForgeHooksClient.getFluidSprites(world, pos, fluid.getFluid().getDefaultState())[0];

        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();
        float deltaV = maxV - minV;
        float tankLevel = percent * height;

        int count = 1 + ((int) Math.ceil(tankLevel)) / 16;
        for(int i = 0; i < count; i++)
        {
            float subHeight = Math.min(16.0F, tankLevel - (16.0F * i));
            float offsetY = height - 16.0F * i - subHeight;
            drawQuad(fluid, x, y + offsetY, 16, subHeight, minU, (float) (maxV - deltaV * (subHeight / 16.0)), maxU, maxV, world, pos);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void drawQuad(FluidStack fluid, float x, float y, float width, float height, float minU, float minV, float maxU, float maxV, World world, BlockPos pos)
    {
        IVertexBuilder buffer = Tessellator.getInstance().getBuffer().getVertexBuilder();
        Matrix4f matrix = new MatrixStack().getLast().getMatrix();
        int fluidColor = fluid.getFluid().getAttributes().getColor(world, pos);
        float red = (float)(fluidColor >> 16 & 255) / 255.0F;
        float green = (float)(fluidColor >> 8 & 255) / 255.0F;
        float blue = (float)(fluidColor & 255) / 255.0F;

        buffer.pos(matrix, x, y + height, 0).color(red, green, blue, 1.0F).tex(minU, maxV).endVertex();
        buffer.pos(matrix, x + width, y + height, 0).color(red, green, blue, 1.0F).tex(maxU, maxV).endVertex();
        buffer.pos(matrix, x + width, y, 0).color(red, green, blue, 1.0F).tex(maxU, minV).endVertex();
        buffer.pos(matrix, x, y, 0).color(red, green, blue, 1.0F).tex(minU, minV).endVertex();
    }
}