package com.kingparity.betterpets.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;

public class PropertiesBlock extends Block
{
    public PropertiesBlock(Material material)
    {
        this(material, material.getColor());
    }
    
    public PropertiesBlock(Material material, DyeColor dyeColor)
    {
        this(material, dyeColor.getMapColor());
    }
    
    public PropertiesBlock(Material material, MaterialColor materialColor)
    {
        super(Block.Properties.create(material, materialColor));
    }
    
    public PropertiesBlock(Material material, float hardnessAndResistance)
    {
        this(material, material.getColor(), hardnessAndResistance, hardnessAndResistance);
    }
    
    public PropertiesBlock(Material material, DyeColor dyeColor, float hardnessAndResistance)
    {
        this(material, dyeColor.getMapColor(), hardnessAndResistance, hardnessAndResistance);
    }
    
    public PropertiesBlock(Material material, MaterialColor materialColor, float hardnessAndResistance)
    {
        super(Block.Properties.create(material, materialColor).hardnessAndResistance(hardnessAndResistance, hardnessAndResistance));
    }
    
    public PropertiesBlock(Material material, float hardness, float resistance)
    {
        this(material, material.getColor(), hardness, resistance);
    }
    
    public PropertiesBlock(Material material, DyeColor dyeColor, float hardness, float resistance)
    {
        this(material, dyeColor.getMapColor(), hardness, resistance);
    }
    
    public PropertiesBlock(Material material, MaterialColor materialColor, float hardness, float resistance)
    {
        super(Block.Properties.create(material, materialColor).hardnessAndResistance(hardness, resistance));
    }
}
