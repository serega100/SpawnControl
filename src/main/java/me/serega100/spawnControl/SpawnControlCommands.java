package me.serega100.spawnControl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;

@CommandAlias("spawncontrol")
public class SpawnControlCommands extends BaseCommand {
    private SpawnControl plugin;

    SpawnControlCommands(SpawnControl plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @CommandPermission("spawncontrol.reload")
    public void reload(CommandSender sender) {
        sender.sendMessage("SpawnControl | Reload configuration...");
        plugin.reload();
        sender.sendMessage("SpawnControl | Configuration reloaded!");
    }

    @Subcommand("logging")
    @CommandPermission("spawncontrol.logging")
    @CommandCompletion("enable|disable")
    public void logging(CommandSender sender, String resp) {
        if (resp.equalsIgnoreCase("enable")) {
            plugin.setLoggingMode(true);
            plugin.printInConsole("Entity spawn logging has %s", "enabled");
        } else if (resp.equalsIgnoreCase("disable")) {
            plugin.setLoggingMode(false);
            plugin.printInConsole("Entity spawn logging has %s", "disabled");
        } else {
            plugin.printInConsole("Logging may enable or disable only!");
        }
    }
}
