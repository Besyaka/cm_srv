/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeon.server.core;

import static argo.jdom.JsonNodeBuilders.*;
import argo.format.*;
import argo.jdom.*;
import com.zeon.server.core.handlers.UsersDB;

import com.zeon.server.Json;
/**
 *
 * @author Игорь
 */
public class MainHandler {
    private UsersDB _udb = new UsersDB();
    private Json _json = new Json();
    private static final JsonFormatter _jsform = new PrettyJsonFormatter();
    public String MainHandler(String s){
        int opCode = _json.Json(s);
        String response;
        switch(opCode){
            case 1:
                int accId = _json.AccId(s);
                int mrsp = _udb.getUser(accId);
                switch(mrsp){
                    case 1:
                        response = errorHandler("User " + accId + " already exsist");
                        break;
                    case 0:
                        response = errorHandler("User " + accId + " creating");
                        break;
                    default:
                        response = errorHandler("Error Users DB");
                        break;
                }
                break;
            case -1:
                response = "opcode -1";
                break;
            default:
                response = errorHandler("No opCodes");
                break;
        }
        return response;
    }
            public String errorHandler(String s){
            JsonObjectNodeBuilder builder = anObjectBuilder()                    
                    .withField("opCode", aNumberBuilder("-1")) 
                    .withField("message", aStringBuilder(s));
            JsonRootNode json = builder.build();
            String response = _jsform.format(json);
            return response;
        }
}
