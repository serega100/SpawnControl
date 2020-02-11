package me.serega100.spawnControl;

import co.aikar.commands.BukkitCommandManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpawnControl extends JavaPlugin {
    private HashMap<String, Set<CreatureSpawnEvent.SpawnReason>> reasonGroups = new HashMap<>();
    private static SpawnControl instance;
    private boolean loggingMode;

    public static GroupFlag GROUP_FLAG;
    @Override
    public void onLoad() {
        instance = this;
        // Load config and reason groups from it.
        saveDefaultConfig();
        loadGroups();
        // Init WG flags
        FlagRegistry registry = WorldGuardPlugin.inst().getFlagRegistry();
        GROUP_FLAG = new GroupFlag();
        registry.register(GROUP_FLAG);
    }

    @Override
    public void onEnable() {
        new BukkitCommandManager(this).registerCommand(new SpawnControlCommands(this));
        getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void reload() {
        loadGroups();
    }

    private void loadGroups() {
        HashMap<String, Set<CreatureSpawnEvent.SpawnReason>> groups = new HashMap<>();

        ConfigurationSection section = getConfig().getConfigurationSection("allowed-spawn-reason-groups");

        for (String groupName : section.getKeys(false)) {
            List<String> list = section.getStringList(groupName);
            Set<CreatureSpawnEvent.SpawnReason> reasons = new HashSet<>();
            for (String str : list) {
                try {
                    CreatureSpawnEvent.SpawnReason reason = CreatureSpawnEvent.SpawnReason.valueOf(str);
                    reasons.add(reason);
                } catch (IllegalArgumentException e) {
                    printInConsole("Reason %s in group %s is not stated. Edit the config and start the server again.",
                            str, groupName);
                    getServer().getPluginManager().disablePlugin(this);
                }
            }
            groups.put(groupName.toLowerCase(), reasons);
        }

        reasonGroups = groups;
    }

    /** Gets spawn reasons for the group */
    public static Set<CreatureSpawnEvent.SpawnReason> getReasonsFromGroup(String name) throws GroupIsNotDefined {
        Set<CreatureSpawnEvent.SpawnReason> set = instance.reasonGroups.get(name);
        if (set == null) {
            throw new GroupIsNotDefined(name);
        } else {
            return set;
        }
    }

    /** Returns true if the plugin contains the group */
    public static Boolean haveGroup(String name) {
        return instance.reasonGroups.containsKey(name);
    }

    public boolean isLoggingMode() {
        return loggingMode;
    }

    public void setLoggingMode(boolean mode) {
        this.loggingMode = mode;
    }

    public static class GroupIsNotDefined extends Exception {
        private String groupName;

        GroupIsNotDefined(String groupName) {
            this.groupName = groupName;

        }

        public String getGroupName() {
            return groupName;
        }

        public void serveConsole(Location loc) {
            instance.printInConsole("Unable to filter mob spawning! Group %s is not defined. Check " +
                    "'spawn-control-group' flag at %s %s %s %s", loc.getWorld().getName(), loc.getBlockX(),
                    loc.getBlockY(), loc.getBlockZ());
        }
    }

    void printInConsole(String str, Object ... objects) {
        str = "[SpawnControl] " + ChatColor.GREEN + str;
        String[] strings = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            strings[i] = ChatColor.AQUA + objects[i].toString() + ChatColor.GREEN;
        }
        Bukkit.getConsoleSender().sendMessage(String.format(str, (Object[]) strings));
    }
}
