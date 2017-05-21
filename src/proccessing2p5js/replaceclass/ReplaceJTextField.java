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
public class ReplaceJTextField extends ReplaceClassInsertConstructorFunction {

    public ReplaceJTextField() {

        methods = new ReplaceMethod[]{
            new ReplaceMethodToField("getText", "value")
        };

        name = "JTextField";
    constructorCode=
            "\nfunction Processing2p5jsCreateInputText(text=''){    \n"
            + "var x = document.createElement(\"INPUT\");\n"
            + "    x.setAttribute(\"type\", \"text\");\n"
            + "    x.setAttribute(\"value\", text);\n"
            + "    return x;\n"
            + "}\n";
        
    
    }


}
