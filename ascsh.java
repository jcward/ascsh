import com.adobe.flash.compiler.clients.MXMLC;
import com.adobe.flash.compiler.clients.COMPC;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class ascsh
{
  public static void main(String[] args)
  {
    System.out.println("ascsh v0.2 by jeff.ward@simbulus.com");

    if (args.length>0) compile(args);
    System.out.println("(ascsh)");

    String line = "";
    Scanner stdin = new Scanner(System.in);
    do
    {
      if (line.equals("")) continue;
      try {
        String[] result = Commandline.translateCommandline(line);
        compile(result);
      } catch (Exception e) {
        System.out.println("Error parsing command:\n"+line);        
      }
      System.out.println("(ascsh)");

    } while (stdin.hasNextLine() &&
             (line=stdin.nextLine())!=null &&
             !line.equals("quit") &&
             !line.equals("exit"));

    System.exit(0);
  }

  public static void compile(String[] args)
  {
    // Shift first element (command) from args
    ArrayList<String> list = new ArrayList<String>(Arrays.asList(args));
    String command = list.remove(0);
    args = list.toArray(new String[list.size()]);

    int exitCode;
    if (command.equals("mxmlc")) {
      exitCode = MXMLC.staticMainNoExit(args);
    } else if (command.equals("compc")) {
      exitCode = COMPC.staticMainNoExit(args);
    } else {
      System.out.println("ascsh unknown command '"+command+"'");
      exitCode = 255;
    }
    System.out.println("Compile status: "+exitCode);
  }
}
