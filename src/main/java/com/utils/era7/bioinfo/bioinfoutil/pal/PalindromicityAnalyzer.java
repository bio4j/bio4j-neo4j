package com.era7.bioinfo.bioinfoutil.pal;

import com.era7.bioinfo.bioinfoutil.BitOperations;
import com.era7.bioinfo.bioinfoutil.model.PalindromicityResult;
import com.era7.bioinfoxml.pal.PalindromicityResultXML;

public class PalindromicityAnalyzer {

    
    public static String DEFAULT_SEPARATOR = ",";

    
    public PalindromicityResultXML analyzeOddAxisPalindromicityXML(
                                        int windowLength,
                                        String seq,
                                        boolean onlyWholeWindows,
                                        boolean withPatterns,
                                        boolean withMaxWordLength) throws Exception {

        PalindromicityResultXML temp = new PalindromicityResultXML();
        PalindromicityResult palResult = analyzeOddAxisPalindromicity(windowLength, seq, onlyWholeWindows, withPatterns,withMaxWordLength);
        temp.setResult(palResult.getResult());
        if(withPatterns){
            temp.setPatterns(palResult.getPatterns());
        }
        if(withMaxWordLength){
            temp.setResultMaxWordLength(palResult.getResultMaxWordLength());
        }
        temp.setSeparator(DEFAULT_SEPARATOR);
        temp.setAxis(PalindromicityResultXML.ODD_AXIS);

        return temp;
    }
    /**
     * Analyzes the palindromicity with an odd axis of the sequence provided
     * Letters must be lower-case
     * @param windowLength Length of the window used
     * @param seq Sequence to be analyzed
     * @return The result of the palindromic analysis
     */
    public PalindromicityResult analyzeOddAxisPalindromicity(
                                        int windowLength,
                                        String seq,
                                        boolean onlyWholeWindows,
                                        boolean withPatterns,
                                        boolean withMaxWordLength) throws Exception{
        if(windowLength < 2){
            throw new Exception("Window length must be greater than 2");
        }

        PalindromicityResult palResult = new PalindromicityResult();
        StringBuilder result = new StringBuilder(seq.length());
        StringBuilder patterns = null;
        StringBuilder resultMax = new StringBuilder(seq.length());
        boolean[] patternArray = null;

        if(withPatterns){
            patterns = new StringBuilder(seq.length());
            patternArray = new boolean[windowLength/2];
            for (int i = 0; i < patternArray.length; i++) {
                patternArray[i] = false;
            }
        }

        if(withMaxWordLength){
            resultMax = new StringBuilder(seq.length());
        }

        int i;
        int limit;
        if(onlyWholeWindows){
            i = windowLength/2;
            limit = seq.length() - 1 - (windowLength/2);
        }else{
            i = 1;
            limit = seq.length() - 2;
        }


        for (; i <= limit; i++) {

            int counter = 0;
            int counterMax = 0;
            int maxWordLength = 0;
            char[] value1 = new char[1];
            char[] value2 = new char[1];

            for (int j = 1; j <= windowLength / 2 && (i + j) < seq.length() && (i - j) >= 0; j++) {

                seq.getChars(i - j, i - j + 1, value1, 0);
                seq.getChars(i + j, i + j + 1, value2, 0);

                if (areComplementaryCommon(value1[0], value2[0])) {
                    counter++;
                    if(withPatterns){                        
                        patternArray[(windowLength/2)-j] = true;
                    }
                    if(withMaxWordLength){
                        counterMax++;
                        if (counterMax > maxWordLength) {
                            maxWordLength = counterMax;
                        }
                    }
                }else{
                    if(withMaxWordLength){
                        counterMax = 0;
                    }
                }
            }

            result.append((counter + DEFAULT_SEPARATOR));

            if(withMaxWordLength){
                resultMax.append((maxWordLength + DEFAULT_SEPARATOR));
            }

            if(withPatterns){
                int patternCode = BitOperations.convertToEquivalentInt(patternArray);
                patterns.append((patternCode + DEFAULT_SEPARATOR));
            }
        }

        palResult.setResult(result.substring(0, result.length() - 1));
        if(withPatterns){
            palResult.setPatterns(patterns.substring(0, patterns.length() - 1));
        }
        if(withMaxWordLength){
            palResult.setResultMaxWordLength(resultMax.substring(0, resultMax.length() - 1));
        }

        return palResult;
    }


