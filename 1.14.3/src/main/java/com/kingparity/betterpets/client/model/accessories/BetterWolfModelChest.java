package com.kingparity.betterpets.client.model.accessories;

import com.kingparity.betterpets.client.model.BetterWolfModel;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;

public class BetterWolfModelChest<T extends BetterWolfEntity> extends BetterWolfModel<T>
{
    protected RendererModel chest;
    
    public BetterWolfModelChest()
    {
        textureWidth = 256;
        textureHeight = 128;
    
        chest = new RendererModel(this);
        chest.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.addChild(chest);
        chest.cubeList.add(new ModelBox(chest, 59, 5, 3.0F, -5.0F, -2.5F, 2, 6, 7, 0.0F, false));
        chest.cubeList.add(new ModelBox(chest, 59, 5, -5.0F, -5.0F, -2.5F, 2, 6, 7, 0.0F, false));
        chest.cubeList.add(new ModelBox(chest, 64, 10, -3.0F, -5.0F, 2.5F, 6, 6, 2, 0.0F, false));
    }
    
    @Override
    public void func_212843_a_(T betterWolf, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        super.func_212843_a_(betterWolf, limbSwing, limbSwingAmount, partialTickTime);
        if(betterWolf.isLayingDown())
        {
            this.chest.rotateAngleX = 0;
            //this.chest.rotateAngleY = -10 * 0.0625F;
            this.chest.rotateAngleZ = 0.0F * 0.0625F;
        
            this.chest.offsetX = 4.0F * 0.0625F;
            this.chest.offsetY = -7.0F * 0.0625F;
            this.chest.offsetZ = 0.0F * 0.0625F;
        }
        else
        {
            this.chest.rotateAngleX = 0;
            this.chest.rotateAngleY = 0;
            this.chest.rotateAngleZ = 0;
    
            this.chest.offsetX = 0;
            this.chest.offsetY = 0;
            this.chest.offsetZ = 0;
        }
    }
    
    @Override
    public void func_212844_a_(T betterWolf, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        super.func_212844_a_(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
    }
}
