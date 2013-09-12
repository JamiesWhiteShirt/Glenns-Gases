package glenn.gases.util;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class DVec
{
	public double x;
	public double y;
	public double z;
	
	public DVec()
	{
		this.x = 0.0D;
		this.y = 0.0D;
		this.z = 0.0D;
	}
	
	public DVec(DVec vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public DVec(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public DVec set(DVec vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		
		return this;
	}
	
	public DVec set(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	public DVec add(DVec vec)
	{
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
		
		return this;
	}
	
	public DVec added(DVec vec)
	{
		return this.clone().add(vec);
	}
	
	public DVec add(double x, double y, double z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
		
		return this;
	}
	
	public DVec added(double x, double y, double z)
	{
		return this.clone().add(x, y, z);
	}
	
	public DVec subtract(DVec vec)
	{
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;
		
		return this;
	}
	
	public DVec subtracted(DVec vec)
	{
		return this.clone().subtract(vec);
	}
	
	public DVec subtract(double x, double y, double z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;
		
		return this;
	}
	
	public DVec subtracted(double x, double y, double z)
	{
		return this.clone().subtract(x, y, z);
	}
	
	public DVec multiply(DVec vec)
	{
		this.x *= vec.x;
		this.y *= vec.y;
		this.z *= vec.z;
		
		return this;
	}
	
	public DVec multiplied(DVec vec)
	{
		return this.clone().multiply(vec);
	}
	
	public DVec multiply(double x, double y, double z)
	{
		this.x *= x;
		this.y *= y;
		this.z *= z;
		
		return this;
	}
	
	public DVec multiplied(double x, double y, double z)
	{
		return this.clone().multiply(x, y, z);
	}
	
	public DVec divide(DVec vec)
	{
		this.x /= vec.x;
		this.y /= vec.y;
		this.z /= vec.z;
		
		return this;
	}
	
	public DVec divided(DVec vec)
	{
		return this.clone().divide(vec);
	}
	
	public DVec divide(double x, double y, double z)
	{
		this.x /= x;
		this.y /= y;
		this.z /= z;
		
		return this;
	}
	
	public DVec divided(double x, double y, double z)
	{
		return this.clone().divide(x, y, z);
	}
	
	public double dotProduct(DVec vec)
	{
		return this.x * vec.x + this.y * vec.y + this.z * vec.z;
	}
	
	public double dotProduct(double x, double y, double z)
	{
		return this.x * x + this.y * y + this.z * z;
	}
	
	public DVec scale(double d)
	{
		this.x *= d;
		this.y *= d;
		this.z *= d;
		
		return this;
	}
	
	public DVec scaled(double d)
	{
		return clone().scale(d);
	}
	
	public DVec iScale(double d)
	{
		this.x /= d;
		this.y /= d;
		this.z /= d;
		
		return this;
	}
	
	public DVec iScaled(double d)
	{
		return this.clone().iScale(d);
	}
	
	public DVec invert()
	{
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
		
		return this;
	}
	
	public DVec inverted()
	{
		return this.clone().invert();
	}
	
	public DVec normalize()
	{
		return this.iScale(length());
	}
	
	public DVec normalized()
	{
		return clone().normalize();
	}
	
	public DVec xRotate(double d)
	{
		double c = Math.cos(d);
		double s = Math.sin(d);
		
		double ty = this.y;
		double tz = this.z;
		
		this.y = c * ty + s * tz;
		this.z = c * tz - s * ty;
		
		return this;
	}
	
	public DVec xRotated(double d)
	{
		return this.clone().xRotate(d);
	}
	
	public DVec yRotate(double d)
	{
		double c = Math.cos(d);
		double s = Math.sin(d);
		
		double tx = this.x;
		double tz = this.z;
		
		this.x = c * tx + s * tz;
		this.z = c * tz - s * tx;
		
		return this;
	}
	
	public DVec yRotated(double d)
	{
		return this.clone().yRotate(d);
	}
	
	public DVec zRotate(double d)
	{
		double c = Math.cos(d);
		double s = Math.sin(d);
		
		double tx = this.x;
		double ty = this.y;
		
		this.x = c * tx + s * ty;
		this.y = c * ty - s * tx;
		
		return this;
	}
	
	public DVec zRotated(double d)
	{
		return this.clone().zRotate(d);
	}
	
	public double squaredLength()
	{
		return x * x + y * y + z * z;
	}
	
	public double length()
	{
		return MathHelper.sqrt_double(squaredLength());
	}
	
	public DVec clone()
	{
		return new DVec(this.x, this.y, this.z);
	}
	
	public boolean isNull()
	{
		return this.x == 0.0D & this.y == 0.0D & this.z == 0.0D;
	}
	
	
	
	public DVec2 xy()
	{
		return new DVec2(this.x, this.y);
	}
	
	public DVec2 xz()
	{
		return new DVec2(this.x, this.z);
	}
	
	public DVec2 yz()
	{
		return new DVec2(this.y, this.z);
	}
	
	public static DVec randomNormalizedVec(Random random)
	{
		return new DVec(random.nextDouble() - 0.5D, random.nextDouble() - 0.5D, random.nextDouble() - 0.5D).normalized();
	}
}