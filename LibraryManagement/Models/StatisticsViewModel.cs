namespace LibraryManagement.Models;

public class StatisticsViewModel
{
    public IEnumerable<(string BookTitle, int BorrowCount)> MostBorrowedBooks { get; set; } = [];
    public IEnumerable<(string ReaderName, int BorrowCount)> MostActiveReaders { get; set; } = [];
    public IEnumerable<BorrowTransaction> OverdueBooks { get; set; } = [];
}
