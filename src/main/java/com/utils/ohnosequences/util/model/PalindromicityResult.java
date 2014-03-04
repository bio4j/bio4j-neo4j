/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ohnosequences.util.model;

/**
 *
 * @author ppareja
 */
public class PalindromicityResult {

    protected String result;
    protected String resultMaxWordLength;
    protected String patterns;

    public PalindromicityResult(String result, String patterns) {
        this.result = result;
        this.patterns = patterns;
    }

    public PalindromicityResult(String result, String resultMaxWordLength, String patterns) {
        this.result = result;
        this.resultMaxWordLength = resultMaxWordLength;
        this.patterns = patterns;
    }

    public PalindromicityResult() {
    }

    public String getPatterns() {
        return patterns;
    }

    public void setPatterns(String patterns) {
        this.patterns = patterns;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultMaxWordLength() {
        return resultMaxWordLength;
    }

    public void setResultMaxWordLength(String resultMaxWordLength) {
        this.resultMaxWordLength = resultMaxWordLength;
    }

    @Override
    public String toString(){
        return "result: " + result + "\npatterns: " + patterns + "\nresultMaxWordLength: " + resultMaxWordLength;
    }

}
