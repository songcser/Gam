package com.stark.web.service;

import java.util.Random;

public class NumberManager implements INumberManager{

	private static long number = 0;
	public static long numberGenerator(){
		number ++;
		return number;
	}
	
	public static int[] GetRandomSequence(int total)
    {

        int[] sequence = new int[total];
        int[] output = new int[total];

        for (int i = 0; i < total; i++)
        {
            sequence[i] = i;
        }

        Random random = new Random();

        int end = total - 1;

        for (int i = 0; i < total; i++)
        {
            int num = random.nextInt( end + 1);
            output[i] = sequence[num];
            sequence[num] = sequence[end];
            end--;
        }

        return output;
    }

	public static long getNumber() {

		return number;
	}
	
	public static void setNumber(long n){
		number = n;
	}
}
