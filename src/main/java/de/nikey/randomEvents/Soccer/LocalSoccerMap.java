package de.nikey.randomEvents.Soccer;

import de.nikey.randomEvents.General.FileUtil;
import de.nikey.randomEvents.RandomEvents;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class LocalSoccerMap implements SoccerMap{
    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World bukkitWorld;

    public LocalSoccerMap(File worldFolder, String worldName, boolean loadOnInit) {
        this.sourceWorldFolder = new File(worldFolder, worldName);

            if (loadOnInit) load();
    }


    public boolean load() {
        if (isLoaded())return true;

        this.activeWorldFolder = new File(Bukkit.getWorldContainer().getParentFile(),sourceWorldFolder.getName()+"_active_"+System.currentTimeMillis());

        try {
            FileUtil.copy(sourceWorldFolder,activeWorldFolder);
        }catch (IOException ex) {
            RandomEvents.getPlugin().getLogger().severe("Failed to load SoccerMap from source folder " + sourceWorldFolder.getName());
            ex.printStackTrace();
            return false;
        }

        this.bukkitWorld = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));

        if (this.bukkitWorld != null ) this.bukkitWorld.setAutoSave(false);
        return isLoaded();
    }


    public void unload() {
        if (bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld,false);
        if (activeWorldFolder != null) FileUtil.delete(activeWorldFolder);

        bukkitWorld = null;
        activeWorldFolder = null;
    }


    public boolean restoreFromSource() {
        unload();
        return load();
    }


    public boolean isLoaded() {
        return bukkitWorld != null;
    }

    public World getWorld() {
        return bukkitWorld;
    }
}
