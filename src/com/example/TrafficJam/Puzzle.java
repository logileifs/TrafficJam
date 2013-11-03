package com.example.TrafficJam;

/**
 * Created with IntelliJ IDEA.
 * User: logan
 * Date: 10/31/13
 * Time: 10:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Puzzle
{

	public int level;
	public int number;
	public String setup;
	public String description;
    public boolean isFinished;

	public Puzzle()
	{

	}

	public Puzzle(int number, int level, String setup, boolean finished)
	{
		this.number = number;
		this.level = level;
		this.setup = setup;
        this.isFinished = finished;
		setDescription(this.number, this.level);
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public void setSetup(String setup)
	{
		this.setup = setup;
	}

	public void setDescription(int number, int level)
	{
		this.description = "Puzzle " + number + "\n" + "Level " + level;
	}
}
