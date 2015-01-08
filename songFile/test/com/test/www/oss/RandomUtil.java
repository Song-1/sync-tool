/**
 * 
 */
package com.test.www.oss;

import java.util.Random;

/**
 * @author Administrator
 *
 */
public class RandomUtil {
	
	public static int randomId(int start,int end){
		int counts = end - start;
		Random random = new Random();
		int randomNUmber = random.nextInt(counts + 1);
		randomNUmber += start;
		return randomNUmber;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(randomId(0,1));
	}

}
