package com.zeon.server.core;

import java.io.FileReader;
import java.io.IOException;
import java.lang.*;
import java.net.SocketAddress;

import com.zeon.server.Json;
import com.zeon.server.core.handlers.*;
import com.zeon.server.core.opCodes;

import org.apache.log4j.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
//import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.DownstreamMessageEvent;

import static argo.jdom.JsonNodeBuilders.*;
import argo.format.*;
import argo.jdom.*;

public class GeneralChannelHandler extends SimpleChannelUpstreamHandler {
    boolean _debug = true;
        private static final JsonFormatter jsform = new PrettyJsonFormatter();
        opCodes oc = new opCodes();
	private static final Logger logger = Logger.getLogger(GeneralChannelHandler.class);
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e){
            Users.getInstance().AddUser(ctx);
            System.out.println("New User" + ctx.getChannel());
        }
        public void channelDisconnected( ChannelHandlerContext ctx, ChannelStateEvent e ) throws Exception
        {
            Users.getInstance().RemoveUser(ctx);
            System.out.println("Remove User" + ctx.getChannel());
        }
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		try {
			new CommandProcessorSmall("cmdProcessor", new NetContext(e.getChannel(), (String) e.getMessage()), _debug).start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
            /*
            */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		// Log the exceptorg.jbossn
		logger.error("Exception", e.getCause());
        }
        
        public String errorHandler(String s){
            JsonObjectNodeBuilder builder = anObjectBuilder()                    
                    .withField("opCode", aNumberBuilder("-1")) 
                    .withField("message", aStringBuilder(s));
            JsonRootNode json = builder.build();
            String response = jsform.format(json);
            Object r = response;
            return response;
        }
}
