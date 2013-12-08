package glenn.gasesframework;

/**
 * An enumerator to set gas combustion properties. Gases are not combustible ({@link Combustibility#NONE}) by default.
 * @author Glenn
 *
 */
public enum Combustibility
{
	/**
	 * Not combustible. Burn rate: 0
	 */
	NONE(0, -1, 0.0F),
	/**
	 * Lightly flammable, but only in controlled environments. Burn rate: 1
	 */
	CONTROLLABLE(1, -1, 0.0F),
	/**
	 * Flammable in controlled environments and burns in gas form. Burn rate: 2
	 */
	FLAMMABLE(2, 8, 0.0F),
	/**
	 * Flammable in controlled environments and burns quickly in gas form. Burn rate: 3
	 */
	HIGHLY_FLAMMABLE(3, 4, 0.0F),
	/**
	 * Flammable in controlled environments and explodes in gas form. Burn rate: 4
	 */
	EXPLOSIVE(4, -1, 0.8F),
	/**
	 * Flammable in controlled environments and explodes violently in gas form. Burn rate: 5
	 */
	HIGHLY_EXPLOSIVE(5, -1, 1.2F);
	
	/**
	 * The efficiency of the burning process in controlled environments of gases using this combustibility.
	 */
	public final int burnRate;
	/**
	 * How quickly a gas with this combustibility will burn in the world.
	 */
	public final int fireSpreadRate;
	/**
	 * How powerful the explosion of gases using this combustibility will be. Overall explosion power can be adjusted in the Gases Framework config.
	 */
	public final float explosionPower;
	/**
	 * The lantern block used for bottles of gas with this combustibility. Each combustibility needs an unique lantern block for technical reasons.
	 */
	public BlockLantern lanternBlock;
	
	Combustibility(int burnRate, int fireSpreadRate, float explosionPower)
	{
		this.burnRate = burnRate;
		this.fireSpreadRate = fireSpreadRate;
		this.explosionPower = explosionPower;
	}
}
