package fle.core;

/**
 * The FLE version class.
 * @author ueyudiud
 * @see fle.core.FLE
 */
public class FLEVersion
{
	/**
	 * The main version of modification.<br>
	 * Related with Minecraft version and rebuild count.<br>
	 * Each time the number changed will cause a big change,
	 * please NOT use old API please.
	 */
	public static final int MAJOR_VERSION = 3;
	/**
	 * The minor version of modification.<br>
	 * Related with new optional element added.<br>
	 * Such as API changed, new child modification added.
	 * If minor version is 0, means modification is in alpha
	 * version, the API will change frequently.
	 */
	public static final int MINOR_VERSION = 0;
	/**
	 * The sub version of modification.<br>
	 * Update when fixed bug each time.
	 */
	public static final int SUB_VERSION = 4;
	/**
	 * The snapshot version.<br>
	 * The snapshot version is between two main version.
	 * Which marked this version is beta version,
	 * there is still lots of things need to do and bugs
	 * to fix.
	 */
	public static final int SNAPSHOT_VERSION = 5;
	
	/**
	 * Get if this version is SNAPSHOT VERSION.
	 * @return
	 */
	public static boolean isSnapshotVersion()
	{
		return SNAPSHOT_VERSION > 0;
	}
}
