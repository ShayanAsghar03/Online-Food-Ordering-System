-- Create the FoodOrderingSystem Database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'FoodOrderingSystem')
BEGIN
    CREATE DATABASE FoodOrderingSystem;
END
GO

-- Use the newly created database
USE FoodOrderingSystem;
GO

-- Create Categories Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[dbo].[Categories]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
    CREATE TABLE Categories (
        CategoryID INT PRIMARY KEY IDENTITY(1,1),
        CategoryName NVARCHAR(100) NOT NULL UNIQUE
    );
END
GO

-- Create FoodItems Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[dbo].[FoodItems]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
    CREATE TABLE FoodItems (
        FoodItemID INT PRIMARY KEY IDENTITY(1,1),
        Name NVARCHAR(200) NOT NULL,
        Description NVARCHAR(500),
        Price DECIMAL(10, 2) NOT NULL,
        CategoryID INT NOT NULL,
        FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
    );
END
GO

-- Create Orders Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[dbo].[Orders]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
    CREATE TABLE Orders (
        OrderID INT PRIMARY KEY IDENTITY(1,1),
        CustomerName NVARCHAR(255) NOT NULL,
        CustomerAddress NVARCHAR(500) NOT NULL,
        OrderDate DATETIME DEFAULT GETDATE(),
        TotalAmount DECIMAL(10, 2) NOT NULL
    );
END
GO

-- Create OrderDetails Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[dbo].[OrderDetails]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
    CREATE TABLE OrderDetails (
        OrderDetailID INT PRIMARY KEY IDENTITY(1,1),
        OrderID INT NOT NULL,
        FoodItemID INT NOT NULL,
        Quantity INT NOT NULL,
        PricePerItem DECIMAL(10, 2) NOT NULL, -- Price at the time of order
        FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
        FOREIGN KEY (FoodItemID) REFERENCES FoodItems(FoodItemID)
    );
END
GO

-- Insert Sample Data into Categories
IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = 'Burgers')
    INSERT INTO Categories (CategoryName) VALUES ('Burgers');
IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = 'Pizzas')
    INSERT INTO Categories (CategoryName) VALUES ('Pizzas');
IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = 'Cold Drinks')
    INSERT INTO Categories (CategoryName) VALUES ('Cold Drinks');
IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = 'Desserts')
    INSERT INTO Categories (CategoryName) VALUES ('Desserts');
IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = 'Fried Chicken')
    INSERT INTO Categories (CategoryName) VALUES ('Fried Chicken');
IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = 'Fries')
    INSERT INTO Categories (CategoryName) VALUES ('Fries');
IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = 'Noodles')
    INSERT INTO Categories (CategoryName) VALUES ('Noodles');
IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = 'Salads')
    INSERT INTO Categories (CategoryName) VALUES ('Salads');
IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = 'Sandwiches')
    INSERT INTO Categories (CategoryName) VALUES ('Sandwiches');
IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = 'Wrap & Rolls')
    INSERT INTO Categories (CategoryName) VALUES ('Wrap & Rolls');
GO

-- Insert Sample Data into FoodItems
-- Burgers (Assuming CategoryID for 'Burgers' is 1, adjust if IDENTITY starts differently or already exists)
DECLARE @BurgersID INT;
SELECT @BurgersID = CategoryID FROM Categories WHERE CategoryName = 'Burgers';
IF @BurgersID IS NOT NULL
BEGIN
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Classic Beef Burger (Small)' AND CategoryID = @BurgersID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Classic Beef Burger (Small)', 'Juicy beef patty with lettuce, tomato, onion.', 5.99, @BurgersID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Classic Beef Burger (Medium)' AND CategoryID = @BurgersID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Classic Beef Burger (Medium)', 'Juicy beef patty with lettuce, tomato, onion.', 7.99, @BurgersID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Classic Beef Burger (Large)' AND CategoryID = @BurgersID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Classic Beef Burger (Large)', 'Juicy beef patty with lettuce, tomato, onion.', 9.99, @BurgersID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Spicy Chicken Burger' AND CategoryID = @BurgersID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Spicy Chicken Burger', 'Grilled chicken patty with spicy sauce and coleslaw.', 6.50, @BurgersID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Veggie Delight Burger' AND CategoryID = @BurgersID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Veggie Delight Burger', 'Wholesome vegetarian patty with fresh veggies.', 5.25, @BurgersID);
END

