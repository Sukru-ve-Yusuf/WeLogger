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
}
