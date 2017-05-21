/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessing2p5js.replaceclass;

/**
 *
 * @author dahjon
 */
public class ReplaceMethodAndInsert extends ReplaceMethod{

    private String oldMethod;
    private String newMethod;

    public ReplaceMethodAndInsert(String oldMethod, String newMethod) {
        this.oldMethod = oldMethod;
        this.newMethod = newMethod;
    }


    @Override
    int replace(StringBuffer procKod, String name) {
        return 0;
    }
    
}
