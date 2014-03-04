/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohnosequences.util.oric;

import com.ohnosequences.xml.model.oric.Oric;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * 
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class OricDataRetriever {

    public static String ORIC_INFORMATION_URL = "http://tubic.tju.edu.cn/doric/information.php?ac=";

    public static Oric getOricData(Oric oric) throws MalformedURLException, IOException {

        URL url = new URL(ORIC_INFORMATION_URL + oric.getId());
        // Connect
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        boolean connected = false;
        BufferedReader reader = null;
        while(!connected){
            try{
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                connected = true;
            }catch(SocketTimeoutException excp){
                System.out.println(excp.getMessage() + "... trying again...");
                urlConnection = (HttpURLConnection) url.openConnection();
            }catch(ConnectException ex){
                System.out.println(ex.getMessage() + "... trying again...");
                urlConnection = (HttpURLConnection) url.openConnection();
            }
        }       
         
        //BufferedReader reader = new BufferedReader(new FileReader(new File("pruebaOric.txt")));
        StringBuilder stringBuilder = new StringBuilder();
        String temp = null;
        while((temp = reader.readLine()) != null){
            stringBuilder.append(temp);
        }

        String pageSource = stringBuilder.toString();

        // -----------------start end positions------------------------
        String pageSplit = pageSource.split("The location of oriC region")[1];

        String[] pageSplit2 = pageSplit.split("\\.\\.");

        String[] tempStartSplit = pageSplit2[0].split(">");
        String start = tempStartSplit[tempStartSplit.length-1].trim();

        System.out.println("start = " + start);

        String end = pageSplit2[1].split("nt")[0].trim();

        System.out.println("end = " + end);

        oric.setLocationStart(Integer.parseInt(start));
        oric.setLocationEnd(Integer.parseInt(end));

        //-------------------genome size---------------------------------
        String gSplit = pageSource.split("Genome size")[1].split(" nt")[0];
        String[] gSplit2 = gSplit.split(">");
        int genomeSize = Integer.parseInt(gSplit2[gSplit2.length-1].trim());
        oric.setGenomeSize(genomeSize);

        //--------------------type--------------------
        String tSplit = pageSource.split("Type")[2].split("<")[0].trim();
        oric.setType(Integer.parseInt(tSplit));

        //------------------organism--------------------
        String oSplit = pageSource.split("Organism")[1].split("<")[4];
        oric.setOrganism(oSplit.split(">")[1].trim());

        return oric;
    }
}
