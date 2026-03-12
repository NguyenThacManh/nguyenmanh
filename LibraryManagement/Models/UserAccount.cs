using System.ComponentModel.DataAnnotations;

namespace LibraryManagement.Models;

public class UserAccount
{
    public int Id { get; set; }

    [Required]
    [Display(Name = "Tên đăng nhập")]
    public string Username { get; set; } = string.Empty;

    [Required]
    [Display(Name = "Mật khẩu")]
    public string Password { get; set; } = string.Empty;

    [Display(Name = "Họ tên")]
    public string FullName { get; set; } = string.Empty;
}
