package com.example.TrafficJam;

/**
 * Created with IntelliJ IDEA.
 * User: logan
 * Date: 11/1/13
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class Car
{
	public int x;
	public int y;
	public int length;
	public char alignment;

	public Car()
	{

	}

	public Car(int x, int y, int length, char alignment)
	{
		this.x = x;
		this.y = y;
		this.length = length;
		this.alignment = alignment;
	}
}
