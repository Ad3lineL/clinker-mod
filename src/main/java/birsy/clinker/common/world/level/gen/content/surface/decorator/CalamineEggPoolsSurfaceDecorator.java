package birsy.clinker.common.world.level.gen.content.surface.decorator;

import birsy.clinker.common.world.block.FallingLayerBlock;
import birsy.clinker.common.world.level.gen.system.noise.NoiseContext;
import birsy.clinker.common.world.level.gen.system.noise.NoiseFieldCache;
import birsy.clinker.common.world.level.gen.system.surface.decorator.SurfaceDecorator;
import birsy.clinker.core.registry.ClinkerBlocks;
import birsy.clinker.core.registry.worldgen.ClinkerNoiseComputers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import static birsy.clinker.core.registry.worldgen.ClinkerNoiseComputers.BASE_NOISE_2D;
import static birsy.clinker.core.registry.worldgen.ClinkerNoiseComputers.BASE_NOISE_2D_ALT;

public class CalamineEggPoolsSurfaceDecorator extends SurfaceDecorator {
    @Override
    public void prefillNoiseFields(NoiseFieldCache cache) {
        cache.fillNoiseField(BASE_NOISE_2D[3]);
        cache.fillNoiseField(BASE_NOISE_2D[5]);
        cache.fillNoiseField(BASE_NOISE_2D_ALT[5]);
        cache.fillNoiseField(BASE_NOISE_2D[6]);
        cache.fillNoiseField(BASE_NOISE_2D[7]);
        cache.fillNoiseField(BASE_NOISE_2D_ALT[7]);
        cache.fillNoiseField(BASE_NOISE_2D[8]);
    }

    @Override
    public void decorateSurface(ChunkAccess chunk, BlockPos.MutableBlockPos pos, int seaLevel, boolean canSeeSun, int depth, int maxElevationIncrease, int maxElevationDecrease, int surfaceHeight, double surfaceHeightGradient, NoiseContext context, RandomSource random) {
        int x = pos.getX(), z = pos.getZ();
        double wiggleNoise = context.retrieve(BASE_NOISE_2D[3], x, 0, z);

        double cliff0Fac = context.retrieve(BASE_NOISE_2D[6], x, 0, z);
        double cliff0 = Math.pow(Math.abs(cliff0Fac), 1 / 12.0) * Math.signum(cliff0Fac) * 8;

        double cliff1Fac = context.retrieve(BASE_NOISE_2D_ALT[7], x, 0, z) - 0.5;
        double cliff1 = Math.pow(Math.abs(cliff1Fac), 1 / 24.0) * Math.signum(cliff1Fac) * 30;
        double cliff1Mask = context.retrieve(BASE_NOISE_2D[8], x, 0, z) * 0.5 + 0.5;
        cliff1 *= cliff1Mask;

        double surfaceFac = context.retrieve(BASE_NOISE_2D[7], x, 0, z);
        double surface = surfaceFac * 8;


        boolean isBorder = Math.max(maxElevationDecrease, maxElevationDecrease) >= 1;

        if (isBorder) {
            chunk.setBlockState(pos, ClinkerBlocks.CALAMINE.get().defaultBlockState(), false);
        } else {
            chunk.setBlockState(pos, ClinkerBlocks.BRIMSTONE.get().defaultBlockState(), false);
        }
    }

    public boolean shouldCalculateElevationChange(boolean canSeeSun, int y, int surfaceHeight) {
        return y >= surfaceHeight - 20;
    }
}
