namespace LibraryManagement.Models;

public class BooksIndexViewModel
{
    public IEnumerable<Book> Books { get; set; } = [];
    public IEnumerable<string> Categories { get; set; } = [];

    public string SearchTerm { get; set; } = string.Empty;
    public string Category { get; set; } = string.Empty;
    public string SortBy { get; set; } = "title_asc";

    public int PageNumber { get; set; }
    public int PageSize { get; set; } = 6;
    public int TotalItems { get; set; }

    public int StartItem => TotalItems == 0 ? 0 : ((PageNumber - 1) * PageSize) + 1;
    public int EndItem => Math.Min(PageNumber * PageSize, TotalItems);

    public int TotalPages => TotalItems <= 0
        ? 1
        : (int)Math.Ceiling(TotalItems / (double)PageSize);
}
