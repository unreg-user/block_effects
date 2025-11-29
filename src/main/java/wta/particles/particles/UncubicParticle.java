package wta.particles.particles;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

public class UncubicParticle extends Particle {
	protected UncubicParticle(ClientWorld world, double x, double y, double z) {
		super(world, x, y, z);
		this.maxAge = 60;
		this.setBoundingBoxSpacing(0.01f, 0.01f); // почти нет коллизии
		this.collidesWithWorld = false;
		this.setColor(0.0f, 0.0f, 1.0f); // синий
		this.alpha = 0.8f;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		// Плавная позиция между предыдущим и текущим кадром
		float x = (float) MathHelper.lerp(tickDelta, this.prevPosX, this.x);
		float y = (float) MathHelper.lerp(tickDelta, this.prevPosY, this.y);
		float z = (float) MathHelper.lerp(tickDelta, this.prevPosZ, this.z);

		float size = 0.25f; // половина ширины куба

		int light = this.getBrightness(tickDelta); // освещение как у блоков
		int r = (int)(this.red * 255);
		int g = (int)(this.green * 255);
		int b = (int)(this.blue * 255);
		int a = (int)(this.alpha * 255);

		// сам uncubic

		// Задняя грань (z - size)
		quad(vertexConsumer, x - size, y - size, z - size,
			  x + size, y - size, z - size,
			  x + size, y + size, z - size,
			  x - size, y + size, z - size,
			  r, g, b, a, light);

		// Передняя грань (z + size)
		quad(vertexConsumer, x + size, y - size, z + size,
			  x - size, y - size, z + size,
			  x - size, y + size, z + size,
			  x + size, y + size, z + size,
			  r, g, b, a, light);

		// Левая грань (x - size)
		quad(vertexConsumer, x - size, y - size, z + size,
			  x - size, y - size, z - size,
			  x - size, y + size, z - size,
			  x - size, y + size, z + size,
			  r, g, b, a, light);

		// Правая грань (x + size)
		quad(vertexConsumer, x + size, y - size, z - size,
			  x + size, y - size, z + size,
			  x + size, y + size, z + size,
			  x + size, y + size, z - size,
			  r, g, b, a, light);

		// Низ (y - size)
		quad(vertexConsumer, x - size, y - size, z + size,
			  x + size, y - size, z + size,
			  x + size, y - size, z - size,
			  x - size, y - size, z - size,
			  r, g, b, a, light);

		// Верх (y + size)
		quad(vertexConsumer, x - size, y + size, z - size,
			  x + size, y + size, z - size,
			  x + size, y + size, z + size,
			  x - size, y + size, z + size,
			  r, g, b, a, light);
	}

	// Вспомогательный метод: добавить один квадрат
	private void quad(VertexConsumer consumer,
	                  float x0, float y0, float z0,
	                  float x1, float y1, float z1,
	                  float x2, float y2, float z2,
	                  float x3, float y3, float z3,
	                  int r, int g, int b, int a, int light) {
		consumer.vertex(x0, y0, z0).color(r, g, b, a).light(light);
		consumer.vertex(x1, y1, z1).color(r, g, b, a).light(light);
		consumer.vertex(x2, y2, z2).color(r, g, b, a).light(light);
		consumer.vertex(x3, y3, z3).color(r, g, b, a).light(light);
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Factory implements ParticleFactory<SimpleParticleType> {
		public Factory(FabricSpriteProvider provider){

		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new UncubicParticle(world, x, y, z);
		}
	}
}