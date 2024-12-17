package istemci.WeLoggerClient.src.main.java.com.github.Sukru_ve_Yusuf.WeLoggerClient;

import java.security.SecureRandom;
import java.util.Base64;

public class UserClient {
    private String ad; // Eşsiz ad
    private String kullanıcı_adı; // Kullanıcı adı (rumuz)
    private String pass; // Kullanıcı inputu ile alınan ham (hash'lenmemiş) parola
    private byte[] kimlik; // Kullanıcıya ait eşsiz kimlik verisi

    // Yapıcı metod
    public UserClient(String ad, String kullanıcı_adı, String pass) {
        this.ad = ad;
        this.kullanıcı_adı = kullanıcı_adı;
        this.pass = pass;
        this.kimlik = generateKimlik(); // Kimlik üretimi
    }

    // Getter ve Setter metodları
    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getKullanıcıAdı() {
        return kullanıcı_adı;
    }

    public void setKullanıcıAdı(String kullanıcı_adı) {
        this.kullanıcı_adı = kullanıcı_adı;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public byte[] getKimlik() {
        return kimlik;
    }

    // Kimlik oluşturma (random byte array)
    private byte[] generateKimlik() {
        SecureRandom random = new SecureRandom();
        byte[] kimlik = new byte[32]; // 32 byte'lık bir kimlik oluşturuluyor
        random.nextBytes(kimlik);
        return kimlik;
    }

    // Kimliği Base64 formatında döndürme (opsiyonel)
    public String getKimlikBase64() {
        return Base64.getEncoder().encodeToString(this.kimlik);
    }
}