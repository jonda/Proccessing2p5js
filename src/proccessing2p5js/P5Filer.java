/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessing2p5js;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author dahjon
 */
public class P5Filer {

//    final static private String BASE_OUTPUT_DIR = "D:\\xampp\\htdocs\\p5";
    final static private String BASE_OUTPUT_DIR = "C:\\wamp64\\www\\p5";
    final static public String BROWSER_PATH = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
    final static public String BASE_URL = "http://localhost/p5/";
    final static public String HTML_FILE_NAME = "index.html";

    static public void sparaFiler(String procFilePathStr, String p5Code) throws IOException {
        procFilePathStr = procFilePathStr.replaceAll(".pde", ".js");
        Path procFilePath = Paths.get(procFilePathStr);
        String fileName = procFilePath.getFileName().toString();
        String dirName = fileName.substring(0, fileName.length() - 3);
        sparaP5jsFil(dirName, fileName, p5Code);

        sparaHTMLFil(dirName, fileName);
        runInBrowser(dirName);

    }

    public static void sparaP5jsFil(String dirName, String fileName, String p5Code) throws IOException {
        String dirPathStr = BASE_OUTPUT_DIR + File.separator + dirName;
        Path dirPath = Paths.get(dirPathStr);
        if (!Files.exists(dirPath)) {
            Files.createDirectory(dirPath);
        }
        String p5FilePath = dirPathStr + File.separator + fileName;
        System.out.println("p5FilePath = " + p5FilePath);
        System.out.println("fileName = " + fileName);
        Path path = Paths.get(p5FilePath);

        Files.write(path, p5Code.getBytes());
    }

    private static void sparaHTMLFil(String dirName, String fileName) throws IOException {

        String pathStr = BASE_OUTPUT_DIR + File.separator + dirName + File.separator + HTML_FILE_NAME;
        String html = "<html>\n"
                + "  <head>\n"
                + "    <script src=\"//cdnjs.cloudflare.com/ajax/libs/p5.js/0.5.9/p5.js\"></script>\n"
                + "    <script src=\""
                + fileName
                + "\"></script>\n"
                + "  </head>\n"
                + "  <body>\n"
                + "  </body>\n"
                + "</html>";
        Path path = Paths.get(pathStr);

        Files.write(path, html.getBytes());
    }

    private static void runInBrowser(String dirName) throws IOException {
        String filNamn = BASE_URL+dirName+"/"+HTML_FILE_NAME;
        String filNamnURL = filNamn.replaceAll(" ", "%20");
        String cmd = BROWSER_PATH + " " + filNamnURL;
        System.out.println(cmd);
        Process p = Runtime.getRuntime().exec(cmd);
    }
}
