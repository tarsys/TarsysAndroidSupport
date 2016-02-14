package com.github.tarsys.android.support.utilities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.github.tarsys.android.support.R;
import com.github.tarsys.android.support.utilities.entities.IUser;
import com.github.tarsys.android.support.utilities.entities.TelephoneContact;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by tarsys on 21/9/15.
 */
public class AndroidSupport {
    /**
     * Displays a notification in the notification area of the device
     * @param context App Context
     * @param id Notification Unique Identifier
     * @param notificationIcon Icon Resource
     * @param barTitle Notification title
     * @param title Notification title when bar is expanded
     * @param text Notification text
     * @param notificationTime Time when the notification is showed at notification bar (in milliseconds)
     * @param audibleNotification true if the notification plays default sound, false otherwise
     * @param vibrateOnNotify true if notification launch vibration, false otherwise
     * @param onClickActivity Intent to execute when we click the notification
     */
    public static void ShowNotification(Context context, int id, int notificationIcon, String barTitle, String title, String text, long notificationTime, boolean audibleNotification, boolean vibrateOnNotify, Intent onClickActivity)
    {
        try
        {
            NotificationManager gestorNotificacion = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notificacion = new Notification(notificationIcon, barTitle, notificationTime);

            PendingIntent contentIntent = onClickActivity != null ? PendingIntent.getActivity(context, 0, onClickActivity, 0) : null;
            SharedPreferences preferencias = context.getSharedPreferences(context.getPackageName(), 0);
            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
            contentView.setImageViewResource(R.id.image, notificationIcon);
            contentView.setTextViewText(R.id.title, title);
            contentView.setTextViewText(R.id.text, text);
            notificacion.contentView = contentView;

            if (audibleNotification)
            {
                notificacion.defaults |= Notification.DEFAULT_SOUND;
            }
            if (vibrateOnNotify)
            {
                notificacion.defaults |= Notification.DEFAULT_VIBRATE;
            }
            notificacion.contentIntent = contentIntent;
            notificacion.flags = Notification.FLAG_AUTO_CANCEL;
            gestorNotificacion.cancel(id);

            gestorNotificacion.notify(id, notificacion);
        }
        catch (Exception ex)
        {
            Log.e(context.getPackageName(), ex.toString());
        }
    }


