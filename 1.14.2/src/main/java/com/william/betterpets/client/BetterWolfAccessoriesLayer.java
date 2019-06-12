package com.william.betterpets.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.william.betterpets.client.model.BetterWolfModel;
import com.william.betterpets.client.model.BetterWolfModelChest;
import com.william.betterpets.client.model.BetterWolfModelHat;
import com.william.betterpets.entity.BetterWolfEntity;
import com.william.betterpets.init.BetterPetItems;
import com.william.betterpets.util.Reference;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BetterWolfAccessoriesLayer<T extends BetterWolfEntity> extends LayerRenderer<T, BetterWolfModel<T>>
{
    private BetterWolfModelChest<T> chest;
    private BetterWolfModelHat<T> hat;
    
    public BetterWolfAccessoriesLayer(IEntityRenderer<T, BetterWolfModel<T>> model)
    {
        super(model);
    }
    
    @Override
    public void func_212842_a_(T betterWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.func_215333_a(new ResourceLocation(Reference.ID + ":textures/entity/better_wolf_chest2.png"));
        //this.renderer.bindTexture(new ResourceLocation("minecraft:textures/block/snow.png"));
        
        if(betterWolf.hasChest())
        {
            this.chest = new BetterWolfModelChest<>();
            this.chest.setLivingAnimations(betterWolf, limbSwing, limbSwingAmount, partialTicks);
            GlStateManager.color4f(1, 1, 1, 1);
            this.chest.render(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if(betterWolf.getHatItem().getItem() == BetterPetItems.PET_BIRTHDAY_HAT)
        {
            this.hat = new BetterWolfModelHat<>();
            this.hat.setLivingAnimations(betterWolf, limbSwing, limbSwingAmount, partialTicks);
            GlStateManager.color4f(1, 1, 1, 1);
            this.hat.render(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}
