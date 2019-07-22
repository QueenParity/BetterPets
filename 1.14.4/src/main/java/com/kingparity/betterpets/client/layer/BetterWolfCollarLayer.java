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
public class BetterWolfCollarLayer extends LayerRenderer<BetterWolfEntity, BetterWolfModel<BetterWolfEntity>>
{
    private static final ResourceLocation COLLAR = new ResourceLocation(Reference.ID + ":textures/entity/wolf/better_wolf_collar.png");
    
    public BetterWolfCollarLayer(IEntityRenderer<BetterWolfEntity, BetterWolfModel<BetterWolfEntity>> model)
    {
        super(model);
    }
    
    @Override
    public void render(BetterWolfEntity betterWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if(betterWolf.isTamed() && !betterWolf.isInvisible())
        {
            this.bindTexture(COLLAR);
            float[] afloat = betterWolf.getCollarColor().getColorComponentValues();
            GlStateManager.color3f(afloat[0], afloat[1], afloat[2]);
            this.getEntityModel().render(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }
}