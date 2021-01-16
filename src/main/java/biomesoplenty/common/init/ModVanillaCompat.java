package biomesoplenty.common.init;

import java.util.List;

import com.google.common.collect.Lists;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.entities.projectiles.dispenser.DispenserBehaviorMudball;
import biomesoplenty.common.util.biome.BiomeUtils;
import biomesoplenty.common.world.BOPMapGenScatteredFeature;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.WoodlandMansion;

public class ModVanillaCompat
{
    public static void init()
    {
    	registerDispenserBehaviors();
    	
    	MapGenStructureIO.registerStructure(BOPMapGenScatteredFeature.Start.class, "BOPTemple");
    	List<Biome> mansionBiomes = BiomeUtils.filterPresentBiomes(BOPBiomes.coniferous_forest, BOPBiomes.dead_forest, BOPBiomes.ominous_woods, BOPBiomes.snowy_coniferous_forest, BOPBiomes.woodland);
    	mansionBiomes.addAll(Lists.newArrayList(Biomes.ROOFED_FOREST, Biomes.MUTATED_ROOFED_FOREST));
    	WoodlandMansion.ALLOWED_BIOMES = mansionBiomes;
    }
    
    private static void registerDispenserBehaviors()
    {
    	BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(BOPItems.mudball, new DispenserBehaviorMudball());
    }
}
