import com.adobe.flash.compiler.clients.MXMLC;
import com.adobe.flash.compiler.clients.COMPC;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

public class ascsh
{
  /**
   * ascsh main entry point
   */
  public static void main(String[] args)
  {
    System.out.println("ascsh v0.4 by Jeff Ward, simbulus.com");

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
          startCapture();
          exitCode = ((MXMLC)compiler).staticMainNoExit(args);
          stopCapture();          
        } else {
          startCapture();
          exitCode = ((COMPC)compiler).staticMainNoExit(args);
          stopCapture();
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
        startCapture();
        exitCode = compiler.staticMainNoExit(args);
        stopCapture();
      } else if (command.equals("compc")) {
        COMPC compiler = new COMPC();
        compilers.add(compiler);
        targets.add(list);
        startCapture();
        exitCode = compiler.staticMainNoExit(args);
        stopCapture();
      } else {
        System.out.println("ascsh unknown command '"+command+"'");
        exitCode = 255;
      }
    }
    System.out.println("Compile status: "+exitCode);
  }
  
  static ByteArrayOutputStream buffer;
  static PrintStream oldOut;
  static Pattern errPattern = Pattern.compile(".*\\.[a-z]+.*:[0-9]+");
  
  static void startCapture()
  {
    buffer = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(buffer);
    // IMPORTANT: Save the old System.out!
    oldOut = System.err;
    // Tell Java to use your special stream
    System.setErr(ps);
    // Print some output: goes to your special stream
  }
  
  static void stopCapture()
  {
    // Put things back
    System.err.flush();
    System.setErr(oldOut);
    
    // Reformat errors to match FCSH
    String[] lines = buffer.toString().split("\n");
    for (int i=0, len=lines.length; i<len; i++) {
      String line = lines[i].trim();
      if (errPattern.matcher(line).matches()) {
        System.err.print(line);
        System.err.print(": ");
      } else System.err.println(lines[i]);
    }
      
    oldOut = null;
    buffer = null;
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
