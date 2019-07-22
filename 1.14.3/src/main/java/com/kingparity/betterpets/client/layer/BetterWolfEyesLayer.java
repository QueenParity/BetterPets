package com.kingparity.betterpets.client.layer;

import com.kingparity.betterpets.client.model.BetterWolfModel;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.util.Reference;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BetterWolfEyesLayer extends LayerRenderer<BetterWolfEntity, BetterWolfModel<BetterWolfEntity>>
{
    private static final ResourceLocation EYES_TAMED = new ResourceLocation(Reference.ID + ":textures/entity/wolf/better_wolf_eyes_tamed.png");
    private static final ResourceLocation EYES_SLEEPING = new ResourceLocation(Reference.ID + ":textures/entity/wolf/better_wolf_eyes_sleeping.png");
    private static final ResourceLocation EYES_ANGRY = new ResourceLocation(Reference.ID + ":textures/entity/wolf/better_wolf_eyes_angry.png");
    
    public BetterWolfEyesLayer(IEntityRenderer<BetterWolfEntity, BetterWolfModel<BetterWolfEntity>> model)
    {
        super(model);
    }
    
    @Override
    public void render(BetterWolfEntity betterWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if(!betterWolf.isInvisible())
        {
            if(betterWolf.isTamed())
            {
                if(betterWolf.isAngry())
                {
                    this.bindTexture(EYES_ANGRY);
                }
                else if(betterWolf.isSleeping())
                {
                    this.bindTexture(EYES_SLEEPING);
                }
                else
                {
                    this.bindTexture(EYES_TAMED);
                }
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.getEntityModel().render(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
            else if(betterWolf.isAngry())
            {
                this.bindTexture(EYES_ANGRY);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.getEntityModel().render(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
    }
    
    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }
}