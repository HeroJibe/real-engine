import java.util.Random;

public class Main 
{
	public static void Main(String[] args)
	{
		Random r = new Random();
		int res = r.nextInt(3);
		System.out.print("result: " + res);
	}
}
