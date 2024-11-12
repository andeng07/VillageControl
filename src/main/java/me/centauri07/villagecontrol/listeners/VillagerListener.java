package me.centauri07.villagecontrol.listeners;

import me.centauri07.villagecontrol.trade.VillagerModifier;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.MerchantRecipe;

/**
 * @author Centauri07
 */
public class VillagerListener implements Listener {

    private final VillagerModifier villagerModifier;

    public VillagerListener(VillagerModifier villagerModifier) {
        this.villagerModifier = villagerModifier;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager villager)) return;

        if (villagerModifier.isUpdated(villager))
            return;

        villagerModifier.modifyTrades(villager);

        villagerModifier.update(villager);
    }

    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        MerchantRecipe recipe = event.getRecipe();

        if (villagerModifier.isRestricted(recipe)) {
            event.setCancelled(true);
            return;
        }

        event.setRecipe(villagerModifier.modifyRecipe(event.getRecipe()));
    }

}
