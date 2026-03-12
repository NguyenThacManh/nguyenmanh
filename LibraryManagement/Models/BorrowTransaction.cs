using System.ComponentModel.DataAnnotations;

namespace LibraryManagement.Models;

public class BorrowTransaction
{
    public int Id { get; set; }

    [Display(Name = "Sách")]
    public int BookId { get; set; }

    [Display(Name = "Người đọc")]
    public int ReaderId { get; set; }

    [Display(Name = "Ngày mượn")]
    public DateTime BorrowDate { get; set; }

    [Display(Name = "Hạn trả")]
    public DateTime DueDate { get; set; }

    [Display(Name = "Ngày trả")]
    public DateTime? ReturnDate { get; set; }

    public Book? Book { get; set; }
    public Reader? Reader { get; set; }

    public bool IsReturned => ReturnDate.HasValue;
}
