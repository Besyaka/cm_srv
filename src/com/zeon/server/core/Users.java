/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeon.server.core;

import org.jboss.netty.channel.group.DefaultChannelGroup;
import java.util.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.ChannelHandlerContext;
/**
 *
 * @author Игорь
 */
public class Users {
    private static Users _instance;
    private static ChannelGroup channels = new DefaultChannelGroup();
    public static Users getInstance()
    {
        if (_instance == null)
        {
            synchronized (Users.class){
                if(_instance == null)
                    _instance = new Users();
            }   
        }
        return _instance;
    }
    public void AddUser(ChannelHandlerContext ctx){
        channels.add(ctx.getChannel());
    }
    public void RemoveUser(ChannelHandlerContext ctx){
        channels.remove(ctx.getChannel());
    }
    public ChannelGroup GetChannelsList(){
        return channels;
    }
}
