/*
 * Video
 *
 * Sürüm 0.1
 *
 * Aralık 2024
 *
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces.*;
import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanı.*;
import java.util.*;
import java.security.SecureRandom;
import com.fasterxml.jackson.annotation.*;

/**
 * Kullanıcıların çektiği videoların her birini temsil eder.
 * VideoListesi sınıfı kullanılarak bağlı liste yapılabilir.
 * 
 * @see VideoListesi
 * @see IKimlikli
 */
@JsonPropertyOrder({"_id", "DosyaYolu", "Açıklama", "İye", "Tarih"})
public class Video implements IKimlikli
{
    /**
     * Videonun dosya sistemindeki adresi.
     */
    protected String dosya_yolu;
    /**
     * Kullanıcının video hakkındaki açıklama yazısı.
     */
    protected String açıklama;
    /**
     * Base64 metni olarak videonun sahibi olan kullanıcının kimliği.
     * @see Kullanıcı#kimlik
     */
    protected String iye;
    /**
     * Video nesnesini ayırt etmek için eşsiz bir sayı.
     */
    protected byte[] kimlik;
    /**
     * Videonun çekildiği tarih ve saat.
     * @see java.util.Calendar
     */
    protected Calendar tarih;
    
    /**
     * Belirtilen kimlikle boş bir video nesnesi oluşturur.
     * 
     * @param kimlik_base64 Base64 metni olarak video kimliği
     */
    protected Video(String kimlik_base64)
    {
        Base64.Decoder b64decoder = Base64.getDecoder();
        this.kimlik = b64decoder.decode(kimlik_base64);
    }
    /**
     * Verilen bilgilerle yeni bir video nesnesi oluşturur.
     * Oluşturulan nesneye yeni bir kimlik atanır.
     * 
     * @param dosya_yolu    Temsil edilen video dosyasının adresi
     * @param açıklama      Kullanıcının video hakkındaki açıklama metni
     * @param iye           Videonun sahibi olan kullanıcı
     * @param tarih         Videonun çekildiği tarih ve saat bilgisi
     */
    public Video(String dosya_yolu, String açıklama, Kullanıcı iye,
            Calendar tarih)
    {
        this.dosya_yolu = dosya_yolu;
        this.açıklama = açıklama;
        this.iye = iye.getKimlikBase64();
        this.tarih = tarih;
        this.KimliğiYenile();
    }
    /**
     * Verilen bilgilerle yeni bir video nesnesi oluşturur.
     * Oluşturulan nesneye yeni bir eşsiz kimlik atanır.
     * 
     * @param dosya_yolu        Videonun dosya sistemindeki konumu
     * @param açıklama          Kullanıcının video hakkında açıklama metni
     * @param kullanıcı_kimliği Videonun sahibinin kullanıcı kimliği
     * @param tarih             Videonun çekildiği tarih ve saat bilgisi
     * @param video_vt          Kimlik eşsizliği video veri tabanı hizmeti
     */
    public Video(String dosya_yolu, String açıklama, String kullanıcı_kimliği,
            Calendar tarih, VideoVT video_vt)
    {
        this.setDosyaYolu(dosya_yolu);
        this.setAçıklama(açıklama);
        this.setİye(kullanıcı_kimliği);
        this.setTarih(tarih);
        this.KimliğiYenile(video_vt);
    }
    /**
     * Verilen bilgilerle yeni bir video nesnesi oluşturur.
     * 
     * @param dosya_yolu    Temsil edilen video dosyasının adresi
     * @param açıklama      Kullanıcının video hakkındaki açıklama metni
     * @param iye           Videonun sahibi olan kullanıcı
     * @param tarih         Videonun çekildiği tarih ve saat bilgisi
     * @param kimlik        Base64 metni olarak videonun kimliği
     */
    public Video(String dosya_yolu, String açıklama, Kullanıcı iye,
            Calendar tarih, String kimlik)
    {
        this.setDosyaYolu(dosya_yolu);
        this.setAçıklama(açıklama);
        this.setİye(iye);
        this.setTarih(tarih);
        Base64.Decoder b64decoder = Base64.getDecoder();
        this.kimlik = b64decoder.decode(kimlik);
    }
    /**
     * Verilen bilgilerle yeni bir video nesnesi oluşturur.
     * 
     * @param dosya_yolu    Temsil edilen video dosyasının adresi
     * @param açıklama      Kullanıcının video hakkındaki açıklama metni
     * @param iye_base64    Base64 metni olarak videonun sahibinin kimliği
     * @param tarih         Videonun çekildiği tarih ve saat bilgisi
     * @param kimlik_base64 Base64 metni olarak videonun kimliği
     */
    @JsonCreator
    public Video(
            @JsonProperty("DosyaYolu") String dosya_yolu,
            @JsonProperty("Açıklama") String açıklama,
            @JsonProperty("İye") String iye_base64,
            @JsonProperty("Tarih") Calendar tarih,
            @JsonProperty("_id") String kimlik_base64)
    {
        this.setDosyaYolu(dosya_yolu);
        this.setAçıklama(açıklama);
        this.setİye(iye_base64);
        this.setTarih(tarih);
        Base64.Decoder b64decoder = Base64.getDecoder();
        this.kimlik = b64decoder.decode(kimlik_base64);
    }
    /**
     * Başka bir video nesnesinin içeriğinden yeni bir video nesnesi oluşturur.
     * İsteğe bağlı olarak kimlik korunabilir ya da yenilenebilir.
     * 
     * @param başka_video       İçeriği alınacak video nesnesi
     * @param kimlik_yenilensin true ise yeni bir kimlik atanır,
     *                          false ise kaynak nesnenin kimliği kopyalanır.
     */
    public Video(Video başka_video, boolean kimlik_yenilensin)
    {
        this.setDosyaYolu(başka_video.getDosyaYolu());
        this.setAçıklama(başka_video.getAçıklama());
        this.setİye(başka_video.getİye());
        this.setTarih(başka_video.getTarih());
        if (kimlik_yenilensin)
        {
            this.KimliğiYenile();
        }
        else
        {
            this.kimlik = başka_video.getKimlikBytes().clone();
        }
    }
    
