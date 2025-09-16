package wta.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wta.blocks.blockEntities.EffectAmplifierBEClass;

import static wta.Block_effects.MODID;

public class BlocksInit {
	public static Block radiusEffectB;
	public static Item radiusEffectI;

	public static Block radiusEffectAmplifierB;
	public static Item radiusEffectAmplifierI;

	public static Block effectAmplifierB;
	public static Item effectAmplifierI;
	public static BlockEntityType<EffectAmplifierBEClass> effectAmplifierBE;

	public static void init(){
		radiusEffectB=Registry.register(
			  Registries.BLOCK,
			  Identifier.of(MODID, "radius_effect_block"),
			  new Block(
					AbstractBlock.Settings.create()
			  )
		);
		radiusEffectI=Registry.register(
			  Registries.ITEM,
			  Identifier.of(MODID, "radius_effect_block"),
			  new BlockItem(radiusEffectB, new Item.Settings())
		);

		radiusEffectAmplifierB=Registry.register(
			  Registries.BLOCK,
			  Identifier.of(MODID, "radius_effect_amplifier_block"),
			  new Block(
					AbstractBlock.Settings.create()
			  )
		);
		radiusEffectAmplifierI=Registry.register(
			  Registries.ITEM,
			  Identifier.of(MODID, "radius_effect_amplifier_block"),
			  new BlockItem(radiusEffectAmplifierB, new Item.Settings())
		);

		effectAmplifierB=Registry.register(
			  Registries.BLOCK,
			  Identifier.of(MODID, "effect_amplifier_block"),
			  new Block(
					AbstractBlock.Settings.create()
			  )
		);
		effectAmplifierI=Registry.register(
			  Registries.ITEM,
			  Identifier.of(MODID, "effect_amplifier_block"),
			  new BlockItem(effectAmplifierB, new Item.Settings())
		);
		effectAmplifierBE=Registry.register(
			  Registries.BLOCK_ENTITY_TYPE,
			  Identifier.of(MODID, "effect_amplifier_block"),
			  BlockEntityType.Builder.create(EffectAmplifierBEClass::new, effectAmplifierB).build()
		);
	}
}
