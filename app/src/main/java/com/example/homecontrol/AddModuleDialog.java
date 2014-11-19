package com.example.homecontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homecontrol.model.Module;

/**
 * Created by Ranganesh Kumar on 11/19/2014.
 */
public class AddModuleDialog extends DialogFragment implements Spinner.OnItemSelectedListener {

    public static final String EXTRA_ZONE_NAME = "HOMECONTROL.ZONENAME";
    public static final String EXTRA_MOD_NAME = "HOMECONTROL.MOD_NAME";
    public static final String EXTRA_MOD_STATUS = "HOMECONTROL.MOD_STATUS";
    public static final String EXTRA_MOD_TYPE = "HOMECONTROL.MOD_TYPE";

    Spinner spStatus;
    EditText etName;
    EditText etType;
    TextView tvZone;
    ArrayAdapter<String> cAdapter;
    String[] typesArray;
    private int modStatus = -1;
    private boolean validType = false;
    private String zone;

    public AddModuleDialog newInstance(String zone) {
        Bundle args = new Bundle();
        args.putString(EXTRA_ZONE_NAME, zone);

        AddModuleDialog dialog = new AddModuleDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public Dialog onCreateDialog(Bundle savedState) {
        View v = getActivity().getLayoutInflater().inflate(
                R.layout.frag_add_module_dialog, null);

        // Get zone name that the component is to be added to.
        zone = getArguments().getString(EXTRA_ZONE_NAME);

        spStatus = (Spinner) v.findViewById(R.id.spModuleStatus);
        etName = (EditText) v.findViewById(R.id.etModuleName);
        etType = (EditText) v.findViewById(R.id.etModuleType);
        tvZone = (TextView) v.findViewById(R.id.tvModuleZone);

        typesArray = getResources().getStringArray(R.array.module_status);
        tvZone.setText("Add Module for \"" + zone + "\"");

        cAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_simple_text_view, typesArray);
        cAdapter.setDropDownViewResource(R.layout.spinner_simple_text_view);

        spStatus.setAdapter(cAdapter);
        spStatus.setOnItemSelectedListener(this);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Add Module")
                .setPositiveButton(R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    public void sendResult(int resultCode) {
        String modName = etName.getText().toString();
        String modType = etType.getText().toString();

        if (!validType) {
            Toast.makeText(getActivity(), "Please select a valid module status.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_ZONE_NAME, zone);
        i.putExtra(EXTRA_MOD_NAME, modName);
        i.putExtra(EXTRA_MOD_STATUS, modStatus);
        i.putExtra(EXTRA_MOD_TYPE, modType);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String mtype = ((TextView) view.findViewById(R.id.tvComponentType)).getText().toString();
        if (mtype == getResources().getString(R.string.spinner_hint)) {
            validType = false;
            return;
        }

        if (mtype.equals(getResources().getString(R.string.status_on)))
            modStatus = Module.MOD_ON;
        else if (mtype.equals(getResources().getString(R.string.status_off)))
            modStatus = Module.MOD_OFF;
        else if (mtype.equals(getResources().getString(R.string.status_not_available)))
            modStatus = Module.MOD_NA;

        validType = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
