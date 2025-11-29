package wta.fun;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;

public class FloatEffectInstance {
	public StatusEffect effect;
	public float duration;
	public float amplifier;

	public FloatEffectInstance(StatusEffect effect, float duration, float amplifier) {
		this.effect = effect;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	public StatusEffectInstance getStatusEffectInstance(RegistryEntry<StatusEffect> effectEntry){
		return new StatusEffectInstance(
			  effectEntry,
			  (int) duration,
			  (int) amplifier
		);
	}
}
