package birsy.clinker.common.world.level.gen.content.feature;

import birsy.clinker.core.registry.ClinkerBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class CalamineEggFeature extends Feature<NoneFeatureConfiguration> {
    public CalamineEggFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();

        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(level.getSeed()));
        NormalNoise noise = NormalNoise.create(worldgenrandom, -4, new double[]{(double)1.0F});

        float scale = 1f;

        int xOffset = (int) (Mth.map(random.nextDouble(), 0, 1, -3, 3));
        int yOffset = 7;
        int zOffset = (int) (Mth.map(random.nextDouble(), 0, 1, -3, 3));
        BlockPos offsetOrigin = origin.offset(xOffset, yOffset, zOffset);

        int noiseIntensity = 4;
        int radius = (int) (6 * scale);
        int generationRadius = radius + noiseIntensity;

        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-generationRadius, -generationRadius, -generationRadius), origin.offset(generationRadius, generationRadius, generationRadius))) {

            double distanceToCenter = pos.distToCenterSqr(origin.getCenter());
            distanceToCenter = Math.sqrt(distanceToCenter);
            distanceToCenter += noise.getValue(pos.getX(),pos.getY(),pos.getZ()) * noiseIntensity;

            double distanceToCracker = pos.distToCenterSqr(offsetOrigin.getCenter());
            distanceToCracker = Math.sqrt(distanceToCracker);
            distanceToCracker += noise.getValue(pos.getX(),pos.getY(),pos.getZ()) * noiseIntensity;

            if (distanceToCenter <= radius && distanceToCracker >= yOffset - 3 + scale * 4) {
                if (distanceToCenter >= radius - 2.5) {
                    level.setBlock(pos, ClinkerBlocks.CALAMINE.get().defaultBlockState(), 3);
                }
            }
        }

        return true;
    }
}
