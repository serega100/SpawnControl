# SpawnControl
Simple Spigot plugin that allows certain spawn reasons in certain WorldGuard regions
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
### /spawncontrol reload
**Permission:** spawncontorol.reload\
**Description:** reloads the plugin configuration

I recommend using it while your server is debugging. The plugin doesn't work while reloading.

### /spawncontrol logging enable/disable
**Permission:** spawncontrol.logging\
**Description:** start/stop showing in console all mobs that has spawned now and their spawn reason.
The command helps to get necessary spawn reasons.
## Todo list
- [x] Initial sketch of the plugin
- [x] Publish the plugin on GitHub
- [x] Move the plugin from ACF to Spigot Command 
- [x] Logging spawn reasons to player
- [ ] Add disallow-spawn-reason-groups
- [ ] Publish builds of the plugin to Spigot site


If you have any idea or any problem with the plugin, don't be afraid to issue it.