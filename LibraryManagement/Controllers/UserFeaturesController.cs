using LibraryManagement.Data;
using LibraryManagement.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;

namespace LibraryManagement.Controllers;

public class UserFeaturesController(LibraryDbContext db) : Controller
{
    public IActionResult Index(int? readerId)
    {
        var rid = readerId ?? db.Readers.Select(x => (int?)x.Id).FirstOrDefault();
        if (rid is null)
        {
            return View(new UserDashboardViewModel());
        }

        ViewBag.ReaderId = new SelectList(db.Readers.OrderBy(x => x.FullName), "Id", "FullName", rid.Value);
        var notifications = BuildNotifications(rid.Value);

        var vm = new UserDashboardViewModel
        {
            BorrowHistory = db.BorrowTransactions.Include(x => x.Book).Where(x => x.ReaderId == rid.Value).OrderByDescending(x => x.BorrowDate).ToList(),
            Reservations = db.Reservations.Include(x => x.Book).Where(x => x.ReaderId == rid.Value).OrderByDescending(x => x.ReservedDate).ToList(),
            Notifications = notifications
        };

        return View(vm);
    }

    [HttpGet]
    public IActionResult Reserve()
    {
        ViewBag.BookId = new SelectList(db.Books.OrderBy(x => x.Title), "Id", "Title");
        ViewBag.ReaderId = new SelectList(db.Readers.OrderBy(x => x.FullName), "Id", "FullName");
        return View(new Reservation { ReservedDate = DateTime.Today });
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public IActionResult Reserve(Reservation reservation)
    {
        reservation.ReservedDate = DateTime.Today;
        reservation.NotifiedAvailable = false;
        db.Reservations.Add(reservation);
        db.SaveChanges();
        TempData["Message"] = "Đặt trước sách thành công.";
        return RedirectToAction(nameof(Index), new { readerId = reservation.ReaderId });
    }

    private List<NotificationItem> BuildNotifications(int readerId)
    {
        var now = DateTime.Today;
        var dueSoon = db.BorrowTransactions.Include(x => x.Book)
            .Where(x => x.ReaderId == readerId && x.ReturnDate == null && x.DueDate >= now && x.DueDate <= now.AddDays(2))
            .ToList();

        var reservedAvailable = db.Reservations.Include(x => x.Book)
            .Where(x => x.ReaderId == readerId && !x.NotifiedAvailable && x.Book != null && x.Book.Quantity > 0)
            .ToList();

        var list = new List<NotificationItem>();
        list.AddRange(dueSoon.Select(x => new NotificationItem
        {
            Type = "DueSoon",
            Message = $"Sách '{x.Book?.Title}' sắp đến hạn trả ({x.DueDate:dd/MM/yyyy})."
        }));

        list.AddRange(reservedAvailable.Select(x => new NotificationItem
        {
            Type = "ReservationAvailable",
            Message = $"Sách đặt trước '{x.Book?.Title}' đã có sẵn."
        }));

        return list;
    }
}
