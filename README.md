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
