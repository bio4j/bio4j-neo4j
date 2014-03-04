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
package com.era7.bioinfoxml.util;

import com.era7.era7xmlapi.model.XMLElement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class FlexXMLWrapperClassCreator {

    public static final String NECESSARY_IMPORTS = "import com.era7.util.BooleanParser;\nimport com.era7.xmlapi.model.XMLError;\nimport com.era7.xmlapi.model.XMLObject;";
    public static final String VARS_PREFIX = "public static const ";
    private static final String CLASS_NAME_CONSTRUCTOR_VAR = "lalala";
    private static final String CONSTRUCTORS_STR = "public function " + CLASS_NAME_CONSTRUCTOR_VAR + "(... args){\n"
            + "\tvar temp:Object;\n"
            + "\tvar proceed:Boolean = false;\n"
            + "\tif(args.length == 0){\n"
            + "\t\ttemp = new XML(<{TAG_NAME}/>);\n"
            + "\t\tproceed = true;\n"
            + "\t}else if(args.length == 1){\n"
            + "\t\ttemp = args[0];\n"
            + "\t\tproceed = true;\n"
            + "\t}else{\n"
            + "\t\tproceed = false;\n"
            + "\t}\n"
            + "\tif(proceed){\n"
            + "\t\tsuper(temp);\n"
            + "\tif(this.content.name() != TAG_NAME){\n"
            + "\t\tthrow new XMLError(XMLError.WRONG_TAG_NAME_ERROR);\n"
            + "\t}\n"
            + "\t}else{\n"
            + "\t\tthrow new XMLError(XMLError.TOO_MANY_PARAMETERS_FOR_THE_CONSTRUCTOR);\n"
            + "\t}\n"
            + "}\n";
    private static final String GETTERS_STR = "\n//----------------GETTERS-------------------\n";
    private static final String SETTERS_STR = "\n//----------------SETTERS-------------------\n";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("El programa espera un parametro: \n"
                    + "1. Nombre del archivo xml con la descripcion de la clase \n");
        } else {
            File inFile = new File(args[0]);

            try {

                BufferedReader reader = new BufferedReader(new FileReader(inFile));
                String line = null;
                StringBuilder stBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stBuilder.append(line);
                }
                reader.close();

                XMLElement classes = new XMLElement(stBuilder.toString());
                List<Element> list = classes.getRoot().getChildren(XMLWrapperClass.TAG_NAME);

                for (Element elem : list) {
                    XMLWrapperClass wrapper = new XMLWrapperClass(elem);

                    //primero creo los directorios del package
                    String[] packageSplit = wrapper.getPackage().split("\\.");
                    String currentFolder = "./";
                    for (int i = 0; i < packageSplit.length; i++) {
                        String string = packageSplit[i];
                        currentFolder += string;
                        System.out.println("currentFolder = " + currentFolder);
                        File tempFile = new File(currentFolder);
                        if(!tempFile.exists()){
                            tempFile.mkdir();
                            System.out.println("no existe: tempFile = " + tempFile);
                        }else{
                            System.out.println("existe: tempFile = " + tempFile);
                        }
                        currentFolder += "/";

                    }

                    BufferedWriter outBuff = new BufferedWriter(new FileWriter(new File(currentFolder + wrapper.getClassName() + ".as")));
                    outBuff.write("package " + wrapper.getPackage() + "\n{\n");
                    outBuff.write(NECESSARY_IMPORTS);
                    outBuff.write("\n\npublic class " + wrapper.getClassName() + " extends XMLObject{\n\n");
                    outBuff.write("public static const TAG_NAME:String = \"" + wrapper.getTagName() + "\";\n\n");

                    HashMap<String, String> varsDeclarationNames = new HashMap<String, String>();
                    HashMap<String, String> varsTypes = new HashMap<String, String>();

                    List<Element> vars = wrapper.getVars().getChildren();
                    for (Element element : vars) {
                        outBuff.write(VARS_PREFIX + " ");
                        String varName = element.getText();
                        String varDeclarationName = "";
                        for (int i = 0; i < varName.length(); i++) {
                            char c = varName.charAt(i);
                            if (Character.isUpperCase(c)) {
                                varDeclarationName += "_" + c;
                            } else {
                                varDeclarationName += ("" + c).toUpperCase();
                            }
                        }
                        varDeclarationName = varDeclarationName + "_TAG_NAME";
                        varsDeclarationNames.put(varName, varDeclarationName);
                        varsTypes.put(varName, element.getName());

                        //En este ciclo cambio la mayuscula del nombre de la variable por '_'
                        String tempTagName = "";
                        for (int i = 0; i < varName.length(); i++) {
                            char c = varName.charAt(i);
                            if (Character.isUpperCase(c)) {
                                tempTagName += "_" + ("" + c).toLowerCase();
                            } else {
                                tempTagName += "" + c;
                            }

                        }

                        outBuff.write(varDeclarationName + ":String = \"" + tempTagName + "\";\n");
                    }

                    //Ahora la parte de los constructores
                    outBuff.write("\n" + CONSTRUCTORS_STR.replaceAll(CLASS_NAME_CONSTRUCTOR_VAR, wrapper.getClassName()));


                    Set<String> varsKeySet = varsDeclarationNames.keySet();
                    //A rellenar los getters!
                    outBuff.write("\n" + GETTERS_STR);
                    for (String key : varsKeySet) {

                        String varType = varsTypes.get(key);
                        String getStr = key;
                        outBuff.write("\npublic function get " + getStr + "():" + varType + "{\treturn ");
                        if (varType.equals("int")) {
                            outBuff.write("parseInt(getTagText(" + varsDeclarationNames.get(key) + "));}");
                        } else if (varType.equals("Number")) {
                            outBuff.write("new Number(getTagText(" + varsDeclarationNames.get(key) + "));}");
                        } else if (varType.equals("String")) {
                            outBuff.write("getTagText(" + varsDeclarationNames.get(key) + ");}");
                        } else if(varType.equals("Boolean")){
                            outBuff.write("BooleanParser.parseBoolean(getTagText(" + varsDeclarationNames.get(key) + "));}");
                        } else{
                            outBuff.write("getTagText(" + varsDeclarationNames.get(key) + ");}");
                        }

                    }

                    //A rellenar los setters!
                    outBuff.write("\n" + SETTERS_STR);
                    for (String key : varsKeySet) {
                        String varType = varsTypes.get(key);
                        String setStr = " set " + key;
                        outBuff.write("\n[Bindable]\npublic function " + setStr + "(value:" + varType + "):void{\t setTagText("
                                + varsDeclarationNames.get(key) + ", ");
                        if (varType.equals("String")) {
                            outBuff.write("value");
                        } else {
                            outBuff.write("String(value)");
                        }
                        outBuff.write(");}");
                    }


                    //Llave que cierra la clase
                    outBuff.write("\n}\n");
                    //Llave que cierra el package
                    outBuff.write("\n}\n");

                    outBuff.close();
                }




            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
