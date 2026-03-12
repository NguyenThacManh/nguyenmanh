using LibraryManagement.Data;
using LibraryManagement.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace LibraryManagement.Controllers;

public class ReportsController(LibraryDbContext db) : Controller
{
    public IActionResult Index()
    {
        var now = DateTime.Today;
        var all = db.BorrowTransactions.Include(x => x.Book).Include(x => x.Reader).ToList();

        var vm = new ReportViewModel
        {
            TotalBorrows = all.Count,
            ReturnedBorrows = all.Count(x => x.ReturnDate != null),
            BorrowingBorrows = all.Count(x => x.ReturnDate == null),
            OverdueBorrows = all.Count(x => x.ReturnDate == null && x.DueDate < now),
            RecentTransactions = all.OrderByDescending(x => x.BorrowDate).Take(10)
        };

        return View(vm);
    }
}
