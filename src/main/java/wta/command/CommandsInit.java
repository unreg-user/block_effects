package wta.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import wta.gamerule.GamerulesInit;
import wta.gamerule.types.IdentifierEntryedListGRRule;

import java.util.ArrayList;

public class CommandsInit {
	public static void init(){
		CommandRegistrationCallback.EVENT.register(
			  (dispatcher, registryAccess, environment) -> {
					dispatcher.register(
						  CommandManager.literal("block_effect").then(
								CommandManager.literal("get").executes(
									  (context) ->{
											    ServerCommandSource source = context.getSource();
												IdentifierEntryedListGRRule<StatusEffect> rule=source.getServer().getGameRules().get(GamerulesInit.BlockDisabledEffectsGR);
												source.sendFeedback(() -> Text.literal("disabled: ").append(rule.serialize().replace(",", ",  ")), false);
											    return rule.getCommandResult();
									  }
								)
						  ).then(
							    CommandManager.literal("set").requires((source) -> source.hasPermissionLevel(2)).then(
									  CommandManager.literal("disable").then(
										    CommandManager.argument("effect", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.STATUS_EFFECT)).executes(
												  (context) ->{
													  ServerCommandSource source = context.getSource();
													  RegistryEntry<StatusEffect> entry=RegistryEntryReferenceArgumentType.getRegistryEntry(context, "effect", RegistryKeys.STATUS_EFFECT);
													  IdentifierEntryedListGRRule<StatusEffect> rule=source.getServer().getGameRules().get(GamerulesInit.BlockDisabledEffectsGR);
													  ArrayList<RegistryEntry<StatusEffect>> list=rule.getValue();
													  if (list.contains(entry)){
														  list.remove(entry);
														  rule.changed((context.getSource()).getServer());
														  source.sendFeedback(() -> Text.literal(entry.getKey().get().getValue().toString()).append(" was disabled to list"), true);
														  return rule.getCommandResult();
													  }
													  return 0;
												  }
										    )
									  )
							    ).then(
								      CommandManager.literal("enable").then(
										    CommandManager.argument("effect", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.STATUS_EFFECT)).executes(
											      (context) ->{
												      ServerCommandSource source = context.getSource();
												      RegistryEntry<StatusEffect> entry=RegistryEntryReferenceArgumentType.getRegistryEntry(context, "effect", RegistryKeys.STATUS_EFFECT);
												      IdentifierEntryedListGRRule<StatusEffect> rule=source.getServer().getGameRules().get(GamerulesInit.BlockDisabledEffectsGR);
												      ArrayList<RegistryEntry<StatusEffect>> list=rule.getValue();
												      if (!list.contains(entry)){
													      list.add(entry);
													      rule.changed((context.getSource()).getServer());
													      source.sendFeedback(() -> Text.literal(entry.getKey().get().getValue().toString()).append(" was enabled to list"), true);
													      return rule.getCommandResult();
												      }
												      return 0;
											      }
										    )
								      )
							    ).then(
								      CommandManager.literal("clear").executes(
											(context) ->{
												ServerCommandSource source = context.getSource();
												IdentifierEntryedListGRRule<StatusEffect> rule=source.getServer().getGameRules().get(GamerulesInit.BlockDisabledEffectsGR);
												ArrayList<RegistryEntry<StatusEffect>> list=rule.getValue();
												if (!list.isEmpty()){
													rule.setValue(new ArrayList<>());
													rule.changed((context.getSource()).getServer());
													source.sendFeedback(() -> Text.literal("list was cleared"), true);
													return rule.getCommandResult();
												}
												return 0;
											}
								      )
							    )
						  )
					);
			  }
		);
	}
}
