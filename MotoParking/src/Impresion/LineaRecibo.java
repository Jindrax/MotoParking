package Impresion;

import java.awt.Font;

public class LineaRecibo {
	public String linea;
	public int mod;
	public int size;
	/**
	 * @param linea
	 * @param mod
	 * @param size
	 */
	public LineaRecibo(String linea, int mod, int size) {
		super();
		this.linea = linea;
		this.mod = mod;
		this.size = size;
	}
	/**
	 * @param linea
	 */
	public LineaRecibo(String linea) {
		super();
		this.linea = linea;
		this.mod = Font.PLAIN;
		this.size = 10;
	}
	
}
