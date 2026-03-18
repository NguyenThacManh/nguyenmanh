# LibraryManagement - ASP.NET Core MVC 8

Project mẫu quản lý thư viện xây dựng bằng ASP.NET Core MVC 8.

## 6 module chính
1. Dashboard
2. Books
3. Categories
4. Readers
5. Borrow / Return
6. Reports

## Bảo mật truy cập
- Bắt buộc đăng nhập trước khi sử dụng bất kỳ chức năng nào của hệ thống.
- Nếu chưa đăng nhập, mọi route sẽ được chuyển hướng về `/Account/Login` và thanh điều hướng sẽ chỉ hiển thị Login/Register.

## Tính năng mở rộng đã thêm
### 1) Chức năng người dùng
- Đăng ký tài khoản.
- Đăng nhập / đăng xuất.
- Xem lịch sử mượn sách theo người đọc.
- Đặt trước sách.

### 2) Thống kê
- Sách được mượn nhiều nhất.
- Độc giả mượn nhiều nhất.
- Danh sách sách quá hạn.

### 3) Thông báo
- Thông báo sách sắp đến hạn trả (trong 2 ngày).
- Thông báo sách đặt trước đã có sẵn (khi còn số lượng > 0).

### 4) Tính năng nâng cao
- Tìm kiếm nâng cao (từ khóa, thể loại, sắp xếp, số dòng/trang).
- Giao diện danh sách sách được làm đẹp (thẻ lọc, badge, table responsive).
- QR code cho sách (lưu chuỗi QR trong thông tin sách).
- Barcode scan khi mượn sách (nhập/quét barcode để tự chọn sách).
- Upload ảnh bìa sách.

## Cách chạy
Yêu cầu: .NET 8 SDK

```bash
cd LibraryManagement
dotnet restore
dotnet run
```

Mặc định truy cập: `https://localhost:5001` hoặc URL được in ở terminal.
