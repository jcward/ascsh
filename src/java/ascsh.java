import com.adobe.flash.compiler.clients.MXMLC;
import com.adobe.flash.compiler.clients.COMPC;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ascsh
{
  /**
   * ascsh main entry point
   */
  public static void main(String[] args)
  {
    System.out.println("ascsh v0.3 by Jeff Ward, simbulus.com");

    if (args.length>0) {
      compile(args);
    }

    // Accept commands from STDIN
    String line;
    Scanner stdin = new Scanner(System.in);

    while (true) {
      System.out.print("(fcsh) ");
      line = stdin.nextLine();
      if (line==null || line.equals("")) continue;

      line = line.trim();
      if (line.equals("quit") || line.equals("exit")) break;
      if (line.equals("help")) { print_help(); continue; }

      // Parse String command into args array
      String[] compile_args = null;
      if (line.indexOf("|x|")>=0) {
        // join token used by ascshd
        compile_args = line.split("\\|x\\|");
      } else {
        // This isn't 100% reliable, hence the special token above
        try {
          //System.out.println("commandline parsing...");
          compile_args = Commandline.translateCommandline(line);
        } catch (Exception e) {
          System.out.println("Error parsing command:\n"+line);
          continue;
        }
      }
      compile(compile_args);
    }

    System.exit(0);
  }

  private static ArrayList<ArrayList<String>> targets = new ArrayList<ArrayList<String>>();
  private static ArrayList<Object> compilers = new ArrayList<Object>();

  /**
   * Invoke MXMLC or COMPC
   */
  public static void compile(String[] args)
  {

    ArrayList<String> list = new ArrayList<String>(Arrays.asList(args));
    String command = list.remove(0); // Shift first element (command) from args
    int exitCode;

    // Compile existing target
    if (command.equals("compile")) {
      int idx = Integer.parseInt(list.get(0))-1;
      if (idx>=compilers.size()) {
        System.out.println("fcsh: Target "+(idx+1)+" not found");
        return;
      } else {
        Object compiler = compilers.get(idx);
        list = targets.get(idx);
        args = list.toArray(new String[list.size()]);
        if (compiler instanceof MXMLC) {
          //exitCode = ((MXMLC)compiler).mainNoExit(args); // hangs on second compile...
          exitCode = ((MXMLC)compiler).staticMainNoExit(args);
        } else {
          //exitCode = ((COMPC)compiler).mainNoExit(args); // hangs on second compile...
          exitCode = ((COMPC)compiler).staticMainNoExit(args);
        }
      }
    } else {
      args = list.toArray(new String[list.size()]);

      if (targets.size()>0) {
        System.out.println("WARNING: ascsh currently only reliably handles one compile target via staticMainNoExit");
      }
      System.out.println("fcsh: Assigned "+(targets.size()+1)+" as the compile target id");
   
      if (command.equals("mxmlc")) {
        MXMLC compiler = new MXMLC();
        compilers.add(compiler);
        targets.add(list);
        //exitCode = compiler.mainNoExit(args); // hangs on second compile...
        exitCode = compiler.staticMainNoExit(args);
      } else if (command.equals("compc")) {
        COMPC compiler = new COMPC();
        compilers.add(compiler);
        targets.add(list);
        //exitCode = compiler.mainNoExit(args); // hangs on second compile...
        exitCode = compiler.staticMainNoExit(args);
      } else {
        System.out.println("ascsh unknown command '"+command+"'");
        exitCode = 255;
      }
    }
    System.out.println("Compile status: "+exitCode);
  }

  /**
   * Print available commands
   */
  public static void print_help()
  {
    System.out.print(
       "List of fcsh commands:\n"
      +"mxmlc arg1 arg2 ...      full compilation and optimization; return a target id\n"
      +"compc arg1 arg2 ...      full SWC compilation\n"
      +"compile id               incremental compilation\n"
      +"clear [id]               clear target(s) (NOT SUPPORTED)\n"
      +"info [id]                display compile target info\n"
      +"quit                     quit\n"
    );
  }
}
