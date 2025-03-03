package hiff.hiff.behiff.global.util;

//import static hiff.hiff.behiff.global.auth.application.AuthService.appleKeyFile;

import hiff.hiff.behiff.global.response.properties.ErrorCode;
import hiff.hiff.behiff.global.util.exception.UtilsException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileReader {

    public static String readAppleKeyFile(String path) {
            try {
                InputStream inputStream = ResourceUtils.getURL(path).openStream();
                BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));
                String readLine = null;
                StringBuilder stringBuilder = new StringBuilder();
                while ((readLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(readLine);
                    stringBuilder.append("\n");
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                throw new UtilsException(ErrorCode.KEY_FILE_IOEXCEPTION);
            }
    }
}