-- Pizzas (Assuming CategoryID for 'Pizzas' is 2)
DECLARE @PizzasID INT;
SELECT @PizzasID = CategoryID FROM Categories WHERE CategoryName = 'Pizzas';
IF @PizzasID IS NOT NULL
BEGIN
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Pepperoni Pizza (Small)' AND CategoryID = @PizzasID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Pepperoni Pizza (Small)', 'Classic pepperoni with mozzarella cheese.', 8.00, @PizzasID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Pepperoni Pizza (Medium)' AND CategoryID = @PizzasID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Pepperoni Pizza (Medium)', 'Classic pepperoni with mozzarella cheese.', 12.00, @PizzasID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Pepperoni Pizza (Large)' AND CategoryID = @PizzasID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Pepperoni Pizza (Large)', 'Classic pepperoni with mozzarella cheese.', 15.00, @PizzasID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Margherita Pizza' AND CategoryID = @PizzasID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Margherita Pizza', 'Tomato, mozzarella, and fresh basil.', 10.50, @PizzasID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'BBQ Chicken Pizza' AND CategoryID = @PizzasID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('BBQ Chicken Pizza', 'Grilled chicken, BBQ sauce, red onions.', 13.75, @PizzasID);
END

-- Cold Drinks (Assuming CategoryID for 'Cold Drinks' is 3)
DECLARE @ColdDrinksID INT;
SELECT @ColdDrinksID = CategoryID FROM Categories WHERE CategoryName = 'Cold Drinks';
IF @ColdDrinksID IS NOT NULL
BEGIN
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Coca-Cola' AND CategoryID = @ColdDrinksID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Coca-Cola', 'Refreshing carbonated soft drink.', 2.00, @ColdDrinksID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Sprite' AND CategoryID = @ColdDrinksID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Sprite', 'Lemon-lime flavored soft drink.', 2.00, @ColdDrinksID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Orange Juice' AND CategoryID = @ColdDrinksID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Orange Juice', 'Freshly squeezed orange juice.', 3.50, @ColdDrinksID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Mango Juice' AND CategoryID = @ColdDrinksID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Mango Juice', 'Fresh mango juice.', 4.50, @ColdDrinksID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Apple Juice' AND CategoryID = @ColdDrinksID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Apple Juice', 'Fresh apple juice.', 2.50, @ColdDrinksID);
END

