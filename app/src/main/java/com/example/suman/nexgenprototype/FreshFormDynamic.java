package com.example.suman.nexgenprototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FreshFormDynamic extends AppCompatActivity {

    private LinearLayout linearLayout;
    private TextView txtJSONResponse;

    private static String url = "http://appreciations.in/nexgensurvey/surveyformapi.php?formno=15";

    private final String TAG = this.getClass().getSimpleName();

    //JSON Node Names
    private static final String TAG_STUFF = "stuff";
    private static final String TAG_FORMNAME = "formname";
    private static final String TAG_ORDERNO = "orderno";
    private static final String TAG_ELE = "elementname";
    private static final String TAG_ATTRNAME = "attributesname";
    private static final String TAG_CONTENT = "contentvalue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh_form_dynamic);

        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        new JSONParserAsyncTask().execute();
    }

    private class JSONParserAsyncTask extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //txtJSONResponse = (TextView) findViewById(R.id.txt_json_response);
            progressDialog = new ProgressDialog(FreshFormDynamic.this);
            progressDialog.setMessage("Getting Data");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = jsonParser.getJSONFromUrl(url);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.dismiss();

            if (jsonObject == null) {
                Toast.makeText(getApplicationContext(), "JSON response empty..!!", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                try {
                    JSONArray stuff = jsonObject.getJSONArray(TAG_STUFF);
                    Toast.makeText(getApplicationContext(), stuff.toString(), Toast.LENGTH_LONG).show();
                    JSONObject ob;
                    String str = "";
                    int orderNo = 1;
                    int count = 0;
                    String elementName = "";

                    ArrayList<String> attributes = new ArrayList<>();
                    ArrayList<String> contents = new ArrayList<>();
                    //Toast.makeText(getApplicationContext(), stuff.length(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Length of Array : " + stuff.length());
                    for (int i = 0; i < stuff.length(); i++) {
                        ob = stuff.getJSONObject(i);

                        if (ob.get("orderno").equals(orderNo+"")) {
                            if (count == 0) {
                                elementName = ob.getString(TAG_ELE);
                                count++;
                            }
                            attributes.add(ob.getString(TAG_ATTRNAME));
                            contents.add(ob.getString(TAG_CONTENT));
                        } else {
                            //Log.e(TAG, "Element Name : " + elementName);
                            //Log.e(TAG, "Attributes : " + attributes.toString());
                            //Log.e(TAG, "Contents : " + contents.toString());
                            formatDisplay(getApplicationContext(), elementName, attributes, contents, orderNo);
                            //linearLayout.addView(view);
                            count = 0;
                            attributes = new ArrayList<>();
                            contents = new ArrayList<>();
                            orderNo++;
                            i--;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private void formatDisplay(Context applicationContext, String elementName, ArrayList<String> attributes, ArrayList<String> contents, int orderNo) {

            Log.e(TAG, "Element Name : " + elementName);
            Log.e(TAG, "Attributes : " + attributes.toString());
            Log.e(TAG, "Contents : " + contents.toString());
            switch (elementName) {
                case "File" :
                    Log.e(TAG, "File to be displayed");
                    ImageView imageView = new ImageView(applicationContext);
                    for (int i = 0; i < attributes.size(); i++) {
                        String attribute = attributes.get(i);
                        String c = contents.get(i);
                        switch (attribute) {
                            case "name" :
                                imageView.setId(orderNo);
                                break;

                            case "placeholder" :
                                imageView.setImageResource(R.drawable.image1);
                                imageView.setLayoutParams(new LinearLayout.LayoutParams(50,50));
                                break;

                            case "required" :
                                break;

                            case "value" :
                                break;

                            case "multiple" :
                                break;
                        }
                    }
                    linearLayout.addView(imageView);
                    break;

                case "Textbox" :
                    Log.e(TAG, "Textbox to be displayed");
                    EditText editText = new EditText(applicationContext);
                    for (int i= 0; i < attributes.size(); i++) {
                        String attribute = attributes.get(i);
                        String c = contents.get(i);
                        switch (attribute) {
                            case "name" :
                                editText.setId(orderNo);
                                break;

                            case "placeholder" :
                                editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                Log.e(TAG, "Hint : " + c);
                                editText.setHintTextColor(Color.GRAY);
                                editText.setHint(c);
                                break;

                            case "color" :
                                editText.setTextColor(Color.BLACK);
                                break;

                            case "required" :

                                break;

                            case "size" :
                                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                                break;

                        }
                    }
                    linearLayout.addView(editText);
                    break;

                case "Label" :
                    Log.e(TAG, "Label to be displayed");
                    TextView textView = new TextView(applicationContext);
                    for (int i= 0; i < attributes.size(); i++) {
                        String attribute = attributes.get(i);
                        String c = contents.get(i);
                        switch (attribute) {
                            case "name" :
                                textView.setId(orderNo);
                                textView.setText(c);
                                break;

                            case "placeholder" :
                                break;

                            case "required" :
                                break;

                            case "size" :
                                textView.setTextColor(Color.BLACK);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                                break;

                            case "value" :
                                break;
                        }
                    }
                    linearLayout.addView(textView);
                    break;

                case "Radiobutton" :
                    Log.e(TAG, "Radiobutton to be displayed");
                    RadioGroup radioGroup = new RadioGroup(applicationContext);
                    radioGroup.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    radioGroup.setOrientation(LinearLayout.VERTICAL);
                    for (int i= 0; i < attributes.size(); i++) {
                        String attribute = attributes.get(i);
                        String c = contents.get(i);
                        switch (attribute) {
                            case "name" :
                                radioGroup.setId(orderNo);
                                break;

                            case "placeholder" :
                                break;

                            case "required" :
                                break;

                            case "size" :
                                break;

                            case "value" :
                                String[] values = c.split(",");
                                int countRadio = 1;
                                for (String v : values) {
                                    RadioButton radioButton = new RadioButton(applicationContext);
                                    radioButton.setId(countRadio++);
                                    radioButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    radioButton.setText(v);
                                    radioButton.setTextColor(Color.BLACK);
                                    radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                                    radioGroup.addView(radioButton);
                                }
                                break;
                        }
                    }
                    linearLayout.addView(radioGroup);
                    break;

                case "Button" :
                    Log.e(TAG, "Button to be displayed");
                    Button button = new Button(applicationContext);
                    for (int i= 0; i < attributes.size(); i++) {
                        String attribute = attributes.get(i);
                        String c = contents.get(i);
                        switch (attribute) {
                            case "name" :
                                button.setId(orderNo);
                                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                break;

                            case "placeholder" :
                                break;

                            case "required" :
                                break;

                            case "size" :
                                button.setTextColor(Color.BLACK);
                                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                                break;

                            case "default" :
                                break;

                            case "value" :
                                button.setText(c);
                                break;
                        }
                    }
                    linearLayout.addView(button);
                    break;
            }
        }
    }
}
