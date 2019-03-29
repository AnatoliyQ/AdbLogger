package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor {
    private CommandExecutor() {
    }

    public static BufferedReader executeCommand(String command) {
        Process process = null;
        try {
            System.out.println("Executing command " + command);
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

}
