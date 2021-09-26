package com.example.bookacar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity2  extends Activity {

    public class GeocodingTask extends AsyncTask<String, Void, String> {

        TextView textViewRqs, textViewParsed;
        String rqs;

        HereResponse geoCodingRes;

        GeocodingTask(TextView vRqs, TextView vParsed) {
            textViewRqs = vRqs;
            textViewParsed = vParsed;
        }

        @Override
        protected String doInBackground(String... params) {

            final String rqs = "http://geocoder.cit.api.here.com/6.2/geocode.xml"
                    + "?searchtext=" + params[0] + "&gen=7"
                    + "&app_id=6uUIpmahXn7UP3LEdZVn"
                    + "&app_code=v-QZwq32F-Wtr9ZPypXQ2g";

            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    textViewRqs.setText(rqs);
                }});

            geoCodingRes = Parse(rqs);
            return geoCodingRes.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            textViewParsed.setText(result);

            if(!geoCodingRes.DisplayPosition_Latitude.equals("")
                    &&!geoCodingRes.DisplayPosition_Longitude.equals("")){
                double lat = Double.parseDouble(geoCodingRes.DisplayPosition_Latitude);
                double lon = Double.parseDouble(geoCodingRes.DisplayPosition_Longitude);
                updateHereMap(lat, lon);
            }
        }

        private HereResponse Parse(String urlString) {
            HereResponse hereResponse = new HereResponse();

            final String XmlTag_Response = "Response";
            final String XmlTag_MetaInfo = "MetaInfo";
            final String XmlTag_Timestamp = "Timestamp";
            final String XmlTag_View = "View";
            final String XmlTag_ViewId = "ViewId";
            final String XmlTag_Result = "Result";
            final String XmlTag_Location = "Location";

            final String XmlTag_DisplayPosition = "DisplayPosition";
            final String XmlTag_NavigationPosition = "NavigationPosition";
            final String XmlTag_TopLeft = "TopLeft";
            final String XmlTag_BottomRight = "BottomRight";

            final String XmlTag_Latitude = "Latitude";
            final String XmlTag_Longitude = "Longitude";

            final String XmlTag_Label = "Label";
            final String XmlTag_Country = "Country";
            final String XmlTag_State = "State";
            final String XmlTag_County = "County";
            final String XmlTag_City = "City";
            final String XmlTag_District = "District";
            final String XmlTag_Street = "Street";
            final String XmlTag_HouseNumber = "HouseNumber";
            final String XmlTag_PostalCode = "PostalCode";

            String xmlPara = "";
            String xmlGroupPara = "";

            XmlPullParserFactory factory;
            try {
                factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput((new URL(urlString)).openConnection()
                        .getInputStream(), "UTF_8");

                int eventType;
                do {
                    xpp.next();
                    eventType = xpp.getEventType();

                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            xmlPara = "";  //0;
                            xmlGroupPara = ""; //0;
                            break;

                        case XmlPullParser.END_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            String tag = xpp.getName();

                            xmlPara = tag;

                            if(tag.equals(XmlTag_DisplayPosition)
                                    || tag.equals(XmlTag_NavigationPosition)
                                    || tag.equals(XmlTag_TopLeft)
                                    || tag.equals(XmlTag_BottomRight)){
                                xmlGroupPara = tag;
                            }

                            break;

                        case XmlPullParser.END_TAG:
                            break;

                        case XmlPullParser.TEXT:

                            if(xmlPara.equals(XmlTag_Timestamp)){
                                hereResponse.Timestamp = xpp.getText();
                            }else if(xmlPara.equals(XmlTag_ViewId)){
                                hereResponse.ViewId = xpp.getText();
                            }else if(xmlPara.equals(XmlTag_Latitude)){
                                if(xmlGroupPara.equals(XmlTag_DisplayPosition)){
                                    hereResponse.DisplayPosition_Latitude = xpp.getText();
                                }else if(xmlGroupPara.equals(XmlTag_NavigationPosition)){
                                    hereResponse.NavigationPosition_Latitude = xpp.getText();
                                }else if(xmlGroupPara.equals(XmlTag_TopLeft)){
                                    hereResponse.TopLeft_Latitude = xpp.getText();
                                }else if(xmlGroupPara.equals(XmlTag_BottomRight)){
                                    hereResponse.BottomRight_Latitude = xpp.getText();
                                }
                            }else if(xmlPara.equals(XmlTag_Longitude)){
                                if(xmlGroupPara.equals(XmlTag_DisplayPosition)){
                                    hereResponse.DisplayPosition_Longitude = xpp.getText();
                                }else if(xmlGroupPara.equals(XmlTag_NavigationPosition)){
                                    hereResponse.NavigationPosition_Longitude = xpp.getText();
                                }else if(xmlGroupPara.equals(XmlTag_TopLeft)){
                                    hereResponse.TopLeft_Longitude = xpp.getText();
                                }else if(xmlGroupPara.equals(XmlTag_BottomRight)){
                                    hereResponse.BottomRight_Longitude = xpp.getText();
                                }
                            }else if(xmlPara.equals(XmlTag_Label)){
                                hereResponse.Label = xpp.getText();
                            }else if(xmlPara.equals(XmlTag_Country)){
                                hereResponse.Country = xpp.getText();
                            }else if(xmlPara.equals(XmlTag_State)){
                                hereResponse.State = xpp.getText();
                            }else if(xmlPara.equals(XmlTag_County)){
                                hereResponse.County = xpp.getText();
                            }else if(xmlPara.equals(XmlTag_City)){
                                hereResponse.City = xpp.getText();
                            }else if(xmlPara.equals(XmlTag_District)){
                                hereResponse.District = xpp.getText();
                            }else if(xmlPara.equals(XmlTag_Street)){
                                hereResponse.Street = xpp.getText();
                            }else if(xmlPara.equals(XmlTag_HouseNumber)){
                                hereResponse.HouseNumber = xpp.getText();
                            }else if(xmlPara.equals(XmlTag_PostalCode)){
                                hereResponse.PostalCode = xpp.getText();
                            }

                            break;
                    }

                } while (eventType != XmlPullParser.END_DOCUMENT);
            } catch (XmlPullParserException e) {
                hereResponse.err = (e.getMessage());
                e.printStackTrace();
            } catch (MalformedURLException e) {
                hereResponse.err = (e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                hereResponse.err = (e.getMessage());
                e.printStackTrace();
            }

            return hereResponse;
        }

    }

    public class HereResponse {

        String Timestamp = "";
        String ViewId = "";
        String DisplayPosition_Latitude = "";
        String DisplayPosition_Longitude = "";
        String NavigationPosition_Latitude = "";
        String NavigationPosition_Longitude = "";
        String TopLeft_Latitude = "";
        String TopLeft_Longitude = "";
        String BottomRight_Latitude = "";
        String BottomRight_Longitude = "";

        String Label = "";
        String Country = "";
        String State = "";
        String County = "";
        String City = "";
        String District = "";
        String Street = "";
        String HouseNumber = "";
        String PostalCode = "";

        String err = "";

        public String toString() {
            return "Timestamp: " + Timestamp + "\n"
                    + "ViewId: " + ViewId + "\n"
                    + "DisplayPosition_Latitude: " + DisplayPosition_Latitude + "\n"
                    + "DisplayPosition_Longitude: " + DisplayPosition_Longitude + "\n"
                    + "NavigationPosition_Latitude: " + NavigationPosition_Latitude + "\n"
                    + "NavigationPosition_Longitude: " + NavigationPosition_Longitude + "\n"
                    + "TopLeft_Latitude: " + TopLeft_Latitude + "\n"
                    + "TopLeft_Longitude: " + TopLeft_Longitude + "\n"
                    + "BottomRight_Latitude: " + BottomRight_Latitude + "\n"
                    + "BottomRight_Longitude: " + BottomRight_Longitude + "\n"

                    + "Label: " + Label + "\n"
                    + "Country: " + Country + "\n"
                    + "State: " + State + "\n"
                    + "County: " + County + "\n"
                    + "City: " + City + "\n"
                    + "District: " + District + "\n"
                    + "Street: " + Street + "\n"
                    + "HouseNumber: " + HouseNumber + "\n"
                    + "PostalCode: " + PostalCode + "\n"

                    + "err: " + err;
        }
    }

    public class LoadHereMapTask extends AsyncTask<URL, Void, Bitmap> {

        ImageView imageView;

        LoadHereMapTask(ImageView v){
            imageView = v;
        }

        @Override
        protected Bitmap doInBackground(URL... params) {
            Bitmap bm = null;
            URL urlMapImage = params[0];
            try {
                bm = BitmapFactory.decodeStream(urlMapImage.openConnection().getInputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

    }

    private void updateHereMap(double lat, double lon){
        URL urlTarget;

        textMapRqs.setText("");
        mapImage.setImageBitmap(null);

        try {
            String strTarget = genHereMapTileRequest(lat, lon);
            textMapRqs.setText(strTarget);
            urlTarget = new URL(strTarget);
            new LoadHereMapTask(mapImage).execute(urlTarget);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private String getMercatorProjection(double lat, double lon, int z){
        /*
         * reference:
         * http://developer.here.com/rest-apis/documentation/enterprise-map-tile/topics/key-concepts.html
         */

        double latRad = lat * Math.PI/180;
        double n = Math.pow(2, z);
        double xTile = n * ((lon + 180)/360);
        double yTile = n * (1-(Math.log(Math.tan(latRad) + 1/Math.cos(latRad))/Math.PI))/2;

        String strProjection = "/"+ String.valueOf(z)
                + "/" + String.valueOf((int)xTile)
                + "/" + String.valueOf((int)yTile);

        return strProjection;
    }

    private String genHereMapTileRequest(double lat, double lon){
        /*
         * reference:
         * https://developer.here.com/rest-apis/documentation/enterprise-map-tile/topics/resource-map-tile.html
         */

        String BaseURL = "http://1.base.maps.cit.api.here.com";
        String Path = "/maptile/2.1/";
        String Resource = "maptile";
        String Version = "/newest";
        String scheme = "/normal.day";
        String pixelCnt = "/512";

        String ApplicationId = "DemoAppId01082013GAL";  //for demo
        String ApplicationCode = "AJKnXv84fjrb0KIHawS0Tg"; //for demo
        String png8 = "/png8";

        //Always zoom = 12
        String strZoomColumnRow = getMercatorProjection(lat, lon, 12);

        String rqs = BaseURL + Path + Resource + Version + scheme + strZoomColumnRow
                + pixelCnt
                + png8
                + "?app_id=" + ApplicationId
                + "&app_code=" + ApplicationCode;

        return rqs;
    }

    EditText textAddressIn;
    Button buttonGet;
    TextView textRqs, textParsed;

    TextView textMapRqs;
    ImageView mapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textAddressIn = (EditText) findViewById(R.id.addressin);
        buttonGet = (Button) findViewById(R.id.get);
        textRqs = (TextView) findViewById(R.id.textrqs);
        textParsed = (TextView) findViewById(R.id.textparsed);

        textMapRqs = (TextView) findViewById(R.id.maprqs);
        mapImage = (ImageView) findViewById(R.id.mapimage);

        // for easy testing
        textAddressIn.setText("425 W Randolph Street in Chicago");

        buttonGet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String strIn = textAddressIn.getText().toString();
                if (!strIn.equals("")) {
                    String strHtml = strIn.replace(" ", "+");

                    getGetGeocode(strHtml);
                }
            }
        });
    }

    private void getGetGeocode(String addr) {
        new GeocodingTask(textRqs, textParsed).execute(addr);
    }

}