namespace LibraryManagement.Models;

public class BooksIndexViewModel
{
    public IEnumerable<Book> Books { get; set; } = [];
    public string SearchTerm { get; set; } = string.Empty;
    public int PageNumber { get; set; }
    public int PageSize { get; set; }
    public int TotalItems { get; set; }

    public int TotalPages => TotalItems <= 0
        ? 1
        : (int)Math.Ceiling(TotalItems / (double)PageSize);
}
