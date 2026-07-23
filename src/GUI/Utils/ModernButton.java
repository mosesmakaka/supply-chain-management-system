package GUI.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private boolean isHovered = false;
    private Color originalBackground;
    private Color hoverBackground;
    private Color clickBackground;
    private float borderRadius = 8f;

    public ModernButton(String text, Color bgColor) {
        super(text);
        this.originalBackground = bgColor;
        this.hoverBackground = brighten(bgColor, 0.1f);
        this.clickBackground = darken(bgColor, 0.2f);
        
        setupStyle();
    }

    public ModernButton(String text) {
        this(text, ModernColors.PRIMARY);
    }

    private void setupStyle() {
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setForeground(Color.WHITE);
        setBackground(originalBackground);
        setOpaque(false);
        setBorder(new EmptyBorder(10, 16, 10, 16));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                hoverBackground = brighten(originalBackground, 0.15f);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color drawColor = isHovered ? hoverBackground : originalBackground;
        
        // Draw rounded background
        g2d.setColor(drawColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), (int)borderRadius * 2, (int)borderRadius * 2);

        // Draw shadow
        if (isHovered) {
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 1, (int)borderRadius * 2, (int)borderRadius * 2);
        }

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border
    }

    private Color brighten(Color color, float factor) {
        return new Color(
            Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor)),
            Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor)),
            Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor))
        );
    }

    private Color darken(Color color, float factor) {
        return new Color(
            Math.max(0, (int)(color.getRed() * (1 - factor))),
            Math.max(0, (int)(color.getGreen() * (1 - factor))),
            Math.max(0, (int)(color.getBlue() * (1 - factor)))
        );
    }

    public void setColor(Color bgColor) {
        this.originalBackground = bgColor;
        this.hoverBackground = brighten(bgColor, 0.15f);
        this.clickBackground = darken(bgColor, 0.2f);
        setBackground(bgColor);
    }
}
