package me.centauri07.villagecontrol.plugin;

import me.centauri07.villagecontrol.constants.Constants;
import me.centauri07.villagecontrol.trade.TradeItemReplacement;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author Centauri07
 */
public class VillageControlSettings {

    private final Plugin plugin;
    private File file;
    private YamlConfiguration yamlConfiguration;

    public List<TradeItemReplacement> tradeItemReplacements;
    public List<Material> restrictedItems;
    public String settingVersion;

    public VillageControlSettings(Plugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        // Set up the file and load the YAML configuration
        file = new File(plugin.getDataFolder(), Constants.SETTINGS_FILE_NAME + ".yml");

        // If the file doesn't exist, create it by copying the default resource
        if (!file.exists()) {
            plugin.saveResource(Constants.SETTINGS_FILE_NAME + ".yml", false);
        }

        yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.options().parseComments(true);

        // Load the YAML configuration from the file
        try {
            yamlConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException("Something went wrong while trying to load the " + Constants.SETTINGS_FILE_NAME + ".yml", e);
        }

        // Load trade item replacements from the configuration
        tradeItemReplacements = Objects.requireNonNull(yamlConfiguration.getConfigurationSection("trade-item-replacements"))
                .getKeys(false)
                .stream()
                .map(key -> {
                    // Retrieve target item, input replacement, and output replacement for each index
                    String targetItem = yamlConfiguration.getString("trade-item-replacements." + key + ".target-item");
                    String inputReplacement = yamlConfiguration.getString("trade-item-replacements." + key + ".input-replacement");
                    String outputReplacement = yamlConfiguration.getString("trade-item-replacements." + key + ".output-replacement");

                    // Return a new TradeItemReplacement object
                    assert targetItem != null;
                    return TradeItemReplacement.of(targetItem, inputReplacement, outputReplacement);
                })
                .toList();

        restrictedItems = yamlConfiguration.getStringList("restricted-outputs").stream().map(
                material -> {
                    Material materialFromString = Material.getMaterial(material);

                    if (materialFromString == null)
                        throw new IllegalArgumentException("A material (" + material + ") from restricted-outputs is unknown.");

                    return materialFromString;
                }
        ).toList();

        // Load the setting version
        settingVersion = yamlConfiguration.getString("settings-version");
    }
}
