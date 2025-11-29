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
import wta.fun.mathHelp.MathH;

import java.util.ArrayList;
import java.util.Objects;

public class ContextualChooseEffS extends ChooseEffS {
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
	public StatusEffect getStatusEffect() {
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
							int sqDistance =  MathH.getSqDist(ix, iy, iz);
							if (sqDistance < difDis){
								difDis = sqDistance;
								difList.clear();
								difList.add(posI);
							}else if(sqDistance == difDis){
								difList.add(posI);
							}
						}
					} else if (block==BlocksInit.effectAmplifierB) {
						boostPoses.add(posI);
					}
				}
			}
		}

		ArrayList<StatusEffect> results12 = new ArrayList<>();
		boolean isChanced = false;
		if (radList.isEmpty() && difList.isEmpty()) {
			isChanced = true;
			if (!farDifList.isEmpty()){
				results12 = new ArrayList<>(farDifList.stream()
					  .map(this::getDifEffect)
					  .filter(Objects::nonNull)
					  .toList());
			}
		}else{
			if (radDis <= difDis) {
				results12.addAll(
					  radList.stream()
						    .map(pos -> new DefaultChooseEffS(world, difficulty, pos, isMonster).getStatusEffect())
							.toList()
				);
			}
			if (difDis <= radDis) {
				results12.addAll(
					  difList.stream()
						    .map(this::getDifEffect)
						    .filter(Objects::nonNull)
						    .toList()
				);
			}
		}

		if (results12.isEmpty()){
			return new DefaultChooseEffS(world, difficulty, pos, isMonster).getStatusEffect();
		} else if (isChanced) {
			if (world.random.nextInt(results12.size()+1)!=0){
				return results12.get(world.random.nextInt(results12.size()));
			}
			return new DefaultChooseEffS(world, difficulty, pos, isMonster).getStatusEffect();
		} else {
			return results12.get(world.random.nextInt(results12.size()));
		}
	}

	@Override
	public StatusEffectInstance getStatusEffectInstance() {
		RegistryEntry<StatusEffect> effectEntry=getEntryStatusEffect();
		StatusEffect effect=effectEntry.value();
		StatusEffectCategory category=effect.getCategory();

		int categoryAmplifier = getCategoryAmplifier(isMonster, difficulty, category);

		return new StatusEffectInstance(
			  effectEntry,
			  120,
			  categoryAmplifier-1
		);
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
