using LibraryManagement.Data;
using LibraryManagement.Models;
using Microsoft.EntityFrameworkCore;

namespace LibraryManagement.Services;

public class EntityFrameworkLibraryService(LibraryDbContext db) : ILibraryService
{
    public IEnumerable<Book> GetAll() => db.Books.AsNoTracking().OrderBy(x => x.Title).ToList();

    public IEnumerable<Book> Search(string? searchTerm)
    {
        var normalized = searchTerm?.Trim();
        IQueryable<Book> query = db.Books.AsNoTracking();

        if (!string.IsNullOrWhiteSpace(normalized))
        {
            query = query.Where(x =>
                EF.Functions.Like(x.Title, $"%{normalized}%") ||
                EF.Functions.Like(x.Author, $"%{normalized}%"));
        }

        return query.OrderBy(x => x.Title).ToList();
    }

    public Book? GetById(int id) => db.Books.AsNoTracking().FirstOrDefault(x => x.Id == id);

    public void Add(Book book)
    {
        db.Books.Add(book);
        db.SaveChanges();
    }

    public void Update(Book book)
    {
        var existing = db.Books.FirstOrDefault(x => x.Id == book.Id);
        if (existing is null)
        {
            return;
        }

        existing.Title = book.Title;
        existing.Author = book.Author;
        existing.Category = book.Category;
        existing.Quantity = book.Quantity;
        existing.ShelfLocation = book.ShelfLocation;
        existing.PublishYear = book.PublishYear;
        existing.Barcode = book.Barcode;
        existing.QrCode = book.QrCode;
        existing.CoverImagePath = book.CoverImagePath;

        db.SaveChanges();
    }

    public bool Delete(int id)
    {
        var book = db.Books.FirstOrDefault(x => x.Id == id);
        if (book is null)
        {
            return false;
        }

        db.Books.Remove(book);
        db.SaveChanges();
        return true;
    }
}