    public PalindromicityResultXML analyzeMaxWordLengthOddAxisPalindromicityXML(
                                        int windowLength,
                                        String seq,
                                        boolean onlyWholeWindows,
                                        boolean withPatterns) throws Exception {

        PalindromicityResultXML temp = new PalindromicityResultXML();
        PalindromicityResult palResult = analyzeMaxWordLengthOddAxisPalindromicity(windowLength, seq, onlyWholeWindows, withPatterns);
        temp.setResultMaxWordLength(palResult.getResultMaxWordLength());
        if(withPatterns){
            temp.setPatterns(palResult.getPatterns());
        }
        temp.setSeparator(DEFAULT_SEPARATOR);
        temp.setAxis(PalindromicityResultXML.ODD_AXIS);
        return temp;
    }

    /**
     * Analyzes the maximum contiguous word length palindromicity with an odd axis for
     * the sequence provided
     * <p>Letters must be lower-case
     * @param windowLength Length of the window used
     * @param seq Sequence to be analyzed
     * @return The result of the palindromic analysis
     */
    public PalindromicityResult analyzeMaxWordLengthOddAxisPalindromicity(
                                        int windowLength,
                                        String seq,
                                        boolean onlyWholeWindows,
                                        boolean withPatterns) throws Exception {

        if(windowLength < 2){
            throw new Exception("Window length must be greater than 2");
        }

        
        PalindromicityResult palResult = new PalindromicityResult();
        StringBuilder result = new StringBuilder(seq.length());
        StringBuilder patterns = null;
        boolean[] patternArray = null;

        if(withPatterns){
            patterns = new StringBuilder(seq.length());
            patternArray = new boolean[windowLength/2];
            for (int i = 0; i < patternArray.length; i++) {
                patternArray[i] = false;
            }
        }

        int i;
        int limit;
        if(onlyWholeWindows){
            i = windowLength/2;
            limit = seq.length() - 1 - (windowLength/2);
        }else{
            i = 1;
            limit = seq.length() - 2;
        }

        for (; i <= limit; i++) {

            int counter = 0;
            char[] value1 = new char[1];
            char[] value2 = new char[1];

            int maxWordLength = 0;

            for (int j = 1; j <= windowLength / 2 && (i + j) < seq.length() && (i - j) >= 0; j++) {

                seq.getChars(i - j, i - j + 1, value1, 0);
                seq.getChars(i + j, i + j + 1, value2, 0);

                if (areComplementaryCommon(value1[0], value2[0])) {
                    counter++;
                    if (counter > maxWordLength) {
                        maxWordLength = counter;
                    }
                    if(withPatterns){
                        patternArray[(windowLength/2)-j] = true;
                    }
                } else {
                    counter = 0;
                }
            }

            result.append((maxWordLength + DEFAULT_SEPARATOR));
            if(withPatterns){
                int patternCode = BitOperations.convertToEquivalentInt(patternArray);
                patterns.append((patternCode + DEFAULT_SEPARATOR));
            }
        }       

        palResult.setResult(result.substring(0, result.length() - 1));
        if(withPatterns){
            palResult.setPatterns(patterns.substring(0, patterns.length() - 1));
        }

        return palResult;
    }


