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
public class StringBufferUtils {


    public static int  replaceAll(StringBuffer procKod, String serachStr, String replStr) {
        Pattern pattern = Pattern.compile(serachStr);
        Matcher m = pattern.matcher(procKod);
        int end=0;
        int forandring=0;
        while (m.find(end)) {
            System.out.println("replaceAll m.group() = " + m.group());
            procKod.replace(m.start(), m.end(), replStr);
            end=m.start()+replStr.length();
            forandring = (m.end()-m.start())-replStr.length();
        }
        return forandring;
    }    
}
