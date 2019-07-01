package com.kingparity.betterpets.client.renderer.tileentity;

import com.google.common.collect.Lists;
import com.kingparity.betterpets.block.RotatedBlock;
import com.kingparity.betterpets.block.WaterFilterBlock;
import com.kingparity.betterpets.client.util.HermiteInterpolator;
import com.kingparity.betterpets.init.BetterPetBlocks;
import com.kingparity.betterpets.tileentity.WaterFilterTileEntity;
import com.kingparity.betterpets.util.RenderUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeColors;
import org.lwjgl.opengl.GL11;

public class WaterFilterTESR extends TileEntityRenderer<WaterFilterTileEntity>
{
    @Override
    public void render(WaterFilterTileEntity waterFilter, double x, double y, double z, float partialTicks, int destroyStage)
    {
        BlockState state = waterFilter.getWorld().getBlockState(waterFilter.getPos());
        if(state.getBlock() != BetterPetBlocks.WATER_FILTER)
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
    
            Direction direction = state.get(RotatedBlock.DIRECTION);
            GlStateManager.translated(0.5, 0.5, 0.5);
            GlStateManager.rotatef(direction.getHorizontalIndex() * -90F - 90F, 0, 1, 0);
            GlStateManager.translated(-0.5, -0.5, -0.5);
            double height = 13.0 * (waterFilter.getFluidAmount() / (double)waterFilter.MAX_CONTENTS);
            if(height > 0)
            {
                drawFluid(waterFilter, 2.01 * 0.0625, 7.01 * 0.0625, 6.01 * 0.0625, (12 - 0.02) * 0.0625, (height - 0.02) * 0.0625, (8 - 0.02) * 0.0625, false);
            }
    
            drawFluid(waterFilter, 5.01 * 0.0625, 4.01 * 0.0625, 2.01 * 0.0625, (6 - 0.02) * 0.0625, (height - 0.02) * 0.0625, (3 - 0.02) * 0.0625, true);
            
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();
        
        renderTube(waterFilter, x, y, z, partialTicks);
    }
    
    private void renderTube(WaterFilterTileEntity waterFilter, double x, double y, double z, float partialTicks)
    {
        BlockPos blockPos = waterFilter.getPos();
        BlockState state = waterFilter.getWorld().getBlockState(blockPos);
        if(state.getBlock() != BetterPetBlocks.WATER_FILTER)
        {
            return;
        }
        
        Direction direction = state.get(WaterFilterBlock.DIRECTION);
        double[] pos = this.fixRotation(direction, -2 * 0.0625 /*0.640625*/, 8 * 0.0625, -2 * 0.0625, 10 * 0.0625);
    
        GlStateManager.pushMatrix();
        {
            GlStateManager.translated(x, y, z);
            
            HermiteInterpolator.Point destPoint;
            if(waterFilter.getTubingEntity() != null)
            {
                PlayerEntity entity = waterFilter.getTubingEntity();
                double side = entity.getPrimaryHand() == HandSide.RIGHT ? 1 : -1;
                double playerX = (double) blockPos.getX() - (entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks);
                double playerY = (double) blockPos.getY() - (entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks);
                double playerZ = (double) blockPos.getZ() - (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks);
                float renderYawOffset = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * partialTicks;
                Vec3d lookVec = Vec3d.fromPitchYaw(-20F, renderYawOffset);
                Vec3d tubeVec = new Vec3d(-0.35 * side, 0.01, 0.0625);
                if(entity instanceof AbstractClientPlayerEntity)
                {
                    String skinType = ((AbstractClientPlayerEntity) entity).getSkinType();
                    if(skinType.equals("slim"))
                    {
                        tubeVec = tubeVec.add(0.03 * side, -0.03, 0.0);
                    }
                }
                tubeVec = tubeVec.rotateYaw(-renderYawOffset * 0.017453292F);
                if(entity.equals(Minecraft.getInstance().player))
                {
                    if(Minecraft.getInstance().gameSettings.thirdPersonView == 0)
                    {
                        lookVec = Vec3d.fromPitchYaw(0F, entity.rotationYaw);
                        tubeVec = new Vec3d(-0.25, 0.5, -0.25).rotateYaw(-entity.rotationYaw * 0.017453292F);
                    }
                }
                destPoint = new HermiteInterpolator.Point(new Vec3d(-playerX + tubeVec.x, -playerY + 0.8 + tubeVec.y, -playerZ + tubeVec.z), new Vec3d(lookVec.x * 3, lookVec.y * 3, lookVec.z * 3));
            }
            else
            {
                double[] destPos = this.fixRotation(direction, 0.345, 1.06, 0.345, 1.06);
                destPoint = null;// = new HermiteInterpolator.Point(new Vec3d(destPos[0], 0.0625, destPos[1]), new Vec3d(0, 3, 0));
            }
            
            if(destPoint != null)
            {
                HermiteInterpolator spline = new HermiteInterpolator(Lists.newArrayList(new HermiteInterpolator.Point(new Vec3d(pos[0], 5.25 * 0.0625, pos[1]), new Vec3d(0, -1.5, 0)), destPoint));
                
                ItemStack stack = new ItemStack(Blocks.BLACK_CONCRETE);
                IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack);
                
                GlStateManager.pushMatrix();
                {
                    int steps = 100;
                    for(int i = 0; i < spline.getSize() - 1; i++)
                    {
                        for(int j = 0; j <= steps; j++)
                        {
                            float percent = j / (float)steps;
                            HermiteInterpolator.Result r = spline.get(i, percent);
                            GlStateManager.pushMatrix();
                            GlStateManager.translated(r.getPoint().x, r.getPoint().y, r.getPoint().z);
                            GlStateManager.rotated((float)Math.toDegrees(Math.atan2(r.getDir().x, r.getDir().z)), 0, 1, 0);
                            GlStateManager.rotated((float)Math.toDegrees(Math.asin(-r.getDir().normalize().y)), 1, 0, 0);
                            GlStateManager.scaled(0.075, 0.075, 0.075);
                            RenderUtil.renderItemModel(stack, model, ItemCameraTransforms.TransformType.NONE);
                            GlStateManager.popMatrix();
                        }
                    }
                }
                GlStateManager.popMatrix();
    
                stack = new ItemStack(Blocks.WHITE_CONCRETE);
                model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack);
                
                GlStateManager.pushMatrix();
                {
                    //GlStateManager.translated(7.5 * 0.0625, 4.5 * 0.0625, 17.5 * 0.0625);
                    GlStateManager.translated(0.5, 0, 0.5);
                    GlStateManager.rotated(direction.getHorizontalIndex() * -90F, 0, 1, 0);
                    GlStateManager.translated(-0.5, 0, -0.5);
                    GlStateManager.translated(6 * 0.0625, 4.75 * 0.0625, -2 * 0.0625);
                    GlStateManager.pushMatrix();
                    {
                        GlStateManager.scaled(1.5 * 0.0625, 0.5 * 0.0625, 1.5 * 0.0625);
                        RenderUtil.renderItemModel(stack, model, ItemCameraTransforms.TransformType.NONE);
                    }
                    GlStateManager.popMatrix();
                }
                GlStateManager.popMatrix();
            }
            
