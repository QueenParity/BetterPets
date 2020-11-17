package com.kingparity.betterpets.client.render.tileentity;

import com.kingparity.betterpets.tileentity.TankTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.ForgeHooksClient;

public class TankRenderer extends TileEntityRenderer<TankTileEntity>
{
    public TankRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
    }
    
    @Override
    public void render(TankTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay)
    {
        matrixStack.push();
        float height = (float) (16.0 * (tileEntity.getFluidLevel() / (double) tileEntity.getCapacity()));
        if(height > 0)
        {
            this.drawFluid(tileEntity, matrixStack, typeBuffer, 2.01F * 0.0625F, 0.0F, 2.01F * 0.0625F, (12 - 0.02F) * 0.0625F, height * 0.0625F, (12 - 0.02F) * 0.0625F);
        }
        matrixStack.pop();
    }
    
    private void drawFluid(TankTileEntity te, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, float x, float y, float z, float width, float height, float depth)
    {
        Fluid fluid = te.getFluidStackTank().getFluid();
        if(fluid == Fluids.EMPTY) return;
    
        TextureAtlasSprite sprite = ForgeHooksClient.getFluidSprites(te.getWorld(), te.getPos(), fluid.getDefaultState())[0];
        float minU = sprite.getMinU();
        float maxU = Math.min(minU + (sprite.getMaxU() - minU) * width, sprite.getMaxU());
        float minV = sprite.getMinV();
        float maxV = Math.min(minV + (sprite.getMaxV() - minV) * height, sprite.getMaxV());
        int waterColor = fluid.getAttributes().getColor(te.getWorld(), te.getPos());
        float red = (float) (waterColor >> 16 & 255) / 255.0F;
        float green = (float) (waterColor >> 8 & 255) / 255.0F;
        float blue = (float) (waterColor & 255) / 255.0F;
        int light = this.getCombinedLight(te.getWorld(), te.getPos());
    
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
        
        boolean renderTop = false;
        
        TileEntity tileEntityAbove = te.getWorld().getTileEntity(te.getPos().up());
        TankTileEntity tankAbove = null;
        if(tileEntityAbove instanceof TankTileEntity)
        {
            tankAbove = (TankTileEntity) tileEntityAbove;
        }
        
        if(te.getFluidLevel() < te.getCapacity())
        {
            renderTop = true;
        }
        else if(tankAbove != null)
        {
            if(tankAbove.getFluidLevel() == 0)
            {
                renderTop = true;
            }
        }
        
        if(te.getFluidLevel() == te.getCapacity() && tankAbove == null)
        {
            renderTop = false;
        }
        
        if(renderTop)
        {
            //top
            buffer.pos(matrix, x, y + height, z).color(red, green, blue, 1.0F).tex(maxU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
            buffer.pos(matrix, x, y + height, z + depth).color(red, green, blue, 1.0F).tex(minU, minV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
            buffer.pos(matrix, x + width, y + height, z + depth).color(red, green, blue, 1.0F).tex(minU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
            buffer.pos(matrix, x + width, y + height, z).color(red, green, blue, 1.0F).tex(maxU, maxV).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        }
    }
    
    private int getCombinedLight(IBlockDisplayReader lightReader, BlockPos pos)
    {
        int i = WorldRenderer.getCombinedLight(lightReader, pos);
        int j = WorldRenderer.getCombinedLight(lightReader, pos.up());
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
    }
}
