package com.kingparity.betterpets.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class FluidUtils
{
    @OnlyIn(Dist.CLIENT)
    public static void drawFluidTankInGUI(FluidStack fluid, double x, double y, double percent, int height)
    {
        if(fluid == null)
            return;
        
        ResourceLocation resource = fluid.getFluid().getStill();
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(resource.toString());
        if(sprite != null)
        {
            double minU = sprite.getMinU();
            double maxU = sprite.getMaxU();
            double minV = sprite.getMinV();
            double maxV = sprite.getMaxV();
            double deltaV = maxV - minV;
            double tankLevel = percent * height;
            
            Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            
            GlStateManager.enableBlend();
            
            int count = 1 + ((int) Math.ceil(tankLevel)) / 16;
            for(int i = 0; i < count; i++)
            {
                double subHeight = Math.min(16.0, tankLevel - (16.0 * i));
                double offsetY = height - 16.0 * i - subHeight;
                drawQuad(x, y + offsetY, 16, subHeight, minU, maxV - deltaV * (subHeight / 16.0), maxU, maxV);
            }
            
            GlStateManager.disableBlend();
        }
    }
    
    private static void drawQuad(double x, double y, double width, double height, double minU, double minV, double maxU, double maxV)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0).tex(minU, maxV).endVertex();
        buffer.pos(x + width, y + height, 0).tex(maxU, maxV).endVertex();
        buffer.pos(x + width, y, 0).tex(maxU, minV).endVertex();
        buffer.pos(x, y, 0).tex(minU, minV).endVertex();
        tessellator.draw();
    }
    
    public static void fixEmptyTag(CompoundNBT tag)
    {
        if(tag.contains("FluidName", Constants.NBT.TAG_STRING) && tag.contains("Amount", Constants.NBT.TAG_INT))
        {
            tag.remove("Empty");
        }
    }
}
