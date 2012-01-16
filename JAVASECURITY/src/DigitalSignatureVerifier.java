import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


public class DigitalSignatureVerifier {
	public static void main(String...k){
		try {
			verifySignature();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static byte[] loadPublicKey()throws FileNotFoundException,IOException{
		FileInputStream fileInputStream=new FileInputStream("publicKey");		
		byte[] publicKey=new byte[fileInputStream.available()];
		fileInputStream.read(publicKey);
		fileInputStream.close();
		return publicKey;
	}
	
	
	private static PublicKey generateStoredPublicKey()throws FileNotFoundException, IOException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException{
		byte[] publicKeyBuffer=loadPublicKey();
		X509EncodedKeySpec encodedKeySpec=new X509EncodedKeySpec(publicKeyBuffer);
		PublicKey publicKey=KeyFactory.getInstance("DSA", "SUN").generatePublic(encodedKeySpec);
		
		return publicKey;		
	}
	
	private static void verifySignature() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException, IOException, SignatureException{
		Signature signature=Signature.getInstance("SHA1withDSA", "SUN");
		signature.initVerify(generateStoredPublicKey());
		
		FileInputStream datafis =new FileInputStream("test.txt");
		BufferedInputStream bufin =new BufferedInputStream(datafis);

		byte[] buffer = new byte[1024];
		int len;
		while (bufin.available() != 0) {
		    len = bufin.read(buffer);
		    signature.update(buffer, 0, len);
		};
		bufin.close();
		boolean verifies=signature.verify(loadSignature());
		//System.out.println("Load Signature: "+loadPublicKey()));
		//System.out.println("Actual Signature: "+ new String(signature.sign()));
		System.out.println("verifies== "+ verifies);
	}
	private static byte[] loadSignature() throws IOException {
		FileInputStream sigfis =new FileInputStream("sign");
		byte[] sigToVerify = new byte[sigfis.available()]; 
		sigfis.read(sigToVerify);
		sigfis.close();
		return sigToVerify;
	}
			
}
