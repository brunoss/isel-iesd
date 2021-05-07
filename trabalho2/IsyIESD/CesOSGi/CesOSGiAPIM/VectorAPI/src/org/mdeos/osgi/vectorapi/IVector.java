package org.mdeos.osgi.vectorapi;

public interface IVector {
	int read(int idx);
	void write(int idx, int value);
}
