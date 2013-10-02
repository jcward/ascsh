import com.adobe.flash.compiler.clients.MXMLC;
import java.util.Scanner;

public class ascsh extends MXMLC
{
  public static void main(String[] args)
  {
    System.out.println("ascsh 0.1");

    String line;
    Scanner stdin = new Scanner(System.in);
    do
    {
      int exitCode = MXMLC.staticMainNoExit(args);
      System.out.println("Compile status: "+exitCode);
    } while(stdin.hasNextLine() && !(line=stdin.nextLine()).equals("exit"));

    System.exit(0);
  }
}
