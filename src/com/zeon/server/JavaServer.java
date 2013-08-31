package com.zeon.server;

import com.zeon.server.core.GeneralChannelHandler;
import com.zeon.server.core.JavaServerProperties;
import com.zeon.server.core.BaseJavaServerLoggedClass;
import com.zeon.server.core.ServerPipeLineFactory;
import com.zeon.server.core.handlers.opCodes;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class JavaServer extends BaseJavaServerLoggedClass {

	public static JavaServerProperties properties;
	private static NioServerSocketChannelFactory factory;
	private static GeneralChannelHandler generalChannelHandler;

	public static void main(String[] args) {
		prepare();
		info("String server...");
		try {
			initServer();
		} catch (Exception e) {
			exception("Server init fail", e);
		}
		info("Server READY.");
                info("Start to:" + properties.getInt("server.port"));
	}

	private static void prepare() {
		try {
			loadProperties();
			initLogger();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}

	private static void loadProperties() throws Exception {
		properties = new JavaServerProperties();
		properties.loadProperties();
	}

	private static void initLogger() {
		org.apache.log4j.PropertyConfigurator.configure(properties.getProperty("log4j.propertyFileName"));
	}

	private static void initServer() {
		factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		initGeneralChannelHandler();
	}

	private static void initGeneralChannelHandler() {
		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		generalChannelHandler = new GeneralChannelHandler();

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ServerPipeLineFactory());

		//Socket options
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);

		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(properties.getInt("server.port")));
	}


}
