package wta.other;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static wta.Block_effects.*;

public class OnLoad implements ServerLifecycleEvents.ServerStarted {
	@Override
	public void onServerStarted(MinecraftServer minecraftServer) {
		reInitEffects(minecraftServer);
	}
}
