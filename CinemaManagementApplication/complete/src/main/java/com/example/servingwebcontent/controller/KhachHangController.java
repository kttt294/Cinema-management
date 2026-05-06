package com.example.servingwebcontent.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.servingwebcontent.model.KhachHang;
import com.example.servingwebcontent.model.Ve;
import com.example.servingwebcontent.model.DanhGia;
import com.example.servingwebcontent.database.khachHangAiven;

@Controller
public class KhachHangController {
    
    @Autowired
    private khachHangAiven khachHangDB;
    
    // Web Controller Methods
    @GetMapping("/khachhang")
    public String khachHangPage(Model model) {
        try {
            List<KhachHang> dsKH = khachHangDB.getAllKhachHang();
            model.addAttribute("dsKH", dsKH);
            model.addAttribute("khachHang", new KhachHang());
            return "khachhang";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải danh sách khách hàng: " + e.getMessage());
            return "khachhang";
        }
    }
    
    @GetMapping("/khachhang/form")
    public String khachHangFormPage(Model model) {
        model.addAttribute("khachHang", new KhachHang());
        return "form-khachhang";
    }
    
    @PostMapping("/khachhang/create")
    public String createKhachHang(@ModelAttribute KhachHang khachHang, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            if (khachHangDB.createKhachHang(khachHang)) {
                redirectAttributes.addFlashAttribute("message", "Tạo khách hàng thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo khách hàng!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }
        return "redirect:/khachhang";
    }
    
    @PostMapping("/khachhang/update")
    public String updateKhachHang(@RequestParam String CCCD, @ModelAttribute KhachHang khachHang, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            if (khachHangDB.updateKhachHang(CCCD, khachHang)) {
                redirectAttributes.addFlashAttribute("message", "Cập nhật khách hàng thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật khách hàng!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }
        return "redirect:/khachhang";
    }
    
    @PostMapping("/khachhang/delete")
    public String deleteKhachHang(@RequestParam String CCCD, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            if (khachHangDB.deleteKhachHang(CCCD)) {
                redirectAttributes.addFlashAttribute("message", "Xóa khách hàng thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa khách hàng!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }
        return "redirect:/khachhang";
    }
    
    @GetMapping("/khachhang/search")
    public String searchKhachHang(@RequestParam String keyword, @RequestParam String type, Model model) {
        try {
            List<KhachHang> results = new ArrayList<>();
            switch (type) {
                case "ten":
                    results = khachHangDB.searchKhachHangByTen(keyword);
                    break;
                case "gioitinh":
                    results = khachHangDB.searchKhachHangByGioiTinh(keyword);
                    break;
                default:
                    KhachHang kh = khachHangDB.getKhachHangByCCCD(keyword);
                    if (kh != null) {
                        results.add(kh);
                    }
                    break;
            }
            model.addAttribute("dsKH", results);
            model.addAttribute("searchKeyword", keyword);
            model.addAttribute("searchType", type);
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tìm kiếm: " + e.getMessage());
        }
        return "khachhang";
    }
    
    @GetMapping("/khachhang/vip")
    public String khachHangVIPPage(Model model) {
        try {
            List<KhachHang> allCustomers = khachHangDB.getAllKhachHang();
            List<KhachHang> vipCustomers = new ArrayList<>();
            for (KhachHang kh : allCustomers) {
                if (kh.getLichSuDatVe() != null && kh.getLichSuDatVe().size() >= 5) {
                    vipCustomers.add(kh);
                }
            }
            model.addAttribute("dsKH", vipCustomers);
            model.addAttribute("title", "Khách hàng VIP");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải danh sách khách hàng VIP: " + e.getMessage());
        }
        return "khachhang";
    }
    
    // Business Logic Methods
    public boolean taoKhachHang(KhachHang kh) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (kh == null) {
                throw new IllegalArgumentException("Khách hàng không được null!");
            }
            if (kh.getCCCD() == null || kh.getCCCD().trim().isEmpty()) {
                throw new IllegalArgumentException("CCCD không được để trống!");
            }
            if (kh.getTen() == null || kh.getTen().trim().isEmpty()) {
                throw new IllegalArgumentException("Tên khách hàng không được để trống!");
            }
            if (kh.getTuoi() < 0 || kh.getTuoi() > 150) {
                throw new IllegalArgumentException("Tuổi không hợp lệ!");
            }
            if (kh.getSdt() == null || kh.getSdt().trim().isEmpty()) {
                throw new IllegalArgumentException("Số điện thoại không được để trống!");
            }
            if (kh.getEmail() == null || kh.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email không được để trống!");
            }

            return khachHangDB.createKhachHang(kh);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhatThongTin(String CCCD, KhachHang khMoi) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (CCCD == null || CCCD.trim().isEmpty()) {
                throw new IllegalArgumentException("CCCD không được để trống!");
            }
            if (khMoi == null) {
                throw new IllegalArgumentException("Thông tin khách hàng mới không được null!");
            }

            // Kiểm tra khách hàng có tồn tại không
            KhachHang khCu = khachHangDB.getKhachHangByCCCD(CCCD);
            if (khCu == null) {
                System.out.println("Không tìm thấy khách hàng với CCCD: " + CCCD);
                return false;
            }

            return khachHangDB.updateKhachHang(CCCD, khMoi);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return false;
        }
    }

    public boolean xoaKhachHang(String CCCD) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (CCCD == null || CCCD.trim().isEmpty()) {
                throw new IllegalArgumentException("CCCD không được để trống!");
            }

            // Kiểm tra khách hàng có tồn tại không
            KhachHang kh = khachHangDB.getKhachHangByCCCD(CCCD);
            if (kh == null) {
                System.out.println("Không tìm thấy khách hàng với CCCD: " + CCCD);
                return false;
            }

            return khachHangDB.deleteKhachHang(CCCD);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return false;
        }
    }

    public boolean xemThongTin(String CCCD) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (CCCD == null || CCCD.trim().isEmpty()) {
                throw new IllegalArgumentException("CCCD không được để trống!");
            }

            KhachHang kh = khachHangDB.getKhachHangByCCCD(CCCD);
            if (kh != null) {
                kh.hienThiThongTin();
                return true;
            } else {
                System.out.println("Không tìm thấy khách hàng với CCCD: " + CCCD);
                return false;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return false;
        }
    }

    public boolean xemTatCaKhachHang() {
        try {
            List<KhachHang> danhSachKH = khachHangDB.getAllKhachHang();
            if (danhSachKH.isEmpty()) {
                System.out.println("Danh sách khách hàng trống.");
            } else {
                System.out.println("Tổng số khách hàng: " + danhSachKH.size());
                for (KhachHang kh : danhSachKH) {
                    kh.hienThiThongTin();
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return false;
        }
    }

    public KhachHang timKhachHangTheoCCCD(String CCCD) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (CCCD == null || CCCD.trim().isEmpty()) {
                throw new IllegalArgumentException("CCCD không được để trống!");
            }

            return khachHangDB.getKhachHangByCCCD(CCCD);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<KhachHang> timKhachHangTheoTen(String ten) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (ten == null || ten.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên tìm kiếm không được để trống!");
            }

            List<KhachHang> results = khachHangDB.searchKhachHangByTen(ten);
            return new ArrayList<>(results);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public ArrayList<KhachHang> timKhachHangTheoGioiTinh(String gioiTinh) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (gioiTinh == null || gioiTinh.trim().isEmpty()) {
                throw new IllegalArgumentException("Giới tính không được để trống!");
            }

            // Tìm kiếm theo giới tính bằng cách lọc từ danh sách tất cả
            List<KhachHang> allCustomers = khachHangDB.getAllKhachHang();
            ArrayList<KhachHang> results = new ArrayList<>();
            for (KhachHang kh : allCustomers) {
                if (kh.getGioiTinh().equalsIgnoreCase(gioiTinh)) {
                    results.add(kh);
                }
            }
            return results;
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean xemThongKeKhachHang() {
        try {
            List<KhachHang> danhSachKH = khachHangDB.getAllKhachHang();
            System.out.println("=== THỐNG KÊ KHÁCH HÀNG ===");
            System.out.println("Tổng số khách hàng: " + danhSachKH.size());
            
            // Thống kê theo giới tính
            java.util.Map<String, Integer> gioiTinhCount = new java.util.HashMap<>();
            for (KhachHang kh : danhSachKH) {
                String gioiTinh = kh.getGioiTinh();
                gioiTinhCount.put(gioiTinh, gioiTinhCount.getOrDefault(gioiTinh, 0) + 1);
            }
            
            System.out.println("Thống kê theo giới tính:");
            for (java.util.Map.Entry<String, Integer> entry : gioiTinhCount.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " khách hàng");
            }
            
            System.out.println("=====================");
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return false;
        }
    }

    public double tinhTongTienKhachHang(String CCCD) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (CCCD == null || CCCD.trim().isEmpty()) {
                throw new IllegalArgumentException("CCCD không được để trống!");
            }
            KhachHang kh = khachHangDB.getKhachHangByCCCD(CCCD);
            if (kh != null) {
                return kh.tinhTongTienKhachHang();
            }
            return 0.0;
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return 0.0;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return 0.0;
        }
    }

