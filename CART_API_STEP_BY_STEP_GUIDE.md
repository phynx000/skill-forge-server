# HƯỚNG DẪN KHẮC PHỤC API GIỎ HÀNG - TỪNG BƯỚC CỤ THỂ

## 🚨 KHẮC PHỤC NGAY LẬP TỨC - PRIORITY 1

### Bước 1: Sửa lỗi đệ quy trong CartService.updateCart()

**Vấn đề hiện tại:**
```java
// NGUY HIỂM - CÓ THỂ GÂY STACKOVERFLOW
else {
   Cart newCart = new Cart();
   newCart.setUser(userService.getCurrentUser());
   cardRepository.save(newCart);
   updateCart(itemId, quantity); // ← ĐỆ QUY VÔ HẠN!
}
```

**Giải pháp:**
Thay thế đoạn code trên bằng:
```java
else {
    // Tạo cart mới và xử lý logic ngay tại đây, không gọi đệ quy
    Cart newCart = new Cart();
    newCart.setUser(user);
    newCart.setTotalItems(0);
    newCart.setTotalPrice(BigDecimal.ZERO);
    newCart.setItems(new ArrayList<>());
    Cart savedCart = cardRepository.save(newCart);
    
    // Tìm CartItem theo ID để update
    CartItem existingItem = cartItemRepository.findById(itemId)
        .orElseThrow(() -> new RuntimeException("Item không tồn tại"));
    
    existingItem.setQuantity(quantity);
    existingItem.setCart(savedCart);
    cartItemRepository.save(existingItem);
    
    cartItemDTO = cartMapper.toCardItemDTO(existingItem);
    cartItemDTO.setQuantity(quantity);
}
```

### Bước 2: Tối ưu logic lặp lại trong CartService

**Vấn đề hiện tại:**
```java
// Stream filter được gọi 2 lần cho cùng mục đích
cartMapper.toCardItemDTO(existingCart.getItems().stream()
    .filter(item -> item.getId().equals(itemId))
    .findFirst()...); // Lần 1

cartItem.setCourse(existingCart.getItems().stream()
    .filter(item -> item.getId().equals(itemId))
    .findFirst()...); // Lần 2 - LÃNG PHÍ!
```

**Giải pháp:**
```java
// Chỉ filter 1 lần, lưu kết quả
CartItem existingItem = existingCart.getItems().stream()
    .filter(item -> item.getId().equals(itemId))
    .findFirst()
    .orElseThrow(() -> new RuntimeException("Item not found in cart"));

// Sử dụng existingItem cho tất cả logic tiếp theo
cartItemDTO = cartMapper.toCardItemDTO(existingItem);
cartItemDTO.setQuantity(quantity);

// Cập nhật trực tiếp existingItem thay vì tạo mới
existingItem.setQuantity(quantity);
cartItemRepository.save(existingItem);
```

## 🛠️ THÊM CÁC API CẦN THIẾT - PRIORITY 2

### Bước 3: Thêm API GET Cart

**Thêm vào CartController:**
```java
@GetMapping("/carts")
@ApiMessage(value = "Get user's cart")
public ResponseEntity<CartDTO> getCart() {
    CartDTO cart = cartService.getCart();
    return ResponseEntity.ok(cart);
}
```

**Thêm vào CartService:**
```java
@Transactional(readOnly = true)
public CartDTO getCart() {
    User user = userService.getCurrentUser();
    Optional<Cart> cartOpt = cardRepository.findByUserId(user.getId());
    
    if (cartOpt.isPresent()) {
        return cartMapper.toCartDTO(cartOpt.get());
    } else {
        // Trả về cart rỗng thay vì null
        CartDTO emptyCart = new CartDTO();
        emptyCart.setId(null);
        emptyCart.setTotalItems(0);
        emptyCart.setTotalPrice(BigDecimal.ZERO);
        emptyCart.setItems(new ArrayList<>());
        return emptyCart;
    }
}
```

### Bước 4: Thêm API POST Add to Cart

**Tạo AddToCartRequest DTO:**
```java
// File: dto/request/AddToCartRequest.java
package com.skillforge.skillforge_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {
    @NotNull(message = "Course ID không được để trống")
    private Long courseId;
    
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private int quantity = 1;
}
```

**Thêm vào CartController:**
```java
@PostMapping("/carts/items")
@ApiMessage(value = "Add item to cart")
public ResponseEntity<CartDTO> addToCart(@Valid @RequestBody AddToCartRequest request) {
    CartDTO cart = cartService.addToCart(request.getCourseId(), request.getQuantity());
    return ResponseEntity.ok(cart);
}
```

**Thêm vào CartService:**
```java
@Transactional
public CartDTO addToCart(Long courseId, int quantity) {
    User user = userService.getCurrentUser();
    
    // Lấy hoặc tạo cart (chỉ 1 lần duy nhất)
    Cart cart = getOrCreateCart(user);
    
    // Kiểm tra course có tồn tại
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));
    
    // Kiểm tra item đã có trong cart chưa
    Optional<CartItem> existingItem = cart.getItems().stream()
        .filter(item -> item.getCourse().getId().equals(courseId))
        .findFirst();
    
    if (existingItem.isPresent()) {
        // Cộng dồn số lượng
        CartItem item = existingItem.get();
        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);
    } else {
        // Tạo mới
        CartItem newItem = new CartItem();
        newItem.setCourse(course);
        newItem.setQuantity(quantity);
        newItem.setPrice(course.getPrice());
        newItem.setCart(cart);
        cartItemRepository.save(newItem);
    }
    
    // Cập nhật tổng của cart
    updateCartTotals(cart);
    return cartMapper.toCartDTO(cart);
}

// Helper method
private Cart getOrCreateCart(User user) {
    return cardRepository.findByUserId(user.getId())
        .orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setTotalItems(0);
            newCart.setTotalPrice(BigDecimal.ZERO);
            newCart.setItems(new ArrayList<>());
            return cardRepository.save(newCart);
        });
}
```

