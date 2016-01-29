#Mobil ben takip uygulaması
Bu uygulama tasarım süreçleri dersi için yapılmıştır..

---------------------

### Uygulamanın çalışma şekli

| 1 - Yeni kullanıcı ekleme
------

 * Parse.com da `User` tablosuna yeni kullanıcı eklenir
  
| 2 - Giriş yapma
------
  
 * Parse.com dan kullanıcı adı ve şifresi çekilip anamenüye yönlendirilir

| 3 - Ana Menü
--------
    
 * Benin vucutta bulunduğu kısım seçilir
  
| 4 - Galeriden resim yükleme veya kameradan çekip yükleme
--------
    
 * Benin resmi Parse.com da `image_upload` tablosuna kaydedilir
  
| 5 - Resmi analiz etme (Gray Scale ve Edge Detection)
--------
  
 * Parse.com dan çekilen resim alınır ve işlenir
  
 * İşlenen resimler de `ImageResultAfterOpencv` tablosuna kaydedilir

------------------------------

#### Kullanılan araçlar

| 1 - [Android Studio](http://developer.android.com/sdk/index.html)
------------

| 2 - [Parse.com](http://parse.com)
------------

| 3 - [Opencv](http://opencv.org)
-----------

--------------------
| [@ozcaan11](https://github.com/ozcaan11)
---------
