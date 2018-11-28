package kivi.ugran.com.launcher.navigationdrawer;

import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import kivi.ugran.com.core.Constants;
import kivi.ugran.com.core.SharedPreferenceUtils;
import kivi.ugran.com.launcher.R;

public class DrawerMenuAdapter extends ArrayAdapter<DrawerMenuOptionItem> implements AdapterView.OnItemClickListener {

    private int mExtraPadding;
    private LayoutInflater mInflater;
    private CompoundButton.OnCheckedChangeListener switchListenerForKiviOnline;

    public enum RowType {
        DIVIDER,    // non-clickable, divider line
        HEADING,    // non-clickable, header for each item category
        ITEM,      // index into the Pager view
        FIRST_ITEM, HOME, HEADER,     // non clickable, title line
        TOGGLEABLE, // switched setting (Private Viewing)
        FEEDBACK,   // Level drawable on right (Offers)
        INTENT,     // startActivity item
    }

    public DrawerMenuAdapter(Context context, int resource) {
        super(context, resource);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mExtraPadding = (int) (17 * context.getResources().getDisplayMetrics().density);
    }

    public DrawerMenuAdapter(Context context, int resource, DrawerMenuOptionItem[] objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mExtraPadding = (int) (17 * context.getResources().getDisplayMetrics().density);
    }

    public DrawerMenuAdapter(Context context, int resource, List<DrawerMenuOptionItem> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mExtraPadding = (int) (17 * context.getResources().getDisplayMetrics().density);
    }

    public void setCompoundButtonOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        this.switchListenerForKiviOnline = listener;
    }

    @Override public int getItemViewType(int position) {
        return getItem(position).rowType;
    }

    public RowType getItemViewTypeEnum(int position) {
        // can drop the clamping when the values are set by enum, right now they're set by int
        return RowType.values()[Math.max(0, Math.min(getItemViewType(position), getViewTypeCount()))];
    }

    @Override public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override public boolean areAllItemsEnabled() {
        return true;
    }

    @Override public boolean isEnabled(int position) {
        return getItem(position).enabled;
    }

    @Override public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        DrawerMenuOptionItem item = getItem(position);

        if (convertView == null) {
            v = prepareView(position, getItemViewTypeEnum(position), viewGroup);
        }

        bindView(position, ViewHolder.get(v), getItemViewTypeEnum(position), item, v);

        return v;
    }

    public int getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        mSelectedItem = selectedItem;
    }

    private int mSelectedItem = -1;

    /**
     * receives a RowType and is responsible for creating and initializing a view according to the row Type
     * That involves either configuring visibility of components in a layout, or modifying the layout params/
     * attributes
     * The Row item is not passed in to reflect the idea that everything in here should respond to the rowType and not
     * the content of
     * the item to be displayed.
     */
    private View prepareView(int position, RowType rowType, ViewGroup viewGroup) {
        View view;
        ViewHolder viewHolder = null;
        switch (rowType) {
            case DIVIDER:
                view = mInflater.inflate(R.layout.list_divider, viewGroup, false);
                view.setPadding(view.getPaddingLeft() + mExtraPadding, view.getPaddingTop(),
                        view.getPaddingRight() + mExtraPadding, view.getPaddingBottom());
                break;
            case HEADING:
                view = mInflater.inflate(R.layout.list_heading, viewGroup, false);
                view.setPadding(view.getPaddingLeft() + mExtraPadding, view.getPaddingTop(), view.getPaddingRight(),
                        view.getPaddingBottom());
                break;
            case TOGGLEABLE:
                view = mInflater.inflate(R.layout.list_row_text_switch, viewGroup, false);
                viewHolder = new ViewHolder(view);
                viewHolder.switchCompat.setVisibility(View.VISIBLE);
                viewHolder.switchCompat.setOnCheckedChangeListener(switchListenerForKiviOnline);
                break;
            case FEEDBACK:
                view = mInflater.inflate(R.layout.list_row_text_feedback, viewGroup, false);
                viewHolder = new ViewHolder(view);
                break;
            // list out the default cases
            case HEADER:
            case INTENT:
            case FIRST_ITEM:
            case ITEM:
            case HOME:
            default:
                view = mInflater.inflate(R.layout.list_filter_item, viewGroup, false);
                viewHolder = new ViewHolder(view);
                viewHolder.title = view.findViewById(R.id.filter_name);
                if (rowType == RowType.FIRST_ITEM) {
                    view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + mExtraPadding, view.getPaddingRight(),
                            view.getPaddingBottom());
                }
                break;
        }
        if (viewHolder != null) view.setTag(viewHolder);
        return view;
    }

    /*
    Receives a specific Item and binds the values of that item to the view, the viewHOlder should have already cached everythin in the view
     so we only need to pass in the Holder
     */
    private void bindView(int position, ViewHolder viewHolder, RowType rowType, DrawerMenuOptionItem item, View v) {

        switch (rowType) {

            case DIVIDER:
                return;
            case HEADING:
                TextView heading = (TextView) v.findViewById(R.id.heading_title);
                heading.setText(item.headingTitle);
                if (item.textSize > 0) {
                    heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.textSize);
                }
                return;
            case TOGGLEABLE:
                if (viewHolder.switchCompat != null) {
                    //viewHolder.switchCompat.setChecked(item.feedback == 1);
                    if (SharedPreferenceUtils.loadBoolean(getContext(), Constants.KiviPreferences.KEY_KIVI_ONLINE)) {
                        viewHolder.switchCompat.setChecked(true);
                    } else {
                        viewHolder.switchCompat.setChecked(false);
                    }
                }
                break;
            case FEEDBACK:
                if (viewHolder.feedback != null) viewHolder.feedback.setText(Integer.toString(item.feedback));
                break;
            case ITEM:
            case FIRST_ITEM:
            case HOME:
            case HEADER:
            case INTENT:
            default:
                break;
        }

        if (viewHolder.title != null && !TextUtils.isEmpty(item.title)) {
            viewHolder.title.setText(item.title);
            if (item.textSize > 0) {
                viewHolder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.textSize);
            }
        }
        if (viewHolder.selectionIndicator != null) {
            if (position == getSelectedItem()) {
                viewHolder.selectionIndicator.setVisibility(View.VISIBLE);
                viewHolder.selectionIndicator.setBackgroundColor(
                        ContextCompat.getColor(getContext(), R.color.profile_settings_selector_selected_color));
                viewHolder.title.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.profile_settings_selector_selected_color));
            } else {
                viewHolder.selectionIndicator.setVisibility(View.INVISIBLE);
                viewHolder.title.setTextColor(Color.WHITE);
            }
        }
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int j = 0;
        j++;
    }

    private static class ViewHolder {
        TextView title;
        TextView feedback;
        SwitchCompat switchCompat;
        LinearLayout manageProfile;
        View selectionIndicator;

        private ViewHolder(View view) {
            view.setTag(R.id.holder, this);
            title = view.findViewById(R.id.item_name);
            switchCompat = view.findViewById(R.id.toggle_switch);
            feedback = view.findViewById(R.id.count_feedback);
            selectionIndicator = view.findViewById(R.id.selection_indicator);
            manageProfile = view.findViewById(R.id.item_layout);
        }

        public static ViewHolder get(View view) {
            return (ViewHolder) view.getTag(R.id.holder);
        }
    }
}

