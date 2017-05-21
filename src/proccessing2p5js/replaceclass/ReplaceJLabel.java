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
public class ReplaceJLabel extends ReplaceClassInsertConstructorFunction {

    public ReplaceJLabel() {

        methods = new ReplaceMethod[]{
            new ReplaceMethodToField("getText", "innerHTML"),
            new ReplaceFieldToMethod("setText", "innerHTML")
        };

        name = "JLabel";
        constructorCode
                = "function Processing2p5jsCreateSpan(text=''){    \n"
                + "    var x = document.createElement(\"span\");\n"
                + "    x.innerHTML= text;\n"
                + "    return x;\n"
                + "}\n";

    }

}
