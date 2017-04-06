package com.github.tarsys.android.support.auth.listeners;



import com.github.tarsys.android.support.utilities.entities.IUser;

import java.util.EventListener;

/**
 * Created by tarsys on 11/04/15.
 * (c) TaRSyS
 */
public interface OnAuthenticationListener extends EventListener {
    void AuthenticationResult (IUser user, String errorMessage);
}
