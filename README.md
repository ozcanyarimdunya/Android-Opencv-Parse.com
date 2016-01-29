#Mobil ben takip uygulaması
Bu uygulama tasarım süreçleri dersi için yapılmıştır..

---------------------

##### Uygulamanın çalışma şekli

| Yeni kullanıcı ekleme
------

 * Parse.com da `User` tablosuna yeni kullanıcı eklenir
  
| Giriş yapma
------
  
 * Parse.com dan kullanıcı adı ve şifresi çekilip anamenüye yönlendirilir

| Ana Menü
--------
    
 * Benin vucutta bulunduğu kısım seçilir
  
| Galeriden resim yükleme veya kameradan çekip yükleme
--------
    
 * Benin resmi Parse.com da `image_upload` tablosuna kaydedilir
  
| Resmi analiz etme (Gray Scale ve Edge Detection)
--------
  
 * Parse.com dan çekilen resim alınır ve işlenir
  
 * İşlenen resimler de `ImageResultAfterOpencv` tablosuna kaydedilir

------------------------------

#### Kullanılan araçlar

| [Android Studio](http://developer.android.com/sdk/index.html)
------------

| [Parse.com](http://parse.com)
------------

| [Opencv](http://opencv.org)
-----------

--------------------
| [@ozcaan11](https://github.com/ozcaan11)
---------
