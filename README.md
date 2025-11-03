# Nhom8BaiTapLon
# Ảnh : https://drive.google.com/file/u/1/d/16PWV1qX9IrLeFNUhoA2iNiqVSi3BhOfq/view?usp=drive_open

# Link video demo : 

## 1. Thành viên nhóm & phân công công việc

      *Đinh Huy Hoàng - 24020138
          - Lập trình các lớp Ball, Bricks, Game.
          - Xử lí va chạm bóng với paddle và gạch.
          - Xử lí logic thắng thua game.
          - Xử lí sự kiện bàn phím.
          - Xử lí các hình ảnh và âm thanh xuất hiện trong 1 màn chơi.
          - Thiết kế cốt truyện cho game.
      *Đỗ Đức Kiệt - 24020192
          - Tạo các lớp Menu (Menu chính, MapMenu) hiển thị bản đồ.
          - Balo gồm balls và items.
            + Ball quản lý balls.
            + Item quản lý items.
          - Thiết kế powerUp trong game và xử lý va chạm PowerUp.
          - Thiết kế hình ảnh của brick và ball.
      *Phạm Gia Khánh - 24020174
          - Lập trình lớp ball, xử lí va chạm với boss.
          - Lập trình lớp Bricks, tạo ra maptxt và vẽ từ mảng 2D đó.
          - Thêm tính năng va chạm giữa paddle và skillboss.
          - Lập trình lớp Boss và các skillboss, xử lí riêng hình ảnh của các skill.
          - Thêm xử lí logic game khi màn chơi có boss.
          - Thiết kế hình ảnh của boss và skillboss.
          - Thêm tính năng levels ở các màn chơi.
      *Viên Khương Duy - 24020111
          - Tạo GachaMachine(vòng quay gacha vật phẩm).
          - Tạo hiệu ứng rung màn hình(GraphicsEffect).
          - Tạo lớp Coin, các hàm artifact.
          - Thiết kế hình ảnh hiển thị của các artifact.
          - Tạo balo chứa đồ quay được.

## 2. Giới thiệu

ARKANOID là một tựa game mini được phát triển bằng Java Swing và awt, lấy cảm hứng từ trò chơi cổ điển Arkanoid nhưng được mở rộng với:
- Hệ thống vòng quay Gacha để nhận vật phẩm hiếm (Artifacts).
- Các vật phẩm có thể trang bị để tăng sức mạnh hoặc kích hoạt hiệu ứng đặc biệt khi chơi.  
- Hiệu ứng âm thanh, ánh sáng và hệ thống xu để tăng tính hấp dẫn.
- Hệ thống gạch và bóng nguyên tố.
- Những con boss xuất hiện ở cuối màn chơi tăng độ khó, khiến trải nghiệm trở nên thú vị.  

## 3. Cách chơi

### Trong Game Arkanoid:
- Di chuyển thanh đỡ bằng phím Trái / Phải.  
- Phá các viên gạch để ghi điểm và thu thập Coin.
- Tạm dừng bằng phím P, bắn bóng bằng nút space, sử dụng artifact bằng phím E.  
- Một số viên gạch đặc biệt rơi ra vật phẩm hỗ trợ.
- Người chơi có thể đầu hàng nếu màn chơi quá khó.
- Người chơi có thể trang bị bóng và artifact trước khi vào trận

### Trong Gacha Machine:
- Sử dụng Coin để quay vòng quay vật phẩm.  
- Mỗi lượt quay tiêu tốn 1000 Coin.  
- Các vật phẩm nhận được sẽ lưu trong Túi đồ (Inventory).  
- Vật phẩm có thể trang bị trong phần Artifact để kích hoạt hiệu ứng trong Arkanoid.
