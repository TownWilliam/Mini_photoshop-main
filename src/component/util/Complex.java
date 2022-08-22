package src.component.util;

public class Complex {
	double r;
	double i;
	
	public Complex(double r,double i)
	{
		this.r=r;
		this.i=i;
	}
	public Complex add(Complex c)
	{
		return new Complex(this.r+c.r, this.i+c.i);
	}
	public Complex multiple(Complex c)
	{
		return new Complex(this.r*c.r-this.i*c.i, this.r*c.i + this.i*c.r);
	}
	public Complex multiple(double k)
	{
		return new Complex(k*this.r, k*this.i);
	}
	public Complex exp()
	{
		return new Complex(Math.exp(this.r)*Math.cos(this.i),Math.exp(this.r)*Math.sin((i)));
	}
	public double mod()
	{
		return Math.sqrt(this.r*this.r+this.i*this.i);
	}
	@Override
	public String toString()
	{  if(this.i>=0)
		return String.valueOf(this.r)+"+"+String.valueOf(this.i)+"i";
	else
		return String.valueOf(this.r)+String.valueOf(this.i)+"i";
	}
}
