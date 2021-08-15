package app.taplock.sapo;

import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

// https://gist.github.com/franzwarning/1e5fab9d7d2e90086e567768e71904be

public class AES {

    public static String decryptedResult;
    public static String encryptedResult;

    public static void main(String key, String initVector, String value) {

         //key = "sixteencharacter";
         //initVector = "jvHJ1XFt0IXBrxxx";
         //value = "secret message of any length";


        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            encryptedResult = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                encryptedResult = Base64.getUrlEncoder().encodeToString(encrypted);
            }
            else
            {
            }
            System.out.println("Encrypted string: " + encryptedResult);

            Cipher cipherd = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipherd.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                original = cipherd.doFinal(Base64.getUrlDecoder().decode(encryptedResult));
            }
            else
            {
            }
            decryptedResult = new String(original);
            System.out.println("Decrypted string: " + decryptedResult);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}