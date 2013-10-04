import com.adobe.flash.compiler.clients.MXMLC;
import com.adobe.flash.compiler.clients.COMPC;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class ascsh
{
  public static void main(String[] args)
  {
    System.out.println("ascsh v0.2 by Jeff Ward, simbulus.com");

    // Allow compile from initial commandline
    if (args.length>0) compile(args);
    System.out.println("(ascsh)");

    // Monitor stdin for new commands
    String line = "";
    Scanner stdin = new Scanner(System.in);
    do
    {
      if (line.equals("")) continue;

      String[] result = null;
      if (line.indexOf("|x|")>=0) {
        // join token used by ascshd
        result = line.split("\\|x\\|");
      } else {
        // This isn't 100% reliable, hence the special token above
        try {
          System.out.println("commandline parsing...");
          result = Commandline.translateCommandline(line);
        } catch (Exception e) {
          System.out.println("Error parsing command:\n"+line);
          System.out.println("(ascsh)");
          continue;
        }
      }
      compile(result);
      System.out.println("(ascsh)");

    } while (stdin.hasNextLine() &&
             (line=stdin.nextLine())!=null &&
             !line.equals("quit") &&
             !line.equals("exit"));

    System.exit(0);
  }

  // Invoke MXMLC or COMPC
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
