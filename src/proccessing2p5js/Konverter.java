/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessing2p5js;

import proccessing2p5js.replaceclass.ReplaceClass;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//new JFRame  getElementById;
//new Jbutton 
/**
 *
 * @author dahjon
 */
public class Konverter {

    public static final String BASETYPES = "void|boolean|color|int|float|double|long|String|StringBuffer|char|byte|ArrayList<.+>|PVector";

    static String[][] replaceFunctions = {
        {"size", "createCanvas"},
        {"println", "console.log"},
        {"System.out.println", "print"},
        {"System.out.print", "print"},
        {"mousePressed", "touchStarted"},
        {"mouseDragged", "touchMoved"},
        {"mouseReleased", "touchEnded"},
        {"pushMatrix", "push"},
        {"popMatrix", "pop"},
        {"Integer.parseInt", "Number"},
        {"Double.pareDouble", "Number"},
        {"Integer.valueOf", "Number"},
        {"Double.valueOf", "Number"},
        {"JOptionPane.showInputDialog", "prompt"},
        {"new PVector", "createVector"}, //{"new JFrame", "document.getElementById(\"settings\""}
    //{"new JButton", "document.createElement(\"BUTTON\""},
    };

    static String[][] replaceVariables = {
        {"P3D", "WEBGL"},
        {"keyPressed", "keyIsPressed"},
        {"ESC", "ESCAPE"},
        {"LEFT", "LEFT_ARROW"},
        {"RIGHT", "RIGHT_ARROW"},
        {"UP", "UP_ARROW"},
        {"DOWN", "DOWN_ARROW"},
        {"THIRD_PI", "(PI/3)"},
        {"mousePressed", "mouseIsPressed"}
    };

    static public StringBuffer konvert(StringBuffer procKod) {
        ReplaceClass.replaceClasses(procKod);
        convertColorComparison(procKod);
        ArrayList<String> classList = ConvertClass.convertClasses(procKod);
        ConvertPVector.convertPVector(procKod);
        System.out.println("classList = " + classList);
        String allTypes = getAllTypes(classList);
        System.out.println("allTypes = " + allTypes);
        ConvertArrayLists.convertArrayLists(procKod);
        convertFunctions(procKod, allTypes);
        convertThreeDimArrayCreation(procKod);
        convertTwoDimArrayCreation(procKod);
        convertArraysDeclaration(procKod);

        convertOneDimArrayCreation(procKod);
        convertVariables(procKod, allTypes);
        replaceFunctions(procKod);
        removeCasts(procKod);
        procKod = replaceVariables(procKod);
        insertSetupMethodIfNeeded(procKod);
        moveInit(procKod);
        exceptions(procKod);
        return procKod;
    }

    public static void convertFunctions(StringBuffer procKod, String allTypes) {
        //        String functionPatternStr = "([a-zA-Z0-9]+)\\[([ ,.a-zA-Z0-9]+)\\]";
        String functionPatternStr = "(" + allTypes + ")\\s+([a-zA-Z0-9]+)\\([ ,.a-zA-Z0-9\\[\\]]*\\)";
        Pattern functionPattern = Pattern.compile(functionPatternStr);
        Matcher funcm = functionPattern.matcher(procKod);
        int end = 0;
        while (funcm.find(end)) {
            int start = funcm.start();
            end = funcm.end();
            String funcname = funcm.group(2);
            String funcExp = funcm.group(0);
            funcExp = funcExp.replaceFirst("(" + allTypes + ")", "function");
            funcExp = funcExp.replaceAll("(" + allTypes + ")", "");
            funcExp = funcExp.replaceAll("\\[\\]", "");
            procKod.replace(start, end, funcExp);

            System.out.println("convertFunctions funcm.group(0)=funcExp: " + funcExp);
            /*System.out.println("\nfunctions namn: '" + funcname + "'\n");

            String type = replaceFirstMatch(funcm, procKod, "function");
            end = funcm.end();
            //System.out.println("end = " + end);
            end += "function".length() - type.length();
            int start = funcm.start();*/
            //System.out.println(type);
            //Pattern paramPattern = Pattern.compile("(void|int|float|double|long|String|StringBuffer|char|byte) [a-zA-Z0-9]+");
            /*            Pattern paramPattern = Pattern.compile("(void|color|int|float|double|long|String|StringBuffer|char|byte)");
            Matcher paramm = paramPattern.matcher(procKod);
//            System.out.println("start = " + start);
//            System.out.println("end = " + end);
            paramm.region(start, end);
            int endparam = start;
            while (paramm.find(endparam-start)) {
                endparam = paramm.end();
                System.out.println("paramm.start() = " + paramm.start());
                System.out.println("endparam = " + endparam);
                System.out.println("convertFunctions paramm.group(0): " + paramm.group(0));
                System.out.println("convertFunctions paramm.group(1): " + paramm.group(1));
               // System.out.println("procKod = " + procKod);
                System.out.println("paramm.start(), end+2: "+procKod.substring(paramm.start(), endparam+2));
                String typep = replaceFirstMatch(paramm, procKod, "");

//                System.out.println("endparam = " + endparam);
                endparam -=  typep.length();
            }
            if (endparam == 0) {
                System.out.println("inga param i " + funcname);
            }*/
        }
        if (end == 0) {
            System.out.println("Ingen matchning på funktion");
        }
    }

