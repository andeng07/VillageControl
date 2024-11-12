package me.centauri07.villagecontrol.plugin;

import me.centauri07.villagecontrol.trade.VillagerModifier;
import me.centauri07.villagecontrol.listeners.VillagerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Centauri07
 */
public class VillageControlPlugin extends JavaPlugin {

    private VillageControlSettings villageControlSettings;
    private VillagerModifier villagerModifier;

    @Override
    public void onEnable() {

        villageControlSettings = new VillageControlSettings(this);

        villageControlSettings.load();

        villagerModifier = new VillagerModifier(this, villageControlSettings);

        Bukkit.getPluginManager().registerEvents(new VillagerListener(villagerModifier), this);

    }
}
