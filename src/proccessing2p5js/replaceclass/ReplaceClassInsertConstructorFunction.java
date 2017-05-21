/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessing2p5js.replaceclass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import proccessing2p5js.InsertFunctions;

/**
 *
 * @author dahjon
 */
public class ReplaceClassInsertConstructorFunction extends ReplaceClass {

     public  String constructorCode;
    public ReplaceClassInsertConstructorFunction() {

        methods = new ReplaceMethod[]{
            new ReplaceMethodToField("getText", "value")
        };

        name = "JTextField";
        

    }
    
    @Override
    public void replace(StringBuffer procKod) {
        construcorReplacement = InsertFunctions.getFunctionName(constructorCode);
        System.out.println("ReplaceClassInsertConstructorFunction "+" name: "+name+"constructorCode = " + constructorCode );
        Pattern pattern = Pattern.compile(name+"\\s+(\\w+)\\s*=\\s*new\\s+"+name+"\\s*\\(");
        Matcher m = pattern.matcher(procKod);
        String objectName = null;
        while (m.find()) {
//            System.out.println("ConvertPVector convertCreation m.group() = " + m.group());
            objectName=m.group(1);
            procKod.replace(m.start(), m.end(),"var " + objectName+"="+ construcorReplacement);
            replaceMethosCalls(procKod, objectName);
            InsertFunctions.insert(procKod, constructorCode);
        }
    }

}