    private static void convertVariables(StringBuffer procKod, String allTypes) {
        Pattern vairablePattern = Pattern.compile("(" + allTypes + ")\\s+([a-zA-Z0-9]+)");
        //Pattern vairablePattern = Pattern.compile("(void|color|int|float|double|long|String|StringBuffer|char|byte) ([a-zA-Z0-9]+)^\\(");
        Matcher varm = vairablePattern.matcher(procKod);
        int end = 0;
        while (varm.find(end)) {

            end = varm.end();
            int lineStart = Math.max(procKod.lastIndexOf("\n", end),0);
            //System.out.println("lineStart = " + lineStart);
            //System.out.println("end = " + end);
            if (procKod.substring(lineStart, varm.start()).indexOf("class")==-1){
                String type = replaceFirstMatch(varm, procKod, "var");
                end += "var".length() - type.length();
            }

        }

    }

    private static void convertArraysDeclaration(StringBuffer procKod) {
//        Pattern vairablePattern = Pattern.compile("(void|color|int|float|double|long|String|StringBuffer|char|byte)([\\[\\]]+)\\s*([a-zA-Z0-9]+)\\s*[;=]");
        Pattern vairablePattern = Pattern.compile("([a-zA-Z0-9]+)(\\s*[\\[\\]]+)\\s*([a-zA-Z0-9]+)\\s*[;=]");
        Matcher varm = vairablePattern.matcher(procKod);
        int end = 0;
        while (varm.find(end)) {

            end = varm.end();
            //System.out.println("end = " + end);
            //System.out.println("convertArraysDefinitions varm.group(0) = " + varm.group(0));
            //System.out.println("convertArraysDefinitions varm.group(1) = " + varm.group(1));
            //final String brackets = varm.group(2);
            //System.out.println("convertArraysDefinitions brackets = " + brackets);
            //System.out.println("convertArraysDefinitions varm.group(3) = " + varm.group(3));
            //String type = replaceFirstMatch(varm, procKod, "var");
            String type = replaceMatch(1, 2, varm, procKod, "var");
            end += "var".length() - type.length();

        }

    }
//int[] position = {20,22,25,30,35,40,42,40,35,30,25,22,20,17,15,14,13,14,15,17,19,20,20};
    //new int[40]
/*  bTemp = {
        createVector(), createVector()
      };
    */
    private static void convertOneDimArrayCreation(StringBuffer procKod) {
//        Pattern pattern = Pattern.compile("new (color|int|float|double|long|String|StringBuffer|char|byte)(\\[[a-zA-Z0-9]+\\])");
        Pattern pattern = Pattern.compile("new ([a-zA-Z0-9]+)(\\[[a-zA-Z0-9]+\\])");
        Matcher varm = pattern.matcher(procKod);
        int end = 0;
        while (varm.find(end)) {
            System.out.println("convertOneDimArrayCreation varm.group(0) = " + varm.group(0));
            System.out.println("convertOneDimArrayCreation varm.group(1) = " + varm.group(1));
            System.out.println("convertOneDimArrayCreation varm.group(2) = " + varm.group(2));
            end = varm.end();
            int start = varm.start();
            String type = replaceFirstMatch(varm, procKod, "Array");
            int indexLeftBracket = procKod.indexOf("[", start);
            procKod.setCharAt(indexLeftBracket, '(');
            int indexRightBracket = procKod.indexOf("]", start);
            procKod.setCharAt(indexRightBracket, ')');

            end += "Array".length() - type.length();

        }
        pattern = Pattern.compile("=\\s*\\{[. \\n,A-Za-z\\(\\)\\s]+\\}\\s*;");
        varm = pattern.matcher(procKod);
        end = 0;
        System.out.println("Kollar om det finns endim array {...}");
        while (varm.find(end)) {
            end = varm.end();
            String arrInit = varm.group(0);
            System.out.println("convertOneDimArrayCreation arrInit = " + arrInit);
            arrInit = arrInit.replaceAll("\\{", "[");
            arrInit = arrInit.replaceAll("\\}", "]");
            procKod.replace(varm.start(), end, arrInit);

        }

    }
    //new int[10][20];
    //var a = new Array(); 
//while(a.push(new Array(20)) < 10);

