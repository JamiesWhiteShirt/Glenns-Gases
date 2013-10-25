package glenn.gases;

/**
 * An enumerator to simplify the reaction between a gas and blocks registered with {@link Gases#registerIgnitionBlock(int)}.
 * @author Glenn
 *
 */
public enum Combustibility
{
	NONE(0, -1, 0.0F), CONTROLLABLE(1, -1, 0.0F), FLAMMABLE(2, 8, 0.0F), HIGHLY_FLAMMABLE(3, 4, 0.0F), EXPLOSIVE(4, -1, 0.8F), HIGHLY_EXPLOSIVE(5, -1, 1.2F);
	
	public final int burnRate;
	public final int fireSpreadRate;
	public final float explosionPower;
	
	Combustibility(int burnRate, int fireSpreadRate, float explosionPower)
	{
		this.burnRate = burnRate;
		this.fireSpreadRate = fireSpreadRate;
		this.explosionPower = explosionPower;
	}
}
