package fle.core.util.noise;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import flapi.util.FleLog;
import flapi.util.io.JsonInput;
import fle.core.util.FLEMath;

public class VecNoiseHandler
{	
	/**
	 * Debug mode.
	 * @param args
	 */
	public static void main(String[] args)
	{
		long l = new Random().nextLong();
		System.out.print(l);
		//FLESurfaceChunkProvider.drawImage(128, "Debug Chunk");
		drawImage(512, "Debug", 
				new NoiseMix(l, 
						new NoiseFuzzy(1L, 4, 1.6D, 3.2D, 0.4F), 
						new NoiseSmooth(
								new NoiseGauss(1L, 3, 
										new NoiseSmooth(18F, 
												new NoisePerlin(1L))))), 
				new VecNoisePerlin(3759180L, 3, 1.0F));
		drawImage(512, "Debug1", 
				new NoiseMontain(l, 7, 2, .3F),
				new VecNoisePerlin(35917541L, 2, 4.0F));
	}
	
	final static double basic;
	
	static
	{
		double a = 0;
		for(int j0 = -2; j0 <= 2; ++j0)
			for(int j1 = -2; j1 <= 2; ++j1)
				a += FLEMath.alpha(2D, FLEMath.distance(j0, j1));
		basic = a;
	}

	public static NoiseBase loadNoiseFromJson(JsonInput stream) throws IOException
	{
		return loadNoiseFromJson(true, stream);
	}
	
	private static NoiseBase loadNoiseFromJson(boolean isFirstLoad, JsonInput stream) throws IOException
	{
		try
		{
			if(!stream.contain("Type"))
			{
				throw new IOException();
			}
			String name = stream.getString("Type", null);
			if("perlin".equals(name))
			{
				return new NoisePerlin(0L);
			}
			else if("cell".equals(name))
			{
				return new NoiseCell(0L);
			}
			else if("fuzzy".equals(name))
			{
				int octive = stream.getInteger("Octive", 1);
				double start = stream.getDouble("StartLevel", 4.0);
				double increase = stream.getDouble("IncreaseLevel", 2.0F);
				float weakness = (float) stream.getDouble("Weakness", .5F);
				return new NoiseFuzzy(0L, octive, start, increase, weakness);
			}
			else if("gauss".equals(name))
			{
				int size = stream.getInteger("Size", 2);
				return new NoiseGauss(0L, size, loadNoiseFromJson(false, stream.sub("SubNoise")));
			}
			else if("mix".equals(name))
			{
				return new NoiseMix(0L, loadNoiseFromJson(false, stream.sub("SubNoise1")), loadNoiseFromJson(false, stream.sub("SubNoise2")));
			}
			else if("montain".equals(name))
			{
				int size = stream.getInteger("Size", 1);
				int gen = stream.getInteger("GenChance", 3);
				double level = stream.getDouble("Level", 1);
				return new NoiseMontain(0L, size, gen, level);
			}
			else if("smooth".equals(name))
			{
				int type = stream.getInteger("Type", 0);
				double size = stream.getDouble("Size", 4.0F);
				return new NoiseSmooth(type, size, loadNoiseFromJson(false, stream.sub("SubNoise")));
			}
		}
		catch(Throwable e)
		{
			if(!isFirstLoad) FleLog.addExceptionToCache(e);
			else FleLog.resetAndCatchException("FLE : Fail to load custom noise.");
			throw new IOException();
		}
		return null;
	}
	
	public static double[] getValue(int x, int z, NoiseBase height, VecNoiseBase noise, int size)
	{
		/**
		double[] a = a(x - 1, z - 1, height, noise, size + 2);
		double[] ret = new double[size * size];
		for(int i = 0; i < size; ++i)
			for(int j = 0; j < size; ++j)
			{
				double v = 0;
				for(int k = -1; k <= 1; ++k)
					for(int l = -1; l <= 1; ++l)
					{
						v += a[(i + k + 1) * (size + 2) + (j + l + 1)] * ((k | l) == 0x0 ? 0.75 : 0.03125);
					}
				ret[i * size + j] = v;
			}
		return ret;
		 */
		return a(x, z, height, noise, size);
	}
	
	private static double[] a(int x, int z, NoiseBase height, VecNoiseBase noise, int size)
	{
		Vec[] vecNoise = noise.noise(null, x - 3, z - 3, size + 6, size + 6);
		double[] heightNoise = height.noise(null, x - 3, z - 3, size + 6, size + 6);
		double[] ret = new double[size * size];
		for(int i0 = 0; i0 < size; ++i0)
			for(int i1 = 0; i1 < size; ++i1)
			{
				double a = 0;
				for(int j0 = -3; j0 <= 3; ++j0)
					for(int j1 = -3; j1 <= 3; ++j1)
					{
						Vec vec = vecNoise[i0 + j0 + 3 + (i1 + j1 + 3) * (size + 6)];
						double o = FLEMath.alpha(2D, FLEMath.distance(j0 + vec.xCoord, j1 + vec.zCoord));
						double b = heightNoise[i0 + j0 + 3 + (i1 + j1 + 3) * (size + 6)];
						//b = b * 0.8F + 0.2F;
						a += b * o;
					}
				a /= basic;
				a = a * 0.9D + 0.1D;
				if(a > 1.0D)
				{
					a = (a - 1D) / 2D + 1D;
				}
				else if(a < -1.0D)
				{
					a = (a + 1D) / 2D - 1D;
				}
				
				if(a > 1.0D) a = 1.0D;
				else if(a < -1.0D) a = -1.0D;
				ret[i0 + i1 * size] = a;
			}
		return ret;
	}
	
	public static void drawImage(int size, String name, NoiseBase height, VecNoiseBase noise)
	{
	    try
	    {
	    	File outFile = new File(name + ".png");
	    	if (outFile.exists())
	    	{
	    		return;
	    	}
	    	double[] ds = getValue(0, 0, height, noise, size);
	    	int[] ints = new int[size * size];
	    	for(int i = 0; i < ds.length; ++i)
	    		ints[i] = (int) (ds[i] * 64 + 128);
	    	BufferedImage outBitmap = new BufferedImage(size, size, 1);
	    	Graphics2D graphics = (Graphics2D)outBitmap.getGraphics();
	    	graphics.clearRect(0, 0, size, size);
	    	FleLog.getLogger().info(name + ".png");
	    	for (int x = 0; x < size; x++)
	    	{
	    		for (int z = 0; z < size; z++)
	    		{
	    			graphics.setColor(new Color(0x010101 * ints[size * z + x]));
	    			graphics.drawRect(x, z, 1, 1);
	    		}
	    	}
	    	ImageIO.write(outBitmap, "png", outFile);
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
}