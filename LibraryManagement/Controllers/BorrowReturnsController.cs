using LibraryManagement.Data;
using LibraryManagement.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;

namespace LibraryManagement.Controllers;

public class BorrowReturnsController(LibraryDbContext db) : Controller
{
    public IActionResult Index()
    {
        var data = db.BorrowTransactions
            .Include(x => x.Book)
            .Include(x => x.Reader)
            .OrderByDescending(x => x.BorrowDate)
            .ToList();
        return View(data);
    }

    public IActionResult Borrow()
    {
        LoadSelections();
        return View(new BorrowTransaction { BorrowDate = DateTime.Today, DueDate = DateTime.Today.AddDays(7) });
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public IActionResult Borrow(BorrowTransaction model, string? barcode)
    {
        if (!string.IsNullOrWhiteSpace(barcode))
        {
            var byBarcode = db.Books.FirstOrDefault(x => x.Barcode == barcode.Trim());
            if (byBarcode is not null)
            {
                model.BookId = byBarcode.Id;
            }
        }

        var selectedBook = db.Books.FirstOrDefault(x => x.Id == model.BookId);
        if (selectedBook is null || selectedBook.Quantity <= 0)
        {
            ModelState.AddModelError(nameof(BorrowTransaction.BookId), "Sách không khả dụng để mượn.");
        }

        if (!ModelState.IsValid)
        {
            LoadSelections();
            return View(model);
        }

        model.ReturnDate = null;
        db.BorrowTransactions.Add(model);
        selectedBook!.Quantity -= 1;
        db.SaveChanges();
        TempData["Message"] = "Tạo phiếu mượn thành công.";
        return RedirectToAction(nameof(Index));
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public IActionResult Return(int id)
    {
        var trx = db.BorrowTransactions.Include(x => x.Book).FirstOrDefault(x => x.Id == id);
        if (trx is null) return NotFound();
        if (trx.ReturnDate is null)
        {
            trx.ReturnDate = DateTime.Today;
            if (trx.Book is not null)
            {
                trx.Book.Quantity += 1;
            }
        }

        db.SaveChanges();
        TempData["Message"] = "Đã trả sách.";
        return RedirectToAction(nameof(Index));
    }

    private void LoadSelections()
    {
        ViewBag.BookId = new SelectList(db.Books.OrderBy(x => x.Title).ToList(), "Id", "Title");
        ViewBag.ReaderId = new SelectList(db.Readers.OrderBy(x => x.FullName).ToList(), "Id", "FullName");
    }
}
