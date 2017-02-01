package farcore.util.runnable;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Deprecated
public class Explode implements Runnable
{
	public static enum ExplosionType
	{
		STANDARD,
		FLAMING,
		NUKE;
	}
	
	/** whether or not the explosion sets fire to blocks around it */
	private final ExplosionType type;
	/** whether or not this explosion spawns smoke particles */
	private final boolean isSmoking;
	private final Random explosionRNG;
	private final World world;
	private final double explosionX;
	private final double explosionY;
	private final double explosionZ;
	private final Entity exploder;
	private final float explosionSize;
	private final List<BlockPos> affectedBlockPositions;
	private final Map<EntityPlayer, Vec3d> playerKnockbackMap;
	private final Vec3d position;

	@SideOnly(Side.CLIENT)
	public Explode(World worldIn, Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions)
	{
		this(worldIn, entityIn, x, y, z, size, ExplosionType.STANDARD, true, affectedPositions);
	}

	@SideOnly(Side.CLIENT)
	public Explode(World worldIn, Entity entityIn, double x, double y, double z, float size, ExplosionType type, boolean smoking, List<BlockPos> affectedPositions)
	{
		this(worldIn, entityIn, x, y, z, size, type, smoking);
		affectedBlockPositions.addAll(affectedPositions);
	}

	public Explode(World worldIn, Entity entityIn, double x, double y, double z, float size, ExplosionType type, boolean smoking)
	{
		explosionRNG = new Random();
		affectedBlockPositions = Lists.<BlockPos>newArrayList();
		playerKnockbackMap = Maps.<EntityPlayer, Vec3d>newHashMap();
		world = worldIn;
		exploder = entityIn;
		explosionSize = size;
		explosionX = x;
		explosionY = y;
		explosionZ = z;
		this.type = type;
		isSmoking = smoking;
		position = new Vec3d(explosionX, explosionY, explosionZ);
	}
	
	@Override
	public void run()
	{
	}
}