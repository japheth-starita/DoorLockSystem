package com.example.doorlocksystem;

import java.util.Random;

public class GenerateKey {
	private int publicKey, privateKey;
	
	private void genpublicNum(){
		Random r = new Random();
		int min = 1000;
		int max = 10000;
		int ran = r.nextInt(max - min + 1) + min;
		while(!(isPrime(ran))){
		
			ran+=1;
		}
	}
	
	private int genprivateNum(){
		Random r = new Random();
		int min = 10;
		int max = 97; //highest 2-digit prime number
		int ran = r.nextInt(max - min + 1) + min;
		while(!(isPrime(ran))){
		
			ran+=1;
		}
		return ran;
	}
	
	private boolean isPrime(int n) {
	    // fast even test.
	    if(n > 2 && (n & 1) == 0)
	       return false;
	    // only odd factors need to be tested up to n^0.5
	    for(int i = 3; i * i <= n; i += 2)
	        if (n % i == 0) 
	            return false;
	    return true;
	}

	public int getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(int publicKey) {
		this.publicKey = publicKey;
	}

	public int getPrivateKey() {
		return genprivateNum();
	}

	public void setPrivateKey(int privateKey) {
		this.privateKey = privateKey;
	}
}
