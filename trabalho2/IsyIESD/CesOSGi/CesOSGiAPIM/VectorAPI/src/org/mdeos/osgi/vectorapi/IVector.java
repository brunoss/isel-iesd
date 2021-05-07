package org.mdeos.osgi.vectorapi;

import java.util.Collection;

public interface IVector {
	int read(String token, int idx);
	void write(String token, int idx, int value);
}
