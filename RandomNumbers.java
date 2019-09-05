import java.util.Random;

public class RandomNumbers
{

  public static void main(String[] args)
  {
  
  }
  
  public static double randomDouble(int low, int high) //random double between the given limits, low and high
  {
	  Random rand = new Random();
	  double num1 = rand.nextDouble()* (high-low) + low;//calculates the randomdouble to be used in random drivers.
  		return num1;
  }
   /* The above multiplies by the (high - low) and adds the low in order to for example get a number within
    the range of 3-7 if the lower was 3 and upper was 7.*/
  public static int randomInt(int low, int high)// random integer inclusive of the given limits, low and high
  {
  	Random rand = new Random();
	if(high == low)
	return 0;
	if(high < low)
	{
		int temp = high;
		high = low;
		low = high;
	}
  int num2 = (rand.nextInt((high - low) + 1)) + low;//calculates the randomint to be used in random drivers
  return num2;
  }
  
}
