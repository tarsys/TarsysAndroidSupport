package com.github.tarsys.android.support.utilities.entities;

/**
 * Creado por TaRSyS el 7/04/14.
 */
public class LatLngPoint
{
	private final double latitud;
	private final double longitud;

	public LatLngPoint(double lat, double lng){
		this.latitud = lat;
		this.longitud = lng;
	}

	public double Latitud(){
		return this.latitud;
	}

	public double Longitud(){
		return this.longitud;
	}

	public boolean EsPuntoGeografico(){
		return this.latitud != 0 || this.longitud != 0;
	}
}
