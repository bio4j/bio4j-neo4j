/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohnosequences.util.file;

import java.io.File;

/**
 *
 * @author ppareja
 */
public class FileUtil {

    public static boolean collapseEverySubFileToCurrentDir(File dir) {

        boolean success = true;

        if (dir.isDirectory()) {

            File[] files = dir.listFiles();
            for(File file : files){
                if(file.isDirectory()){
                    collapseFilesRecursive(dir, file);
                }
            }

        } else {
            System.out.println("A file was provided instead of a dir...");
            success = false;
        }


        return success;
    }

    private static boolean collapseFilesRecursive(File destination, File currentDir) {
        if (currentDir.isDirectory()) {
            File[] files = currentDir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    boolean success = file.renameTo(new File(destination, file.getName()));
                    if (!success) {
                        System.out.println("There was a problem moving the file: " + file.getAbsolutePath());
                        return false;
                    }
                }else{
                    boolean success = collapseFilesRecursive(destination, file);
                    if(!success){
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