    private static void convertTwoDimArrayCreation(StringBuffer procKod) {
//        Pattern pattern = Pattern.compile("new (color|int|float|double|long|String|StringBuffer|char|byte)(\\[[a-zA-Z0-9]+\\])");
        Pattern pattern = Pattern.compile("new [a-zA-Z0-9]+\\[([a-zA-Z0-9]+)\\]\\[([a-zA-Z0-9]+)\\]");
        Matcher varm = pattern.matcher(procKod);
        while (varm.find()) {
            System.out.println("convertTwoDimArrayCreation varm.group(0) = " + varm.group(0));
            System.out.println("convertTwoDimArrayCreation varm.group(1) = " + varm.group(1));
            System.out.println("convertTwoDimArrayCreation varm.group(2) = " + varm.group(2));

            String arrStr = "processing2p5jsNew2DArray(" + varm.group(1) + "," + varm.group(2) + ")";
            procKod.replace(varm.start(), varm.end(), arrStr);
            InsertFunctions.insert(procKod, InsertFunctions.New2DArray);
            //System.out.println("convertTwoDimArrayCreation varm.group(3) = " + varm.group(3));

        }

    }

    private static void convertThreeDimArrayCreation(StringBuffer procKod) {
//        Pattern pattern = Pattern.compile("new (color|int|float|double|long|String|StringBuffer|char|byte)(\\[[a-zA-Z0-9]+\\])");
        Pattern pattern = Pattern.compile("new [a-zA-Z0-9]+\\[([a-zA-Z0-9]+)\\]\\[([a-zA-Z0-9]+)\\]\\[([a-zA-Z0-9]+)\\]");
        Matcher varm = pattern.matcher(procKod);
        while (varm.find()) {
            System.out.println("convertThreeDimArrayCreation varm.group(0) = " + varm.group(0));
            System.out.println("convertThreeDimArrayCreation varm.group(1) = " + varm.group(1));
            System.out.println("convertThreeDimArrayCreation varm.group(2) = " + varm.group(2));

            String arrStr = "processing2p5jsNew3DArray(" + varm.group(1) + "," + varm.group(2) + "," + varm.group(3) + ")";
            procKod.replace(varm.start(), varm.end(), arrStr);
            InsertFunctions.insert(procKod, InsertFunctions.New3DArray);
            //System.out.println("convertTwoDimArrayCreation varm.group(3) = " + varm.group(3));

        }

    }

    private static void replaceFunctions(StringBuffer procKod) {
//        String functionPatternStr = "([a-zA-Z0-9]+)\\s*\\([ ,.a-zA-Z0-9]*\\)";
        String functionPatternStr = "([a-zA-Z0-9\\.]+)\\s*\\(";
        Pattern functionPattern = Pattern.compile(functionPatternStr);
        Matcher m = functionPattern.matcher(procKod);
        int end = 0;
        while (m.find(end)) {
            end = m.end();

            String funcname = m.group(1);
            //System.out.println("replaceFunctions funcname: '" + funcname + "'");
            String news = searchReplaceFunction(funcname);
            if (news != null) {
                //System.out.println("replaceFunctions m.start(1) = " + m.start(1));
                int charBeforeIndex = Math.max(m.start(1) - 1, 0);
                char charBefore = procKod.charAt(charBeforeIndex);
                //System.out.println("charBefore = " + charBefore);
                //String name = replaceFirstMatch(funcm, procKod, news);
                //  end += news.length() - name.length();
                //Undantag om funktionen står på första raden... charBeforeIndex==0
                if (!(Character.isLetter(charBefore) || charBefore == '.') || charBeforeIndex == 0) {
                    procKod.replace(m.start(1), m.end(1), news);
                    end = m.start(1) + news.length();
                }

            }
        }
    }

