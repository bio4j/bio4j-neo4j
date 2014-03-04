/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ohnosequences.util;

import com.ohnosequences.xml.model.util.Argument;
import com.ohnosequences.xml.model.util.Arguments;
import com.ohnosequences.xml.model.util.Execution;
import com.ohnosequences.xml.model.util.ScheduledExecutions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Element;

/**
 * 
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class ExecuteFromFile {

    public static void main(String[] args){

        if(args.length != 1){
            System.out.println("El programa espera un parametro: " + "\n"
                    + "1. Nombre del archivo xml con las ejecuciones programadas.");
        }else{

            try {
                
                File xmlFile = new File(args[0]);
                BufferedReader buff = new BufferedReader(new FileReader(xmlFile));
                String temp = null;
                StringBuilder stBuilder = new StringBuilder();
                while((temp = buff.readLine()) != null){
                    stBuilder.append(temp);
                }

                ScheduledExecutions exec = new ScheduledExecutions(stBuilder.toString());
                ArrayList<Execution> executions = exec.getExecutions();

                for(Execution execution : executions){
                    
                    String className = execution.getClassFullName();
                    Class classToExecute = Class.forName(className);

                    Arguments arguments = execution.getArguments();
                    List<Element> list = arguments.getRoot().getChildren(Argument.TAG_NAME);

                    ArrayList<String> listString = new ArrayList<String>();
                    Class[] params = new Class[1];
                    params[0] = listString.getClass();

                    for(Element elem : list){
                        Argument tempArgument = new Argument(elem);
                        listString.add(tempArgument.getArgumentText());
                    }
                    
                    Object object = classToExecute.newInstance();
                    Executable executable = (Executable)object;
                    executable.execute(listString);
                    
                }


                buff.close();
                
            } catch (Exception ex) {
                Logger.getLogger(ExecuteFromFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
