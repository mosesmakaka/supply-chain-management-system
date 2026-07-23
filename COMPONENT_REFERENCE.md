# UI Component Reference Guide

## Color Palette Reference

```
PRIMARY BLUE:       #3366CC (RGB: 51, 102, 204)
PRIMARY DARK:       #193366 (RGB: 25, 51, 102)
PRIMARY LIGHT:      #6699FF (RGB: 102, 153, 255)

SUCCESS GREEN:      #4CAF50 (RGB: 76, 175, 80)
SUCCESS DARK:       #388E3C (RGB: 56, 142, 60)
SUCCESS LIGHT:      #81C784 (RGB: 129, 199, 132)

WARNING ORANGE:     #FF9800 (RGB: 255, 152, 0)
WARNING DARK:       #E67C00 (RGB: 230, 124, 0)
WARNING LIGHT:      #FFB74D (RGB: 255, 183, 77)

DANGER RED:         #F44336 (RGB: 244, 67, 54)
DANGER DARK:        #D32F2F (RGB: 211, 47, 47)
DANGER LIGHT:       #FF8179 (RGB: 255, 129, 123)

BACKGROUND:         #F5F5F5 (RGB: 245, 245, 245)
SURFACE:            #FFFFFF (RGB: 255, 255, 255)
SURFACE DARK:       #EEEEEE (RGB: 238, 238, 238)

TEXT PRIMARY:       #212121 (RGB: 33, 33, 33)
TEXT SECONDARY:     #757575 (RGB: 117, 117, 117)
TEXT DISABLED:      #BDBDBD (RGB: 189, 189, 189)

BORDER:             #E0E0E0 (RGB: 224, 224, 224)
INFO:               #2196F3 (RGB: 33, 150, 243)
```

## Icon Reference

### Navigation Icons
```
← Back          (U+2190)
⌂ Home          (U+2302)
⚙ Settings      (U+2699)
⏻ Exit/Power    (U+23FB)
```

### CRUD Operations
```
+ Add           (U+002B)
✎ Edit          (U+270E)
✕ Delete        (U+2715)
▶ Open          (U+25B6)
💾 Save         (U+1F4BE)
✗ Cancel        (U+2717)
```

### Business Operations
```
🛒 Buy          (Emoji)
💰 Sell         (Emoji)
📦 Inventory    (Emoji)
🏭 Factory      (Emoji)
💵 Price        (Emoji)
```

### Entity Types
```
🌾 Producer     (Emoji)
🏭 Factory      (Emoji)
🏪 Market       (Emoji)
👤 Customer     (Emoji)
```

### Status Icons
```
✓ Check         (U+2713)
ℹ Info          (U+2139)
⚠ Alert         (U+26A0)
✘ Error         (U+2718)
```

## Component Usage Examples

### Using ModernButton
```java
import GUI.Utils.*;

// Create a primary button
ModernButton btn = new ModernButton(Icons.ADD + "New Item", ModernColors.SUCCESS);
btn.setPreferredSize(new Dimension(200, 50));

// Add click listener
btn.addActionListener(e -> {
    // Handle action
});

// Change color dynamically
btn.setColor(ModernColors.DANGER);
```

### Using ModernPanel
```java
import GUI.Utils.*;

// Create a modern panel
ModernPanel panel = new ModernPanel();
panel.setLayout(new BorderLayout());
panel.setBorder(new EmptyBorder(10, 10, 10, 10));

// Add components
JLabel label = new JLabel("Title");
label.setForeground(ModernColors.TEXT_PRIMARY);
label.setFont(new Font("Segoe UI", Font.BOLD, 16));
panel.add(label, BorderLayout.NORTH);

// Customize appearance
panel.setBorderRadius(12);
panel.setShadow(true);
panel.setShadowOffset(2);
```

### Using ModernColors
```java
import GUI.Utils.*;

// Set button color
button.setForeground(ModernColors.TEXT_PRIMARY);
button.setBackground(ModernColors.PRIMARY);

// Set panel color
panel.setBackground(ModernColors.SURFACE);

// Use status colors
successButton.setColor(ModernColors.SUCCESS);
warningButton.setColor(ModernColors.WARNING);
errorButton.setColor(ModernColors.DANGER);
```

## Button Styling by Action Type

### Primary Actions (Open, Edit, Main CTA)
```java
ModernButton btn = new ModernButton(Icons.OPEN + "Open", ModernColors.PRIMARY);
```
**Use for**: Opening items, main workflows

### Success Actions (Add, Create, Produce)
```java
ModernButton btn = new ModernButton(Icons.ADD + "New", ModernColors.SUCCESS);
```
**Use for**: Creating new items, positive operations

### Danger Actions (Delete, Exit)
```java
ModernButton btn = new ModernButton(Icons.DELETE + "Delete", ModernColors.DANGER);
```
**Use for**: Destructive operations, warnings

### Secondary Actions (Back, Cancel)
```java
ModernButton btn = new ModernButton(Icons.BACK + "Back", ModernColors.TEXT_SECONDARY);
```
**Use for**: Navigation back, cancellations

### Status-Specific (Markets, Customers, etc.)
```java
// Markets header
ModernPanel header = new ModernPanel(ModernColors.INFO);

// Factories header
ModernPanel header = new ModernPanel(ModernColors.PRIMARY);

// Raw Materials header
ModernPanel header = new ModernPanel(ModernColors.SUCCESS);
```

## List View Styling

### Standard List Configuration
```java
// Create and style list
JList<Item> list = new JList<>(listModel);
list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
list.setBackground(ModernColors.SURFACE);
list.setSelectionBackground(ModernColors.PRIMARY_LIGHT);
list.setSelectionForeground(Color.WHITE);
list.setFixedCellHeight(40);

// Wrap in scroll pane with border
JScrollPane scrollPane = new JScrollPane(list);
scrollPane.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER));
```

## Header Panel Pattern

### Standard Header
```java
ModernPanel headerPanel = new ModernPanel(ModernColors.PRIMARY);
headerPanel.setLayout(new BorderLayout());
headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

JLabel titleLabel = new JLabel(Icons.CUSTOMER + "Customers");
titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
titleLabel.setForeground(Color.WHITE);
headerPanel.add(titleLabel, BorderLayout.WEST);
```

## Button Panel Pattern

### Standard Button Bar
```java
ModernPanel buttonPanel = new ModernPanel(ModernColors.SURFACE_DARK);
buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
buttonPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

// Add buttons in order: Back, Delete, Add, Primary Action
buttonPanel.add(backButton);
buttonPanel.add(deleteButton);
buttonPanel.add(addButton);
buttonPanel.add(openButton);
```

## Size Recommendations

### Components
- **Large Buttons**: 200x60 px (main actions)
- **Standard Buttons**: 140x40 px (secondary actions)
- **Small Buttons**: 100x30 px (inline actions)
- **List Row Height**: 40 px (comfortable touch target)

### Windows
- **Dashboard**: 600x500 px
- **List Windows**: 650x500 px
- **Detail Windows**: 500x400 px
- **Form Windows**: 500x550 px

### Spacing
- **Panel Padding**: 15px
- **Button Spacing**: 10px
- **Element Margin**: 5px

## Font Specifications

- **Font Family**: Segoe UI (fallback: Arial)
- **Header Text**: 16px Bold (color: White on colored backgrounds)
- **Title Text**: 18px Bold (color: TEXT_PRIMARY)
- **Body Text**: 13px Regular (color: TEXT_PRIMARY)
- **Helper Text**: 12px Regular (color: TEXT_SECONDARY)
