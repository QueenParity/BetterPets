package com.kingparity.betterpets.client.renderer.tileentity;

import com.kingparity.betterpets.block.PetHorizontalBlock;
import com.kingparity.betterpets.block.WaterFilterBlock;
import com.kingparity.betterpets.core.ModItems;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeColors;
import org.lwjgl.opengl.GL11;

public class WaterFilterTESR extends TileEntityRenderer<WaterFilterTileEntity>
{
    private ItemEntity itemEntity = new ItemEntity(null, 0D, 0D, 0D);
    
    @Override
    public void render(WaterFilterTileEntity waterFilter, double x, double y, double z, float partialTicks, int destroyStage)
    {
        BlockState state = waterFilter.getWorld().getBlockState(waterFilter.getPos());
        if(!(state.getBlock() instanceof WaterFilterBlock))
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
            GlStateManager.pushMatrix();
            {
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    
                Direction direction = state.get(PetHorizontalBlock.DIRECTION);
                GlStateManager.translated(0.5, 0.5, 0.5);
                GlStateManager.rotatef(direction.getHorizontalIndex() * -90F - 90F, 0, 1, 0);
                GlStateManager.translated(-0.5, -0.5, -0.5);
                double heightCleanWater = 13.0 * (waterFilter.getFluidAmount(0) / (double)waterFilter.getCapacity());
                if(heightCleanWater > 0)
                {
                    drawFluid(waterFilter, 2.01 * 0.0625, 7.01 * 0.0625, 2.01 * 0.0625, (12 - 0.02) * 0.0625, (heightCleanWater - 0.02) * 0.0625, (12 - 0.02) * 0.0625, false);
                }
    
                double heightDirtyWater = 13.0 * (waterFilter.getFluidAmount(1) / (double)waterFilter.getCapacity());
                if(heightDirtyWater > 0)
                {
                    drawFluid(waterFilter, 2.01 * 0.0625, 14.01 * 0.0625, 2.01 * 0.0625, (12 - 0.02) * 0.0625, (heightDirtyWater - 0.02) * 0.0625, (12 - 0.02) * 0.0625, true);
                }
    
                ItemStack fabric = waterFilter.getStackInSlot(0);
                if(fabric.getItem() == ModItems.WATER_FILTER_FABRIC)
                {
                    itemEntity.setItem(fabric);
                    GlStateManager.pushMatrix();
                    {
                        GlStateManager.rotated(180, 0, 1, 1);
                        GlStateManager.translated(-8 * 0.0625, 8 * 0.0625, 13.5 * 0.0625);
                        GlStateManager.scaled(13.67 * 0.0625, 13.67 * 0.0625, 13.67 * 0.0625);
                        Minecraft.getInstance().getItemRenderer().renderItem(fabric, ItemCameraTransforms.TransformType.NONE);
                    }
                    GlStateManager.popMatrix();
                }
            }
            GlStateManager.popMatrix();
            
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();
    }
    
    private void drawFluid(WaterFilterTileEntity waterFilter, double x, double y, double z, double width, double height, double depth, boolean isFiltered)
    {
        if(!waterFilter.hasFluid())
        {
            return;
        }
        
        ResourceLocation resource = waterFilter.getStill();
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(resource.toString());
        if(sprite != null)
        {
            int i;
            if(isFiltered)
            {
                i = BiomeColors.getWaterColor(waterFilter.getWorld(), waterFilter.getPos()); //int: 4159204 hex: 3F76E4 Red: 63.0 Green: 118.0 Blue: 228.0
            }
            else
            {
                i = 5628096; //int: 562809, hex: 55E0C0 Red: 85 Green: 224 Blue: 192
            }
            //RGB to int/hex converter: https://www.shodor.org/stella2java/rgbint.html
            float f = (float)(i >> 16 & 255) / 255.0F;
            float f1 = (float)(i >> 8 & 255) / 255.0F;
            float f2 = (float)(i & 255) / 255.0F;
            
            double minU = sprite.getMinU();
            double maxU = Math.min(minU + (sprite.getMaxU() - minU) * width, sprite.getMaxU());
            double minV = sprite.getMinV();
            double maxV = Math.min(minV + (sprite.getMaxV() - minV) * height, sprite.getMaxV());
    
            Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            int light = getWorld().getCombinedLight(waterFilter.getPos(), Blocks.WATER.getDefaultState().getLightValue());
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
