package com.kingparity.betterpets.client.render.entity;

import com.kingparity.betterpets.client.render.entity.layers.BetterWolfCollarLayer;
import com.kingparity.betterpets.client.render.entity.model.BetterWolfModel;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.util.Reference;
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
    private static final ResourceLocation WOLF_TEXTURES = new ResourceLocation(Reference.ID + ":textures/entity/wolf/wolf.png");
    private static final ResourceLocation TAMED_WOLF_TEXTURES = new ResourceLocation(Reference.ID + ":textures/entity/wolf/wolf_tame.png");
    private static final ResourceLocation ANGRY_WOLF_TEXTURES = new ResourceLocation(Reference.ID + ":textures/entity/wolf/wolf_angry.png");
    
    public BetterWolfRenderer(EntityRendererManager renderManager)
    {
        super(renderManager, new BetterWolfModel<>(), 0.5F);
        this.addLayer(new BetterWolfCollarLayer(this));
    }
    
    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(BetterWolfEntity betterWolf, float partialTicks)
    {
        return betterWolf.getTailRotation();
    }
    
    public void render(BetterWolfEntity betterWolf, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        if(betterWolf.isWolfWet())
        {
            float f = betterWolf.getShadingWhileWet(partialTicks);
            this.entityModel.setTint(f, f, f);
        }
        
        super.render(betterWolf, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        if(betterWolf.isWolfWet())
        {
            this.entityModel.setTint(1.0F, 1.0F, 1.0F);
        }
        
    }
    
    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(BetterWolfEntity betterWolf)
    {
        if(betterWolf.isTamed())
        {
            return TAMED_WOLF_TEXTURES;
        }
        else
        {
            return betterWolf.func_233678_J__() ? ANGRY_WOLF_TEXTURES : WOLF_TEXTURES;
        }
    }
}
