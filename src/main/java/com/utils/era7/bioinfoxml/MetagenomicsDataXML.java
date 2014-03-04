/*
 * Copyright (C) 2010-2012  "Oh no sequences!"
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.era7.bioinfoxml;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class MetagenomicsDataXML extends XMLElement{

    public static final String TAG_NAME = "metagenomica_data";
    
    public static final String QUEUE_NAME_TAG_NAME = "queue_name";
    public static final String READS_PER_FILE_TAG_NAME = "reads_per_file";
    public static final String READ_PACKAGES_BUCKET_FOLDER_TAG_NAME = "read_packages_bucket_folder";
    public static final String BUCKET_TAG_NAME = "bucket";
    public static final String READS_BUCKET_FOLDER_TAG_NAME = "reads_bucket_folder";
    public static final String RESULTS_BUCKET_FOLDER_TAG_NAME = "results_bucket_folder";
    public static final String ERRORS_BUCKET_FOLDER_TAG_NAME = "errors_bucket_folder";
    
    public static final String INSTANCES_IDLE_TIME_IN_SECONDS_TAG_NAME = "instances_idle_time_in_seconds";
    public static final String INSTANCE_TYPE_TAG_NAME = "instance_type";
    
    public static final String BASE_FOLDER_TAG_NAME = "base_folder";
    public static final String BLAST_PATH_TAG_NAME = "blast_path";
    
    public static final String MD5_FILE_SUFFIX_TAG_NAME = "md5_file_suffix";
    public static final String ERROR_FILE_SUFFIX_TAG_NAME = "error_file_suffix";
    
    public static final String PROJECT_PREFIX_TAG_NAME = "project_prefix";
    
    public static final String SNAPSHOT_ID_TAG_NAME = "snapshot_id";
    public static final String VOLUME_SIZE_IN_GB_TAG_NAME = "volume_size_in_gb";
    public static final String VOLUME_DEVICE_TAG_NAME = "volume_device";
    
    public static final String MOUNTING_DIR_TAG_NAME = "mounting_dir";
    public static final String KEY_PAIR_NAME_TAG_NAME = "key_pair_name";
    
    public static final String NUMBER_OF_CONCURRENT_INSTANCES_TAG_NAME = "number_of_concurrent_instances";
    public static final String SPOT_INSTANCE_PRICE_TAG_NAME = "spot_instance_price";
    
    public static final String USER_DATA_FILE_TAG_NAME = "user_data_file";
    
    public static final String ERRORS_SNS_TOPIC_NAME_TAG_NAME = "errors_sns_topic_name";
    public static final String ERRORS_SNS_TOPIC_ARN_TAG_NAME = "errors_sns_topic_arn";
        
    public static final String RESULTS_DB_FOLDER = "results_db_folder";
    public static final String MLM_CONFIG_PROPS_TAG_NAME = "mlm_config_props";
    
    public MetagenomicsDataXML(){
        super(new Element(TAG_NAME));
    }
    public MetagenomicsDataXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public MetagenomicsDataXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }
    
    //----------------SETTERS-------------------
    public void setQueueName(String value){    setNodeText(QUEUE_NAME_TAG_NAME, value);}
    public void setReadsPerFile(String value){  setNodeText(READS_PER_FILE_TAG_NAME, value);}
    public void setReadPackagesBucketFolder(String value){    setNodeText(READ_PACKAGES_BUCKET_FOLDER_TAG_NAME, value);}
    public void setBucket(String value){    setNodeText(BUCKET_TAG_NAME, value);}
    public void setReadsBucketFolder(String value){ setNodeText(READS_BUCKET_FOLDER_TAG_NAME, value);}
    public void setResultsBucketFolder(String value){   setNodeText(RESULTS_BUCKET_FOLDER_TAG_NAME, value);}
    public void setErrorsBucketFolder(String value){   setNodeText(ERRORS_BUCKET_FOLDER_TAG_NAME, value);}
    public void setInstancesIdleTimeInSeconds(String value){    setNodeText(INSTANCES_IDLE_TIME_IN_SECONDS_TAG_NAME, value);}
    public void setInstanceType(String value){  setNodeText(INSTANCE_TYPE_TAG_NAME, value);}
    public void setBaseFolder(String value){    setNodeText(BASE_FOLDER_TAG_NAME, value);}
    public void setBlastPath(String value){ setNodeText(BLAST_PATH_TAG_NAME, value);}
    public void setMD5FileSufix(String value){  setNodeText(MD5_FILE_SUFFIX_TAG_NAME, value);}
    public void setErrorFileSufix(String value){    setNodeText(ERROR_FILE_SUFFIX_TAG_NAME, value);}
    public void setProjectPrefix(String value){ setNodeText(PROJECT_PREFIX_TAG_NAME, value);}
    public void setSnapshotId(String value){ setNodeText(SNAPSHOT_ID_TAG_NAME, value);}
    public void setVolumeSizeInGB(String value){ setNodeText(VOLUME_SIZE_IN_GB_TAG_NAME, value);}
    public void setVolumeDevice(String value){ setNodeText(VOLUME_DEVICE_TAG_NAME, value);}
    public void setMountingDir(String value){ setNodeText(MOUNTING_DIR_TAG_NAME, value);}
    public void setKeyPairName(String value){ setNodeText(KEY_PAIR_NAME_TAG_NAME, value);}
    public void setNumberOfConcurrentInstances(String value){ setNodeText(NUMBER_OF_CONCURRENT_INSTANCES_TAG_NAME, value);}
    public void setSpotInstancePrice(String value){ setNodeText(SPOT_INSTANCE_PRICE_TAG_NAME, value);}
    public void setUserDataFile(String value){  setNodeText(USER_DATA_FILE_TAG_NAME, value);}
    public void setErrorsSNSTopicName(String value){    setNodeText(ERRORS_SNS_TOPIC_NAME_TAG_NAME, value);}
    public void setErrorsSNSTopicArn(String value){ setNodeText(ERRORS_SNS_TOPIC_ARN_TAG_NAME, value);}
    public void setResultsDBFolder(String value){   setNodeText(RESULTS_DB_FOLDER, value);}
    public void setMLMConfigProps(String value){   setNodeText(MLM_CONFIG_PROPS_TAG_NAME, value);}
    
    //----------------GETTERS---------------------
    public String getQueueName(){       return getNodeText(QUEUE_NAME_TAG_NAME);  }
    public int getReadsPerFile(){    return Integer.parseInt(getNodeText(READS_PER_FILE_TAG_NAME)); }
    public String getReadPackagesBucketFolder(){  return getNodeText(READ_PACKAGES_BUCKET_FOLDER_TAG_NAME);}
    public String getBucket(){  return getNodeText(BUCKET_TAG_NAME);}
    public String getResultsBucketFolder(){ return getNodeText(RESULTS_BUCKET_FOLDER_TAG_NAME);}
    public String getErrorsBucketFolder(){  return getNodeText(ERRORS_BUCKET_FOLDER_TAG_NAME);}
    public String getReadsBucketFolder(){   return getNodeText(READS_BUCKET_FOLDER_TAG_NAME);}
    public int getInstancesIdleTimeInSeconds(){  return Integer.parseInt(getNodeText(INSTANCES_IDLE_TIME_IN_SECONDS_TAG_NAME));}
    public String getInstanceType(){    return getNodeText(INSTANCE_TYPE_TAG_NAME);}
    public String getBaseFolder(){  return getNodeText(BASE_FOLDER_TAG_NAME);}
    public String getBlastPath(){   return getNodeText(BLAST_PATH_TAG_NAME);}
    public String getMD5FileSufix(){    return getNodeText(MD5_FILE_SUFFIX_TAG_NAME);}
    public String getProjectPrefix(){   return getNodeText(PROJECT_PREFIX_TAG_NAME);}
    public String getErroFileSufix(){   return getNodeText(ERROR_FILE_SUFFIX_TAG_NAME);}
    public String getSnapshotId(){   return getNodeText(SNAPSHOT_ID_TAG_NAME);}
    public int getVolumeSizeInGB(){   return Integer.parseInt(getNodeText(VOLUME_SIZE_IN_GB_TAG_NAME));}
    public String getVolumeDevice(){   return getNodeText(VOLUME_DEVICE_TAG_NAME);}
    public String getMountingDir(){   return getNodeText(MOUNTING_DIR_TAG_NAME);}
    public String getKeyPairName(){   return getNodeText(KEY_PAIR_NAME_TAG_NAME);}
    public int getNumberOfConcurrentInstances(){   return Integer.parseInt(getNodeText(NUMBER_OF_CONCURRENT_INSTANCES_TAG_NAME));}
    public String getSpotInstancePrice(){   return getNodeText(SPOT_INSTANCE_PRICE_TAG_NAME);}
    public String getUserDataFile(){    return getNodeText(USER_DATA_FILE_TAG_NAME);}
    public String getErrorsSNSTopicName(){  return getNodeText(ERRORS_SNS_TOPIC_NAME_TAG_NAME);}
    public String getErrorsSNSTopicArn(){  return getNodeText(ERRORS_SNS_TOPIC_ARN_TAG_NAME);}
    public String getResultsDBFolder(){ return getNodeText(RESULTS_DB_FOLDER);}
    public String getMLMConfigProps(){ return getNodeText(MLM_CONFIG_PROPS_TAG_NAME);}
    
}
