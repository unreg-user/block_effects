package wta.other;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static wta.Block_effects.MODID;
import static wta.Block_effects.allEffectList;

public class OnLoad implements IdentifiableResourceReloadListener {
	public static final Identifier id=Identifier.of(MODID, "on_load");

	@Override
	public Identifier getFabricId() {
		return id;
	}

	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		return CompletableFuture.runAsync(() -> {}).thenCompose(synchronizer::whenPrepared).thenAccept((q) -> allEffectList=null);
	}
}
