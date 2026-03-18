using LibraryManagement.Data;
using LibraryManagement.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace LibraryManagement.Controllers;

public class DashboardController(LibraryDbContext db) : Controller
{
    public IActionResult Index()
    {
        var now = DateTime.Today;
        var vm = new DashboardViewModel
        {
            TotalBooks = db.Books.Count(),
            TotalCategories = db.Categories.Count(),
            TotalReaders = db.Readers.Count(),
            BorrowingCount = db.BorrowTransactions.Count(x => x.ReturnDate == null),
            OverdueCount = db.BorrowTransactions.Count(x => x.ReturnDate == null && x.DueDate < now)
        };

        return View(vm);
    }
}
