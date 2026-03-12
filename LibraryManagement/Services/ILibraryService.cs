using LibraryManagement.Models;

namespace LibraryManagement.Services;

public interface ILibraryService
{
    IEnumerable<Book> GetAll();
    IEnumerable<Book> Search(string? searchTerm);
    Book? GetById(int id);
    void Add(Book book);
    void Update(Book book);
    bool Delete(int id);
}
