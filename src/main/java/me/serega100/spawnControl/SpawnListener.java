package me.serega100.spawnControl;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Set;

public class SpawnListener implements Listener
{
    private SpawnControl plugin;

    SpawnListener(SpawnControl plugin)
    {
        this.plugin = plugin;
    }

    /** Mob spawn event handler */
    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        Location loc = event.getLocation();
        RegionManager rm = WorldGuardPlugin.inst().getRegionContainer().get(loc.getWorld());

        if (rm == null) {
            return;
        }

        ApplicableRegionSet set = rm.getApplicableRegions(loc);
        String group = set.queryValue(null, SpawnControl.GROUP_FLAG);

        if (group != null) {
            try {
                Set<CreatureSpawnEvent.SpawnReason> reasons = SpawnControl.getReasonsFromGroup(group);
                if (!reasons.contains(event.getSpawnReason())) {
                    event.setCancelled(true);
                }
            } catch (SpawnControl.GroupIsNotDefined e) {
                e.serveConsole(loc);
            }
        }

        if (plugin.isLoggingMode()) {
            plugin.printInConsole("Spawn Event | Type: %s  Reason: %s  Spawned: %s", event.getEntityType(),
                    event.getSpawnReason(), !event.isCancelled());
        }

    }
}
