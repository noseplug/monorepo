package com.noseplugapp.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.noseplugapp.android.events.CommentAddedEvent;
import com.noseplugapp.android.models.OdorEvent;
import com.noseplugapp.android.models.OdorReport;
import com.noseplugapp.android.models.Wallpost;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static android.R.attr.id;
import static android.R.id.text1;

public class OdorEventDetailsActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {
    private static final String TAG = OdorEventDetailsActivity.class.getSimpleName();
    private OdorEvent odorEvent;
    private final Context context = this;
    private final App app = App.getInstance();

    private String commentText;

    private ListView commentList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_odor_event_details);
        setSupportActionBar((Toolbar) this.findViewById(R.id.my_toolbar));

        UUID odorEventId = ((ParcelUuid) getIntent().getParcelableExtra(
                getResources().getString(R.string.intent_extra_odor_event_id))
        ).getUuid();

        odorEvent = app.api().getOdorEvent(odorEventId);
        String description = odorEvent.getOdorReports().get(0).getOdor().getDescription();

        Log.v(TAG, String.format("Starting activity with odor event %s (%s)",
                odorEventId, description));

        final ListView reportList = (ListView) this.findViewById(R.id.odor_report_list);
        reportList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, odorEvent.getOdorReports()));
        reportList.setOnItemClickListener(this);
        ArrayAdapter<Wallpost> x = new ArrayAdapter<Wallpost>(this,
                android.R.layout.simple_list_item_1, odorEvent.getSortedWallposts());

        commentList = (ListView) this.findViewById(R.id.comment_list);
        commentList.setAdapter(new WallPostAdapter<Wallpost>(this,
                android.R.layout.simple_list_item_1, odorEvent.getSortedWallposts()));

        findViewById(R.id.commentButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Log.v(TAG, "Clicked make comment button");
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);


                alertDialogBuilder
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        commentText = userInput.getText().toString();
                                        Wallpost newWallpost = new Wallpost("You", commentText, Wallpost.Type.normal);
                                        app.api().addWallPost(newWallpost, odorEvent.getId().toString());
                                        //((BaseAdapter) commentList.getAdapter()).notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog commentDialog = alertDialogBuilder.create();
                commentDialog.show();
            }

        });
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v(TAG, "Received click for odor report at index " + position);
        Intent intent = new Intent(this, OdorReportDetailsActivity.class);
        OdorReport report = odorEvent.getOdorReports().get(position);
        intent.putExtra(getResources().getString(R.string.intent_extra_odor_report_id),
                new ParcelUuid(report.getId()));
        startActivity(intent);
    }

    @Subscribe
    public void onCommentAddedEvent(CommentAddedEvent commentAddedEvent)
    {

        Log.d(TAG, "Comment added event received at odorevent details");

        ((BaseAdapter) commentList.getAdapter()).notifyDataSetChanged();
        ((ArrayAdapter<Wallpost>) commentList.getAdapter()).sort(
                new Comparator<Wallpost>() {
                    @Override
                    public int compare(Wallpost o1, Wallpost o2) {
                        if (o1.type != o2.type) {
                            if (o1.type == Wallpost.Type.normal) {
                                return 1;
                            }
                            else {
                                return -1;
                            }
                        }
                        else {
                            return o2.date.compareTo(o1.date);
                        }
                    }
                }
        );
        ((BaseAdapter) commentList.getAdapter()).notifyDataSetChanged();
    }

    private class WallPostAdapter<Wallpost> extends ArrayAdapter<Wallpost> {

        public WallPostAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public WallPostAdapter(Context context, int resource, List<Wallpost> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(android.R.layout.simple_list_item_1, null);
            }

            Wallpost p = getItem(position);

            if (p != null) {
                TextView textView = (TextView) v.findViewById(text1);

                if (textView != null) {

                    Spanned text = Html.fromHtml(p.toString());
                    URLSpan[] currentSpans = text.getSpans(0, text.length(), URLSpan.class);

                    SpannableString buffer = new SpannableString(text);
                    Linkify.addLinks(buffer, Linkify.WEB_URLS);

                    for (URLSpan span : currentSpans) {
                        int end = text.getSpanEnd(span);
                        int start = text.getSpanStart(span);
                        buffer.setSpan(span, start, end, 0);
                    }
                    textView.setText(buffer);
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    //Linkify.addLinks(textView, Linkify.WEB_URLS);
                }
            }

            return v;

        }
    }
}


