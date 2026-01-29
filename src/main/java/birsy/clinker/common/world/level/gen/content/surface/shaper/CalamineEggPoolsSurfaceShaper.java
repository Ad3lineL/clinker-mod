package birsy.clinker.common.world.level.gen.content.surface.shaper;

import birsy.clinker.common.world.level.gen.system.noise.NoiseContext;
import birsy.clinker.common.world.level.gen.system.noise.NoiseFieldCache;
import birsy.clinker.core.registry.worldgen.ClinkerNoiseComputers;
import birsy.clinker.core.util.MathUtils;
import net.minecraft.util.Mth;

import static birsy.clinker.core.registry.worldgen.ClinkerNoiseComputers.*;

public class CalamineEggPoolsSurfaceShaper extends SimpleSurfaceShaper {

    @Override
    public void prefillHeightmapNoiseFields(NoiseFieldCache cache) {
        cache.fillNoiseField(BASE_NOISE_2D_ALT[9]);
    }

    @Override
    public double getHeight(int x, int z, double weight, NoiseContext context) {
        return (140 + context.retrieve(BASE_NOISE_2D_ALT[9], x, 0, z) * 10) * weight;
    }

    @Override
    public void prefillDensityNoiseFields(NoiseFieldCache cache, int minSurfaceHeight, int maxSurfaceHeight) {
        cache.fillNoiseField(minSurfaceHeight, maxSurfaceHeight, BASE_NOISE_2D[6]);
        cache.fillNoiseField(minSurfaceHeight, maxSurfaceHeight, BASE_NOISE_2D[7]);
    }

    @Override
    public double surfaceDensity(int x, int y, int z, double heightmapHeight, double heightmapGradient, double distanceToSurface, double biomeWeight, NoiseContext context) {
        double baseNoise = context.retrieve(BASE_NOISE_2D[6], x, y, z);

        double cliff0Fac = context.retrieve(BASE_NOISE_2D[6], x, 0, z) - 0.20;
        double cliff0 = Math.pow(Math.abs(cliff0Fac), 0.3) * Math.signum(cliff0Fac);
        cliff0 = Mth.map(cliff0, -1, 1, 0, 1) * 12;

        double surface = context.retrieve(BASE_NOISE_2D[7], x, y, z);
        surface = Mth.map(surface, -1, 1, 0, 1) * 16;

        cliff0 = MathUtils.smoothMinExpo(cliff0 * biomeWeight, surface, 2);



        return (y - heightmapHeight) + baseNoise - cliff0;
    }
}
