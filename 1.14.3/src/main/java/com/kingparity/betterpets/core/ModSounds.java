package com.kingparity.betterpets.core;

import com.kingparity.betterpets.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSounds
{
    @ObjectHolder(Reference.ID + ":tube_pick_up")
    public static final SoundEvent TUBE_PICK_UP = null;
    
    @ObjectHolder(Reference.ID + ":tube_put_down")
    public static final SoundEvent TUBE_PUT_DOWN = null;
    
    private static final List<SoundEvent> SOUND_EVENTS = new LinkedList<>();
    
    @SubscribeEvent
    public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event)
    {
        register("tube_pick_up");
        register("tube_put_down");
        
        SOUND_EVENTS.forEach(soundEvent -> event.getRegistry().register(soundEvent));
    }
    
    private static SoundEvent register(String name)
    {
        ResourceLocation resource = new ResourceLocation(Reference.ID, name);
        SoundEvent soundEvent = new SoundEvent(resource).setRegistryName(resource);
        SOUND_EVENTS.add(soundEvent);
        return soundEvent;
    }
}
