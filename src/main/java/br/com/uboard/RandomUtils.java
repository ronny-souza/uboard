package br.com.uboard;

import java.util.Random;

public class RandomUtils {
	public static RandomUtils getInstance() {
		return new RandomUtils();
	}

	public String generateRandomCode() {
		Random random = new Random();
		int number = random.nextInt(999999);

		return String.format("%06d", number);
	}

}
