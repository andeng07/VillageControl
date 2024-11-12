package me.centauri07.villagecontrol.trade;

import org.bukkit.Material;

import javax.annotation.Nullable;

/**
 * @author Centauri07
 */
public record TradeItemReplacement(Material target, @Nullable Material inputReplacement,
                                   @Nullable Material outputReplacement) {

    public static TradeItemReplacement of(String target, @Nullable String inputReplacement, @Nullable String outputReplacement) {
        Material targetFromString = Material.getMaterial(target);
        Material inputReplacementFromString = inputReplacement == null ? null : Material.getMaterial(inputReplacement);
        Material outputReplacementFromString = outputReplacement == null ? null : Material.getMaterial(outputReplacement);

        if (targetFromString == null)
            throw new IllegalArgumentException("The trade item target (" + target + ") does not exist.");

        if (inputReplacement != null && inputReplacementFromString == null)
            throw new IllegalArgumentException("The input replacement material (" + inputReplacement + ") for trade item (" + targetFromString.name() + ") does not exist.");

        if (outputReplacement != null && outputReplacementFromString == null)
            throw new IllegalArgumentException("The output replacement material (" + outputReplacement + ") for trade item (" + targetFromString.name() + ") does not exist.");

        return new TradeItemReplacement(targetFromString, inputReplacementFromString, outputReplacementFromString);
    }

}