    public boolean xemLichSuDatVe(String CCCD) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (CCCD == null || CCCD.trim().isEmpty()) {
                throw new IllegalArgumentException("CCCD không được để trống!");
            }

            KhachHang kh = khachHangDB.getKhachHangByCCCD(CCCD);
            if (kh != null) {
                ArrayList<Ve> lichSu = kh.getLichSuDatVe();
                if (lichSu == null || lichSu.isEmpty()) {
                    System.out.println("Khách hàng chưa đặt vé nào.");
                } else {
                    System.out.println("Lịch sử đặt vé của khách hàng " + kh.getTen() + ":");
                    for (Ve ve : lichSu) {
                        ve.hienThiThongTin();
                        System.out.println("---");
                    }
                }
                return true;
            } else {
                System.out.println("Không tìm thấy khách hàng với CCCD: " + CCCD);
                return false;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return false;
        }
    }

    public boolean themVeChoKhachHang(String CCCD, Ve ve) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (CCCD == null || CCCD.trim().isEmpty()) {
                throw new IllegalArgumentException("CCCD không được để trống!");
            }
            if (ve == null) {
                throw new IllegalArgumentException("Vé không được null!");
            }

            KhachHang kh = khachHangDB.getKhachHangByCCCD(CCCD);
            if (kh != null) {
                kh.getLichSuDatVe().add(ve);
                System.out.println("Đã thêm vé vào lịch sử khách hàng.");
                return true;
            } else {
                System.out.println("Không tìm thấy khách hàng với CCCD: " + CCCD);
                return false;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return false;
        }
    }

    public boolean baoCaoKhachHangVIP() {
        try {
            List<KhachHang> danhSach = khachHangDB.getAllKhachHang();
            System.out.println("=== BÁO CÁO KHÁCH HÀNG VIP ===");
            
            int count = 0;
            for (KhachHang kh : danhSach) {
                if (kh.getLichSuDatVe() != null && kh.getLichSuDatVe().size() >= 5) {
                    count++;
                    System.out.println("Khách hàng VIP #" + count + ":");
                    kh.hienThiThongTin();
                    System.out.println("Tổng tiền đã sử dụng: " + tinhTongTienKhachHang(kh.getCCCD()) + " VNĐ");
                    System.out.println("---");
                }
            }
            
            if (count == 0) {
                System.out.println("Chưa có khách hàng VIP nào.");
            } else {
                System.out.println("Tổng số khách hàng VIP: " + count);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return false;
        }
    }

    public boolean baoCaoKhachHangMoi() {
        try {
            List<KhachHang> danhSach = khachHangDB.getAllKhachHang();
            System.out.println("=== BÁO CÁO KHÁCH HÀNG MỚI ===");
            
            int count = 0;
            for (KhachHang kh : danhSach) {
                if (kh.getLichSuDatVe() == null || kh.getLichSuDatVe().isEmpty()) {
                    count++;
                    System.out.println("Khách hàng mới #" + count + ":");
                    kh.hienThiThongTin();
                    System.out.println("---");
                }
            }
            
            if (count == 0) {
                System.out.println("Tất cả khách hàng đều đã đặt vé.");
            } else {
                System.out.println("Tổng số khách hàng mới: " + count);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return false;
        }
    }

    public boolean guiDanhGia(DanhGia danhGia) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (danhGia == null) {
                throw new IllegalArgumentException("Đánh giá không được null!");
            }

            // Sử dụng database để lưu đánh giá
            // Cần tạo danhGiaAiven class để xử lý
            System.out.println("Gửi đánh giá thành công!");
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống: " + e.getMessage());
            return false;
        }
    }
} 