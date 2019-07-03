package com.kingparity.betterpets.client.renderer.tileentity;

import com.kingparity.betterpets.block.FluidPipeBlock;
import com.kingparity.betterpets.tileentity.FluidPipeTileEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeColors;
import org.lwjgl.opengl.GL11;

public class FluidPipeTESR extends TileEntityRenderer<FluidPipeTileEntity>
{
    @Override
    public void render(FluidPipeTileEntity fluidPipe, double x, double y, double z, float partialTicks, int destroyStage)
    {
        BlockState state = fluidPipe.getWorld().getBlockState(fluidPipe.getPos());
        if(!(state.getBlock() instanceof FluidPipeBlock))
        {
            return;
        }
        GlStateManager.pushMatrix();
        {
            GlStateManager.translated(x, y, z);
            
            GlStateManager.disableCull();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.enableAlphaTest();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            BlockState fluidPipeState = fluidPipe.getWorld().getBlockState(fluidPipe.getPos());
            boolean north = fluidPipeState.get(FluidPipeBlock.NORTH);
            boolean east = fluidPipeState.get(FluidPipeBlock.EAST);
            boolean south = fluidPipeState.get(FluidPipeBlock.SOUTH);
            boolean west = fluidPipeState.get(FluidPipeBlock.WEST);
            double fX1, fZ1, fW1, fD1;
            double fX2 = 0.0, fZ2 = 0.0, fW2 = 0.0, fD2 = 0.0;
            double fX3 = 0.0, fZ3 = 0.0, fW3 = 0.0, fD3 = 0.0;
            boolean hasf2 = false, hasf3 = false;
            if(fluidPipeState.get(FluidPipeBlock.EAST) && fluidPipeState.get(FluidPipeBlock.WEST))
            {
                fX1 = 6.51 * 0.0625;
                fZ1 = 0.00 * 0.0625;
                fW1 = (3 - 0.02) * 0.0625;
                fD1 = (16 - 0.01) * 0.0625;
                if(fluidPipeState.get(FluidPipeBlock.NORTH))
                {
                    hasf2 = true;
                    fX2 = 0.01 * 0.0625;
                    fZ2 = 6.51 * 0.0625;
                    fW2 = (7 - 0.51) * 0.0625;
                    fD2 = (3 - 0.02) * 0.0625;
                }
                if(fluidPipeState.get(FluidPipeBlock.SOUTH))
                {
                    hasf3 = true;
                    fX3 = 9.50 * 0.0625;
                    fZ3 = 6.51 * 0.0625;
                    fW3 = (7 - 0.50) * 0.0625;
                    fD3 = (3 - 0.02) * 0.0625;
                }
            }
            else if(fluidPipeState.get(FluidPipeBlock.NORTH) && fluidPipeState.get(FluidPipeBlock.SOUTH))
            {
                fX1 = 0.01 * 0.0625;
                fZ1 = 6.51 * 0.0625;
                fW1 = (16 - 0.01) * 0.0625;
                fD1 = (3 - 0.02) * 0.0625;
                if(fluidPipeState.get(FluidPipeBlock.EAST))
                {
                    hasf2 = true;
                    fX2 = 6.51 * 0.0625;
                    fZ2 = 0.00 * 0.0625;
                    fW2 = (3 - 0.02) * 0.0625;
                    fD2 = (7 - 0.50) * 0.0625;
                }
                if(fluidPipeState.get(FluidPipeBlock.WEST))
                {
                    hasf3 = true;
                    fX3 = 6.51 * 0.0625;
                    fZ3 = 9.50 * 0.0625;
                    fW3 = (3 - 0.02) * 0.0625;
                    fD3 = (7 - 0.51) * 0.0625;
                }
            }
            else if(fluidPipeState.get(FluidPipeBlock.NORTH))
            {
                fX1 = 0.01 * 0.0625;
                fZ1 = 6.51 * 0.0625;
                fW1 = (9.5 - 0.02) * 0.0625;
                fD1 = (3 - 0.02) * 0.0625;
                if(fluidPipeState.get(FluidPipeBlock.EAST))
                {
                    hasf2 = true;
                    fX2 = 6.51 * 0.0625;
                    fZ2 = 0.00 * 0.0625;
                    fW2 = (3 - 0.02) * 0.0625;
                    fD2 = (7 - 0.50) * 0.0625;
                }
                if(fluidPipeState.get(FluidPipeBlock.WEST))
                {
                    hasf3 = true;
                    fX3 = 6.51 * 0.0625;
                    fZ3 = 9.50 * 0.0625;
                    fW3 = (3 - 0.02) * 0.0625;
                    fD3 = (7 - 0.51) * 0.0625;
                }
            }
            else if(fluidPipeState.get(FluidPipeBlock.EAST))
            {
                fX1 = 6.51 * 0.0625;
                fZ1 = 0.00 * 0.0625;
                fW1 = (3 - 0.02) * 0.0625;
                fD1 = (9.5 - 0.02) * 0.0625;
                if(fluidPipeState.get(FluidPipeBlock.SOUTH))
                {
                    hasf3 = true;
                    fX3 = 9.50 * 0.0625;
                    fZ3 = 6.51 * 0.0625;
                    fW3 = (7 - 0.50) * 0.0625;
                    fD3 = (3 - 0.02) * 0.0625;
                }
            }
            else if(fluidPipeState.get(FluidPipeBlock.SOUTH))
            {
                fX1 = 6.51 * 0.0625;
                fZ1 = 6.51 * 0.0625;
                fW1 = (9.5 - 0.01) * 0.0625;
                fD1 = (3 - 0.02) * 0.0625;
                if(fluidPipeState.get(FluidPipeBlock.WEST))
                {
                    hasf3 = true;
                    fX3 = 6.51 * 0.0625;
                    fZ3 = 9.50 * 0.0625;
                    fW3 = (3 - 0.02) * 0.0625;
                    fD3 = (7 - 0.51) * 0.0625;
                }
            }
            else if(fluidPipeState.get(FluidPipeBlock.WEST))
            {
                fX1 = 6.51 * 0.0625;
                fZ1 = 6.51 * 0.0625;
                fW1 = (3 - 0.02) * 0.0625;
                fD1 = (9.5 - 0.02) * 0.0625;
            }
            else
            {
                fX1 = 6.51 * 0.0625;
                fZ1 = 6.51 * 0.0625;
                fW1 = (3 - 0.02) * 0.0625;
                fD1 = (3 - 0.02) * 0.0625;
            }
            
            GlStateManager.translated(0.5, 0.5, 0.5);
            GlStateManager.rotatef(0 * -90F - 90F, 0, 1, 0);
            GlStateManager.translated(-0.5, -0.5, -0.5);
            double height = 3 * (fluidPipe.getFluidAmount() / (double)fluidPipe.getCapacity());
            if(height > 0)
            {
                drawFluid(fluidPipe, fX1, 6.51 * 0.0625, fZ1, fW1, (height - 0.02) * 0.0625, fD1);
                if(hasf2)
                {
                    drawFluid(fluidPipe, fX2, 6.51 * 0.0625, fZ2, fW2, (height - 0.02) * 0.0625, fD2);
                }
                if(hasf3)
                {
                    drawFluid(fluidPipe, fX3, 6.51 * 0.0625, fZ3, fW3, (height - 0.02) * 0.0625, fD3);
                }
            }
            /*System.out.println(height);
            System.out.println(fluidPipe.getFluidAmount());
            System.out.println(fluidPipe.getCapacity());*/
            
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();
    }
    
    private void drawFluid(FluidPipeTileEntity fluidPipe, double x, double y, double z, double width, double height, double depth)
    {
        if(fluidPipe.getFluidAmount() == 0.0F)
        {
            //System.out.println(3);
            return;
        }
        
        ResourceLocation resource = fluidPipe.getStill();
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(resource.toString());
        if(sprite != null)
        {
            int i = BiomeColors.getWaterColor(fluidPipe.getWorld(), fluidPipe.getPos());
            float f = (float)(i >> 16 & 255) / 255.0F;
            float f1 = (float)(i >> 8 & 255) / 255.0F;
            float f2 = (float)(i & 255) / 255.0F;
            
            double minU = sprite.getMinU();
            double maxU = Math.min(minU + (sprite.getMaxU() - minU) * width, sprite.getMaxU());
            double minV = sprite.getMinV();
            double maxV = Math.min(minV + (sprite.getMaxV() - minV) * height, sprite.getMaxV());
    
            Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            int light = getWorld().getCombinedLight(fluidPipe.getPos(), Blocks.WATER.getDefaultState().getLightValue());
            int lightX = light >> 0x10 & 0xFFFF;
            int lightY = light & 0xFFFF;
            
            //left side
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
            buffer.pos(x + width, y, z).tex(maxU, minV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
            buffer.pos(x, y, z).tex(minU, minV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
            buffer.pos(x, y + height, z).tex(minU, maxV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
            buffer.pos(x + width, y + height, z).tex(maxU, maxV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
            tessellator.draw();
            
            //right side
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
            buffer.pos(x, y, z + depth).tex(maxU, minV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
            buffer.pos(x + width, y, z + depth).tex(minU, minV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
            buffer.pos(x + width, y + height, z + depth).tex(minU, maxV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
            buffer.pos(x, y + height, z + depth).tex(maxU, maxV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
            tessellator.draw();
            
            maxU = Math.min(minU + (sprite.getMaxU() - minU) * depth, sprite.getMaxU());
            
            //back side
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
            buffer.pos(x + width, y, z + depth).tex(maxU, minV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
            buffer.pos(x + width, y, z).tex(minU, minV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
            buffer.pos(x + width, y + height, z).tex(minU, maxV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
            buffer.pos(x + width, y + height, z + depth).tex(maxU, maxV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
            tessellator.draw();
    
            //front side
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
            buffer.pos(x, y + height, z + depth).tex(maxU, minV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
            buffer.pos(x, y + height, z).tex(minU, minV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
            buffer.pos(x, y, z).tex(minU, maxV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
            buffer.pos(x, y, z + depth).tex(maxU, maxV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
            tessellator.draw();
            
            maxV = Math.min(minV + (sprite.getMaxV() - minV) * width, sprite.getMaxV());
            
            //top
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
            buffer.pos(x, y + height, z).tex(maxU, minV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
            buffer.pos(x, y + height, z + depth).tex(minU, minV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
            buffer.pos(x + width, y + height, z + depth).tex(minU, maxV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
            buffer.pos(x + width, y + height, z).tex(maxU, maxV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
            tessellator.draw();
    
            //bottom
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
            buffer.pos(x + width, y, z + depth).tex(maxU, minV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
            buffer.pos(x + width, y, z).tex(minU, minV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
            buffer.pos(x, y, z).tex(minU, maxV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
            buffer.pos(x, y, z + depth).tex(maxU, maxV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
            tessellator.draw();
        }
    }
}
