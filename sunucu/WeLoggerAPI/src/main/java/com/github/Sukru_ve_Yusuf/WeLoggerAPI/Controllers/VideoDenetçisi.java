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
    
    /**
     * Kişinin video çektiği günlerin tarihleriyle açıklamalarını döndürür.
     * 
     * @param oturum    Oturum kimliği
     * @param kullanıcı Kullanıcı kimliği
     * @return  200 ile bulunan günlerin bilgilerinin olduğu bir JSON,
     *          gün bulunamazsa 404
     */
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
        while (i < tarihler.length-1)
        {
            json.append("\"");
            json.append(tarihler[i][0]);
            json.append("\"");
            json.append(",");
            i++;
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
    /**
     * Tarihi verilen gündeki videoların bilgilerini döndürür.
     * 
     * @param oturum    Oturum kimliği
     * @param kullanıcı Kullanıcı kimliği
     * @param yıl       Aranan günün hangi yılda olduğu
     * @param ay        Aranan günün hangi ayda olduğu
     * @param gün       Aranan günün ayın kaçıncı günü olduğu
     * @return  Bulunursa 200 ile günün video bilgilerinin olduğu JSON,
     *          bulunamazsa 404.
     */
    @POST @Path("GününVideoları/{yil}/{ay}/{gun}/")
    public Response GününVideoları(
            @HeaderParam("Oturum") String oturum,
            @HeaderParam("Kullanici") String kullanıcı,
            @PathParam("yil") int yıl,
            @PathParam("ay") int ay,
            @PathParam("gun") int gün)
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
        
        SıralıGünler aranan_gün = iye.getÖmür().GünüBul(yıl, ay, gün);
        if (aranan_gün == null)
        {
            return Response.status(404).build();
        }
        
        if (aranan_gün.getVideolar() == null)
        {
            return Response.status(404).build();
        }
        String[][] vid_bil = aranan_gün.getVideolar()
                .TarihlerAçıklamalarVeKimlikler();
        
        StringBuilder json = new StringBuilder();
        json.append("{\"Tarihler\":[");
        int i = 0;
        while (i < vid_bil.length-1)
        {
            json.append("\"");
            json.append(vid_bil[i][0]);
            json.append("\"");
            json.append(",");
            i++;
        }
        json.append("\"");
        json.append(vid_bil[i][0]);
        json.append("\"");
        json.append("],");
        json.append("\"Açıklamalar\":[");
        i = 0;
        for (; i < vid_bil.length-1; i++)
        {
            json.append("\"");
            json.append(vid_bil[i][1]);
            json.append("\"");
            json.append(",");
        }
        json.append("\"");
        json.append(vid_bil[i][1]);
        json.append("\"");
        json.append("],");
        json.append("\"Kimlikler\":[");
        i = 0;
        for (; i < vid_bil.length-1; i++)
        {
            json.append("\"");
            json.append(vid_bil[i][2]);
            json.append("\"");
            json.append(",");
        }
        json.append("\"");
        json.append(vid_bil[i][2]);
        json.append("\"");
        json.append("]}");
        
        return Response.status(200).entity(json.toString())
                .type("application/json").build();
    }
}
