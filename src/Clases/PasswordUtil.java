package Clases;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verificar(String password, String hashAlmacenado) {
        if (hashAlmacenado == null || hashAlmacenado.isEmpty()) return false;
        if (hashAlmacenado.length() == 64 && hashAlmacenado.matches("[0-9a-f]+")) {
            return hash(password).equals(hashAlmacenado);
        }
        return password.equals(hashAlmacenado);
    }
}
