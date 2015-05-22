


public class Euclidean implements iDistance
{

	@Override
	public double calculate(Data d, Centroid c) 
	{
		return Math.sqrt(Math.pow((c.Y() - d.Y()), 2) + Math.pow((c.X() - d.X()), 2));
		
	}


}