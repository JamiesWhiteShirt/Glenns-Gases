package glenn.gasesframework.util;

public class PipeBranch
{
	private static final int[] xDirection = new int[]{
		0, 0, 1, -1, 0, 0
	};
	private static final int[] yDirection = new int[]{
		-1, 1, 0, 0, 0, 0
	};
	private static final int[] zDirection = new int[]{
		0, 0, 0, 0, 1, -1
	};
	
	public byte x;
	public byte y;
	public byte z;
	private byte[] direction;
	public byte length;
	
	public PipeBranch(byte x, byte y, byte z, byte direction)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = new byte[16];
		this.direction[0] = direction;
		this.length = 1;
	}
	
	private PipeBranch(byte x, byte y, byte z, byte[] direction, byte length)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
		this.length = length;
	}
	
	public PipeBranch(int x, int y, int z, int direction)
	{
		this((byte)x, (byte)y, (byte)z, (byte)direction);
	}
	
	public void addDirection(byte direction)
	{
		this.direction[length++] = direction;
	}
	
	public void removeDirection()
	{
		if(length-- < 16)
		{
			this.direction[length + 1] = 0;
		}
	}
	
	public PipeBranch branch(int direction)
	{
		return branch((byte)direction);
	}
	
	public PipeBranch branch(byte direction)
	{
		PipeBranch pipeBranch = (PipeBranch)this.clone();
		pipeBranch.x += xDirection[direction];
		pipeBranch.y += yDirection[direction];
		pipeBranch.z += zDirection[direction];
		pipeBranch.addDirection(direction);
		
		return pipeBranch;
	}
	
	public PipeBranch unBranch()
	{
		PipeBranch pipeBranch = (PipeBranch)this.clone();
		byte direction = getDirection();
		pipeBranch.x -= xDirection[direction];
		pipeBranch.y -= yDirection[direction];
		pipeBranch.z -= zDirection[direction];
		pipeBranch.removeDirection();
		
		return pipeBranch;
	}
	
	public PipeBranch clone()
	{
		PipeBranch pipeBranch = new PipeBranch(x, y, z, direction.clone(), length);
		return pipeBranch;
	}
	
	public byte getDirection()
	{
		return direction[length - 1];
	}
    
    public int getReverseDirection()
    {
    	byte d = getDirection();
    	if(d >= 0 & d < 6)
    	{
    		return (d / 2) * 2 + 1 - d % 2;
    	}
    	return -1;
    }
    
    public boolean holds(int x, int y, int z)
    {
    	int i = length;
    	int x1 = this.x;
    	int y1 = this.y;
    	int z1 = this.z;
    	int direction;
    	do
    	{
    		if(x == x1 && y == y1 && z == z1) return true;
    		direction = this.direction[i - 1];
    		x1 -= xDirection[direction];
    		y1 -= yDirection[direction];
    		z1 -= zDirection[direction];
    	}
    	while(--i > 0);
    	
    	return false;
    }
}
