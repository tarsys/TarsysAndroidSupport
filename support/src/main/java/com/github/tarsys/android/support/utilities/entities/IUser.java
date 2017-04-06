package com.github.tarsys.android.support.utilities.entities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Base64;

import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * Created by tarsys on 7/01/15.
 * (c) TaRSyS
 */
public abstract class IUser implements Serializable {
    private String login = "";
    private String password = "";
    private String customerId = "";
    private String userName = "";
    private boolean creation = false;
    private boolean defaultUser = false;

    public IUser(){
        this.login = "";
        this.password = "";
        this.customerId = "";
        this.userName = "";
        this.creation = false;
    }

    @Override
    public String toString(){
        return this.userName;
    }

    public IUser(String userName, boolean creation){
        this();
        this.userName = userName;
        this.creation = creation;
    }

    public boolean isCreation(){
        return this.creation;
    }

    public IUser(String login, String password) {
        this();
        this.login = login;
        this.password = password;
    }

    public static <T> T fromAccount(Context ctx, Class<T> classType, Account account){
        T retorno = null;
        try{
            retorno = (T) new GsonBuilder().create().fromJson(new String(Base64.decode(AccountManager.get(ctx).getPassword(account), Base64.DEFAULT)), classType);
        }catch (Exception ex){

        }
        return retorno;
    }

    public String getAccountName(){
        return String.format("%s (%s)", this.userName, this.login);
    }

    public boolean isDefaultUser()
    {
        return defaultUser;
    }

    public void setDefaultUser(boolean defaultUser) {
        this.defaultUser = defaultUser;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getLogin()
    {
        return login;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

}
