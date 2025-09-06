package wta.gamerule.types;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class IdentifierEntryedListGRRule<T> extends GameRules.Rule<IdentifierEntryedListGRRule<T>> {
	private ArrayList<RegistryEntry<T>> value;
	private ArrayList<T> tValue;
	private final Registry<T> registry;

	public IdentifierEntryedListGRRule(GameRules.Type<IdentifierEntryedListGRRule<T>> type, Registry<T> registry, ArrayList<RegistryEntry<T>> value) {
		super(type);
		this.registry=registry;
		this.value=value;
		deserializeT();
	}

	public IdentifierEntryedListGRRule(GameRules.Type<IdentifierEntryedListGRRule<T>> type, Registry<T> registry, ArrayList<RegistryEntry<T>> value, ArrayList<T> tValue) {
		super(type);
		this.registry=registry;
		this.value=value;
		this.tValue=tValue;
	}

	@Override
	protected void setFromArgument(CommandContext<ServerCommandSource> context, String name) {
		String ctx=StringArgumentType.getString(context, name);
		deserialize(ctx);
	}

	@Override
	protected void deserialize(String value) {
		this.value=new ArrayList<>(Set.copyOf(Arrays.stream(value.split(","))
			  .map(String::trim)
			  .map(Identifier::tryParse)
			  .filter(Objects::nonNull)
			  .map(id -> registry.getEntry(id).orElse(null))
			  .filter(Objects::nonNull)
			  .toList()));
		deserializeT();
	}

	public void deserializeT() {
		tValue=new ArrayList<>(value.stream()
			  .map(RegistryEntry::value)
			  .toList());
	}

	@Override
	public String serialize() {
		return value.stream()
			  .map(entry -> entry.getKey().orElse(null))
			  .filter(Objects::nonNull)
			  .map(key -> key.getValue().toString())
			  .collect(Collectors.joining(","));
	}

	@Override
	public int getCommandResult() {
		return this.value.size();
	}

	@Override
	protected IdentifierEntryedListGRRule<T> getThis() {
		return this;
	}

	@Override
	protected IdentifierEntryedListGRRule<T> copy() {
		return new IdentifierEntryedListGRRule<>(type, registry, new ArrayList<>(value));
	}

	@Override
	public void setValue(IdentifierEntryedListGRRule<T> rule, @Nullable MinecraftServer server) {
		this.value=rule.value;

		this.changed(server);
	}

	public ArrayList<RegistryEntry<T>> getValue(){
		return value;
	}

	public ArrayList<T> getTValue(){
		return tValue;
	}

	public void setValue(ArrayList<RegistryEntry<T>> value){
		this.value=value;
	}

	@Override
	public void changed(@Nullable MinecraftServer server) {
		super.changed(server);
	}
}