    public static byte[] InputStream2ByteArray (InputStream is){
        byte[] retorno = null;

        try{
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            // this is storage overwritten on each iteration with bytes
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            // we need to know how may bytes were read to write them to the byteBuffer
            int len = 0;

            while ((len = is.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            retorno = byteBuffer.toByteArray();

        }catch	(Exception ex){

        }

        return retorno;
    }

    /**
     * Set specified typeface to container recursively
     * @param v container view
     * @param fontRegular
     * @param fontBold
     * @param fontItalic
     * @param fontBoldItalic
     */
    public static void SetTypeFaceContainer (View v, Typeface fontRegular, Typeface fontBold, Typeface fontItalic, Typeface fontBoldItalic)
    {
        List<Class> clases = Arrays.asList(new Class[]{TextView.class, EditText.class, Button.class, CheckBox.class, RadioButton.class});

        if (v == null || fontRegular == null) return;

        int nHijos = v instanceof ViewGroup ? ((ViewGroup) v).getChildCount() : 1;

        for (int i = 0; i < nHijos; i++)
        {
            final View hijo = v instanceof ViewGroup ? ((ViewGroup) v).getChildAt(i) : v;
            if (hijo instanceof ViewGroup)
            {
                AndroidSupport.SetTypeFaceContainer((ViewGroup) hijo, fontRegular, fontBold, fontItalic, fontBoldItalic);
            }
            else if (clases.contains(hijo.getClass()))
            {
                Method mGetTypeface = null;

                try
                {
                    mGetTypeface = hijo.getClass().getMethod("getTypeface");
                }
                catch (Exception e)
                { /* Do something... */ }

                try
                {
                    if (mGetTypeface != null)
                    {
                        Typeface fOrig = (Typeface) mGetTypeface.invoke(hijo);

                        Typeface font = fOrig == null ? fontRegular : fOrig.isBold() && fOrig.isItalic() ? fontBoldItalic : fOrig.isBold() ? fontBold : fOrig.isItalic() ? fontItalic : fontRegular;

                        Method mSetTypeface = null;
                        if (hijo.getClass() == TextView.class){
                            ((TextView) hijo).setTypeface(font);
                        }else if (hijo.getClass() == EditText.class){
                            ((EditText) hijo).setTypeface(font);
                        }else if (hijo.getClass() == Button.class){
                            ((Button) hijo).setTypeface(font);
                        }else if (hijo.getClass() == CheckBox.class){
                            ((CheckBox) hijo).setTypeface(font);
                        }else if (hijo.getClass() == RadioButton.class){
                            ((RadioButton) hijo).setTypeface(font);
                        }
                    }
                }
                catch (Exception ex)
                {
                }
            }
        }
    }
    /**
     * Runs sending multiple streams to the users listed in the execution context that has provided
     * @param context App Context
     * @param title Window share selection title
     * @param contacts Contact list
     * @param extStreams Stream hashmap with streams to share <identifier, stream to share>
     */
    public static void ShareOutputStreams(Context context, String title, ArrayList<TelephoneContact> contacts, java.util.HashMap<String, ArrayList<ByteArrayOutputStream>> extStreams){
        final ArrayList<File> ficheros = new ArrayList<File>();

        String []emailsCCO = new String[contacts.size()];
        for(int i = 0; i < contacts.size(); i++) emailsCCO[i] = contacts.get(i).geteMail();
        String Urls = "";
        for (String kExt : extStreams.keySet()){
            ArrayList<ByteArrayOutputStream> streams = extStreams.get(kExt);
            if (kExt.equals(".uri")){

                for(ByteArrayOutputStream stream : streams){
                    Urls += "* <a href=\"" + new String(stream.toByteArray()) + "\">" + new String(stream.toByteArray()) + "</a><br/>\r\n";
                }


            }else{
                for(ByteArrayOutputStream stream : streams){
                    try
                    {
                        File tmpFile = File.createTempFile("Share_", kExt, new File(Environment.getExternalStorageDirectory() + "/"));
                        FileOutputStream fos = new FileOutputStream(tmpFile);
                        fos.write(stream.toByteArray());
                        fos.close();
                        ficheros.add(tmpFile);
                    } catch (java.io.IOException ex)
                    {
                        Log.e(SerializationSupport.class.getName(), ex.toString());
                    }
                }
            }
        }

        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.putExtra(Intent.EXTRA_BCC, emailsCCO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[" + context.getApplicationInfo().processName + "] " + title);
        emailIntent.setType("text/html");

        emailIntent.putExtra(Intent.EXTRA_TEXT, android.text.Html.fromHtml("Hola!, adjunto te env&iacute;o estos/as " + title +
                " que he encontrado.<br/><br/>" + Urls + "<br/>Saludos."));
        if (ficheros.size() > 0){
            ArrayList<Uri> uris = new ArrayList<>();
            for(File f : ficheros) uris.add(Uri.parse("file://" + f.getAbsolutePath()));
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        }
        context.startActivity(Intent.createChooser(emailIntent, "Enviar " + title + ":"));

        // Para terminar, se eliminan los archivos temporales creados...
        Handler hndBorrado = new Handler();
        //en cosa de 4 minutos, los ficheros se borrarán...
        hndBorrado.postDelayed(new Runnable() {
            public void run()
            {
                for(File f : ficheros) {
                    f.delete();
                }
            }
        }, 240000);

    }

    /**
     * Get user accounts from provided type.
     * @param context App context
     * @param accountType Account Type
     * @return Account List of provided type.
     */
    public static ArrayList<Account> LoadUserAccounts(Context context, String accountType){
        ArrayList<Account> retorno = new ArrayList<Account>();
        AccountManager gestionCuenta = AccountManager.get(context);
        Account []ctas = null;
        if (gestionCuenta != null)
        {
            try
            {
                ctas = gestionCuenta.getAccountsByType(accountType);
            }
            catch (Exception ex)
            {
                ctas = null;
            }
        }
        if (retorno.isEmpty()){
            if (ctas != null){
                retorno.addAll(Arrays.asList(ctas));
            }
        }
        return retorno;
    }

    /**
     * Gets the User account identified by its type and name
     * @param context
     * @param accountType
     * @param accountName
     * @return
     */
    public static Account getUserAccount(Context context, String accountType, String accountName){
        Account retorno = null;
        ArrayList<Account> cuentas = AndroidSupport.LoadUserAccounts(context, accountType);
        for(Account cta : cuentas) {
            if (cta.name.equalsIgnoreCase(accountName)){
                retorno = cta;
                break;
            }
        }
        return retorno;
    }

    /**
     * Saves IUser entity as Account into Android Account management system
     * Require permissions android.permission.MANAGE_ACCOUNTS, android.permission.AUTHENTICATE_ACCOUNTS, android.permission.USE_CREDENTIALS, android.permission.GET_ACCOUNTS
     * @param context
     * @param accountType
     * @param iUser
     * @return true if the account has been created / updated succesfully, false otherwise
     */
    public static boolean SaveUserAccount(Context context, String accountType, IUser iUser){
        boolean retorno = false;

        if (iUser != null){
            AccountManager gestorCuenta = AccountManager.get(context);
            String jsonUsuario = new GsonBuilder().create().toJson(iUser);
            String objSerializadoLogin = Base64.encodeToString(jsonUsuario.getBytes(), Base64.DEFAULT);

            Account cuentaUsuario = AndroidSupport.getUserAccount(context, accountType, iUser.getAccountName());

            try {
                // Si la cuenta existe... se actualiza, si es nueva... se crea...
                if (cuentaUsuario == null) {
                    cuentaUsuario = new Account(iUser.getAccountName(), accountType);
                    retorno = gestorCuenta.addAccountExplicitly(cuentaUsuario, objSerializadoLogin, Bundle.EMPTY);
                } else {
                    gestorCuenta.setPassword(cuentaUsuario, objSerializadoLogin);
                    retorno = true;
                }
            }catch(Exception ex){
                retorno = false;
            }
        }

        return retorno;
    }

    /**
     * Gets an object date we interpret as null date (01/01/1900)
     * @return
     */
    public static Date NullDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(1900, Calendar.JANUARY, 1);

        return cal.getTime();
    }

    public static String ValidSpanishIdentityDocument(String DNI){

        String juegoCaracteres="TRWAGMYFPDXBNJZSQVHLCKET";
        String retorno = DNI;

        ArrayList<String> letrasNIE = new ArrayList<String>(Arrays.asList(new String []{"X", "Y", "Z"}));
        ArrayList<String> letrasCIF = new ArrayList<String>(Arrays.asList(new String []{"A","B","C","D","E","F","G","H","K","L","M","N","P","Q","S","U","V","W"}));

        String primerCaracter = String.valueOf(DNI.charAt(0)).toUpperCase();
        String numeroDNI = DNI.substring(letrasNIE.contains(primerCaracter) ? 1 : 0);

        if (Pattern.matches("\\d", primerCaracter) || letrasNIE.contains(primerCaracter)) // Solo actuaremos si es un nif normal
        {
            // Si es un NIE, el primer caracter será la posición de la letra en el sitio...
            if (letrasNIE.contains(primerCaracter)) numeroDNI = String.valueOf(letrasNIE.indexOf(primerCaracter)) + numeroDNI;

            // Comprobamos si el último caracter del nif es un número, en caso contrario, eliminaremos ese último caracter...
            if (Pattern.matches("\\D",String.valueOf(numeroDNI.charAt(numeroDNI.length()-1)))){
                numeroDNI = numeroDNI.substring(0, numeroDNI.length()-1);
            }
            int dniNum = Integer.parseInt(numeroDNI);

            int modulo= dniNum % 23;
            char letra = juegoCaracteres.charAt(modulo);
            if (letrasNIE.contains(primerCaracter)){
                retorno = String.format("%s%08d%s", primerCaracter,dniNum,letra);
            }else{
                retorno = String.format("%09d%s", dniNum,letra);
            }
        }else if (letrasCIF.contains(primerCaracter)){ // Es un CIF
            String nroCIF = DNI.substring(1);
            retorno = "";
            if (nroCIF.length() >= 7){
                nroCIF = nroCIF.substring(0,7);
                // Ahora, comenzamos el cálculo del dígito de control del CIF...
                if (!Pattern.matches("\\D",nroCIF)){
                    int[]calculoImpar = new int[]{0,2,4,6,8,1,3,5,7,9};
                    int a = 0, b = 0, x;
                    for(x=1;x<=5;x+=2)  {
                        a += Integer.parseInt(String.valueOf(nroCIF.charAt(x)));
                        b += calculoImpar[Integer.parseInt(String.valueOf(nroCIF.charAt(x-1)))];
                    }
                    b += calculoImpar[Integer.parseInt(String.valueOf(nroCIF.charAt(x-1)))];

                    String[] letrasCif =  new String []{"J","A","B","C","D","E","F","G","H","I"};
                    ArrayList<String> letrasCifConLetra = new ArrayList<String>(Arrays.asList(new String[]{"N","P","Q","R","S","W"}));
                    int d = 10 - ((a + b)%10);
                    String ultCaracter = letrasCifConLetra.contains(primerCaracter)? letrasCif[d] : String.valueOf(d);
                    retorno = primerCaracter + nroCIF + ultCaracter;
                }
            }
        }

        return retorno;
    }

    public static LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap =
                new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)){
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String)key, (Integer)val);
                    break;
                }

            }

        }
        return sortedMap;
    }

    /**
     * Determine if the ads are blocked
     * @return true if the ads are blocked, false otherwise
     */
    public static boolean isAdsBlocked(){
        boolean retorno = false;

        BufferedReader in;

        try
        {
            in = new BufferedReader(new InputStreamReader(new FileInputStream("/etc/hosts")));
            String line;

            while ((line = in.readLine()) != null)
            {
                if (line.contains("admob"))
                {
                    retorno = true;
                    break;
                }
            }
        }catch(IOException ex){
            retorno = false;
        }

        return retorno;
    }

    /**
     * Gets ArrayList with all Contacts on device
     * @param context App Context
     * @param resDefaultPhoto Photo default  ResourceId  (when the contact haven't a photo)
     * @param onlyContactsWithTelephone true, gets only contacts with telephone number, false gets all
     * @param contactsWithEmail true, gets contacts with email, false gets all
     * @return
     */
    public static ArrayList<TelephoneContact> getTelephoneContacts(Context context, int resDefaultPhoto, boolean onlyContactsWithTelephone, boolean contactsWithEmail){

        BitmapDrawable drawableFotoDefecto = ((BitmapDrawable)context.getResources().getDrawable(resDefaultPhoto));

        return getTelephoneContacts(context, drawableFotoDefecto, onlyContactsWithTelephone, contactsWithEmail);
    }

    public static ArrayList<TelephoneContact> getTelephoneContacts(Context context, int resDefaultPhoto, boolean onlyContactsWithTelephone, boolean contactsWithEmail, boolean onlyVisibleContacts){

        BitmapDrawable drawableFotoDefecto = ((BitmapDrawable)context.getResources().getDrawable(resDefaultPhoto));

        return getTelephoneContacts(context, drawableFotoDefecto, onlyContactsWithTelephone, contactsWithEmail, onlyVisibleContacts);
    }

    public static ArrayList<TelephoneContact> getTelephoneContacts(Context context, BitmapDrawable defaultPhotoDrawable, boolean onlyContactsWithTelephone, boolean contactsWithEmail){
        return getTelephoneContacts(context, defaultPhotoDrawable, onlyContactsWithTelephone, contactsWithEmail, false);
    }
    public static ArrayList<TelephoneContact> getTelephoneContacts(Context context, BitmapDrawable defaultPhotoDrawable, boolean onlyContactsWithTelephone, boolean contactsWithEmail, boolean onlyVisibleContacts){
        ArrayList<TelephoneContact> retorno = new ArrayList<TelephoneContact>();
        ContentResolver cResolver = context.getContentResolver();

        if (onlyContactsWithTelephone){


            // Primero buscamos el Contact_Id a partir del teléfono...

            Cursor cur = cResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            while (cur.moveToNext()){
                int idContacto = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String telefono = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (idContacto > 0){
                    // Si tenemos id de contacto, sacamos sus datos...
                    Cursor curContacto = cResolver.query(ContactsContract.Contacts.CONTENT_URI,
                            null,
                            String.format("%s = ? %s %s %s", ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                    onlyVisibleContacts? "and" : "",
                                    onlyVisibleContacts? ContactsContract.Contacts.IN_VISIBLE_GROUP : "",
                                    onlyVisibleContacts? " = ? " : ""),
                            onlyVisibleContacts? new String []{ String.valueOf(idContacto), "1" } :
                                    new String []{ String.valueOf(idContacto) },
                            null);
                    if (curContacto.moveToNext()){
                        int idFoto = curContacto.getInt(curContacto.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                        // La foto, por defecto
                        Bitmap foto = defaultPhotoDrawable.getBitmap();
                        // Buscamos la foto que corresponde...

                        Uri uriFoto = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idContacto);


                        if (uriFoto != null){
                            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cResolver, uriFoto);
                            Bitmap photo = BitmapFactory.decodeStream(input);
                            if (photo != null){
                                foto = photo;
                            }
                        }
                        String eMail = "";

                        Cursor curEmail = cResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String []{String.valueOf(idContacto)},null);
                        if (curEmail != null){
                            if (curEmail.moveToNext()){
                                eMail = curEmail.getString(curEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            }
                            curEmail.close();
                        }
                        TelephoneContact contacto = new TelephoneContact(foto,
                                telefono,
                                curContacto.getString(curContacto.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                                eMail);
                        if ((contactsWithEmail && !eMail.trim().equals("")) || !contactsWithEmail){
                            retorno.add(contacto);
                        }
                    }
                    curContacto.close();

                }
            }
            if (!cur.isClosed()){
                cur.close();
            }
        }else{
            // Salen todos...
            Cursor curContacto = cResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null,
                    onlyVisibleContacts ? ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ?" : null,
                    onlyVisibleContacts ? new String[]{"1"} : null,
                    null);
            while (curContacto.moveToNext()){
                int idFoto = curContacto.getInt(curContacto.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                int idContacto = curContacto.getInt(curContacto.getColumnIndex(ContactsContract.Contacts._ID));
                String telefono = "";
                Cursor curTlf = cResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String []{ String.valueOf(idContacto) },
                        null);
                if (curTlf.moveToNext()){
                    telefono = curTlf.getString(curTlf.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                curTlf.close();
                // La foto, por defecto
                Bitmap foto = defaultPhotoDrawable.getBitmap();
                // Buscamos la foto que corresponde...

                Uri uriFoto = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idContacto);


                if (uriFoto != null){
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cResolver, uriFoto);
                    Bitmap photo = BitmapFactory.decodeStream(input);
                    if (photo != null){
                        foto = photo;
                    }
                }
                String eMail = "";

                Cursor curEmail = cResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String []{String.valueOf(idContacto)},null);
                if (curEmail.moveToNext()){
                    eMail = curEmail.getString(curEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                curEmail.close();
                TelephoneContact contacto = new TelephoneContact(foto,
                        telefono,
                        curContacto.getString(curContacto.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                        eMail);
                if ((contactsWithEmail && !eMail.trim().equals("")) || !contactsWithEmail){
                    retorno.add(contacto);
                }
            }
            curContacto.close();
        }
        return retorno;
    }

    /**
     * Gets contact identified by provided phone number
     * @param context App Context
     * @param resDefaultPhoto Photo default  ResourceId  (when the contact haven't a photo)
     * @param telephoneNumber contact phone number
     * @return
     */
    public static TelephoneContact getTelephoneContact(Context context, int resDefaultPhoto, String telephoneNumber){
        TelephoneContact retorno = null;
        ContentResolver cResolver = context.getContentResolver();

        // Primero buscamos el Contact_Id a partir del teléfono...
        try{
            String tlfLike;
            if (telephoneNumber.startsWith("+34") && telephoneNumber.length() >=12){
                tlfLike="%" + telephoneNumber.substring(3,6) + "%" + telephoneNumber.substring(6,8) + "%" + telephoneNumber.substring(8,10) + "%" + telephoneNumber.substring(10,12);
            }
            else if (telephoneNumber.length() >=9){
                tlfLike="%" + telephoneNumber.substring(0,3) + "%" + telephoneNumber.substring(3,5) + "%" + telephoneNumber.substring(5,7) + "%" + telephoneNumber.substring(7,9);
            }else{
                tlfLike = "%" + telephoneNumber;
            }
            Cursor cur = cResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.NUMBER +" like ?",
                    new String []{tlfLike},
                    null);
            while (cur.moveToNext()){
                int idContacto = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                if (idContacto > 0){
                    // Si tenemos id de contacto, sacamos sus datos...
                    Cursor curContacto = cResolver.query(ContactsContract.Contacts.CONTENT_URI,
                            null,
                            ContactsContract.Contacts._ID +" = ?",
                            new String []{String.valueOf(idContacto)},
                            null);
                    if (curContacto.moveToNext()){
                        int idFoto = curContacto.getInt(curContacto.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                        // La foto, por defecto
                        Bitmap foto = ((BitmapDrawable)context.getResources().getDrawable(resDefaultPhoto)).getBitmap();
                        // Buscamos la foto que corresponde...

                        Uri uriFoto = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idContacto);


                        if (uriFoto != null){
                            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cResolver, uriFoto);
                            Bitmap photo = BitmapFactory.decodeStream(input);
                            if (photo != null){
                                foto = photo;
                            }
                        }
                        String eMail = "";

                        Cursor curEmail = cResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String []{String.valueOf(idContacto)},null);
                        if (curEmail != null){
                            if (curEmail.moveToNext()){
                                eMail = curEmail.getString(curEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            }
                        }
                        retorno = new TelephoneContact(foto,
                                telephoneNumber,
                                curContacto.getString(curContacto.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                                eMail);
                    }
                    curContacto.close();

                }
            }
            if (!cur.isClosed()){
                cur.close();
            }
        }catch (Resources.NotFoundException ex){

        }
        if (retorno == null){
            Bitmap foto = ((BitmapDrawable)context.getResources().getDrawable(resDefaultPhoto)).getBitmap();
            retorno = new TelephoneContact(foto, telephoneNumber,telephoneNumber,"");
        }
        return retorno;
    }

    public static double String2Double(String s){
        double retorno;

        try{
            retorno = Double.parseDouble(s.replace(',', '.'));
        }catch(NumberFormatException ex){
            retorno = 0;
        }

        return retorno;
    }

    public static long String2Long(String s){
        long retorno;

        try{
            retorno = Long.parseLong(s);
        }catch(NumberFormatException ex){
            retorno = 0;
        }

        return retorno;
    }

    public static HttpsURLConnection getHttpsUrlConnection(String url, HashMap<String,String> headers, String method, HashMap<String,String> postValues){
        HttpsURLConnection returnValue = null;

        try{
            HttpsURLConnection.setDefaultSSLSocketFactory(new TLSSocketFactory());
            URL urlConex = new URL(url);

            returnValue = (HttpsURLConnection) urlConex.openConnection();

            returnValue.setRequestMethod(method.toUpperCase());
            returnValue.setDoInput(true);
            returnValue.setDoOutput(method.toUpperCase().equals("POST"));

            if (headers != null) {
                for (String key : headers.keySet())
                    returnValue.setRequestProperty(key, headers.get(key));
            }

            String requestData = "";

            if (postValues != null){
                for(String key : postValues.keySet()){
                    requestData += (requestData.isEmpty() ? "" : "&") + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(postValues.get(key), "UTF-8");
                }
            }

            returnValue.setSSLSocketFactory(new TLSSocketFactory());
            if (method.equalsIgnoreCase("post")) {
                if (!requestData.trim().isEmpty()) {
                    if (!headers.containsKey("Content-Type"))
                        returnValue.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    returnValue.setRequestProperty("Content-Length", Integer.toString(requestData.getBytes().length));
                    returnValue.setFixedLengthStreamingMode(requestData.getBytes().length);
                    OutputStream os = returnValue.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(requestData);
                    writer.flush();
                    writer.close();
                    os.close();
                }
            }

            if (!method.equalsIgnoreCase("post"))
                returnValue.connect();
        }catch (Exception ex){
            returnValue = null;
        }

        return returnValue;
    }


    public static DefaultHttpClient getNewHttpClient() {
        DefaultHttpClient retorno;

        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {

            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                // retry a max of 7 times
                boolean retorno = false;
                if(executionCount < 7){
                    if(exception instanceof NoHttpResponseException || exception instanceof ClientProtocolException){
                        retorno = true;
                    }
                }
                return retorno;
            }
        };

        try {
            final KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            SocketFactory sf = new LayeredSocketFactory() {

                SSLSocketFactory delegate = new MySSLSocketFactory(trustStore);
                @Override public Socket createSocket() throws IOException {
                    delegate.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    return delegate.createSocket();
                }
                @Override public Socket connectSocket(Socket sock, String host, int port,
                                                      InetAddress localAddress, int localPort, HttpParams params) throws IOException {
                    delegate.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    return delegate.connectSocket(sock, host, port, localAddress, localPort, params);
                }
                @Override public boolean isSecure(Socket sock) throws IllegalArgumentException {
                    delegate.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    return delegate.isSecure(sock);
                }
                @Override public Socket createSocket(Socket socket, String host, int port,
                                                     boolean autoClose) throws IOException {
                    delegate.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    injectHostname(socket, host);
                    return delegate.createSocket(socket, host, port, autoClose);
                }
                private void injectHostname(Socket socket, String host) {
                    try {
                        Field field = InetAddress.class.getDeclaredField("hostName");
                        field.setAccessible(true);
                        field.set(socket.getInetAddress(), host);
                    } catch (NoSuchFieldException ignored) {
                    } catch (SecurityException ignored)
                    {
                    } catch (IllegalAccessException ignored)
                    {
                    } catch (IllegalArgumentException ignored)
                    {
                    }
                }
            };

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            retorno = new DefaultHttpClient(ccm, params);
        } catch (KeyStoreException e) {
            retorno = null;
        } catch (UnrecoverableKeyException e)
        {
            retorno = null;
        } catch (KeyManagementException e)
        {
            retorno = null;
        } catch (NoSuchAlgorithmException e)
        {
            retorno = null;
        }
        //retorno.setHttpRequestRetryHandler(retryHandler);
        return retorno;
    }

    public static Document getXmlDocumentFromString(String xmlCode){
        Document retorno = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setExpandEntityReferences(true);
            factory.setCoalescing(true);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            retorno = builder.parse(new ByteArrayInputStream(xmlCode.getBytes()));
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AndroidSupport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex)
        {
            Logger.getLogger(AndroidSupport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(AndroidSupport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public static String Md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (int i=0; i<messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return "";
    }

    public static boolean InputStream2OutputStream (InputStream inStr, String fichero){
        FileOutputStream outFichero;

        try{
            outFichero = new FileOutputStream(fichero);
            int read;
            byte[] bytes = new byte[1024];

            while ((read = inStr.read(bytes)) != -1) {
                outFichero.write(bytes, 0, read);
            }

            inStr.close();
            outFichero.flush();
            outFichero.close();
        }catch (IOException ex){
            outFichero = null;
        }
        return outFichero != null;
    }

    public static String Stream2String(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        is.close();
        return sb.toString();
    }

    public static String Stream2StringSinCR(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    public static InputStreamReader ZipFileToFileReader(InputStream ficheroZip){
        InputStreamReader retorno = null;

        try{
            ZipInputStream zin = new ZipInputStream(ficheroZip);
            ZipEntry ze = null;

            if ((ze = zin.getNextEntry()) != null){
                //solo nos quedamos con el primer archivo
                retorno = new InputStreamReader(zin, "UTF-8");
            }

        }catch(IOException ex){
            retorno = null;
        }
        return retorno;
    }

    public static boolean UnzipStream (InputStream zipStream, String directorioDestino){
        boolean retorno;
        try{
            ZipInputStream zin = new ZipInputStream(zipStream);
            ZipEntry ze;

            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()){
                    File f = new File(directorioDestino + ze.getName());
                    if (!f.isDirectory()){
                        f.mkdirs();
                    }
                }else{
                    FileOutputStream fout = new FileOutputStream(directorioDestino + ze.getName());
                    byte []buffer = new byte[1024];
                    int c;
                    while ((c = zin.read(buffer))> 0){
                        fout.write(buffer, 0, c);
                    }
                    zin.closeEntry();
                    fout.close();
                }
            }
            zin.close();
            retorno = true;
        }catch(IOException ex){
            Logger.getLogger(AndroidSupport.class.getName()).log(Level.SEVERE, null, ex);
            retorno = false;
        }
        return retorno;
    }

    /**
     * Returns date object formatted in RFC3339 (yyyy-MM-dd'T'HH:mm:ss'Z' || yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z' || yyyy-MM-dd'T'HH:mm:ssZ || yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ || yyyy-MM-dd'T'HH:mm:ss.SSS+SSSS)
     * @param datestring
     * @return objeto date que representa la fecha proporcionada, fecha actual en caso de que la cadena no se corresponda a RFC3339
     */
    public static Date parseRFC3339Date(String datestring) {
        Date d = new Date();

        if (!datestring.equals("")){
            // if there is no time zone, we don't need to do any special parsing.
            if (datestring.endsWith("Z")) {
                try {
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // spec for RFC3339
                    d = s.parse(datestring);
                } catch (java.text.ParseException pe) {// try again with optional decimals
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");// spec for RFC3339 (with fractional seconds)
                    s.setLenient(true);
                    try{
                        d = s.parse(datestring);
                    }catch (ParseException ex){
                        Logger.getLogger(AndroidSupport.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return d;
            }

            // step one, split off the timezone.
            String firstpart = datestring.substring(0, datestring.lastIndexOf('-'));
            String secondpart = datestring.substring(datestring.lastIndexOf('-'));

            // step two, remove the colon from the timezone offset

            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");// spec for RFC3339

            try {
                d = s.parse(datestring);
            } catch (java.text.ParseException pe) {// try again with optional decimals
                s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");// spec for RFC3339 (with fractional seconds)
                s.setLenient(true);
                try{
                    d = s.parse(datestring);
                }catch(ParseException ex){
                    s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+SSSS");// spec for RFC3339 (with fractional seconds)
                    s.setLenient(true);
                    try{
                        d = s.parse(datestring);
                    }catch(ParseException ex2){
                        if (ex2 != null){
                            Logger.getLogger(AndroidSupport.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                    }
                }
            }
        }
        return d;
    }

    public static byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    public static long DateString2Long(String dateString){
        long retorno;
        try{

            Date fecha = new SimpleDateFormat().parse(dateString);
            // una vez tenemos el objeto fecha...
            retorno = fecha.getTime();
        }catch(Exception ex){
            Logger.getLogger(AndroidSupport.class.getName()).log(Level.SEVERE, null, ex);
            retorno = 0;
        }
        return retorno;
    }

    public static boolean isWiFiConnected(Context context){
        boolean retorno = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        NetworkInfo infoNetWork;
        if (cm != null && mTelephony != null){
            if ((infoNetWork = cm.getActiveNetworkInfo()) != null)
            {
                int netType = infoNetWork.getType();
                int netSubtype = infoNetWork.getSubtype();

                if (netType == ConnectivityManager.TYPE_WIFI)
                {
                    retorno = infoNetWork.isConnected();
                }
            }
        }

        return retorno;
    }

    public static boolean isGSMConnected(Context context){
        boolean retorno = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        NetworkInfo infoNetWork;
        if (cm != null && mTelephony != null){
            if ((infoNetWork = cm.getActiveNetworkInfo()) != null)
            {
                int netType = infoNetWork.getType();
                int netSubtype = infoNetWork.getSubtype();

                if (netType == ConnectivityManager.TYPE_MOBILE)
                {
                    retorno = infoNetWork.isConnected();
                }
            }
        }

        return retorno;
    }

    public static boolean isAppInExternalMemory(Context context){
        boolean retorno;
        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            ApplicationInfo ai = pi.applicationInfo;
            retorno = (ai.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
        }catch(PackageManager.NameNotFoundException ex){
            retorno = false;
        }
        return retorno;
    }


    public static int [] AddressGpsCoordinates(String address, String state, String country){
        int [] retorno = new int []{0,0};

        String urlJSON = String.format("http://maps.googleapis.com/maps/api/geocode/json?address=%s&sensor=true", URLEncoder.encode(address + "," + state + "," + country));
        DefaultHttpClient clienteHttp = AndroidSupport.getNewHttpClient();
        try{
            HttpGet getter = new HttpGet(urlJSON);
            HttpResponse respuesta = clienteHttp.execute(getter);
            HttpEntity entidad = respuesta.getEntity();
            if (entidad != null){
                String stringJSon = EntityUtils.toString(entidad, "UTF-8");
                JSONObject objetoJSon = new JSONObject(stringJSon);
                JSONObject resultados = objetoJSon.getJSONArray("results").getJSONObject(0);
                if (resultados != null){
                    retorno[0] = (int)(resultados.getJSONObject("geometry").getJSONObject("location").getDouble("lat")*1e6);
                    retorno[1] = (int)(resultados.getJSONObject("geometry").getJSONObject("location").getDouble("lng")*1e6);
                }
            }
        }catch (IOException ex){
            Log.e(AndroidSupport.class.getName(), ex.toString());
        } catch (org.apache.http.ParseException ex)
        {
            Log.e(AndroidSupport.class.getName(), ex.toString());
        } catch (JSONException ex)
        {
            Log.e(AndroidSupport.class.getName(), ex.toString());
        }

        return retorno;
    }


}
