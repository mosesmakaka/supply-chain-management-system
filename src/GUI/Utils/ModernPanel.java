package GUI.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernPanel extends JPanel {
    private float borderRadius = 12f;
    private boolean hasShadow = true;
    private Color shadowColor = ModernColors.SHADOW;
    private int shadowOffset = 2;

    public ModernPanel() {
        super();
        setOpaque(false);
        setBackground(ModernColors.SURFACE);
    }

    public ModernPanel(Color bg) {
        this();
        setBackground(bg);
    }

    public ModernPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        setBackground(ModernColors.SURFACE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw shadow
        if (hasShadow) {
            g2d.setColor(shadowColor);
            g2d.fillRoundRect(
                shadowOffset, shadowOffset,
                width - 2 * shadowOffset, height - 2 * shadowOffset,
                (int)borderRadius * 2, (int)borderRadius * 2
            );
        }

        // Draw main panel background
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, width, height, (int)borderRadius * 2, (int)borderRadius * 2);

        super.paintComponent(g);
    }

    public void setBorderRadius(float radius) {
        this.borderRadius = radius;
    }

    public void setShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
    }

    public void setShadowOffset(int offset) {
        this.shadowOffset = offset;
    }
}
