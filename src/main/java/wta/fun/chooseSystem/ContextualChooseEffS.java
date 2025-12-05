package wta.fun.chooseSystem;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import wta.blocks.BlocksInit;
import wta.blocks.blockEntities.EffectAmplifierBEClass;
import wta.fun.FloatEffectInstance;
import wta.fun.mathHelp.MathH;
import wta.gamerule.GamerulesInit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContextualChooseEffS extends ChooseEffS<ArrayList<StatusEffect>, ArrayList<StatusEffectInstance>, ArrayList<RegistryEntry<StatusEffect>>> {
	public static final int radiusEff=2;
	public static final int radiusDifCh=3;
	public static final int radiusAmp=4;

	private final BlockPos pos;
	private final ServerWorld world;

	private ArrayList<BlockPos> boostPoses=new ArrayList<>();

	public ContextualChooseEffS(ServerWorld world, Difficulty difficulty, BlockPos pos, boolean isMonster) {
		super(difficulty, isMonster);
		this.world=world;
		this.pos=pos;
	}

    @Override
    public ArrayList<RegistryEntry<StatusEffect>> getEntryStatusEffect() {
        return null;
    }

    @Override
	public ArrayList<StatusEffect> getStatusEffect() {
		ArrayList<BlockPos> radList=new ArrayList<>(); //1
		int radDis=Integer.MAX_VALUE;
		ArrayList<BlockPos> difList=new ArrayList<>(); //2
		int difDis=Integer.MAX_VALUE;
		ArrayList<BlockPos> farDifList=new ArrayList<>(); //3

		for (int ix = -radiusAmp; ix <= radiusAmp; ix++) {
			for (int iy = -radiusAmp; iy <= radiusAmp; iy++) {
				for (int iz = -radiusAmp; iz <= radiusAmp; iz++) {
					BlockPos posI=pos.add(ix, iy, iz);
					Block block=world.getBlockState(posI).getBlock();
					int maxAxisDis=Math.max(Math.max(Math.abs(ix), Math.abs(iy)), Math.abs(iz));
					
					if (block==BlocksInit.radiusEffectB){
						if (maxAxisDis > radiusEff) continue;
						int sqDistance = MathH.getSqDist(ix, iy, iz);
						if (sqDistance < radDis){
							radDis = sqDistance;
							radList.clear();
							radList.add(posI);
						}else if(sqDistance == radDis){
							radList.add(posI);
						}
					} else if (block==BlocksInit.diffuserB) {
						if (maxAxisDis > radiusDifCh){
							continue;
						}
						if (maxAxisDis > radiusEff){ //chance zone
							farDifList.add(posI);
						} else { //always zone
							difList.add(posI);
						}
					} else if (block==BlocksInit.effectAmplifierB) {
						boostPoses.add(posI);
					}
				}
			}
		}

		ArrayList<StatusEffect> results12;
		boolean isChanced = false;
        boolean isShouldChooseOne = false;
		if (radList.isEmpty()) {
            if (difList.isEmpty()){
                isChanced = true;
                if (!farDifList.isEmpty()){
                    results12 = new ArrayList<>(farDifList.stream()
                            .map(this::getDifEffect)
                            .filter(Objects::nonNull)
                            .toList());
                } else {
                    return new ArrayList<>(List.of(
                            new DefaultChooseEffS(world, difficulty, pos, isMonster).getStatusEffect()
                    ));
                }
            } else {
                results12 = new ArrayList<>(
                        difList.stream()
                                .map(this::getDifEffect)
                                .filter(Objects::nonNull)
                                .toList()
                );
            }
		} else {
            isShouldChooseOne = true;
            results12 = new ArrayList<>(
                    radList.stream()
                            .map(pos -> new DefaultChooseEffS(world, difficulty, pos, isMonster).getStatusEffect())
                            .toList()
            );
        }

		if (isShouldChooseOne) {
            return new ArrayList<>(List.of(
                    results12.get(world.random.nextInt(results12.size()))
            ));
        } else if (isChanced) {
            if (world.random.nextInt(results12.size()+1)!=0){
				return results12;
			}
			return new ArrayList<>(List.of(
                    new DefaultChooseEffS(world, difficulty, pos, isMonster).getStatusEffect()
            ));
		} else {
			return results12;
		}
	}

	@Override
	public ArrayList<StatusEffectInstance> getStatusEffectInstance() {
		ArrayList<RegistryEntry<StatusEffect>> effectEntries = getEntryStatusEffect();
        ArrayList<StatusEffectInstance> ret = new ArrayList<>();

        for (RegistryEntry<StatusEffect> entryI : effectEntries) {
            StatusEffect effectI = entryI.value();
            StatusEffectCategory category = effectI.getCategory();

            int categoryAmplifier = getCategoryAmplifier(isMonster, difficulty, category);

            FloatEffectInstance instanceF = getFloatInstanceDefaultRule(effectI, categoryAmplifier, world);

            for (BlockPos posI : boostPoses) {
                EffectAmplifierBEClass beI = (EffectAmplifierBEClass) world.getBlockEntity(posI);
                if (beI != null) beI.boost(instanceF, isMonster);
            }
            ret.add(instanceF.getStatusEffectInstance(entryI));
        }

		return ret;
	}

	private StatusEffect getDifEffect(BlockPos pos){
		ArrayList<BlockPos> radList1=getCommonDifEffect(pos);
		if (radList1.isEmpty()) return null;

		ArrayList<BlockPos> radList2=new ArrayList<>();
		int radDis2=Integer.MAX_VALUE;

		for (BlockPos posI : radList1) {
			int dx = posI.getX() - this.pos.getX();
			int dy = posI.getY() - this.pos.getY();
			int dz = posI.getZ() - this.pos.getZ();
			int sqDist = MathH.getSqDist(dx, dy, dz);

			if (sqDist < radDis2) {
				radDis2 = sqDist;
				radList2.clear();
				radList2.add(posI);
			} else if (sqDist == radDis2) {
				radList2.add(posI);
			}
		}

		return new DefaultChooseEffS(
			  world,
			  difficulty,
			  radList2.get(world.random.nextInt(radList2.size())),
			  isMonster
		).getStatusEffect();
	}

	private ArrayList<BlockPos> getCommonDifEffect(BlockPos pos){
		ArrayList<BlockPos> radList=new ArrayList<>();
		int radDis=Integer.MAX_VALUE;

		for (int ix = -radiusEff; ix <= radiusEff; ix++) {
			for (int iy = -radiusEff; iy <= radiusEff; iy++) {
				for (int iz = -radiusEff; iz <= radiusEff; iz++) {
					BlockPos posI=pos.add(ix, iy, iz);
					Block block=world.getBlockState(posI).getBlock();

					if (block==BlocksInit.radiusEffectB){
						int sqDistance = MathH.getSqDist(ix, iy, iz);
						if (sqDistance < radDis){
							radDis = sqDistance;
							radList.clear();
							radList.add(posI);
						}else if(sqDistance == radDis){
							radList.add(posI);
						}
					}
				}
			}
		}

		return radList;
	}
}
