package com.kingparity.betterpets.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBinds
{
    public static final KeyBinding KEY_J = new KeyBinding("key.j", 74, "key.categories.betterpets");
    public static final KeyBinding KEY_K = new KeyBinding("key.k", 74, "key.categories.betterpets");
    
    public static void register()
    {
        ClientRegistry.registerKeyBinding(KEY_J);
        ClientRegistry.registerKeyBinding(KEY_K);
    }
}
