import { useState, useEffect } from "react";
import axios from "axios";
import Popup from "reactjs-popup";
import "reactjs-popup/dist/index.css";   // Popup için gerekli
import "./App.css";

function App() {
  const [message, setMessage] = useState("");
  const [sendStatus, setSendStatus] = useState("");    //hookların tanımlandığı bölüm
  const [stopStatus, setStopStatus] = useState("");
  const [messages, setMessages] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  const [isPopupOpen, setPopupOpen] = useState(false);

  const API_BASE = "http://localhost:8080";    //uri ın ilk kısmı 

  const onFileChange = (event) => {              
    setSelectedFile(event.target.files[0]);
  };

  // mesaj gönderme 
  const handleSend = async () => {
    if (!message.trim()) {
      setSendStatus("Lütfen bir mesaj yaz.");
      return;
    }

    try {
      const res = await fetch(`${API_BASE}/api/tcp/send`, {    //fetch, endpointe istek atılıyor
        method: "POST",                                        //Post isteği olduğu belirtiliyor
        headers: { "Content-Type": "text/plain;charset=UTF-8" },  //format
        body: message,
      });

      const text = await res.text();
      setSendStatus(text);
      setMessage("");
    } catch (err) {
      setSendStatus("Hata: " + err.message);
    }
  };

  // TCP Server durdurulması
  const handleStop = async () => {
    try {
      const res = await fetch(`${API_BASE}/api/tcp/stop`, { method: "POST" });  //fetch, endpointe istek atılıyor
      const text = await res.text();
      setStopStatus(text);                        
    } catch (err) {
      setStopStatus("Hata: " + err.message);
    }
  };

  // Mesajları Çek
  const handleLoadMessages = async () => {
    try {
      const res = await fetch(`${API_BASE}/api/tcp/messages`);
      const data = await res.json();
      setMessages(data);
    } catch (err) {
      setSendStatus("Mesajları çekerken hata: " + err.message);
    }
  };

  useEffect(() => {
    const interval = setInterval(() => {    //mesajların her bir saniyede yenilenmesini sağlıyor
      handleLoadMessages();
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  // Dosya Upload
  const onFileUpload = async () => {
    const formData = new FormData();
    formData.append("myFile", selectedFile);

    await axios.post(`${API_BASE}/api/tcp/files`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });

    setPopupOpen(true);
  };

// seçilen dosyanın bilgilerini ekranda gösteren yardımcı fonksiyonudur.
// dosya seçilmişse: adını, türünü ve son değiştirilme tarihini gösteriyor.
// dosya seçilmemişse: kullanıcıya "önce dosya seçin" uyarısı veriyor.
  const fileData = () => {
    if (selectedFile) {
      return (
        <div>
          <h2>File Details:</h2>
          <p>File Name: {selectedFile.name}</p>
          <p>File Type: {selectedFile.type}</p>
          <p>Last Modified: {new Date(selectedFile.lastModified).toDateString()}</p>
        </div>
      );
    } else {
      return (
        <div>
          <br />
          <h4>seçtikten sonra upload butonuna basın</h4>
        </div>
      );
    }
  };

  return (          //return ve sonrası uygulamanın ana arayüzünü oluşturan yapı 
          //tcp server kontrol butonları, file upload komponent, mesaj yazılan komponent hep burada oluşturulmuş ve eklenmiştir
          // Bu bölümde tüm UI bileşenleri ekrana çiziliyor
          //Yani uygulamadaki tüm etkileşimli komponentler burada bir araya getirilmiş ve ekranda gösterilmiştir.
    <div className="app-container">
      <h1>TCP Monitor Panel</h1>

      {/* Mesaj Gönder */}
      <div className="card">
        <h3>TCP Server'a Mesaj Gönder</h3>
        <textarea
          rows={3}
          className="input-area"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <button className="btn" onClick={handleSend}>Gönder</button>
        {sendStatus && <p className="status">{sendStatus}</p>}
      </div>

      {/* DOSYA GÖNDER */}
      <div className="card">
        <h3>Dosya Gönder</h3>
      {/*input alan kısım*/}
        <input type="file" onChange={onFileChange} className="file-input" /> 

        <button
          className="btn upload-btn"
          onClick={() => {
            if (!selectedFile) {
              alert("Lütfen dosya seçin!");
              return;
            }
            onFileUpload();
          }}
        >
          Upload
        </button>

        {fileData()}

        <Popup
          open={isPopupOpen}    //popup ın eklenmesi
          onClose={() => setPopupOpen(false)}  
          closeOnDocumentClick
          position="center center"
        >
          <div className="popup-content">
            <h3>Dosya başarıyla gönderildi!</h3>
            <p>{selectedFile ? selectedFile.name : ""}</p>
            <button className="btn" onClick={() => setPopupOpen(false)}>Kapat</button>
          </div>
        </Popup>
      </div>

      {/* TCP server kontrol */}
      <div className="card">
        <h3>TCP Server Kontrol</h3>
        <button className="btn stop-btn" onClick={handleStop}>
          TCP Server'ı Durdur
        </button>
        {stopStatus && <p className="status">{stopStatus}</p>}
      </div>

      {/* mesaj listesi */}
      <div className="card">
        <h3>Gelen TCP Mesajları</h3>
        <button className="btn small" onClick={handleLoadMessages}>Yenile</button>

        {messages.length === 0 ? (
          <p>Henüz mesaj yok.</p>
        ) : (
          <table className="msg-table">  
            <thead>
              <tr>
                <th>ID</th>
                <th>From</th>
                <th>Content</th>
                <th>Zaman</th>
              </tr>
            </thead>
            <tbody>
              {messages.map((m) => (
                <tr key={m.id}>
                  <td>{m.id}</td>
                  <td>{m.clientFrom}</td>
                  <td>{m.content}</td>
                  <td>{m.timestamp}</td>
                </tr>
              ))}             
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default App;
