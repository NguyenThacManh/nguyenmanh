namespace LibraryManagement.Models;

public class UserDashboardViewModel
{
    public IEnumerable<BorrowTransaction> BorrowHistory { get; set; } = [];
    public IEnumerable<Reservation> Reservations { get; set; } = [];
    public IEnumerable<NotificationItem> Notifications { get; set; } = [];
}
