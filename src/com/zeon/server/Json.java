/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeon.server;
import argo.jdom.JdomParser;
import argo.format.*;
import argo.saj.*;
import argo.jdom.JsonRootNode;


/**
 *
 * @author Игорь
 */
public class Json {
    JsonRootNode json; 
    
    public int Json(String s){
        try{
        json = new JdomParser().parse(s);
        int opcode = Integer.parseInt(json.getNumberValue("opCode"));
        System.out.println(opcode);
        return opcode;
        } catch(Exception e){
            return -1;
        }
    }
    public int AccId(String s){
        try{
            json = new JdomParser().parse(s);
            int accId = Integer.parseInt(json.getNumberValue("id"));
            return accId;
        }catch(Exception e){
            return -1;
        }
    }
    public String Name(String s){
        try{
            json = new JdomParser().parse(s);
            String rn = json.getStringValue("fname") + " " + json.getStringValue("lname");
            return rn;
        }catch(Exception e){
            return "error";
        }
    }
}
