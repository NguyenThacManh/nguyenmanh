using LibraryManagement.Data;
using LibraryManagement.Models;
using Microsoft.AspNetCore.Mvc;

namespace LibraryManagement.Controllers;

public class CategoriesController(LibraryDbContext db) : Controller
{
    public IActionResult Index() => View(db.Categories.OrderBy(x => x.Name).ToList());

    public IActionResult Create() => View(new Category());

    [HttpPost]
    [ValidateAntiForgeryToken]
    public IActionResult Create(Category category)
    {
        if (!ModelState.IsValid) return View(category);
        db.Categories.Add(category);
        db.SaveChanges();
        TempData["Message"] = "Thêm thể loại thành công.";
        return RedirectToAction(nameof(Index));
    }

    public IActionResult Edit(int id)
    {
        var category = db.Categories.FirstOrDefault(x => x.Id == id);
        return category is null ? NotFound() : View(category);
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public IActionResult Edit(int id, Category category)
    {
        if (id != category.Id) return BadRequest();
        if (!ModelState.IsValid) return View(category);

        var existing = db.Categories.FirstOrDefault(x => x.Id == id);
        if (existing is null) return NotFound();

        existing.Name = category.Name;
        existing.Description = category.Description;
        db.SaveChanges();
        TempData["Message"] = "Cập nhật thể loại thành công.";
        return RedirectToAction(nameof(Index));
    }
}
