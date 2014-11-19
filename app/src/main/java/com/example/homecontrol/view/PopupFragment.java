package com.example.homecontrol.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.homecontrol.R;
import com.example.homecontrol.model.Module;

/**
 * Created by Ranganesh Kumar on 11/19/2014.
 */
public class PopupFragment extends DialogFragment {
    private static final String FRAGMENT_TAG = PopupFragment.class.getName();

    private static final String KEY_NAME = "bitmap";
    private static final String KEY_STATUS = "status";
    private static final String KEY_TYPE = "type";
    private Activity activity;

    public static void show(final FragmentManager fm, String name, int status, String type) {
        instance(name, status, type).show(fm, FRAGMENT_TAG);
    }

    private static PopupFragment instance(String name, int status, String type) {
        final PopupFragment fragment = new PopupFragment();

        final Bundle args = new Bundle();
        args.putString(KEY_NAME, name);
        args.putInt(KEY_STATUS, status);
        args.putString(KEY_TYPE, type);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final Bundle args = getArguments();

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final TextView name = (TextView) dialog.findViewById(R.id.tvModuleName);
        final TextView status = (TextView) dialog.findViewById(R.id.tvModuleStatus);
        final TextView type = (TextView) dialog.findViewById(R.id.tvModuleType);
        name.setText(args.getString(KEY_NAME));

        if (args.getInt(KEY_STATUS) == Module.MOD_ON) {
            status.setText(getActivity().getResources().getString(R.string.status_on));
        } else if (args.getInt(KEY_STATUS) == Module.MOD_OFF) {
            status.setText(getActivity().getResources().getString(R.string.status_off));
        } else {
            status.setText(getActivity().getResources().getString(R.string.status_not_available));
        }

        type.setText(args.getString(KEY_TYPE));

        final Button closeButton = (Button) dialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dismiss();
            }
        });

        return dialog;
    }

}
