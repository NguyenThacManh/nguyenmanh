using LibraryManagement.Models;
using LibraryManagement.Services;
using Microsoft.AspNetCore.Mvc;

namespace LibraryManagement.Controllers;

public class BooksController(ILibraryService libraryService, IWebHostEnvironment env) : Controller
{
    private const int PageSize = 6;

    [HttpGet]
    public IActionResult Index(string? searchTerm, int page = 1)
    {
        var validPage = page < 1 ? 1 : page;
        var matchedBooks = libraryService.Search(searchTerm).ToList();
        var totalItems = matchedBooks.Count;

        var pagedBooks = matchedBooks
            .Skip((validPage - 1) * PageSize)
            .Take(PageSize)
            .ToList();

        var viewModel = new BooksIndexViewModel
        {
            Books = pagedBooks,
            SearchTerm = searchTerm?.Trim() ?? string.Empty,
            PageNumber = validPage,
            PageSize = PageSize,
            TotalItems = totalItems
        };

        return View(viewModel);
    }

    [HttpGet]
    public IActionResult Details(int id)
    {
        var book = libraryService.GetById(id);
        if (book is null)
        {
            return NotFound();
        }

        return View(book);
    }

    [HttpGet]
    public IActionResult Create()
    {
        return View(new Book());
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public IActionResult Create(Book book, IFormFile? coverFile)
    {
        if (!ModelState.IsValid)
        {
            return View(book);
        }

        book.CoverImagePath = SaveCover(coverFile, book.CoverImagePath);
        book.QrCode = string.IsNullOrWhiteSpace(book.QrCode) ? $"BOOK-{book.Barcode}" : book.QrCode;
        libraryService.Add(book);

        TempData["Message"] = "Thêm sách thành công.";
        return RedirectToAction(nameof(Index));
    }

    [HttpGet]
    public IActionResult Edit(int id)
    {
        var book = libraryService.GetById(id);
        if (book is null)
        {
            return NotFound();
        }

        return View(book);
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public IActionResult Edit(int id, Book book, IFormFile? coverFile)
    {
        if (id != book.Id)
        {
            return BadRequest();
        }

        if (!ModelState.IsValid)
        {
            return View(book);
        }

        book.CoverImagePath = SaveCover(coverFile, book.CoverImagePath);
        book.QrCode = string.IsNullOrWhiteSpace(book.QrCode) ? $"BOOK-{book.Barcode}" : book.QrCode;
        libraryService.Update(book);
        TempData["Message"] = "Cập nhật sách thành công.";
        return RedirectToAction(nameof(Index));
    }

    [HttpGet]
    public IActionResult Delete(int id)
    {
        var book = libraryService.GetById(id);
        if (book is null)
        {
            return NotFound();
        }

        return View(book);
    }

    [HttpPost, ActionName("Delete")]
    [ValidateAntiForgeryToken]
    public IActionResult DeleteConfirmed(int id)
    {
        libraryService.Delete(id);
        TempData["Message"] = "Xóa sách thành công.";
        return RedirectToAction(nameof(Index));
    }

    private string SaveCover(IFormFile? file, string currentPath)
    {
        if (file is null || file.Length == 0)
        {
            return currentPath;
        }

        var uploads = Path.Combine(env.WebRootPath, "uploads");
        Directory.CreateDirectory(uploads);
        var fileName = $"{Guid.NewGuid()}_{Path.GetFileName(file.FileName)}";
        var fullPath = Path.Combine(uploads, fileName);

        using var stream = System.IO.File.Create(fullPath);
        file.CopyTo(stream);

        return $"/uploads/{fileName}";
    }
}
