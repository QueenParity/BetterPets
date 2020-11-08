package com.kingparity.betterpets.util;



import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

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
            result = VoxelShapes.combine(result, shape, BooleanBiFunction.OR);
        }
        return result.simplify();
    }

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
        double[] adjustedValues = adjustValues(direction, source.getMin(Direction.Axis.X), source.getMin(Direction.Axis.Y), source.getMin(Direction.Axis.Z), source.getMax(Direction.Axis.X), source.getMax(Direction.Axis.Y), source.getMax(Direction.Axis.Z));
        return VoxelShapes.cuboid(adjustedValues[0], adjustedValues[1], adjustedValues[2], adjustedValues[3], adjustedValues[4], adjustedValues[5]);
    }

    private static double[] adjustValues(Direction direction, double startX, double startY, double startZ, double endX, double endY, double endZ)
    {
        switch(direction)
        {
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
            case WEST:
                double var_temp_1 = startX;
                startX = 1.0F - endX;
                double var_temp_2 = startZ;
                startZ = 1.0F - endZ;
                endX = 1.0F - var_temp_1;
                endZ = 1.0F - var_temp_2;
                break;
            case DOWN:
                double var_temp_7 = startX;
                startX = startY;
                startY = 1.0F - endX;
                endX = endY;
                endY = 1.0F - var_temp_7;
                break;
            case UP:
                double var_temp_8 = startX;
                startX = 1.0F - endY;
                double var_temp_9 = startY;
                startY = var_temp_8;
                double var_temp_10 = endX;
                endX = 1.0F - var_temp_9;
                endY = var_temp_10;
                break;
            default:
                break;
        }
        return new double[]{startX, startY, startZ, endX, endY, endZ};
    }
}