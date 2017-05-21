/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessing2p5js.replaceclass;

import proccessing2p5js.StringBufferUtils;

/**
 *
 * @author dahjon
 */
public class ReplaceMethodWithNothing extends ReplaceMethod{

    private String oldMethod;

    public ReplaceMethodWithNothing(String oldMethod) {
        this.oldMethod = oldMethod;
    }

    
    @Override
    int replace(StringBuffer procKod, String objectName) {
        String name1 = objectName+"\\."+oldMethod;
        System.out.println("ReplaceMethodWithNothing replace name1 = " + name1);
        return StringBufferUtils.replaceAll(procKod, name1, "");
        
    }
    
}