    public static String searchReplaceFunction(String old) {
        for (int i = 0; i < replaceFunctions.length; i++) {
            String[] replaceFunction = replaceFunctions[i];
            if (old.equals(replaceFunction[0])) {
                return replaceFunction[1];
            }
        }
        return null;
    }

    private static void removeCasts(StringBuffer procKod) {
        String patternStr = "\\(([int|float|double|long]*)\\)\\s*([a-zA-Z0-9]+)";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher m = pattern.matcher(procKod);
        int end = 0;
        while (m.find(end)) {
            end = m.end();
            int start = m.start();
            System.out.println("removeCasts m.group(0) = " + m.group(0));
            String type = m.group(1);
            System.out.println("removeCasts type = " + type);
            int removeLength = type.length() + 2;
            System.out.println("removeLength = " + removeLength);
            procKod.delete(start, start + removeLength);
            end = end - removeLength;

            //System.out.println("removeCasts m.group(2) = " + m.group(0));
        }
    }

    private static StringBuffer replaceVariables(StringBuffer procKod) {
        for (int i = 0; i < replaceVariables.length; i++) {

            procKod = new StringBuffer(procKod.toString().replaceAll(replaceVariables[i][0], replaceVariables[i][1]));
        }
        return procKod;
    }

    private static void insertSetupMethodIfNeeded(StringBuffer procKod) {
        //String patternStr = "function \\s+setup\\s*\\(";
        String patternStr = "function\\s+setup\\s*\\(";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher m = pattern.matcher(procKod);
        if (!m.find()) {
            replaceAll(procKod, "\n", "\n   ");
            procKod.insert(0, "function setup(){\n   ");
            procKod.append("\n}\n");
        }

    }

    public static void replaceAll(StringBuffer kod, String patternStr, String newstr) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher m = pattern.matcher(kod);
        int end = 0;
        while (m.find(end)) {
            int start = m.start();
            end = m.end();

            kod.replace(start, end, newstr);
        }

    }

    public static String replaceFirstMatch(Matcher funcm, StringBuffer procKod, String str) {

        //return replaceMatch(1, 1, funcm, procKod, str);
        return replaceMatchIndex(1, funcm, procKod, str);

    }

    public static String replaceSecondtMatch(Matcher funcm, StringBuffer procKod, String str) {

        //return replaceMatch(2, 1, funcm, procKod, str);
        return replaceMatchIndex(2, funcm, procKod, str);
    }

    public static String replaceMatch(int i, int antal, Matcher funcm, StringBuffer procKod, String str) {

        int start = funcm.start();
        String type = "";
        int totlen = 0;
        for (int k = 0; k < antal; k++) {
            type += funcm.group(i + k);
        }
        totlen = type.length();

        procKod.delete(start, start + totlen);
        procKod.insert(start, str);
        return type;
    }

    public static String replaceMatchIndex(int i, Matcher funcm, StringBuffer procKod, String str) {

        int start = funcm.start();
        String type = funcm.group(i);
        int startIndex = procKod.indexOf(type, start);

        int len = type.length();

        procKod.replace(startIndex, startIndex + len, str);

        //procKod.delete(start, start + totlen);
        //procKod.insert(start, str);
        return type;
    }

    private static String getAllTypes(ArrayList<String> classList) {
        String all = BASETYPES;
        for (int i = 0; i < classList.size(); i++) {
            String cl = classList.get(i);
            all += "|" + cl;
        }
        return all;
    }

    /*
    color red;  */
    private static void convertColorComparison(StringBuffer procKod) {
        ArrayList<String> colorNames = new ArrayList<>();
        Pattern pattern = Pattern.compile("\n\\s*color\\s+([A-Za-z0-9]+)[;=]");
        Matcher m = pattern.matcher(procKod);
        while (m.find()) {
            colorNames.add(m.group(1));
            //   System.out.println("convertSize m.group() = " + m.group());
            //   procKod.replace(m.start(1), m.end(1), ".length");
        }
        for (int i = 0; i < colorNames.size(); i++) {
            String col = colorNames.get(i);
            pattern = Pattern.compile("==\\s*" + col);
            Matcher mr = pattern.matcher(procKod);
            while (mr.find()) {
                int firstParen = getPreviousParenAndSkippOrOperator(procKod, mr.start());
                String firstParam = procKod.substring(firstParen + 1, mr.start());
                String newCode = "processing2p5jsCompareColors(" + firstParam + ", " + col + ")";
                procKod.replace(firstParen + 1, mr.end(), newCode);
                InsertFunctions.insert(procKod, InsertFunctions.CompareColors);
            }

        }

        System.out.println("colorNames = " + colorNames);
    }

    private static int getPreviousParenAndSkipp(StringBuffer procKod, int start) {
        int i = start;
        int antal = 0;
        while (i > 0) {
            if (procKod.charAt(i) == '(') {
                antal--;
            } else if (procKod.charAt(i) == ')') {
                antal++;
            }
            if (antal == -1) {
                return i;
            }
            i--;
        }
        return 0;
    }

    private static int getPreviousParenAndSkippOrOperator(StringBuffer procKod, int start) {
        int i = start;
        int antal = 0;
        while (i > 0) {
            char c = procKod.charAt(i);
            if (c == '(') {
                antal--;
            } else if (procKod.charAt(i) == ')') {
                antal++;
            } else if (c == '|' || c == '&') {
                return i;
            }
            if (antal == -1) {
                return i;
            }
            i--;
        }
        return 0;
    }
