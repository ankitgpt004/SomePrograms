//program for prime number
class PrimeNumber{
static int n=125;
static int m=n/2;
  public static void main(String args[]){
  for(int i=2;i<=m;i++){
  if(n%i==0){
  System.out.println("number is not prime");
    break;
   }
    else
      System.out.println("number is prime");
  }
 }
}
