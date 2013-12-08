package glenn.gasesframework.util;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class DVec2
{
	public double x;
	public double y;
	
	public DVec2()
	{
		this.x = 0.0D;
		this.y = 0.0D;
	}
	
	public DVec2(DVec2 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
	}
	
	public DVec2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public DVec2 set(DVec2 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		
		return this;
	}
	
	public DVec2 set(double x, double y)
	{
		this.x = x;
		this.y = y;
		
		return this;
	}
	
	public DVec2 add(DVec2 vec)
	{
		this.x += vec.x;
		this.y += vec.y;
		
		return this;
	}
	
	public DVec2 added(DVec2 vec)
	{
		return this.clone().add(vec);
	}
	
	public DVec2 add(double x, double y)
	{
		this.x += x;
		this.y += y;
		
		return this;
	}
	
	public DVec2 added(double x, double y)
	{
		return this.clone().add(x, y);
	}
	
	public DVec2 subtract(DVec2 vec)
	{
		this.x -= vec.x;
		this.y -= vec.y;
		
		return this;
	}
	
	public DVec2 subtracted(DVec2 vec)
	{
		return this.clone().subtract(vec);
	}
	
	public DVec2 subtract(double x, double y)
	{
		this.x -= x;
		this.y -= y;
		
		return this;
	}
	
	public DVec2 subtracted(double x, double y)
	{
		return this.clone().subtract(x, y);
	}
	
	public DVec2 multiply(DVec2 vec)
	{
		this.x *= vec.x;
		this.y *= vec.y;
		
		return this;
	}
	
	public DVec2 multiplied(DVec2 vec)
	{
		return this.clone().multiply(vec);
	}
	
	public DVec2 multiply(double x, double y)
	{
		this.x *= x;
		this.y *= y;
		
		return this;
	}
	
	public DVec2 multiplied(double x, double y)
	{
		return this.clone().multiply(x, y);
	}
	
	public DVec2 divide(DVec2 vec)
	{
		this.x /= vec.x;
		this.y /= vec.y;
		
		return this;
	}
	
	public DVec2 divided(DVec2 vec)
	{
		return this.clone().divide(vec);
	}
	
	public DVec2 divide(double x, double y)
	{
		this.x /= x;
		this.y /= y;
		
		return this;
	}
	
	public DVec2 divided(double x, double y)
	{
		return this.clone().divide(x, y);
	}
	
	public double dotProduct(DVec2 vec)
	{
		return this.x * vec.x + this.y * vec.y;
	}
	
	public double dotProduct(double x, double y)
	{
		return this.x * x + this.y * y;
	}
	
	public DVec2 scale(double d)
	{
		this.x *= d;
		this.y *= d;
		
		return this;
	}
	
	public DVec2 scaled(double d)
	{
		return clone().scale(d);
	}
	
	public DVec2 iScale(double d)
	{
		this.x /= d;
		this.y /= d;
		
		return this;
	}
	
	public DVec2 iScaled(double d)
	{
		return this.clone().iScale(d);
	}
	
	public DVec2 invert()
	{
		this.x = -this.x;
		this.y = -this.y;
		
		return this;
	}
	
	public DVec2 inverted()
	{
		return this.clone().invert();
	}
	
	public DVec2 normalize()
	{
		return this.iScale(length());
	}
	
	public DVec2 normalized()
	{
		return clone().normalize();
	}
	
	public double squaredLength()
	{
		return x * x + y * y;
	}
	
	public double length()
	{
		return MathHelper.sqrt_double(squaredLength());
	}
	
	public double angle()
	{
		return Math.atan2(y, x);
	}
	
	public DVec2 clone()
	{
		return new DVec2(this.x, this.y);
	}
	
	public boolean isNull()
	{
		return this.x == 0.0D & this.y == 0.0D;
	}
	
	public static DVec2 randomNormalizedVec(Random random)
	{
		return new DVec2(random.nextDouble() - 0.5D, random.nextDouble() - 0.5D).normalized();
	}
}