package com.william.betterpets.client.model;
//Made with Blockbench
//Paste this code into your mod.

import com.william.betterpets.entity.BetterWolfEntity;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;

public class BetterWolfModelArmor<T extends BetterWolfEntity> extends BetterWolfModel<T>
{
    public BetterWolfModelArmor(float scale)
    {
        textureWidth = 256;
        textureHeight = 128;
    
        front_leg_left = new RendererModel(this);
        front_leg_left.setRotationPoint(0.5F, 16.0F, -4.0F);
        front_leg_left.cubeList.add(new ModelBox(front_leg_left, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, scale, false));
    
        front_leg_right = new RendererModel(this);
        front_leg_right.setRotationPoint(-2.5F, 16.0F, -4.0F);
        front_leg_right.cubeList.add(new ModelBox(front_leg_right, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, scale, false));
    
        back_leg_left = new RendererModel(this);
        back_leg_left.setRotationPoint(0.5F, 16.0F, 7.0F);
        back_leg_left.cubeList.add(new ModelBox(back_leg_left, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, scale, false));
    
        back_leg_right = new RendererModel(this);
        back_leg_right.setRotationPoint(-2.5F, 16.0F, 7.0F);
        back_leg_right.cubeList.add(new ModelBox(back_leg_right, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, scale, false));
        
        mane = new RendererModel(this);
        mane.setRotationPoint(-1.0F, 14.0F, -3.0F);
        //setRotationAngle(mane, 1.5708F, 0.0F, 0.0F);
        mane.cubeList.add(new ModelBox(mane, 21, 0, -3.0F, -3.0F, -3.0F, 8, 6, 7, scale, false));
    
        body = new RendererModel(this);
        body.setRotationPoint(0.0F, 14.0F, 2.0F);
        //setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
        body.cubeList.add(new ModelBox(body, 18, 14, -3.0F, -2.0F, -3.0F, 6, 9, 6, scale, false));
    
        /*head = new RendererModel(this);
        head.setRotationPoint(-1.0F, 13.5F, -7.0F);
        head.cubeList.add(new ModelBox(head, 0, 0, -2.0F, -3.0F, -2.0F, 6, 6, 4, scale, false));
        head.cubeList.add(new ModelBox(head, 16, 14, 2.0F, -5.0F, 0.0F, 2, 2, 1, scale, false));
        head.cubeList.add(new ModelBox(head, 16, 14, -2.0F, -5.0F, 0.0F, 2, 2, 1, scale, false));
        head.cubeList.add(new ModelBox(head, 0, 10, -0.5F, 0.0F, -5.0F, 3, 3, 4, scale, false));*/
    
        tail = new RendererModel(this);
        tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        tail.cubeList.add(new ModelBox(tail, 9, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, scale, false));
        
        /*body = new RendererModel(this);
        body.setRotationPoint(0.0F, 14.0F, 2.0F);
        setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
        body.cubeList.add(new ModelBox(body, 18, 14, -3.0F, -2.0F, -3.0F, 6, 9, 6, 0.0F, false));
        
        head = new RendererModel(this);
        head.setRotationPoint(-1.0F, 13.5F, -7.0F);
        head.cubeList.add(new ModelBox(head, 0, 0, -3.0F, -13.5F, -10.0F, 6, 6, 4, 0.0F, false));
        
        nose = new RendererModel(this);
        nose.setRotationPoint(-1.0F, 13.5F, -7.0F);
        nose.cubeList.add(new ModelBox(nose, 0, 10, -1.5F, -10.5F, -13.0F, 3, 3, 4, 0.0F, false));
        
        ear_right = new RendererModel(this);
        ear_right.setRotationPoint(-1.0F, 13.5F, -7.0F);
        ear_right.cubeList.add(new ModelBox(ear_right, 16, 14, -3.0F, -15.5F, -7.0F, 2, 2, 1, 0.0F, false));
        
        ear_left = new RendererModel(this);
        ear_left.setRotationPoint(-1.0F, 13.5F, -7.0F);
        ear_left.cubeList.add(new ModelBox(ear_left, 16, 14, 1.0F, -15.5F, -7.0F, 2, 2, 1, 0.0F, false));
        
        tail = new RendererModel(this);
        tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        tail.cubeList.add(new ModelBox(tail, 9, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));*/
    }
}