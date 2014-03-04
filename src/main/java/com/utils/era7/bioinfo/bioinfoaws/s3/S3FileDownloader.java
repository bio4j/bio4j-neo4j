/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.era7.bioinfo.bioinfoaws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class S3FileDownloader {


    public static InputStream getS3FileInputStream(String fileName,
                                            String bucketName,
                                            AmazonS3 amazonS3){

        GetObjectRequest request = new GetObjectRequest(bucketName, fileName);
        S3Object s3Object = amazonS3.getObject(request);
        return s3Object.getObjectContent();

    }

    public static void downloadFileFromS3(String fileName,
                                            String bucketName,
                                            AmazonS3 amazonS3,
                                            File fileFolder) throws Exception{

        if(fileFolder.isDirectory()){

            if(fileFolder.exists()){

                GetObjectRequest request = new GetObjectRequest(bucketName, fileName);
                S3Object s3Object = amazonS3.getObject(request);

                FileOutputStream outStream = new FileOutputStream(new File(fileFolder,fileName));
                //BufferedWriter outBuff = new BufferedWriter(new FileWriter(new File(fileFolder,fileName)));
                InputStream s3InputStream = s3Object.getObjectContent();
                
//                BufferedReader inBuff = new BufferedReader(new InputStreamReader(s3InputStream));
//                String line = null;
//                while((line = inBuff.readLine()) != null){
//                    outBuff.write(line);
//                }
                byte[] buffer = new byte[1024];
                int len;
                while ((len = s3InputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }

                s3InputStream.close();
                outStream.close();


            }else{
                throw new Exception("Folder parameter does not exist");
            }

        }else{
            throw new Exception("Folder parameter is not a folder");
        }

    }

    
    public static void downloadFilesFromS3(List<String> fileNames,
                                            String bucketName,
                                            AmazonS3 amazonS3,
                                            File fileFolder) throws Exception{
        for(String fileName : fileNames){
            downloadFileFromS3(fileName, bucketName, amazonS3, fileFolder);
        }
    }

}
