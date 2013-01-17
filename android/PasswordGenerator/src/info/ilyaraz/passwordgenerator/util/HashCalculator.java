package info.ilyaraz.passwordgenerator.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.util.Base64;

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
		List<Character> chars = new ArrayList<Character>(new TreeSet<Character>(alphabet));
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < passwordLength; ++i) {
			res.append(chars.get(num.mod(BigInteger.valueOf(chars.size())).intValue()));
			num = num.divide(BigInteger.valueOf(chars.size()));
		}
		return res.toString();
	}
	
	public static String base64SHA512(String text) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-512");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return Base64.encodeToString(digest.digest(text.getBytes()), Base64.DEFAULT);
	}
}
