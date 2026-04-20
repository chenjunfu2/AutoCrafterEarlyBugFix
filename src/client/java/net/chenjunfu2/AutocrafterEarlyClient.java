package net.chenjunfu2;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.chenjunfu2.gui.screen.ingame.CrafterScreen;
import net.chenjunfu2.registry.ClientModParticles;
import net.chenjunfu2.registry.ModScreenHandlers;

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