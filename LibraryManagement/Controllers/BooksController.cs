using LibraryManagement.Models;
using LibraryManagement.Services;
using Microsoft.AspNetCore.Mvc;

namespace LibraryManagement.Controllers;

public class BooksController(ILibraryService libraryService, IWebHostEnvironment env) : Controller
{
    private const int DefaultPageSize = 6;

    [HttpGet]
    public IActionResult Index(string? searchTerm, string? category, string sortBy = "title_asc", int page = 1, int pageSize = DefaultPageSize)
    {
        var validPage = page < 1 ? 1 : page;
        var validPageSize = pageSize is 6 or 12 or 24 ? pageSize : DefaultPageSize;
        var normalizedSearch = searchTerm?.Trim() ?? string.Empty;
        var normalizedCategory = category?.Trim() ?? string.Empty;

        var allBooks = libraryService.Search(normalizedSearch).ToList();
        var categories = allBooks
            .Select(x => x.Category)
            .Where(x => !string.IsNullOrWhiteSpace(x))
            .Distinct(StringComparer.OrdinalIgnoreCase)
            .OrderBy(x => x)
            .ToList();

        var filteredBooks = allBooks.AsEnumerable();

        if (!string.IsNullOrWhiteSpace(normalizedCategory))
        {
            filteredBooks = filteredBooks.Where(x => x.Category.Equals(normalizedCategory, StringComparison.OrdinalIgnoreCase));
        }

        filteredBooks = sortBy switch
        {
            "title_desc" => filteredBooks.OrderByDescending(x => x.Title),
            "author_asc" => filteredBooks.OrderBy(x => x.Author),
            "author_desc" => filteredBooks.OrderByDescending(x => x.Author),
            "year_desc" => filteredBooks.OrderByDescending(x => x.PublishYear),
            "year_asc" => filteredBooks.OrderBy(x => x.PublishYear),
            _ => filteredBooks.OrderBy(x => x.Title)
        };

        var totalItems = filteredBooks.Count();
        var totalPages = Math.Max(1, (int)Math.Ceiling(totalItems / (double)validPageSize));
        if (validPage > totalPages)
        {
            validPage = totalPages;
        }

        var pagedBooks = filteredBooks
            .Skip((validPage - 1) * validPageSize)
            .Take(validPageSize)
            .ToList();

        var viewModel = new BooksIndexViewModel
        {
            Books = pagedBooks,
            Categories = categories,
            SearchTerm = normalizedSearch,
            Category = normalizedCategory,
            SortBy = sortBy,
            PageNumber = validPage,
            PageSize = validPageSize,
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
