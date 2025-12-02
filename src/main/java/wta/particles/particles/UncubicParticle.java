package wta.particles.particles;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class UncubicParticle extends BillboardParticle {
    private final FabricSpriteProvider spriteProvider;
    private float size = 0.2f;

    // Конструктор — принимаем velocity!
    protected UncubicParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, FabricSpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.maxAge = 60;
        this.setBoundingBoxSpacing(size*2, size*2); // чуть больше для столкновений
        this.collidesWithWorld = true; // ← ВАЖНО!
        this.gravityStrength = 1.0F;   // ← как у BlockDustParticle
        this.alpha = 0.8f;
    }

    // Обновляем логику движения — как в Particle (но с учётом gravity и friction)
    @Override
    public void move(double dx, double dy, double dz) {
        // Вызываем родительский move — он обрабатывает столкновения
        super.move(dx, dy, dz);
        // После столкновения с блоком — затухание скорости (как в vanilla)
        if (this.onGround) {
            this.velocityX *= 0.7;
            this.velocityZ *= 0.7;
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (onGround) {
            super.buildGeometry(vertexConsumer, camera, tickDelta);
            return;
        }

        Vec3d vec3d = camera.getPos();
        float x = (float)(MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float y = (float)(MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float z = (float)(MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

        int light = this.getBrightness(tickDelta);
        int a = (int)(this.alpha * 255);

        Sprite sprite = this.spriteProvider.getSprite(this.random);

        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();

        int white = 255;

        // Передняя грань (z + size) — CCW при взгляде СПЕРЕДИ
        quad(vertexConsumer,
                x - size, y - size, z + size,
                x - size, y + size, z + size,
                x + size, y + size, z + size,
                x + size, y - size, z + size,
                minU, maxU, minV, maxV, white, white, white, a, light
        );

        // Задняя грань (z - size) — CCW при взгляде СЗАДИ
        quad(vertexConsumer,
                x + size, y - size, z - size,
                x + size, y + size, z - size,
                x - size, y + size, z - size,
                x - size, y - size, z - size,
                minU, maxU, minV, maxV, white, white, white, a, light
        );

        // Левая грань (x - size) — CCW при взгляде СЛЕВА
        quad(vertexConsumer,
                x - size, y - size, z - size,
                x - size, y + size, z - size,
                x - size, y + size, z + size,
                x - size, y - size, z + size,
                minU, maxU, minV, maxV, white, white, white, a, light
        );

        // Правая грань (x + size) — CCW при взгляде СПРАВА
        quad(vertexConsumer,
                x + size, y - size, z + size,
                x + size, y + size, z + size,
                x + size, y + size, z - size,
                x + size, y - size, z - size,
                minU, maxU, minV, maxV, white, white, white, a, light
        );

        // Верхняя грань (y + size) — CCW при взгляде СВЕРХУ
        quad(vertexConsumer,
                x - size, y - size, z - size,
                x - size, y - size, z + size,
                x + size, y - size, z + size,
                x + size, y - size, z - size,
                minU, maxU, minV, maxV, white, white, white, a, light
        );

        // Нижняя грань (y - size) — CCW при взгляде СНИЗУ
        quad(vertexConsumer,
                x - size, y + size, z + size,
                x - size, y + size, z - size,
                x + size, y + size, z - size,
                x + size, y + size, z + size,
                minU, maxU, minV, maxV, white, white, white, a, light
        );
    }

    @Override
    protected float getMinU() {
        Sprite sprite = this.spriteProvider.getSprite(this.random);
        return sprite.getMinU();
    }

    @Override
    protected float getMaxU() {
        Sprite sprite = this.spriteProvider.getSprite(this.random);
        return sprite.getMaxU();
    }

    @Override
    protected float getMinV() {
        Sprite sprite = this.spriteProvider.getSprite(this.random);
        return sprite.getMinV();
    }

    @Override
    protected float getMaxV() {
        Sprite sprite = this.spriteProvider.getSprite(this.random);
        return sprite.getMaxV();
    }

    private void quad(VertexConsumer c, float x0, float y0, float z0,  // нижний левый
                      float x1, float y1, float z1,                    // верхний левый
                      float x2, float y2, float z2,                    // верхний правый
                      float x3, float y3, float z3,                    // нижний правый
                      float minU, float maxU, float minV, float maxV,
                      int r, int g, int b, int a, int light) {
        c.vertex(x0, y0, z0).texture(minU, maxV).color(r, g, b, a).light(light);
        c.vertex(x1, y1, z1).texture(minU, minV).color(r, g, b, a).light(light);
        c.vertex(x2, y2, z2).texture(maxU, minV).color(r, g, b, a).light(light);
        c.vertex(x3, y3, z3).texture(maxU, maxV).color(r, g, b, a).light(light);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final FabricSpriteProvider provider;

        public Factory(FabricSpriteProvider provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new UncubicParticle(world, x, y, z, velocityX, velocityY, velocityZ, provider);
        }
    }
}