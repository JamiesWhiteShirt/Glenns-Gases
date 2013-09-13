package glenn.gases.util;

public class PipeBranch
{
	public byte x;
	public byte y;
	public byte z;
	public byte direction;
	public byte length;
	
	public PipeBranch(byte x, byte y, byte z, byte direction, byte length)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
		this.length = length;
	}
	
	public PipeBranch(int x, int y, int z, int direction, int length)
	{
		this((byte)x, (byte)y, (byte)z, (byte)direction, (byte)length);
	}
    
    public int reverseDirection()
    {
    	if(direction >= 0 & direction < 6)
    	{
    		return (direction / 2) * 2 + 1 - direction % 2;
    	}
    	return -1;
    }
}
