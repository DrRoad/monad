package se.uu.csproject.monadvehicle;

import android.os.AsyncTask;

public class LoginTask extends AsyncTask <String, Void, String> {
    private AsyncLoginInteraction callingClass;

    public LoginTask(AsyncLoginInteraction callingClass) {
        this.callingClass = callingClass;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        return VehicleAdministration.postSignInRequest(params[0], params[1], params[2]);
    }

    @Override
    protected void onPostExecute(String response) {
        callingClass.processReceivedLoginResponse(response);
    }
}