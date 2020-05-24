package com.kingparity.betterpets.client.render.tileentity;

import com.kingparity.betterpets.block.PetHorizontalBlock;
import com.kingparity.betterpets.tileentity.WaterCollectorTileEntity;
import com.kingparity.betterpets.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.ForgeHooksClient;

public class WaterCollectorRenderer extends TileEntityRenderer<WaterCollectorTileEntity>
{
    public WaterCollectorRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    public void render(WaterCollectorTileEntity waterCollector, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int lightTexture, int overlayTexture)
    {
        matrixStack.push();
        {
            Direction direction = waterCollector.getBlockState().get(PetHorizontalBlock.DIRECTION);
            matrixStack.translate(0.5, 0.5, 0.5);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(direction.getHorizontalIndex() * -90F - 90F));
            matrixStack.translate(-0.5, -0.5, -0.5);
            float height = (float) (13.0 * (waterCollector.getFluidAmount() / (double) waterCollector.getCapacity()));
            if(height > 0)
            {
                drawFluid(waterCollector, matrixStack, typeBuffer, 2.01F * 0.0625F, 8.01F * 0.0625F, 2.01F * 0.0625F, (12 - 0.02F) * 0.0625F, height * 0.0625F, (12 - 0.02F) * 0.0625F);
            }
        }
        matrixStack.pop();
    }

    private void drawFluid(WaterCollectorTileEntity waterCollector, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, float x, float y, float z, float width, float height, float depth)
    {
        Fluid fluid = waterCollector.getFluidTank().getFluid().getFluid();
        if(fluid == Fluids.EMPTY)
        {
            return;
        }

        TextureAtlasSprite sprite = ForgeHooksClient.getFluidSprites(waterCollector.getWorld(), waterCollector.getPos(), fluid.getDefaultState())[0];

        //int i = BiomeColors.getWaterColor(waterCollector.getWorld(), waterCollector.getPos());

        float minU = sprite.getMinU();
        float maxU = Math.min(minU + (sprite.getMaxU() - minU) * width, sprite.getMaxU());
        float minV = sprite.getMinV();
        float maxV = Math.min(minV + (sprite.getMaxV() - minV) * height, sprite.getMaxV());
        int waterColor = fluid.getAttributes().getColor(waterCollector.getWorld(), waterCollector.getPos());
        float red = (float)(waterColor >> 16 & 255) / 255.0F;
        float green = (float)(waterColor >> 8 & 255) / 255.0F;
        float blue = (float)(waterColor & 255) / 255.0F;
        int light = RenderUtil.getCombinedLight(waterCollector.getWorld(), waterCollector.getPos());

        IVertexBuilder buffer = typeBuffer.getBuffer(RenderType.getTranslucent());
        Matrix4f matrix = matrixStack.getLast().getMatrix();

        //left side
        buffer.pos(matrix, x + width, y, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).tex(maxU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x, y, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).tex(minU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x, y + height, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).tex(minU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x + width, y + height, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).tex(maxU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        //right side
        buffer.pos(matrix, x, y, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).tex(maxU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x + width, y, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).tex(minU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x + width, y + height, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).tex(minU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x, y + height, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).tex(maxU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        maxU = Math.min(minU + (sprite.getMaxU() - minU) * depth, sprite.getMaxU());

        //back side
        buffer.pos(matrix, x + width, y, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).tex(maxU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x + width, y, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).tex(minU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x + width, y + height, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).tex(minU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x + width, y + height, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).tex(maxU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        //front side
        buffer.pos(matrix, x, y + height, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).tex(maxU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x, y + height, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).tex(minU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x, y, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).tex(minU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x, y, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).tex(maxU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        maxV = Math.min(minV + (sprite.getMaxV() - minV) * width, sprite.getMaxV());

        //top
        buffer.pos(matrix, x, y + height, z).color(red, green, blue, 1.0F).tex(maxU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x, y + height, z + depth).color(red, green, blue, 1.0F).tex(minU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x + width, y + height, z + depth).color(red, green, blue, 1.0F).tex(minU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x + width, y + height, z).color(red, green, blue, 1.0F).tex(maxU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        //bottom
        buffer.pos(matrix, x + width, y, z + depth).color(red, green, blue, 1.0F).tex(maxU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x + width, y, z).color(red, green, blue, 1.0F).tex(minU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x, y, z).color(red, green, blue, 1.0F).tex(minU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, x, y, z + depth).color(red, green, blue, 1.0F).tex(maxU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
    }
}
