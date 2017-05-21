/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessing2p5js.replaceclass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dahjon
 */
public class ReplaceClass {

    private static ReplaceClass[] replaceCallList = {
      new ReplaceJFrame(),
        new ReplaceJTextField(),
        new ReplaceJLabel(),
        new ReplaceJSlider()
    };
    
    public static void replaceClasses(StringBuffer procCode){
        for (int i = 0; i < replaceCallList.length; i++) {
            ReplaceClass replaceClass = replaceCallList[i];
            replaceClass.replace(procCode);
            
        }
    }
    
    ReplaceMethod[] methods;
    String name;
     String construcorReplacement;
    
    public void replace(StringBuffer procKod) {
        Pattern pattern = Pattern.compile(name+"\\s+(\\w+)\\s*=\\s*new\\s+"+name+"\\s*\\(");
        Matcher m = pattern.matcher(procKod);
        String objectName = null;
        int end=0;
        while (m.find(end)) {
//            System.out.println("ConvertPVector convertCreation m.group() = " + m.group());
            procKod.replace(m.start(), m.end(), construcorReplacement+"(");
            objectName=m.group(1);
            end =m.end() +replaceMethosCalls(procKod, objectName);
        }
    }

     int replaceMethosCalls(StringBuffer procKod, String objectName) {
        int change=0;
        for (int i = 0; i < methods.length; i++) {
            ReplaceMethod method = methods[i];
            change+=method.replace(procKod, objectName);
            
        }
        return change;
    }
}
