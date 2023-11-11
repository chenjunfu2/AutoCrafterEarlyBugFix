package net.quackimpala7321.crafter.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.quackimpala7321.crafter.AutocrafterEarly;
import net.quackimpala7321.crafter.block.entity.CrafterBlockEntity;

public class ModBlockEntities {
    public static final BlockEntityType<CrafterBlockEntity> CRAFTER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier("crafter"),
            FabricBlockEntityTypeBuilder.create(CrafterBlockEntity::new, ModBlocks.CRAFTER).build()
    );

    public static void registerBlockEntities() {}
}
