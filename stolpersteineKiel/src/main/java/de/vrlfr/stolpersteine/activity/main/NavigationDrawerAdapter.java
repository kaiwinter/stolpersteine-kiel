package de.vrlfr.stolpersteine.activity.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.vrlfr.stolpersteine.R;

public class NavigationDrawerAdapter extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] text;
	private final Integer[] imageId;

	public NavigationDrawerAdapter(Activity context, String[] text, Integer[] imageId) {
		super(context, R.layout.navigation_drawer_list_item, text);
		this.context = context;
		this.text = text;
		this.imageId = imageId;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		NavigationDrawerViewHolder viewHolder;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.navigation_drawer_list_item, parent, false);

			viewHolder = new NavigationDrawerViewHolder();
			view.setTag(viewHolder);

			viewHolder.imageView = view.findViewById(R.id.img);
			viewHolder.textView = view.findViewById(R.id.txt);
		} else {
			viewHolder = (NavigationDrawerViewHolder) view.getTag();
		}

		viewHolder.imageView.setImageResource(imageId[position]);
		viewHolder.textView.setText(text[position]);
		return view;
	}

	private static final class NavigationDrawerViewHolder {
		ImageView imageView;
		TextView textView;
	}
}