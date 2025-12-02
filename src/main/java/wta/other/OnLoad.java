package wta.other;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import static wta.Block_effects.reInitEffects;

public class OnLoad implements ServerLifecycleEvents.ServerStarted {
	@Override
	public void onServerStarted(MinecraftServer minecraftServer) {
		reInitEffects(minecraftServer);
	}
}