//var bla=color(255,0,0);
//
    //var rod = color(0, 0, 255);
//var boll = new Boll();

    private static void moveInit(StringBuffer procKod) {

        //Vi måste börja med att hitta första funktionen
        Pattern pattern = Pattern.compile("\\s*function\\s+[A-Za-z0-9]+\\s*\\(");
        Matcher m = pattern.matcher(procKod);
        m.find();
        int slutVarDel = m.start();
        String varDeclCode = procKod.substring(0, slutVarDel);
        System.out.println("varDeclCode = " + varDeclCode);
        //var\s+([A-Za-z0-9]+\s*=[\\s*new]?\s*[A-Za-z0-9]+\s*\()

        pattern = Pattern.compile("var\\s+([A-Za-z0-9]+\\s*)(=(\\s*new)?\\s*[A-Za-z0-9]+\\s*\\(.+;)");
        StringBuffer varDelBuf = new StringBuffer(varDeclCode);
        m = pattern.matcher(varDelBuf);
        int end = 0;
        String initStr = "\n";
        while (m.find(end)) {
            end = m.end();
            System.out.println("moveInit m.group(0) = " + m.group(0));
            System.out.println("moveInit m.group(1) = " + m.group(1));
            System.out.println("moveInit m.group(2) = " + m.group(2));
            initStr += "   " + m.group(1) + m.group(2) + "\n";
            end = end - (m.group(2).length() - 1);

            varDelBuf.delete(m.start(2), m.end(2) - 1);

        }
        System.out.println("varDelBuf = " + varDelBuf);
        procKod.replace(0, slutVarDel, varDelBuf.toString());
        //Nu är det dags att stoppa in strä'ngen på rätt ställe
        System.out.println("moveInit initStr = " + initStr);
        //function\s+setup\s*\(.+{
        // function setup() {

        pattern = Pattern.compile("function\\s+setup\\(.+\\{");
        m = pattern.matcher(procKod);
        if (m.find()) {
            procKod.insert(m.end(), initStr);
        } else {
            System.out.println("moveInit Ingen setupmetod!!!! konstigt ");
        }
    }

    private static void exceptions(StringBuffer procKod) {
        StringBufferUtils.replaceAll(procKod, "// js ", "");

        //Ta bort rader som inte ska vara i js
        Pattern pattern = Pattern.compile("// notjs\\s");
        Matcher m = pattern.matcher(procKod);
        int end = 0;
        while (m.find(end)) {
            int rowStart = procKod.lastIndexOf("\n", m.start()) + 1;
            int rowEnd = procKod.indexOf("\n", m.start());
            procKod.replace(rowStart, rowEnd, "");
            end = rowStart + 1;
        }

        //Ta bort raden innan....
    }
}
