package de.vrlfr.stolpersteine.activity.stolperstein;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.common.util.Strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.database.Stolperstein;

public final class NamesRowItemAdapter extends ArrayAdapter<String> {

    private static final int ROW_ID = R.layout.stolperstein_listview_item;

    private final List<Collection<Stolperstein>> stolpersteineList;

    public NamesRowItemAdapter(Activity context, Collection<Collection<Stolperstein>> stolpersteine) {
        super(context, ROW_ID);
        this.stolpersteineList = new ArrayList<>(stolpersteine);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        MotionRowViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(ROW_ID, parent, false);

            viewHolder = new MotionRowViewHolder();
            convertView.setTag(viewHolder);

            viewHolder.textViewNamen = convertView.findViewById(R.id.stolpersteinNamen);

        } else {
            viewHolder = (MotionRowViewHolder) convertView.getTag();
        }

        Collection<Stolperstein> stolpersteine = stolpersteineList.get(position);
        String alleNamenString = "";
        for (Stolperstein stolperstein : stolpersteine) {

            if (!alleNamenString.isEmpty()) {
                alleNamenString += "<br/>";
            }

            if (Strings.isEmptyOrWhitespace(stolperstein.tod)) {
                alleNamenString += "<b>" + stolperstein.name + "</b> * " + stolperstein.geboren;
            } else {
                alleNamenString += "<b>" + stolperstein.name + "</b> * " + stolperstein.geboren + "  † " + stolperstein.tod;
            }
        }
        viewHolder.textViewNamen.setText(Html.fromHtml(alleNamenString));

        final Stolperstein stolperstein = stolpersteine.iterator().next();
        ImageView stolpersteinBildImageView = convertView.findViewById(R.id.stolpersteinBild);
        int imageResource = stolperstein.getResourceIdForImageId(getContext());

        if (imageResource > 0) {
            stolpersteinBildImageView.setImageResource(imageResource);
            stolpersteinBildImageView.setOnClickListener(v -> {
                Intent intent = FullscreenImageActivity.newIntent(v.getContext(), imageResource,
                        stolperstein.adresse);
                NamesRowItemAdapter.this.getContext().startActivity(intent);
            });
        }

        Button biografieImageViewTxt = convertView.findViewById(R.id.biografieButtonTxt);
        if (stolperstein.bioId == -1) {
            biografieImageViewTxt.setVisibility(View.GONE);
        } else {
            biografieImageViewTxt.setOnClickListener(v -> {
                Intent intent = FullscreenTxtActivity.newIntent(v.getContext(), stolperstein.bioId);
                NamesRowItemAdapter.this.getContext().startActivity(intent);
            });
        }

        TextView verlegedatumCopyrightTf = convertView.findViewById(R.id.detail_verlegedatum_copyright);
        verlegedatumCopyrightTf.setText(getContext().getString(R.string.image_verlegt_am) + stolperstein.verlegedatum);

        return convertView;
    }

    @Override
    public int getCount() {
        return stolpersteineList.size();
    }

    private static final class MotionRowViewHolder {
        public TextView textViewNamen;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
