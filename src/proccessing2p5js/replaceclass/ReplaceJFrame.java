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
public class ReplaceJFrame extends ReplaceClass {

    public ReplaceJFrame() {

        methods = new ReplaceMethod[]{
            new ReplaceMethodSimple("add", "appendChild"),
            //  frame.setVisible(true);
            new ReplaceMethodWithNothing("setVisible\\(\\w*\\)"),
            //new ReplaceMethodSimpleWithArguments("setVisible\\(\\s*true\\s*\\)", ""),
            //new ReplaceMethodSimpleWithArguments("setVisible\\(\\s*false\\s*\\)", ""),
            //setLayout(new FlowLayout());
            new ReplaceMethodWithNothing("setLayout\\(.*\\)"),
            new ReplaceMethodWithNothing("pack\\(\\s*\\)"),
        };

        name = "JFrame";
        
        construcorReplacement ="document.getElementById('settings')";

    }
    public void replace(StringBuffer procKod) {
        Pattern pattern = Pattern.compile(name+"\\s+(\\w+)\\s*=\\s*new\\s+"+name+"\\s*\\(\\s*\\)");
        Matcher m = pattern.matcher(procKod);
        String objectName = null;
         int end=0;
       while (m.find(end)) {
//            System.out.println("ConvertPVector convertCreation m.group() = " + m.group());
            objectName=m.group(1);
            System.out.println("ReplaceJFrame replace objectName = " + objectName);
            procKod.replace(m.start(), m.end(),"var "+ objectName + "="+construcorReplacement);
            end =m.end() +replaceMethosCalls(procKod, objectName);
        }
    }

}

