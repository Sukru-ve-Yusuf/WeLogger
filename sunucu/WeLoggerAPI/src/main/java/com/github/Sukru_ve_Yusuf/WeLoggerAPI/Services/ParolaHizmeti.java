/*
 * ParolaHizmeti
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models.*;
import java.util.*;
import java.security.SecureRandom;
import static com.kosprov.jargon2.api.Jargon2.*;

/**
 * Parolaların güvenli bir yolla işlenmesi için hizmet sınıfı
 */
public class ParolaHizmeti
{
    /**
     * Kullanıcıdan alınmış parolayı Argon2id ile karar.
     * 
     * @param parola Karılmamış parola
     * @return Argon2id karma sonucu
     */
    public static String YeniParola(String parola)
    {
        try
        {
            byte[] ikili_parola = parola.getBytes("UTF-8");
            byte[] yeni_tuz = new byte[24];
            SecureRandom rast = new SecureRandom();
            rast.nextBytes(yeni_tuz);
            Hasher karıcı = jargon2Hasher()
                    .type(Type.ARGON2id)
                    .memoryCost(64*1024)
                    .timeCost(8)
                    .parallelism(2)
                    .hashLength(48);
            
            String karma = karıcı.salt(yeni_tuz).password(ikili_parola).encodedHash();
            for (int i = 0; i < ikili_parola.length; i++)
            {
                ikili_parola[i] = 0;
            }
            return karma;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Kullanıcıdan alınan parolayı, kayıtlı olan karmayla karşılaştırır.
     * Kayıtlı olan karmanın <code>YeniParola(String)</code> ile üretilmiş
     * olması gerekir.
     * 
     * @param girilen Kullanıcıdan alınan, karılmamış parola
     * @param karma Veri tabanınandan alınan, karılmış parola
     * @return  Girilen parola doğruysa <code>true</code>,
     *          yanlışsa veya hata olursa <code>false</code>
     * 
     * @see #YeniParola(java.lang.String) 
     */
    public static boolean ParolaDoğru(String girilen, String karma)
    {
        try
        {
            byte[] ikili_parola = girilen.getBytes("UTF-8");
            Verifier denetçi = jargon2Verifier();
            boolean sonuç = denetçi.hash(karma).password(ikili_parola).verifyEncoded();
            for (int i = 0; i < ikili_parola.length; i++)
            {
                ikili_parola[i] = 0;
            }
            return sonuç;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
