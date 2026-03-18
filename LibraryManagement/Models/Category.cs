using System.ComponentModel.DataAnnotations;

namespace LibraryManagement.Models;

public class Category
{
    public int Id { get; set; }

    [Required(ErrorMessage = "Vui lòng nhập tên thể loại")]
    [Display(Name = "Tên thể loại")]
    public string Name { get; set; } = string.Empty;

    [Display(Name = "Mô tả")]
    public string Description { get; set; } = string.Empty;
}
