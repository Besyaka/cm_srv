package com.zeon.server.core;


import com.zeon.server.core.handlers.opCodes;
import com.zeon.server.core.handlers.Users;
import org.apache.log4j.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.ChannelStateEvent;

import static argo.jdom.JsonNodeBuilders.*;
import argo.format.*;
import argo.jdom.*;

public class GeneralChannelHandler extends SimpleChannelUpstreamHandler {
    boolean _debug = true;
        private static final JsonFormatter jsform = new PrettyJsonFormatter();
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
