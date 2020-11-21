package com.kingparity.betterpets.client.render.entity.layers;

import com.kingparity.betterpets.client.render.entity.model.BetterWolfModel;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.util.Reference;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BetterWolfCollarLayer extends LayerRenderer<BetterWolfEntity, BetterWolfModel<BetterWolfEntity>>
{
    private static final ResourceLocation BETTER_WOLF_COLLAR = new ResourceLocation(Reference.ID + ":textures/entity/wolf/wolf_collar.png");
    
    public BetterWolfCollarLayer(IEntityRenderer<BetterWolfEntity, BetterWolfModel<BetterWolfEntity>> renderer)
    {
        super(renderer);
    }
    
    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, BetterWolfEntity betterWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if(betterWolf.isTamed() && !betterWolf.isInvisible())
        {
            float[] color = betterWolf.getCollarColor().getColorComponentValues();
            renderCutoutModel(this.getEntityModel(), BETTER_WOLF_COLLAR, matrixStack, buffer, packedLight, betterWolf, color[0], color[1], color[2]);
        }
    }
}
