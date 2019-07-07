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
import net.minecraft.util.Direction;
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
            boolean north = fluidPipeState.get(FluidPipeBlock.north);
            boolean east = fluidPipeState.get(FluidPipeBlock.east);
            boolean south = fluidPipeState.get(FluidPipeBlock.south);
            boolean west = fluidPipeState.get(FluidPipeBlock.west);
            boolean up = fluidPipeState.get(FluidPipeBlock.up);
            boolean down = fluidPipeState.get(FluidPipeBlock.down);
            
            double height = 3 * (fluidPipe.getFluidAmount() / (double)fluidPipe.getCapacity());
            if(height > 0)
            {
                /*if(north && east && south && west)
                {
                    draw4WayHorizontal(fluidPipe, (height - 0.02) * 0.0625);
                }
                else if((north && east && south) || (north && west && south) || (north && east && west) ||(south && east && west))
                {
                    GlStateManager.translated(0.5, 0.5, 0.5);
                    Direction direction;
                    if(north && east && west)
                    {
                        direction = Direction.EAST;
                    }
                    else if(north && east && south)
                    {
                        direction = Direction.SOUTH;
                    }
                    else if(north && west && south)
                    {
                        direction = Direction.NORTH;
                    }
                    else
                    {
                        direction = Direction.WEST;
                    }
                    GlStateManager.rotatef(direction.getHorizontalIndex() * -90F - 90F, 0, 1, 0);
                    GlStateManager.translated(-0.5, -0.5, -0.5);
                    draw3WayHorizontal(fluidPipe, (height - 0.02) * 0.0625);
                }
                else if((north && south) || (east && west))
                {
                    GlStateManager.translated(0.5, 0.5, 0.5);
                    Direction direction;
                    if(north)
                    {
                        direction = Direction.NORTH;
                    }
                    else
                    {
                        direction = Direction.EAST;
                    }
                    GlStateManager.rotatef(direction.getHorizontalIndex() * -90F - 90F, 0, 1, 0);
                    GlStateManager.translated(-0.5, -0.5, -0.5);
                    drawBox(fluidPipe, 0, 6.51 * 0.0625, 6.51 * 0.0625, 16 * 0.0625, (height - 0.02) * 0.0625, (3 - 0.02) * 0.0625, 7);
                }
                else if(north || east || south || west)
                {
                    GlStateManager.translated(0.5, 0.5, 0.5);
                    Direction direction;
                    if(north)
                    {
                        direction = Direction.NORTH;
                    }
                    else if(east)
                    {
                        direction = Direction.EAST;
                    }
                    else if(south)
                    {
                        direction = Direction.SOUTH;
                    }
                    else
                    {
                        direction = Direction.WEST;
                    }
                    GlStateManager.rotatef(direction.getHorizontalIndex() * -90F - 90F, 0, 1, 0);
                    GlStateManager.translated(-0.5, -0.5, -0.5);
                    drawBox(fluidPipe, 6.51 * 0.0625, 6.51 * 0.0625, 6.51 * 0.0625, (9.5 - 0.01) * 0.0625, (height - 0.02) * 0.0625, (3 - 0.02) * 0.0625, 3);
                }
                else if(!north && !east && !south && !west)
                {
                    drawBox(fluidPipe, 6.51 * 0.0625, 6.51 * 0.0625, 6.51 * 0.0625, (3 - 0.02) * 0.0625, (height - 0.02) * 0.0625, (3 - 0.02) * 0.0625, 0);
                }*/
                drawBox(fluidPipe, 6.51 * 0.0625, 6.51 * 0.0625, 6.51 * 0.0625, (3 - 0.02) * 0.0625, (height - 0.02) * 0.0625, (3 - 0.02) * 0.0625, !north, !east, !south, !west, !up, !down);
    
                for(Direction direction : FluidPipeTileEntity.faces)
                {
                    GlStateManager.pushMatrix();
                    {
                        GlStateManager.translated(0.5, 0.5, 0.5);
                        Direction rotation;
                        if(north && direction == Direction.NORTH)
                        {
                            rotation = Direction.EAST;
                        }
                        else if(east && direction == Direction.EAST)
                        {
                            rotation = Direction.SOUTH;
                        }
                        else if(south && direction == Direction.SOUTH)
                        {
                            rotation = Direction.WEST;
                        }
                        else if(west && direction == Direction.WEST)
                        {
                            rotation = Direction.NORTH;
                        }
                        else if(up && direction == Direction.UP)
                        {
                            rotation = Direction.UP;
                        }
                        else if(down && direction == Direction.DOWN)
                        {
                            rotation = Direction.DOWN;
                        }
                        else
                        {
                            rotation = null;
                        }
                        if(rotation != null)
                        {
                            GlStateManager.rotatef(rotation.getHorizontalIndex() * -90F - 90F, 0, 1, 0);
                            GlStateManager.translated(-0.5, -0.5, -0.5);
                            drawBox(fluidPipe, 6.51 * 0.0625, 6.51 * 0.0625, 0, (3 - 0.02) * 0.0625, (height - 0.02) * 0.0625, 6.51 * 0.0625, false, true, false, true, true, true);
                        }
                    }
                    GlStateManager.popMatrix();
                }
            }
            
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();
    }
    
    private void drawBox(FluidPipeTileEntity fluidPipe, double x, double y, double z, double width, double height, double depth, boolean north, boolean east, boolean south, boolean west, boolean up, boolean down)
    {
        if(fluidPipe.getFluidAmount() == 0.0F)
        {
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