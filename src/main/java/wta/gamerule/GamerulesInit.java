package wta.gamerule;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.world.GameRules;
import wta.Block_effects;
import wta.gamerule.types.IdentifierEntryedListGRRule;

import java.util.ArrayList;

public class GamerulesInit {
	public  static GameRules.Key<IdentifierEntryedListGRRule<StatusEffect>> BlockDisabledEffectsGR;
    public  static GameRules.Key<GameRules.IntRule> BlockEffectsDuration;
	//public  static GameRules.Key<GameRules.BooleanRule> IsBlockEffectsDisabledGR;

	public static void init(){
		BlockDisabledEffectsGR = GameRuleRegistry.register(
			  "block_disabled_effects",
			  GameRules.Category.MISC,
			  new GameRules.Type<IdentifierEntryedListGRRule<StatusEffect>>(
				    StringArgumentType::greedyString,
				    (type) -> new IdentifierEntryedListGRRule<>(type, Registries.STATUS_EFFECT, new ArrayList<>()),
				    (minecraftServer, gamerule) -> {gamerule.onChanged(); Block_effects.reInitEffects(gamerule.getTValue());},
				    (visitor, key, type) -> visitor.visit(key, type)
			  )
		);
        BlockEffectsDuration = GameRuleRegistry.register(
                "block_effects_duration",
                GameRules.Category.MISC,
                GameRuleFactory.createIntRule(140, 30)
        );
		/*/IsBlockEffectsDisabledGR=GameRuleRegistry.register(
			  "is_block_effects_disabled",
			  GameRules.Category.MOBS,
			  new GameRules.Type<>(
				    BoolArgumentType::bool,
				    (type) -> new GameRules.BooleanRule(type, true),
				    (minecraftServer, booleanRule) -> {},
				    GameRules.Visitor::visitBoolean
			  )
		);/*/
	}
}
