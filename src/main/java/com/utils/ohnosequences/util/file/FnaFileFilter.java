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
public class FnaFileFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        if (name.toLowerCase().endsWith("fna")) {
            return true;
        }else{
            return false;
        }
    }
}
