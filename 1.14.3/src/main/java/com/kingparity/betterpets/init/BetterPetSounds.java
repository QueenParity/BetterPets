package com.kingparity.betterpets.init;

import com.kingparity.betterpets.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterPetSounds
{
    public static SoundEvent TUBE_PICK_UP, TUBE_PUT_DOWN;
    
    private static final List<SoundEvent> SOUND_EVENTS = new LinkedList<>();
    
    public static void add(SoundEvent soundEvent)
    {
        SOUND_EVENTS.add(soundEvent);
    }
    
    public static List<SoundEvent> getSoundEvents()
    {
        return Collections.unmodifiableList(SOUND_EVENTS);
    }
    
    static
    {
        TUBE_PICK_UP = register("tube_pick_up");
        TUBE_PUT_DOWN = register("tube_put_down");
    }
    
    @SubscribeEvent
    public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event)
    {
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
