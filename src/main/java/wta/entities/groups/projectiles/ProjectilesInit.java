package wta.entities.groups.projectiles;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wta.entities.EntityLayers;
import wta.entities.groups.projectiles.uncubicExp.UncubicExp;
import wta.entities.groups.projectiles.uncubicExp.UncubicExpModel;
import wta.entities.groups.projectiles.uncubicExp.UncubicExpRenderer;

import static wta.Block_effects.MODID;

public class ProjectilesInit {
    public static EntityType<UncubicExp> uncubicExpE;

    public static void init(){
        uncubicExpE =Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(MODID, "uncubic_exp"),
                EntityType.Builder
                        .create(UncubicExp::registryCrate, SpawnGroup.MISC)
                        .dimensions(0.5f, 0.5f)
                        .build("uncubic_exp")
        );
    }

    public static void initClient(){
        EntityModelLayerRegistry.registerModelLayer(EntityLayers.uncubicExpLayer, UncubicExpModel::getTexturedModelData);
        EntityRendererRegistry.register(uncubicExpE, context -> new UncubicExpRenderer(context, new UncubicExpModel(context.getPart(EntityLayers.uncubicExpLayer)), 0f));
    }
}
