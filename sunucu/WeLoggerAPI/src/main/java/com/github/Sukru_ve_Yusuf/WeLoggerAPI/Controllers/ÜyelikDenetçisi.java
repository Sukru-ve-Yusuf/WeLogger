/*
 * ÜyelikDenetçisi
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Controllers;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models.*;
import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.*;
import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanı.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.io.*;
import java.util.*;
import org.bson.Document;
import com.fasterxml.jackson.databind.*;

/**
 * Üyelik işlemlerini yürütmek üzere API denetçisi.
 * 
 * @see Kullanıcı
 * @see KullanıcıVT
 * @see ParolaHizmeti
 */
@Path("/Üyelik")
public class ÜyelikDenetçisi
{
    /**
     * Uygulamada yer alan veri tabanı hizmetlerine erişim sağlar.
     * 
     * @see VeriTabanıHizmetleri
     */
    private final VeriTabanıHizmetleri VT;
    
    /**
     * Yanlış oluşturmayı önlemek için gizli başlatıcı.
     */
    private ÜyelikDenetçisi(VeriTabanıHizmetleri vt_hizmetleri)
    {
        this.VT = vt_hizmetleri;
    }
    
    /**
     * Belirtilen veri tabanı hizmetlerine bağlı yeni ÜyelikDenetçisi oluşturur.
     * 
     * @param vt_hizmetleri Uygulamada kullanılan veri tabanı hizmetleri
     * @return  Yeni ÜyelikDenetçisi
     */
    public static ÜyelikDenetçisi Başlat(VeriTabanıHizmetleri vt_hizmetleri)
    {
        if (vt_hizmetleri == null)
            return null;
        
        ÜyelikDenetçisi üye_d = new ÜyelikDenetçisi(vt_hizmetleri);
        return üye_d;
    }
    
    /**
     * Verilen bilgilerle yeni bir kullanıcı oluşturur.
     * Kullanıcı adı başka biri tarafından kullanılıyorsa işlem yapmaz.
     * 
     * @param Ad            Yeni kullanıcının adı.
     * @param KullanıcıAdı  Yeni kullanıcının rumuzu
     * @param Parola        Yeni kullanıcının parolası
     * @return  Yeni kullanıcı oluşturulursa 200, oluşturulmazsa 400,
     *          hata olursa 500
     */
    @PUT @Path("ÜyeOl/{Ad}/{KullaniciAdi}/{Parola}/")
    public Response ÜyeOl(
            @PathParam("Ad") String Ad,
            @PathParam("KullaniciAdi") String KullanıcıAdı,
            @PathParam("Parola") String Parola)
    {
        if (Ad == null)
            return Response.status(400).build(); // Bad Request
        if (KullanıcıAdı == null || KullanıcıAdı.isBlank())
            return Response.status(400).build(); // Bad Request
        if (Parola == null || Parola.isEmpty())
            return Response.status(400).build(); // Bad Request
        try
        {
            KullanıcıVT üye_vt = VT.getKullanıcıVT();
            long nicelik = üye_vt.KullanıcıAdıNiceliği(KullanıcıAdı);
            if (nicelik > 0)
            {
                return Response.status(400).build();
            }
            else if (nicelik < 0)
            {
                return Response.status(500).build();
            }
            
            String karılmış_parola = ParolaHizmeti.YeniParola(Parola);
            Kullanıcı yeni_üye = new Kullanıcı(Ad, KullanıcıAdı,
                    karılmış_parola, üye_vt);
            
            boolean kaydedildi = üye_vt.KullanıcıEkle(yeni_üye);
            if (kaydedildi)
            {
                return Response.status(201).build();
            }
            return Response.status(500).build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(400).build(); // Bad Request
        }
    }
    
    /**
     * Kimlik bilgileri verilen kullanıcı için yeni oturum başlatır.
     * 
     * @param KullanıcıAdı  Oturum açan kullanıcının kullanıcı adı
     * @param Parola        Oturum açan kullanıcının karılmamış parolası
     * @return  Yeni oturum başlatılırsa 200 ile oturum bilgilerini içeren JSON,
     *          başlatılmazsa 403, bilgiler girilmemişse 400, hata olursa 500
     */
    @POST @Path("OturumAç/{KullaniciAdi}/{Parola}/")
    @Produces("application/json")
    public Response OturumAç(
            @PathParam("KullaniciAdi") String KullanıcıAdı,
            @PathParam("Parola") String Parola)
    {
        if (KullanıcıAdı == null || KullanıcıAdı.isBlank())
            return Response.status(400).build(); // Bad Request
        if (Parola == null || Parola.isEmpty())
            return Response.status(400).build(); // Bad Request
        
        try
        {
            KullanıcıVT kullanıcı_vt = VT.getKullanıcıVT();
            OturumVT oturum_vt = VT.getOturumVT();
            Kullanıcı girecek = kullanıcı_vt.KullanıcıAdıylaBul(KullanıcıAdı);
            if (girecek == null)
                return Response.status(403).build(); // Forbidden
            
            boolean parola_doğru = ParolaHizmeti.ParolaDoğru(Parola,
                    girecek.getParola());
            
            if (parola_doğru)
            {
                Oturum yeni_oturum = Oturum.Başlat(
                        girecek.getKimlikBase64(),
                        12,
                        oturum_vt);
                if (yeni_oturum == null)
                    return Response.status(500).build();
                
                ObjectMapper haritacı = new ObjectMapper();
                String oturum_json = haritacı.writeValueAsString(yeni_oturum);
                boolean kaydedildi = oturum_vt.OturumKaydet(yeni_oturum);
                if (kaydedildi)
                {
                    Response yanıt = Response.status(200).entity(oturum_json)
                            .type("application/json").build();
                    return yanıt;
                }
            }
            return Response.status(403).build(); // Forbidden
        }
        catch (Exception e)
        {
            return Response.status(500).build();
        }
    }
    /**
     * İstek başlığında belirtilen oturumu geçersiz duruma getirir.
     * 
     * @param oturum    Oturum kimliği (Header'daki Oturum değişkeni)
     * @param kullanıcı Kullanıcı kimliği (Header'daki Kullanici değişkeni
     * @return  İşlemin başarılıysa 200, başarısızsa 500
     */
    @POST @Path("OturumKapat")
    public Response OturumKapat(
            @HeaderParam("Oturum") String oturum,
            @HeaderParam("Kullanici") String kullanıcı)
    {
        OturumVT oturum_vt = VT.getOturumVT();
        boolean kapandı = oturum_vt.OturumKapat(oturum, kullanıcı);

        if (kapandı)
            return Response.status(200).build();
        return Response.status(500).build();
    }
    /**
     * İstek başlığındaki bilgiler doğrultusunda oturumun geçerliliğini
     * denetler.
     * 
     * @param oturum    Oturum kimliği (Header'daki Oturum değişkeni)
     * @param kullanıcı Kullanıcı kimliği (Header'daki Kullanici değişkeni)
     * @return  Oturum açıksa 200, kapalıysa 404, hata olursa 500
     */
    @POST @Path("OturumAçık")
    public Response OturumAçık(
            @HeaderParam("Oturum") String oturum,
            @HeaderParam("Kullanici") String kullanıcı)
    {
        OturumVT oturum_vt = VT.getOturumVT();
        byte oturum_durumu = oturum_vt.OturumAçık(oturum, kullanıcı);
        if (oturum_durumu == 1)
        {
            return Response.status(200).build();
        }
        if (oturum_durumu < 0)
        {
            return Response.status(500).build();
        }
        
        return Response.status(404).build();
    }
}
