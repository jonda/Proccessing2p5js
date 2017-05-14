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
public class ConvertClass {

    static String ex = "public class Boll {\n"
            + "  float bollx;\n"
            + "  float y;\n"
            + "  float r;  \n"
            + "  Boll(float x, float y, float r) {\n"
            + "    this.x=x;\n"
            + "    this.y=y;\n"
            + "    this.r=r;\n"
            + "  }\n"
            + "  \n"
            + "int Kalle;\n"
            + "  void flytta(float speedx, float speedy){\n"
            + "     float a;\n"
            + "    bollx = bollx + speedx;\n"
            + "    y = y + speedy;\n"
            + "  }\n"
            + "  void rita() {\n"
            + "    fill(0,0,255);\n"
            + "    ellipse(bollx, y, r, r);\n"
            + "  }\n"
            + "  \n"
            + "\n"
            + "}";

    public static ArrayList<String> convertClasses(StringBuffer procKod) {
//        String patternStr = "(public|private)? class \\s+([a-zA-Z0-9]+)\\([ ,.a-zA-Z0-9]*\\)\\s*\\{";
        ArrayList<String> classList = new ArrayList<String>();
        String patternStr = "(private|public)?\\s*class\\s+([a-zA-Z0-9]+)\\s*\\{";

        Pattern pattern = Pattern.compile(patternStr);
        Matcher m = pattern.matcher(procKod);
        int end = 0;
        while (m.find(end)) {
            end = m.end();
            int start = m.start();
            //end=procKod.indexOf("{",end)
            end = passCurlyBrackets(procKod, end - 1);
            String classCode = procKod.substring(start, end);
            classList.add(getClassName(classCode.trim()));
            System.out.println("classCode = " + classCode);
            String newCode = convertClass(classCode);
            procKod.replace(start, end, newCode);
            end = end + newCode.length() - classCode.length();
            System.out.println("convertClasses end = " + end + "procCode.length(): " + procKod.length());
        }
        if (end == 0) {
            System.out.println("Ingen klass hittad");
        }
        return classList;
    }

    public static String convertClass(String code) {

        code = code.trim();
        code = removeIfFirst(code, "public");
        code = removeIfFirst(code, "private");
        StringBuffer codeBuf = new StringBuffer(code);
        ArrayList<String> members = getAndDeleteMembers(codeBuf);
        String initCode = getMembersAndInit(codeBuf, members);//Obs lägger till i members
        System.out.println("convertClass initCode = " + initCode);
        //fixMemberVariablesinMethods(codeBuf);
        code = codeBuf.toString();
        System.out.println("members = " + members);
        code = code.trim();
        String className = getClassName(code);
        System.out.println("convertClass className = " + className);
        code = changeConstructor(code, className, members, initCode);
        code = convertFunctions(new StringBuffer(code), members);
        //code = removeMembers(code);
        return code;
    }

    /*private static void fixMemberVariablesinMethods(StringBuffer codeBuf){
        Pattern function = Pattern.compile(patternStr);
    }*/
    private static ArrayList<String> getAndDeleteMembers(StringBuffer codeBuf) {

        ArrayList<String> mem = new ArrayList<>();

        String patternStr = "(void|color|int|float|double|long|String|StringBuffer|char|byte)\\s+([a-zA-Z]+)\\s*;";
        Pattern function = Pattern.compile(patternStr);

        Matcher m = function.matcher(codeBuf);
        int end = codeBuf.indexOf("{") + 1;
        while (m.find(end)) {
            //int start = funcm.start();

            //String funcname = funcm.group(2);
            String varNamn = m.group(2);
            //System.out.println("m.group(0) = " + m.group(0));
            int nextCurly = codeBuf.indexOf("{", end);
            //System.out.println("nextCurly = " + nextCurly);
            //System.out.println("m.start() = " + m.start());
            if (nextCurly < m.start()) {
                end = passCurlyBrackets(codeBuf, nextCurly);
            } else {
                // System.out.println("varNamn = " + varNamn);
                mem.add(varNamn);
                codeBuf.delete(m.start(), m.end());
                end = m.end();
                end -= m.group().length();

            }
        }

        return mem;
    }
    private static String getMembersAndInit(StringBuffer codeBuf, ArrayList<String> mem) {


        String patternStr = "(void|color|int|float|double|long|String|StringBuffer|char|byte)\\s+([a-zA-Z]+)(\\s*=\\s*.+;)";
        Pattern function = Pattern.compile(patternStr);
        String initCode="";
        Matcher m = function.matcher(codeBuf);
        int end = codeBuf.indexOf("{") + 1;
        while (m.find(end)) {
            //int start = funcm.start();

            //String funcname = funcm.group(2);
            String varNamn = m.group(2);
//            String init = "this."+varNamn+m.group(3);
            String init = varNamn+m.group(3);
            //System.out.println("m.group(0) = " + m.group(0));
            System.out.println("getAndDeleteMembers init = " + init);
            int nextCurly = codeBuf.indexOf("{", end);
            //System.out.println("nextCurly = " + nextCurly);
            //System.out.println("m.start() = " + m.start());
            if (nextCurly < m.start()) {
                end = passCurlyBrackets(codeBuf, nextCurly);
            } else {
                // System.out.println("varNamn = " + varNamn);
                mem.add(varNamn);
                codeBuf.delete(m.start(), m.end());
                end = m.end();
                end -= m.group().length();
                initCode+="    "+init+"\n";

            }
        }

        return initCode;
    }

