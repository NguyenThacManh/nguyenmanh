using System.ComponentModel.DataAnnotations;

namespace LibraryManagement.Models;

public class Book
{
    public int Id { get; set; }

    [Required(ErrorMessage = "Vui lòng nhập tên sách")]
    [Display(Name = "Tên sách")]
    public string Title { get; set; } = string.Empty;

    [Required(ErrorMessage = "Vui lòng nhập tác giả")]
    [Display(Name = "Tác giả")]
    public string Author { get; set; } = string.Empty;

    [Required(ErrorMessage = "Vui lòng chọn thể loại")]
    [Display(Name = "Thể loại")]
    public string Category { get; set; } = string.Empty;

    [Range(1, 5000, ErrorMessage = "Số lượng phải lớn hơn 0")]
    [Display(Name = "Số lượng")]
    public int Quantity { get; set; }

    [Display(Name = "Vị trí kệ")]
    public string ShelfLocation { get; set; } = string.Empty;

    [Display(Name = "Năm xuất bản")]
    [Range(1800, 2100, ErrorMessage = "Năm xuất bản không hợp lệ")]
    public int? PublishYear { get; set; }

    [Display(Name = "Mã QR")]
    public string QrCode { get; set; } = string.Empty;

    [Display(Name = "Barcode")]
    public string Barcode { get; set; } = string.Empty;

    [Display(Name = "Ảnh bìa")]
    public string CoverImagePath { get; set; } = string.Empty;
}
