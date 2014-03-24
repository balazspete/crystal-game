/**
 * 
 */
package com.example.crystalgame.game.energy;

import java.util.Date;

/**
 * Event class to represent the available energy level
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class EnergyEvent {

	private Date energyLowTime;
	private double energyLevel;
	
	/**
	 * 
	 */
	public EnergyEvent() {
	}
	
	public Date getEnergyLowTime() {
		return energyLowTime;
	}
	
	public void setEnergyLowTime(Date energyLowTime) {
		this.energyLowTime = energyLowTime;
	}
	
	public double getEnergyLevel() {
		return energyLevel;
	}
	
	public void setEnergyLevel(double energyLevel) {
		this.energyLevel = energyLevel;
	}
	
	@Override
	public String toString() {
		return (energyLowTime.toString()+" | "+energyLevel);
	}
}
