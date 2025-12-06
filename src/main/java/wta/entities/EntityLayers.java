package wta.entities;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import static wta.Block_effects.MODID;

public class EntityLayers {
    public static EntityModelLayer uncubicExpLayer=createMain("uncubic_exp");

    private static EntityModelLayer createMain(Identifier id) {
        return new EntityModelLayer(id, "main");
    }
    private static EntityModelLayer createMain(String name) {
        return createMain(Identifier.of(MODID, name));
    }
}
