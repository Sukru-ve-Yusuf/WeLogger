/*
 * VideoDenetçisi
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
 * Video işlemlerini yürütmek üzere API denetçisi.
 * 
 * @see Video
 * @see SıralıVideolar
 * @see VideoVT
 * @see Gün
 * @see SıralıGünler
 * @see GünVT
 */
@Path("/Video")
public class VideoDenetçisi
{
    /**
     * Uygulamada yer alan veri tabanı hizmetlerine erişim sağlar.
     * 
     * @see VeriTabanıHizmetleri
     */
    private final VeriTabanıHizmetleri VT;
    
    /**
     * Yanlış oluşturmayı önlemek için gizli başlatıcı.
     * 
     * @param vt_hizmetleri Uygulamada kullanılan veri tabanı hizmetleri
     */
    private VideoDenetçisi(VeriTabanıHizmetleri vt_hizmetleri)
    {
        this.VT = vt_hizmetleri;
    }
    
    /**
     * Belirtilen veri tabanı hizmetlerine bağlı yeni VideoDenetçisi oluşturur.
     * 
     * @param vt_hizmetleri Uygulamada kullanılan veri tabanı hizmetleri
     * @return  Yeni VideoDenetçisi
     */
    public static VideoDenetçisi Başlat(VeriTabanıHizmetleri vt_hizmetleri)
    {
        if (vt_hizmetleri == null)
            return null;
        
        VideoDenetçisi video_d = new VideoDenetçisi(vt_hizmetleri);
        return video_d;
    }
    
    @POST @Path("Günlerim")
    public Response Günlerim(
            @HeaderParam("Oturum") String oturum,
            @HeaderParam("Kullanici") String kullanıcı)
    {
        OturumVT oturum_vt = VT.getOturumVT();
        KullanıcıVT üye_vt = VT.getKullanıcıVT();
        byte oturum_durumu = oturum_vt.OturumAçık(oturum, kullanıcı);
        if (oturum_durumu != 1)
        {
            return Response.status(404).build();
        }
        
        Kullanıcı iye = üye_vt.KullanıcıOku(kullanıcı);
        
        if (iye == null)
        {
            return Response.status(404).build();
        }
        if (iye.getÖmür() == null)
        {
            return Response.status(404).build();
        }
        
        String[][] tarihler = iye.getÖmür().TarihlerİleAçıklamalar();
        
        StringBuilder json = new StringBuilder();
        json.append("{\"Tarihler\":[");
        int i = 0;
        for (; i < tarihler.length-1; i++)
        {
            json.append("\"");
            json.append(tarihler[i][0]);
            json.append("\"");
            json.append(",");
        }
        json.append("\"");
        json.append(tarihler[i][0]);
        json.append("\"");
        json.append("],");
        json.append("\"Açıklamalar\":[");
        i = 0;
        for (; i < tarihler.length-1; i++)
        {
            json.append("\"");
            json.append(tarihler[i][1]);
            json.append("\"");
            json.append(",");
        }
        json.append("\"");
        json.append(tarihler[i][1]);
        json.append("\"");
        json.append("]}");
        
        return Response.status(200).entity(json.toString())
                .type("application/json").build();
    }
}
