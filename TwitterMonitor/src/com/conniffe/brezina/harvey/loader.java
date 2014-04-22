package com.conniffe.brezina.harvey;

public final class loader extends Thread {
    
    public void run() {
	try {
	    Thread.sleep(60000);
	    rl();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public void rl() {
	try {
	    main.load();
	    Thread.sleep(60000);
	    rl();
	}
	catch (Exception e) {
            e.printStackTrace();
	}
    }
}