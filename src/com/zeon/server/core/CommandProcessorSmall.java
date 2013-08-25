/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zeon.server.core;

import org.jboss.netty.channel.Channel;

public class CommandProcessorSmall extends Thread {
	private NetContext _nCtx;
	private boolean _debug;
        private MainHandler _mhandler = new MainHandler();

	public CommandProcessorSmall(String name, NetContext nCtx, boolean debug) {
		super();
		setName(name);
		
		_nCtx = nCtx;
		_debug = debug;
	}
	
	@Override
	public void run() {
		try {
			if (_debug) {
				System.out.print("IN  > "+_nCtx.message+"\n");
			}
//                        _mhandler = new MainHandler();
			//Варианты ответов
			//String response = (String)_combat.processCommand(_nCtx);   //Игровая логика генерирует ответ
//			String response = "{\"resp\":\"Anwer in JSON format\"}";   //JSON
			//String response = "<resp>Answer in XML format</resp>";       //XML
                        String response = _mhandler.MainHandler(_nCtx.message);
			((Channel)(_nCtx.channel)).write(response+"\0");
	
			if (_debug) {
				System.out.print("OUT > "+response+"\n");	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
