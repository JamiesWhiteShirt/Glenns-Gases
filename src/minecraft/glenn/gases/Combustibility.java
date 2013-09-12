package glenn.gases;

/**
 * An enumerator to simplify the reaction between a gas and blocks registered with {@link Gases#registerIgnitionBlock(int blockID)}.
 * @author Glenn
 *
 */
public enum Combustibility
{
	NONE(false, false), FLAMMABLE(true, false), EXPLOSIVE(false, true);
	
	public final boolean catchesFire;
	public final boolean explodes;
	
	Combustibility(boolean catchesFire, boolean explodes)
	{
		this.catchesFire = catchesFire;
		this.explodes = explodes;
	}
}
