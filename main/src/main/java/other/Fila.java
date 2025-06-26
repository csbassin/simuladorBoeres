package main.java.other;

import java.util.ArrayList;

public class Fila <T> extends ArrayList<T>{
	
	public Fila(){
		super();
	}
	
	public void enqueue(T elemento) {
		this.addFirst(elemento);
	}
	public T unqueue() {
		return this.removeLast();
	}

}
