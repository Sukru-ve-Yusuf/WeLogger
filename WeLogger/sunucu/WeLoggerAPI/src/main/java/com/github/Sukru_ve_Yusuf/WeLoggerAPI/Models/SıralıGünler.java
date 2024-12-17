/*
 * SıralıGünler
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
 * Günleri tarihsel olarak geçmişten geleceğe doğru
 * sıralı tutan bir çift yönlü bağlı liste.
 * 
 * @see Gün
 * @see IÇiftYönlüBağlıListe
 */
public class SıralıGünler extends Gün
        implements IÇiftYönlüBağlıListe<SıralıGünler>
{
    /**
     * Tarihsel sıralamada bir geçmişteki gün.
     */
    private SıralıGünler önceki;
    /**
     * Tarihsel sıralamada bir gelecekteki gün.
     */
    private SıralıGünler sonraki;
    
    /**
     * Verilen bilgilerle yeni bir SıralıGünler nesnesi oluşturur.
     * Oluşturulan nesneye yeni bir kimlik atanır.
     * 
     * @param başkahraman   Oluşturulan günü yaşamış kişi
     * @param açıklama      Bu gün hakkında açıklama metni
     * @param videolar      Başkahramanın bu günde çektiği videolar
     * @param tarih         Bu günün tarihi
     * 
     * @see Gün
     * @see Kullanıcı
     * @see SıralıVideolar
     * @see Calendar
     */
    public SıralıGünler(Kullanıcı başkahraman, String açıklama,
            SıralıVideolar videolar, Calendar tarih)
    {
        super(başkahraman, açıklama, videolar, tarih);
        this.setÖnceki(null);
        this.setSonraki(null);
    }
    /**
     * Bir gün nesnesini kullanarak yeni bir sıralı gün nesnesi oluşturur.
     * İsteğe bağlı olarak kimlik korunabilir ya da yenilenebilir.
     * 
     * @param kaynak_gün        İçeriği alınacak gün nesnesi
     * @param kimlik_yenilensin true ise yeni kimlik atanır,
     *                          false ise kaynak günün kimliği kopyalanır.
     * 
     * @see Gün
     */
    public SıralıGünler(Gün kaynak_gün, boolean kimlik_yenilensin)
    {
        super(kaynak_gün, kimlik_yenilensin);
        this.setÖnceki(null);
        this.setSonraki(null);
    }
    /**
     * Sıralı gün listesinin tarihsel sıraya uygun bir noktasına
     * yeni video ekler.
     * 
     * @param yeni_düğüm    Listeye eklenecek yeni sıralı gün nesnesi
     * @return  Eklenen yeni düğüm. Sıralama konusunda sorun yaşanırsa null.
     */
    @Override
    public SıralıGünler DüğümEkle(SıralıGünler yeni_düğüm)
    {
        if (yeni_düğüm.getTarih() == null)
        {
            return null;
        }
        SıralıGünler kök = this.Kök();
        SıralıGünler kafa = kök;
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
     * En geçmişteki güne erişim sağlar.
     * 
     * @return  Listede en geçmişteki gün
     */
    public SıralıGünler Kök()
    {
        SıralıGünler kafa = this;
        while (kafa.getÖnceki() != null)
        {
            kafa = kafa.getÖnceki();
        }
        return kafa;
    }
    /**
     * En gelecekteki güne erişim sağlar.
     * 
     * @return  Listede en gelecekteki gün
     */
    @Override
    public SıralıGünler UçDüğüm()
    {
        SıralıGünler kafa = this;
        while (kafa.getSonraki() != null)
        {
            kafa = kafa.getSonraki();
        }
        return kafa;
    }
    
    @Override
    public SıralıGünler Kopar()
    {
        SıralıGünler önce = this.getÖnceki();
        SıralıGünler sonra = this.getSonraki();
        
        this.setÖnceki(null);
        this.setSonraki(null);
        
        if (sonra != null)
        {
            sonra.setÖnceki(önce);
        }
        if (önce != null)
        {
            önce.setSonraki(sonra);
            // Dönüş önceliği köke yakın olsun diye 
            return önce;
        }
        return sonra;
    }
    
    /**
     * Bir geçmişteki güne erişim sağlar.
     * 
     * @return  Listede bir geçmişteki günün sıralı gün nesnesi
     */
    public SıralıGünler getÖnceki()
    {
        return this.önceki;
    }
    /**
     * Belirtilen günü bir gelecekteki gün olarak tanımlar.
     * Belirtilen günün tarihine erişilemiyorsa işlem yapılmaz.
     * Belirtilen günün tarihi daha sonraysa işlem yapılmaz.
     * 
     * @param yeni_önceki   Bir geleceğe yerleştirilecek gün
     * @return  İşlem başarılıysa true, başarısızsa false
     */
    public boolean setÖnceki(SıralıGünler yeni_önceki)
    {
        if (yeni_önceki == null)
        {
            this.önceki = null;
            return true;
        }
        // İki nesneden birinin tarihine erişilemiyorsa
        if (this.getTarih() == null || yeni_önceki.getTarih() == null)
        {
            return false;
        }
        // Yeni önceki gerçekten de sonra gelmiyorsa
        if (!yeni_önceki.getTarih().after(this.getTarih()))
        {
            this.önceki = yeni_önceki;
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Bir gelecekteki güne erişim sağlar.
     * 
     * @return  Listede bir gelecekteki günün sıralı gün nesnesi
     */
    public SıralıGünler getSonraki()
    {
        return this.sonraki;
    }
    /**
     * Belirtilen günü bir gelecekteki gün olarak tanımlar.
     * Belirtilen günün tarihine erişilemiyorsa işlem yapılmaz.
     * Belirtilen günün tarihi daha önceyse işlem yapılmaz.
     * 
     * @param yeni_sonraki  Bir geleceğe yerleştirilecek gün
     * @return  İşlem başarılıysa true, başarısızsa false
     */
    public boolean setSonraki(SıralıGünler yeni_sonraki)
    {
        if (yeni_sonraki == null)
        {
            this.sonraki = null;
            return true;
        }
        // İki nesneden birinin tarihine erişilemiyorsa
        if (this.getTarih() == null || yeni_sonraki.getTarih() == null)
        {
            return false;
        }
        // Yeni sonraki gerçekten de önce gelmiyorsa
        if (!yeni_sonraki.getTarih().before(this.getTarih()))
        {
            this.sonraki = yeni_sonraki;
            return true;
        }
        else
        {
            return false;
        }
    }
}
