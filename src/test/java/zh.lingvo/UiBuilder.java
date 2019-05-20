package zh.lingvo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UiBuilder {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java UiBuilder <path-to-ui-folder>");
            System.exit(1);
        }
        runProcessCommand("./bin/reDeployUi.sh", args[0]);
    }

    private static void runProcessCommand(String... command) {
        String s;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(command);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null)
                System.out.println("[stdout] " + s);
            while ((s = stdError.readLine()) != null)
                System.out.println("[stderr] " + s);

            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
