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
import com.example.homecontrol.model.Zone;
import com.example.homecontrol.model.ZoneList;

/**
 * Created by Ranganesh Kumar on 11/19/2014.
 */
public class AddModuleDialog extends DialogFragment implements Spinner.OnItemSelectedListener {

    public static final String EXTRA_ZONE_NAME = "HOMECONTROL.ZONENAME";
    public static final String EXTRA_MOD_NAME = "HOMECONTROL.MOD_NAME";
    public static final String EXTRA_MOD_STATUS = "HOMECONTROL.MOD_STATUS";
    public static final String EXTRA_MOD_TYPE = "HOMECONTROL.MOD_TYPE";
    public static final String EXTRA_MODULE = "HOMECONTROL.MODULE";
    public static final String EXTRA_OLD_MODULE = "HOMECONTROL.OLD_MODULE";
    public static final String EXTRA_ZONE_LIST = "HOMECONTROL.ZONELIST";
    Spinner spStatus;
    EditText etName;
    EditText etType;
    TextView tvZone;
    ArrayAdapter<String> cAdapter;
    String[] typesArray;
    private ZoneList zoneList;
    private Module moduleToMod;
    private String dialogTitle = "";
    private int modStatus = -1;
    private boolean validType = false;
    private String zone;

    public AddModuleDialog newInstance(String zname) {
        Bundle args = new Bundle();
        args.putString(EXTRA_ZONE_NAME, zname);

        AddModuleDialog dialog = new AddModuleDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public AddModuleDialog newInstance(ZoneList zones, String zname, Module selModule) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ZONE_LIST, zones);
        args.putString(EXTRA_ZONE_NAME, zname);
        args.putSerializable(EXTRA_MODULE, selModule);

        AddModuleDialog dialog = new AddModuleDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public Dialog onCreateDialog(Bundle savedState) {
        View v = getActivity().getLayoutInflater().inflate(
                R.layout.frag_add_module_dialog, null);

        zoneList = (ZoneList) getArguments().getSerializable(EXTRA_ZONE_LIST);
        // Get zone name that the module is to be added to.
        zone = getArguments().getString(EXTRA_ZONE_NAME);
        moduleToMod = (Module) getArguments().getSerializable(EXTRA_MODULE);

        tvZone = (TextView) v.findViewById(R.id.tvModuleZone);
        etName = (EditText) v.findViewById(R.id.etModuleName);
        spStatus = (Spinner) v.findViewById(R.id.spModuleStatus);
        etType = (EditText) v.findViewById(R.id.etModuleType);

        typesArray = getResources().getStringArray(R.array.module_status);
        cAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_simple_text_view, typesArray);
        cAdapter.setDropDownViewResource(R.layout.spinner_simple_text_view);

        spStatus.setAdapter(cAdapter);
        spStatus.setOnItemSelectedListener(this);

        if (moduleToMod == null) {
            dialogTitle = getActivity().getResources().getString(R.string.modules_add_dialog_title);
            tvZone.setText("Add Module for \"" + zone + "\"");
        } else {
            dialogTitle = "Edit \"" + moduleToMod.getName() + "\"";
            tvZone.setText("Edit Module for \"" + zone + "\"");
            etName.setText(moduleToMod.getName());
            etType.setText(moduleToMod.getType());
            spStatus.setSelection(moduleToMod.getStatus());
        }
        etName.requestFocus();

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(dialogTitle)
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
        int reqCode = getTargetRequestCode();
        String modName = etName.getText().toString();
        String modType = etType.getText().toString();

        if (!validType) {
            Toast.makeText(getActivity(), "Please select a valid module status.", Toast.LENGTH_SHORT).show();
            return;
        }

        if ((modName.length() < 1) || (modName.equals(""))) {
            Toast.makeText(getActivity(), "You must enter a name.", Toast.LENGTH_SHORT).show();
            return;
        }

        if ((modType.length() < 1) || (modType.equals(""))) {
            Toast.makeText(getActivity(), "You must enter a type.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (typeOnList(modType) && (reqCode == FragZones.REQ_NEW_MOD)) {
                Toast.makeText(getActivity(), "That type is already in use.  Please enter another type.",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }

        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_ZONE_NAME, zone);
        i.putExtra(EXTRA_MOD_NAME, modName);
        i.putExtra(EXTRA_MOD_STATUS, modStatus);
        i.putExtra(EXTRA_MOD_TYPE, modType);
        if (reqCode == FragZones.REQ_MOD_MOD)
            i.putExtra(EXTRA_OLD_MODULE, moduleToMod);

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

    boolean typeOnList(String type) {
        int zcount = 0;
        for (Zone tempZone : zoneList) {
            if (tempZone.getName().equals(zone))
                break;
            zcount++;
        }

        int mcount = 0;
        for (Module tempModule : zoneList.get(zcount).getModules()) {
            if (tempModule.getType().equals(type))
                return true;
            mcount++;

        }
        return false;
    }
}
