package com.example.servingwebcontent.model;
import java.util.ArrayList;

public class KhachHang {
    // Thông tin cơ bản
    private String CCCD;
    private String ten;
    private int tuoi;
    private String sdt;
    private String email;
    private String gioiTinh; // Nam, Nu, Khac
    private String diaChi;
    private String ngheNghiep;
    private String ngaySinh;
    private String soVisa;
    private String tenDangNhap;
    private String matKhau;
    private ArrayList<Ve> lichSuDatVe;

    private static ArrayList<KhachHang> danhSachKhachHang = new ArrayList<>();

    public KhachHang() {
        this.lichSuDatVe = new ArrayList<>();
    }

    public KhachHang(String CCCD, String ten, int tuoi, String sdt, String email) {
        this.CCCD = CCCD;
        this.ten = ten;
        this.tuoi = tuoi;
        this.sdt = sdt;
        this.email = email;
        this.lichSuDatVe = new ArrayList<>();
    }

    public KhachHang(String CCCD, String ten, int tuoi, String sdt, String email, String gioiTinh) {
        this(CCCD, ten, tuoi, sdt, email);
        this.gioiTinh = gioiTinh;
    }

    // Getters and Setters
    public String getCCCD() { return CCCD; }
    public void setCCCD(String CCCD) { this.CCCD = CCCD; }
    
    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    
    public int getTuoi() { return tuoi; }
    public void setTuoi(int tuoi) { this.tuoi = tuoi; }
    
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    
    public String getNgheNghiep() { return ngheNghiep; }
    public void setNgheNghiep(String ngheNghiep) { this.ngheNghiep = ngheNghiep; }
    
    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }
    
    public String getSoVisa() { return soVisa; }
    public void setSoVisa(String soVisa) { this.soVisa = soVisa; }
    
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    
    public ArrayList<Ve> getLichSuDatVe() { return lichSuDatVe; }
    public void setLichSuDatVe(ArrayList<Ve> lichSuDatVe) { this.lichSuDatVe = lichSuDatVe; }

    // CRUD
    public static void Create(KhachHang khachHang) {
        if (khachHang == null || khachHang.getCCCD() == null || khachHang.getCCCD().trim().isEmpty() ||
            khachHang.getTen() == null || khachHang.getTen().trim().isEmpty()) {
            System.out.println("Lỗi: Thông tin khách hàng không được để trống.");
            return;
        }
        if (getKhachHangByCCCD(khachHang.getCCCD()) != null) {
            System.out.println("Lỗi: Khách hàng đã tồn tại.");
            return;
        }
        
        danhSachKhachHang.add(khachHang);
        System.out.println("Đã thêm khách hàng thành công.");
    }

    public static ArrayList<KhachHang> Read() {
        if (danhSachKhachHang.isEmpty()) {
            System.out.println("Danh sách khách hàng trống.");
        } else {
            for (KhachHang kh : danhSachKhachHang) {
                kh.hienThiThongTin();
                System.out.println("---");
            }
        }
        System.out.println("Tổng số khách hàng: " + danhSachKhachHang.size());
        return new ArrayList<>(danhSachKhachHang);
    }

    public static void Read(String CCCD) {
        if (danhSachKhachHang.isEmpty()) {
            System.out.println("Danh sách khách hàng trống.");
            return;
        }
        KhachHang khachHang = getKhachHangByCCCD(CCCD);
        if (khachHang != null) {
            khachHang.hienThiThongTin();
        } else {
            System.out.println("Không tìm thấy khách hàng với CCCD: " + CCCD);
        }
    }

    public static void Update(String CCCD, KhachHang khachHang) {
        if (khachHang == null || khachHang.getCCCD() == null || khachHang.getCCCD().trim().isEmpty()) {
            System.out.println("Lỗi: Thông tin khách hàng không được để trống.");
            return;
        }
        int idx = getKhachHangIndexByCCCD(CCCD);
        if (idx == -1) {
            System.out.println("Không tìm thấy khách hàng với CCCD: " + CCCD);
            return;
        }
        
        khachHang.setCCCD(CCCD);
        danhSachKhachHang.set(idx, khachHang);
        System.out.println("Cập nhật khách hàng thành công.");
    }

    public static void Delete(String CCCD) {
        int idx = getKhachHangIndexByCCCD(CCCD);
        if (idx == -1) {
            System.out.println("Không tìm thấy khách hàng với CCCD: " + CCCD);
            return;
        }
        danhSachKhachHang.remove(idx);
        System.out.println("Đã xóa khách hàng thành công.");
    }

    // Phương thức tìm kiếm
    public static KhachHang getKhachHangByCCCD(String CCCD) {
        for (KhachHang khachHang : danhSachKhachHang) {
            if (khachHang.getCCCD() != null && khachHang.getCCCD().equals(CCCD)) {
                return khachHang;
            }
        }
        return null;
    }

    public static int getKhachHangIndexByCCCD(String CCCD) {
        for (int i = 0; i < danhSachKhachHang.size(); i++) {
            if (danhSachKhachHang.get(i).getCCCD() != null && danhSachKhachHang.get(i).getCCCD().equals(CCCD)) {
                return i;
            }
        }
        return -1;
    }

    // Phương thức tìm kiếm theo tên
    public static ArrayList<KhachHang> timKiemTheoTen(String ten) {
        ArrayList<KhachHang> ketQua = new ArrayList<>();
        for (KhachHang khachHang : danhSachKhachHang) {
            if (khachHang.getTen() != null && khachHang.getTen().toLowerCase().contains(ten.toLowerCase())) {
                ketQua.add(khachHang);
            }
        }
        return ketQua;
    }

    // Phương thức tìm kiếm theo giới tính
    public static ArrayList<KhachHang> timKiemTheoGioiTinh(String gioiTinh) {
        ArrayList<KhachHang> ketQua = new ArrayList<>();
        for (KhachHang khachHang : danhSachKhachHang) {
            if (khachHang.getGioiTinh() != null && khachHang.getGioiTinh().equalsIgnoreCase(gioiTinh)) {
                ketQua.add(khachHang);
            }
        }
        return ketQua;
    }

    // Hiển thị thông tin
    public void hienThiThongTin() {
        System.out.println("CCCD: " + CCCD);
        System.out.println("Tên khách hàng: " + ten);
        System.out.println("Tuổi: " + tuoi);
        System.out.println("SĐT: " + sdt);
        System.out.println("Email: " + email);
        System.out.println("Giới tính: " + gioiTinh);
        System.out.println("Số vé đã đặt: " + (lichSuDatVe != null ? lichSuDatVe.size() : 0));
    }

    // Phương thức thống kê cho Admin
    public static void xemThongKe() {
        if (danhSachKhachHang.isEmpty()) {
            System.out.println("Danh sách khách hàng trống.");
            return;
        }
        System.out.println("=== THỐNG KÊ KHÁCH HÀNG ===");
        System.out.println("Tổng số khách hàng: " + danhSachKhachHang.size());
        
        // Thống kê theo giới tính
        int nam = 0, nu = 0, khac = 0;
        for (KhachHang kh : danhSachKhachHang) {
            if (kh.getGioiTinh() != null) {
                switch (kh.getGioiTinh().toLowerCase()) {
                    case "nam":
                        nam++;
                        break;
                    case "nu":
                    case "nữ":
                        nu++;
                        break;
                    default:
                        khac++;
                        break;
                }
            }
        }
        
        System.out.println("\nThống kê theo giới tính:");
        System.out.println("- Nam: " + nam);
        System.out.println("- Nữ: " + nu);
        System.out.println("- Khác: " + khac);
        
        // Thống kê theo độ tuổi
        int tre = 0, trungNien = 0, caoNien = 0;
        for (KhachHang kh : danhSachKhachHang) {
            int tuoi = kh.getTuoi();
            if (tuoi < 18) {
                tre++;
            } else if (tuoi < 60) {
                trungNien++;
            } else {
                caoNien++;
            }
        }
        
        System.out.println("\nThống kê theo độ tuổi:");
        System.out.println("- Dưới 18 tuổi: " + tre);
        System.out.println("- 18-59 tuổi: " + trungNien);
        System.out.println("- Trên 60 tuổi: " + caoNien);
        
        // Thống kê khách hàng có vé
        int coVe = 0, khongCoVe = 0;
        for (KhachHang kh : danhSachKhachHang) {
            if (kh.getLichSuDatVe() != null && !kh.getLichSuDatVe().isEmpty()) {
                coVe++;
            } else {
                khongCoVe++;
            }
        }
        
        System.out.println("\nThống kê theo lịch sử đặt vé:");
        System.out.println("- Có đặt vé: " + coVe);
        System.out.println("- Chưa đặt vé: " + khongCoVe);
    }

    // Phương thức tính tổng tiền khách hàng đã sử dụng
    public double tinhTongTienKhachHang() {
        double tongTien = 0;
        for (HoaDon hd : HoaDon.Read()) {
            if (this.CCCD != null && this.CCCD.equals(hd.getCCCD())) {
                tongTien += hd.getTongTien();
            }
        }
        return tongTien;
    }
} 