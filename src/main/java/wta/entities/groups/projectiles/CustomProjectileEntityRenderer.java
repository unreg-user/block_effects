package wta.entities.groups.projectiles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public abstract class CustomProjectileEntityRenderer<T extends Entity, M extends EntityModel<T>> extends EntityRenderer<T> {
    protected M root;

    public CustomProjectileEntityRenderer(EntityRendererFactory.Context context, M model, float f) {
        super(context);
        root=model;
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float offsetY=offsetY();
        matrices.push();
        matrices.translate(0.0D, offsetY, 0.0D);
        float animationProgress = entity.age + tickDelta;
        VertexConsumer vertexConsumer=vertexConsumers.getBuffer(RenderLayer.getEntityCutout(getTexture(entity)));
        root.setAngles(
                entity,
                0,
                0,
                animationProgress,
                entity.getHeadYaw(),
                entity.getPitch()
        );
        root.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    protected float offsetY(){
        return 0;
    }
}
