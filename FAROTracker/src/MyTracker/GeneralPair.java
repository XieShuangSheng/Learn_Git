/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

/**
 *
 * @author szhc
 */
public class GeneralPair<E extends Object,F extends Object>{
    private E first;
    private F second;
    
    public GeneralPair() {
        
    }
    public GeneralPair(E first,F second) {
        this.first = first;
        this.second = second;
    }
    
    public E getFirst() {
        return first;
    }
    public void setFirst(E first) {
        this.first = first;
    }
    public F getSecond() {
        return second;
    }
    public void setSecond(F second) {
        this.second = second;
    }
}
