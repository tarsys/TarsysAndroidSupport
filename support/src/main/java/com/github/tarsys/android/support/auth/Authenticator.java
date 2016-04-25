package com.github.tarsys.android.support.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import com.github.tarsys.android.support.R;
import com.github.tarsys.android.support.auth.enums.AuthenticatorLoginType;
import com.github.tarsys.android.support.auth.enums.AuthenticatorOperation;
import com.github.tarsys.android.support.utilities.AndroidSupport;
import com.github.tarsys.android.support.utilities.entities.IUser;

import java.util.ArrayList;

/**
 * Created by tarsys on 11/04/15.
 * (c) TaRSyS
 */
public class Authenticator extends AbstractAccountAuthenticator {

    private final Context context;
    private final Class classActivAuthenticator;
    private final int resourceAccountType;

    //region Variables públicas static

    public static final String ACTION_OPERATION = "ActionOperation";
    public static final String ACTION_LOGINTYPE = "LoginType";

    public static final String KEY_AUTH_TOKEN_TYPE = "authTokenType";
    public static final String KEY_REQUIRED_FEATURES = "requiredFeatures";
    public static final String KEY_LOGIN_OPTIONS = "loginOptions";
    public static final String KEY_ACCOUNT = "account";
    private boolean onlyOneAccount;

    //endregion

    //region Clases Privadas

    /**
     * Abstract exception for fail cases. Deals with building the failure bundle which can be returned from most
     * AbstractAccountAuthenticator functions.
     */
    private abstract class AuthenticatorException extends Exception
    {
        private static final long serialVersionUID = 1L;

        private Bundle mFailureBundle;

        protected AuthenticatorException(Context ctx, int pErrorCode, int pErrorMessageStringResourceID)
        {
            mFailureBundle = new Bundle();
            mFailureBundle.putInt(AccountManager.KEY_ERROR_CODE, pErrorCode);
            mFailureBundle.putString(AccountManager.KEY_ERROR_MESSAGE, ctx.getResources().getString(pErrorMessageStringResourceID));
        }

        public Bundle GetFailureBundle()
        {
            return mFailureBundle;
        }
    }

    /**
     * Exception to throw when a provided account type is not supported by this authenticator.
     */
    private class UnSupportedAccountTypeException extends AuthenticatorException
    {
        private static final long serialVersionUID = 1L;

        public UnSupportedAccountTypeException(Context ctx)
        {
            super(ctx, AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION, R.string.error_unsupported_account_type);
        }
    }

    /**
     * Exception to throw when a provided auth token type is not supported by this authenticator.
     */
    private class UnsupportedAuthTokenTypeException extends AuthenticatorException
    {
        private static final long serialVersionUID = 1L;

        public UnsupportedAuthTokenTypeException(Context ctx)
        {
            super(ctx, AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION, R.string.error_unsupported_auth_token_type);
        }
    }

    /**
     * Exception to throw when a provided feature set is not supported by this authenticator.
     */
    private class UnsupportedFeaturesException extends AuthenticatorException
    {
        private static final long serialVersionUID = 1L;

        public UnsupportedFeaturesException(Context ctx)
        {
            super(ctx, AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION, R.string.error_unsupported_features);
        }
    }

    //endregion

    //region Métodos privados

    private void validateAccountType(String pAccountType) throws UnSupportedAccountTypeException
    {
        if (!pAccountType.equals(this.context.getResources().getString(this.resourceAccountType)))
        {
            throw new UnSupportedAccountTypeException(this.context);
        }
    }

    private void validateAuthTokenType(String pAuthTokenType) throws UnsupportedAuthTokenTypeException
    {
    }

    private void validateRequiredFeatures(String[] pRequiredFeatures) throws UnsupportedFeaturesException
    {
    }

    //endregion

