using LibraryManagement.Data;
using LibraryManagement.Models;
using Microsoft.AspNetCore.Mvc;

namespace LibraryManagement.Controllers;

public class AccountController(LibraryDbContext db) : Controller
{
    [HttpGet]
    public IActionResult Register() => View(new UserAccount());

    [HttpPost]
    [ValidateAntiForgeryToken]
    public IActionResult Register(UserAccount model)
    {
        if (!ModelState.IsValid) return View(model);
        if (db.UserAccounts.Any(x => x.Username == model.Username))
        {
            ModelState.AddModelError(nameof(UserAccount.Username), "Tên đăng nhập đã tồn tại.");
            return View(model);
        }

        db.UserAccounts.Add(model);
        db.SaveChanges();
        TempData["Message"] = "Đăng ký thành công. Vui lòng đăng nhập.";
        return RedirectToAction(nameof(Login));
    }

    [HttpGet]
    public IActionResult Login() => View();

    [HttpPost]
    [ValidateAntiForgeryToken]
    public IActionResult Login(string username, string password)
    {
        var user = db.UserAccounts.FirstOrDefault(x => x.Username == username && x.Password == password);
        if (user is null)
        {
            ViewBag.Error = "Sai tài khoản hoặc mật khẩu.";
            return View();
        }

        HttpContext.Session.SetInt32("UserId", user.Id);
        HttpContext.Session.SetString("Username", user.Username);
        TempData["Message"] = $"Xin chào {user.FullName}!";
        return RedirectToAction("Index", "UserFeatures");
    }

    public IActionResult Logout()
    {
        HttpContext.Session.Clear();
        TempData["Message"] = "Đã đăng xuất.";
        return RedirectToAction(nameof(Login));
    }
}
