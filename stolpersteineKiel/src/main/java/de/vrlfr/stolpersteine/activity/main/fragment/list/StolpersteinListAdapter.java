package de.vrlfr.stolpersteine.activity.main.fragment.list;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import de.vrlfr.stolpersteine.database.Stolperstein;

public class StolpersteinListAdapter extends ArrayAdapter<Stolperstein> implements Filterable {

	private final List<Stolperstein> stolpersteineFilterList;

	private ValueFilter valueFilter;

	public StolpersteinListAdapter(Context context, List<Stolperstein> stolpersteine) {
		super(context, android.R.layout.simple_list_item_1);
		addAll(stolpersteine);
		this.stolpersteineFilterList = stolpersteine;
	}

	@Override
	public Filter getFilter() {
		if (valueFilter == null) {
			valueFilter = new ValueFilter();
		}
		return valueFilter;
	}

	private class ValueFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();

			if (constraint != null && constraint.length() > 0) {
				ArrayList<Stolperstein> filterList = new ArrayList<>();
				for (Stolperstein stolperstein : stolpersteineFilterList) {
					if (stolperstein.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
						filterList.add(stolperstein);
					}
				}
				results.count = filterList.size();
				results.values = filterList;
			} else {
				results.count = stolpersteineFilterList.size();
				results.values = stolpersteineFilterList;
			}
			return results;

		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			clear();
			ArrayList<Stolperstein> stolpersteine = (ArrayList<Stolperstein>) results.values;
			addAll(stolpersteine);
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}

}
