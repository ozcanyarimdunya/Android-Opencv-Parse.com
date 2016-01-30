#Mobil ben takip uygulaması
Bu uygulama Tasarım Süreçleri dersi projesi için yapılmıştır..

---------------------

### Uygulamanın çalışma şekli

-

| 1 - [Yeni kullanıcı ekleme](app/src/main/java/ozcan/com/design8december3/RegisterActivity.java)
------
Parse.com da `User` tablosuna yeni kullanıcı eklenir

-
  
| 2 - [Giriş yapma](app/src/main/java/ozcan/com/design8december3/LoginActivity.java)
------
Parse.com dan kullanıcı adı ve şifresi çekilip anamenüye yönlendirilir

-
| 3 - [Ana Menü](app/src/main/java/ozcan/com/design8december3/BodySelectionActivity.java)
--------
Benin vücutta bulunduğu kısım seçilir

-
| 4 - [Galeriden resim yükleme veya kameradan çekip yükleme](app/src/main/java/ozcan/com/design8december3/CameraActivity.java)
--------
Benin resmi Parse.com da `image_upload` tablosuna kaydedilir

-  

| 5 - [Resmi analiz etme (Gray Scale ve Edge Detection)](app/src/main/java/ozcan/com/design8december3/ReportActivity.java)
--------
Parse.com dan çekilen resim alınır ve işlenir

İşlenen resimler de `ImageResultAfterOpencv` tablosuna kaydedilir


------------------------------

#### Kullanılan araçlar

| 1. [Android Studio](http://developer.android.com/sdk/index.html)
------------

| 2. [Parse.com](http://parse.com)
------------

| 3. [Opencv](http://opencv.org)
-----------

--------------------
| [@ozcaan11](https://github.com/ozcaan11)
---------
