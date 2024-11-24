package de.nikey.randomEvents.Soccer;

import org.bukkit.World;

public interface SoccerMap {
    boolean load();
    void unload();
    boolean restoreFromSource();

    boolean isLoaded();
    World getWorld();
}
