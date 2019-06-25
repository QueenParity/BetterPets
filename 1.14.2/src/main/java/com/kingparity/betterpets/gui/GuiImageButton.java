package com.kingparity.betterpets.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;

public class GuiImageButton extends Button
{
    private ResourceLocation resource;
    private int textureU, textureV;
    private int textureWidth, textureHeight;
    
    public GuiImageButton(int x, int y, int textureU, int textureV, int textureWidth, int textureHeight, ResourceLocation resource, IPressable pressable)
    {
        this(x, y, textureU, textureV, textureWidth, textureHeight, resource, "", pressable);
    }
    
    public GuiImageButton(int x, int y, int textureU, int textureV, int textureWidth, int textureHeight, ResourceLocation resource, String msg, IPressable pressable)
    {
        super(x, y, textureWidth + 4, textureHeight + 4, msg, pressable);
        this.resource = resource;
        this.textureU = textureU;
        this.textureV = textureV;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.visible = true;
    }
    
    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks)
    {
        /*if(! this.visible)
        {
            return;
        }
        this.zLevel = 100.0F;
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        mc.getTextureManager().bindTexture(resource);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(x + 3, y + 3, textureU, textureV, textureWidth, textureHeight);
        this.zLevel = 0.0F;*/
        Minecraft mc = Minecraft.getInstance();
        if(this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(resource);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = 0;//this.getHoverState(this.hovered);
            if(this.isHovered)
            {
                i = 1;
            }
            this.blit(this.x, this.y, textureU, i * 18, textureWidth, textureHeight);
            //this.drawTexturedModalRect(this.x, this.y, textureU, i * 18, textureWidth, textureHeight);
            this.renderBg(mc, mouseX, mouseY);
            /*int j = 14737632;
    
            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }
    
            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);*/
        }
    }
}