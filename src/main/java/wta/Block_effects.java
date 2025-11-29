package wta;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wta.blocks.BlocksInit;
import wta.command.CommandsInit;
import wta.gamerule.GamerulesInit;
import wta.other.OnLoad;

import java.util.List;
import java.util.Map;

public class Block_effects implements ModInitializer {
	public static final String MODID = "block_effects";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static List<StatusEffect> allEffectList;

	@Override
	public void onInitialize() {
		GamerulesInit.init();
		CommandsInit.init();
		BlocksInit.init();
		AllInit.init();
		ServerLifecycleEvents.SERVER_STARTED.register(new OnLoad());
		LOGGER.info("Hello Fabric world! Now blocks have effects! Be careful!");
	}

	public static void reInitEffects(ServerWorld world){
		reInitEffects(world.getGameRules().get(GamerulesInit.BlockDisabledEffectsGR).getTValue());
	}
	public static void reInitEffects(MinecraftServer server){
		reInitEffects(server.getGameRules().get(GamerulesInit.BlockDisabledEffectsGR).getTValue());
	}

	public static void reInitEffects(List<StatusEffect> disabled_effects){
		allEffectList=Registries.STATUS_EFFECT.getEntrySet().stream().map(Map.Entry::getValue).filter(e -> !disabled_effects.contains(e)).toList();
	}
}