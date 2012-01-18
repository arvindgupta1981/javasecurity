import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;


public class DigitalSignatureGeneration {	
	private static KeyPair keyPair=null;
	static{
		try {
			keyPair=keyPairGenerator();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException {
		
		storeSignature();
		storePublicKey();
	}

	private static PublicKey getPublicKey() {
		PublicKey publicKey=keyPair.getPublic();
		return publicKey;
	}

	private static PrivateKey getPrivateKey() {
		PrivateKey privateKey=keyPair.getPrivate();
		return privateKey;
	}

	private static KeyPair keyPairGenerator() throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyPairGenerator generator=KeyPairGenerator.getInstance("DSA","SUN");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

		generator.initialize(1024, random);
		KeyPair keyPair=generator.genKeyPair();
		return keyPair;
	}
	private static byte[] getSingnature() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException{
		Signature signature=Signature.getInstance("SHA1withDSA","SUN");
		signature.initSign(getPrivateKey());		
		FileInputStream fis = new FileInputStream("test.txt");
		BufferedInputStream bufin = new BufferedInputStream(fis);
		byte[] buffer = new byte[1024];
		int len;
		while ((len = bufin.read(buffer)) >= 0) {
			signature.update(buffer, 0, len);
		};
		bufin.close();		
		return signature.sign();		
	}
	private static void storeSignature() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException{
		FileOutputStream fileOutputStream=new FileOutputStream("sign");
		byte[] signature=getSingnature();
		fileOutputStream.write(signature);
		fileOutputStream.close();		
	}
	private static void storePublicKey() throws IOException, NoSuchAlgorithmException, NoSuchProviderException{
		FileOutputStream fileOutputStream=new FileOutputStream("publicKey");		
		PublicKey publicKey=getPublicKey();
		fileOutputStream.write(publicKey.getEncoded());
		fileOutputStream.close();
		
	}
}
