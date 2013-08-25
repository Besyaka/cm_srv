/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeon.server.core.mysql;

import com.zeon.server.core.MySQLFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 *
 * @author Игорь
 */
public class UsersTableHandler {
    private static UsersTableHandler _instance;
    public static UsersTableHandler getInstance()
    {
        if (_instance == null)
        {
            _instance = new UsersTableHandler();
        }
        return _instance;
    }
    public int AddUser(String[] s, int i, long Time){
        String fn = s[0];
        String ln = s[1];
        System.out.println("Creating user with name: " + fn + " " + ln + " and id: " + i);
        java.sql.Connection con = null;
        try
        {
            con = MySQLFactory.getInstance().getConnection();
            PreparedStatement statement;
            statement = con.prepareStatement("INSERT INTO users (id, fname, lname, last_login) VALUES(?,?,?,?)");
            statement.setInt(1, i);
            statement.setString(2, fn);
            statement.setString(3, ln);
            statement.setLong(4, Time);
            statement.execute();
            statement.close();
            return 1;
        }
        catch (Exception e)
        {
            System.out.println("could not store clans wars data:"+e);
            return -1;
        } 
        finally 
        {
            try { con.close(); } catch (Exception e) {}
        }
    }
    
    public int RemoveUser(int i){
        java.sql.Connection con = null;
        try{
            return 1;
        }catch(Exception e){
            return -1;
        }finally{
            
        }
    }
    public int FindUser(int i){
        System.out.println("Finding user: " + i);
        java.sql.Connection con = null;
        try{
            System.out.println("yeapp");
            con = MySQLFactory.getInstance().getConnection();
            System.out.println("Create connection...");
            PreparedStatement statement;
            statement = con.prepareStatement("SELECT id FROM users WHERE id=?");
            System.out.println("Get message");
            statement.setInt(1, i);
            ResultSet rset = statement.executeQuery();
            if(rset.next()==false) {
            System.out.println("False");
            return 0;
            }
            rset.close();
            statement.close();
            return 1;
        }catch(Exception e){
            System.out.println(e + "z");
            return -1;
        }finally{
            try { con.close(); } catch (Exception e) {}
        }
    }
}
