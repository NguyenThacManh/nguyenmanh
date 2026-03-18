using LibraryManagement.Models;

namespace LibraryManagement.Data;

public static class DbSeeder
{
    public static void Seed(LibraryDbContext db)
    {
        if (!db.Categories.Any())
        {
            db.Categories.AddRange(
                new Category { Name = "Lập trình", Description = "Sách công nghệ và phát triển phần mềm" },
                new Category { Name = "Văn học", Description = "Tác phẩm văn học trong và ngoài nước" },
                new Category { Name = "Kỹ năng", Description = "Kỹ năng sống và làm việc" }
            );
        }

        if (!db.Books.Any())
        {
            db.Books.AddRange(
                new Book { Title = "Clean Code", Author = "Robert C. Martin", Category = "Lập trình", Quantity = 0, ShelfLocation = "A1", PublishYear = 2008, Barcode = "B001", QrCode = "BOOK-B001" },
                new Book { Title = "Dế Mèn Phiêu Lưu Ký", Author = "Tô Hoài", Category = "Văn học", Quantity = 3, ShelfLocation = "B2", PublishYear = 1941, Barcode = "B002", QrCode = "BOOK-B002" },
                new Book { Title = "Đắc Nhân Tâm", Author = "Dale Carnegie", Category = "Kỹ năng", Quantity = 10, ShelfLocation = "C1", PublishYear = 1936, Barcode = "B003", QrCode = "BOOK-B003" }
            );
        }

        if (!db.Readers.Any())
        {
            db.Readers.AddRange(
                new Reader { FullName = "Nguyễn Minh Anh", Email = "anh.nguyen@example.com", PhoneNumber = "0901000001", Address = "Hà Nội" },
                new Reader { FullName = "Trần Quang Huy", Email = "huy.tran@example.com", PhoneNumber = "0901000002", Address = "Đà Nẵng" }
            );
        }

        if (!db.UserAccounts.Any())
        {
            db.UserAccounts.Add(new UserAccount
            {
                Username = "user1",
                Password = "123456",
                FullName = "Người dùng mẫu"
            });
        }

        db.SaveChanges();

        if (!db.BorrowTransactions.Any())
        {
            var firstBook = db.Books.First();
            var firstReader = db.Readers.First();

            db.BorrowTransactions.Add(new BorrowTransaction
            {
                BookId = firstBook.Id,
                ReaderId = firstReader.Id,
                BorrowDate = DateTime.Today.AddDays(-10),
                DueDate = DateTime.Today.AddDays(-1)
            });
        }

        if (!db.Reservations.Any())
        {
            var firstBook = db.Books.First();
            var secondReader = db.Readers.OrderBy(x => x.Id).Skip(1).FirstOrDefault() ?? db.Readers.First();
            db.Reservations.Add(new Reservation
            {
                BookId = firstBook.Id,
                ReaderId = secondReader.Id,
                ReservedDate = DateTime.Today.AddDays(-2),
                NotifiedAvailable = false
            });
        }

        db.SaveChanges();
    }
}
