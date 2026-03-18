using LibraryManagement.Data;
using LibraryManagement.Services;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllersWithViews();
builder.Services.AddDbContext<LibraryDbContext>(options =>
    options.UseSqlite(builder.Configuration.GetConnectionString("DefaultConnection")));
builder.Services.AddScoped<ILibraryService, EntityFrameworkLibraryService>();
builder.Services.AddSession();

var app = builder.Build();

using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<LibraryDbContext>();
    db.Database.EnsureCreated();
    DbSeeder.Seed(db);
}

if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseSession();
app.Use(async (context, next) =>
{
    var path = context.Request.Path.Value ?? string.Empty;
    var isLoginPath = path.StartsWith("/Account/Login", StringComparison.OrdinalIgnoreCase);
    var isRegisterPath = path.StartsWith("/Account/Register", StringComparison.OrdinalIgnoreCase);

    var isAllowedPath = isLoginPath || isRegisterPath;
    var isLoggedIn = context.Session.GetInt32("UserId").HasValue;

    if (!isLoggedIn && !isAllowedPath)
    {
        context.Response.Redirect("/Account/Login");
        return;
    }

    await next();
});
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
