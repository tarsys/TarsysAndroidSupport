package com.github.tarsys.android.support.utilities;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by tarsys on 21/9/15.
 */
public class SerializationSupport {

    public static String Object2Base64 (Object object){
        String retorno;
        try{
            java.io.ByteArrayOutputStream fileOut = new java.io.ByteArrayOutputStream();
            ObjectOutputStream objectOut;
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            fileOut.flush();
            // Una vez tenemos el bytearray buffer, lo convertimos a base64...
            retorno = Base64.encodeToString(fileOut.toByteArray(), Base64.DEFAULT);
        }catch(Exception ex){
            Log.e(SerializationSupport.class.getName(), ex.toString());
            retorno = "";
        }
        return retorno;
    }

    public static Object Base64ToObject(String strBase64){
        Object retorno;
        try{
            java.io.ByteArrayInputStream fileIn = new java.io.ByteArrayInputStream(Base64.decode(strBase64, Base64.DEFAULT));
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            retorno = objectIn.readObject();
        }catch(Exception ex){
            Log.e(SerializationSupport.class.getName(), ex.toString());
            retorno = null;
        }
        return retorno;
    }

    public static boolean SerializeToFile(Context context, Object object, String filename) {

        boolean returnValue = false;
        ObjectOutputStream objectOut = null;
        if (context != null)
        {
            try
            {

                FileOutputStream fileOut = context.openFileOutput(filename, Activity.MODE_PRIVATE);
                objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(object);
                fileOut.getFD().sync();

            }
            catch (IOException e)
            {
                Log.e(SerializationSupport.class.getName(), e.toString());
            }
            finally
            {
                if (objectOut != null)
                {
                    try
                    {
                        objectOut.close();
                        returnValue = true;
                    }
                    catch (IOException e)
                    {
                        // do nowt
                        Log.e(SerializationSupport.class.getName(), e.toString());
                    }
                }
            }
        }
        return returnValue;
    }

    public static Object DeserializeFromFile(String filename) {

        ObjectInputStream objectIn = null;
        Object object = null;
        try {

            FileInputStream fileIn = new FileInputStream(filename);
            objectIn = new ObjectInputStream(fileIn);
            object = objectIn.readObject();

        } catch (FileNotFoundException e) {
            Log.e(SerializationSupport.class.getName(), e.toString());
            // Do nothing
        } catch (IOException e) {
            Log.e(SerializationSupport.class.getName(), e.toString());
        } catch (ClassNotFoundException e) {
            Log.e(SerializationSupport.class.getName(), e.toString());
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    // do nowt
                    Log.e(SerializationSupport.class.getName(), e.toString());
                }
            }
        }

        return object;
    }
}
