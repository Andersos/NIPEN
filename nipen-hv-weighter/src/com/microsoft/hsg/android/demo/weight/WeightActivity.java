/*
 * Copyright 2011 Microsoft Corp.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.microsoft.hsg.android.demo.weight;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.microsoft.hsg.Request;
import com.microsoft.hsg.android.HealthVaultService;
import com.microsoft.hsg.android.PersonInfo;
import com.microsoft.hsg.android.Record;
import com.microsoft.hsg.android.ShellActivity;
import com.microsoft.hsg.android.XmlUtils;
import com.microsoft.hsg.request.RequestMarshaller;
import com.microsoft.hsg.request.SimpleRequestTemplate;

public class WeightActivity extends Activity {

    private HealthVaultService service;
    private Record selectedRecord;

    private final String jsonMessage =
            "{\"userId\":1,\"timestamp\":\"%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS\",\"value\":%2$s,\"unit\":\"kg\"}";

    private URL endPoint;
    private HttpURLConnection httpConnection;
    
    /**
     * Called when the activity is first created.
     * 
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight);
        service = HealthVaultService.getInstance();

        Button startAuth = (Button) findViewById(R.id.auth);
        /*startAuth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HealthVaultService service = HealthVaultService.getInstance();
                startActivity(ShellActivity.createAppAuthIntent(
                    WeightActivity.this, service.getAppId()));
            }
        });*/
        startAuth.setOnClickListener(new ButtonListener());
        
        Button newApp = (Button) findViewById(R.id.newapp);
        /*newApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                service.reset();
                Intent i = new Intent(WeightActivity.this, Welcome.class);
                startActivity(i);
                finish();
            }
        });*/
        
        Button putThing = (Button) findViewById(R.id.putThing);
        /*putThing.setOnClickListener(new View.OnClickListener() {
             public void onClick(View view) {
                EditText text = (EditText) findViewById(R.id.weightInput);
                PutWeight putAction = new PutWeight(text.getText().toString());
                putAction.execute();
             }
        });*/
        putThing.setOnClickListener(new ButtonListener());
        
        Spinner s = (Spinner) findViewById(R.id.spinner);
        s.setOnItemSelectedListener( new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                View view, int pos, long id) {
                selectedRecord = (Record)parent.getItemAtPosition(pos);
                InitializeWeights();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Button fetchButton = (Button) findViewById(R.id.fetch);
        fetchButton.setOnClickListener(new ButtonListener());

        Button clearButton = (Button) findViewById(R.id.clear);
        clearButton.setOnClickListener(new ButtonListener());

        try {
            endPoint = new URL("http://mhealthdemo03.cloudapp.net/nipen/api/human/weight");
        } catch (Exception e) {
            // ignore
        }
    }
    
    @Override
    protected void onResume()
    {
        InitializeControls();
        super.onResume();
    }
    
    private void InitializeControls() {
        InitializeRecords();
    }
    
    private void InitializeWeights() {
        new InitializeWeights().execute();
    }
    
    private void InitializeRecords() {
        new InitializeRecords().execute();
    }
    
    private class InitializeRecords extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private List<Record> records = new ArrayList<Record>();
        private ProgressDialog progressDialog;
        
        public InitializeRecords() {
            progressDialog = ProgressDialog.show(
                    WeightActivity.this,
                    "",
                    "Please wait...", 
                    true);
        }
        
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                if (service.getConnectionStatus() == HealthVaultService.ConnectionStatus.Connected) {
                    List<PersonInfo> personInfos = service.getPersonInfoList();
                    for (PersonInfo personInfo : personInfos) {
                        for (Record record : personInfo.getRecords()) {
                            records.add(record);
                        }
                    }
                    
                    if (records.size() > 0) {
                        selectedRecord = records.get(0);                    }
                }
            } catch(Exception e) {
                exception = e;
            }
            
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            
            if (exception == null) {
                Spinner s = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<Record> adapter = new ArrayAdapter<Record>(
                    WeightActivity.this, 
                    android.R.layout.simple_spinner_item,
                    records);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
            } else {
                Toast.makeText(
                        WeightActivity.this, 
                        "An error occurred.  " + exception.getMessage(), 
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    
    /**
     * The Class PutWeight.
     */
    private class PutWeight extends AsyncTask<Void, Void, Void> {
        private String weight;
        private Exception exception;
        ProgressDialog progressDialog;
        
        /**
         * Instantiates a new put weight.
         * 
         * @param weight the weight
         */
        public PutWeight(String weight) {
            this.weight = weight;
            
            progressDialog = ProgressDialog.show(
                    WeightActivity.this,
                    "",
                    "Please wait for put...", 
                    true);
        }
        
        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        protected Void doInBackground(Void... v) {
            try {
                EditText text = (EditText) findViewById(R.id.weightInput);
                putWeight(weight);
                sendWeight(weight);
            } catch(Exception e) {
                exception = e;
            }
            
            return null;
         }
                   
        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void v) {
            progressDialog.dismiss();

            if (exception == null) {
                InitializeWeights();
            } else {
                Toast.makeText(
                    WeightActivity.this, 
                    "An error occurred.  " + exception.getMessage(), 
                    Toast.LENGTH_LONG).show();
            }    
        }    
    }
    
    private class InitializeWeights extends AsyncTask<Void, Void, Void> {
        private Exception exception;
        private List<String> weights = new ArrayList<String>();
        private ProgressDialog progressDialog;
        
        public InitializeWeights() {
            progressDialog = ProgressDialog.show(
                WeightActivity.this,
                "",
                "Loading data...", 
                true);
        }
        
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                if (service.getConnectionStatus() == HealthVaultService.ConnectionStatus.Connected) {
                    if (selectedRecord != null) {
                        weights = getWeights();
                    }
                }
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            
            if (exception == null) {
                ListView lv = (ListView)findViewById(R.id.weightList);
                lv.setAdapter(new ArrayAdapter<String>(
                    WeightActivity.this,
                    android.R.layout.simple_list_item_1,
                    weights));
                //lv.invalidate();
            } else {
                Toast.makeText(
                        WeightActivity.this, 
                        "An error occurred.  " + exception.getMessage(), 
                        Toast.LENGTH_LONG).show();
            }
        }
    }
                                                                       
    private void putWeight(String value)
    {
        Weight weight = new Weight(Double.parseDouble(value));
        PersonInfo personInfo = service.getPersonInfoList().get(0);
        SimpleRequestTemplate template = new SimpleRequestTemplate(
                service.getConnection(),
                selectedRecord.getPersonId(),
                selectedRecord.getId());
        
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append("<info><thing><type-id>");
        infoBuilder.append(Weight.TYPE);
        infoBuilder.append("</type-id><data-xml>");
        infoBuilder.append(weight.toXml());
        infoBuilder.append("<common/></data-xml></thing></info>");
        
        Request request = new Request();
        request.setMethodName("PutThings");
        request.setInfo(infoBuilder.toString());
        template.makeRequest(request);
    }

    private void sendWeight(String weight) {
        InputStream inputStream;
        OutputStream outputStream;

        //String response = new String();
        StringBuilder buf = new StringBuilder();

        Formatter formatter = new Formatter(buf);
        formatter.format(jsonMessage, Calendar.getInstance(), weight);

        System.out.println("[WEIGHT] " + buf.toString());

        try {
            httpConnection = (HttpURLConnection)endPoint.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.addRequestProperty("Content-Type", "application/json");
            httpConnection.connect();

            outputStream = httpConnection.getOutputStream();
            outputStream.write(buf.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            // we must read anyway !
            inputStream = httpConnection.getInputStream();
            inputStream.close();

        } catch (Exception e) {
            System.out.println("[WEIGHT] EXCEPTION: " + e.getMessage() + e.toString());
        } finally {
            httpConnection.disconnect();
        }
    }
    
    @SuppressWarnings("unchecked")
    private List<String> getWeights()
    {
        Record record = selectedRecord;
        
        SimpleRequestTemplate template = new SimpleRequestTemplate(
                service.getConnection(),
                record.getPersonId(),
                record.getId());
        
        String info = "<info><group max=\"25\"><filter><type-id>" +
            Weight.TYPE +
            "</type-id></filter><format><section>core</section>" +
            "<xml/></format></group></info>";
        
        Request request = new Request();
        request.setMethodName("GetThings");
        request.setInfo(info);
        
        return (List<String>)template.makeRequest(request, new RequestMarshaller() {
            public Object marshal(InputStream istream) throws Exception {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser= factory.newPullParser();    
                parser.setInput(istream, "utf8");
                
                XmlUtils.nextStartTag(parser, "response");
                XmlUtils.nextStartTag(parser, "status");
                XmlUtils.skipSubTree(parser);
                XmlUtils.nextStartTag(parser, "wc:info");
                XmlUtils.nextStartTag(parser, "group");
                
                List<String> weights = new ArrayList<String>();
                
                while (parser.nextTag() == XmlPullParser.START_TAG) {
                  if ("thing".equals(parser.getName())) {
                      int thingDepth = parser.getDepth();
                      while (parser.nextTag() == XmlPullParser.START_TAG
                              && parser.getDepth() > thingDepth) {
                          if ("data-xml".equals(parser.getName()))
                          {
                              int dataXmlDepth = parser.getDepth();
                              while (parser.nextTag() == XmlPullParser.START_TAG
                                      && parser.getDepth() > dataXmlDepth) {
                                  if ("weight".equals(parser.getName())) {
                                      weights.add(
                                          String.valueOf(Weight.createFromXml(parser).getValue()));
                                  } 
                                  else {
                                      XmlUtils.skipSubTree(parser);
                                  }
                              }
                          }
                          else
                          {
                              XmlUtils.skipSubTree(parser);
                          }
                      }
                  } else {
                     XmlUtils.skipSubTree(parser);
                  }
                }
                
                return weights;
            }
        });
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.auth: {
                    HealthVaultService service = HealthVaultService.getInstance();
                    startActivity(ShellActivity.createAppAuthIntent(
                            WeightActivity.this, service.getAppId()));
                } break;
                case R.id.newapp: {
                    service.reset();
                    Intent i = new Intent(WeightActivity.this, Welcome.class);
                    startActivity(i);
                    finish();
                } break;
                case R.id.putThing: {
                    EditText text = (EditText) findViewById(R.id.weightInput);
                    PutWeight putAction = new PutWeight(text.getText().toString());
                    putAction.execute();
                } break;
                case R.id.fetch: {
                    InitializeWeights();
                } break;
                case R.id.clear: {
                    ListView list = (ListView) findViewById(R.id.weightList);
                    // will probably crash android 1.6
                    list.setAdapter(null);
                } break;
            }
        }
    }
}
