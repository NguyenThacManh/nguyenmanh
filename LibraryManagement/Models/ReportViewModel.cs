namespace LibraryManagement.Models;

public class ReportViewModel
{
    public int TotalBorrows { get; set; }
    public int ReturnedBorrows { get; set; }
    public int BorrowingBorrows { get; set; }
    public int OverdueBorrows { get; set; }
    public IEnumerable<BorrowTransaction> RecentTransactions { get; set; } = [];
}
