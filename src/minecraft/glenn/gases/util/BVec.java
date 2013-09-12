package glenn.gases.util;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class BVec
{
	public byte x;
	public byte y;
	public byte z;
	
	public BVec()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public BVec(BVec vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public BVec(byte x, byte y, byte z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public BVec set(BVec vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		
		return this;
	}
	
	public BVec set(byte x, byte y, byte z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	public BVec add(BVec vec)
	{
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
		
		return this;
	}
	
	public BVec added(BVec vec)
	{
		return this.clone().add(vec);
	}
	
	public BVec add(byte x, byte y, byte z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
		
		return this;
	}
	
	public BVec added(byte x, byte y, byte z)
	{
		return this.clone().add(x, y, z);
	}
	
	public BVec subtract(BVec vec)
	{
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;
		
		return this;
	}
	
	public BVec subtracted(BVec vec)
	{
		return this.clone().subtract(vec);
	}
	
	public BVec subtract(byte x, byte y, byte z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;
		
		return this;
	}
	
	public BVec subtracted(byte x, byte y, byte z)
	{
		return this.clone().subtract(x, y, z);
	}
	
	public BVec multiply(BVec vec)
	{
		this.x *= vec.x;
		this.y *= vec.y;
		this.z *= vec.z;
		
		return this;
	}
	
	public BVec multiplied(BVec vec)
	{
		return this.clone().multiply(vec);
	}
	
	public BVec multiply(byte x, byte y, byte z)
	{
		this.x *= x;
		this.y *= y;
		this.z *= z;
		
		return this;
	}
	
	public BVec multiplied(byte x, byte y, byte z)
	{
		return this.clone().multiply(x, y, z);
	}
	
	public BVec divide(BVec vec)
	{
		this.x /= vec.x;
		this.y /= vec.y;
		this.z /= vec.z;
		
		return this;
	}
	
	public BVec divided(BVec vec)
	{
		return this.clone().divide(vec);
	}
	
	public BVec divide(byte x, byte y, byte z)
	{
		this.x /= x;
		this.y /= y;
		this.z /= z;
		
		return this;
	}
	
	public BVec divided(byte x, byte y, byte z)
	{
		return this.clone().divide(x, y, z);
	}
	
	public BVec invert()
	{
		this.x = (byte)-this.x;
		this.y = (byte)-this.y;
		this.z = (byte)-this.z;
		
		return this;
	}
	
	public BVec inverted()
	{
		return this.clone().invert();
	}
	
	public int squaredLength()
	{
		return x * x + y * y + z * z;
	}
	
	public double length()
	{
		return MathHelper.sqrt_double(squaredLength());
	}
	
	public BVec clone()
	{
		return new BVec(this.x, this.y, this.z);
	}
	
	public boolean isNull()
	{
		return this.x == 0.0D & this.y == 0.0D & this.z == 0.0D;
	}
}