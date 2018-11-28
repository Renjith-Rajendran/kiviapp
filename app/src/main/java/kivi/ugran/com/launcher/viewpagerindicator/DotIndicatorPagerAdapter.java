package kivi.ugran.com.launcher.viewpagerindicator;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import kivi.ugran.com.launcher.R;

public class DotIndicatorPagerAdapter extends PagerAdapter {
    private static final List<Item> items =
            Arrays.asList(new Item(R.color.md_indigo_500), new Item(R.color.md_green_500),
                    new Item(R.color.md_red_500), new Item(R.color.md_orange_500),
                    new Item(R.color.md_purple_500));

    @NonNull @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View item = LayoutInflater.from(container.getContext())
                .inflate(R.layout.fragment_tutorial_page_1, container, false);
        //CardView cardView = item.findViewById(R.id.card_view);
        //cardView.setCardBackgroundColor(
        //    ContextCompat.getColor(container.getContext(), (items.get(position).color)));
        container.addView(item);
        return item;
    }

    @Override public int getCount() {
        return items.size();
    }

    @Override public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private static class Item {
        private final int color;

        private Item(int color) {
            this.color = color;
        }
    }
}
