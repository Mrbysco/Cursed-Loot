package com.mrbysco.cursedloot.util;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.util.info.CursePos;
import net.minecraft.nbt.CompoundNBT;

public enum CurseDirection {
    NORTH("north", new CursePos(32, 0)),
    NORTH_EAST("northeast", new CursePos(64, 0)),
    EAST("east", new CursePos(64, 32)),
    SOUTH_EAST("southeast", new CursePos(64, 64)),
    SOUTH("south", new CursePos(32, 64)),
    SOUTH_WEST("southwest", new CursePos(0, 64)),
    WEST("west", new CursePos(0, 32)),
    NORTH_WEST("northwest", new CursePos(0, 0));

    private final String curseTag;
    private final CursePos directionPos;

    private CurseDirection(String direction, CursePos directionPos) {
        this.curseTag = Reference.PREFIX + direction;
        this.directionPos = directionPos;
    }

    public String getDirectionTag() {
        return this.curseTag;
    }

    public CursePos getDirectionPos() {
        return this.directionPos;
    }

    public static CursePos getDirectionFromTag(CompoundNBT tag) {
        for(CurseDirection direction : values()) {
            if(tag.getBoolean(direction.curseTag)) {
                return direction.getDirectionPos();
            }
        }
        return new CursePos(0, 0);
    }

    public static CurseDirection getByName(String name) {
        return CurseDirection.valueOf(name);
    }
}
