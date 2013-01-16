package info.ilyaraz.passwordgenerator.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Set;

public class HashCalculator {
	public static String getPassword(String masterPassword, String clue, int passwordLength, Set<Character> alphabet) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-512");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		digest.update(masterPassword.getBytes());
		digest.update(clue.getBytes());
		BigInteger num = BigInteger.ZERO;
		byte[] hash = digest.digest();
		for (int i = 0; i < hash.length; ++i) {
			num = num.multiply(BigInteger.valueOf(256)).add(BigInteger.valueOf((hash[i] >= 0) ? hash[i] : (((int)hash[i]) + 256)));
		}
		return num.toString();
	}
}
