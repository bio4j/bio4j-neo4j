/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.era7.bioinfo.bioinfoutil;

/**
 * 
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 * @param <A>
 * @param <B>
 */
public class Pair<A, B> {
    private A value1;
    private B value2;

    public Pair(A value1, B value2) {
        super();
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public int hashCode() {
        int hashValue1 = value1 != null ? value1.hashCode() : 0;
        int hashValue2 = value2 != null ? value2.hashCode() : 0;

        return (hashValue1 + hashValue2) * hashValue2 + hashValue1;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Pair) {
                Pair otherPair = (Pair) other;
                return
                ((  this.value1 == otherPair.value1 ||
                        ( this.value1 != null && otherPair.value1 != null &&
                          this.value1.equals(otherPair.value1))) &&
                 (      this.value2 == otherPair.value2 ||
                        ( this.value2 != null && otherPair.value2 != null &&
                          this.value2.equals(otherPair.value2))) );
        }

        return false;
    }

    @Override
    public String toString()
    {
           return "(" + value1 + ", " + value2 + ")";
    }

    public A getValue1() {
        return value1;
    }

    public void setValue1(A value1) {
        this.value1 = value1;
    }

    public B getValue2() {
        return value2;
    }

    public void setValue2(B value2) {
        this.value2 = value2;
    }
}
