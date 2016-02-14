package com.github.tarsys.android.support.utilities.entities;

import android.graphics.Bitmap;

/**
 * Creado por TaRSyS el 7/04/14.
 */
public class TelephoneContact
{
	private final Bitmap contactPhoto;
	private final String phoneNumber;
	private final String contactName;
	private final String eMail;

	public TelephoneContact(Bitmap contactPhoto, String phoneNumber, String contactName, String mail){
		this.contactPhoto = contactPhoto;
		this.phoneNumber = phoneNumber;
		this.contactName = contactName;
		this.eMail = mail;
	}

	public Bitmap getContactPhoto() {
		return contactPhoto;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getContactName() {
		return contactName;
	}

	public String geteMail() {
		return eMail;
	}
}
