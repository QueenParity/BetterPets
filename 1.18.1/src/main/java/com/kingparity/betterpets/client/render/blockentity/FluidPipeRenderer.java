package com.kingparity.betterpets.client.render.blockentity;

import com.kingparity.betterpets.blockentity.FluidPipeBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.ForgeHooksClient;

public class FluidPipeRenderer implements BlockEntityRenderer<FluidPipeBlockEntity>
{
    public FluidPipeRenderer(BlockEntityRendererProvider.Context context)
    {
        super();
    }
    
    @Override
    public void render(FluidPipeBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        //Direction direction = blockEntity.getBlockState().getValue(FluidPipeBlock.DIRECTION);
        //poseStack.mulPose(Vector3f.YP.rotationDegrees(direction.get2DDataValue() * -90F - 90F));
        poseStack.translate(-0.5, -0.5, -0.5);
        //float height = (float) (16.0 * (blockEntity.getFluidLevel() / (double) blockEntity.getCapacity()));
        float level = blockEntity.getFluidLevel();
        float height = (4.0F * ((Mth.clamp(level, 0, 80)) / 80.0F));
        if(level > 0)
        {
            //this.drawFluid(blockEntity, poseStack, buffer, 4.01F * 0.0625F, 12.01F * 0.0625F, 4.01F * 0.0625F, (8 - 0.02F) * 0.0625F, height * 0.0625F, (8 - 0.02F) * 0.0625F);
            if(blockEntity.getLinkBoolean(Direction.DOWN.get3DDataValue()))
            {
                this.drawFluid(blockEntity, poseStack, buffer, 6.01F * 0.0625F, 0.01F * 0.0625F, 6.01F * 0.0625F, (height - 0.02F) * 0.0625F, (5 - 0.02F) * 0.0625F, (4 - 0.02F) * 0.0625F);
                level -= 80;
            }
            //this.drawFluid(blockEntity, poseStack, buffer, 4.51F * 0.0625F, 4.49F * 0.0625F, 4.51F * 0.0625F, (7 - 0.02F) * 0.0625F, height * 0.0625F, (7 - 0.02F) * 0.0625F);
        }
        int connections = 0;
        for(int i = 2; i < 6; i++)
        {
            if(blockEntity.getLinkBoolean(i))
            {
                connections++;
            }
        }
        
        if(level > 0)
        {
            float nodeHeight = (1.0F * Mth.clamp(level, 0, 36) / 36.0F) ;
            if(level - 36 > 0)
            {
                int partThing = (80*connections) + (36*4);
                height = (4.0F * ((Mth.clamp(level - 36, 0, partThing)) / (float)(partThing)));
                
                if(blockEntity.getLinkBoolean(Direction.NORTH.get3DDataValue()))
                {
                    this.drawFluid(blockEntity, poseStack, buffer, 6.01F * 0.0625F, 6.01F * 0.0625F, 0.01F * 0.0625F, (4 - 0.02F) * 0.0625F, (height - 0.02F) * 0.0625F, (5 - 0.02F) * 0.0625F);
                }
                if(blockEntity.getLinkBoolean(Direction.SOUTH.get3DDataValue()))
                {
                    this.drawFluid(blockEntity, poseStack, buffer, 6.01F * 0.0625F, 6.01F * 0.0625F, 11.01F * 0.0625F, (4 - 0.02F) * 0.0625F, (height - 0.02F) * 0.0625F, (5 - 0.02F) * 0.0625F);
                }
                if(blockEntity.getLinkBoolean(Direction.WEST.get3DDataValue()))
                {
                    this.drawFluid(blockEntity, poseStack, buffer, 0.01F * 0.0625F, 6.01F * 0.0625F, 6.01F * 0.0625F, (5 - 0.02F) * 0.0625F, (height - 0.02F) * 0.0625F, (4 - 0.02F) * 0.0625F);
                }
                if(blockEntity.getLinkBoolean(Direction.EAST.get3DDataValue()))
                {
                    this.drawFluid(blockEntity, poseStack, buffer, 11.01F * 0.0625F, 6.01F * 0.0625F, 6.01F * 0.0625F, (5 - 0.02F) * 0.0625F, (height - 0.02F) * 0.0625F, (4 - 0.02F) * 0.0625F);
                }
                
                nodeHeight += height;
            }
            level = level - (80*connections) - 216 + 36;
            height = (nodeHeight + (1.0F * Mth.clamp(level, 0, 36) / 36.0F));
            this.drawFluid(blockEntity, poseStack, buffer, 5.01F * 0.0625F, 5.01F * 0.0625F, 5.01F * 0.0625F, (6 - 0.02F) * 0.0625F, (height - 0.02F) * 0.0625F, (6 - 0.02F) * 0.0625F);
            
            level -= 36;
            if(level > 0)
            {
                height = (4.0F * ((Mth.clamp(level, 0, 80)) / 80.0F));
                if(blockEntity.getLinkBoolean(Direction.UP.get3DDataValue()))
                {
                    this.drawFluid(blockEntity, poseStack, buffer, 6.01F * 0.0625F, 11.01F * 0.0625F, 6.01F * 0.0625F, (height - 0.02F) * 0.0625F, (5 - 0.02F) * 0.0625F, (4 - 0.02F) * 0.0625F);
                }
            }
        }
        poseStack.popPose();
    }
    
