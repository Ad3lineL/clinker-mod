package birsy.clinker.common.world.level.gen.content.surface.decorator;

import birsy.clinker.common.world.block.FallingLayerBlock;
import birsy.clinker.common.world.level.gen.system.noise.NoiseContext;
import birsy.clinker.common.world.level.gen.system.noise.NoiseFieldCache;
import birsy.clinker.common.world.level.gen.system.surface.decorator.SurfaceDecorationContext;
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

    public CalamineEggPoolsSurfaceDecorator() {}

    @Override
    public void prefillNoiseFields(NoiseFieldCache cache) {
        cache.fillNoiseField(BASE_NOISE_2D[3]);
        cache.fillNoiseField(BASE_NOISE_2D[4]);
        cache.fillNoiseField(BASE_NOISE_2D_ALT[4]);
    }

    @Override

    public void decorateSurface(BlockPos.MutableBlockPos pos, int seaLevel, ChunkAccess chunk, NoiseContext noiseContext, RandomSource random, SurfaceDecorationContext surfaceContext) {
        int x = pos.getX(), z = pos.getZ();
        double wiggleNoise = (noiseContext.retrieve(BASE_NOISE_2D[4], x, 0, z) + 0.8) * (noiseContext.retrieve(BASE_NOISE_2D[3], x, 0, z) + 0.1 );
        double wobbleNoise = noiseContext.retrieve(BASE_NOISE_2D_ALT[4], x, 0, z);

        BlockState fluidType = Blocks.WATER.defaultBlockState();

        BlockState rockType = ClinkerBlocks.CALAMINE.get().defaultBlockState();
        if (wobbleNoise - 0.4 + random.triangle(0, 0.35) > 0) {
            rockType = ClinkerBlocks.BRIMSTONE.get().defaultBlockState();
        }

        BlockState sedimentType = fluidType;
        if (wiggleNoise - 0.5 + random.triangle(0, 0.25) > 0) {
            sedimentType = ClinkerBlocks.ASHEN_REGOLITH.get().defaultBlockState();
        } else if (wiggleNoise - 0.3 + random.triangle(0, 0.25) > 0) {
            sedimentType = rockType;
        }

        int maxElevationDecrease = surfaceContext.maxElevationDecrease(),
                maxElevationIncrease = surfaceContext.maxElevationIncrease();

        boolean isBorder = Math.max(maxElevationDecrease, maxElevationDecrease) >= 1;
        boolean isLedge = Math.max(maxElevationIncrease, maxElevationIncrease) >= 1;

        if (isBorder) {
            chunk.setBlockState(pos, rockType, false);
        } else if (isLedge) {
            chunk.setBlockState(pos, fluidType, false);
            if (wiggleNoise + 0.1 + random.triangle(0, 0.25) > 0) {
                chunk.setBlockState(pos, rockType, false);
            }

        } else {
            chunk.setBlockState(pos, sedimentType, false);
        }
    }

    public boolean shouldCalculateElevationChange(boolean visibleToSky, int y, double surfaceHeight) {
        return y >= surfaceHeight - 20;
    }
}
