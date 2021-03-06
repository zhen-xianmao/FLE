package nebula.common.util;

import nebula.Log;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;

public enum Direction implements IStringSerializable
{
	/** --Y */
	D( 0, -1,  0,  0,     -1),
	/** --Y */
	U( 0,  1,  0,  0,     -1),
	/** --Z */
	N( 0,  0, -1,  0,      2),
	/** ++Z */
	S( 0,  0,  1,  0,      0),
	/** --X */
	W(-1,  0,  0,  0,      1),
	/** ++X */
	E( 1,  0,  0,  0,      3),
	/** --T */
	A( 0,  0,  0, -1,     -1),
	/** ++T */
	B( 0,  0,  0,  1,     -1),
	/** Unknown direction. */
	Q( 0,  0,  0,  0,     -1);
	
	/**
	 * @see nebula.common.util.Facing
	 */
	@Deprecated
	public static final Direction[][] MACHINE_ROTATION = {
			{D, U, N, S, W, E, A, B, Q},
			{D, U, S, N, E, W, A, B, Q},
			{D, U, S, N, E, W, A, B, Q},
			{D, U, N, S, W, E, A, B, Q},
			{D, U, W, E, S, N, A, B, Q},
			{D, U, E, W, N, S, A, B, Q},
			{D, U, N, S, W, E, A, B, Q},
			{U, D, S, N, E, W, A, B, Q},
			{D, U, N, S, W, E, A, B, Q}
	};
	
	public static final int[] OPPISITE = {1, 0, 3, 2, 5, 4, 7, 6, 8};
	private static final int[] POSITIVE = {1, 1, 3, 3, 5, 5, 7, 7, 8};
	public static final byte T_2D = 0x0;
	public static final byte T_3D = 0x1;
	public static final byte T_4D = 0x2;
	public static final byte T_2D_NONNULL = 0x4;
	public static final byte T_3D_NONNULL = 0x5;
	public static final byte T_4D_NONNULL = 0x6;
	public static final Direction[] DIRECTIONS_2D = {S, W, N, E};
	public static final Direction[] DIRECTIONS_3D = {D, U, N, S, W, E};
	public static final Direction[] DIRECTIONS_4D = {D, U, N, S, W, E, A, B};
	//4D rotation next(B) and last(A) is real number
	public static final Direction[][] ROTATION_4D = {
			{D, U, W, E, S, N, A, B},
			{D, U, E, W, N, S, A, B},
			{E, W, N, S, D, U, A, B},
			{W, E, S, N, U, D, A, B},
			{S, N, U, D, W, E, A, B},
			{N, S, D, U, W, E, A, B},
			{D, U, S, N, W, E, A, B},
			{U, D, N, S, E, W, A, B}};
	//3D rotation use left hand rule.
	public static final Direction[][] ROTATION_3D = {
			{D, U, W, E, S, N},
			{D, U, E, W, N, S},
			{E, W, N, S, D, U},
			{W, E, S, N, U, D},
			{S, N, U, D, W, E},
			{N, S, D, U, W, E}};
	
	private static final int[] CAST = {0, 1, 2, 3, 4, 5, 6, 6, 6};
	
	public static Direction of(EnumFacing direction)
	{
		return direction == null ? Q :
			values()[direction.ordinal()];
	}
	
	public static Direction of(ModelRotation rotation, EnumFacing facing)
	{
		return of(rotation.rotateFace(facing));
	}
	
	public static EnumFacing of(Direction direction)
	{
		return direction == null ? null :
			EnumFacing.VALUES[CAST[direction.ordinal()]];
	}
	
	public static Direction heading(EntityLivingBase entity)
	{
		return entity == null ? Q : values()[entity.getHorizontalFacing().ordinal()];
	}
	
	public final int x;
	public final int y;
	public final int z;
	public final int t;
	public final int boundX;
	public final int boundY;
	public final int boundZ;
	/** The direction flag for <tt>long</tt> state marker, this is normal directions flag. */
	public final int flag;
	/** The direction flag for <tt>long</tt> state marker, this is horizontal directions flag. */
	public final int flag1;
	public final char chr;
	public final boolean horizontal;
	public final int horizontalOrdinal;
	public final Axis axis;
	
	Direction(int x, int y, int z, int t, int h)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.t = t;
		this.boundX = x != 0 ? -1 : 1;
		this.boundY = y != 0 ? -1 : 1;
		this.boundZ = z != 0 ? -1 : 1;
		this.flag = 1 << ordinal();
		this.flag1 = h == -1 ? -1 : 1 << h;
		this.chr = name().toLowerCase().charAt(0);
		this.horizontal = (x | z) != 0;
		this.horizontalOrdinal = h;
		this.axis = x != 0 ? Axis.X : y != 0 ? Axis.Y : z != 0 ? Axis.Z : null;
	}
	
	public Direction getOpposite()
	{
		return this == Q ? Q : DIRECTIONS_3D[ordinal() ^ 1];
	}
	
	public Direction getPositive()
	{
		return values()[POSITIVE[ordinal()]];
	}
	
	public Direction getNegative()
	{
		return values()[OPPISITE[POSITIVE[ordinal()]]];
	}
	
	public boolean isPositive()
	{
		return this != Q && POSITIVE[ordinal()] == ordinal();
	}
	
	public boolean isNegative()
	{
		return this != Q && POSITIVE[ordinal()] == OPPISITE[ordinal()];
	}
	
	public Direction getRotation4D(Direction axis)
	{
		return ROTATION_4D[axis.ordinal()][ordinal()];
	}
	
	public Direction getRotation3D(Direction axis)
	{
		return ROTATION_3D[axis.ordinal()][ordinal()];
	}
	
	public Direction validDirection3D()
	{
		return this == A || this == B ? Q : this;
	}
	
	public Direction validDirection2D()
	{
		return this == A || this == B || this == U || this == D ? Q : this;
	}
	
	public BlockPos offset(BlockPos pos)
	{
		return pos.add(this.x, this.y, this.z);
	}
	
	public EnumFacing of()
	{
		return of(this);
	}
	
	@Override
	public String getName()
	{
		return Character.toString(this.chr);
	}
	
	public static Direction readFromNBT(NBTTagCompound nbt, String key, byte type)
	{
		return readFromNBT(nbt, key, type, (type & 0x4) != 0 ? N : Q);
	}
	
	public static Direction readFromNBT(NBTTagCompound nbt, String key, byte type, Direction def)
	{
		if(!nbt.hasKey(key)) return def;
		try
		{
			Direction dir = values()[nbt.getByte(key)];
			switch (type & 0x3)
			{
			case 0 :
				if(dir.y != 0)
				{
					Log.warn("The side %s is not valid for this object, the nbt might broken.", dir);
					return N;
				}
			case 1 :
				if(dir.t != 0)
				{
					Log.warn("The side %s is not valid for this object, the nbt might broken.", dir);
					return N;
				}
			case 2 :
			default:
				return dir;
			}
		}
		catch(Exception exception)
		{
			return def;
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt, String key, byte type)
	{
		boolean flag = (type & 0x4) != 0;
		Direction dir = this;
		if(flag)
		{
			if(dir == Q)
			{
				dir = N;
			}
		}
		else if(dir == Q) return;
		switch (type & 0x3)
		{
		case 0 :
			if(this.y != 0)
			{
				dir = N;
			}
		case 1 :
			if(this.t != 0)
			{
				dir = N;
			}
		case 2 :
		default:
			break;
		}
		nbt.setByte(key, (byte) ordinal());
	}
}