            /*if(waterFilter.getTubingEntity() == null)
            {
                GlStateManager.pushMatrix();
                {
                    double[] destPos = this.fixRotation(direction, 0.29, 1.06, 0.29, 1.06);
                    GlStateManager.translated(destPos[0], 0.5, destPos[1]);
                    GlStateManager.rotated(direction.getHorizontalIndex() * -90F, 0, 1, 0);
                    GlStateManager.rotated(180F, 0, 1, 0);
                    GlStateManager.rotated(90F, 1, 0, 0);
                    GlStateManager.scaled(0.8, 0.8, 0.8);
                    RenderUtil.renderItemModel(new ItemStack(BetterPetItems.FLUID_TUBE), Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(Reference.ID, "fluid_tube"), null)), ItemCameraTransforms.TransformType.NONE);
                }
                GlStateManager.popMatrix();
            }*/
        }
        GlStateManager.popMatrix();
    }
    
    private void drawFluid(WaterFilterTileEntity waterFilter, double x, double y, double z, double width, double height, double depth, boolean isSoap)
    {
        if(!waterFilter.hasFluid())
        {
            return;
        }
        
        ResourceLocation resource = waterFilter.getStill();
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(resource.toString());
        if(sprite != null)
        {
            int i = BiomeColors.getWaterColor(waterFilter.getWorld(), waterFilter.getPos());
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
    
    private double[] fixRotation(Direction direction, double x1, double z1, double x2, double z2)
    {
        switch(direction)
        {
            case WEST:
            {
                double origX1 = x1;
                x1 = 1.0F - x2;
                double origZ1 = z1;
                z1 = 1.0F - z2;
                x2 = 1.0F - origX1;
                z2 = 1.0F - origZ1;
                break;
            }
            case NORTH:
            {
                double origX1 = x1;
                x1 = z1;
                z1 = 1.0F - x2;
                x2 = z2;
                z2 = 1.0F - origX1;
                break;
            }
            case SOUTH:
            {
                double origX1 = x1;
                x1 = 1.0F - z2;
                double origZ1 = z1;
                z1 = origX1;
                double origX2 = x2;
                x2 = 1.0F - origZ1;
                z2 = origX2;
                break;
            }
            default:
                break;
        }
        return new double[] { x1, z1, x2, z2 };
    }
}
