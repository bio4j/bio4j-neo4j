/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohnosequences.util.uniprot;

import com.ohnosequences.xml.model.PredictedGene;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author ppareja
 */
public class UniprotProteinRetreiver {

    public static String URL_UNIPROT = "http://www.uniprot.org/uniprot/";

    public static PredictedGene getUniprotDataFor(PredictedGene gene, boolean withSequence) throws Exception {


        String columnsParameter = "protein names,organism,comment(FUNCTION),ec,interpro,go,pathway,families,keywords,length,subcellular locations,citation,genes,go-id,domains,length";
        if(withSequence){
            columnsParameter += ",sequence";
        }

        HttpPost post = new HttpPost(URL_UNIPROT);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("query", "accession:" + gene.getAnnotationUniprotId()));
        formparams.add(new BasicNameValuePair("format", "tab"));
        formparams.add(new BasicNameValuePair("columns", columnsParameter));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        post.setEntity(entity);

        // execute the POST
        String response = null;
        HttpClient client = new DefaultHttpClient();
        do {
            System.out.println("Performing POST request...");
            
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseSt = client.execute(post, responseHandler);

            String respNoHeader = responseSt.split("\n")[1];
            
            if (respNoHeader.isEmpty()) {
                System.out.println("There was no response, trying again....");
            }else{
                response = respNoHeader;
            }
        } while (response == null);


        int maxI = 16;
        if(withSequence){
            maxI = 17;
        }

        String[] columns = response.split("\t");
//        System.out.println("columns = " + columns);
//        System.out.println("columns.length = " + columns.length);

        for (int i = 0; i < maxI; i++) {
            String currentValue = "";
//            int index = response.indexOf("\t");
//            if (i < 11) {
//                currentValue = response.substring(0, index);
//            } else {
//                currentValue = response.replaceFirst("\t", "");
//            }

            currentValue = columns[i];

            //System.out.println("i = " + i + " currentValue = " +  currentValue);
            switch (i) {
                case 0:
                    gene.setProteinNames(currentValue);
                    break;
                case 1:
                    gene.setOrganism(currentValue);
                    break;
                case 2:
                    gene.setCommentFunction(currentValue);
                    break;
                case 3:
                    gene.setEcNumbers(currentValue);
                    break;
                case 4:
                    gene.setInterpro(currentValue);
                    break;
                case 5:
                    gene.setGeneOntology(currentValue);
                    break;
                case 6:
                    gene.setPathway(currentValue);
                    break;
                case 7:
                    gene.setProteinFamily(currentValue);
                    break;
                case 8:
                    gene.setKeywords(currentValue);
                    break;
                case 9:
                    gene.setLength(Integer.parseInt(currentValue));
                    break;
                case 10:
                    gene.setSubcellularLocations(currentValue);
                    break;
                case 11:
                    gene.setPubmedId(currentValue);
                    break;
                case 12:
                    gene.setGeneNames(currentValue);
                    break;
                case 13:
                    gene.setGeneOntologyId(currentValue);
                    break;
                case 14:
                    gene.setDomains(currentValue);
                    break;
                case 15:
                    gene.setLength(Integer.parseInt(currentValue));
                    break;
                case 16:
                    gene.setSequence(currentValue.replaceAll(" ", ""));
                    break;

            }

//            response = response.substring(index);
//            if (i != 10) {
//                response = response.replaceFirst("\t", "");
//            }

        }

        //pongo como accession el unipot id
        gene.setAccession(gene.getAnnotationUniprotId());

        return gene;
    }
}
