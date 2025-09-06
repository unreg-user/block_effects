package wta;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.resource.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wta.command.CommandsInit;
import wta.gamerule.GamerulesInit;
import wta.other.OnLoad;

import java.util.List;

public class Block_effects implements ModInitializer {
	public static final String MODID = "block_effects";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static List<StatusEffect> allEffectList;

	@Override
	public void onInitialize() {
		GamerulesInit.init();
		CommandsInit.init();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new OnLoad());
		LOGGER.info("Hello Fabric world! Now blocks have effects! Be careful!");
	}
}