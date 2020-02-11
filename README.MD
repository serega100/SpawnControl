# SpawnControl
Simple Spigot plugin that allows current spawn reasons in current WorldGuard regions
## How to use it
Firstly, add into config.yml groups and their spawn reasons to allow like in the example below
```yaml
allowed-spawn-reason-groups:
  default:
    - CUSTOM
    - SPAWNER_EGG
  plugins_only:
    - CUSTOM
`````
The spawn reasons can be found on [Spigot JavaDocs](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/entity/CreatureSpawnEvent.SpawnReason.html)
 
Secondly, set flag _spawn-control_ for current regions `/rg setflag <region> spawn-control <spawn-group>`

And it's ready to work :smiley:

## Commands
####/spawncontrol reload
**Permission:** spawncontorol.reload\
**Description:** reloads the plugin configuration

I don't recommend to use it because 

####/spawncontrol logging enable/disable
**Permission:** spawncontrol.logging\
**Description:** start/stop showing in console all mobs with their spawn reason that has spawned now.
The command helps to get important spawn reasons
## Todo list
- [ ] Make table with spawn reasons
- [ ] Solve ACF problem (include it to build or public it as an external plugin)
- [ ] Add build of the plugin to Spigot site
- [ ] Logging spawn reasons to player

If you have any idea or any problem with the plugin, don't be afraid to issue it.