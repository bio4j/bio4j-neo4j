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
public class Entry<A, B> {
    private A key;
    private B value;

    public Entry(A key, B value) {
        super();
        this.key = key;
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hashKey = key != null ? key.hashCode() : 0;
        int hashValue = value != null ? value.hashCode() : 0;

        return (hashKey + hashValue) * hashValue + hashKey;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Entry) {
                Entry otherEntry = (Entry) other;
                return
                ((  this.key == otherEntry.key ||
                        ( this.key != null && otherEntry.key != null &&
                          this.key.equals(otherEntry.key))) &&
                 (      this.value == otherEntry.value ||
                        ( this.value != null && otherEntry.value != null &&
                          this.value.equals(otherEntry.value))) );
        }

        return false;
    }

    @Override
    public String toString()
    {
           return "(" + key + ", " + value + ")";
    }

    public A getKey() {
        return key;
    }

    public void setKey(A key) {
        this.key = key;
    }

    public B getValue() {
        return value;
    }

    public void setValue(B value) {
        this.value = value;
    }

}
