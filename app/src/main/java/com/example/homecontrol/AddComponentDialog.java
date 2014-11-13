package com.example.homecontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homecontrol.model.Component;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by James Hartman on 11/7/2014.
 */
public class AddComponentDialog extends DialogFragment implements Spinner.OnItemSelectedListener{

    public static final String EXTRA_ZONE_NAME = "HOMECONTROL.ZONENAME";
    public static final String EXTRA_COMP_TYPE = "HOMECONTROL.COMP_TYPE";
    public static final String EXTRA_COMP_IP = "HOMECONTROL.COMP_IP";

    Spinner spType;
    EditText etIP;
    TextView tvZone;

    private int compType = -1;
    private boolean validType = false;
    private String zone;

    ArrayAdapter<String> cAdapter;
    String[] typesArray;

    public AddComponentDialog newInstance(String zone){
        Bundle args = new Bundle();
        args.putString(EXTRA_ZONE_NAME, zone);

        AddComponentDialog dialog = new AddComponentDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public Dialog onCreateDialog(Bundle savedState){
        View v = getActivity().getLayoutInflater().inflate(
                R.layout.frag_add_component_dialog, null);

        // Get zone name that the component is to be added to.
        zone = getArguments().getString(EXTRA_ZONE_NAME);

        spType = (Spinner) v.findViewById(R.id.spComponentType);
        etIP = (EditText) v.findViewById(R.id.etComponentIP);
        tvZone = (TextView) v.findViewById(R.id.tvComponentZone);

        typesArray = getResources().getStringArray(R.array.component_types);
        tvZone.setText("Add component for \"" + zone + "\"");

        cAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_simple_text_view, typesArray);
        cAdapter.setDropDownViewResource(R.layout.spinner_simple_text_view);

        spType.setAdapter(cAdapter);
        spType.setOnItemSelectedListener(this);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Add Component")
                .setPositiveButton(R.string.ok, new AlertDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    public void sendResult(int resultCode){
        String ip = etIP.getText().toString();

        if (!validType){
            Toast.makeText(getActivity(), "Please select a valid component type.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validIP(ip)){
            Toast.makeText(getActivity(), "Please enter a valid IP address.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_ZONE_NAME, zone);
        i.putExtra(EXTRA_COMP_TYPE, compType);
        i.putExtra(EXTRA_COMP_IP, ip);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    public boolean validIP(String testip){
        // verify 3 periods
        int periods = 0;
        // used to determine position of period
        int place = 0;

        while (true){
            place = testip.indexOf(".");

            // if there are no more periods, exit the loop.
            if (place == -1) break;

            periods++;

            // test the numbers between periods to see if they
            // are indeed valid numbers
            String num = testip.substring(0, place);
            try {
                int numVal = Integer.parseInt(num);
                if (numVal > 255 || numVal < 0)
                    return false;
            } catch (NumberFormatException e) {
                return false;
            }
            testip = testip.substring(place + 1);
        }

        // check last number for validity
        try {
            Integer.parseInt(testip);
        } catch (NumberFormatException e) {
            return false;
        }
        // if we don't have 3 periods, it's not
        // a valid ip.
        if (periods != 3) return false;

        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String ctype = ((TextView) view.findViewById(R.id.tvComponentType)).getText().toString();
        if (ctype == getResources().getString(R.string.spinner_hint)){
            validType = false;
            return;
        }

        if (ctype.equals(getResources().getString(R.string.type_ac_120v_light)))
            compType = Component.TYPE_AC_120V_LIGHT;
        else if (ctype.equals(getResources().getString(R.string.type_dc_12v_led_color)))
            compType = Component.TYPE_DC_12V_LED_COLOR;
        else if (ctype.equals(getResources().getString(R.string.type_dc_12v_led_white)))
            compType = Component.TYPE_DC_12V_LED_WHITE;

        validType = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
