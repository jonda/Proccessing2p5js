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
public class ReplaceMethodSimpleWithArguments extends ReplaceMethod{

    private String oldMethod;
    private String newMethod;

    public ReplaceMethodSimpleWithArguments(String oldMethod, String newMethod) {
        this.oldMethod = oldMethod;
        this.newMethod = newMethod;
    }

    
    @Override
    int  replace(StringBuffer procKod, String name) {
        return StringBufferUtils.replaceAll(procKod, oldMethod, newMethod);
        
    }
    
}
