/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.era7.bioinfo.bioinfoaws.ec2;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author ppareja
 */
public class InstanceUtil {

    public static final String METADA_INSTANCE_ID_WEB_SERVICE_URL = "http://169.254.169.254/latest/meta-data/instance-id";
    public static final String METADATA_AVAILABILITY_ZONE_WEB_SERVICE_URL = "http://169.254.169.254/latest/meta-data/placement/availability-zone";

    public static String getRunningInstanceId() throws ClientProtocolException, IOException {
        String id = "";

        HttpGet httpget = new HttpGet(METADA_INSTANCE_ID_WEB_SERVICE_URL);

        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        id = client.execute(httpget, responseHandler);

        return id;
    }
    
public static String getRunningInstanceAvailabilityZone() throws IOException{
        
        String availabilityZone = "";

        HttpGet httpget = new HttpGet(METADATA_AVAILABILITY_ZONE_WEB_SERVICE_URL);

        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        availabilityZone = client.execute(httpget, responseHandler);

        return availabilityZone;
    }

}
