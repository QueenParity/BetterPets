package com.kingparity.betterpets.client.render.entity;

import com.kingparity.betterpets.BetterPets;
import com.kingparity.betterpets.client.render.entity.layers.BetterWolfCollarLayer;
import com.kingparity.betterpets.client.render.entity.model.BetterWolfModel;
import com.kingparity.betterpets.client.render.entity.model.BetterWolfModelLayers;
import com.kingparity.betterpets.entity.BetterWolf;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BetterWolfRenderer extends MobRenderer<BetterWolf, BetterWolfModel<BetterWolf>>
{
    public static final ResourceLocation WOLF_TEXTURES = new ResourceLocation(BetterPets.ID + ":textures/entity/wolf/wolf.png");
    public static final ResourceLocation TAMED_WOLF_TEXTURES = new ResourceLocation(BetterPets.ID + ":textures/entity/wolf/wolf_tame.png");
    public static final ResourceLocation ANGRY_WOLF_TEXTURES = new ResourceLocation(BetterPets.ID + ":textures/entity/wolf/wolf_angry.png");
    
    public BetterWolfRenderer(EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new BetterWolfModel<>(renderManager.bakeLayer(BetterWolfModelLayers.BETTER_WOLF)), 0.5F);
        this.addLayer(new BetterWolfCollarLayer(this));
    }
    
    @Override
    protected float getBob(BetterWolf betterWolf, float p_115306_)
    {
        return betterWolf.getTailAngle();
    }
    
    public void render(BetterWolf betterWolf, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        if(betterWolf.isWet())
        {
            float f = betterWolf.getWetShade(partialTicks);
            this.model.setColor(f, f, f);
        }
        
        super.render(betterWolf, entityYaw, partialTicks, poseStack, buffer, packedLight);
        if(betterWolf.isWet())
        {
            this.model.setColor(1.0F, 1.0F, 1.0F);
        }
    }
    
    @Override
    public ResourceLocation getTextureLocation(BetterWolf betterWolf)
    {
        if(betterWolf.isTame())
        {
            return TAMED_WOLF_TEXTURES;
        }
        else
        {
            return betterWolf.isAngry() ? ANGRY_WOLF_TEXTURES : WOLF_TEXTURES;
        }
    }
}