package com.kingparity.betterpets.client.render.entity;

import com.kingparity.betterpets.client.render.entity.layers.BetterWolfCollarLayer;
import com.kingparity.betterpets.client.render.entity.model.BetterWolfModel;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BetterWolfRenderer extends MobRenderer<BetterWolfEntity, BetterWolfModel<BetterWolfEntity>>
{
    private static final ResourceLocation WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf.png");
    private static final ResourceLocation TAMED_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
    private static final ResourceLocation ANGRY_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_angry.png");
    
    public BetterWolfRenderer(EntityRendererManager renderManager)
    {
        super(renderManager, new BetterWolfModel<>(), 0.5F);
        this.addLayer(new BetterWolfCollarLayer(this));
    }
    
    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    protected float handleRotationFloat(BetterWolfEntity betterWolf, float partialTicks)
    {
        return betterWolf.getTailRotation();
    }
    
    @Override
    public void render(BetterWolfEntity betterWolf, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        if(betterWolf.isWolfWet())
        {
            float f = betterWolf.getBrightness() * betterWolf.getShadingWhileWet(partialTicks);
            this.entityModel.setTint(f, f, f);
        }
        
        super.render(betterWolf, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if(betterWolf.isWolfWet())
        {
            this.entityModel.setTint(1.0F, 1.0F, 1.0F);
        }
        
    }
    
    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getEntityTexture(BetterWolfEntity entity)
    {
        if(entity.isTamed())
        {
            return TAMED_WOLF_TEXTURES;
        }
        else
        {
            return entity.isAngry() ? ANGRY_WOLF_TEXTURES : WOLF_TEXTURES;
        }
    }
}