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
        sonra.add(Calendar.HOUR, 1);
        Calendar önce = Calendar.getInstance();
        önce.add(Calendar.HOUR, -1);
        SıralıVideolar vid = new SıralıVideolar("./", "açıklama", a, şimdi);
        vid.setSonraki(new SıralıVideolar("./vid2", "aç", a, sonra));
        vid.setÖnceki(new SıralıVideolar("./vid3", "lama", a, önce));
        Calendar bugün = new Calendar.Builder()
                .setDate(şimdi.get(Calendar.YEAR),
                        şimdi.get(Calendar.MONTH),
                        şimdi.get(Calendar.DAY_OF_MONTH)).build();
        System.out.println(bugün);
        SıralıGünler günler = new SıralıGünler(a, "gün açıklaması", vid, bugün);
        a.setÖmür(günler);
        try
        {
            VideoVT vt_vid = vt_hizmetleri.getVideoVT();
            System.out.println(vt_vid.TekVideoEkle(vid));
            System.out.println(vt_vid.TekVideoEkle(vid.getSonraki()));
            System.out.println(vt_vid.TekVideoEkle(vid.getÖnceki()));
            System.out.println("---");
            GünVT vt_gün = vt_hizmetleri.getGünVT();
            System.out.println(vt_gün.TekGünEkle(günler));
            System.out.println("---");
            KullanıcıVT vt_üye = vt_hizmetleri.getKullanıcıVT();
            System.out.println(vt_üye.KullanıcıEkle(a));
            System.out.println("#####");
            Kullanıcı b = vt_üye.KullanıcıOku(a.getKimlikBase64());
            System.out.println(a.getKimlikBase64());
            System.out.println(b.getKimlikBase64());
            System.out.println(günler.getKimlikBase64());
            System.out.println(b.getÖmrünKimliği());
            System.out.println(vid.getKimlikBase64());
            System.out.println(b.getÖmür().getVideolarınKimliği());
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
