/*
 * WeLoggerAPI
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */

package com.github.Sukru_ve_Yusuf.WeLoggerAPI;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models.*;
import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.*;
import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanı.*;
import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Controllers.*;

import java.util.*;
import org.apache.cxf.jaxrs.*;

import java.io.*;
import java.nio.file.Files;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;

/**
 * Videolu günlük tutma uygulaması WeLogger'ın art ucunun ana sınıfı.
 */
public class WeLoggerAPI
{
    /**
     * WeLogger'ın başlangıç noktası.
     * 
     * @param args  Açılış değişkenleri (görmezden gelinecek)
     */
    public static void main(String[] args)
    {
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        
        VeriTabanıHizmetleri VT = VeriTabanıHizmetleri
                .HizmetleriBaşlat("./DB.config.json");
        if (VT == null)
        {
            System.out.println("Veri tabanı hizmetleri başlatılamadı.");
            return;
        }
        
        ÜyelikDenetçisi üye_denet = ÜyelikDenetçisi.Başlat(VT);
        if (üye_denet == null)
        {
            System.out.println("Üyelik Denetçisi başlatılamadı.");
            return;
        }
        
        VideoDenetçisi video_denet = VideoDenetçisi.Başlat(VT);
        if (video_denet == null)
        {
            System.out.println("Video Denetçisi başlatılamadı.");
            return;
        }
        
        //factoryBean.setResourceClasses(ÜyelikDenetçisi.class);
        factoryBean.setServiceBeanObjects(üye_denet, video_denet);
        factoryBean.setAddress("http://localhost:9000/");
        factoryBean.create();
    }
    
}