    public PalindromicityResultXML analyzeMaxWordLengthEvenAxisPalindromicityXML(
                                        int windowLength,
                                        String seq,
                                        boolean onlyWholeWindows,
                                        boolean withPatterns) throws Exception{

        PalindromicityResultXML temp = new PalindromicityResultXML();
        PalindromicityResult palResult = analyzeMaxWordLengthEvenAxisPalindromicity(windowLength, seq, onlyWholeWindows,withPatterns);
        temp.setResultMaxWordLength(palResult.getResultMaxWordLength());
        if(withPatterns){
            temp.setPatterns(palResult.getPatterns());
        }
        temp.setSeparator(DEFAULT_SEPARATOR);
        temp.setAxis(PalindromicityResultXML.EVEN_AXIS);

        return temp;

    }


    /**
     * Analyzes the palindromicity with an even axis of the sequence provided
     * Letters must be lower-case
     * @param windowLength Length of the window used
     * @param seq Sequence to be analyzed
     * @return The result of the palindromic analysis
     */
    public PalindromicityResult analyzeMaxWordLengthEvenAxisPalindromicity(
                                        int windowLength,
                                        String seq,
                                        boolean onlyWholeWindows,
                                        boolean withPatterns) throws Exception {

        if(windowLength < 2){
            throw new Exception("Window length must be greater than 2");
        }

        PalindromicityResult palResult = new PalindromicityResult();
        StringBuilder result = new StringBuilder(seq.length());
        StringBuilder patterns = null;
        boolean[] patternArray = null;

        if(withPatterns){
            patterns = new StringBuilder(seq.length());
            patternArray = new boolean[windowLength/2];
            for (int i = 0; i < patternArray.length; i++) {
                patternArray[i] = false;
            }
        }

        int i;
        int limit;
        if(onlyWholeWindows){
            i = (windowLength/2) - 1;
            limit = seq.length() - (windowLength/2);
        }else{
            i = 0;
            limit = seq.length() - 1;
        }

        for (; i < limit; i++) {

            int counter = 0;
            char[] value1 = new char[1];
            char[] value2 = new char[1];

            int maxWordLength = 0;

            for (int j = 0; j < windowLength / 2 && (i + j + 1) < seq.length() && (i - j) >= 0; j++) {

                seq.getChars(i - j, i - j + 1, value1, 0);
                seq.getChars(i + j + 1, i + j + 2, value2, 0);

                if (areComplementaryCommon(value1[0], value2[0])) {
                    counter++;
                    if (counter > maxWordLength) {
                        maxWordLength = counter;
                    }
                    if(withPatterns){
                        patternArray[(windowLength/2)-1-j] = true;
                    }
                } else {
                    counter = 0;
                }

            }

            result.append((maxWordLength + DEFAULT_SEPARATOR));
            if(withPatterns){                
                int patternCode = BitOperations.convertToEquivalentInt(patternArray);
                patterns.append((patternCode + DEFAULT_SEPARATOR));
            }


        }       

        palResult.setResultMaxWordLength(result.substring(0, result.length() - 1));
        if(withPatterns){
            palResult.setPatterns(patterns.substring(0, patterns.length() - 1));
        }
        
        return palResult;
    }


    public PalindromicityResultXML analyzeEvenAxisPalindromicityXML(
                                        int windowLength,
                                        String seq,
                                        boolean onlyWholeWindows,
                                        boolean withPatterns,
                                        boolean withMaxWordLength) throws Exception {
        
        PalindromicityResultXML temp = new PalindromicityResultXML();
        PalindromicityResult palResult = analyzeEvenAxisPalindromicity(windowLength,
                                        seq, onlyWholeWindows,withPatterns,withMaxWordLength);
        temp.setResult(palResult.getResult());
        if(withPatterns){
            temp.setPatterns(palResult.getPatterns());
        }
        if(withMaxWordLength){
            temp.setResultMaxWordLength(palResult.getResultMaxWordLength());
        }
        temp.setSeparator(DEFAULT_SEPARATOR);
        temp.setAxis(PalindromicityResultXML.EVEN_AXIS);

        return temp;
    }

