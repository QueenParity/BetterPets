package com.kingparity.betterpets.client.renderer.entity;

import com.kingparity.betterpets.client.layer.BetterWolfAccessoriesLayer;
import com.kingparity.betterpets.client.layer.BetterWolfCollarLayer;
import com.kingparity.betterpets.client.layer.BetterWolfEyesLayer;
import com.kingparity.betterpets.client.model.BetterWolfModel;
import com.kingparity.betterpets.entity.BetterWolfEntity;
import com.kingparity.betterpets.util.Reference;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BetterWolfRenderer extends MobRenderer<BetterWolfEntity, BetterWolfModel<BetterWolfEntity>>
{
    /*private static final ResourceLocation WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf.png");
    private static final ResourceLocation TAMED_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
    private static final ResourceLocation ANRGY_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_angry.png");*/
    
    public BetterWolfRenderer(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn, new BetterWolfModel<>(), 0.5F);
        this.addLayer(new BetterWolfCollarLayer(this));
        this.addLayer(new BetterWolfEyesLayer(this));
        this.addLayer(new BetterWolfAccessoriesLayer<>(this));
    }
    
    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    protected float handleRotationFloat(BetterWolfEntity betterWolf, float partialTicks)
    {
        return betterWolf.getTailRotation();
    }
    
    /**
     * Renders the desired {@code T} type Entity.
     */
    @Override
    public void doRender(BetterWolfEntity betterWolf, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if(betterWolf.isWolfWet())
        {
            float f = betterWolf.getBrightness() * betterWolf.getShadingWhileWet(partialTicks);
            GlStateManager.color3f(f, f, f);
        }
        
        super.doRender(betterWolf, x, y, z, entityYaw, partialTicks);
    }
    
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(BetterWolfEntity betterWolf)
    {
        /*if(betterWolf.isTamed())
        {
            if(betterWolf.isSleeping())
            {
                return new ResourceLocation(Reference.ID + ":textures/entity/better_wolf_tamed_" + betterWolf.getTexture() + "_sleeping.png");
            }
            else
            {
                return new ResourceLocation(Reference.ID + ":textures/entity/better_wolf_tamed_" + betterWolf.getTexture() + ".png");
            }
        }
        else
        {
            return new ResourceLocation(Reference.ID + ":textures/entity/better_wolf_" + betterWolf.getTexture() + ".png");
        }*/
        return new ResourceLocation(Reference.ID + ":textures/entity/wolf/better_wolf_" + betterWolf.getTexture() + ".png");
    }
}