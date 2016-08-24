package farcore.lib.util;

import java.util.Random;

public class NoisePerlin extends NoiseBase
{
	private double frequency;
	private double persistence;

	private NoiseBase[] octaves;
	
	public NoisePerlin(Random random, double persistence, NoiseBase...octaves)
	{
		super(random.nextLong());
		frequency = 1.0F;
		this.persistence = persistence;
		this.octaves = octaves;
	}
	
	public NoisePerlin(Random random, int size, double start, double frequency, double persistence)
	{
		super(random.nextLong());
		this.frequency = frequency;
		this.persistence = persistence;
		octaves = new NoiseCoherent[size];
		double t = start;
		for(int i = 0; i < size; octaves[i++] = new NoiseCoherent(seed + i, t),
				t *= frequency)
		{
			;
		}
	}
	
	public NoisePerlin(long seed, double persistence, NoiseBase...octaves)
	{
		super(seed);
		frequency = 1.0F;
		this.persistence = persistence;
		this.octaves = octaves;
	}

	/**
	 *
	 * @param seed
	 * @param size
	 * @param start
	 * @param frequency The noise changed speed increase.
	 * @param persistence
	 */
	public NoisePerlin(long seed, int size, double start, double frequency, double persistence)
	{
		super(seed);
		this.frequency = frequency;
		this.persistence = persistence;
		octaves = new NoiseCoherent[size];
		double t = start;
		for(int i = 0; i < size; octaves[i++] = new NoiseCoherent(seed + i, t),
				t *= frequency)
		{
			;
		}
	}

	@Override
	public NoiseBase setSeed(long seed)
	{
		for(int i = 0; i < octaves.length; octaves[i++].setSeed(seed + i))
		{
			;
		}
		return super.setSeed(seed);
	}
	
	@Override
	public double[] noise(double[] array, int u, int v, int w, double x, double y, double z, double xScale,
			double yScale, double zScale)
	{
		if(array == null || array.length < u * v * w)
		{
			array = new double[u * v * w];
		}
		double[] array1 = null;
		double a = persistence + 1D;
		for(int i = 0; i < octaves.length; ++i)
		{
			array1 = octaves[i].noise(array1, u, v, w, x, y, z, xScale, yScale, zScale);
			for(int p = 0; p < w; ++p)
			{
				for(int q = 0; q < v; ++q)
				{
					for(int r = 0; r < u; ++r)
					{
						int id = (p * v + q) * u + r;
						if(i == 0)
						{
							array[id] += array1[id];
						}
						else
						{
							array[id] += array1[id] * persistence;
							array[id] /= a;
						}
					}
				}
			}
		}
		return array;
	}
	
	@Override
	public double noise(double x, double y, double z)
	{
		double ret = 0;
		double f = 1D;
		double a = persistence + 1D;
		for(int i = 0; i < octaves.length; ++i)
		{
			if(i == 0)
			{
				ret += octaves[i].noise(x, y, z);
			}
			else
			{
				ret += octaves[i].noise(x, y, z) * persistence;
				ret /= a;
			}
		}
		return ret;
	}
}