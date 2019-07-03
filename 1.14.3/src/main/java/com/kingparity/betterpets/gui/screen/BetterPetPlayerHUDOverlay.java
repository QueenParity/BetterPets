package com.kingparity.betterpets.gui.screen;

import com.kingparity.betterpets.BetterPetMod;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class BetterPetPlayerHUDOverlay
{
    public static final ResourceLocation THIRST_BAR_ICONS = new ResourceLocation(Reference.ID + ":textures/gui/thirst_bar.png");
    
    public static void onRenderGameOverLayEvent(RenderGameOverlayEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if(event.getType() == RenderGameOverlayEvent.ElementType.HEALTH)
        {
            IngameGui gui = Minecraft.getInstance().ingameGUI;
            mc.getTextureManager().bindTexture(THIRST_BAR_ICONS);
            
            ClientPlayerEntity player = Minecraft.getInstance().player;
            
            if(!player.isRidingHorse() && BetterPetMod.PROXY.getClientStats() != null)
            {
                int thirstLevel = BetterPetMod.PROXY.getClientStats().thirstLevel;
                int xStart = mc.mainWindow.getScaledWidth()/2 + 10;
                int yStart = mc.mainWindow.getScaledHeight() - 49;
                
                int poisonModifier = BetterPetMod.PROXY.getClientStats().poisoned ? 16 : 0;
                
                for(int i = 0; i < 10; i++)
                {
                    gui.blit(xStart + i*8, yStart, 1, 1, 7, 9); //empty thirst droplet
                    if(thirstLevel % 2 != 0 && 10 - i - 1 == thirstLevel/2)
                    {
                        gui.blit(xStart + i*8, yStart, 17 + poisonModifier, 1, 7, 9);
                    }
                    else if(thirstLevel/2 >= 10 - i)
                    {
                        gui.blit(xStart + i*8, yStart, 9 + poisonModifier, 1, 7, 9);
                    }
                }
            }
            
            mc.getTextureManager().bindTexture(AbstractButton.GUI_ICONS_LOCATION);
        }
        else if(event.getType() == RenderGameOverlayEvent.ElementType.AIR)
        {
            event.setCanceled(true);
            
            IngameGui gui = Minecraft.getInstance().ingameGUI;
            MainWindow mainWindow = Minecraft.getInstance().mainWindow;
            
            int xStart = (mainWindow.getScaledWidth() / 2) + 91;
            int yStart = mainWindow.getScaledHeight() - 49;
            int xModifier = 0;
            int yModifier = 0;
            
            int armorLevel = mc.player.getTotalArmorValue();
            
            if(!Minecraft.getInstance().player.isRidingHorse())
            {
                if(armorLevel > 0)
                {
                    yModifier = -10;
                }
                else
                {
                    xModifier = -101;
                }
            }
            
            if(Minecraft.getInstance().player.areEyesInFluid(FluidTags.WATER, true))
            {
                int air = Minecraft.getInstance().player.getAir();
                int full = MathHelper.ceil(((air - 2) * 10.0D) / 300.0D);
                int partial = MathHelper.ceil((air * 10.0D) / 300.0D) - full;
                
                for(int i = 0; i < (full + partial); ++i)
                {
                    gui.blit((xStart - (i * 8) - 9) + xModifier, yStart + yModifier, (i < full ? 16 : 25), 18, 9, 9);
                }
            }
        }
    }
}