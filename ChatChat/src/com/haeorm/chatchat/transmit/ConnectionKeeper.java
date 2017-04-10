package com.haeorm.chatchat.transmit;

import java.awt.AWTException;
import java.awt.Robot;
import java.net.Socket;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.root.view.RootLayoutController.CONNECTION_STATUS;
import com.haeorm.chatchat.transmit.receiver.Receiver;
import com.haeorm.chatchat.transmit.sender.Sender;
import com.haeorm.chatchat.util.ExceptionDialog;
import com.sun.javafx.geom.transform.GeneralTransform3D;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert.AlertType;

public class ConnectionKeeper extends Thread {
	
	Client client = null;
	public IntegerProperty dumpPing = new SimpleIntegerProperty();
	
	public ConnectionKeeper(Client client) {
		this.client = client;
		
		dumpPing.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(newValue.intValue() < 4){
					client.getRootStage().getRootLayoutController().setConnectionStatus(CONNECTION_STATUS.GOOD);
					return;
				}
				else if(newValue.intValue() < 8){
					client.getRootStage().getRootLayoutController().setConnectionStatus(CONNECTION_STATUS.NOMAL);
					return;
				}
				else{
					client.getRootStage().getRootLayoutController().setConnectionStatus(CONNECTION_STATUS.BAD);
					
					Platform.runLater(() -> {
						//client.tryReConnectServer();
					});
					
				}
					
				
			}
		});
		
	}

	@Override
	public void run() {
		
		while(!currentThread().isInterrupted()){
			
			dumpPing.set(dumpPing.get()+1);
			client.getManager().sendConnectionCheckPing();
			
			if(dumpPing.get() > 5)
				client.tryReConnectServer();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				
			}
			
		}
		
		super.run();
	}

	
}
