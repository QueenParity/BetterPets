package com.kingparity.betterpets.blockentity;

import com.google.common.collect.Maps;
import net.minecraft.core.Direction;

import java.util.Map;

public enum Parts
{
    DOWN(Direction.DOWN),
    UP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST),
    CENTER(null);
    
    public static final Parts[] FACES;
    
    private static final Map<Direction, Parts> facingMap = Maps.newEnumMap(Direction.class);
    
    public final Direction face;
    
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
    
    Parts(Direction face)
    {
        this.face = face;
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
    
    public int getIndex()
    {
        if(face == null)
        {
            return 6;
        }
        return face.get3DDataValue();
    }
}