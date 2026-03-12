using LibraryManagement.Data;
using LibraryManagement.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace LibraryManagement.Controllers;

public class StatisticsController(LibraryDbContext db) : Controller
{
    public IActionResult Index()
    {
        var now = DateTime.Today;

        var mostBorrowedBooks = db.BorrowTransactions
            .Where(x => x.Book != null)
            .GroupBy(x => x.Book!.Title)
            .Select(g => new { Title = g.Key, Count = g.Count() })
            .OrderByDescending(x => x.Count)
            .Take(10)
            .ToList()
            .Select(x => (x.Title, x.Count))
            .ToList();

        var mostActiveReaders = db.BorrowTransactions
            .Where(x => x.Reader != null)
            .GroupBy(x => x.Reader!.FullName)
            .Select(g => new { ReaderName = g.Key, Count = g.Count() })
            .OrderByDescending(x => x.Count)
            .Take(10)
            .ToList()
            .Select(x => (x.ReaderName, x.Count))
            .ToList();

        var overdue = db.BorrowTransactions
            .Include(x => x.Book)
            .Include(x => x.Reader)
            .Where(x => x.ReturnDate == null && x.DueDate < now)
            .ToList();

        var vm = new StatisticsViewModel
        {
            MostBorrowedBooks = mostBorrowedBooks,
            MostActiveReaders = mostActiveReaders,
            OverdueBooks = overdue
        };

        return View(vm);
    }
}
