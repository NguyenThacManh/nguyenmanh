# Cờ caro trên web (Visual Studio)

Dự án đã được chuyển sang dạng **ASP.NET Core Web App** để bạn có thể mở và build trực tiếp bằng **Visual Studio** qua file solution `CaroWeb.sln`.

## Mở và chạy bằng Visual Studio

1. Mở file `CaroWeb.sln` bằng Visual Studio 2022.
2. Chọn project startup là `CaroWeb`.
3. Nhấn **F5** (hoặc **Ctrl+F5**) để chạy.
4. Trình duyệt sẽ mở game cờ caro tại `http://localhost:5200`.

## Cấu trúc chính

- `CaroWeb.sln`: solution cho Visual Studio.
- `CaroWeb/CaroWeb.csproj`: project web .NET 8.
- `CaroWeb/Program.cs`: cấu hình middleware phục vụ static files.
- `CaroWeb/wwwroot/index.html`: entrypoint giao diện game.
- `CaroWeb/wwwroot/css/styles.css`: giao diện và responsive layout.
- `CaroWeb/wwwroot/js/main.js`: logic trò chơi cờ caro.

## Build từ command line (tùy chọn)

Nếu máy đã cài .NET SDK 8:

```bash
dotnet build CaroWeb.sln
dotnet run --project CaroWeb/CaroWeb.csproj
```

## Tính năng game

- Bàn cờ 15x15.
- Chế độ 2 người chơi hoặc đấu với máy.
- Tự động kiểm tra thắng/thua khi đủ 5 quân liên tiếp.
- Hoàn tác nước đi.
- Bảng điểm X / O / Hòa.
