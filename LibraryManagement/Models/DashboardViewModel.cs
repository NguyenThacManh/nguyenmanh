namespace LibraryManagement.Models;

public class DashboardViewModel
{
    public int TotalBooks { get; set; }
    public int TotalCategories { get; set; }
    public int TotalReaders { get; set; }
    public int BorrowingCount { get; set; }
    public int OverdueCount { get; set; }
}
