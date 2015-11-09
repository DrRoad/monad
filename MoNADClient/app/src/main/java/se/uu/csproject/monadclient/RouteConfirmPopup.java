package se.uu.csproject.monadclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import se.uu.csproject.monadclient.recyclerviews.FullTrip;

public class RouteConfirmPopup extends AppCompatActivity implements AsyncResponseString{

    private TextView busIdView, startTimeView, endTimeView, startPositionView, endPositionView;
    private FullTrip trip;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_route_confirm);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .40));

        busIdView = (TextView) findViewById(R.id.businfo);
        startTimeView = (TextView) findViewById(R.id.departtime);
        endTimeView = (TextView) findViewById(R.id.arrivetime);
        startPositionView = (TextView) findViewById(R.id.departname);
        endPositionView = (TextView) findViewById(R.id.arrivename);
        context = getApplicationContext();

        Bundle b = getIntent().getExtras();
        trip = b.getParcelable("selectedTrip");

        busIdView.setText(getResources().getString(R.string.label_trip_businfo) + " " + trip.getBusLinesString());
        startTimeView.setText(formatTime(trip.getStartTime()));
        endTimeView.setText(formatTime(trip.getEndTime()));
        startPositionView.setText(trip.getStartBusStop());
        endPositionView.setText(trip.getEndBusStop());

        ImageButton cancel = (ImageButton)findViewById(R.id.cancelbutton);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vw) {
                //return to the previous activity instead of start a new one
                finish();
            }


        });
    }

    // Book the trip
    public void confirmTrip(View view){
        String userTripId = trip.getId();
        Log.d("oops", "trip id: " + userTripId);
        SendBookingRequest asyncTask = new SendBookingRequest();
        asyncTask.delegate = this;
        asyncTask.execute(userTripId);
    }

    // Deal with the response from the server after the user books the trip
    public void processFinish(String response){
        Toast toast = Toast.makeText(context, response, Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(RouteConfirmPopup.this, TripsActivity.class);
        startActivity(intent);
        finish();
    }

    private String formatTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(calendar.getTime());
    }
}
