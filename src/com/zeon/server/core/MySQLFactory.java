/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeon.server.core;

import com.zeon.server.core.JavaServerProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PoolConfig;
import com.mchange.v2.c3p0.PooledDataSource;
/**
 *
 * @author Игорь
 */
public class MySQLFactory{
    private static MySQLFactory _instance;
    private DataSource _source;
    public static JavaServerProperties properties;
    	public MySQLFactory() throws SQLException
	{
		try
		{
                    System.out.println("fucking db");
			PoolConfig config = new PoolConfig();
			config.setAutoCommitOnClose(true);
			config.setInitialPoolSize(3);   // 3 is the default for c3p0 anyway  
			// if > MaxPoolSize, it will be ignored - no worry
			// (as said in c3p0 docs, it's only a suggestion
			// how many connections to acquire to start with)

			config.setMinPoolSize(1);
			config.setMaxPoolSize(properties.getInt("database.max.connections"));

 
			config.setAcquireRetryAttempts(0); // try to obtain connections indefinitely (0 = never quit)
			config.setAcquireRetryDelay(500);  // 500 miliseconds wait before try to acquire connection again
			config.setCheckoutTimeout(0);      // 0 = wait indefinitely for new connection
			// if pool is exhausted
			config.setAcquireIncrement(5);     // if pool is exhausted, get 5 more connections at a time
			// cause there is a "long" delay on acquire connection
			// so taking more than one connection at once will make connection pooling 
			// more effective. 
 
			// this "connection_test_table" is automatically created if not already there
			config.setAutomaticTestTable("connection_test_table");  // very very fast test, don't worry
			config.setTestConnectionOnCheckin(true); // this will *not* make l2j slower in any way
 
			// testing OnCheckin used with IdleConnectionTestPeriod is faster than  testing on checkout
 
			config.setIdleConnectionTestPeriod(60); // test idle connection every 60 sec
			config.setMaxIdleTime(0); // 0 = idle connections never expire 
			// *THANKS* to connection testing configured above
			// but I prefer to disconnect all connections not used
			// for more than 1 hour  

			// enables statement caching,  there is a "semi-bug" in c3p0 0.9.0 but in 0.9.0.2 and later it's fixed
			config.setMaxStatementsPerConnection(100);

			config.setBreakAfterAcquireFailure(false);  // never fail if any way possible
			// setting this to true will make
			// c3p0 "crash" and refuse to work 
			// till restart thus making acquire
			// errors "FATAL" ... we don't want that
			// it should be possible to recover
 
			Class.forName(properties.getProperty("database.driverClassName")).newInstance();

			if (properties.getBoolen("database.debug")) System.out.println("Database Connection Working");

			DataSource unpooled = DataSources.unpooledDataSource(properties.getProperty("database.url"), properties.getProperty("database.username"), properties.getProperty("database.password"));
			_source = DataSources.pooledDataSource( unpooled, config);
			
			/* Test the connection */
			_source.getConnection().close();

		}
		catch (SQLException x)
		{
			if (properties.getBoolen("database.debug")) System.out.println("Database Connection FAILED");
			// rethrow the exception
			throw x;
		}
		catch (Exception e)
		{
			if (properties.getBoolen("database.debug")) System.out.println("Database Connection FAILED");
			throw new SQLException("could not init DB connection:"+e);
		}
	}
    public void shutdown()
    {
        try {
            ((PooledDataSource) _source).close();
        } catch (SQLException e) {System.out.println(e + "f");}
        try {
            DataSources.destroy(_source);
        } catch (SQLException e) {System.out.println(e + "e");}
    }
	public static MySQLFactory getInstance() throws SQLException
	{
		if (_instance == null)
		{
			_instance = new MySQLFactory();
		}
		return _instance;
	}
        public Connection getConnection() //throws SQLException
	{
		Connection con=null;
                System.out.println("Yeal DB");
		while(con==null)
		{
			try
			{
				con=_source.getConnection();
			} catch (SQLException e)
			{
				System.out.println("L2DatabaseFactory: getConnection() failed, trying again "+e);
			}
		}
		return con;
	}
	public int getBusyConnectionCount() throws SQLException
	{
	    return ((PooledDataSource) _source).getNumBusyConnectionsDefaultUser();
	}

	public int getIdleConnectionCount() throws SQLException
	{
	    return ((PooledDataSource) _source).getNumIdleConnectionsDefaultUser();
	}
}

