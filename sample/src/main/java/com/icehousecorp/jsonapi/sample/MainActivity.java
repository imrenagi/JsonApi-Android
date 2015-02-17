package com.icehousecorp.jsonapi.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.icehousecorp.jsonapi.JSONAPI;
import com.icehousecorp.jsonapi.sample.model.DataResponse;
import com.icehousecorp.jsonapi.sample.model.WishlistDataResponse;
import com.icehousecorp.jsonapi.sample.model.json3.Post;
import com.icehousecorp.jsonapi.sample.model.json3.Posts;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Object object = new JSONAPI().fromJson(readJson(), DataResponse.class);
            System.out.println("");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String readJson() throws IOException {
        InputStream stream = getAssets().open("json.txt");
        int size = stream.available();
        byte[] buffer = new byte[size];
        stream.read(buffer);
        stream.close();
        String text = new String(buffer);
        return text;
    }
}
