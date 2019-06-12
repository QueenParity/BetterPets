package com.william.betterpets.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.william.betterpets.client.model.BetterWolfModel;
import com.william.betterpets.entity.BetterWolfEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BetterWolfCollarLayer extends LayerRenderer<BetterWolfEntity, BetterWolfModel<BetterWolfEntity>>
{
    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("minecraft:textures/entity/wolf/wolf_collar.png");
    
    public BetterWolfCollarLayer(IEntityRenderer<BetterWolfEntity, BetterWolfModel<BetterWolfEntity>> model)
    {
        super(model);
    }
    
    @Override
    public void func_212842_a_(BetterWolfEntity betterWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if(betterWolf.isTamed() && !betterWolf.isInvisible())
        {
            this.func_215333_a(WOLF_COLLAR);
            float[] afloat = betterWolf.getCollarColor().getColorComponentValues();
            GlStateManager.color3f(afloat[0], afloat[1], afloat[2]);
            this.func_215332_c().render(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }
}