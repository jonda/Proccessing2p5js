/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessing2p5js.replaceclass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import proccessing2p5js.StringBufferUtils;

/**
 *
 * @author dahjon
 */
public class ReplaceJSlider extends ReplaceClassInsertConstructorFunction {

    public ReplaceJSlider() {

        methods = new ReplaceMethod[]{
            //new ReplaceMethodToField("getValue", "value")
            new ReplaceSliderValue()
            
        };

        name = "JSlider";
        constructorCode
                = "\nfunction Processing2p5jsCreateInputRange(min=0, max=100, value=50){    \n"
                + "var elem = document.createElement(\"INPUT\");\n"
                + "    elem.setAttribute(\"type\", \"range\");\n"
                + "    elem.setAttribute(\"min\", min);\n"
                + "    elem.setAttribute(\"max\", max);\n"
                + "    elem.setAttribute(\"value\", value);\n"
                + "    return elem;\n"
                + "}\n";

    }

    class ReplaceSliderValue extends ReplaceMethod {

        @Override
        int replace(StringBuffer procKod, String objectName) {
            String name1 = objectName + "\\.getValue\\(.*\\)";
            System.out.println("ReplaceClassInsertConstructorFunction replace name1 = " + name1);
            //return StringBufferUtils.replaceAll(procKod, name1, "int("+objectName+".value)");
            return StringBufferUtils.replaceAll(procKod, name1, "Number("+objectName+".value)");

        }

    }

}
