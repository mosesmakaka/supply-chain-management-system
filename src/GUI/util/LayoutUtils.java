package GUI.util;

import java.awt.*;

public class LayoutUtils {
    public static GridBagConstraints gbc(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridx; c.gridy = gridy; c.gridwidth = gridwidth; c.gridheight = gridheight;
        c.weightx = weightx; c.weighty = weighty;
        c.anchor = anchor; c.fill = fill;
        c.insets = new Insets(6, 10, 6, 10);
        return c;
    }

    public static GridBagConstraints gbc(int gridx, int gridy) {
        return gbc(gridx, gridy, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
    }
}

