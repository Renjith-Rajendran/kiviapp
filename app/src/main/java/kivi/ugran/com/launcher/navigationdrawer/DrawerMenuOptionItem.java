package kivi.ugran.com.launcher.navigationdrawer;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerMenuOptionItem {

    public static final int TEXT_STYLE_LIGHT = 1;
    public static final int TEXT_STYLE_NORMAL = 2;
    public static final int TEXT_STYLE_BOLD = 3;
    public final int rowType;
    public final CharSequence title;
    public final boolean enabled;
    public final int textSize;
    public final int viewContext;
    @TextStyle public final int textStyle;
    public int icon = 0;
    public int feedback = 0;
    public MenuCommand command;
    public CharSequence headingTitle = null;

    public DrawerMenuOptionItem(int rowType, CharSequence title, boolean enabled, int icon, int textSize, int textStyle,
            int feedback, MenuCommand command, int viewContext) {
        this.rowType = rowType;
        this.title = title;
        this.enabled = enabled;
        this.icon = icon;
        this.feedback = feedback;
        this.command = command;
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.viewContext = viewContext;
    }

    @IntDef({ TEXT_STYLE_LIGHT, TEXT_STYLE_NORMAL, TEXT_STYLE_BOLD }) @Retention(RetentionPolicy.SOURCE)
    public @interface TextStyle {
    }

    public static class Builder {
        public int textStyle = 0;
        private int rowType;
        private CharSequence title;
        private boolean enabled = true;
        private int icon = 0;
        private int feedback = 0;
        private MenuCommand command;
        private CharSequence headingTitle = null;
        private int textSize = 0;
        private int viewContext = 0;

        public Builder rowType(DrawerMenuAdapter.RowType rowType) {
            this.rowType = rowType.ordinal();
            return this;
        }

        public Builder title(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder headingTitle(CharSequence heading) {
            this.headingTitle = heading;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder icon(int icon) {
            this.icon = icon;
            return this;
        }

        public Builder viewContext(int viewContext) {
            this.viewContext = viewContext;
            return this;
        }

        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder command(MenuCommand command) {
            this.command = command;
            return this;
        }

        public Builder textStyle(@TextStyle int textStyle) {
            this.textStyle = textStyle;
            return this;
        }

        public DrawerMenuOptionItem build() {
            setDefaults();
            DrawerMenuOptionItem profileWorkspaceOptionItem =
                    new DrawerMenuOptionItem(rowType, title, enabled, icon, textSize, textStyle, feedback, command,
                            viewContext);
            if (headingTitle != null) profileWorkspaceOptionItem.headingTitle = headingTitle;
            return profileWorkspaceOptionItem;
        }

        private void setDefaults() {
            if (title == null) {
                title = "";
            }

            if (command == null) {
                command = new MenuCommandImpl(-1);
            }
        }
    }
}

