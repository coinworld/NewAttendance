package me.blog.tastedroid.attendance.process;

import me.blog.tastedroid.attendance.AttendServices;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class FileTextTools {

    public static void setText(String text, File file) throws Exception {
        try (PrintWriter pw = new PrintWriter(file, "UTF-8")) {
            pw.print(text);
        }
    }

    public static String get(String urlstr) throws Exception {
        URL url = new URL(urlstr);
        return getString(url.openStream());
    }

    public static String getInside(String filename) throws Exception {
        return getString(AttendServices.getProcessor().getResource(filename));
    }

    public static String getFile(File file) throws FileNotFoundException {
        return getString(new FileInputStream(file));
    }

    public static String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try (Scanner fsc = new Scanner(is)) {
            while (fsc.hasNextLine()) {
                sb.append(fsc.nextLine()).append("\n");
            }
        }
        return sb.toString();
    }
}