    private static int passCurlyBrackets(StringBuffer strBuf, int start) {
        //System.out.println("passCurlyBrackets");
        while (strBuf.charAt(start) == ' ' || strBuf.charAt(start) == '\t'
                || strBuf.charAt(start) == '\n') {
            if ((start - 1) < strBuf.length()) {
                start++;
                //System.out.println("strBuf.charAt(start) = " + strBuf.charAt(start));
            } else {
                break;
            }
        }
        int nr = 0;
        //System.out.println("passCurlyBrackets start: " + start + " strBuf.length(): " + strBuf.length());
        for (int i = start; i < strBuf.length(); i++) {
            //System.out.println("i loop strBuf.charAt(" + i + ") = " + strBuf.charAt(i));
            if (strBuf.charAt(i) == '{') {
                nr++;
                //System.out.println("hittat { nr: " + nr + " i: " + i);
            } else if (strBuf.charAt(i) == '}') {
                nr--;
                //System.out.println("hittat } nr: " + nr + " i:" + i);
                if (nr == 0) {
                    return i + 1;
                }
            }

        }
        return start;
    }
    private static void insertInitCode(StringBuffer codeBuf, int start,  String initCode) {
        int startBody = codeBuf.indexOf("{", start) + 1;
        codeBuf.insert(startBody+1, "\n"+initCode);
    }
    private static String changeConstructor(String code, String className, ArrayList<String> members, String initCode) {
        String searchString = className + "\\(";
        System.out.println("changeConstructorName searchString = " + searchString);
        code = code.replaceFirst(searchString, "constructor(");
        //String pStr = "(void|int|float|double|long|String|StringBuffer|char|byte)";
        StringBuffer procKod = new StringBuffer(code);
        String functionPatternStr = "constructor\\([ ,.a-zA-Z0-9]*\\)";
        Pattern functionPattern = Pattern.compile(functionPatternStr);
        Matcher consm = functionPattern.matcher(procKod);
        int end = 0;
        if (consm.find(end)) {
            int start = consm.start();
            end = consm.end();
            //String funcname = funcm.group(2);
            String funcExp = consm.group(0);
            funcExp = funcExp.replaceAll("(void|color|int|float|double|long|String|StringBuffer|char|byte)", "");
            procKod.replace(start, end, funcExp);

            String[] paramList = getFunctionParameters(funcExp);
            System.out.print("changeConstructorName paramList = ");
            printArray(paramList);
            // procKod.replace(start, end, funcExp);
            end = start + funcExp.length();
            insertInitCode(procKod, end, initCode);
            System.out.println("changeConstructor efter insertIninitCode procKod = " + procKod);
            fixMemberVariablesinMethods(procKod, end, paramList, members);
        }
        if (end == 0) {
            System.out.println("Ingen konstriktor/constructor");
        }
        return procKod.toString();

    }

    /* private static String removeMembers(String code) {
        code = code.replaceAll("(void|color|int|float|double|long|String|StringBuffer|char|byte)\\s+[a-zA-Z]+;", "");
        return code;
    }*/
    private static String[] getFunctionParameters(String funcExp) {

        System.out.println("funcExp = " + funcExp);
        int leftParanIndex = funcExp.indexOf('(');
        System.out.println("leftParanIndex = " + leftParanIndex);
        int rightParanIndex = funcExp.indexOf(')');
        System.out.println("rightParanIndex = " + rightParanIndex);
        String params = funcExp.substring(leftParanIndex + 1, rightParanIndex);
        params = params.replaceAll(" ", "");
        System.out.println("params = " + params);
        String[] paramList = params.split(",");
        return paramList;
    }

