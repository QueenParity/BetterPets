package com.kingparity.betterpets.client.model.accessories;

import com.kingparity.betterpets.client.model.BetterWolfModel;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;

public class BetterWolfModelHat<T extends BetterWolfEntity> extends BetterWolfModel<T>
{
    protected RendererModel hat;
    
    public BetterWolfModelHat()
    {
        textureWidth = 256;
        textureHeight = 128;
        
        hat = new RendererModel(this);
        hat.setRotationPoint(0.0F, -3.0F, -2.5F);
        head.addChild(hat);
        hat.cubeList.add(new ModelBox(hat, 34, 37, -2.5F, -3.0F, 0.0F, 7, 3, 7, 0.0F, false));
        hat.cubeList.add(new ModelBox(hat, 68, 37, -2.0F, -4.0F, 0.5F, 6, 1, 6, 0.0F, false));
        hat.cubeList.add(new ModelBox(hat, 58, 18, -1.5F, -7.0F, 5.0F, 1, 3, 1, 0.0F, false));
        hat.cubeList.add(new ModelBox(hat, 74, 18, 2.5F, -7.0F, 5.0F, 1, 3, 1, 0.0F, false));
        hat.cubeList.add(new ModelBox(hat, 66, 18, 2.5F, -7.0F, 1.0F, 1, 3, 1, 0.0F, false));
        hat.cubeList.add(new ModelBox(hat, 62, 18, -1.5F, -7.0F, 1.0F, 1, 3, 1, 0.0F, false));
        hat.cubeList.add(new ModelBox(hat, 70, 18, 0.5F, -7.0F, 3.0F, 1, 3, 1, 0.0F, false));
    }
    
    @Override
    public void setLivingAnimations(T betterWolf, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        super.setLivingAnimations(betterWolf, limbSwing, limbSwingAmount, partialTickTime);
        if(betterWolf.isLayingDown())
        {
            hat.offsetX = 8.0F * 0.0625F;
            hat.offsetY = 6F * 0.0625F;
            hat.offsetZ = -5.0F * 0.0625F;
        }
        else if(betterWolf.isSitting())
        {
            hat.rotateAngleX = -0.2F;
            hat.rotateAngleY = 0.0F;
            hat.rotateAngleZ = 0.0F;
        }
        else
        {
            hat.rotateAngleX = 0.0F;
            hat.rotateAngleY = 0.0F;
            hat.rotateAngleZ = 0.0F;
        }
    }
    
    @Override
    public void setRotationAngles(T betterWolf, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        super.setRotationAngles(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
    }
}
