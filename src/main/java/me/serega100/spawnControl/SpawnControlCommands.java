package me.serega100.spawnControl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpawnControlCommands implements CommandExecutor, TabCompleter {
    private final SpawnControl plugin;

    SpawnControlCommands(SpawnControl plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("spawncontrol.reload")) {
                    ConsoleCommandSender console = Bukkit.getConsoleSender();
                    sender.sendMessage(SpawnControl.CHAT_PREFIX + "Reload configuration...");
                    if (!sender.equals(console)) console.sendMessage(SpawnControl.CHAT_PREFIX + "Reload configuration...");
                    plugin.reload();
                    if (!sender.equals(console)) console.sendMessage(SpawnControl.CHAT_PREFIX + "Configuration reloaded!");
                    sender.sendMessage(SpawnControl.CHAT_PREFIX + "Configuration reloaded!");
                } else {
                    sender.sendMessage(SpawnControl.CHAT_PREFIX + ChatColor.RED + "You don't have permission to execute the command!");
                }
            } else {
                showHelp(sender);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("logging")) {
            if (sender.hasPermission("spawncontrol.logging")) {
                if (args[1].equalsIgnoreCase("enable")) {
                    plugin.getLoggingCmdSenders().add(sender);
                    sender.sendMessage(SpawnControl.CHAT_PREFIX + "Entity spawn logging has " + ChatColor.GOLD + "enabled" + ChatColor.WHITE + " for you.");
                } else if (args[1].equalsIgnoreCase("disable")) {
                    plugin.getLoggingCmdSenders().remove(sender);
                    sender.sendMessage(SpawnControl.CHAT_PREFIX + "Entity spawn logging has " + ChatColor.GOLD + "disabled" + ChatColor.WHITE + " for you.");
                } else {
                    sender.sendMessage(SpawnControl.CHAT_PREFIX + ChatColor.RED + "You can enable or disable the logging only!");
                }
            } else {
                sender.sendMessage(SpawnControl.CHAT_PREFIX + ChatColor.RED + "You don't have permission to execute the command!");
            }
        } else {
            showHelp(sender);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tabCompletions = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("spawncontrol.reload")) tabCompletions.add("reload");
            if (sender.hasPermission("spawncontrol.logging")) tabCompletions.add("logging");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("logging") && sender.hasPermission("spawncontrol.logging")) {
            tabCompletions.addAll(Arrays.asList("enable", "disable"));
        }
        return tabCompletions;
    }

    private void showHelp(CommandSender sender) {
        List<String> helpList = new ArrayList<>();
        if (sender.hasPermission("spawncontrol.reload")) {
            helpList.add(ChatColor.GOLD + "/spawncontrol reload " + ChatColor.WHITE + "- reload the plugin");
        }
        if (sender.hasPermission("spawncontrol.logging")) {
            helpList.add(ChatColor.GOLD + "/spawncontrol logging enable/disable " + ChatColor.WHITE + "- enable/disable entity spawn logging for you");
        }

        if (helpList.size() != 0) {
            sender.sendMessage(SpawnControl.CHAT_PREFIX + "Available command list:");
            for (String msgText : helpList) {
                sender.sendMessage(msgText);
            }
        }
    }
}
