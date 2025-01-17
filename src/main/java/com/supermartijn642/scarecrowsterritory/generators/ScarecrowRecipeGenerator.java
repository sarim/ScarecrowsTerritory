package com.supermartijn642.scarecrowsterritory.generators;

import com.supermartijn642.core.generator.RecipeGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.scarecrowsterritory.ScarecrowType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

/**
 * Created 02/01/2023 by SuperMartijn642
 */
public class ScarecrowRecipeGenerator extends RecipeGenerator {

    public ScarecrowRecipeGenerator(ResourceCache cache){
        super("scarecrowsterritory", cache);
    }

    @Override
    public void generate(){
        // Primitive scarecrow
        this.shaped("scarecrow", ScarecrowType.PRIMITIVE.items.get(DyeColor.PURPLE))
            .pattern(" A ")
            .pattern("BCB")
            .pattern(" B ")
            .input('A', Items.CARVED_PUMPKIN)
            .input('B', Tags.Items.RODS_WOODEN)
            .input('C', Items.HAY_BLOCK)
            .unlockedBy(Tags.Items.RODS_WOODEN);

        // Colored scarecrows
        TagKey<Item> scarecrowTag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("scarecrowsterritory", "primitive_scarecrows"));
        for(DyeColor color : DyeColor.values()){
            this.shapeless(ScarecrowType.PRIMITIVE.items.get(color))
                .input(scarecrowTag)
                .input(color.getTag())
                .unlockedBy(scarecrowTag);
        }
    }
}
