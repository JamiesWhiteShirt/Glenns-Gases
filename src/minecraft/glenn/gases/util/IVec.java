package glenn.gases.util;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class IVec
{
	public int x;
	public int y;
	public int z;
	
	public IVec()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public IVec(IVec vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public IVec(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public IVec set(IVec vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		
		return this;
	}
	
	public IVec set(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	public IVec add(IVec vec)
	{
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
		
		return this;
	}
	
	public IVec added(IVec vec)
	{
		return this.clone().add(vec);
	}
	
	public IVec add(int x, int y, int z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
		
		return this;
	}
	
	public IVec added(int x, int y, int z)
	{
		return this.clone().add(x, y, z);
	}
	
	public IVec subtract(IVec vec)
	{
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;
		
		return this;
	}
	
	public IVec subtracted(IVec vec)
	{
		return this.clone().subtract(vec);
	}
	
	public IVec subtract(int x, int y, int z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;
		
		return this;
	}
	
	public IVec subtracted(int x, int y, int z)
	{
		return this.clone().subtract(x, y, z);
	}
	
	public IVec multiply(IVec vec)
	{
		this.x *= vec.x;
		this.y *= vec.y;
		this.z *= vec.z;
		
		return this;
	}
	
	public IVec multiplied(IVec vec)
	{
		return this.clone().multiply(vec);
	}
	
	public IVec multiply(int x, int y, int z)
	{
		this.x *= x;
		this.y *= y;
		this.z *= z;
		
		return this;
	}
	
	public IVec multiplied(int x, int y, int z)
	{
		return this.clone().multiply(x, y, z);
	}
	
	public IVec divide(IVec vec)
	{
		this.x /= vec.x;
		this.y /= vec.y;
		this.z /= vec.z;
		
		return this;
	}
	
	public IVec divided(IVec vec)
	{
		return this.clone().divide(vec);
	}
	
	public IVec divide(int x, int y, int z)
	{
		this.x /= x;
		this.y /= y;
		this.z /= z;
		
		return this;
	}
	
	public IVec divided(int x, int y, int z)
	{
		return this.clone().divide(x, y, z);
	}
	
	public IVec invert()
	{
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
		
		return this;
	}
	
	public IVec inverted()
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
	
	public IVec clone()
	{
		return new IVec(this.x, this.y, this.z);
	}
	
	public boolean isNull()
	{
		return this.x == 0.0D & this.y == 0.0D & this.z == 0.0D;
	}
}