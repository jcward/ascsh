import com.adobe.flash.compiler.clients.MXMLC;
import java.util.Scanner;

public class ascsh extends MXMLC
{
  public static void main(String[] args)
  {
    // TODO: compile id's (args -> MXMLC / COMPC)

    String line;
    Scanner stdin = new Scanner(System.in);
    do
    {
      int exitCode = MXMLC.staticMainNoExit(args);
      System.out.println("Compile status: "+exitCode);
      System.out.println("----------------------------");
      System.out.println("(ascsh)");
    } while(stdin.hasNextLine() && !(line=stdin.nextLine()).equals("quit"));

    System.exit(0);
  }
}
