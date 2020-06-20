package me.serega100.spawnControl;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;

import javax.annotation.Nullable;

public class GroupFlag extends Flag<String> {

    public GroupFlag() {
        super("spawn-control");
    }

    @Override
    public String parseInput(FlagContext flagContext) throws InvalidFlagFormat {
        String str = flagContext.getUserInput().split(" ")[0].toLowerCase();
        if (SpawnControl.getInstance().hasGroup(str)) {
            return str;
        } else {
            throw new InvalidFlagFormat(SpawnControl.CHAT_PREFIX + "Group " + str + " is not defined!");
        }
    }

    @Override
    public String unmarshal(@Nullable Object o) {
        return o instanceof String ? (String)o : null;
    }

    @Override
    public Object marshal(String s) {
        return s;
    }
}
