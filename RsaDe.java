
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import javax.crypto.Cipher;


public class RsaDe {

  
  public static final String ALGORITHM = "RSA";

  
  public static final String PRIVATE_KEY_FILE = "private.key";

  
  public static final String PUBLIC_KEY_FILE = "public.key";

  
  public static void generateKey() {
    try {
      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
      keyGen.initialize(1024);
      final KeyPair key = keyGen.generateKeyPair();

      File privateKeyFile = new File(PRIVATE_KEY_FILE);
      File publicKeyFile = new File(PUBLIC_KEY_FILE);

      // Create files to store public and private key
      if (privateKeyFile.getParentFile() != null) {
        privateKeyFile.getParentFile().mkdirs();
      }
      privateKeyFile.createNewFile();

      if (publicKeyFile.getParentFile() != null) {
        publicKeyFile.getParentFile().mkdirs();
      }
      publicKeyFile.createNewFile();

      // Saving the Public key in a file
      ObjectOutputStream publicKeyOS = new ObjectOutputStream(
          new FileOutputStream(publicKeyFile));
      publicKeyOS.writeObject(key.getPublic());
      publicKeyOS.close();

      // Saving the Private key in a file
      ObjectOutputStream privateKeyOS = new ObjectOutputStream(
          new FileOutputStream(privateKeyFile));
      privateKeyOS.writeObject(key.getPrivate());
      privateKeyOS.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  
  public static boolean areKeysPresent() {

    File privateKey = new File(PRIVATE_KEY_FILE);
    File publicKey = new File(PUBLIC_KEY_FILE);

    if (privateKey.exists() && publicKey.exists()) {
      return true;
    }
    return false;
  }

  


 
  public static String decrypt(byte[] text, PrivateKey key) {
    byte[] dectyptedText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance(ALGORITHM);

      // decrypt the text using the private key
      cipher.init(Cipher.DECRYPT_MODE, key);
      dectyptedText = cipher.doFinal(text);

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return new String(dectyptedText);
  }

  
  public static void main(String[] args) {

    try {

      // Check if the pair of keys are present else generate those.
      if (!areKeysPresent()) {
        // Method generates a pair of keys using the RSA algorithm and stores it
        // in their respective files
        generateKey();
      }

     byte[] cipherText = Files.readAllBytes(new File("encrypted.txt").toPath());

/*      String cipherText = "";
      FileReader fr=new FileReader("encrypted.txt");  
      int i1;  
      while((i1=fr.read())!= -1) {  
      cipherText = cipherText + (char)i1 ; 
      }
*/

      // final String originalText = "Text to be encrypted ";
      ObjectInputStream inputStream = null;

      

      // Decrypt the cipher text using the private key.
      inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
      final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
      final String plainText = decrypt(cipherText, privateKey);

      // Printing the Original, Encrypted and Decrypted Text
      // System.out.println("Encrypted: " +cipherText.toString());
      System.out.println("Decrypted: " + plainText);


     

      
      File file1 = new File("decrypted.txt");      
      if (!file1.exists()) {
      file1.createNewFile();
      }
      FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
      BufferedWriter bw1 = new BufferedWriter(fw1);
      bw1.write(plainText);
      bw1.close();





    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}