package net.quackimpala7321.crafter;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;
import net.quackimpala7321.crafter.registry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutocrafterEarly implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MOD_ID = "crafter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModScreenHandlers.registerScreenHandlers();
		ModBlocks.registerBlocks();
		ModBlockEntities.registerBlockEntities();
		ModParticles.registerParticles();
		ModSoundEvents.registerSounds();

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.addAfter(Blocks.DROPPER, ModBlocks.CRAFTER));
	}
}
