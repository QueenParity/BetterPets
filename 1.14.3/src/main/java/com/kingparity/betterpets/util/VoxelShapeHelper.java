package com.kingparity.betterpets.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import java.util.Collection;

/**
 * Credit: MrCrayfish's Furniture Mod
 */
public class VoxelShapeHelper
{
    public static VoxelShape combineAll(Collection<VoxelShape> shapes)
    {
        VoxelShape result = VoxelShapes.empty();
        for(VoxelShape shape : shapes)
        {
            result = VoxelShapes.combine(result, shape, IBooleanFunction.OR);
        }
        return result.simplify();
    }

    /*public static VoxelShape[] getRotatedShapes(VoxelShape source)
    {
        VoxelShape shapeNorth = rotate(source, Direction.NORTH);
        VoxelShape shapeEast = rotate(source, Direction.EAST);
        VoxelShape shapeSouth = rotate(source, Direction.SOUTH);
        VoxelShape shapeWest = rotate(source, Direction.WEST);
        return new VoxelShape[] { shapeSouth, shapeWest, shapeNorth, shapeEast };
    }

    public static VoxelShape rotate(VoxelShape source, Direction direction)
    {
        double[] adjustedValues = adjustValues(direction, source.getStart(Direction.Axis.X), source.getStart(Direction.Axis.Z), source.getEnd(Direction.Axis.X), source.getEnd(Direction.Axis.Z));
        return VoxelShapes.create(adjustedValues[0], source.getStart(Direction.Axis.Y), adjustedValues[1], adjustedValues[2], source.getEnd(Direction.Axis.Y), adjustedValues[3]);
    }

    private static double[] adjustValues(Direction direction, double var1, double var2, double var3, double var4)
    {
        switch(direction)
        {
            case WEST:
                double var_temp_1 = var1;
                var1 = 1.0F - var3;
                double var_temp_2 = var2;
                var2 = 1.0F - var4;
                var3 = 1.0F - var_temp_1;
                var4 = 1.0F - var_temp_2;
                break;
            case NORTH:
                double var_temp_3 = var1;
                var1 = var2;
                var2 = 1.0F - var3;
                var3 = var4;
                var4 = 1.0F - var_temp_3;
                break;
            case SOUTH:
                double var_temp_4 = var1;
                var1 = 1.0F - var4;
                double var_temp_5 = var2;
                var2 = var_temp_4;
                double var_temp_6 = var3;
                var3 = 1.0F - var_temp_5;
                var4 = var_temp_6;
                break;
            default:
                break;
        }
        return new double[]{var1, var2, var3, var4};
    }*/
    
    public static VoxelShape[] getRotatedShapes(VoxelShape source)
    {
        VoxelShape shapeNorth = rotate(source, Direction.NORTH);
        VoxelShape shapeEast = rotate(source, Direction.EAST);
        VoxelShape shapeSouth = rotate(source, Direction.SOUTH);
        VoxelShape shapeWest = rotate(source, Direction.WEST);
        VoxelShape shapeUp = rotate(source, Direction.UP);
        VoxelShape shapeDown = rotate(source, Direction.DOWN);
        return new VoxelShape[] { shapeDown, shapeUp, shapeNorth, shapeSouth, shapeWest, shapeEast };
    }
    
    public static VoxelShape rotate(VoxelShape source, Direction direction)
    {
        double[] adjustedValues = adjustValues(direction, source.getStart(Direction.Axis.X), source.getStart(Direction.Axis.Y), source.getStart(Direction.Axis.Z), source.getEnd(Direction.Axis.X), source.getEnd(Direction.Axis.Y), source.getEnd(Direction.Axis.Z));
        return VoxelShapes.create(adjustedValues[0], adjustedValues[1], adjustedValues[2], adjustedValues[3], adjustedValues[4], adjustedValues[5]);
    }
    
    private static double[] adjustValues(Direction direction, double startX, double startY, double startZ, double endX, double endY, double endZ)
    {
        switch(direction)
        {
            case DOWN:
                double var_temp_8 = startY;
                startY = 1.0F - endZ;
                double var_temp_9 = startZ;
                startZ = var_temp_8;
                double var_temp_10 = endY;
                endY = 1.0F - var_temp_9;
                endZ = var_temp_10;
                break;
            case UP:
                double var_temp_7 = startY;
                startY = startZ;
                startZ = 1.0F - endY;
                endY = endZ;
                endZ = 1.0F - var_temp_7;
                break;
            case WEST:
                double var_temp_1 = startX;
                startX = 1.0F - endX;
                double var_temp_2 = startZ;
                startZ = 1.0F - endZ;
                endX = 1.0F - var_temp_1;
                endZ = 1.0F - var_temp_2;
                break;
            case NORTH:
                double var_temp_3 = startX;
                startX = startZ;
                startZ = 1.0F - endX;
                endX = endZ;
                endZ = 1.0F - var_temp_3;
                break;
            case SOUTH:
                double var_temp_4 = startX;
                startX = 1.0F - endZ;
                double var_temp_5 = startZ;
                startZ = var_temp_4;
                double var_temp_6 = endX;
                endX = 1.0F - var_temp_5;
                endZ = var_temp_6;
                break;
            case EAST:
                break;
        }
        return new double[]{startX, startY, startZ, endX, endY, endZ};
    }
}