    /**
     * Video dosyasının bulunduğu adresi bildirir.
     * 
     * @return  Videonun dosya sistemindeki adresi
     */
    @JsonGetter("DosyaYolu")
    public String getDosyaYolu()
    {
        return this.dosya_yolu;
    }
    /**
     * Video için farklı bir adres belirtilir.
     * Eski adresteki dosya taşınmaz. Video yeni adreste aranır.
     * 
     * @param yeni_yol  Videonun dosya sistemindeki yeni adresi
     */
    public void setDosyaYolu(String yeni_yol)
    {
        this.dosya_yolu = yeni_yol;
    }
    
    /**
     * Videonun açıklama metnine erişim sağlar.
     * 
     * @return  Kullanıcının video hakkındaki açıklama metni
     */
    @JsonGetter("Açıklama")
    public String getAçıklama()
    {
        return this.açıklama;
    }
    /**
     * Videonun açıklama metnini yeniler.
     * 
     * @param yeni_açıklama Mevcut açıklama metninin yerini alacak metin
     */
    public void setAçıklama(String yeni_açıklama)
    {
        this.açıklama = yeni_açıklama;
    }
    
    /**
     * Videonun sahibininin kimliğine erişim sağlar.
     * 
     * @return  Videonun sahibinin kimliği
     * @see Kullanıcı#kimlik
     */
    @JsonGetter("İye")
    public String getİye()
    {
        return this.iye;
    }
    /**
     * Videonun sahibini belirler.
     * 
     * @param yeni_iye  Videonun yeni sahibi
     * @see Kullanıcı
     * @see Kullanıcı#getKimlikBase64() 
     */
    public void setİye(Kullanıcı yeni_iye)
    {
        this.iye = yeni_iye.getKimlikBase64();
    }
    /**
     * Videonun sahibini belirler.
     * 
     * @param iye   Videonun yeni sahibi
     * @see Kullanıcı#kimlik
     */
    public void setİye(String iye)
    {
        this.iye = iye;
    }
    
    @Override @JsonIgnore
    public byte[] getKimlikBytes()
    {
        return this.kimlik;
    }
    @Override @JsonGetter("_id")
    public String getKimlikBase64()
    {
        Base64.Encoder b64encoder = Base64.getEncoder();
        return b64encoder.encodeToString(this.kimlik);
    }
    /**
     * Video için yepyeni bir kimlik atar.
     */
    protected void KimliğiYenile()
    {
        SecureRandom rast = new SecureRandom();
        kimlik = new byte[33];
        rast.nextBytes(this.kimlik);
    }
    /**
     * Video için yepyeni bir eşsiz kimlik atar.
     * 
     * @param video_vt  Eşsizlik denetimi için video veri tabanı hizmeti
     */
    protected void KimliğiYenile(VideoVT video_vt)
    {
        SecureRandom rast = new SecureRandom();
        byte[] yeni = new byte[33];
        rast.nextBytes(yeni);
        
        Base64.Encoder b64encoder = Base64.getEncoder();
        String yeni_b64 = b64encoder.encodeToString(yeni);
        
        byte kullanımda = video_vt.KimlikKullanımda(yeni_b64);
        if (kullanımda == 0)
        {
            this.kimlik = yeni;
        }
        else
        {
            this.KimliğiYenile(video_vt);
        }
    }
    
    /**
     * Videonun çekildiği tarihi bildirir.
     * 
     * @return  Videonun çekildiği tarihi bildiren Calendar nesnesinin klonu
     */
    @JsonGetter("Tarih")
    public Calendar getTarih()
    {
        if (this.tarih == null)
        {
            return null;
        }
        return (Calendar)this.tarih.clone();
    }
    /**
     * Videonun çekildiği tarihi belirtilen yeni tarih olarak değiştirir.
     * Verilen yeni tarih null ise işlem yapmaz.
     * 
     * @param yeni_tarih    Videonun yeni tarihi
     */
    public void setTarih(Calendar yeni_tarih)
    {
        if (yeni_tarih != null)
        {
            this.tarih = (Calendar)yeni_tarih.clone();
        }
    }
}
