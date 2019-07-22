package com.kingparity.betterpets.client.renderer.tileentity;

import com.kingparity.betterpets.block.FluidPumpBlock;
import com.kingparity.betterpets.tileentity.FluidPumpTileEntity;
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
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeColors;
import org.lwjgl.opengl.GL11;

public class FluidPumpTESR extends TileEntityRenderer<FluidPumpTileEntity>
{
    @Override
    public void render(FluidPumpTileEntity fluidPump, double x, double y, double z, float partialTicks, int destroyStage)
    {
        BlockState state = fluidPump.getWorld().getBlockState(fluidPump.getPos());
        if(!(state.getBlock() instanceof FluidPumpBlock))
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
            BlockState fluidPumpState = fluidPump.getWorld().getBlockState(fluidPump.getPos());
            
            double height = 3 * (fluidPump.getFluidAmount() / (double)fluidPump.getCapacity());
            if(height > 0)
            {
                //drawBox(fluidPump, 6.51 * 0.0625, 6.51 * 0.0625, 6.51 * 0.0625, (3 - 0.02) * 0.0625, (height - 0.02) * 0.0625, (3 - 0.02) * 0.0625, false, true, false, true, true, true);
                
                GlStateManager.translated(0.5, 0.5, 0.5);
                Direction rotation = fluidPumpState.get(FluidPumpBlock.FACING);
                if(rotation.getHorizontalIndex() != -1)
                {
                    GlStateManager.rotatef(rotation.getHorizontalIndex() * -90F + 180F, 0, 1, 0);
                }
                else if(rotation == Direction.DOWN)
                {
                    GlStateManager.rotatef(-90F, 1, 0, 0);
                }
                else
                {
                    GlStateManager.rotatef(90F, 1, 0, 0);
                }
                GlStateManager.translated(-0.5, -0.5, -0.5);
                drawBox(fluidPump, 6.51 * 0.0625, 6.51 * 0.0625, 0, (3 - 0.02) * 0.0625, (height - 0.02) * 0.0625, 9.49 * 0.0625, false, true, true, true, true, true, fluidPump.isFiltered());
            }
            
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();
    }
    
    private void drawBox(FluidPumpTileEntity fluidPump, double x, double y, double z, double width, double height, double depth, boolean north, boolean east, boolean south, boolean west, boolean up, boolean down, boolean isFiltered)
    {
        if(fluidPump.getFluidAmount() == 0.0F)
        {
            return;
        }
        
        ResourceLocation resource = fluidPump.getStill();
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(resource.toString());
        if(sprite != null)
        {
            int i;
            if(isFiltered)
            {
                i = 51455; //int: 51455 hex: DEDEDE Red: 0.0 Green: 200.0 Blue: 255.0
            }
            else
            {
                i = BiomeColors.getWaterColor(fluidPump.getWorld(), fluidPump.getPos()); //int: 4159204 hex: 3F76E4 Red: 63.0 Green: 118.0 Blue: 228.0
            }
            float f = (float)(i >> 16 & 255) / 255.0F;
            float f1 = (float)(i >> 8 & 255) / 255.0F;
            float f2 = (float)(i & 255) / 255.0F;
            
            double minU = sprite.getMinU();
            double maxU = Math.min(minU + (sprite.getMaxU() - minU) * width, sprite.getMaxU());
            double minV = sprite.getMinV();
            double maxV = Math.min(minV + (sprite.getMaxV() - minV) * height, sprite.getMaxV());
            
            Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            int light = getWorld().getCombinedLight(fluidPump.getPos(), Blocks.WATER.getDefaultState().getLightValue());
            int lightX = light >> 0x10 & 0xFFFF;
            int lightY = light & 0xFFFF;
            
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            //left side
            if(north)
            {
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
                buffer.pos(x + width, y, z).tex(maxU, minV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
                buffer.pos(x, y, z).tex(minU, minV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
                buffer.pos(x, y + height, z).tex(minU, maxV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
                buffer.pos(x + width, y + height, z).tex(maxU, maxV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
                tessellator.draw();
            }
            
            //right side
            if(south)
            {
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
                buffer.pos(x, y, z + depth).tex(maxU, minV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
                buffer.pos(x + width, y, z + depth).tex(minU, minV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
                buffer.pos(x + width, y + height, z + depth).tex(minU, maxV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
                buffer.pos(x, y + height, z + depth).tex(maxU, maxV).lightmap(lightX, lightY).color(f * 0.75F, f1 * 0.75F, f2 * 0.75F, 1.0F).endVertex();
                tessellator.draw();
            }
            
            maxU = Math.min(minU + (sprite.getMaxU() - minU) * depth, sprite.getMaxU());
            
            //back side
            if(east)
            {
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
                buffer.pos(x + width, y, z + depth).tex(maxU, minV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
                buffer.pos(x + width, y, z).tex(minU, minV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
                buffer.pos(x + width, y + height, z).tex(minU, maxV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
                buffer.pos(x + width, y + height, z + depth).tex(maxU, maxV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
                tessellator.draw();
            }
            
            //front side
            if(west)
            {
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
                buffer.pos(x, y + height, z + depth).tex(maxU, minV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
                buffer.pos(x, y + height, z).tex(minU, minV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
                buffer.pos(x, y, z).tex(minU, maxV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
                buffer.pos(x, y, z + depth).tex(maxU, maxV).lightmap(lightX, lightY).color(f * 0.85F, f1 * 0.85F, f2 * 0.85F, 1.0F).endVertex();
                tessellator.draw();
            }
            
            maxV = Math.min(minV + (sprite.getMaxV() - minV) * width, sprite.getMaxV());
            
            //top
            if(up)
            {
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
                buffer.pos(x, y + height, z).tex(maxU, minV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
                buffer.pos(x, y + height, z + depth).tex(minU, minV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
                buffer.pos(x + width, y + height, z + depth).tex(minU, maxV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
                buffer.pos(x + width, y + height, z).tex(maxU, maxV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
                tessellator.draw();
            }
            
            //bottom
            if(down)
            {
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
                buffer.pos(x + width, y, z + depth).tex(maxU, minV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
                buffer.pos(x + width, y, z).tex(minU, minV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
                buffer.pos(x, y, z).tex(minU, maxV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
                buffer.pos(x, y, z + depth).tex(maxU, maxV).lightmap(lightX, lightY).color(f, f1, f2, 1.0F).endVertex();
                tessellator.draw();
            }
        }
    }
}