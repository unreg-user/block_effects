package wta.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wta.particles.particles.UncubicParticle;

import static wta.Block_effects.MODID;

public class ParticlesInit {
	public static SimpleParticleType uncubicPT;
	public static void init(){
		uncubicPT=Registry.register(
			  Registries.PARTICLE_TYPE,
			  Identifier.of(MODID, "uncubic"),
			  FabricParticleTypes.simple(true)
		);
		ParticleFactoryRegistry.getInstance().register(uncubicPT, UncubicParticle.Factory::new);
	}
}