    private static String replaceVar(String code, String var, String repl) {
        System.out.println("repl = " + repl);
        System.out.println("var = " + var);
        System.out.println("replaceVar code = " + code);
        //        String patternStr = "(public|private)? class \\s+([a-zA-Z0-9]+)\\([ ,.a-zA-Z0-9]*\\)\\s*\\{";
        String patternStr = var;
        StringBuffer codeBuf = new StringBuffer(code);

        Pattern pattern = Pattern.compile(patternStr);
        Matcher m = pattern.matcher(codeBuf);
        int end = 0;
        while (m.find(end)) {
            int start = m.start();
            end = m.end();
            if (!Character.isLetter(codeBuf.charAt(start - 1))
                    && !Character.isLetter(codeBuf.charAt(end))) {
                codeBuf.replace(start, end, repl);
                end = end + repl.length() - var.length();
            }
        }
        System.out.println("replaceVar codeBuf = " + codeBuf);
        return codeBuf.toString();
    }
//    private static String replaceVar(String code,String var,String repl){
//        System.out.println("repl = " + repl);
//        System.out.println("var = " + var);
//        System.out.println("replaceVar code = " + code);
//        //        String patternStr = "(public|private)? class \\s+([a-zA-Z0-9]+)\\([ ,.a-zA-Z0-9]*\\)\\s*\\{";
//        String patternStr = "(\\s<>&|\\+-\\*/)("+var+")(\\s|;=<>&|\\+-\\*/)";
//        StringBuffer ocdeBuf=new StringBuffer(code);
//        
//        Pattern pattern = Pattern.compile(patternStr);
//        Matcher m = pattern.matcher(ocdeBuf);
//        int end = 0;
//        while (m.find(end)) {
//            end=m.end();
//            ocdeBuf.replace(m.start(2), m.end(2), repl);
//            end=end+repl.length()-var.length();
//        }
//        System.out.println("replaceVar codeBuf = " + ocdeBuf);
//        return ocdeBuf.toString();
//    }
    //verkar på kommande block!!

    private static void fixMemberVariablesinMethods(StringBuffer codeBuf, int start, String[] paramList, ArrayList<String> members) {
        int startBody = codeBuf.indexOf("{", start) + 1;
        System.out.println("fixMemberVariablesinMethods leftCurly = " + startBody);
//        int endBody = codeBuf.indexOf("}", startBody) - 1;
        int endBody = passCurlyBrackets(codeBuf, startBody - 1);
        System.out.println("fixMemberVariablesinMethods rightCurly = " + endBody);
        String codeBody = codeBuf.substring(startBody, endBody);
        System.out.println("codeBody = " + codeBody);
        for (int j = 0; j < members.size(); j++) {
            boolean lika = false;
            String mem = members.get(j);
            for (int i = 0; i < paramList.length; i++) {
                String param = paramList[i];
                if (param.equals(mem)) {
                    lika = true;
                }
            }
            if (!lika) {
                //codeBody = codeBody.replaceAll(mem, "this." + mem);
                codeBody = replaceVar(codeBody, mem, "this." + mem);
            }
        }
        codeBuf.replace(startBody, endBody, codeBody);
        System.out.println("efter repl codeBody = " + codeBody);
    }

    public static String convertFunctions(StringBuffer procKod, ArrayList<String> members) {
        //        String functionPatternStr = "([a-zA-Z0-9]+)\\[([ ,.a-zA-Z0-9]+)\\]";
        String functionPatternStr = "(void|int|float|double|long|String|StringBuffer|char|byte)\\s+([a-zA-Z0-9]+)\\([ ,.a-zA-Z0-9]*\\)";
        Pattern functionPattern = Pattern.compile(functionPatternStr);
        Matcher funcm = functionPattern.matcher(procKod);
        int end = 0;
        while (funcm.find(end)) {
            int start = funcm.start();
            end = funcm.end();
            //String funcname = funcm.group(2);
            String funcExp = funcm.group(0);
            System.out.println("funcExp = " + funcExp);
            funcExp = funcExp.replaceAll("(void|color|int|float|double|long|String|StringBuffer|char|byte)", "");
            String[] paramList = getFunctionParameters(funcExp);
            System.out.print("paramList = ");
            printArray(paramList);
            procKod.replace(start, end, funcExp);
            end = start + funcExp.length();
            fixMemberVariablesinMethods(procKod, end, paramList, members);
            //end borde kanske korrigeras.....

        }
        if (end == 0) {
            System.out.println("Ingen matchning på funktion");
        }
        return procKod.toString();
    }

    public static void printArray(String[] paramList) {
        for (int i = 0; i < paramList.length; i++) {
            String item = paramList[i];

            System.out.print(item + ", ");
        }
        System.out.println();
    }

    private static String removeIfFirst(String str, String remove) {
        if (str.indexOf(remove) == 0) {
            str = str.substring(remove.length(), str.length());
        }
        return str;
    }

    private static String getClassName(String code) {
        int start = -1;
        for (int i = "class".length(); i < code.length() && (start == -1); i++) {
            if (code.charAt(i) != ' ') {
                start = i;
            }
        }
        //System.out.println("getClassName start = " + start);
        int end = -1;
        for (int i = "class".length() + start; i < code.length() && (end == -1); i++) {
            if (code.charAt(i) == ' ' || code.charAt(i) == '{') {
                end = i;
            }
        }
        //System.out.println("getClassName end = " + end);
        String name = code.substring(start, end);
        //System.out.println("getClassName name = '" + name + "'");
        name=name.trim();
        return name;
    }

    public static void main(String[] args) {
        System.out.println(convertClass(ex));
    }

}
