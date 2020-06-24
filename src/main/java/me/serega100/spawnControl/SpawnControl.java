package me.serega100.spawnControl;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SpawnControl extends JavaPlugin {
    private final HashMap<String, SpawnGroup> spawnGroups = new HashMap<>();
    private final Set<CommandSender> loggingCmdSenders = new HashSet<>();

    private static SpawnControl instance;

    public final static GroupFlag GROUP_FLAG = new GroupFlag();
    final static String CHAT_PREFIX = ChatColor.AQUA + "[SpawnControl] " + ChatColor.WHITE;

    @Override
    public void onLoad() {
        instance = this;
        // Load config and reason groups from it.
        saveDefaultConfig();
        loadGroups();
        // Init WG flags
        FlagRegistry registry = WorldGuardPlugin.inst().getFlagRegistry();
        registry.register(GROUP_FLAG);
    }

    @Override
    public void onEnable() {
        this.getCommand("spawncontrol").setExecutor(new SpawnControlCommands(this));
        this.getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void reload() {
        spawnGroups.clear();
        loadGroups();
    }

    /** Get spawn reasons for the group */
    public SpawnGroup getSpawnGroup(String name) throws GroupIsNotDefined {
        SpawnGroup group = spawnGroups.get(name);
        if (group == null) {
            throw new GroupIsNotDefined(name);
        } else {
            return group;
        }
    }

    /** Returns true if the plugin contains the group */
    public boolean hasGroup(String name) {
        return spawnGroups.containsKey(name);
    }

    Set<CommandSender> getLoggingCmdSenders() {
        return loggingCmdSenders;
    }

    public static SpawnControl getInstance() {
        return instance;
    }

    public static class GroupIsNotDefined extends Exception {
        private final String groupName;

        private GroupIsNotDefined(String groupName) {
            this.groupName = groupName;

        }

        public String getGroupName() {
            return groupName;
        }
    }

    private void loadGroups() {
        ConfigurationSection section = getConfig().getConfigurationSection("spawn-groups");

        for (String groupName : section.getKeys(false)) {
            String strMode = section.getString(groupName + ".mode", "ALLOW").toUpperCase();
            SpawnGroup.Mode mode = SpawnGroup.Mode.valueOf(strMode);
            Set<CreatureSpawnEvent.SpawnReason> reasons = new HashSet<>();
            for (String rName : section.getStringList(groupName + ".reasons")) {
                try {
                    CreatureSpawnEvent.SpawnReason reason = CreatureSpawnEvent.SpawnReason.valueOf(rName);
                    reasons.add(reason);
                } catch (IllegalArgumentException e) {
                    getLogger().severe(CHAT_PREFIX + String.format(
                            "Reason %s in group %s does not exist. Edit the config and start the server again.",
                            ChatColor.GOLD + rName + ChatColor.WHITE,
                            ChatColor.GOLD + groupName + ChatColor.WHITE));
                    getServer().getPluginManager().disablePlugin(this);
                }
            }
            spawnGroups.put(groupName, new SpawnGroup(groupName, mode, reasons));
        }
    }
}
