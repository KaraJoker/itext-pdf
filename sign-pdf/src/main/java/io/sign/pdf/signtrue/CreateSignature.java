package io.sign.pdf.signtrue;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;


public class CreateSignature {

    final static File RESULT_FOLDER = new File("target/test-outputs", "signature");

    public static final String KEYSTORE = "keystores/demo-rsa2048.ks";
    public static final char[] PASSWORD = "demo-rsa2048".toCharArray();

    public static KeyStore ks = null;
    public static PrivateKey pk = null;
    public static Certificate[] chain = null;


    public static void setUp() throws Exception {
        RESULT_FOLDER.mkdirs();

        BouncyCastleProvider bcp = new BouncyCastleProvider();
        //Security.addProvider(bcp);
        Security.insertProviderAt(bcp, 1);

        ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(KEYSTORE), PASSWORD);
        String alias = (String) ks.aliases().nextElement();
        pk = (PrivateKey) ks.getKey(alias, PASSWORD);
        chain = ks.getCertificateChain(alias);
    }


    public void sign50MNaive() throws Exception {
        setUp();
        String filepath = "/Users/huluwa110/work/hxywork/itext-pdf/sign-pdf/src/test/itext5/50m.pdf";
        String digestAlgorithm = "SHA512";
        MakeSignature.CryptoStandard subfilter = MakeSignature.CryptoStandard.CMS;

        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(filepath);
        FileOutputStream os = new FileOutputStream(new File(RESULT_FOLDER, "52m-signedNaive.pdf"));
        PdfStamper stamper =
                PdfStamper.createSignature(reader, os, '\0', RESULT_FOLDER, true);
        // Creating the appearance
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason("reason");
        appearance.setLocation("location");
        appearance.setVisibleSignature(new Rectangle(56, 648, 124, 780), 1, "sig1");
        // Creating the signature
        ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, "BC");
        ExternalDigest digest = new BouncyCastleDigest();
        MakeSignature.signDetached(appearance, digest, pks, chain,
                null, null, null, 0, subfilter);
    }
}
