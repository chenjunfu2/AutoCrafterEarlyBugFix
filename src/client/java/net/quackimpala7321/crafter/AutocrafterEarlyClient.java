package net.quackimpala7321.crafter;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.quackimpala7321.crafter.gui.screen.ingame.CrafterScreen;
import net.quackimpala7321.crafter.registry.ClientModParticles;
import net.quackimpala7321.crafter.registry.ModScreenHandlers;

public class AutocrafterEarlyClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HandledScreens.register(ModScreenHandlers.CRAFTER_3X3, CrafterScreen::new);
		ClientModParticles.registerParticles();
	}

//	private static void registerReceivers() {
//
//	}
}