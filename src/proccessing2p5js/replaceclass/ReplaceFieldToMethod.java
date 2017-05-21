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
public class ReplaceFieldToMethod extends ReplaceMethod{

    private String oldMethod;
    private String fieldName;

    public ReplaceFieldToMethod(String oldMethod, String fieldName) {
        this.oldMethod = oldMethod;
        this.fieldName = fieldName;
    }

    //  lblNyStorlek.setText(""+storlek);

    @Override
    int  replace(StringBuffer procKod, String objectName) {
        String searchStr = objectName+"\\."+oldMethod+"\\s*\\(([\\w\\.\"'\\+]+)\\)";
        System.out.println("ReplaceFieldToMethod searchStr = " + searchStr);
        Pattern pattern = Pattern.compile(searchStr);
        Matcher m = pattern.matcher(procKod);
         int end=0;
         int forendring = 0;
       while (m.find(end)) {
//            System.out.println("ConvertPVector convertCreation m.group() = " + m.group());
            String param=m.group(1);
            System.out.println("ReplaceFieldToMethod replace param = " + param);
            String newStatement = objectName + "."+fieldName+"="+param;
            System.out.println("ReplaceFieldToMethod newStatement = " + newStatement);
            procKod.replace(m.start(), m.end(), newStatement);
            forendring =(m.end()-m.start())+newStatement.length();
            end=m.end()-forendring;
        }        
       return forendring;
    }
    
}
