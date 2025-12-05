# M-lakat_TCP_web_application_Task
TCP sunucusu, mesaj/dosya transferi ve canlı log görüntüleme özelliklerine sahip full-stack bir Spring Boot + React uygulaması.

Bu proje, bir mülakat görevi kapsamında geliştirilmiş olup, tamamen TCP iletişimi, dosya transferi ve canlı olay izleme odaklı bir full-stack uygulamadır.
Uygulama;
Backend: Java Spring Boot
Frontend: React
Veritabanı: PostgreSQL
TCP Sunucu: Java ServerSocket (9090 portu)
kullanılarak geliştirilmiştir.

Bu sistem, dış dünyadan gelen tüm TCP mesajlarını, dosya transferlerini gerçek zamanlı olarak izlemek ve bunları hem veritabanına kaydetmek hem de kullanıcı arayüzünde canlı göstermek için tasarlanmıştır.
UI üzerinden kullanıcı:
TCP sunucusuna mesaj gönderebilir
Dosya yükleyip TCP üzerinden aktarabilir
TCP sunucusundan gelen tüm aktiviteleri (message/file/connect/disconnect) canlı takip edebilir
TCP sunucusunu UI üzerinden durdurabilir

Uygulama açıldığında Spring Boot tarafından otomatik olarak başlatılır.

TCP sunucu:
9090 portunda dinler
Gelen her bağlantıyı ayrı bir thread ile yönetir
Her mesajı işler ve cevap döner
Tüm dosya transferlerini kabul eder
Gelen her olayı veritabanına kaydeder
Spring boot react uygulaması ve tcp server arasında köprü görevi görür.
