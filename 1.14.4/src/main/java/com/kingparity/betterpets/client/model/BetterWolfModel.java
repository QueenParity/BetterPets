package com.kingparity.betterpets.client.model;

import com.kingparity.betterpets.entity.BetterWolfEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;

public class BetterWolfModel<T extends BetterWolfEntity> extends EntityModel<T>
{
    protected RendererModel head;
    protected RendererModel body;
    protected RendererModel legBackRight;
    protected RendererModel legBackLeft;
    protected RendererModel legFrontRight;
    protected RendererModel legFrontLeft;
    protected RendererModel tail;
    protected RendererModel mane;
    
    public BetterWolfModel()
    {
        textureWidth = 256;
        textureHeight = 128;
        
        legFrontLeft = new RendererModel(this);
        legFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
        legFrontLeft.cubeList.add(new ModelBox(legFrontLeft, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));
        
        legFrontRight = new RendererModel(this);
        legFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
        legFrontRight.cubeList.add(new ModelBox(legFrontRight, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));
        
        legBackLeft = new RendererModel(this);
        legBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
        legBackLeft.cubeList.add(new ModelBox(legBackLeft, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));
        
        legBackRight = new RendererModel(this);
        legBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
        legBackRight.cubeList.add(new ModelBox(legBackRight, 0, 18, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));
        
        mane = new RendererModel(this);
        mane.setRotationPoint(-1.0F, 14.0F, -3.0F);
        mane.cubeList.add(new ModelBox(mane, 21, 0, -3.0F, -3.0F, -3.0F, 8, 6, 7, 0.0F, false));
        
        body = new RendererModel(this);
        body.setRotationPoint(0.0F, 14.0F, 2.0F);
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
    }
    
    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(T betterWolf, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(betterWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.head.renderWithRotation(scale);
        this.body.render(scale);
        this.legBackRight.render(scale);
        this.legBackLeft.render(scale);
        this.legFrontRight.render(scale);
        this.legFrontLeft.render(scale);
        this.tail.renderWithRotation(scale);
        this.mane.render(scale);
    }
    
    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the func_212844_a_ method.
     */
    @Override
    public void setLivingAnimations(T betterWolf, float limbSwing, float limbSwingAmount, float partialTick)
    {
        if(betterWolf.isAngry())
        {
            this.tail.rotateAngleY = 0.0F;
        }
        else
        {
            this.tail.rotateAngleY = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }
        
        if(betterWolf.isLayingDown())
        {
            this.body.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.body.rotateAngleX = ((float)Math.PI / 2F);
            this.body.rotateAngleY = 0;
            this.mane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.mane.rotateAngleX = this.body.rotateAngleX;
            this.tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
            this.tail.rotateAngleY = 0;
            this.legBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
            this.legBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
            this.legFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
            this.legFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
            this.legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    
            this.head.setRotationPoint(-1.0F, 13.5F, -7.0F);
    
            this.tail.offsetY = 0;
            this.head.offsetY = 0;
            this.mane.offsetY = 0;
            this.body.offsetY = 0;
        }
        else if(betterWolf.isSitting())
        {
            this.mane.setRotationPoint(-1.0F, 16.0F, -3.0F);
            this.mane.rotateAngleX = 1.2566371F;
            this.mane.rotateAngleY = 0.0F;
            this.body.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.body.rotateAngleX = ((float)Math.PI / 4F);
            this.tail.setRotationPoint(-1.0F, 21.0F, 6.0F);
            this.legBackRight.setRotationPoint(-2.5F, 22.0F, 2.0F);
            this.legBackRight.rotateAngleX = ((float)Math.PI * 1.5F);
            this.legBackLeft.setRotationPoint(0.5F, 22.0F, 2.0F);
            this.legBackLeft.rotateAngleX = ((float)Math.PI * 1.5F);
            this.legFrontRight.rotateAngleX = 5.811947F;
            this.legFrontRight.setRotationPoint(-2.49F, 17.0F, -4.0F);
            this.legFrontLeft.rotateAngleX = 5.811947F;
            this.legFrontLeft.setRotationPoint(0.51F, 17.0F, -4.0F);
        }
        else
        {
            this.body.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.body.rotateAngleX = ((float)Math.PI / 2F);
            this.mane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.mane.rotateAngleX = this.body.rotateAngleX;
            this.tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
            this.legBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
            this.legBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
            this.legFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
            this.legFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
            this.legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }
        
        if(!betterWolf.isLayingDown())
        {
            this.head.rotateAngleZ = betterWolf.getInterestedAngle(partialTick) + betterWolf.getShakeAngle(partialTick, 0.0F);
            this.mane.rotateAngleZ = betterWolf.getShakeAngle(partialTick, -0.08F);
            this.body.rotateAngleZ = betterWolf.getShakeAngle(partialTick, -0.16F);
            this.tail.rotateAngleZ = betterWolf.getShakeAngle(partialTick, -0.2F);
        }
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
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.tail.rotateAngleX = ageInTicks;
    }
}