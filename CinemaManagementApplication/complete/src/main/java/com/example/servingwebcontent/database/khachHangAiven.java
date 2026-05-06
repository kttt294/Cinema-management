package com.example.servingwebcontent.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import com.example.servingwebcontent.model.KhachHang;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class khachHangAiven {
    
    @Autowired
    private myDBConnection mydb;
  
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> danhSachKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM khachhang ORDER BY ten";
        
        try (Connection conn = mydb.getOnlyConn()) {
            if (conn == null) {
                System.out.println("getAllKhachHang: Kết nối database thất bại");
                return danhSachKhachHang;
            }
            
            createTableIfNotExists(conn);
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet reset = pstmt.executeQuery()) {
                
                while (reset.next()) {
                    KhachHang kh = mapResultSetToKhachHang(reset);
                    danhSachKhachHang.add(kh);
                }
                System.out.println("getAllKhachHang: Lấy được " + danhSachKhachHang.size() + " khách hàng");
            }
        } catch (Exception e) {
            System.out.println("getAllKhachHang: Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachKhachHang;
    }
    
    private void createTableIfNotExists(Connection conn) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS khachhang (" +
            "CCCD VARCHAR(20) PRIMARY KEY," +
            "ten VARCHAR(255) NOT NULL," +
            "tuoi INT," +
            "sdt VARCHAR(15)," +
            "email VARCHAR(255)," +
            "gioiTinh VARCHAR(10)," +
            "diaChi TEXT," +
            "ngheNghiep VARCHAR(100)," +
            "ngaySinh VARCHAR(20)," +
            "soVisa VARCHAR(20)," +
            "tenDangNhap VARCHAR(50)," +
            "matKhau VARCHAR(255)" +
            ")";
        
        try (PreparedStatement pstmt = conn.prepareStatement(createTableSQL)) {
            pstmt.executeUpdate();
            System.out.println("Bảng khachhang đã được tạo hoặc đã tồn tại");
        } catch (Exception e) {
            System.out.println("Lỗi tạo bảng khachhang: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public KhachHang getKhachHangByCCCD(String CCCD) {
        String sql = "SELECT * FROM khachhang WHERE CCCD = ?";
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return null;
            
            pstmt.setString(1, CCCD);
            try (ResultSet reset = pstmt.executeQuery()) {
                if (reset.next()) {
                    return mapResultSetToKhachHang(reset);
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public List<KhachHang> searchKhachHangByTen(String ten) {
        List<KhachHang> danhSachKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM khachhang WHERE ten LIKE ? ORDER BY ten";
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return danhSachKhachHang;
            
            pstmt.setString(1, "%" + ten + "%");
            try (ResultSet reset = pstmt.executeQuery()) {
                while (reset.next()) {
                    danhSachKhachHang.add(mapResultSetToKhachHang(reset));
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm kiếm khách hàng theo tên: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachKhachHang;
    }
    
    public boolean createKhachHang(KhachHang khachHang) {
        String sql = "INSERT INTO khachhang (CCCD, ten, tuoi, sdt, email, gioiTinh, diaChi, ngheNghiep, ngaySinh, soVisa, tenDangNhap, matKhau) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = mydb.getOnlyConn()) {
            if (conn == null) {
                System.out.println("createKhachHang: Kết nối database thất bại");
                return false;
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, khachHang.getCCCD());
                pstmt.setString(2, khachHang.getTen());
                pstmt.setInt(3, khachHang.getTuoi());
                pstmt.setString(4, khachHang.getSdt());
                pstmt.setString(5, khachHang.getEmail());
                pstmt.setString(6, khachHang.getGioiTinh());
                pstmt.setString(7, khachHang.getDiaChi());
                pstmt.setString(8, khachHang.getNgheNghiep());
                pstmt.setString(9, khachHang.getNgaySinh());
                pstmt.setString(10, khachHang.getSoVisa());
                pstmt.setString(11, khachHang.getTenDangNhap());
                pstmt.setString(12, khachHang.getMatKhau());
                
                int result = pstmt.executeUpdate();
                System.out.println("createKhachHang: Đã thêm khách hàng " + khachHang.getTen() + ", kết quả: " + (result > 0));
                return result > 0;
            }
        } catch (Exception e) {
            System.out.println("createKhachHang: Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateKhachHang(String CCCD, KhachHang khachHang) {
        String sql = "UPDATE khachhang SET ten = ?, tuoi = ?, sdt = ?, email = ?, gioiTinh = ?, " +
                    "diaChi = ?, ngheNghiep = ?, ngaySinh = ?, soVisa = ? WHERE CCCD = ?";
        
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return false;
            
            pstmt.setString(1, khachHang.getTen());
            pstmt.setInt(2, khachHang.getTuoi());
            pstmt.setString(3, khachHang.getSdt());
            pstmt.setString(4, khachHang.getEmail());
            pstmt.setString(5, khachHang.getGioiTinh());
            pstmt.setString(6, khachHang.getDiaChi());
            pstmt.setString(7, khachHang.getNgheNghiep());
            pstmt.setString(8, khachHang.getNgaySinh());
            pstmt.setString(9, khachHang.getSoVisa());
            pstmt.setString(10, CCCD);
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật khách hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteKhachHang(String CCCD) {
        String sql = "DELETE FROM khachhang WHERE CCCD = ?";
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return false;
            
            pstmt.setString(1, CCCD);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            System.out.println("Lỗi xóa khách hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public int getTotalKhachHang() {
        String sql = "SELECT COUNT(*) as total FROM khachhang";
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet reset = pstmt.executeQuery()) {
            
            if (conn == null) return 0;
            if (reset.next()) {
                return reset.getInt("total");
            }
        } catch (Exception e) {
            System.out.println("Lỗi đếm tổng khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public List<KhachHang> searchKhachHangByGioiTinh(String gioiTinh) {
        List<KhachHang> danhSachKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM khachhang WHERE gioiTinh = ? ORDER BY ten";
        try (Connection conn = mydb.getOnlyConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return danhSachKhachHang;
            
            pstmt.setString(1, gioiTinh);
            try (ResultSet reset = pstmt.executeQuery()) {
                while (reset.next()) {
                    danhSachKhachHang.add(mapResultSetToKhachHang(reset));
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm kiếm theo giới tính: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachKhachHang;
    }

    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws java.sql.SQLException {
        KhachHang khachHang = new KhachHang();
        khachHang.setCCCD(rs.getString("CCCD"));
        khachHang.setTen(rs.getString("ten"));
        khachHang.setTuoi(rs.getInt("tuoi"));
        khachHang.setSdt(rs.getString("sdt"));
        khachHang.setEmail(rs.getString("email"));
        khachHang.setGioiTinh(rs.getString("gioiTinh"));
        khachHang.setDiaChi(rs.getString("diaChi"));
        khachHang.setNgheNghiep(rs.getString("ngheNghiep"));
        khachHang.setNgaySinh(rs.getString("ngaySinh"));
        khachHang.setSoVisa(rs.getString("soVisa"));
        khachHang.setTenDangNhap(rs.getString("tenDangNhap"));
        khachHang.setMatKhau(rs.getString("matKhau"));
        return khachHang;
    }
}