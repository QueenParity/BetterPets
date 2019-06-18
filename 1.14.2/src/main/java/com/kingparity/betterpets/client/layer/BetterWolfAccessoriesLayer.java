package com.kingparity.betterpets.client.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.kingparity.betterpets.client.model.BetterWolfModel;
import com.kingparity.betterpets.client.model.accessories.BetterWolfModelChest;
import com.kingparity.betterpets.client.model.accessories.BetterWolfModelHat;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.init.BetterPetItems;
import com.kingparity.betterpets.util.Reference;
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
    public void render(T betterWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.bindTexture(new ResourceLocation(Reference.ID + ":textures/entity/better_wolf_chest2.png"));
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
