/*******************************************************************************
 * Copyright 2014-2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/

package biomesoplenty.common.init;

import static biomesoplenty.api.item.BOPItems.ash;
import static biomesoplenty.api.item.BOPItems.berries;
import static biomesoplenty.api.item.BOPItems.biome_essence;
import static biomesoplenty.api.item.BOPItems.biome_finder;
import static biomesoplenty.api.item.BOPItems.black_dye;
import static biomesoplenty.api.item.BOPItems.blue_dye;
import static biomesoplenty.api.item.BOPItems.boat_cherry;
import static biomesoplenty.api.item.BOPItems.boat_ebony;
import static biomesoplenty.api.item.BOPItems.boat_ethereal;
import static biomesoplenty.api.item.BOPItems.boat_eucalyptus;
import static biomesoplenty.api.item.BOPItems.boat_fir;
import static biomesoplenty.api.item.BOPItems.boat_hellbark;
import static biomesoplenty.api.item.BOPItems.boat_jacaranda;
import static biomesoplenty.api.item.BOPItems.boat_magic;
import static biomesoplenty.api.item.BOPItems.boat_mahogany;
import static biomesoplenty.api.item.BOPItems.boat_mangrove;
import static biomesoplenty.api.item.BOPItems.boat_palm;
import static biomesoplenty.api.item.BOPItems.boat_pine;
import static biomesoplenty.api.item.BOPItems.boat_redwood;
import static biomesoplenty.api.item.BOPItems.boat_sacred_oak;
import static biomesoplenty.api.item.BOPItems.boat_umbran;
import static biomesoplenty.api.item.BOPItems.boat_willow;
import static biomesoplenty.api.item.BOPItems.brown_dye;
import static biomesoplenty.api.item.BOPItems.crystal_shard;
import static biomesoplenty.api.item.BOPItems.earth;
import static biomesoplenty.api.item.BOPItems.filled_honeycomb;
import static biomesoplenty.api.item.BOPItems.fleshchunk;
import static biomesoplenty.api.item.BOPItems.gem;
import static biomesoplenty.api.item.BOPItems.green_dye;
import static biomesoplenty.api.item.BOPItems.honeycomb;
import static biomesoplenty.api.item.BOPItems.jar_empty;
import static biomesoplenty.api.item.BOPItems.jar_filled;
import static biomesoplenty.api.item.BOPItems.mud_brick;
import static biomesoplenty.api.item.BOPItems.mudball;
import static biomesoplenty.api.item.BOPItems.peach;
import static biomesoplenty.api.item.BOPItems.pear;
import static biomesoplenty.api.item.BOPItems.persimmon;
import static biomesoplenty.api.item.BOPItems.record_wanderer;
import static biomesoplenty.api.item.BOPItems.ricebowl;
import static biomesoplenty.api.item.BOPItems.shroompowder;
import static biomesoplenty.api.item.BOPItems.terrestrial_artifact;
import static biomesoplenty.api.item.BOPItems.white_dye;

import biomesoplenty.api.sound.BOPSounds;
import biomesoplenty.common.command.BOPCommand;
import biomesoplenty.common.item.ItemBOPFood;
import biomesoplenty.common.item.ItemBOPRecord;
import biomesoplenty.common.item.ItemBiomeEssence;
import biomesoplenty.common.item.ItemBiomeFinder;
import biomesoplenty.common.item.ItemGem;
import biomesoplenty.common.item.ItemJarEmpty;
import biomesoplenty.common.item.ItemJarFilled;
import biomesoplenty.common.item.ItemMudball;
import biomesoplenty.common.util.inventory.CreativeTabBOP;
import biomesoplenty.core.BiomesOPlenty;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSoup;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;

public class ModItems
{    
    public static void init()
    {
        registerItems();
    }
    
    public static void registerItems()
    {
        jar_filled = registerItem(new ItemJarFilled(), "jar_filled");
        jar_empty = registerItem(new ItemJarEmpty(), "jar_empty");

        biome_finder = registerItem(new ItemBiomeFinder(), "biome_finder");
        
        record_wanderer = registerItem(new ItemBOPRecord("wanderer", BOPSounds.records_wanderer), "record_wanderer");
        
        mudball = registerItem(new ItemMudball(), "mudball");
        mud_brick = registerItem(new Item(), "mud_brick");
        ash = registerItem(new Item(), "ash");
        fleshchunk = registerItem(new Item(), "fleshchunk");
        
        gem = registerItem(new ItemGem(), "gem");
        
        terrestrial_artifact = registerItem(new Item(), "terrestrial_artifact");
        terrestrial_artifact.setMaxStackSize(1);
        
        crystal_shard = registerItem(new Item(), "crystal_shard");
        
        biome_essence = registerItem(new ItemBiomeEssence(), "biome_essence");
        
        // food
    	berries =           registerItem(new ItemBOPFood(1, 0.1F, 8), "berries"); 
    	pear =              registerItem(new ItemFood(5, 0.3F, false), "pear");
    	peach =             registerItem(new ItemFood(5, 0.2F, false), "peach");
        persimmon =         registerItem(new ItemFood(5, 0.2F, false), "persimmon");
        
        honeycomb = registerItem(new Item(), "honeycomb");
        filled_honeycomb =  registerItem(new ItemBOPFood(3, 0.4F, 16), "filled_honeycomb");
        
        shroompowder =      registerItem(new ItemFood(1, 0.1F, false), "shroompowder");
        ((ItemFood)shroompowder).setAlwaysEdible();
        ((ItemFood)shroompowder).setPotionEffect(new PotionEffect(MobEffects.NAUSEA, 225, 0), 1.0F);
        
        ricebowl =          registerItem(new ItemSoup(2), "ricebowl");
        
        // TODO: move dyes to their own class?
        blue_dye = registerItem(new Item(), "blue_dye");
        brown_dye = registerItem(new Item(), "brown_dye");
        green_dye = registerItem(new Item(), "green_dye");
        white_dye = registerItem(new Item(), "white_dye");
        black_dye = registerItem(new Item(), "black_dye");
        
        earth = registerItem(new Item(), "earth", null);
    }
    
    public static Item registerItem(Item item, String name)
    {
        return registerItem(item, name, CreativeTabBOP.instance);
    }
    
    public static Item registerItem(Item item, String name, @Nullable CreativeTabs tab)
    {    
        item.setUnlocalizedName(name);
        if (tab != null)
        {
            item.setCreativeTab(CreativeTabBOP.instance);
        }

        item.setRegistryName(new ResourceLocation(BiomesOPlenty.MOD_ID, name));
        ForgeRegistries.ITEMS.register(item);
        BOPCommand.itemCount++;
        BiomesOPlenty.proxy.registerItemSided(item);
        
        return item;   
    }
}
