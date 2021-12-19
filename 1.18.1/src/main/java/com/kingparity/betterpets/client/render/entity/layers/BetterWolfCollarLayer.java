package com.kingparity.betterpets.client.render.entity.layers;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.client.render.entity.model.BetterWolfModel;
import com.kingparity.betterpets.entity.BetterWolf;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class BetterWolfCollarLayer extends RenderLayer<BetterWolf, BetterWolfModel<BetterWolf>>
{
    private static final ResourceLocation BETTER_WOLF_COLLAR = new ResourceLocation(BetterPets.ID + ":textures/entity/wolf/wolf_collar.png");
    
    public BetterWolfCollarLayer(RenderLayerParent<BetterWolf, BetterWolfModel<BetterWolf>> renderer)
    {
        super(renderer);
    }
    
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, BetterWolf betterWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if(betterWolf.isTame() && !betterWolf.isInvisible())
        {
            float[] color = betterWolf.getCollarColor().getTextureDiffuseColors();
            renderColoredCutoutModel(this.getParentModel(), BETTER_WOLF_COLLAR, poseStack, buffer, packedLight, betterWolf, color[0], color[1], color[2]);
        }
    }
}
