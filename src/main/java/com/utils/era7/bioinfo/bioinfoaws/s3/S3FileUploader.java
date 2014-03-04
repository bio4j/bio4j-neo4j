/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.era7.bioinfo.bioinfoaws.s3;

import com.amazonaws.services.s3.AmazonS3;
import java.io.File;

/**
 *
 * @author ppareja
 */
public class S3FileUploader {

    
    public static boolean uploadEveryFileToS3Bucket(File file,
                                                    String bucketName,
                                                    String bucketFolder,
                                                    AmazonS3 amazonS3,
                                                    boolean includeSubFiles) {

        boolean success = true;

        if (file.isDirectory() && includeSubFiles) {

            File[] files = file.listFiles();
            for(File subFile : files){
                uploadEveryFileToS3Bucket(subFile,bucketName,bucketFolder,amazonS3,includeSubFiles);
            }

        } else {

            amazonS3.putObject(bucketName, bucketFolder+file.getName(), file);
            System.out.println(file.getName() + " file uploaded to s3 bucket "
                    + bucketName + " successfully :)");
            
        }


        return success;
    }

}