### Bước 5: Thêm API DELETE Remove Item

**Thêm vào CartController:**
```java
@DeleteMapping("/carts/items/{courseId}")
@ApiMessage(value = "Remove item from cart")
public ResponseEntity<CartDTO> removeFromCart(@PathVariable Long courseId) {
    CartDTO cart = cartService.removeFromCart(courseId);
    return ResponseEntity.ok(cart);
}
```

**Thêm vào CartService:**
```java
@Transactional
public CartDTO removeFromCart(Long courseId) {
    User user = userService.getCurrentUser();
    Cart cart = cardRepository.findByUserId(user.getId())
        .orElseThrow(() -> new RuntimeException("Không có giỏ hàng"));
    
    CartItem itemToRemove = cart.getItems().stream()
        .filter(item -> item.getCourse().getId().equals(courseId))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong giỏ hàng"));
    
    cartItemRepository.delete(itemToRemove);
    updateCartTotals(cart);
    return cartMapper.toCartDTO(cart);
}
```

## 🔧 SỬA MAPPER VÀ DTO - PRIORITY 3

### Bước 6: Hoàn thiện CartMapper

**Thêm method toCartDTO:**
```java
public CartDTO toCartDTO(Cart cart) {
    CartDTO cartDTO = new CartDTO();
    cartDTO.setId(cart.getId());
    cartDTO.setTotalItems(cart.getTotalItems());
    cartDTO.setTotalPrice(cart.getTotalPrice());
    
    List<CartDTO.CardItemDTO> itemDTOs = cart.getItems().stream()
        .map(this::toCardItemDTO)
        .collect(Collectors.toList());
    cartDTO.setItems(itemDTOs);
    
    return cartDTO;
}

// Sửa method toCardItemDTO để set quantity
public CartDTO.CardItemDTO toCardItemDTO(CartItem cartItem) {
    CartDTO.CardItemDTO cardItemDTO = new CartDTO.CardItemDTO();
    cardItemDTO.setId(cartItem.getId());
    cardItemDTO.setCourseName(cartItem.getCourse().getTitle());
    cardItemDTO.setCourseId(cartItem.getCourse().getId());
    cardItemDTO.setImageUrl(cartItem.getCourse().getThumbnailUrl());
    cardItemDTO.setPrice(cartItem.getCourse().getPrice());
    cardItemDTO.setQuantity(cartItem.getQuantity()); // ← THIẾU DÒNG NÀY!
    return cardItemDTO;
}
```

### Bước 7: Thêm constructor cho CartDTO

**Sửa CartDTO:**
```java
@Getter
@Setter
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private int totalItems;
    private BigDecimal totalPrice;
    private List<CardItemDTO> items;

    // Thêm constructor mặc định
    public CartDTO() {
    }
    
    // Inner class giữ nguyên
}
```

## 📊 HELPER METHODS CẦN THIẾT

### Bước 8: Thêm method updateCartTotals

**Thêm vào CartService:**
```java
private void updateCartTotals(Cart cart) {
    // Tính lại tổng items và tổng giá
    int totalItems = cart.getItems().stream()
        .mapToInt(CartItem::getQuantity)
        .sum();
    
    BigDecimal totalPrice = cart.getItems().stream()
        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
    cart.setTotalItems(totalItems);
    cart.setTotalPrice(totalPrice);
    cardRepository.save(cart);
}
```

## 🎯 CÁCH SỬ DỤNG CHO FRONTEND

### Scenario 1: Lần đầu thêm sản phẩm vào cart
```javascript
POST /api/v1/carts/items
{
    "courseId": 123,
    "quantity": 1
}
// Tự động tạo cart nếu chưa có
```

### Scenario 2: Xem giỏ hàng
```javascript
GET /api/v1/carts
// Trả về cart hiện tại hoặc cart trống
```

### Scenario 3: Cập nhật số lượng
```javascript
PATCH /api/v1/carts/{itemId}
{
    "quantity": 3
}
```

### Scenario 4: Xóa sản phẩm
```javascript
DELETE /api/v1/carts/items/{courseId}
```

## ✅ CHECKLIST THỰC HIỆN

- [ ] **Bước 1**: Sửa lỗi đệ quy trong updateCart() (**QUAN TRỌNG NHẤT**)
- [ ] **Bước 2**: Tối ưu logic lặp lại 
- [ ] **Bước 3**: Thêm API GET cart
- [ ] **Bước 4**: Thêm API POST add to cart
- [ ] **Bước 5**: Thêm API DELETE remove item
- [ ] **Bước 6**: Hoàn thiện CartMapper
- [ ] **Bước 7**: Sửa CartDTO constructor
- [ ] **Bước 8**: Thêm helper methods
- [ ] **Testing**: Test từng API với Postman
- [ ] **Documentation**: Cập nhật API docs

## 🚦 THỨ TỰ ƯU TIÊN

1. **NGAY LẬP TỨC**: Sửa Bước 1 & 2 (tránh lỗi production)
2. **TUẦN NÀY**: Hoàn thành Bước 3, 4, 5 (API cơ bản)
3. **TUẦN SAU**: Hoàn thiện Bước 6, 7, 8 (polish & optimization)

Sau khi hoàn thành, bạn sẽ có một bộ API giỏ hàng hoàn chỉnh, tối ưu và dễ sử dụng cho frontend!
