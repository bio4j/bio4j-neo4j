/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.era7.bioinfo.bioinfoutil.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author ppareja
 */
public class PttFileFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        if (name.toLowerCase().endsWith("ptt")) {
            return true;
        }else{
            return false;
        }
    }
}
