package birsy.clinker.common.world.level.gen.content.surface.shaper;

import birsy.clinker.common.world.level.gen.system.noise.NoiseContext;
import birsy.clinker.common.world.level.gen.system.noise.NoiseFieldCache;

import static birsy.clinker.core.registry.worldgen.ClinkerNoiseComputers.*;

public class CalamineEggPoolsSurfaceShaper extends SimpleSurfaceShaper {

    @Override
    public void prefillHeightmapNoiseFields(NoiseFieldCache cache) {
        cache.fillNoiseField(BASE_NOISE_2D_ALT[9]);
    }

    @Override
    public double getHeight(int x, int z, double weight, NoiseContext context) {
        return (115 + context.retrieve(BASE_NOISE_2D_ALT[9], x, 0, z) * 10) * weight;
    }

    @Override
    public void prefillDensityNoiseFields(NoiseFieldCache cache, int minSurfaceHeight, int maxSurfaceHeight) {
        cache.fillNoiseField(minSurfaceHeight, maxSurfaceHeight, BASE_NOISE_2D[6]);
    }

    @Override
    public double surfaceDensity(int x, int y, int z, double heightmapHeight, double heightmapGradient, double distanceToSurface, double biomeWeight, NoiseContext context) {
        double noiseValue = context.retrieve(BASE_NOISE_2D[6], x, y, z);
        return y - heightmapHeight - noiseValue * 10;
    }
}