    /**
     * Analyzes the palindromicity with an even axis of the sequence provided
     * Letters must be lower-case
     * @param windowLength Length of the window used
     * @param seq Sequence to be analyzed
     * @return The result of the palindromic analysis
     */
    public PalindromicityResult analyzeEvenAxisPalindromicity(
                                        int windowLength,
                                        String seq,
                                        boolean onlyWholeWindows,
                                        boolean withPatterns,
                                        boolean withMaxWordLength) throws Exception {

        if(windowLength < 2){
            throw new Exception("Window length must be greater than 2");
        }

        PalindromicityResult palResult = new PalindromicityResult();
        StringBuilder result = new StringBuilder(seq.length());
        StringBuilder patterns = null;
        StringBuilder resultMax = null;
        boolean[] patternArray = null;

        if(withPatterns){
            patterns = new StringBuilder(seq.length());
            patternArray = new boolean[windowLength/2];
            for (int i = 0; i < patternArray.length; i++) {
                patternArray[i] = false;
            }
        }

        if(withMaxWordLength){
            resultMax = new StringBuilder(seq.length());
        }

        int i;
        int limit;
        if(onlyWholeWindows){
            i = (windowLength/2) - 1;
            limit = seq.length() - (windowLength/2);
        }else{
            i = 0;
            limit = seq.length() - 1;
        }

        for (; i < limit; i++) {

            int counter = 0;
            int counterMax = 0;
            int maxWordLength = 0;
            char[] value1 = new char[1];
            char[] value2 = new char[1];

            for (int j = 0; j < windowLength / 2 && (i + j + 1) < seq.length() && (i - j) >= 0; j++) {

                seq.getChars(i - j, i - j + 1, value1, 0);
                seq.getChars(i + j + 1, i + j + 2, value2, 0);

                if (areComplementaryCommon(value1[0], value2[0])) {
                    counter++;

                    if(withPatterns){
                        patternArray[(windowLength/2)-1-j] = true;
                    }
                    if(withMaxWordLength){
                        counterMax++;
                        if (counterMax > maxWordLength) {
                            maxWordLength = counterMax;
                        }
                    }

                }else{
                    if(withMaxWordLength){
                        counterMax = 0;
                    }
                }
            }

            result.append((counter + DEFAULT_SEPARATOR));

            if(withMaxWordLength){
                resultMax.append((maxWordLength + DEFAULT_SEPARATOR));
            }

            if(withPatterns){
                int patternCode = BitOperations.convertToEquivalentInt(patternArray);
                patterns.append((patternCode + DEFAULT_SEPARATOR));
            }
        }

        palResult.setResult(result.substring(0, result.length() - 1));
        if(withPatterns){
            palResult.setPatterns(patterns.substring(0, patterns.length() - 1));
        }
        if(withMaxWordLength){
            palResult.setResultMaxWordLength(resultMax.substring(0, resultMax.length() - 1));
        }

        return palResult;
    }

 
    public boolean areComplentary(String value1, String value2){
        char c1 = value1.toLowerCase().charAt(0);
        char c2 = value2.toLowerCase().charAt(0);
        return areComplementaryCommon(c1, c2);
    }
    public boolean areComplementaryLowerCase(char value1, char value2){
        return areComplementaryCommon(value1, value2);
    }

    private boolean areComplementaryCommon(char value1, char value2) {        
//        if (value1 == 'a' && value2 == 't') {
//            return true;
//        }
//        else if (value1 == 't' && value2 == 'a') {
//            return true;
//        }
//        else if (value1 == 'c' && value2 == 'g') {
//            return true;
//        }
//        else if (value1 == 'g' && value2 == 'c') {
//            return true;
//        }
        if (value1 == 'A' && value2 == 'T') {
            return true;
        }else if (value1 == 'T' && value2 == 'A') {
            return true;
        }else if (value1 == 'C' && value2 == 'G') {
            return true;
        }else if (value1 == 'G' && value2 == 'C') {
            return true;
        }else{
             return false;
        }
    }


}
