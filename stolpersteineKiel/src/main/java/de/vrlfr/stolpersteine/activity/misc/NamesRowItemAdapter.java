package de.vrlfr.stolpersteine.activity.misc;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonsware.cwac.provider.StreamProvider;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.activity.FullscreenImageActivity;
import de.vrlfr.stolpersteine.database.StolpersteinBo;

public final class NamesRowItemAdapter extends ArrayAdapter<String> {

    private static final String TAG = NamesRowItemAdapter.class.getSimpleName();
    private static final int ROW_ID = R.layout.stolperstein_listview_item;
    private static final String AUTHORITY = "de.vrlfr.stolpersteine.provider";
    private static final Uri PROVIDER = Uri.parse("content://" + AUTHORITY);

    private final List<Collection<StolpersteinBo>> stolpersteineList;

    public NamesRowItemAdapter(Activity context, Collection<Collection<StolpersteinBo>> stolpersteine) {
        super(context, ROW_ID);
        this.stolpersteineList = new ArrayList<>(stolpersteine);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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

        Collection<StolpersteinBo> stolpersteine = stolpersteineList.get(position);
        String alleNamenString = "";
        for (StolpersteinBo stolperstein : stolpersteine) {
            String name = stolperstein.name;
            String geboren = stolperstein.geboren;
            String tod = stolperstein.tod;
            String daten = geboren + " - " + tod;

            if (alleNamenString.length() > 0) {
                alleNamenString += "<br/>";
            }
            alleNamenString += "<b>" + name + "</b> " + daten;
        }
        viewHolder.textViewNamen.setText(Html.fromHtml(alleNamenString));

        final StolpersteinBo stolpersteinBo = stolpersteine.iterator().next();
        ImageView stolpersteinBildImageView = convertView.findViewById(R.id.stolpersteinBild);
        String uri = "@drawable/id" + stolpersteinBo.imageId;
        final int imageResource = getContext().getResources().getIdentifier(uri, null, getContext().getPackageName());

        if (imageResource > 0) {
            Picasso.with(getContext()).load(imageResource).error(android.R.drawable.ic_delete)
                    .into(stolpersteinBildImageView);
            stolpersteinBildImageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = FullscreenImageActivity.newIntent(v.getContext(), imageResource,
                            stolpersteinBo.adresse);
                    NamesRowItemAdapter.this.getContext().startActivity(intent);
                }
            });
        }

        ImageView biografieImageViewPdf = convertView.findViewById(R.id.biografieBild);
        if (stolpersteinBo.bioId == -1) {
            biografieImageViewPdf.setVisibility(View.GONE);
        } else {
            biografieImageViewPdf.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    openBiografiePdf(stolpersteinBo.bioId);
                }
            });
        }

        TextView verlegedatumCopyrightTf = convertView.findViewById(R.id.detail_verlegedatum_copyright);
        verlegedatumCopyrightTf.setText(getContext().getString(R.string.image_verlegt_am) + stolpersteinBo.verlegedatum);

        return convertView;
    }

    private void openBiografiePdf(int bioId) {
        Uri path = PROVIDER
                .buildUpon()
                .appendPath(StreamProvider.getUriPrefix(AUTHORITY))
                .appendPath("assets/id" + bioId + ".pdf")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW, path);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Fehler").setMessage("Es wird eine App benötigt, die PDFs anzeigen kann. Bitte installiere eine aus dem Play Store").setPositiveButton("OK", null);
            builder.create().show();
        }
    }

    @Override
    public int getCount() {
        return stolpersteineList.size();
    }

    private final class MotionRowViewHolder {
        public TextView textViewNamen;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
