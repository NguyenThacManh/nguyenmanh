using LibraryManagement.Models;

namespace LibraryManagement.Services;

public class InMemoryLibraryService : ILibraryService
{
    private readonly List<Book> _books =
    [
        new Book
        {
            Id = 1,
            Title = "Clean Code",
            Author = "Robert C. Martin",
            Category = "Lập trình",
            Quantity = 5,
            ShelfLocation = "A1",
            PublishYear = 2008
        },
        new Book
        {
            Id = 2,
            Title = "Dế Mèn Phiêu Lưu Ký",
            Author = "Tô Hoài",
            Category = "Văn học",
            Quantity = 8,
            ShelfLocation = "B2",
            PublishYear = 1941
        }
    ];

    private int _nextId = 3;

    public IEnumerable<Book> GetAll() => _books.OrderBy(x => x.Title);

    public IEnumerable<Book> Search(string? searchTerm)
    {
        var normalized = searchTerm?.Trim();
        var query = _books.AsEnumerable();

        if (!string.IsNullOrWhiteSpace(normalized))
        {
            query = query.Where(x =>
                x.Title.Contains(normalized, StringComparison.OrdinalIgnoreCase) ||
                x.Author.Contains(normalized, StringComparison.OrdinalIgnoreCase));
        }

        return query.OrderBy(x => x.Title);
    }

    public Book? GetById(int id) => _books.FirstOrDefault(x => x.Id == id);

    public void Add(Book book)
    {
        book.Id = _nextId++;
        _books.Add(book);
    }

    public void Update(Book book)
    {
        var existing = GetById(book.Id);
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
    }

    public bool Delete(int id)
    {
        var book = GetById(id);
        if (book is null)
        {
            return false;
        }

        _books.Remove(book);
        return true;
    }
}
