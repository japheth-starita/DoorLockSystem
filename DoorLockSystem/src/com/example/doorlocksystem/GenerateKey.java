package com.example.doorlocksystem;

import java.util.Random;

public class GenerateKey {
	private static int privateKey;
	
	public static void genprivateNum(){
		Random r = new Random();
		int min = 10;
		int max = 97; //highest 2-digit prime number
		int ran = r.nextInt(max - min + 1) + min;
		while(!(isPrime(ran))){
		
			ran+=1;
		}
		privateKey = ran;
	}
	
	private static boolean isPrime(int n) {
	    // fast even test.
	    if(n > 2 && (n & 1) == 0)
	       return false;
	    // only odd factors need to be tested up to n^0.5
	    for(int i = 3; i * i <= n; i += 2)
	        if (n % i == 0) 
	            return false;
	    return true;
	}

	

	public static int getPrivateKey() {
		return privateKey;
	}
	
	//when sending product key
	//return value of public key and private key to 0
	public static int getProductKey(int publicKey){
		resetKey();
		return publicKey * privateKey;
	}
	
	public static void resetKey(){
		privateKey =  0;
	}
}
