package com.kingparity.betterpets.client.render.blockentity;

import com.kingparity.betterpets.blockentity.FluidPumpBlockEntity;
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

public class FluidPumpRenderer implements BlockEntityRenderer<FluidPumpBlockEntity>
{
    public FluidPumpRenderer(BlockEntityRendererProvider.Context context)
    {
        super();
    }
    
    @Override
    public void render(FluidPumpBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.translate(-0.5, -0.5, -0.5);
        int direction;
        direction = Direction.DOWN.get3DDataValue();
        if(blockEntity.getLinkBoolean(direction))
        {
            float height = (5.0F * ((Mth.clamp(blockEntity.getFluidLevelSide(direction), 0, FluidPumpBlockEntity.SECTION_CAPACITY)) / (float)FluidPumpBlockEntity.SECTION_CAPACITY));
            if(height > 0)
            {
                this.drawFluid(blockEntity.getFluidStack(direction).getFluid(), blockEntity, poseStack, buffer, 6.01F * 0.0625F, 0.01F * 0.0625F, 6.01F * 0.0625F, (height - 0.02F) * 0.0625F, (5 - 0.02F) * 0.0625F, (4 - 0.02F) * 0.0625F);
            }
        }
        direction = Direction.NORTH.get3DDataValue();
        if(blockEntity.getLinkBoolean(direction))
        {
            float height = (4.0F * ((Mth.clamp(blockEntity.getFluidLevelSide(direction), 0, FluidPumpBlockEntity.SECTION_CAPACITY)) / (float)FluidPumpBlockEntity.SECTION_CAPACITY));
            if(height > 0)
            {
                this.drawFluid(blockEntity.getFluidStack(direction).getFluid(), blockEntity, poseStack, buffer, 6.01F * 0.0625F, 6.01F * 0.0625F, 0.01F * 0.0625F, (4 - 0.02F) * 0.0625F, (height - 0.02F) * 0.0625F, (5 - 0.02F) * 0.0625F);
            }
        }
        direction = Direction.SOUTH.get3DDataValue();
        if(blockEntity.getLinkBoolean(direction))
        {
            float height = (4.0F * ((Mth.clamp(blockEntity.getFluidLevelSide(direction), 0, FluidPumpBlockEntity.SECTION_CAPACITY)) / (float)FluidPumpBlockEntity.SECTION_CAPACITY));
            if(height > 0)
            {
                this.drawFluid(blockEntity.getFluidStack(direction).getFluid(), blockEntity, poseStack, buffer, 6.01F * 0.0625F, 6.01F * 0.0625F, 11.01F * 0.0625F, (4 - 0.02F) * 0.0625F, (height - 0.02F) * 0.0625F, (5 - 0.02F) * 0.0625F);
            }
        }
        direction = Direction.WEST.get3DDataValue();
        if(blockEntity.getLinkBoolean(direction))
        {
            float height = (4.0F * ((Mth.clamp(blockEntity.getFluidLevelSide(direction), 0, FluidPumpBlockEntity.SECTION_CAPACITY)) / (float)FluidPumpBlockEntity.SECTION_CAPACITY));
            if(height > 0)
            {
                this.drawFluid(blockEntity.getFluidStack(direction).getFluid(), blockEntity, poseStack, buffer, 0.01F * 0.0625F, 6.01F * 0.0625F, 6.01F * 0.0625F, (5 - 0.02F) * 0.0625F, (height - 0.02F) * 0.0625F, (4 - 0.02F) * 0.0625F);
            }
        }
        direction = Direction.EAST.get3DDataValue();
        if(blockEntity.getLinkBoolean(direction))
        {
            float height = (4.0F * ((Mth.clamp(blockEntity.getFluidLevelSide(direction), 0, FluidPumpBlockEntity.SECTION_CAPACITY)) / (float)FluidPumpBlockEntity.SECTION_CAPACITY));
            if(height > 0)
            {
                this.drawFluid(blockEntity.getFluidStack(direction).getFluid(), blockEntity, poseStack, buffer, 11.01F * 0.0625F, 6.01F * 0.0625F, 6.01F * 0.0625F, (5 - 0.02F) * 0.0625F, (height - 0.02F) * 0.0625F, (4 - 0.02F) * 0.0625F);
            }
        }
        direction = Direction.UP.get3DDataValue();
        if(blockEntity.getLinkBoolean(direction))
        {
            float height = (4.0F * ((Mth.clamp(blockEntity.getFluidLevelSide(direction), 0, FluidPumpBlockEntity.SECTION_CAPACITY)) / (float)FluidPumpBlockEntity.SECTION_CAPACITY));
            if(height > 0)
            {
                this.drawFluid(blockEntity.getFluidStack(direction).getFluid(), blockEntity, poseStack, buffer, 6.01F * 0.0625F, 11.01F * 0.0625F, 6.01F * 0.0625F, (height - 0.02F) * 0.0625F, (5 - 0.02F) * 0.0625F, (4 - 0.02F) * 0.0625F);
            }
        }
    
        float height = (6.0F * ((Mth.clamp(blockEntity.getFluidLevelCenter(), 0, FluidPumpBlockEntity.SECTION_CAPACITY)) / (float)FluidPumpBlockEntity.SECTION_CAPACITY));
        if(height > 0)
        {
            this.drawFluid(blockEntity.getFluidStack(-1).getFluid(), blockEntity, poseStack, buffer, 5.01F * 0.0625F, 5.01F * 0.0625F, 5.01F * 0.0625F, (6 - 0.02F) * 0.0625F, (height - 0.02F) * 0.0625F, (6 - 0.02F) * 0.0625F);
        }
        
        poseStack.popPose();
    }
    
    private void drawFluid(Fluid fluid, FluidPumpBlockEntity be, PoseStack poseStack, MultiBufferSource bufferSource, float x, float y, float z, float width, float height, float depth)
    {
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
