package com.william.betterpets.client.model;

import com.william.betterpets.entity.BetterWolfEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BetterWolfModel<T extends BetterWolfEntity> extends EntityModel<T>
{
    protected RendererModel front_leg_left;
    protected RendererModel front_leg_right;
    protected RendererModel back_leg_left;
    protected RendererModel back_leg_right;
    protected RendererModel mane;
    protected RendererModel body;
    protected RendererModel head;
    /*private final RendererModel nose;
    private final RendererModel ear_right;
    private final RendererModel ear_left;*/
    protected RendererModel tail;
    
    /*protected RendererModel armor_mane;
    protected RendererModel armor_head;
    protected RendererModel armor_front_leg_left;
    protected RendererModel armor_back_leg_left;
    protected RendererModel armor_front_leg_right;
    protected RendererModel armor_back_leg_right;
    protected RendererModel armor_body;
    protected RendererModel armor_tail;*/
    
    public BetterWolfModel()
    {
        textureWidth = 256;
        textureHeight = 128;
        
        front_leg_left = new RendererModel(this);
        front_leg_left.setRotationPoint(0.5F, 16.0F, -4.0F);
        front_leg_left.cubeList.add(new ModelBox(front_leg_left, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));
        
        front_leg_right = new RendererModel(this);
        front_leg_right.setRotationPoint(-2.5F, 16.0F, -4.0F);
        front_leg_right.cubeList.add(new ModelBox(front_leg_right, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));
        
        back_leg_left = new RendererModel(this);
        back_leg_left.setRotationPoint(0.5F, 16.0F, 7.0F);
        back_leg_left.cubeList.add(new ModelBox(back_leg_left, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));
        
        back_leg_right = new RendererModel(this);
        back_leg_right.setRotationPoint(-2.5F, 16.0F, 7.0F);
        back_leg_right.cubeList.add(new ModelBox(back_leg_right, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));
        
        mane = new RendererModel(this);
        mane.setRotationPoint(-1.0F, 14.0F, -3.0F);
        //setRotationAngle(mane, 1.5708F, 0.0F, 0.0F);
        mane.cubeList.add(new ModelBox(mane, 21, 0, -3.0F, -3.0F, -3.0F, 8, 6, 7, 0.0F, false));
        
        body = new RendererModel(this);
        body.setRotationPoint(0.0F, 14.0F, 2.0F);
        //setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
        body.cubeList.add(new ModelBox(body, 18, 14, -3.0F, -2.0F, -3.0F, 6, 9, 6, 0.0F, false));
        
        head = new RendererModel(this);
        head.setRotationPoint(-1.0F, 13.5F, -7.0F);
        head.cubeList.add(new ModelBox(head, 0, 0, -2.0F, -3.0F, -2.0F, 6, 6, 4, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 16, 14, 2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 16, 14, -2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 0, 10, -0.5F, 0.0F, -5.0F, 3, 3, 4, 0.0F, false));
        
        tail = new RendererModel(this);
        tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        tail.cubeList.add(new ModelBox(tail, 9, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));
        
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
    
    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(T betterWolf, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        front_leg_left.render(scale);
        front_leg_right.render(scale);
        back_leg_left.render(scale);
        back_leg_right.render(scale);
        mane.render(scale);
        body.render(scale);
        head.render(scale);
        /*nose.render(scale);
        ear_right.render(scale);
        ear_left.render(scale);*/
        tail.render(scale);
    }
    
    public void setRotationAngle(RendererModel RendererModel, float x, float y, float z)
    {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
    
    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    @Override
    public void setLivingAnimations(T betterWolf, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        /*if (betterWolf.isAngry())
        {
            this.tail.rotateAngleY = 0.0F;
        }
        else
        {*/
        this.tail.rotateAngleY = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        //}
      
      /*if(betterWolf.isLayingDown())
      {
         this.body.setRotationPoint(0.0F, 14.0F, 2.0F);
         this.body.rotateAngleX = ((float)Math.PI / 2F);
         this.body.setRotationPoint(0.0F, 14.0F, -2F);
         this.body.rotateAngleY = -200;
         this.mane.setRotationPoint(-1.0F, 14.0F, -3.0F);
         this.mane.rotateAngleX = this.body.rotateAngleX;
         this.tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
         this.back_leg_right.setRotationPoint(-2.5F, 22.0F, 2.0F);
         this.back_leg_right.rotateAngleX = ((float)Math.PI * 3F / 2F);
         this.back_leg_left.setRotationPoint(0.5F, 22.0F, 2.0F);
         this.back_leg_left.rotateAngleX = ((float)Math.PI * 3F / 2F);
         this.front_leg_right.rotateAngleX = 5.811947F;
         this.front_leg_right.setRotationPoint(-2.49F, 17.0F, -4.0F);
         this.front_leg_left.rotateAngleX = 5.811947F;
         this.front_leg_left.setRotationPoint(0.51F, 17.0F, -4.0F);
         this.tail.setRotationPoint(5.0F, 12.0F, 2.0F);
         this.tail.rotateAngleY = -174;
         this.tail.rotateAngleX = -99.6F;
         this.head.setRotationPoint(0.5F, 13.5F, -8.0F);
         this.head.rotateAngleY = 100;
         this.tail.offsetY = 7.5F * 0.0625F;
         this.head.offsetY = 7.5F * 0.0625F;
         this.mane.offsetY = 7.5F * 0.0625F;
         this.body.offsetY = 7.5F * 0.0625F;
         
         this.front_leg_left.isHidden = true;
         this.front_leg_right.isHidden = true;
         this.back_leg_left.isHidden = true;
         this.back_leg_right.isHidden = true;
         
         this.head.rotateAngleZ = 100;
      }
      else */
        if(betterWolf.isSitting())
        {
            this.mane.setRotationPoint(-1.0F, 16.0F, -3.0F);
            this.mane.rotateAngleX = ((float)Math.PI * 2F / 5F);
            this.mane.rotateAngleY = 0.0F;
            this.body.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.body.rotateAngleX = ((float)Math.PI / 4F);
            this.body.rotateAngleY = 0;
            this.tail.setRotationPoint(-1.0F, 21.0F, 6.0F);
            this.tail.rotateAngleY = 0;
            this.back_leg_right.setRotationPoint(-2.5F, 22.0F, 2.0F);
            this.back_leg_right.rotateAngleX = ((float)Math.PI * 3F / 2F);
            this.back_leg_left.setRotationPoint(0.5F, 22.0F, 2.0F);
            this.back_leg_left.rotateAngleX = ((float)Math.PI * 3F / 2F);
            this.front_leg_right.rotateAngleX = 5.811947F;
            this.front_leg_right.setRotationPoint(-2.49F, 17.0F, -4.0F);
            this.front_leg_left.rotateAngleX = 5.811947F;
            this.front_leg_left.setRotationPoint(0.51F, 17.0F, -4.0F);
            
            this.head.setRotationPoint(-1.0F, 13.5F, -7.0F);
            
            this.tail.offsetY = 0;
            this.head.offsetY = 0;
            this.mane.offsetY = 0;
            this.body.offsetY = 0;
            
            this.front_leg_left.isHidden = false;
            this.front_leg_right.isHidden = false;
            this.back_leg_left.isHidden = false;
            this.back_leg_right.isHidden = false;
        }
        else
        {
            this.body.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.body.rotateAngleX = ((float)Math.PI / 2F);
            this.body.rotateAngleY = 0;
            this.mane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.mane.rotateAngleX = this.body.rotateAngleX;
            this.tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
            this.tail.rotateAngleY = 0;
            this.back_leg_right.setRotationPoint(-2.5F, 16.0F, 7.0F);
            this.back_leg_left.setRotationPoint(0.5F, 16.0F, 7.0F);
            this.front_leg_right.setRotationPoint(-2.5F, 16.0F, -4.0F);
            this.front_leg_left.setRotationPoint(0.5F, 16.0F, -4.0F);
            this.back_leg_right.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.back_leg_left.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.front_leg_right.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.front_leg_left.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            
            this.head.setRotationPoint(-1.0F, 13.5F, -7.0F);
            
            this.tail.offsetY = 0;
            this.head.offsetY = 0;
            this.mane.offsetY = 0;
            this.body.offsetY = 0;
            
            this.front_leg_left.isHidden = false;
            this.front_leg_right.isHidden = false;
            this.back_leg_left.isHidden = false;
            this.back_leg_right.isHidden = false;
        }
        
        this.head.rotateAngleZ = betterWolf.getInterestedAngle(partialTickTime) + betterWolf.getShakeAngle(partialTickTime, 0.0F);
        this.mane.rotateAngleZ = betterWolf.getShakeAngle(partialTickTime, -0.08F);
        this.body.rotateAngleZ = betterWolf.getShakeAngle(partialTickTime, -0.16F);
        this.tail.rotateAngleZ = betterWolf.getShakeAngle(partialTickTime, -0.2F);
        
        /*this.nose.rotateAngleZ = this.head.rotateAngleZ;
        this.ear_left.rotateAngleZ = this.head.rotateAngleZ;
        this.ear_right.rotateAngleZ = this.head.rotateAngleZ;*/
    }
    
    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    @Override
    public void setRotationAngles(T betterWolf, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        super.setRotationAngles(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.tail.rotateAngleX = ageInTicks;
      /*if(!betterWolf.isLayingDown())
      {
         this.head.rotateAngleY = netHeadYaw * 0.017453292F;
      }*/
        
        /*this.nose.rotateAngleX = this.head.rotateAngleX;
        this.nose.rotateAngleY = this.head.rotateAngleY;
    
        this.ear_left.rotateAngleX = this.head.rotateAngleX;
        this.ear_left.rotateAngleY = this.head.rotateAngleY;
    
        this.ear_right.rotateAngleX = this.head.rotateAngleX;
        this.ear_right.rotateAngleY = this.head.rotateAngleY;*/
      
      /*if(!betterWolf.isLayingDown())
      {
         this.tail.rotateAngleX = ageInTicks;
      }*/
    }
    
    
}