    private void drawFluid(FluidPipeBlockEntity be, PoseStack poseStack, MultiBufferSource bufferSource, float x, float y, float z, float width, float height, float depth)
    {
        Fluid fluid = be.getFluidStackTank().getFluid();
        if(fluid == Fluids.EMPTY) return;
        
        TextureAtlasSprite sprite = ForgeHooksClient.getFluidSprites(be.getLevel(), be.getBlockPos(), fluid.defaultFluidState())[0];
        float minU = sprite.getU0();
        float maxU = Math.min(minU + (sprite.getU1() - minU) * width, sprite.getU1());
        float minV = sprite.getV0();
        float maxV = Math.min(minV + (sprite.getV1() - minV) * height, sprite.getV1());
        int waterColor = fluid.getAttributes().getColor(be.getLevel(), be.getBlockPos());
        float red = (float) (waterColor >> 16 & 255) / 255.0F;
        float green = (float) (waterColor >> 8 & 255) / 255.0F;
        float blue = (float) (waterColor & 255) / 255.0F;
        int light = this.getCombinedLight(be.getLevel(), be.getBlockPos());
        
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());
        Matrix4f matrix = poseStack.last().pose();
    
        //left side
        buffer.vertex(matrix, x + width, y, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).uv(maxU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x, y, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).uv(minU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x, y + height, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).uv(minU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x + width, y + height, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).uv(maxU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
    
        //right side
        buffer.vertex(matrix, x, y, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).uv(maxU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x + width, y, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).uv(minU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x + width, y + height, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).uv(minU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x, y + height, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).uv(maxU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
    
        maxU = Math.min(minU + (sprite.getU1() - minU) * depth, sprite.getU1());
    
        //back side
        buffer.vertex(matrix, x + width, y, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).uv(maxU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x + width, y, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).uv(minU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x + width, y + height, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).uv(minU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x + width, y + height, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).uv(maxU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
    
        //front side
        buffer.vertex(matrix, x, y + height, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).uv(maxU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x, y + height, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).uv(minU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x, y, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).uv(minU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x, y, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).uv(maxU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
    
        maxV = Math.min(minV + (sprite.getV1() - minV) * width, sprite.getV1());
    
        //top
        buffer.vertex(matrix, x, y + height, z).color(red, green, blue, 1.0F).uv(maxU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x, y + height, z + depth).color(red, green, blue, 1.0F).uv(minU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x + width, y + height, z + depth).color(red, green, blue, 1.0F).uv(minU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x + width, y + height, z).color(red, green, blue, 1.0F).uv(maxU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
    
        //bottom
        buffer.vertex(matrix, x + width, y, z + depth).color(red, green, blue, 1.0F).uv(maxU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x + width, y, z).color(red, green, blue, 1.0F).uv(minU, minV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x, y, z).color(red, green, blue, 1.0F).uv(minU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x, y, z + depth).color(red, green, blue, 1.0F).uv(maxU, maxV).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
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
