package hiff.hiff.behiff.global.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import hiff.hiff.behiff.global.util.exception.UtilsException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Parser {

    public static PrivateKey getPrivateKeyFromPem(String content) {
        try {
            Reader reader = new StringReader(content);
            PEMParser pemParser = new PEMParser(reader);
            JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
            PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
            return jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);
        } catch (IOException e) {
            throw new UtilsException(ErrorCode.PEM_PARSER_ERROR);
        }
    }

    public static String getAppleIdByIdToken(List<Map<String, Object>> keys, String idToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            for (Map<String, Object> key : keys) {
                RSAKey rsaKey = (RSAKey) JWK.parse(new ObjectMapper().writeValueAsString(key));
                RSAPublicKey rsaPublicKey = rsaKey.toRSAPublicKey();
                JWSVerifier jwsVerifier = new RSASSAVerifier(rsaPublicKey);

                if (signedJWT.verify(jwsVerifier)) {
                    String payload = idToken.split("[.]")[1];
                    Map payloadMap = new ObjectMapper().readValue(new String(
                        Base64.getDecoder().decode(payload)), Map.class);
                    return payloadMap.get("sub").toString();
                }
            }
        } catch (Exception e) {
            throw new UtilsException(ErrorCode.ID_TOKEN_PARSER_ERROR);
        }

        return null;
    }

    public static String getSocialIdByIdToken(String idToken) {
        DecodedJWT jwt = JWT.decode(idToken);
        return jwt.getSubject();
    }
}
