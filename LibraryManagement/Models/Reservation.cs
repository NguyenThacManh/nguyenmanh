using System.ComponentModel.DataAnnotations;

namespace LibraryManagement.Models;

public class Reservation
{
    public int Id { get; set; }
    public int BookId { get; set; }
    public int ReaderId { get; set; }

    [Display(Name = "Ngày đặt")]
    public DateTime ReservedDate { get; set; }

    [Display(Name = "Đã thông báo có sẵn")]
    public bool NotifiedAvailable { get; set; }

    public Book? Book { get; set; }
    public Reader? Reader { get; set; }
}
