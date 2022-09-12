package com.yakupcan.nobetcieczane.util

import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView


object Tools {

    fun nestedScrollTo(nested: NestedScrollView, targetView: View) {
        nested.post { nested.scrollTo(500, targetView.bottom) }
    }

    fun trEngCevir(alinanMetin: String): String {
        var metin = alinanMetin
        var sonuc = ""
        val ilkHarf = charArrayOf('İ', 'ı', 'ü', 'Ü', 'ç', 'Ç', 'Ğ', 'ğ', 'Ş', 'ş', 'ö', 'Ö')
        val yeniHarf = charArrayOf('I', 'i', 'u', 'U', 'c', 'C', 'G', 'g', 'S', 's', 'o', 'O')
        for (sayac in ilkHarf.indices) {
            metin = metin.replace(ilkHarf[sayac], yeniHarf[sayac])
        }
        sonuc = metin
        return sonuc
    }

    fun engTrCevir(alinanMetin: String): String {
        var metin = alinanMetin
        var sonuc = ""
        val ilkHarf = charArrayOf('İ', 'ı', 'ü', 'Ü', 'ç', 'Ç', 'Ğ', 'ğ', 'Ş', 'ş', 'ö', 'Ö')
        val yeniHarf = charArrayOf('İ', 'ı', 'ü', 'Ü', 'ç', 'Ç', 'Ğ', 'ğ', 'Ş', 'ş', 'ö', 'Ö')
        for (sayac in ilkHarf.indices) {
            metin = metin.replace(yeniHarf[sayac], ilkHarf[sayac])
        }
        sonuc = metin
        return sonuc
    }
}