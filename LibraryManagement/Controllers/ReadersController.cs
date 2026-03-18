using LibraryManagement.Data;
using LibraryManagement.Models;
using Microsoft.AspNetCore.Mvc;

namespace LibraryManagement.Controllers;

public class ReadersController(LibraryDbContext db) : Controller
{
    public IActionResult Index() => View(db.Readers.OrderBy(x => x.FullName).ToList());

    public IActionResult Create() => View(new Reader());

    [HttpPost]
    [ValidateAntiForgeryToken]
    public IActionResult Create(Reader reader)
    {
        if (!ModelState.IsValid) return View(reader);
        db.Readers.Add(reader);
        db.SaveChanges();
        TempData["Message"] = "Thêm người đọc thành công.";
        return RedirectToAction(nameof(Index));
    }

    public IActionResult Edit(int id)
    {
        var reader = db.Readers.FirstOrDefault(x => x.Id == id);
        return reader is null ? NotFound() : View(reader);
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public IActionResult Edit(int id, Reader reader)
    {
        if (id != reader.Id) return BadRequest();
        if (!ModelState.IsValid) return View(reader);

        var existing = db.Readers.FirstOrDefault(x => x.Id == id);
        if (existing is null) return NotFound();

        existing.FullName = reader.FullName;
        existing.Email = reader.Email;
        existing.PhoneNumber = reader.PhoneNumber;
        existing.Address = reader.Address;
        db.SaveChanges();
        TempData["Message"] = "Cập nhật người đọc thành công.";
        return RedirectToAction(nameof(Index));
    }
}
