package com.kingparity.betterpets.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.IForgeBakedModel;

import java.util.List;
import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class RenderUtil
{
    /**
     * Draws a textured modal rectangle with more precision than GuiScreen's methods. This will only
     * work correctly if the bound texture is 256x256.
     */
    public static void drawTexturedModalRect(double x, double y, int textureX, int textureY, double width, double height)
    {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(x, y + height, 0).uv(((float) textureX * 0.00390625F), ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.vertex(x + width, y + height, 0).uv(((float) (textureX + width) * 0.00390625F), ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.vertex(x + width, y, 0).uv(((float) (textureX + width) * 0.00390625F), ((float) textureY * 0.00390625F)).endVertex();
        bufferbuilder.vertex(x + 0, y, 0).uv(((float) textureX * 0.00390625F), ((float) textureY * 0.00390625F)).endVertex();
        tesselator.end();
    }

    /**
     * Draws a rectangle with a horizontal gradient between the specified colors (ARGB format).
     */
    public static void drawGradientRectHorizontal(int left, int top, int right, int bottom, int leftColor, int rightColor)
    {
        float redStart = (float)(leftColor >> 24 & 255) / 255.0F;
        float greenStart = (float)(leftColor >> 16 & 255) / 255.0F;
        float blueStart = (float)(leftColor >> 8 & 255) / 255.0F;
        float alphaStart = (float)(leftColor & 255) / 255.0F;
        float redEnd = (float)(rightColor >> 24 & 255) / 255.0F;
        float greenEnd = (float)(rightColor >> 16 & 255) / 255.0F;
        float blueEnd = (float)(rightColor >> 8 & 255) / 255.0F;
        float alphaEnd = (float)(rightColor & 255) / 255.0F;
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        //RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        //RenderSystem.shadeModel(7425);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex((double)right, (double)top, 0).color(greenEnd, blueEnd, alphaEnd, redEnd).endVertex();
        bufferbuilder.vertex((double)left, (double)top, 0).color(greenStart, blueStart, alphaStart, redStart).endVertex();
        bufferbuilder.vertex((double)left, (double)bottom, 0).color(greenStart, blueStart, alphaStart, redStart).endVertex();
        bufferbuilder.vertex((double)right, (double)bottom, 0).color(greenEnd, blueEnd, alphaEnd, redEnd).endVertex();
        tesselator.end();
        //RenderSystem.shadeModel(7424);
        RenderSystem.disableBlend();
        //RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    /*public static void scissor(int x, int y, int width, int height) //TODO might need fixing. I believe I rewrote this in a another mod
    {
        Minecraft mc = Minecraft.getInstance();
        int scale = (int) mc.getMainWindow().getGuiScaleFactor();
        GL11.glScissor(x * scale, mc.getMainWindow().getHeight() - y * scale - height * scale, Math.max(0, width * scale), Math.max(0, height * scale));
    }*/

    /*public static IBakedModel getModel(ItemStack stack)
    {
        return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack);
    }*/

    /*public static void renderColoredModel(IBakedModel model, ItemCameraTransforms.TransformType transformType, boolean leftHanded, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int color, int lightTexture, int overlayTexture)
    {
        matrixStack.push();
        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        matrixStack.translate(-0.5, -0.5, -0.5);
        if(!model.isBuiltInRenderer())
        {
            IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(Atlases.getCutoutBlockType());
            renderModel(model, ItemStack.EMPTY, color, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        }
        matrixStack.pop();
    }*/

    /*public static void renderModel(ItemStack stack, ItemCameraTransforms.TransformType transformType, boolean leftHanded, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int lightTexture, int overlayTexture, IBakedModel model)
    {
        if(!stack.isEmpty())
        {
            matrixStack.push();
            boolean isGui = transformType == ItemCameraTransforms.TransformType.GUI;
            boolean tridentFlag = isGui || transformType == ItemCameraTransforms.TransformType.GROUND || transformType == ItemCameraTransforms.TransformType.FIXED;
            if(stack.getItem() == Items.TRIDENT && tridentFlag)
            {
                model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation("minecraft:trident#inventory"));
            }

            model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
            matrixStack.translate(-0.5, -0.5, -0.5);
            if(!model.isBuiltInRenderer() && (stack.getItem() != Items.TRIDENT || tridentFlag))
            {
                RenderType renderType = RenderTypeLookup.func_239219_a_(stack, false); //TODO test what this flag does
                if(isGui && Objects.equals(renderType, Atlases.getTranslucentCullBlockType()))
                {
                    renderType = Atlases.getTranslucentCullBlockType();
                }
                IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(renderTypeBuffer, renderType, true, stack.hasEffect());
                renderModel(model, stack, -1, lightTexture, overlayTexture, matrixStack, vertexBuilder);
            }
            else
            {
                stack.getItem().getItemStackTileEntityRenderer().func_239207_a_(stack, transformType, matrixStack, renderTypeBuffer, lightTexture, overlayTexture);
            }

            matrixStack.pop();
        }
    }*/

    private static void renderModel(IForgeBakedModel model, ItemStack stack, int color, int lightTexture, int overlayTexture, PoseStack poseStack, VertexConsumer vertexConsumer)
    {
        Random random = new Random();
        for(Direction direction : Direction.values())
        {
            random.setSeed(42L);
            renderQuads(poseStack, vertexConsumer, model.getQuads(null, direction, random, null), stack, color, lightTexture, overlayTexture);
        }
        random.setSeed(42L);
        renderQuads(poseStack, vertexConsumer, model.getQuads(null, null, random, null), stack, color, lightTexture, overlayTexture);
    }

    private static void renderQuads(PoseStack poseStack, VertexConsumer vertexConsumer, List<BakedQuad> quads, ItemStack stack, int color, int lightTexture, int overlayTexture)
    {
        boolean useItemColor = !stack.isEmpty() && color == -1;
        PoseStack.Pose pose = poseStack.last();
        for(BakedQuad quad : quads)
        {
            int tintColor = 0xFFFFFF;
            if(quad.isTinted())
            {
                if(useItemColor)
                {
                    tintColor = Minecraft.getInstance().getItemColors().getColor(stack, quad.getTintIndex());
                }
                else
                {
                    tintColor = color;
                }
            }
            float red = (float) (tintColor >> 16 & 255) / 255.0F;
            float green = (float) (tintColor >> 8 & 255) / 255.0F;
            float blue = (float) (tintColor & 255) / 255.0F;
            vertexConsumer.putBulkData(pose, quad, red, green, blue, lightTexture, overlayTexture, true);
        }
    }
}
