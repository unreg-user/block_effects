package wta;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;

import static wta.Block_effects.MODID;

public class AllInit {
	public static ItemGroup modItems;
	private static ArrayList<Item> inMI=new ArrayList<>(); //in minecraft inventory

	public static void init(){
		modItems=Registry.register(Registries.ITEM_GROUP,
			  Identifier.of(MODID, "block_effect_blocks"),
			  FabricItemGroup.builder()
					.displayName(Text.translatable("itemGroup.blockEffect"))
					.icon(() -> new ItemStack(Blocks.BIRCH_FENCE))
					.entries((displayContext, entries) -> {
						for (Item item : inMI){
							entries.add(item);
						}
					}).build());
		inMI=null;
	}

	public void addToInMI(Collection<? extends Item> collection){
		inMI.addAll(collection);
	}
}
