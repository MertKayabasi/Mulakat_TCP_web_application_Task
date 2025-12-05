package com.task.demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity   //Entity anotasyonu, entity class olduğunu belirtiyor
@Table(name = "events")  //tablo ismimiz
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //id (primary key) otomatik üretiliyor.
    private Long id;

    // Mesaj mı? Dosya mı? Bağlantı mı?
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)  //bu kolonun null olamayacağını belirtiyor.
    private TaskEventType type;


    // kim gönderdi
    private String clientFrom;

    // kime gitti
    private String clientTo;

    // mesaj içeriği, dosya adı da burada olabilir.
    @Column(columnDefinition = "TEXT")
    private String content;

    // dosya varsa
    private String originalFileName;   // kullanıcının gönderdiği ad
    private String storedFileName;     // sunucuda kaydedilen isim
    private Long fileSizeBytes;        // byte olarak

    // sunucunun zamanı, saati.
    @Column(nullable = false)
    private LocalDateTime timestamp; //zaman etiketi

    // yön bilgisi
    private String direction;

    // durum bilgisi, başarılı, başarılı değil gibi
    private String status;

    // constuctor
    public TaskEntity() {}

    //getter ve setter metodlar
    //kullanılmayan getter ve setter lar özellikle kaldırılmadı.

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TaskEventType getType() { return type; }
    public void setType(TaskEventType type) { this.type = type; }

    public String getClientFrom() { return clientFrom; }
    public void setClientFrom(String clientFrom) { this.clientFrom = clientFrom; }

    public String getClientTo() { return clientTo; }
    public void setClientTo(String clientTo) { this.clientTo = clientTo; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }

    public String getStoredFileName() { return storedFileName; }
    public void setStoredFileName(String storedFileName) { this.storedFileName = storedFileName; }

    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}

