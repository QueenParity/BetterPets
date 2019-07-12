package com.kingparity.betterpets.util;

import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

public class FluidUtils
{
    @OnlyIn(Dist.CLIENT)
    public static void drawFluidTankInGUI(WaterFilterTileEntity waterFilter, double x, double y, double percent, int height, int colorCode)
    {
        if(waterFilter == null)
            return;
        
        ResourceLocation resource = waterFilter.getStill();
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(resource.toString());
        if(sprite != null)
        {
            float red = (float)(colorCode >> 16 & 255) / 255.0F;
            float green = (float)(colorCode >> 8 & 255) / 255.0F;
            float blue = (float)(colorCode & 255) / 255.0F;
            
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
                drawQuad(x, y + offsetY, 16, subHeight, minU, maxV - deltaV * (subHeight / 16.0), maxU, maxV, red, green, blue, 1.0F);
            }
            
            GlStateManager.disableBlend();
        }
    }
    
    private static void drawQuad(double x, double y, double width, double height, double minU, double minV, double maxU, double maxV, float red, float green, float blue, float alpha)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
        buffer.pos(x, y + height, 0).tex(minU, maxV).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + width, y + height, 0).tex(maxU, maxV).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + width, y, 0).tex(maxU, minV).color(red, green, blue, alpha).endVertex();
        buffer.pos(x, y, 0).tex(minU, minV).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
    }
}
