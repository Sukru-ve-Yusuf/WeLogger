/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
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
 *
 * @author yusuf
 */
public class WeLoggerAPI {

    public static void main(String[] args)
    {
        VeriTabanıHizmetleri vt_hizmetleri = VeriTabanıHizmetleri
                .HizmetleriBaşlat("./DB.config.json");
        ObjectMapper haritacı = new ObjectMapper();
        Kullanıcı a = new Kullanıcı();
        a.setKullanıcıAdı("kulad");
        Calendar şimdi = Calendar.getInstance();
        System.out.println(şimdi);
        Calendar sonra = Calendar.getInstance();
        sonra.add(Calendar.HOUR, 5);
        Calendar önce = Calendar.getInstance();
        önce.add(Calendar.HOUR, -5);
        SıralıVideolar vid = new SıralıVideolar("./", "açıklama", a, şimdi);
        vid.setSonraki(new SıralıVideolar("./vid2", "aç", a, sonra));
        vid.setÖnceki(new SıralıVideolar("./vid3", "lama", a, önce));
        System.out.println(vid.getSonrakininKimliği());
        try
        {
            VideoVT vt_vid = vt_hizmetleri.getVideoVT();
            System.out.println(vt_vid.TekVideoEkle(vid));
            System.out.println(vt_vid.TekVideoEkle(vid.getSonraki()));
            System.out.println(vt_vid.TekVideoEkle(vid.getÖnceki()));
            System.out.println();
            SıralıVideolar çekim = vt_vid.VideolarıOku(vid.getKimlikBase64());
            System.out.println(çekim.getAçıklama() + " " + çekim.getTarih());
            System.out.println(çekim.getSonraki().getAçıklama() + " " + çekim.getSonraki().getTarih());
            System.out.println(çekim.getÖnceki().getAçıklama() + " " + çekim.getÖnceki().getTarih());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        /*
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setResourceClasses(DeneyselController.class);
        factoryBean.setAddress("http://localhost:9000/");
        factoryBean.create();
        */
    }
    
}
