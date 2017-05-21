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
 * @author Jonathan
 */
public class InsertFunctions {

    //Function creatwTwoDimentionalArray = new Function("")
//    function processing2p5jsNew2DArray(var x, y){
//    var arr = new Array(x);
//    for (var i = 0; i < x; i++) {
//       arr[i] = new Array(y);
//    }
    final static public String ArrayListRemove
            = "function processing2p5jsArrayListRemove(arr, index){\n"
            + "  if (typeof(index) === 'number') {\n"
            + "    var retVal = arr[index];\n"
            + "    arr.splice(index,1);\n"
            + "    return retVal;\n"
            + "    \n"
            + "  }\n"
            + "  else{\n"
            + "    for(var i =0; i < arr.length ; i++){\n"
            + "      if(arr[i]==index){\n"
            + "        var retVal = arr[i];\n"
            + "        arr.splice(i,1);\n"
            + "        return retVal;\n"
            + "      }\n"            
            + "    }\n"
            + "  }\n"
            + "}\n";

    final static public String New2DArray
            = "function processing2p5jsNew2DArray(x,y){\n"
            + "    var arr = new Array(x);\n"
            + "    for (var i = 0; i < x; i++) {\n"
            + "       arr[i] = new Array(y);\n"
            + "    }\n"
            + "    return arr;\n"
            + "}\n";
    final static public String New3DArray
            = "function processing2p5jsNew3DArray(x,y,z){\n"
            + "    var arr = new Array(x);\n"
            + "    for (var i = 0; i < x; i++) {\n"
            + "       arr[i] = new Array(y);\n"
            + "       for (var j = 0; j < y; j++) {\n"
            + "          arr[i][j] = new Array(z);\n"
            + "       }\n"
            + "    }\n"
            + "    return arr;\n"
            + "}\n";
    final static public String CompareColors
            = "function processing2p5jsCompareColors(getc, colc){\n"
            + "  print(\"cmpc getc: \" +getc + \" colc: \"+colc); \n"
            + "  var getColor = color(getc);\n"
            + "  var retVal = getColor.toString()==colc.toString();\n"
            + "  print(\"cmpcc retVal: \"+retVal); \n"
            + "  return retVal;\n"
            + "}";

    /*final static public String PVectorLine
            = "function processing2p5jsPVectorLine(p1, p2){\n"
            + "  line(p1.x, p1.y, p2.x, p2.y);\n"
            + "  \n"
            + "}";
     */
    public static void insert(StringBuffer procCode, String func) {
        if (procCode.indexOf(func) == -1) {
            procCode.append("\n");
            procCode.append(func);
        }
    }

    public static String getFunctionName(String func) {

        Pattern pattern = Pattern.compile("function\\s+([a-zA-Z0-9]+\\()");
        Matcher m = pattern.matcher(new StringBuffer(func));
        m.find();
        return m.group(1);
    }

    public static void replaceAndInsertFunctionOld(String func, StringBuffer procKod) {
        Pattern pattern;
        Matcher m;
        String searchStr = ".remove\\(";
        String functionToInsert = InsertFunctions.ArrayListRemove;
        //Om det inte står först på raden...
        pattern = Pattern.compile(func + searchStr);
        m = pattern.matcher(procKod);
        while (m.find()) {
            System.out.println("replaceAndInsertFunction m.group() = " + m.group());
            String functionName = getFunctionName(func);
            System.out.println("replaceAndInsertFunction functionName = " + functionName);
            procKod.replace(m.start(), m.end(), functionName);
//            procKod.replace(m.start(), m.end(), "Processing2p5jsArrayListRemove(");
            InsertFunctions.insert(procKod, functionToInsert);
        }
    }

    public static void replaceAndInsertFunction(String searchStr, StringBuffer procKod, String functionToInsert) {

        String functionName = getFunctionName(functionToInsert);
        replaceAndInsertFunction(searchStr, procKod, functionName, functionToInsert);

    }

    public static void replaceAndInsertFunction(String searchStr, StringBuffer procKod, String functionName, String functionToInsert) {
        Pattern pattern;
        Matcher m;
        //Om det inte står först på raden...
        pattern = Pattern.compile(searchStr);
        m = pattern.matcher(procKod);
        while (m.find()) {
            System.out.println("convertAdd m.group() = " + m.group());
            procKod.replace(m.start(), m.end(), functionName);
            InsertFunctions.insert(procKod, functionToInsert);
        }
    }
}
