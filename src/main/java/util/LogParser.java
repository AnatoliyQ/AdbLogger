package util;

import java.io.*;

public class LogParser {
    public static String findExeption(String path)   {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"))) {
            String strLine;
            String ex = null;
            StringBuilder buffer = new StringBuilder();

            while ((strLine = reader.readLine()) != null) {
                if (strLine.contains("FATAL EXCEPTION")) {
                    ex = strLine.substring(0, 43);
                    System.out.println("0-43 ______________ " + strLine.substring(0, 43));
                }

                if (ex != null && strLine.contains(ex)) {
                    buffer.append(strLine).append("\n");
                } else if (ex != null) {
                    buffer.append("\n\n");
                    ex = null;
                }

            }

            if (buffer.length() == 0) {
                return "FATAL EXEPTION not found.";
            } else {
                return buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Something was unexpected. Check log file";
    }
}
