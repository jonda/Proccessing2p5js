/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessing2p5js;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dahjon
 */
public class ConvertArrayLists {

    public static void convertArrayLists(StringBuffer procKod) {

        convertCreation(procKod);
        ArrayList<String> names = getArrayListNames(procKod);
        System.out.println("convertArrayLists names = " + names);
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            convertGet(procKod, name);
            convertSize(procKod, name);
            convertAdd(procKod, name);
            convertRemove(procKod, name);

        }

    }
//ArrayList<Kvadrat> kvadratLista = new ArrayList<Kvadrat>();

    private static ArrayList<String> getArrayListNames(StringBuffer procKod) {
        ArrayList<String> names = new ArrayList<String>();
        Pattern pattern = Pattern.compile("ArrayList<.*>\\s+([a-zA-Z0-9]+)");
        Matcher m = pattern.matcher(procKod);
        while (m.find()) {
            System.out.println("getArrayListName m.group(1) = " + m.group(1));
            names.add(m.group(1));
        }
        return names;
    }

    private static void convertCreation(StringBuffer procKod) {
        Pattern pattern = Pattern.compile("(new ArrayList<.*>)\\([0-9]*\\)");
        Matcher m = pattern.matcher(procKod);
        while (m.find()) {
            System.out.println("ConvertArrayLists convertCreation m.group(1) = " + m.group(1));
            procKod.replace(m.start(1), m.end(1), "           new Array");
        }
    }

    private static void convertGet(StringBuffer procKod, String name) {
        System.out.println("");
        //       Pattern pattern = Pattern.compile(name + "(.get\\()[0-9]+(\\))");
        Pattern pattern = Pattern.compile(name + "(.get\\()");
        Matcher m = pattern.matcher(procKod);
        while (m.find()) {
            int firstBrace = m.end() - 1;
            //System.out.println("convertGet m.group(1) = " + m.group(1));
            //System.out.println("convertGet m.group(2) = " + m.group(2));
            //System.out.println("procKod.charAt(m.start(2): " + procKod.charAt(m.start(2)));
            System.out.println("convertGet procKod.charAt(firstBrace) = " + procKod.charAt(firstBrace) + " firstBrace: " + firstBrace);
            int lastBrace = findMatchingBrace(procKod, firstBrace);
            System.out.println("convertGet procKod.charAt(lastBrace) = " + procKod.charAt(lastBrace) + " lastBrace: " + lastBrace);
            procKod.setCharAt(lastBrace, ']');
//            procKod.setCharAt(m.start(2), ']');
            procKod.replace(m.start(1), m.end(1), "[");
            //System.out.println("procKod.charAt(m.start(2) efter...: " + procKod.charAt(m.start(2)));
            //System.out.println("m.group() efter...: " + m.group());
        }
    }

    private static void convertAdd(StringBuffer procKod, String name) {
        Pattern pattern = Pattern.compile(name + "(.add\\()");
        Matcher m = pattern.matcher(procKod);
        while (m.find()) {
            System.out.println("convertAdd m.group() = " + m.group());
            procKod.replace(m.start(1), m.end(1), ".push(");
        }
    }
    private static void convertSize(StringBuffer procKod, String name) {
        Pattern pattern = Pattern.compile(name + "(.size\\(\\s*\\))");
        Matcher m = pattern.matcher(procKod);
        while (m.find()) {
            System.out.println("convertSize m.group() = " + m.group());
            procKod.replace(m.start(1), m.end(1), ".length");
        }
    }    

    private static void convertRemove(StringBuffer procKod, String name) {
        
        //Om remove står först på raden....
        Pattern pattern = Pattern.compile(name + "\n[ \t]*(.remove\\()");
        Matcher m = pattern.matcher(procKod);
        while (m.find()) {
            System.out.println("convertAdd m.group() = " + m.group());

            procKod.replace(m.start(1), m.end(1), ".splice(");
            int firstBrace = procKod.indexOf("(", m.start(1));
            System.out.println("convertGet procKod.charAt(firstBrace) = " + procKod.charAt(firstBrace) + " firstBrace: " + firstBrace);
            int lastBrace = findMatchingBrace(procKod, firstBrace);
            System.out.println("convertGet procKod.charAt(lastBrace) = " + procKod.charAt(lastBrace) + " lastBrace: " + lastBrace);
            procKod.insert(lastBrace, ",1");
        }
        String searchStr = name + ".remove\\(";
        String functionToInsert = InsertFunctions.ArrayListRemove;
        String functionName = InsertFunctions.getFunctionName(functionToInsert);//"Processing2p5jsArrayListRemove(";
        //InsertFunctions.replaceAndInsertFunction(searchStr, procKod, functionToInsert);
        pattern = Pattern.compile(searchStr);
        m = pattern.matcher(procKod);
        while (m.find()) {
            System.out.println("convertAdd m.group() = " + m.group());
            procKod.replace(m.start(), m.end(), functionName+name+", ");
            InsertFunctions.insert(procKod, functionToInsert);
        }        
    }


    private static int findMatchingBrace(StringBuffer code, int firstBrace) {
        int nr = 0;
        for (int i = firstBrace; i < code.length(); i++) {
            if (code.charAt(i) == '(') {

                nr++;
                System.out.println("findMatchingBrace Hittade vänsterparentes nr = " + nr);
            } else if (code.charAt(i) == ')') {
                nr--;
                System.out.println("findMatchingBrace Hittade högerparentes nr = " + nr);
            }
            if (nr == 0) {
                return i;
            }

        }
        return firstBrace;
    }

}