    //region Métodos públicos

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse arg0, String arg1)
    {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse pResponse, String pAccountType, String pAuthTokenType, String[] pRequiredFeatures, Bundle pLoginOptions) throws NetworkErrorException
    {

        if (this.onlyOneAccount && !AndroidSupport.loadUserAccounts(this.context, pAccountType).isEmpty()) {
            Toast.makeText(this.context, R.string.error_only_one_account_allowed, Toast.LENGTH_LONG).show();
            return null;
        }
        /* Validate the input. */
        try
        {
            validateAccountType(pAccountType);
            validateAuthTokenType(pAuthTokenType);
            validateRequiredFeatures(pRequiredFeatures);
        }
        catch (AuthenticatorException lException)
        {
            return lException.GetFailureBundle();
        }

        /*
         * Create and return an intent that will result in an activity that will prompt the user for credentials and add
         * the account to the local credential store (database, preferences, etc.).
         */
        Intent lIntent = new Intent(this.context, this.classActivAuthenticator);
        lIntent.putExtra(AccountManager.KEY_ACCOUNT_MANAGER_RESPONSE, pResponse);
        lIntent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, pAccountType);
        lIntent.putExtra(KEY_AUTH_TOKEN_TYPE, pAuthTokenType);
        lIntent.putExtra(KEY_REQUIRED_FEATURES, pRequiredFeatures);
        lIntent.putExtra(KEY_LOGIN_OPTIONS, pLoginOptions);
        lIntent.putExtra(Authenticator.ACTION_OPERATION, AuthenticatorOperation.NewAccount.toString());
        lIntent.putExtra(Authenticator.ACTION_LOGINTYPE, AuthenticatorLoginType.Authenticator.toString());

        Bundle lBundle = new Bundle();
        lBundle.putParcelable(AccountManager.KEY_INTENT, lIntent);

        return lBundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse arg0, Account arg1, Bundle arg2) throws NetworkErrorException
    {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse arg0, Account arg1, String arg2, Bundle arg3) throws NetworkErrorException
    {
        return null;
    }

    @Override
    public String getAuthTokenLabel(String arg0)
    {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle loginOptions) throws NetworkErrorException
    {
        try
        {
            validateAuthTokenType(authTokenType);
        }
        catch (AuthenticatorException lException)
        {
            return lException.GetFailureBundle();
        }

        /*
         * Create and return an intent that will result in an activity that will prompt the user for credentials and add
         * the account to the local credential store (database, preferences, etc.).
         */
        Intent lIntent = new Intent(this.context, this.classActivAuthenticator);
        lIntent.putExtra(AccountManager.KEY_ACCOUNT_MANAGER_RESPONSE, response);
        lIntent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, this.context.getString(this.resourceAccountType));
        lIntent.putExtra(KEY_AUTH_TOKEN_TYPE, authTokenType);
        lIntent.putExtra(KEY_LOGIN_OPTIONS, loginOptions);
        lIntent.putExtra(Authenticator.ACTION_OPERATION, AuthenticatorOperation.UpdateAccount);
        lIntent.putExtra(Authenticator.ACTION_LOGINTYPE, AuthenticatorLoginType.Authenticator);
        lIntent.putExtra(Authenticator.KEY_ACCOUNT, account);

        Bundle lBundle = new Bundle();
        lBundle.putParcelable(AccountManager.KEY_INTENT, lIntent);

        return lBundle;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse arg0, Account arg1, String[] arg2) throws NetworkErrorException
    {
        return null;
    }

    //endregion

    public Authenticator(Context context, Class classActivAuthenticator, int resourceAccountType) {
        super(context);
        this.context = context;
        this.classActivAuthenticator = classActivAuthenticator;
        this.resourceAccountType = resourceAccountType;
        this.onlyOneAccount = false;
    }

    public Authenticator(Context context, Class classActivAuthenticator, int resourceAccountType, boolean onlyOneAccount) {
        this(context, classActivAuthenticator, resourceAccountType);
        this.onlyOneAccount = onlyOneAccount;
    }

    public Context getContext() {
        return context;
    }

    /**
     * Create or Update Account into the Android Operating System
     * @param iUser
     * @return true if the account is succesfully created or updated, false otherwise
     */
    public <T> boolean createOrUpdateAndroidAccount(IUser iUser, Class<T> userClass){
        boolean returnValue = false;
        try {
            AccountManager accountManager = AccountManager.get(this.context);

            ArrayList<Account> accounts = AndroidSupport.loadUserAccounts(this.context, this.context.getString(this.resourceAccountType));
            Account accountUser = null;

            if (accounts != null && accounts.size() > 0) {
                // If we have some user accounts... we can search if the actual account exists...


                for (Account account : accounts) {
                    IUser user = (IUser) IUser.fromAccount(context, userClass, account);
                    if (user != null && user.getLogin().equals(user.getLogin())) {
                        // the login are the same... catched!
                        accountUser = account;
                        break;
                    }
                }
            }

            String jsonUser = new GsonBuilder().create().toJson(iUser);
            String serializedIUser = Base64.encodeToString(jsonUser.getBytes(), Base64.DEFAULT);


            if (accountUser == null) {
                // New account...
                accountUser = new Account(iUser.getAccountName(), this.context.getString(this.resourceAccountType));
                accountManager.addAccountExplicitly(accountUser, serializedIUser, Bundle.EMPTY);
            } else {
                // updated account...
                accountManager.setPassword(accountUser, serializedIUser);
            }
            returnValue = true;

        }catch(Exception ex){
            returnValue = false;
        }
        return returnValue;
    }
}
