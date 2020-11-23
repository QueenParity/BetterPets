package com.kingparity.betterpets.client.render.blockentity;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.kingparity.betterpets.block.entity.WaterFilterTileEntity;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.BlockRenderView;

public class WaterFilterRenderer extends BlockEntityRenderer<WaterFilterTileEntity>
{
    public WaterFilterRenderer(BlockEntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }
    
    @Override
    public void render(WaterFilterTileEntity waterFilter, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
        /*matrixStack.push();
        {
            Direction direction = waterFilter.getCachedState().get(HorizontalFacingBlock.FACING);
            matrixStack.translate(0.5, 0.5, 0.5);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.getHorizontal() * -90F - 90F));
            matrixStack.translate(-0.5, -0.5, -0.5);
    
            matrixStack.translate(9.5 * 0.0625, 6.5 * 0.0625, 8 * 0.0625);
            ItemStack stack = new ItemStack(ModItems.WATER_FILTER_FABRIC_DISPLAY, 1);
            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.NONE, light, overlay, matrixStack, vertexConsumers);
        }
        matrixStack.pop();*/
        
        matrixStack.push();
        {
            Direction direction = waterFilter.getCachedState().get(HorizontalFacingBlock.FACING);
            matrixStack.translate(0.5, 0.5, 0.5);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.getHorizontal() * -90F - 90F));
            matrixStack.translate(-0.5, -0.5, -0.5);
            
            //FluidTank tankWater = waterFilter.getTankWater();
            //float heightTankWater = (float) ((tankWater.getCapacity() / 1000) * (((double)tankWater.getBuckets()) / ((double)tankWater.getCapacity())));
            
            //FluidTank tankFilteredWater = waterFilter.getTankFilteredWater();
            //float heightTankFilteredWater = (float) ((tankFilteredWater.getCapacity() / 1000) * (((double)tankFilteredWater.getBuckets()) / ((double)tankFilteredWater.getCapacity())));
            float heightTankWater = (float) ((waterFilter.fluidInv.getMaxAmount(0) / FluidVolume.BUCKET) * (((double)waterFilter.fluidInv.getInvFluid(0).getAmount()) / ((double)waterFilter.fluidInv.getMaxAmount(0))));
            float heightTankFilteredWater = (float) ((waterFilter.fluidInv.getMaxAmount(1) / FluidVolume.BUCKET) * (((double)waterFilter.fluidInv.getInvFluid(1).getAmount()) / ((double)waterFilter.fluidInv.getMaxAmount(1))));
            if(heightTankWater > 0)
            {
                drawFluid(waterFilter.fluidInv.getInvFluid(0).getRawFluid(), waterFilter, matrixStack, vertexConsumers, 1.01F * 0.0625F, 1.01F * 0.0625F, 1.01F * 0.0625F, (14 - 0.02F) * 0.0625F, heightTankWater * 0.0625F, (7 - 0.02F) * 0.0625F);
            }
            
            if(heightTankFilteredWater > 0)
            {
                drawFluid(waterFilter.fluidInv.getInvFluid(1).getRawFluid(), waterFilter, matrixStack, vertexConsumers, 1.01F * 0.0625F, 1.01F * 0.0625F, 8.01F * 0.0625F, (14 - 0.02F) * 0.0625F, heightTankFilteredWater * 0.0625F, (7 - 0.02F) * 0.0625F);
            }
        }
        matrixStack.pop();
    }
    
    private void drawFluid(Fluid fluid, WaterFilterTileEntity waterFilter, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, float x, float y, float z, float width, float height, float depth)
    {
        if(fluid == Fluids.EMPTY)
        {
            return;
        }
        
        Sprite sprite = FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidSprites(waterFilter.getWorld(), waterFilter.getPos(), fluid.getDefaultState())[0];
        int waterColor = FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidColor(waterFilter.getWorld(), waterFilter.getPos(), fluid.getDefaultState());
        
        float minU = sprite.getMinU();
        float maxU = Math.min(minU + (sprite.getMaxU() - minU) * width, sprite.getMaxU());
        float minV = sprite.getMinV();
        float maxV = Math.min(minV + (sprite.getMaxV() - minV) * height, sprite.getMaxV());
        float red = (float)(waterColor >> 16 & 255) / 255.0F;
        float green = (float)(waterColor >> 8 & 255) / 255.0F;
        float blue = (float)(waterColor & 255) / 255.0F;
        int light = getCombinedLight(waterFilter.getWorld(), waterFilter.getPos());
        
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
        Matrix4f matrix = matrixStack.peek().getModel();
        
        //left side
        buffer.vertex(matrix, x + width, y, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y + height, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        
        //right side
        buffer.vertex(matrix, x, y, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y + height, z + depth).color(red * 0.75F, green * 0.75F, blue * 0.75F, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        
        maxU = Math.min(minU + (sprite.getMaxU() - minU) * depth, sprite.getMaxU());
        
        //back side
        buffer.vertex(matrix, x + width, y, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        
        //front side
        buffer.vertex(matrix, x, y + height, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y + height, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y, z).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y, z + depth).color(red * 0.85F, green * 0.85F, blue * 0.85F, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        
        maxV = Math.min(minV + (sprite.getMaxV() - minV) * width, sprite.getMaxV());
        
        //top
        buffer.vertex(matrix, x, y + height, z).color(red, green, blue, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y + height, z + depth).color(red, green, blue, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z + depth).color(red, green, blue, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y + height, z).color(red, green, blue, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        
        //bottom
        buffer.vertex(matrix, x + width, y, z + depth).color(red, green, blue, 1.0F).texture(maxU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x + width, y, z).color(red, green, blue, 1.0F).texture(minU, minV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y, z).color(red, green, blue, 1.0F).texture(minU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
        buffer.vertex(matrix, x, y, z + depth).color(red, green, blue, 1.0F).texture(maxU, maxV).light(light).normal(0.0F, 1.0F, 0.0F).next();
    }
    
    public static int getCombinedLight(BlockRenderView lightReader, BlockPos pos)
    {
        int i = WorldRenderer.getLightmapCoordinates(lightReader, pos);
        int j = WorldRenderer.getLightmapCoordinates(lightReader, pos.up());
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
    }
}