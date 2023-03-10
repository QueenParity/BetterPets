package com.kingparity.betterpets.blockentity;

import com.google.common.collect.Maps;
import net.minecraft.core.Direction;

import java.util.Map;

public enum Parts
{
    DOWN(Direction.DOWN, "down"),
    UP(Direction.UP, "up"),
    NORTH(Direction.NORTH, "north"),
    SOUTH(Direction.SOUTH, "south"),
    WEST(Direction.WEST, "west"),
    EAST(Direction.EAST, "east"),
    CENTER(null, "center");
    
    public static final Parts[] FACES;
    
    private static final Map<Direction, Parts> facingMap = Maps.newEnumMap(Direction.class);
    
    private final Direction face;
    private final String name;
    
    static
    {
        for(Parts part : values())
        {
            if(part.face != null)
            {
                facingMap.put(part.face, part);
            }
        }
        FACES = fromFacingArray(Direction.values());
    }
    
    Parts(Direction face, String name)
    {
        this.face = face;
        this.name = name;
    }
    
    private static Parts[] fromFacingArray(Direction... faces)
    {
        Parts[] arr = new Parts[faces.length];
        for(int i = 0; i < faces.length; i++)
        {
            arr[i] = fromFacing(faces[i]);
        }
        return arr;
    }
    
    public static Parts fromFacing(Direction face)
    {
        if(face == null)
        {
            return CENTER;
        }
        return facingMap.get(face);
    }
    
    public Direction getFace()
    {
        return face;
    }
    
    public int getIndex()
    {
        if(face == null)
        {
            return 6;
        }
        return face.get3DDataValue();
    }
    
    public String getName(boolean capitalized)
    {
        if(capitalized)
        {
            return name.substring(0,1).toUpperCase() + name.substring(1);
        }
        return name;
    }
}