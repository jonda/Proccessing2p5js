/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessing2p5js;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dahjon
 */
public class ConvertPVector {

    public static void convertPVector(StringBuffer procCode) {
        convertCreation(procCode);
        convertStaticUse(procCode);
        //convertLine(procCode);
    }

    private static void convertCreation(StringBuffer procKod) {
        Pattern pattern = Pattern.compile("new PVector\\(");
        Matcher m = pattern.matcher(procKod);
        while (m.find()) {
//            System.out.println("ConvertPVector convertCreation m.group() = " + m.group());
            procKod.replace(m.start(), m.end(), "createVector(");
        }
    }

    private static void convertStaticUse(StringBuffer procKod) {
        Pattern pattern = Pattern.compile("PVector\\.");
        Matcher m = pattern.matcher(procKod);
        while (m.find()) {
//            System.out.println("ConvertPVector convertCreation m.group() = " + m.group());
            procKod.replace(m.start(), m.end(), "p5.Vector.");
        }
    }

 /*   private static void convertLine(StringBuffer procKod) {
        Pattern pattern = Pattern.compile("line\\(\\s*[a-zA-Z0-9]\\s*,\\s*[a-zA-Z0-9]\\s*\\)");
        Matcher m = pattern.matcher(procKod);
        while (m.find()) {
//            System.out.println("ConvertPVector convertCreation m.group() = " + m.group());

            procKod.replace(m.start(), m.end(), InsertFunctions.getFunctionName(InsertFunctions.PVectorLine));
        }
        InsertFunctions.insert(procKod, InsertFunctions.PVectorLine);
    }
*/
}
