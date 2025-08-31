# 🌐 IP Address Tracker & Lookup History System  

## 👨‍💻 Author  
**Syed Hassan Tayyab**  
Department of Artificial Intelligence, UET Peshawar  

---

## 📌 Overview  
This is a **Java-based IP Address Tracker & Lookup History System** with both **Admin** and **User** interfaces.  
It allows users to validate IPs, perform lookups, and fetch geolocation details via an external API, while admins can monitor system usage, logs, and alerts.  

---

## ✨ Features  

### ✅ User Features  
- Register/Login with MySQL authentication  
- Validate and lookup IP addresses  
- Fetch IP geolocation details using API  
- Save lookup results into MySQL database  
- Export lookup history to **PDF/CSV**  

### ✅ Admin Features  
- Manage users and their activity  
- Monitor IP search history (`ip_search_history`)  
- Track system logs (`api_requests`, `alerts`)  
- Analyze frequent IP lookups and detect anomalies  

### ✅ GUI & Design  
- Large **Welcome Screen** with role selection (Admin/User)  
- Colorful, user-friendly login and registration panels  
- Interactive dashboards for both users and admins  
- Export options directly from GUI  

---
  
## 💻 Languages & Tools  
<p align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" alt="Java" width="60" height="60"/>
  &nbsp;&nbsp;&nbsp;
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original-wordmark.svg" alt="MySQL" width="70" height="70"/>
  &nbsp;&nbsp;&nbsp;
  <img src="https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA_icon.png" alt="IntelliJ IDEA" width="60" height="60"/>
  &nbsp;&nbsp;&nbsp;
  <img src="https://img.icons8.com/ios-filled/100/000000/mysql-workbench.png" alt="MySQL Workbench" width="60" height="60"/>
</p>

---

## 🛠️ Tech Stack  
- **Programming Language:** Java (Swing/JavaFX for GUI)  
- **Database:** MySQL (via JDBC)  
- **Libraries/Tools:**  
  - iText / Apache POI → Export to PDF & CSV  
  - External IP API (e.g., ip-api.com)  
  - Java AWT & Swing for GUI  

---

## 🚀 How to Run  
1. Clone this repository:  
   ```bash
   git clone https://github.com/your-username/ip-address-tracker.git
   cd ip-address-tracker
