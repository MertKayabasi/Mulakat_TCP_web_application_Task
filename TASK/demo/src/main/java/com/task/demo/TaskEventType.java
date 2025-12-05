package com.task.demo;
public enum TaskEventType {        //enum oluşturulması
    MESSAGE,                       //TCP üzerinden gelen text mesajlarını temsil eder
    CONNECT,                      //client bağlanınca taskservice içinde kaydedilen event için
    FILE,                         //dosya taransferi olan durumlardaki event türü
    DISCONNECT                    //Client TCP bağlantısını kapattığında loglama
}