-- Desserts (Assuming CategoryID for 'Desserts' is 4)
DECLARE @DessertsID INT;
SELECT @DessertsID = CategoryID FROM Categories WHERE CategoryName = 'Desserts';
IF @DessertsID IS NOT NULL
BEGIN
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Chocolate Lava Cake' AND CategoryID = @DessertsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Chocolate Lava Cake', 'Warm chocolate cake with a molten center.', 4.75, @DessertsID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'New York Cheesecake' AND CategoryID = @DessertsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('New York Cheesecake', 'Rich and creamy classic cheesecake.', 4.50, @DessertsID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Vanilla cake' AND CategoryID = @DessertsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Vanilla cake', 'Rich and creamy classic cake.', 3.50, @DessertsID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'BlueBerrycake' AND CategoryID = @DessertsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('BlueBerrycake', 'Rich and creamy classic cake.', 4.50, @DessertsID);
END

-- Fried Chicken (Assuming CategoryID for 'Fried Chicken' is 5)
DECLARE @FriedChickenID INT;
SELECT @FriedChickenID = CategoryID FROM Categories WHERE CategoryName = 'Fried Chicken';
IF @FriedChickenID IS NOT NULL
BEGIN
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = '2 Pc Fried Chicken' AND CategoryID = @FriedChickenID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('2 Pc Fried Chicken', 'Crispy fried chicken pieces.', 5.00, @FriedChickenID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = '5 Pc Fried Chicken Bucket' AND CategoryID = @FriedChickenID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('5 Pc Fried Chicken Bucket', 'Bucket of crispy fried chicken.', 12.00, @FriedChickenID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = '3 Pc Fried Chicken Bucket' AND CategoryID = @FriedChickenID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('3 Pc Fried Chicken Bucket', 'Bucket of crispy fried chicken.', 7.00, @FriedChickenID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = '10 Pc Fried Chicken Bucket' AND CategoryID = @FriedChickenID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('10 Pc Fried Chicken Bucket', 'Bucket of crispy fried chicken.', 24.00, @FriedChickenID);
END

-- Fries (Assuming CategoryID for 'Fries' is 6)
DECLARE @FriesID INT;
SELECT @FriesID = CategoryID FROM Categories WHERE CategoryName = 'Fries';
IF @FriesID IS NOT NULL
BEGIN
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Regular Fries' AND CategoryID = @FriesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Regular Fries', 'Golden crispy french fries.', 2.50, @FriesID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Large Fries' AND CategoryID = @FriesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Large Fries', 'Golden crispy french fries.', 3.50, @FriesID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Loaded Fries' AND CategoryID = @FriesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Loaded Fries', 'Golden crispy french fries.', 2.50, @FriesID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Chicken Fries' AND CategoryID = @FriesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Chicken Fries', 'Golden crispy french fries.', 4.50, @FriesID);
END

-- Noodles (Assuming CategoryID for 'Noodles' is 7)
DECLARE @NoodlesID INT;
SELECT @NoodlesID = CategoryID FROM Categories WHERE CategoryName = 'Noodles';
IF @NoodlesID IS NOT NULL
BEGIN
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Chicken Chow Mein' AND CategoryID = @NoodlesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Chicken Chow Mein', 'Stir-fried noodles with chicken and vegetables.', 7.00, @NoodlesID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Vegetable Lo Mein' AND CategoryID = @NoodlesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Vegetable Lo Mein', 'Noodles with assorted fresh vegetables.', 6.50, @NoodlesID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Soupy Noodles' AND CategoryID = @NoodlesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Soupy Noodles', 'Noodles with soup.', 4.50, @NoodlesID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Spicy Noodles' AND CategoryID = @NoodlesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Spicy Noodles', 'Noodles with extra spice.', 5.50, @NoodlesID);
END

-- Salads (Assuming CategoryID for 'Salads' is 8)
DECLARE @SaladsID INT;
SELECT @SaladsID = CategoryID FROM Categories WHERE CategoryName = 'Salads';
IF @SaladsID IS NOT NULL
BEGIN
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Caesar Salad' AND CategoryID = @SaladsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Caesar Salad', 'Crisp romaine lettuce, croutons, parmesan, Caesar dressing.', 5.75, @SaladsID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Greek Salad' AND CategoryID = @SaladsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Greek Salad', 'Fresh vegetables, feta cheese, olives, Greek dressing.', 6.25, @SaladsID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'German Salad' AND CategoryID = @SaladsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('German Salad', 'Fresh vegetables, feta cheese, olives, German dressing.', 5.25, @SaladsID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Chicken Salad' AND CategoryID = @SaladsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Chicken Salad', 'Fresh vegetables, feta cheese, olives, Chicken dressing.', 8.25, @SaladsID);
END

-- Sandwiches (Assuming CategoryID for 'Sandwiches' is 9)
DECLARE @SandwichesID INT;
SELECT @SandwichesID = CategoryID FROM Categories WHERE CategoryName = 'Sandwiches';
IF @SandwichesID IS NOT NULL
BEGIN
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Club Sandwich' AND CategoryID = @SandwichesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Club Sandwich', 'Triple-layered sandwich with chicken, bacon, lettuce, tomato.', 7.50, @SandwichesID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Veggie Sandwich' AND CategoryID = @SandwichesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Veggie Sandwich', 'Grilled vegetables and cheese in multi-grain bread.', 6.00, @SandwichesID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Chicken Sandwich' AND CategoryID = @SandwichesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Chicken Sandwich', 'Grilled chicken and cheese in multi-grain bread.', 7.00, @SandwichesID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Beaf Sandwich' AND CategoryID = @SandwichesID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Beaf Sandwich', 'Grilled beaf and cheese in multi-grain bread.', 8.00, @SandwichesID);
END

-- Wrap & Rolls (Assuming CategoryID for 'Wrap & Rolls' is 10)
DECLARE @WrapRollsID INT;
SELECT @WrapRollsID = CategoryID FROM Categories WHERE CategoryName = 'Wrap & Rolls';
IF @WrapRollsID IS NOT NULL
BEGIN
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Chicken Tikka Roll' AND CategoryID = @WrapRollsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Chicken Tikka Roll', 'Spicy chicken tikka wrapped in paratha.', 4.00, @WrapRollsID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Falafel Wrap' AND CategoryID = @WrapRollsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Falafel Wrap', 'Crispy falafel with hummus and fresh veggies.', 4.25, @WrapRollsID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Beaf Roll' AND CategoryID = @WrapRollsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Beaf Roll', 'Spicy Beaf wrapped in paratha.', 4.25, @WrapRollsID);
    IF NOT EXISTS (SELECT 1 FROM FoodItems WHERE Name = 'Jumbo Roll' AND CategoryID = @WrapRollsID)
        INSERT INTO FoodItems (Name, Description, Price, CategoryID) VALUES ('Jumbo Roll', 'Spicy Jumbo Roll wrapped in paratha', 4.25, @WrapRollsID);
END
GO

Select * from FoodItems
Select * from Orders