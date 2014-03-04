/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohnosequences.util.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author ppareja
 */
public class RntFileFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        if (name.toLowerCase().endsWith("rnt")) {
            return true;
        }else{
            return false;
        }
    }
}
