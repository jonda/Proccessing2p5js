/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessing2p5js;

/**
 *
 * @author Jonathan
 */
class InsertFunctions {
    
    //Function creatwTwoDimentionalArray = new Function("")
    
    
    class Function {

        public Function(String code) {
            this.code = code;
        }
        
        private String code;
        boolean inserted = false;
        void insert(StringBuffer strbuf){
            if(!inserted){
                inserted=true;
                strbuf.append(code);
            }
        }
    }
}
