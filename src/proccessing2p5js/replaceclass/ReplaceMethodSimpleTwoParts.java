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
public class ReplaceMethodSimpleTwoParts extends ReplaceMethod{

    private String oldMethod;
    private String newMethodPart1;
    private String newMethodPart2;

    public ReplaceMethodSimpleTwoParts(String oldMethod, String newMethodPart1, String newMethodPart2) {
        this.oldMethod = oldMethod;
        this.newMethodPart1 = newMethodPart1;
        this.newMethodPart2 = newMethodPart2;
    }



    
    @Override
    int  replace(StringBuffer procKod, String name) {
        return 0;
    }
    
}
