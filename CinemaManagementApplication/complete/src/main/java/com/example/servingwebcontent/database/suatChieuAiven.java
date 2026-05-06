package com.example.servingwebcontent.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import com.example.servingwebcontent.model.SuatChieu;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.PreparedStatement;

@Component
public class suatChieuAiven {
    
    @Autowired
    private myDBConnection mydb;
  
    public List<SuatChieu> getAllSuatChieu() {
        List<SuatChieu> danhSachSuatChieu = new ArrayList<>();
        String sql = "SELECT * FROM suatchieu";
        
        try (Connection conn = mydb.getOnlyConn()) {
            if (conn == null) return danhSachSuatChieu;
            
            createTableIfNotExists(conn);
             
            try (Statement sta = conn.createStatement();
                 ResultSet reset = sta.executeQuery(sql)) {
                
                System.out.println("Lấy tất cả dữ liệu suất chiếu từ database: ");
                while (reset.next()) {
                    SuatChieu suatChieu = mapResultSetToSuatChieu(reset);
                    danhSachSuatChieu.add(suatChieu);
                    System.out.println("Mã suất chiếu: " + suatChieu.getMaSuatChieu() + " | Mã phim: " + suatChieu.getMaPhim());
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy dữ liệu suất chiếu: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachSuatChieu;
    }
    
    private void createTableIfNotExists(Connection conn) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS suatchieu (" +
            "maSuatChieu VARCHAR(50) PRIMARY KEY," +
            "maPhim VARCHAR(50)," +
            "maPhong VARCHAR(50)," +
            "thoiGianBatDau DATETIME," +
            "thoiGianKetThuc DATETIME," +
            "FOREIGN KEY (maPhim) REFERENCES phim(maPhim)," +
            "FOREIGN KEY (maPhong) REFERENCES phongchieu(maPhong)" +
            ")";
            
        try (PreparedStatement pstmt = conn.prepareStatement(createTableSQL)) {
            pstmt.executeUpdate();
            System.out.println("Bảng suatchieu đã được tạo hoặc đã tồn tại");
        } catch (Exception e) {
            System.out.println("Lỗi tạo bảng suatchieu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public SuatChieu getSuatChieuById(String maSuatChieu) {
        String sql = "SELECT * FROM suatchieu WHERE maSuatChieu = ?";
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return null;
            
            pstmt.setString(1, maSuatChieu);
            try (ResultSet reset = pstmt.executeQuery()) {
                if (reset.next()) {
                    return mapResultSetToSuatChieu(reset);
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm suất chiếu: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public List<SuatChieu> getSuatChieuByPhim(String maPhim) {
        List<SuatChieu> danhSachSuatChieu = new ArrayList<>();
        String sql = "SELECT * FROM suatchieu WHERE maPhim = ?";
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return danhSachSuatChieu;
            
            pstmt.setString(1, maPhim);
            try (ResultSet reset = pstmt.executeQuery()) {
                while (reset.next()) {
                    danhSachSuatChieu.add(mapResultSetToSuatChieu(reset));
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm suất chiếu theo phim: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachSuatChieu;
    }
    
    public List<SuatChieu> getSuatChieuByPhong(String maPhong) {
        List<SuatChieu> danhSachSuatChieu = new ArrayList<>();
        String sql = "SELECT * FROM suatchieu WHERE maPhong = ?";
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return danhSachSuatChieu;
            
            pstmt.setString(1, maPhong);
            try (ResultSet reset = pstmt.executeQuery()) {
                while (reset.next()) {
                    danhSachSuatChieu.add(mapResultSetToSuatChieu(reset));
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm suất chiếu theo phòng: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachSuatChieu;
    }
    
    public boolean createSuatChieu(SuatChieu suatChieu) {
        String sql = "INSERT INTO suatchieu (maSuatChieu, maPhim, maPhong, thoiGianBatDau, thoiGianKetThuc) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return false;
            
            pstmt.setString(1, suatChieu.getMaSuatChieu());
            pstmt.setString(2, suatChieu.getMaPhim());
            pstmt.setString(3, suatChieu.getMaPhong());
            
            if (suatChieu.getThoiGianBatDau() != null) {
                pstmt.setString(4, suatChieu.getThoiGianBatDau().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } else {
                pstmt.setNull(4, java.sql.Types.VARCHAR);
            }
            
            if (suatChieu.getThoiGianKetThuc() != null) {
                pstmt.setString(5, suatChieu.getThoiGianKetThuc().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } else {
                pstmt.setNull(5, java.sql.Types.VARCHAR);
            }
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            System.out.println("Lỗi tạo suất chiếu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateSuatChieu(String maSuatChieu, SuatChieu suatChieuMoi) {
        String sql = "UPDATE suatchieu SET maPhim = ?, maPhong = ?, thoiGianBatDau = ?, thoiGianKetThuc = ? WHERE maSuatChieu = ?";
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return false;
            
            pstmt.setString(1, suatChieuMoi.getMaPhim());
            pstmt.setString(2, suatChieuMoi.getMaPhong());
            
            if (suatChieuMoi.getThoiGianBatDau() != null) {
                pstmt.setString(3, suatChieuMoi.getThoiGianBatDau().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } else {
                pstmt.setNull(3, java.sql.Types.VARCHAR);
            }
            
            if (suatChieuMoi.getThoiGianKetThuc() != null) {
                pstmt.setString(4, suatChieuMoi.getThoiGianKetThuc().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } else {
                pstmt.setNull(4, java.sql.Types.VARCHAR);
            }
            
            pstmt.setString(5, maSuatChieu);
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật suất chiếu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteSuatChieu(String maSuatChieu) {
        String sql = "DELETE FROM suatchieu WHERE maSuatChieu = ?";
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return false;
            
            pstmt.setString(1, maSuatChieu);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            System.out.println("Lỗi xóa suất chiếu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private SuatChieu mapResultSetToSuatChieu(ResultSet rs) throws java.sql.SQLException {
        SuatChieu suatChieu = new SuatChieu();
        suatChieu.setMaSuatChieu(rs.getString("maSuatChieu"));
        suatChieu.setMaPhim(rs.getString("maPhim"));
        suatChieu.setMaPhong(rs.getString("maPhong"));
        
        String startStr = rs.getString("thoiGianBatDau");
        String endStr = rs.getString("thoiGianKetThuc");
        
        if (startStr != null && !startStr.isEmpty()) {
            try {
                // MySQL might return YYYY-MM-DD HH:MM:SS, but ISO_LOCAL_DATE_TIME expects T separator
                // Let's use a more flexible approach or replace space with T
                String normalizedStart = startStr.replace(" ", "T");
                suatChieu.setThoiGianBatDau(LocalDateTime.parse(normalizedStart, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } catch (Exception e) {
                System.err.println("Could not parse start time: " + startStr);
            }
        }
        if (endStr != null && !endStr.isEmpty()) {
            try {
                String normalizedEnd = endStr.replace(" ", "T");
                suatChieu.setThoiGianKetThuc(LocalDateTime.parse(normalizedEnd, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } catch (Exception e) {
                System.err.println("Could not parse end time: " + endStr);
            }
        }
        
        return suatChieu;
    }
} 