package me.serega100.spawnControl;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Set;
import java.util.StringJoiner;

public class SpawnListener implements Listener
{
    private final SpawnControl plugin;

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

        ApplicableRegionSet applicableRegions = rm.getApplicableRegions(loc);
        String group = applicableRegions.queryValue(null, SpawnControl.GROUP_FLAG);
        Set<ProtectedRegion> regions = applicableRegions.getRegions();

        if (group != null) {
            try {
                Set<CreatureSpawnEvent.SpawnReason> reasons = plugin.getReasonsFromGroup(group);
                if (!reasons.contains(event.getSpawnReason())) {
                    event.setCancelled(true);
                }
            } catch (SpawnControl.GroupIsNotDefined e) {
                StringJoiner joiner = new StringJoiner(", ");
                for (ProtectedRegion rg : regions) {
                    joiner.add(ChatColor.GOLD + rg.getId() + ChatColor.WHITE);
                }
                String postFix = " region";
                if (regions.size() > 1) {
                    postFix += "s";
                }
                String strRegions = joiner.toString() + postFix;

                plugin.getLogger().severe(SpawnControl.CHAT_PREFIX + "Unable to filter mob spawning! Group " + ChatColor.GOLD +
                        e.getGroupName() + ChatColor.WHITE + " is not defined. Check 'spawn-control-group' flag at " +
                        strRegions + " or config of the plugin.");
            }
        }

        for (CommandSender sender : plugin.getLoggingCmdSenders()) {
            StringJoiner joiner = new StringJoiner(", ");
            if (regions.size() == 0) {
                joiner.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "<none>" + ChatColor.WHITE);
            } else for (ProtectedRegion rg : regions) {
                joiner.add(ChatColor.GOLD + rg.getId() + ChatColor.WHITE);
            }
            String preFix = "Region";
            if (regions.size() > 1) {
                preFix += "s";
            }
            String strRegions = preFix + ": " + joiner.toString();

            sender.sendMessage(String.format(SpawnControl.CHAT_PREFIX + "%s Type: %s  Reason: %s  Spawned: %s",
                    strRegions,
                    ChatColor.GOLD + event.getEntityType().toString() + ChatColor.WHITE,
                    ChatColor.GOLD + event.getSpawnReason().toString() + ChatColor.WHITE,
                    ChatColor.GOLD + String.valueOf(!event.isCancelled()) + ChatColor.WHITE));
        }

    }
}
