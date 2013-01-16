package info.ilyaraz.passwordgenerator.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ClueData implements Serializable {
	public ClueData(String id, String clueName, int passwordLength, Collection<Character> alphabet) {
		this.id = id;
		this.clueName = clueName;
		this.passwordLength = passwordLength;
		this.alphabet = new HashSet<Character>(alphabet);
	}
	
	private String id;
	private String clueName;
	private int passwordLength;
	private HashSet<Character> alphabet;
	
	public String getId() {
		return id;
	}
	
	public String getClueName() {
		return clueName;
	}
	
	public int getPasswordLength() {
		return passwordLength;
	}
	
	public Set<Character> getAlphabet() {
		return Collections.unmodifiableSet(alphabet);
	}
	
	public String toString() {
		return clueName;
	}
}
