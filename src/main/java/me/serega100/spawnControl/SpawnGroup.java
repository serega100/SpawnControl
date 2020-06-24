package me.serega100.spawnControl;

import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Objects;
import java.util.Set;

public class SpawnGroup {
    private final String name;
    private final Mode mode;
    private final Set<CreatureSpawnEvent.SpawnReason> reasons;

    public SpawnGroup(String name, Mode mode, Set<CreatureSpawnEvent.SpawnReason> reasons) {
        this.name = name;
        this.mode = mode;
        this.reasons = reasons;
    }

    public Mode getMode() {
        return mode;
    }

    public boolean hasReason(CreatureSpawnEvent.SpawnReason reason) {
        return reasons.contains(reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mode, reasons);
    }

    enum Mode {
        ALLOW,
        DISALLOW
    }
}
