package com.akgram.app;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import java.util.*;
import java.util.regex.*;

public class MainActivity extends Activity {
    private LinearLayout body;
    private SharedPreferences prefs;
    private final ArrayList<String> saved = new ArrayList<>();

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        prefs = getSharedPreferences("akgram", MODE_PRIVATE);
        loadSaved();
        buildUi();
        handleShare(getIntent());
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleShare(intent);
    }

    private TextView text(String s, int sp, boolean bold) {
        TextView v = new TextView(this);
        v.setText(s); v.setTextSize(sp); v.setTextColor(Color.rgb(25,25,25));
        v.setPadding(18,12,18,12);
        if (bold) v.setTypeface(null, 1);
        return v;
    }

    private Button btn(String s) {
        Button b = new Button(this); b.setText(s); b.setAllCaps(false); return b;
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        body = new LinearLayout(this); body.setOrientation(LinearLayout.VERTICAL); body.setPadding(12,12,12,40);
        scroll.addView(body);

        TextView logo = text("akgram", 30, true); logo.setGravity(Gravity.CENTER_HORIZONTAL); body.addView(logo);
        TextView sub = text("Social feed + permission-based media downloader", 14, false); sub.setGravity(Gravity.CENTER_HORIZONTAL); body.addView(sub);

        body.addView(text("Feed", 22, true));
        addPost("@akgram", "Welcome to Akgram", "A clean social-style Android MVP. Use the downloader below for media you own or have permission to save.");
        addPost("@creator", "Collections", "Save direct media URLs into your Akgram collection and download one, selected URLs, or everything in bulk.");

        body.addView(text("Download media", 22, true));
        final EditText url = new EditText(this); url.setHint("Paste direct image/video URL (https://...jpg, .png, .mp4, etc.)"); url.setMinLines(2); body.addView(url);
        Button download = btn("Download to device"); body.addView(download);
        download.setOnClickListener(v -> downloadOne(url.getText().toString().trim()));
        Button save = btn("Save URL to Akgram collection"); body.addView(save);
        save.setOnClickListener(v -> { String u=url.getText().toString().trim(); if(validHttp(u)){ saved.add(u); persist(); toast("Saved to collection"); buildUi(); } else toast("Enter a valid http/https URL"); });

        body.addView(text("Saved collection", 22, true));
        if(saved.isEmpty()) body.addView(text("No saved media URLs yet.", 15, false));
        int i=1;
        for(String s: new ArrayList<>(saved)) {
            LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.VERTICAL); row.setPadding(8,8,8,8);
            row.addView(text(i+". "+s, 13, false));
            LinearLayout actions = new LinearLayout(this); actions.setOrientation(LinearLayout.HORIZONTAL);
            Button d=btn("Download"); Button r=btn("Remove");
            actions.addView(d, new LinearLayout.LayoutParams(0,-2,1)); actions.addView(r, new LinearLayout.LayoutParams(0,-2,1)); row.addView(actions);
            d.setOnClickListener(v -> downloadOne(s));
            r.setOnClickListener(v -> { saved.remove(s); persist(); buildUi(); });
            body.addView(row); i++;
        }
        Button all = btn("Download ALL saved media"); body.addView(all); all.setOnClickListener(v -> downloadAll());
        Button clear = btn("Clear collection"); body.addView(clear); clear.setOnClickListener(v -> { saved.clear(); persist(); buildUi(); });

        body.addView(text("Bulk paste", 22, true));
        final EditText bulk = new EditText(this); bulk.setHint("Paste multiple direct media URLs, one per line"); bulk.setMinLines(5); body.addView(bulk);
        Button bulkDl = btn("Download pasted URLs"); body.addView(bulkDl);
        bulkDl.setOnClickListener(v -> { int n=0; for(String x:bulk.getText().toString().split("\\s+")){ if(validHttp(x)){ downloadOne(x.trim()); n++; }} toast(n+" download(s) queued"); });

        body.addView(text("Important", 18, true));
        body.addView(text("Akgram does not bypass private accounts, login protections, DRM, or Instagram restrictions. Instagram post/profile URLs are web pages, not direct media files; this MVP downloads direct media URLs and Akgram-saved collections only.", 13, false));
        setContentView(scroll);
    }

    private void addPost(String user, String title, String caption) {
        LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setPadding(12,12,12,18);
        GradientDrawableCompat.apply(card, Color.rgb(247,247,247));
        card.addView(text(user,16,true));
        TextView art=text("◉  AKGRAM  ◉",28,true); art.setGravity(Gravity.CENTER); art.setPadding(10,70,10,70); art.setBackgroundColor(Color.rgb(235,235,235)); card.addView(art);
        card.addView(text("♡   💬   ↗     🔖",20,false));
        card.addView(text(title,16,true)); card.addView(text(caption,14,false)); body.addView(card);
    }

    private void handleShare(Intent intent){
        if(intent!=null && Intent.ACTION_SEND.equals(intent.getAction())){
            String t=intent.getStringExtra(Intent.EXTRA_TEXT);
            if(t!=null){ String u=extractUrl(t); if(u!=null){ saved.add(u); persist(); toast("Shared URL added to Akgram collection"); } }
        }
    }

    private String extractUrl(String t){ Matcher m=Pattern.compile("https?://\\S+").matcher(t); return m.find()?m.group().replaceAll("[),.]+$",""):null; }
    private boolean validHttp(String u){ try { Uri x=Uri.parse(u); return ("http".equalsIgnoreCase(x.getScheme())||"https".equalsIgnoreCase(x.getScheme())) && x.getHost()!=null; } catch(Exception e){return false;} }

    private void downloadAll(){ if(saved.isEmpty()){toast("Collection is empty");return;} int n=0; for(String s:new ArrayList<>(saved)){ if(validHttp(s)){downloadOne(s); n++;}} toast(n+" download(s) queued"); }

    private void downloadOne(String u){
        if(!validHttp(u)){toast("Enter a valid direct media URL");return;}
        try{
            DownloadManager dm=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
            Uri uri=Uri.parse(u);
            String name=uri.getLastPathSegment(); if(name==null||name.length()<3) name="akgram_"+System.currentTimeMillis();
            name=name.replaceAll("[^A-Za-z0-9._-]","_");
            DownloadManager.Request r=new DownloadManager.Request(uri);
            r.setTitle("Akgram media"); r.setDescription(name); r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"Akgram/"+name);
            dm.enqueue(r); toast("Download queued: "+name);
        }catch(Exception e){ toast("Could not queue download: "+e.getMessage()); }
    }

    private void loadSaved(){ String raw=prefs.getString("saved",""); if(!raw.isEmpty()) saved.addAll(Arrays.asList(raw.split("\\n"))); }
    private void persist(){ StringBuilder b=new StringBuilder(); for(String s:saved){ if(b.length()>0)b.append('\n'); b.append(s);} prefs.edit().putString("saved",b.toString()).apply(); }
    private void toast(String s){ Toast.makeText(this,s,Toast.LENGTH_SHORT).show(); }

    static class GradientDrawableCompat {
        static void apply(View v, int color){ android.graphics.drawable.GradientDrawable g=new android.graphics.drawable.GradientDrawable(); g.setColor(color); g.setCornerRadius(24); v.setBackground(g); }
    }
}
