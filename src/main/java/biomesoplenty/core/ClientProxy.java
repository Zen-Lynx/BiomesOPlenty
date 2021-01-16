/*******************************************************************************
 * Copyright 2014-2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/

package biomesoplenty.core;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import biomesoplenty.api.item.BOPItems;
import biomesoplenty.api.particle.BOPParticleTypes;
import biomesoplenty.client.particle.EntityCurseFX;
import biomesoplenty.client.particle.EntityTrailFX;
import biomesoplenty.common.block.IBOPBlock;
import biomesoplenty.common.config.MiscConfigurationHandler;
import biomesoplenty.common.entities.EntityWasp;
import biomesoplenty.common.entities.RenderWasp;
import biomesoplenty.common.entities.projectiles.EntityMudball;
import biomesoplenty.common.entities.projectiles.RenderMudball;
import biomesoplenty.common.item.IColoredItem;
import biomesoplenty.common.util.inventory.CreativeTabBOP;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static ResourceLocation[] bopTitlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_0.png"), new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_1.png"), new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_2.png"), new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_3.png"), new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_4.png"), new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_5.png")};
    public static ResourceLocation particleTexturesLocation = new ResourceLocation("biomesoplenty:textures/particles/particles.png");

    private static List<Block> blocksToColour = Lists.newArrayList();
    private static List<Item> itemsToColor = Lists.newArrayList();
    
    @Override
    public void registerRenderers()
    {
        if (MiscConfigurationHandler.overrideTitlePanorama)
            GuiMainMenu.TITLE_PANORAMA_PATHS = bopTitlePanoramaPaths;

        //Entity rendering and other stuff will go here in future
        registerEntityRenderer(EntityWasp.class, RenderWasp.class);
        registerEntityRenderer(EntityMudball.class, RenderMudball.class);
    }

    @Override
    public void registerColouring()
    {
        for (Block block : blocksToColour)
        {
            IBOPBlock bopBlock = (IBOPBlock)block;
            if (bopBlock.getBlockColor() != null)Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(bopBlock.getBlockColor(), block);
            if (bopBlock.getItemColor() != null) Minecraft.getMinecraft().getItemColors().registerItemColorHandler(bopBlock.getItemColor(), block);
        }
        
        for (Item item : itemsToColor)
        {
            IColoredItem coloredItem = (IColoredItem)item;
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(coloredItem.getItemColor(), item);
        }
    }

    @Override
    public void registerItemVariantModel(Item item, String name, int metadata)
    {
        Preconditions.checkNotNull(item, "Cannot register models for null item " + name);
        Preconditions.checkArgument(item != Items.AIR, "Cannot register models for air (" + name + ")");

        ModelLoader.registerItemVariants(item, new ResourceLocation("biomesoplenty:" + name));
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(BiomesOPlenty.MOD_ID + ":" + name, "inventory"));
    }

    @Override
    public void registerBlockSided(Block block)
    {
        if (block instanceof IBOPBlock)
        {
            IBOPBlock bopBlock = (IBOPBlock)block;

            //Register non-rendering properties
            IProperty[] nonRenderingProperties = bopBlock.getNonRenderingProperties();

            if (nonRenderingProperties != null)
            {
                // use a custom state mapper which will ignore the properties specified in the block as being non-rendering
                IStateMapper custom_mapper = (new StateMap.Builder()).ignore(nonRenderingProperties).build();
                ModelLoader.setCustomStateMapper(block, custom_mapper);
            }

            //Register colour handlers
            if (bopBlock.getBlockColor() != null || bopBlock.getItemColor() != null)
            {
                blocksToColour.add(block);
            }
        }
    }
    
    @Override
    public void registerItemSided(Item item)
    {
        // register sub types if there are any
        if (item.getHasSubtypes())
        {
            NonNullList<ItemStack> subItems = NonNullList.create();
            item.getSubItems(CreativeTabBOP.instance, subItems);
            for (ItemStack subItem : subItems)
            {
                String subItemName = item.getUnlocalizedName(subItem);
                subItemName =  subItemName.substring(subItemName.indexOf(".") + 1); // remove 'item.' from the front

                ModelLoader.registerItemVariants(item, new ResourceLocation(BiomesOPlenty.MOD_ID, subItemName));
                ModelLoader.setCustomModelResourceLocation(item, subItem.getMetadata(), new ModelResourceLocation(BiomesOPlenty.MOD_ID + ":" + subItemName, "inventory"));
            }
        }
        else
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(BiomesOPlenty.MOD_ID + ":" + item.delegate.name().getResourcePath(), "inventory"));
        }
        
        //Register colour handlers
        if (item instanceof IColoredItem && ((IColoredItem)item).getItemColor() != null)
        {
            itemsToColor.add(item);
        }
    }

    @Override
    public void registerFluidBlockRendering(Block block, String name) 
    {
        final ModelResourceLocation fluidLocation = new ModelResourceLocation(BiomesOPlenty.MOD_ID + ":fluids", name);

        // use a custom state mapper which will ignore the LEVEL property
        ModelLoader.setCustomStateMapper(block, new StateMapperBase()
        {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return fluidLocation;
            }
        });
    }
    
    @Override
    public void spawnParticle(BOPParticleTypes type, double x, double y, double z, Object... info)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        World world = minecraft.world;

        if (world == null)
        {
            return;
        }

        Particle entityFx = null;
        switch (type)
        {
        case MUD:
            int itemId = Item.getIdFromItem(BOPItems.mudball);
            minecraft.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, x, y, z, MathHelper.nextDouble(world.rand, -0.08D, 0.08D), MathHelper.nextDouble(world.rand, -0.08D, 0.08D), MathHelper.nextDouble(world.rand, -0.08D, 0.08D), itemId);
            return;
        case PLAYER_TRAIL:
            if (info.length < 1)
                throw new RuntimeException("Missing argument for trail name!");

            entityFx = new EntityTrailFX(world, x, y, z, (String)info[0]);
            break;
        case CURSE:
            entityFx = new EntityCurseFX(world, x, y, z, MathHelper.nextDouble(world.rand, -0.03, 0.03), 0.05D, MathHelper.nextDouble(world.rand, -0.03, 0.03));
            break;
        default:
            break;
        }

        if (entityFx != null) {minecraft.effectRenderer.addEffect(entityFx);}
    }
    
    // 
    // The below method and class is used as part of Forge 1668+'s workaround for render manager being null during preinit
    //

    private static <E extends Entity> void registerEntityRenderer(Class<E> entityClass, Class<? extends Render<E>> renderClass)
    {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new EntityRenderFactory<E>(renderClass));
    }

    private static class EntityRenderFactory<E extends Entity> implements IRenderFactory<E>
    {
        private Class<? extends Render<E>> renderClass;

        private EntityRenderFactory(Class<? extends Render<E>> renderClass)
        {
            this.renderClass = renderClass;
        }

        @Override
        public Render<E> createRenderFor(RenderManager manager) 
        {
            Render<E> renderer = null;

            try 
            {
                renderer = renderClass.getConstructor(RenderManager.class).newInstance(manager);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }

            return renderer;
        }
    }
}
