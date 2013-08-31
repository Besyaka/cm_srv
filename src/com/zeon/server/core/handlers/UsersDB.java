/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeon.server.core.handlers;

import com.zeon.server.core.mysql.handlers.UsersTableHandler;
import com.zeon.server.Json;

/**
 *
 * @author Игорь
 */
public class UsersDB {
    private Json js;
    public int getUser(int i){
        System.out.println("User id: " + i);
        int response = UsersTableHandler.getInstance().FindUser(i);
        System.out.println("error getUser : " + response);
        return response;
    }
    public boolean newUser(String s, int i){
        String[] name = s.split(" ");
        long epoch = System.currentTimeMillis()/1000;
        boolean response = UsersTableHandler.getInstance().AddUser(name, i, epoch);
        return response;
    }
    
}
