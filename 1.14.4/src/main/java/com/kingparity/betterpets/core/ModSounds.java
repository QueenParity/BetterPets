package com.kingparity.betterpets.core;

import com.kingparity.betterpets.names.SoundNames;
import com.kingparity.betterpets.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSounds
{
    @ObjectHolder(SoundNames.BETTER_WOLF_BARK)
    public static final SoundEvent BETTER_WOLF_BARK = null;
    
    private static final List<SoundEvent> SOUND_EVENTS = new ArrayList<>();
    
    @SubscribeEvent
    public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event)
    {
        register(SoundNames.BETTER_WOLF_BARK);
        
        SOUND_EVENTS.forEach(soundEvent -> event.getRegistry().register(soundEvent));
    }
    
    private static void register(String name)
    {
        SoundEvent event = new SoundEvent(new ResourceLocation(name));
        event.setRegistryName(name);
        SOUND_EVENTS.add(event);
    }
}
