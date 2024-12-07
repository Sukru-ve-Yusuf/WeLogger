/*
 * SıralıVideolar
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces.*;
import java.util.*;

/**
 * Videoları geçmişten geleceğe doğru sıralanmış
 * çift yönlü bağlı liste olarak tutan sınıf.
 * 
 * @see Video
 * @see IBağlıListe
 */
public class SıralıVideolar extends Video
implements IBağlıListe<SıralıVideolar>
{
    /**
     * Tarihsel sıralamada bir geçmişteki video.
     */
    private SıralıVideolar önceki;
    /**
     * Tarihsel sıralamada bir gelecekteki video.
     */
    private SıralıVideolar sonraki;
    
    /**
     * Verilen bilgilerle hiçbir yere bağlı olmayan yepyeni bir video oluşturur.
     * Oluşturulan nesneye yeni bir kimlik atanır.
     * 
     * @param dosya_yolu    Temsil edilen video dosyasının adresi
     * @param açıklama      Kullanıcının video hakkındaki açıklama metni
     * @param iye           Videonun sahibi olan kullanıcı
     * @param tarih         Videonun çekildiği tarih ve saat bilgisi
     */
    public SıralıVideolar(String dosya_yolu, String açıklama,
            Kullanıcı iye, Date tarih)
    {
        super(dosya_yolu, açıklama, iye, tarih);
        this.önceki = null;
        this.sonraki = null;
    }
    /**
     * Bir video nesnesinin içeriğinden yeni bir sıralı video nesnesi oluşturur.
     * İsteğe bağlı olarak kimlik korunabilir ya da yenilenebilir.
     * 
     * @param kaynak_video      İçeriği alınacak video nesnesi
     * @param kimlik_yenilensin true ise yeni bir kimlik atanır,
     *                          false ise kaynak nesnenin kimliği kopyalanır.
     */
    public SıralıVideolar(Video kaynak_video, boolean kimlik_yenilensin)
    {
        super(kaynak_video, kimlik_yenilensin);
        this.önceki = null;
        this.sonraki = null;
    }
    
    /**
     * Sıralı video listesinin tarihsel sıraya uygun bir noktasına
     * yeni video ekler.
     * 
     * @param yeni_düğüm    Listeye eklenecek yeni sıralı video nesnesi
     * @return  Eklenen yeni düğüm. Sıralama konusunda sorun yaşanırsa null.
     */
    @Override
    public SıralıVideolar DüğümEkle(SıralıVideolar yeni_düğüm)
    {
        if (yeni_düğüm.getTarih() == null)
        {
            return null;
        }
        SıralıVideolar kök = this.Kök();
        SıralıVideolar kafa = kök;
        yeni_düğüm.Kopar();
        
        if (yeni_düğüm.getTarih().before(kafa.getTarih()))
        {
            yeni_düğüm.setSonraki(kafa);
            kafa.setÖnceki(yeni_düğüm);
            return yeni_düğüm;
        }
        
        boolean yerleşti = false;
        while (kafa.getSonraki() != null)
        {
            if (kafa.getSonraki().getTarih().after(yeni_düğüm.getTarih()))
            {
                yeni_düğüm.setSonraki(kafa.getSonraki());
                kafa.getSonraki().setÖnceki(yeni_düğüm);
                yeni_düğüm.setÖnceki(kafa);
                kafa.setSonraki(yeni_düğüm);
                yerleşti = true;
                break;
            }
            kafa = kafa.getSonraki();
        }
        if (!yerleşti && !yeni_düğüm.getTarih().before(kafa.getTarih()))
        {
            yeni_düğüm.setÖnceki(kafa);
            kafa.setSonraki(yeni_düğüm);
            yerleşti = true;
        }
        
        if (yerleşti)
        {
            return yeni_düğüm;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * En geçmişteki sıralı video nesnesini döndürür.
     * 
     * @return  Listenin en geçmişteki videosu
     */
    public SıralıVideolar Kök()
    {
        SıralıVideolar kafa = this;
        while (kafa.getÖnceki() != null)
        {
            kafa = kafa.getÖnceki();
        }
        return kafa;
    }
    
    /**
     * En gelecekteki sıralı video nesnesini döndürür.
     * 
     * @return  Listenin en gelecekteki videosu
     */
    @Override
    public SıralıVideolar UçDüğüm()
    {
        SıralıVideolar kafa = this;
        while (kafa.getSonraki() != null)
        {
            kafa = kafa.getSonraki();
        }
        return kafa;
    }
    
    /**
     * Videoyu listeden koparır ve listenin kalanlarını birbirine bağlar.
     * 
     * @return  Listeden kopmuş video
     */
    public SıralıVideolar Kopar()
    {
        SıralıVideolar önce = this.getÖnceki();
        SıralıVideolar sonra = this.getSonraki();
        if (önce != null)
            önce.setSonraki(sonra);
        if (sonra != null)
            sonra.setÖnceki(önce);
        this.setÖnceki(null);
        this.setSonraki(null);
        return this;
    }
    
    /**
     * Bir geçmişteki videoya erişim sağlar.
     * 
     * @return  Bir geçmişteki videonun sıralı video nesnesi
     */
    public SıralıVideolar getÖnceki()
    {
        return this.önceki;
    }
    /**
     * Belirtilen videoyu bir geçmişteki video olarak tanımlar.
     * Belirtilen videonun tarihine erişilemiyorsa işlem yapılmaz.
     * Belirtilen videonun tarihi daha önce değilse işlem yapılmaz.
     * 
     * @param önceki    Bir geçmişe yerleştirilecek video
     * @return  İşlem başarılıysa true, başarısızsa false
     */
    public boolean setÖnceki(SıralıVideolar önceki)
    {
        if (önceki == null)
        {
            this.önceki = null;
            return true;
        }
        // İki nesneden birinin tarihine erişilemiyorsa
        if (this.getTarih() == null || önceki.getTarih() == null)
        {
            return false;
        }
        // Önceki gerçekten de önce geliyorsa
        if (önceki.getTarih().before(this.getTarih()))
        {
            this.önceki = önceki;
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * Bir gelecekteki videoya erişim sağlar.
     * 
     * @return  Bir gelecekteki videonun sıralı video nesnesi
     */
    public SıralıVideolar getSonraki()
    {
        return this.sonraki;
    }
    /**
     * Belirtilen videoyu bir gelecekteki video olarak tanımlar.
     * Belirtilen videonun tarihine erişilemiyorsa işlem yapılmaz.
     * Belirtilen videonun tarihi daha sonra değilse işlem yapılmaz.
     * 
     * @param sonraki    Bir geleceğe yerleştirilecek video
     * @return  İşlem başarılıysa true, başarısızsa false
     */
    public boolean setSonraki(SıralıVideolar sonraki)
    {
        if (sonraki == null)
        {
            this.sonraki = null;
            return true;
        }
        // İki nesneden birinin tarihine erişilemiyorsa
        if (this.getTarih() == null || sonraki.getTarih() == null)
        {
            return false;
        }
        // Sonraki gerçekten de sonra geliyorsa
        if (sonraki.getTarih().after(this.getTarih()))
        {
            this.sonraki = sonraki;
            return true;
        }
        else
        {
            return false;
        }
    }
}
