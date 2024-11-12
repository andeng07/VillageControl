package me.centauri07.villagecontrol.trade;

import me.centauri07.villagecontrol.constants.Constants;
import me.centauri07.villagecontrol.plugin.VillageControlSettings;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Centauri07
 */
public class VillagerModifier {

    private final Plugin plugin;
    private final VillageControlSettings settings;

    public VillagerModifier(Plugin plugin, VillageControlSettings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    public void modifyTrades(Villager villager) {
        List<MerchantRecipe> recipeList = new ArrayList<>(villager.getRecipes());

        recipeList.removeIf(this::isRestricted);

        List<MerchantRecipe> newRecipeList = recipeList.stream().map(this::modifyRecipe).toList();

        villager.setRecipes(newRecipeList);
    }

    public boolean isRestricted(MerchantRecipe recipe) {
        boolean isTypeRestricted = settings.restrictedItems.contains(recipe.getResult().getType());

        boolean isEnchantedRestricted =
                recipe.getResult().getType() == Material.ENCHANTED_BOOK &&
                        recipe.getResult().getItemMeta() != null &&
                        ((EnchantmentStorageMeta) recipe.getResult().getItemMeta()).hasStoredEnchant(Enchantment.MENDING);

        return isTypeRestricted || isEnchantedRestricted;
    }

    public MerchantRecipe modifyRecipe(MerchantRecipe recipe) {
        ItemStack result = recipe.getResult().withType(
                settings.tradeItemReplacements
                        .stream()
                        .filter(tradeItemReplacement -> tradeItemReplacement.target() == recipe.getResult().getType())
                        .map(tradeItemReplacement -> tradeItemReplacement.outputReplacement() == null ? recipe.getResult().getType() : tradeItemReplacement.outputReplacement())
                        .findFirst()
                        .orElse(recipe.getResult().getType()));

        List<ItemStack> ingredients = recipe.getIngredients().stream().map(
                        ingredient -> ingredient.withType(settings.tradeItemReplacements
                                .stream()
                                .filter(tradeItemReplacement -> tradeItemReplacement.target() == ingredient.getType())
                                .map(tradeItemReplacement -> tradeItemReplacement.inputReplacement() == null ? ingredient.getType() : tradeItemReplacement.inputReplacement())
                                .findFirst()
                                .orElse(ingredient.getType())))
                .toList();

        MerchantRecipe merchantRecipe = new MerchantRecipe(
                result,
                recipe.getUses(),
                recipe.getMaxUses(),
                recipe.hasExperienceReward(),
                recipe.getVillagerExperience(),
                recipe.getPriceMultiplier(),
                recipe.getDemand(),
                recipe.getSpecialPrice(),
                recipe.shouldIgnoreDiscounts()
        );

        merchantRecipe.setIngredients(ingredients);

        return merchantRecipe;
    }

    public boolean isUpdated(Villager villager) {
        if (!villager.hasMetadata(Constants.VILLAGE_CONTROL_METADATA)) return false;

        MetadataValue metadataValue = villager.getMetadata(Constants.VILLAGE_CONTROL_METADATA).getFirst();

        return metadataValue.asString().equals(settings.settingVersion);
    }

    public void update(Villager villager) {
        villager.removeMetadata(Constants.VILLAGE_CONTROL_METADATA, plugin);

        villager.setMetadata(Constants.VILLAGE_CONTROL_METADATA, new FixedMetadataValue(plugin, settings.settingVersion));
    }

}
